package mihon.entry.interactions

import eu.kanade.tachiyomi.source.entry.EntryType
import mihon.feature.graph.CapabilityExpression
import mihon.feature.graph.ContributionOwner
import mihon.feature.graph.FeatureArtifactId
import mihon.feature.graph.FeatureContribution
import mihon.feature.graph.FeatureGraphContributionSink
import mihon.feature.graph.FeatureGraphContributor
import mihon.feature.graph.FeatureGraphEvaluation
import mihon.feature.graph.FeatureId
import mihon.feature.graph.FeatureIntegration
import mihon.feature.graph.FeatureIntegrationId
import mihon.feature.graph.SharedFeatureConsequence
import mihon.feature.graph.allOf
import tachiyomi.domain.entry.model.Entry
import tachiyomi.domain.entry.model.EntryChapter
import tachiyomi.domain.entry.service.sortedForReading

private val ENTRY_DOWNLOAD_ACTION_FEATURE_ID = FeatureId("entry.download.actions")
private val ENTRY_DOWNLOAD_ACTION_FEATURE_OWNER = ContributionOwner("entry-download-actions")

private val ENTRY_DOWNLOAD_INDIVIDUAL_INTEGRATION_ID =
    FeatureIntegrationId("entry.download.actions.individual")
private val ENTRY_DOWNLOAD_BULK_INTEGRATION_ID =
    FeatureIntegrationId("entry.download.actions.bulk")
private val ENTRY_DOWNLOAD_BOOKMARKED_BULK_INTEGRATION_ID =
    FeatureIntegrationId("entry.download.actions.bulk.bookmarked")

private val ENTRY_DOWNLOAD_INDIVIDUAL_CONSEQUENCE_ID =
    FeatureArtifactId("entry.download.actions.individual.dispatch")
private val ENTRY_DOWNLOAD_BULK_CONSEQUENCE_ID =
    FeatureArtifactId("entry.download.actions.bulk.resolve")
private val ENTRY_DOWNLOAD_BOOKMARKED_BULK_CONSEQUENCE_ID =
    FeatureArtifactId("entry.download.actions.bulk.bookmarked.resolve")

private object EntryDownloadIndividualConsequence : SharedFeatureConsequence {
    override val id = ENTRY_DOWNLOAD_INDIVIDUAL_CONSEQUENCE_ID
}

private object EntryDownloadBulkConsequence : SharedFeatureConsequence {
    override val id = ENTRY_DOWNLOAD_BULK_CONSEQUENCE_ID
}

private object EntryDownloadBookmarkedBulkConsequence : SharedFeatureConsequence {
    override val id = ENTRY_DOWNLOAD_BOOKMARKED_BULK_CONSEQUENCE_ID
}

internal object EntryDownloadActionFeatureContributor : FeatureGraphContributor {
    override val owner = ENTRY_DOWNLOAD_ACTION_FEATURE_OWNER

    override fun contributeTo(sink: FeatureGraphContributionSink) {
        sink.add(
            FeatureContribution(
                feature = ENTRY_DOWNLOAD_ACTION_FEATURE_ID,
                owner = owner,
                integrations = listOf(
                    FeatureIntegration(
                        id = ENTRY_DOWNLOAD_INDIVIDUAL_INTEGRATION_ID,
                        prerequisites = CapabilityExpression.Provided(EntryDownloadCapability.definition),
                        sharedConsequences = listOf(EntryDownloadIndividualConsequence),
                    ),
                    FeatureIntegration(
                        id = ENTRY_DOWNLOAD_BULK_INTEGRATION_ID,
                        prerequisites = allOf(
                            CapabilityExpression.Provided(EntryDownloadCapability.definition),
                            CapabilityExpression.Provided(EntryBulkDownloadCandidateCapability.definition),
                        ),
                        sharedConsequences = listOf(EntryDownloadBulkConsequence),
                    ),
                    FeatureIntegration(
                        id = ENTRY_DOWNLOAD_BOOKMARKED_BULK_INTEGRATION_ID,
                        prerequisites = allOf(
                            CapabilityExpression.Provided(EntryDownloadCapability.definition),
                            CapabilityExpression.Provided(EntryBulkDownloadCandidateCapability.definition),
                            CapabilityExpression.Provided(EntryBookmarkCapability.definition),
                        ),
                        sharedConsequences = listOf(EntryDownloadBookmarkedBulkConsequence),
                    ),
                ),
            ),
        )
    }
}

internal class DefaultEntryDownloadActionFeature(
    evaluation: FeatureGraphEvaluation,
    private val interaction: EntryDownloadInteraction,
) : EntryDownloadActionFeature {
    private val individualTypes = evaluation.applicableProviderTypes<EntryDownloadProcessor>(
        feature = ENTRY_DOWNLOAD_ACTION_FEATURE_ID,
        integration = ENTRY_DOWNLOAD_INDIVIDUAL_INTEGRATION_ID,
        consequence = ENTRY_DOWNLOAD_INDIVIDUAL_CONSEQUENCE_ID,
    )
    private val bulkTypes = evaluation.applicableProviderTypes<EntryBulkDownloadCandidateProcessor>(
        feature = ENTRY_DOWNLOAD_ACTION_FEATURE_ID,
        integration = ENTRY_DOWNLOAD_BULK_INTEGRATION_ID,
        consequence = ENTRY_DOWNLOAD_BULK_CONSEQUENCE_ID,
    )
    private val bookmarkedBulkTypes = evaluation.applicableProviderTypes<EntryBookmarkProcessor>(
        feature = ENTRY_DOWNLOAD_ACTION_FEATURE_ID,
        integration = ENTRY_DOWNLOAD_BOOKMARKED_BULK_INTEGRATION_ID,
        consequence = ENTRY_DOWNLOAD_BOOKMARKED_BULK_CONSEQUENCE_ID,
    )

    override fun individualAvailability(target: EntryDownloadActionTarget): EntryDownloadActionAvailability {
        return availability(listOf(target), individualTypes)
    }

    override fun individualSelectionAvailability(
        targets: List<EntryDownloadActionTarget>,
    ): EntryDownloadActionAvailability {
        return availability(targets, individualTypes)
    }

    override fun bulkAvailability(
        targets: List<EntryDownloadActionTarget>,
        action: EntryBulkDownloadAction,
    ): EntryDownloadActionAvailability {
        val applicableTypes = when (action.type) {
            EntryBulkDownloadActionType.NEXT,
            EntryBulkDownloadActionType.UNREAD,
            -> bulkTypes
            EntryBulkDownloadActionType.BOOKMARKED -> bookmarkedBulkTypes
        }
        return availability(targets, applicableTypes)
    }

    override fun notificationAvailability(
        target: EntryDownloadActionTarget,
        childCount: Int,
    ): EntryDownloadActionAvailability {
        val base = individualAvailability(target)
        if (base !is EntryDownloadActionAvailability.Available) return base
        return when {
            childCount <= 0 -> blockedBy(EntryDownloadActionBlocker.EMPTY_SELECTION)
            childCount > NOTIFICATION_DOWNLOAD_SELECTION_LIMIT -> {
                blockedBy(EntryDownloadActionBlocker.NOTIFICATION_SELECTION_TOO_LARGE)
            }
            else -> EntryDownloadActionAvailability.Available
        }
    }

    override suspend fun download(
        target: EntryDownloadActionTarget,
        entry: Entry,
        chapters: List<EntryChapter>,
        startNow: Boolean,
    ): EntryDownloadActionResult {
        requireTargetMatchesEntry(target, entry)
        return when (val availability = individualOperationAvailability(target, chapters)) {
            EntryDownloadActionAvailability.Available -> {
                interaction.download(entry, chapters, startNow)
                EntryDownloadActionResult.Performed
            }
            is EntryDownloadActionAvailability.Inapplicable -> availability.asActionResult()
            is EntryDownloadActionAvailability.Blocked -> availability.asActionResult()
        }
    }

    override suspend fun delete(
        target: EntryDownloadActionTarget,
        entry: Entry,
        chapters: List<EntryChapter>,
    ): EntryDownloadActionResult {
        requireTargetMatchesEntry(target, entry)
        return when (val availability = individualOperationAvailability(target, chapters)) {
            EntryDownloadActionAvailability.Available -> {
                interaction.delete(entry, chapters)
                EntryDownloadActionResult.Performed
            }
            is EntryDownloadActionAvailability.Inapplicable -> availability.asActionResult()
            is EntryDownloadActionAvailability.Blocked -> availability.asActionResult()
        }
    }

    override fun cancel(
        target: EntryDownloadActionTarget,
        chapterId: Long,
    ): EntryDownloadCancellationResult {
        return when (val availability = individualAvailability(target)) {
            EntryDownloadActionAvailability.Available -> interaction.cancelQueuedDownload(target.type, chapterId)
                ?.let(EntryDownloadCancellationResult::Cancelled)
                ?: EntryDownloadCancellationResult.NotQueued
            is EntryDownloadActionAvailability.Inapplicable -> {
                EntryDownloadCancellationResult.Inapplicable(availability.types)
            }
            is EntryDownloadActionAvailability.Blocked -> {
                EntryDownloadCancellationResult.Blocked(availability.blockers)
            }
        }
    }

    override fun retry(targets: List<EntryDownloadActionTarget>): EntryDownloadActionResult {
        return when (val availability = availability(targets, individualTypes)) {
            EntryDownloadActionAvailability.Available -> {
                interaction.startDownloads()
                EntryDownloadActionResult.Performed
            }
            is EntryDownloadActionAvailability.Inapplicable -> availability.asActionResult()
            is EntryDownloadActionAvailability.Blocked -> availability.asActionResult()
        }
    }

    override suspend fun resolveBulkDownloadCandidates(
        target: EntryDownloadActionTarget,
        entry: Entry,
        action: EntryBulkDownloadAction,
        candidates: List<EntryChapter>?,
        memberEntryIds: List<Long>,
    ): EntryBulkDownloadResolutionResult {
        requireTargetMatchesEntry(target, entry)
        return when (val availability = bulkAvailability(listOf(target), action)) {
            EntryDownloadActionAvailability.Available -> {
                val pool = interaction.resolveBulkDownloadCandidatePool(entry, candidates)
                pool.selectBulkDownloadCandidates(entry, action, memberEntryIds)
                    .takeIf(List<EntryChapter>::isNotEmpty)
                    ?.let(EntryBulkDownloadResolutionResult::Candidates)
                    ?: EntryBulkDownloadResolutionResult.NoCandidates
            }
            is EntryDownloadActionAvailability.Inapplicable -> {
                EntryBulkDownloadResolutionResult.Inapplicable(availability.types)
            }
            is EntryDownloadActionAvailability.Blocked -> {
                EntryBulkDownloadResolutionResult.Blocked(availability.blockers)
            }
        }
    }

    private fun individualOperationAvailability(
        target: EntryDownloadActionTarget,
        chapters: List<EntryChapter>,
    ): EntryDownloadActionAvailability {
        val availability = individualAvailability(target)
        if (availability !is EntryDownloadActionAvailability.Available) return availability
        return if (chapters.isEmpty()) {
            blockedBy(EntryDownloadActionBlocker.EMPTY_SELECTION)
        } else {
            EntryDownloadActionAvailability.Available
        }
    }

    private fun availability(
        targets: List<EntryDownloadActionTarget>,
        applicableTypes: Set<EntryType>,
    ): EntryDownloadActionAvailability {
        if (targets.isEmpty()) return blockedBy(EntryDownloadActionBlocker.EMPTY_SELECTION)

        val inapplicableTypes = targets.map(
            EntryDownloadActionTarget::type,
        ).filterNot(applicableTypes::contains).toSet()
        if (inapplicableTypes.isNotEmpty()) {
            return EntryDownloadActionAvailability.Inapplicable(inapplicableTypes)
        }

        val blockers = buildSet {
            if (targets.any { it.sourceAccess == EntryDownloadSourceAccess.LOCAL_OR_STUB }) {
                add(EntryDownloadActionBlocker.LOCAL_OR_STUB)
            }
        }
        return if (blockers.isEmpty()) {
            EntryDownloadActionAvailability.Available
        } else {
            EntryDownloadActionAvailability.Blocked(blockers)
        }
    }

    private fun requireTargetMatchesEntry(target: EntryDownloadActionTarget, entry: Entry) {
        require(target.type == entry.type) {
            "Download action target ${target.type} does not match Entry type ${entry.type}"
        }
    }
}

private const val NOTIFICATION_DOWNLOAD_SELECTION_LIMIT = 15

private fun blockedBy(blocker: EntryDownloadActionBlocker): EntryDownloadActionAvailability.Blocked {
    return EntryDownloadActionAvailability.Blocked(setOf(blocker))
}

private fun EntryDownloadActionAvailability.Inapplicable.asActionResult(): EntryDownloadActionResult.Inapplicable {
    return EntryDownloadActionResult.Inapplicable(types)
}

private fun EntryDownloadActionAvailability.Blocked.asActionResult(): EntryDownloadActionResult.Blocked {
    return EntryDownloadActionResult.Blocked(blockers)
}

private fun List<EntryChapter>.selectBulkDownloadCandidates(
    entry: Entry,
    action: EntryBulkDownloadAction,
    memberEntryIds: List<Long>,
): List<EntryChapter> {
    return when (action.type) {
        EntryBulkDownloadActionType.NEXT -> filterNot(EntryChapter::read)
            .sortedForReading(entry, memberEntryIds.ifEmpty { map(EntryChapter::entryId).distinct() })
            .let { chapters -> action.limit?.let(chapters::take) ?: chapters }
        EntryBulkDownloadActionType.UNREAD -> filterNot(EntryChapter::read)
        EntryBulkDownloadActionType.BOOKMARKED -> filter(EntryChapter::bookmark)
    }
}
