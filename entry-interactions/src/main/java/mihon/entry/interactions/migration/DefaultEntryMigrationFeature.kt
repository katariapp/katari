package mihon.entry.interactions

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.withContext
import mihon.entry.interactions.host.EntryMigrationCustomCoverHost
import mihon.entry.interactions.host.EntryMigrationExecutionHost
import mihon.entry.interactions.host.EntryMigrationExecutionInspectionResult
import mihon.entry.interactions.host.EntryMigrationHostInspectionResult
import mihon.entry.interactions.host.EntryMigrationHostOperation
import mihon.entry.interactions.host.EntryMigrationHostReplayResult
import mihon.entry.interactions.host.EntryMigrationHostTransition
import mihon.entry.interactions.host.EntryMigrationHostTransitionResult
import mihon.entry.interactions.host.EntryMigrationPreparationHost
import mihon.entry.interactions.host.EntryMigrationTargetSynchronizationResult
import mihon.feature.graph.FeatureGraphEvaluation
import tachiyomi.domain.entry.model.Entry

internal class DefaultEntryMigrationFeature(
    evaluation: FeatureGraphEvaluation,
    private val preparationHost: EntryMigrationPreparationHost,
    private val executionHost: EntryMigrationExecutionHost,
    private val mergeMigration: EntryMergeMigrationFeature,
    private val progress: EntryProgressFeature,
    private val playbackPreferences: EntryPlaybackPreferencesFeature,
    private val viewerSettings: EntryViewerSettingsFeature,
    private val downloads: EntryDownloadMaintenanceFeature,
    private val customCover: EntryMigrationCustomCoverHost,
    private val consequences: EntryMigrationConsequenceDelivery,
    private val consequenceCodec: EntryMigrationConsequenceCodec = EntryMigrationConsequenceCodec(),
    private val clockMillis: () -> Long = System::currentTimeMillis,
) : EntryMigrationFeature {
    private val migrationTypes = evaluation.applicableProviderTypes<EntryMigrationProvider>(
        feature = ENTRY_MIGRATION_FEATURE_ID,
        integration = ENTRY_MIGRATION_BASE_INTEGRATION_ID,
        consequence = EntryMigrationBaseConsequence.AVAILABILITY.id,
    )
    private val consumptionTypes = evaluation.applicableProviderTypes<EntryConsumptionProcessor>(
        feature = ENTRY_MIGRATION_FEATURE_ID,
        integration = ENTRY_MIGRATION_CONSUMPTION_INTEGRATION_ID,
        consequence = EntryMigrationConsumptionConsequence.TRANSFER.id,
    )
    private val bookmarkTypes = evaluation.applicableProviderTypes<EntryBookmarkProcessor>(
        feature = ENTRY_MIGRATION_FEATURE_ID,
        integration = ENTRY_MIGRATION_BOOKMARK_INTEGRATION_ID,
        consequence = EntryMigrationBookmarkConsequence.TRANSFER.id,
    )

    override fun availability(entry: Entry): EntryMigrationAvailability {
        val rejection = entry.sourceRejection()
        return if (rejection == null) {
            EntryMigrationAvailability.Available
        } else {
            EntryMigrationAvailability.Unavailable(rejection)
        }
    }

    override fun prepareSelection(entries: List<Entry>): EntryMigrationSelectionResult {
        if (entries.isEmpty()) return selectionRejected(EntryMigrationRejection.EMPTY_SELECTION)
        if (entries.map(Entry::profileId).distinct().size != 1) {
            return selectionRejected(EntryMigrationRejection.MIXED_SELECTION_PROFILES)
        }
        entries.firstNotNullOfOrNull { entry -> entry.sourceRejection() }
            ?.let { return selectionRejected(it) }
        return EntryMigrationSelectionResult.Ready(
            entries.map { entry -> EntryMigrationSubject(entry.profileId, entry.id) },
        )
    }

    override suspend fun prepare(intent: EntryMigrationPrepareIntent): EntryMigrationPreparationResult {
        validatePair(intent.source, intent.target)?.let { return preparationRejected(it) }
        return try {
            when (
                val inspection = preparationHost.profile(intent.source.profileId)
                    .inspectPair(intent.source.id, intent.target.id)
            ) {
                is EntryMigrationHostInspectionResult.Ready -> prepare(inspection, intent)
                EntryMigrationHostInspectionResult.SourceMissing -> {
                    preparationRejected(EntryMigrationRejection.UNPERSISTED_ENTRY)
                }
                EntryMigrationHostInspectionResult.TargetMissing -> {
                    preparationRejected(EntryMigrationRejection.UNPERSISTED_ENTRY)
                }
                is EntryMigrationHostInspectionResult.OperationalFailure -> {
                    EntryMigrationPreparationResult.OperationalFailure(inspection.retryable)
                }
            }
        } catch (error: CancellationException) {
            throw error
        } catch (_: Exception) {
            EntryMigrationPreparationResult.OperationalFailure(retryable = true)
        }
    }

    override suspend fun execute(intent: EntryMigrationExecuteIntent): EntryMigrationExecutionResult {
        val reference = intent.reference as? FeatureEntryMigrationReference
            ?: return executionRejected(EntryMigrationRejection.UNRECOGNIZED_REFERENCE)
        if (!reference.availableOptions.containsAll(intent.selectedOptions)) {
            return executionRejected(EntryMigrationRejection.INVALID_OPTIONS)
        }
        return try {
            execute(reference, intent)
        } catch (error: CancellationException) {
            throw error
        } catch (_: Exception) {
            EntryMigrationExecutionResult.OperationalFailure(retryable = true)
        }
    }

    private suspend fun prepare(
        inspection: EntryMigrationHostInspectionResult.Ready,
        intent: EntryMigrationPrepareIntent,
    ): EntryMigrationPreparationResult {
        if (!inspection.source.sameMigrationIdentity(intent.source)) {
            return preparationRejected(EntryMigrationRejection.ENTRY_IDENTITY_CHANGED)
        }
        if (!inspection.target.sameMigrationIdentity(intent.target)) {
            return preparationRejected(EntryMigrationRejection.ENTRY_IDENTITY_CHANGED)
        }
        validatePair(inspection.source, inspection.target)?.let { return preparationRejected(it) }
        val options = buildSet {
            if (inspection.source.type in consumptionTypes || inspection.source.type in bookmarkTypes) {
                add(EntryMigrationOption.CHILD_STATE)
            }
            if (inspection.sourceCategoryIds.isNotEmpty()) add(EntryMigrationOption.CATEGORIES)
            if (inspection.source.notes.isNotBlank()) add(EntryMigrationOption.NOTES)
            if (inspection.sourceHasCustomCover) add(EntryMigrationOption.CUSTOM_COVER)
            if (downloads.inspectEntry(inspection.source) == EntryDownloadMaintenanceInspection.HasDownloads) {
                add(EntryMigrationOption.REMOVE_SOURCE_DOWNLOADS)
            }
        }
        val reference = FeatureEntryMigrationReference(
            sessionId = newEntryMigrationSessionId(),
            source = inspection.source,
            target = inspection.target,
            availableOptions = options,
        )
        return EntryMigrationPreparationResult.Ready(
            reference = reference,
            source = inspection.source.subject(),
            target = inspection.target.subject(),
            availableOptions = options,
        )
    }

    private suspend fun execute(
        reference: FeatureEntryMigrationReference,
        intent: EntryMigrationExecuteIntent,
    ): EntryMigrationExecutionResult {
        val profileId = reference.source.profileId
        val profile = executionHost.profile(profileId)
        val fingerprint = intent.fingerprint()
        when (
            val replay = profile.replay(
                EntryMigrationHostOperation(
                    operationId = reference.sessionId,
                    intentFingerprint = fingerprint,
                    sourceEntryId = reference.source.id,
                    targetEntryId = reference.target.id,
                    mode = intent.mode,
                ),
            )
        ) {
            is EntryMigrationHostReplayResult.Applied -> {
                val followUp = if (replay.hasPendingConsequences) {
                    consequences.deliverOperation(reference.sessionId)
                } else {
                    EntryMigrationFollowUp.COMPLETE
                }
                return applied(reference, intent.mode, followUp)
            }
            EntryMigrationHostReplayResult.Conflict -> return EntryMigrationExecutionResult.Conflict
            EntryMigrationHostReplayResult.NotApplied -> Unit
            is EntryMigrationHostReplayResult.OperationalFailure -> {
                return EntryMigrationExecutionResult.OperationalFailure(replay.retryable)
            }
        }
        when (
            val inspection = preparationHost.profile(profileId)
                .inspectPair(reference.source.id, reference.target.id)
        ) {
            is EntryMigrationHostInspectionResult.Ready -> {
                if (!inspection.source.sameMigrationAuthorization(reference.source) ||
                    !inspection.target.sameMigrationAuthorization(reference.target)
                ) {
                    return EntryMigrationExecutionResult.Conflict
                }
            }
            EntryMigrationHostInspectionResult.SourceMissing,
            EntryMigrationHostInspectionResult.TargetMissing,
            -> return EntryMigrationExecutionResult.Conflict
            is EntryMigrationHostInspectionResult.OperationalFailure -> {
                return EntryMigrationExecutionResult.OperationalFailure(inspection.retryable)
            }
        }

        when (val sync = profile.synchronizeTarget(reference.target.id)) {
            EntryMigrationTargetSynchronizationResult.Synchronized -> Unit
            EntryMigrationTargetSynchronizationResult.TargetMissing -> return EntryMigrationExecutionResult.Conflict
            is EntryMigrationTargetSynchronizationResult.OperationalFailure -> {
                return EntryMigrationExecutionResult.OperationalFailure(sync.retryable)
            }
        }
        val inspected = when (
            val result = profile.inspectExecution(reference.source.id, reference.target.id)
        ) {
            is EntryMigrationExecutionInspectionResult.Ready -> result
            EntryMigrationExecutionInspectionResult.SourceMissing,
            EntryMigrationExecutionInspectionResult.TargetMissing,
            -> return EntryMigrationExecutionResult.Conflict
            is EntryMigrationExecutionInspectionResult.OperationalFailure -> {
                return EntryMigrationExecutionResult.OperationalFailure(result.retryable)
            }
        }
        if (!inspected.source.sameMigrationAuthorization(reference.source) ||
            !inspected.target.sameMigrationAuthorization(reference.target)
        ) {
            return EntryMigrationExecutionResult.Conflict
        }

        val transferChildren = EntryMigrationOption.CHILD_STATE in intent.selectedOptions
        val childUpdates = if (transferChildren) {
            prepareMigrationChildUpdates(
                sourceChildren = inspected.sourceChildren,
                targetChildren = inspected.targetChildren,
                transferConsumption = inspected.source.type in consumptionTypes,
                transferBookmarks = inspected.source.type in bookmarkTypes,
            )
        } else {
            emptyList()
        }
        val replace = intent.mode == EntryMigrationMode.REPLACE
        val progressMappings = if (transferChildren) {
            prepareMigrationProgressMappings(inspected.sourceChildren, inspected.targetChildren)
        } else {
            emptyList()
        }
        var stagedCover: mihon.entry.interactions.host.EntryMigrationCustomCoverPayload? = null
        val consequenceRequests = buildList {
            when (val prepared = progress.prepareMigration(inspected.source, inspected.target, progressMappings)) {
                is EntryProgressMigrationPreparation.Prepared -> add(
                    consequenceRequest(
                        EntryMigrationConsequenceArtifact.PROGRESS,
                        consequenceCodec.encode(prepared.payload),
                    ),
                )
                is EntryProgressMigrationPreparation.Inapplicable -> Unit
                is EntryProgressMigrationPreparation.IncompatibleTypes -> return EntryMigrationExecutionResult.Conflict
            }
            when (val prepared = playbackPreferences.prepareMigration(inspected.source, inspected.target)) {
                is EntryPlaybackPreferencesMigrationPreparation.Prepared -> add(
                    consequenceRequest(
                        EntryMigrationConsequenceArtifact.PLAYBACK_PREFERENCES,
                        consequenceCodec.encode(prepared.payload),
                    ),
                )
                EntryPlaybackPreferencesMigrationPreparation.NoPreferences,
                is EntryPlaybackPreferencesMigrationPreparation.Inapplicable,
                -> Unit
                is EntryPlaybackPreferencesMigrationPreparation.TypeMismatch -> {
                    return EntryMigrationExecutionResult.Conflict
                }
            }
            when (val prepared = viewerSettings.prepareMigration(inspected.source, inspected.target)) {
                is EntryViewerSettingsMigrationPreparation.Prepared -> add(
                    consequenceRequest(
                        EntryMigrationConsequenceArtifact.VIEWER_SETTINGS,
                        consequenceCodec.encode(prepared.payload),
                    ),
                )
                is EntryViewerSettingsMigrationPreparation.Inapplicable -> Unit
                is EntryViewerSettingsMigrationPreparation.TypeMismatch -> return EntryMigrationExecutionResult.Conflict
            }
            if (EntryMigrationOption.REMOVE_SOURCE_DOWNLOADS in intent.selectedOptions) {
                when (val prepared = downloads.prepareRemoval(inspected.source)) {
                    is EntryDownloadRemovalPreparation.Prepared -> add(
                        consequenceRequest(
                            EntryMigrationConsequenceArtifact.DOWNLOAD_REMOVAL,
                            consequenceCodec.encode(prepared.plan),
                        ),
                    )
                    EntryDownloadRemovalPreparation.NothingToRemove,
                    is EntryDownloadRemovalPreparation.Inapplicable,
                    -> Unit
                }
            }
            if (EntryMigrationOption.CUSTOM_COVER in intent.selectedOptions) {
                customCover.stage(reference.sessionId, inspected.source, inspected.target)?.let { payload ->
                    stagedCover = payload
                    add(
                        consequenceRequest(
                            EntryMigrationConsequenceArtifact.CUSTOM_COVER,
                            consequenceCodec.encode(payload),
                        ),
                    )
                }
            }
        }
        val targetUpdate = inspected.target.copy(
            favorite = true,
            chapterFlags = inspected.source.chapterFlags,
            dateAdded = if (replace) inspected.source.dateAdded else clockMillis(),
            notes = if (EntryMigrationOption.NOTES in intent.selectedOptions) {
                inspected.source.notes
            } else {
                inspected.target.notes
            },
        )
        val transition = EntryMigrationHostTransition(
            operationId = reference.sessionId,
            intentFingerprint = fingerprint,
            profileId = profileId,
            mode = intent.mode,
            expectedSource = inspected.source,
            expectedTarget = inspected.target,
            expectedSourceCategoryIds = inspected.sourceCategoryIds
                .takeIf { EntryMigrationOption.CATEGORIES in intent.selectedOptions },
            expectedTargetChildren = inspected.targetChildren.takeIf { transferChildren },
            sourceUpdate = inspected.source.copy(favorite = false, dateAdded = 0L).takeIf { replace },
            targetUpdate = targetUpdate,
            targetCategoryIds = inspected.sourceCategoryIds
                .takeIf { EntryMigrationOption.CATEGORIES in intent.selectedOptions },
            childUpdates = childUpdates,
            expectedSourceTracks = inspected.sourceTracks,
            preparedTracks = inspected.preparedTracks,
            consequenceRequests = consequenceRequests,
        )
        val mergeReplacement = if (replace) {
            suspend {
                mergeMigration.participateInReplacementTransaction(
                    EntryMergeMigrationReplacementIntent(inspected.source, inspected.target),
                )
            }
        } else {
            null
        }
        val result = try {
            profile.applyTransition(transition, mergeReplacement)
        } catch (error: CancellationException) {
            stagedCover?.let { payload ->
                withContext(NonCancellable) { runCatching { customCover.discard(payload) } }
            }
            throw error
        }
        if (result !is EntryMigrationHostTransitionResult.Applied) {
            stagedCover?.let { payload -> runCatching { customCover.discard(payload) } }
        }
        return when (result) {
            is EntryMigrationHostTransitionResult.Applied -> {
                val followUp = if (result.hasPendingConsequences) {
                    consequences.deliverOperation(reference.sessionId)
                } else {
                    EntryMigrationFollowUp.COMPLETE
                }
                applied(reference, intent.mode, followUp)
            }
            EntryMigrationHostTransitionResult.Conflict -> EntryMigrationExecutionResult.Conflict
            is EntryMigrationHostTransitionResult.OperationalFailure -> {
                EntryMigrationExecutionResult.OperationalFailure(result.retryable)
            }
        }
    }

    private fun applied(
        reference: FeatureEntryMigrationReference,
        mode: EntryMigrationMode,
        followUp: EntryMigrationFollowUp,
    ): EntryMigrationExecutionResult.Applied {
        return EntryMigrationExecutionResult.Applied(
            EntryMigrationOutcome(
                source = reference.source.subject(),
                target = reference.target.subject(),
                mode = mode,
                followUp = followUp,
            ),
        )
    }

    private fun validatePair(source: Entry, target: Entry): EntryMigrationRejection? {
        source.sourceRejection()?.let { return it }
        if (target.id <= 0L || target.profileId <= 0L) return EntryMigrationRejection.UNPERSISTED_ENTRY
        if (source.profileId != target.profileId) return EntryMigrationRejection.SOURCE_TARGET_PROFILE_MISMATCH
        if (source.type != target.type) return EntryMigrationRejection.SOURCE_TARGET_TYPE_MISMATCH
        if (target.type !in migrationTypes) return EntryMigrationRejection.UNSUPPORTED_TARGET_TYPE
        if (source.id == target.id) return EntryMigrationRejection.SAME_ENTRY
        return null
    }

    private fun Entry.sourceRejection(): EntryMigrationRejection? {
        if (id <= 0L || profileId <= 0L) return EntryMigrationRejection.UNPERSISTED_ENTRY
        if (!favorite) return EntryMigrationRejection.SOURCE_NOT_IN_LIBRARY
        if (type !in migrationTypes) return EntryMigrationRejection.UNSUPPORTED_SOURCE_TYPE
        return null
    }

    private fun Entry.subject() = EntryMigrationSubject(profileId, id)

    private fun EntryMigrationExecuteIntent.fingerprint(): String {
        return buildString {
            append(mode.name)
            append(':')
            append(selectedOptions.map(EntryMigrationOption::name).sorted().joinToString(","))
        }
    }

    private fun selectionRejected(reason: EntryMigrationRejection) = EntryMigrationSelectionResult.Rejected(reason)

    private fun preparationRejected(reason: EntryMigrationRejection) = EntryMigrationPreparationResult.Rejected(reason)

    private fun executionRejected(reason: EntryMigrationRejection) = EntryMigrationExecutionResult.Rejected(reason)

    private fun consequenceRequest(artifactId: String, payload: String) =
        mihon.entry.interactions.host.EntryMigrationConsequenceRequest(artifactId, payload)
}
