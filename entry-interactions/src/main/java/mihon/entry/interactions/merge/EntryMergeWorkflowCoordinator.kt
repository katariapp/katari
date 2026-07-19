package mihon.entry.interactions

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest
import mihon.entry.interactions.host.EntryMergeConsequenceRequest
import mihon.entry.interactions.host.EntryMergeHost
import mihon.entry.interactions.host.EntryMergeHostExpectation
import mihon.entry.interactions.host.EntryMergeHostExpectedEntry
import mihon.entry.interactions.host.EntryMergeHostMemberKey
import mihon.entry.interactions.host.EntryMergeHostPreparation
import mihon.entry.interactions.host.EntryMergeHostTransition
import mihon.entry.interactions.host.EntryMergeHostTransitionResult
import mihon.entry.interactions.host.EntryMergeMembershipSnapshot
import mihon.feature.graph.FeatureGraphEvaluation
import tachiyomi.domain.entry.model.Entry
import java.util.UUID

internal class EntryMergeWorkflowCoordinator(
    evaluation: FeatureGraphEvaluation,
    private val host: EntryMergeHost,
    private val consequences: EntryMergeConsequenceDelivery,
) : EntryMergeFeature {
    private val applicableTypes = evaluation.mergeTypes(
        ENTRY_MERGE_BASE_INTEGRATION_ID,
        EntryMergeBaseConsequence.WORKFLOW.id,
    )
    private val downloadRemovalTypes = evaluation.mergeTypes(
        ENTRY_MERGE_DOWNLOAD_INTEGRATION_ID,
        EntryMergeDownloadConsequence.REMOVAL.id,
    )

    override suspend fun prepare(intent: EntryMergePrepareIntent): EntryMergePreparationResult {
        if (intent.selectedEntries.isEmpty()) return rejected(EntryMergeRejection.EMPTY_SELECTION)
        if (intent.selectedEntries.map(Entry::type).distinct().size != 1) {
            return rejected(EntryMergeRejection.MIXED_ENTRY_TYPES)
        }
        if (intent.selectedEntries.map(Entry::profileId).distinct().size != 1) {
            return rejected(EntryMergeRejection.MIXED_PROFILES)
        }

        val type = intent.selectedEntries.first().type
        val profileId = intent.selectedEntries.first().profileId
        check(type in applicableTypes) { "Entry type $type was not composed into the Merge feature" }
        val profile = host.profile(profileId)
        val resolvedSelected = mutableListOf<Entry>()
        intent.selectedEntries.forEach { selected ->
            val resolved = if (selected.id > 0L) {
                val authoritative = profile.entries(listOf(selected.id)).singleOrNull()
                    ?: return rejected(EntryMergeRejection.ENTRY_NOT_IN_EDITOR)
                if (contentIdentity(authoritative) != contentIdentity(selected)) {
                    return rejected(EntryMergeRejection.ENTRY_NOT_IN_EDITOR)
                }
                authoritative
            } else {
                profile.resolveEntryIdentity(selected) ?: selected
            }
            resolvedSelected += resolved
        }
        if (resolvedSelected.map(::contentIdentity).distinct().size != resolvedSelected.size) {
            return rejected(EntryMergeRejection.DUPLICATE_ENTRIES)
        }
        if (resolvedSelected.any { it.type != type }) return rejected(EntryMergeRejection.MIXED_ENTRY_TYPES)
        if (resolvedSelected.any { it.profileId != profileId }) return rejected(EntryMergeRejection.MIXED_PROFILES)

        val selectedMemberships = resolvedSelected.map { entry ->
            entry.takeIf { it.id > 0L }?.let { profile.membership(it.id) }
        }
        val existingGroups = selectedMemberships.filterNotNull().distinctBy { it.targetEntryId }
        if (existingGroups.size > 1) return rejected(EntryMergeRejection.MULTIPLE_EXISTING_GROUPS)

        val existingGroup = existingGroups.singleOrNull()
        val existingMembers = existingGroup?.let { profile.entries(it.orderedEntryIds) }.orEmpty()
        if (existingGroup != null && existingMembers.map(Entry::id) != existingGroup.orderedEntryIds) {
            return EntryMergePreparationResult.Rejected(EntryMergeRejection.ENTRY_NOT_IN_EDITOR)
        }

        val preparations = intent.preparations.associateBy { preparation -> contentIdentity(preparation.entry) }
        if (preparations.size != intent.preparations.size) {
            return rejected(EntryMergeRejection.DUPLICATE_ENTRIES)
        }
        val existingMemberIds = existingGroup?.orderedEntryIds.orEmpty().toSet()
        val orderedEntries = buildList {
            resolvedSelected.forEach { selected ->
                val entriesAtSelection = if (selected.id in existingMemberIds) existingMembers else listOf(selected)
                entriesAtSelection.forEach { entry ->
                    if (none { contentIdentity(it) == contentIdentity(entry) }) add(entry)
                }
            }
        }
        if (orderedEntries.size < 2) return rejected(EntryMergeRejection.TOO_FEW_ENTRIES)
        val selectedIdentities = resolvedSelected.mapTo(mutableSetOf(), ::contentIdentity)
        val sessionId = newEntryMergeSessionId()
        val expectedEntries = mutableListOf<EntryMergeHostExpectedEntry>()
        val hostPreparations = mutableListOf<EntryMergeHostPreparation>()
        val editorEntries = linkedMapOf<EntryMergeHostMemberKey, EntryMergeEditorEntry>()

        orderedEntries.forEach { entry ->
            val identity = contentIdentity(entry)
            val persisted = entry.id > 0L
            val preparation = preparations[identity]
            if ((!persisted || !entry.favorite) && identity in selectedIdentities && preparation == null) {
                return rejected(EntryMergeRejection.PREPARATION_MISSING)
            }
            val key = newEntryMergeMemberKey()
            val membership = if (persisted) profile.membership(entry.id) else null
            val reference = FeatureEntryMergeEditorEntryReference(sessionId, key)
            val existingMember = existingGroup?.orderedEntryIds?.contains(entry.id) == true
            val editorEntry = EntryMergeEditorEntry(
                reference = reference,
                entry = entry,
                origin = when {
                    existingMember -> EntryMergeEditorEntryOrigin.EXISTING_MEMBER
                    persisted -> EntryMergeEditorEntryOrigin.SELECTED
                    else -> EntryMergeEditorEntryOrigin.NEW_MEMBER
                },
                removable = existingMember,
                removableFromLibrary = persisted && entry.favorite,
            )
            editorEntries[key] = editorEntry
            expectedEntries += EntryMergeHostExpectedEntry(
                key = key,
                entry = entry,
                persistedEntryId = entry.id.takeIf { persisted },
                membership = membership,
            )
            preparation?.let {
                hostPreparations += EntryMergeHostPreparation(key, it.entry, it.categoryIds.distinct())
            }
        }

        val targetEntryId = existingGroup?.targetEntryId ?: resolvedSelected.first().id
        val target = editorEntries.values.firstOrNull { it.entry.id == targetEntryId }?.reference
            ?: editorEntries.values.first().reference
        val editReference = FeatureEntryMergeEditReference(
            sessionId = sessionId,
            profileId = profileId,
            type = type,
            expectation = EntryMergeHostExpectation(type, expectedEntries),
            preparations = hostPreparations,
            entries = editorEntries,
        )
        return EntryMergePreparationResult.Ready(
            EntryMergeEditorProjection(
                editReference = editReference,
                profileId = profileId,
                type = type,
                entries = editorEntries.values.toList(),
                target = target,
                targetLocked = false,
            ),
        )
    }

    override fun observeExisting(entry: Entry): Flow<EntryMergeEditorProjection?> {
        if (entry.id <= 0L) return kotlinx.coroutines.flow.flowOf(null)
        return host.profile(entry.profileId).observeMembership(entry.id).mapLatest { membership ->
            if (membership == null) {
                null
            } else {
                when (val result = prepare(EntryMergePrepareIntent(listOf(entry)))) {
                    is EntryMergePreparationResult.Ready -> result.editor
                    is EntryMergePreparationResult.Rejected -> null
                }
            }
        }
    }

    override suspend fun execute(intent: EntryMergeWorkflowIntent): EntryMergeExecutionResult {
        return when (intent) {
            is EntryMergeCommitIntent -> commit(intent)
            is EntryMergeDissolveIntent -> dissolve(intent)
            is EntryMergeRemoveEntriesIntent -> removeEntries(intent)
        }
    }

    private suspend fun commit(intent: EntryMergeCommitIntent): EntryMergeExecutionResult {
        val edit = intent.editReference as? FeatureEntryMergeEditReference
            ?: return rejectedExecution(EntryMergeRejection.UNRECOGNIZED_EDIT_REFERENCE)
        val target = intent.target.toKeyOrNull(edit)
            ?: return rejectedExecution(EntryMergeRejection.UNRECOGNIZED_EDIT_REFERENCE)
        val ordered = intent.orderedEntries.toKeysOrNull(edit)
            ?: return rejectedExecution(EntryMergeRejection.UNRECOGNIZED_EDIT_REFERENCE)
        val removed = intent.removedEntries.toKeysOrNull(edit)?.toSet()
            ?: return rejectedExecution(EntryMergeRejection.UNRECOGNIZED_EDIT_REFERENCE)
        val libraryRemoved = intent.libraryRemovalEntries.toKeysOrNull(edit)?.toSet()
            ?: return rejectedExecution(EntryMergeRejection.UNRECOGNIZED_EDIT_REFERENCE)
        if (ordered.distinct().size != ordered.size || ordered.toSet() != edit.entries.keys) {
            return rejectedExecution(EntryMergeRejection.INVALID_ORDER)
        }
        if (removed.any { it !in edit.entries } || libraryRemoved.any { it !in edit.entries }) {
            return rejectedExecution(EntryMergeRejection.ENTRY_NOT_IN_EDITOR)
        }
        if (removed.intersect(libraryRemoved).isNotEmpty()) {
            return rejectedExecution(EntryMergeRejection.INVALID_ORDER)
        }
        if (target in removed || target in libraryRemoved) {
            return rejectedExecution(EntryMergeRejection.TARGET_REMOVED)
        }
        if ((removed + libraryRemoved).any { edit.entries.getValue(it).removable.not() }) {
            return rejectedExecution(EntryMergeRejection.ENTRY_NOT_IN_EDITOR)
        }
        if (libraryRemoved.any { edit.entries.getValue(it).removableFromLibrary.not() }) {
            return rejectedExecution(EntryMergeRejection.ENTRY_NOT_IN_EDITOR)
        }

        val operationId = UUID.randomUUID().toString()
        val consequenceRequests = edit.preparations.map { preparation ->
            consequence(preparation.key, EntryMergeBaseConsequence.LIBRARY_INITIALIZATION.id.value)
        } + libraryRemoved.flatMap { key ->
            buildList {
                add(consequence(key, EntryMergeBaseConsequence.COVER_CLEANUP.id.value))
                if (edit.type in downloadRemovalTypes) {
                    add(consequence(key, EntryMergeDownloadConsequence.REMOVAL.id.value))
                }
            }
        }
        return apply(
            operationId = operationId,
            profileId = edit.profileId,
            transition = EntryMergeHostTransition.CommitEditor(
                operationId = operationId,
                profileId = edit.profileId,
                expected = edit.expectation,
                preparations = edit.preparations,
                target = target,
                orderedEntries = ordered,
                removedEntries = removed,
                libraryRemovalEntries = libraryRemoved,
                consequenceRequests = consequenceRequests,
            ),
        )
    }

    private suspend fun dissolve(intent: EntryMergeDissolveIntent): EntryMergeExecutionResult {
        val profile = host.profile(intent.subject.profileId)
        val expected = profile.membership(intent.subject.entryId)
            ?: return applied(intent.subject, EntryMergeFollowUp.COMPLETE)
        return changeExistingGroup(
            subject = intent.subject,
            expected = expected,
            replacementIds = emptyList(),
            visibleEntryId = intent.subject.entryId,
            libraryRemovalIds = emptySet(),
            removeDownloads = false,
        )
    }

    private suspend fun removeEntries(intent: EntryMergeRemoveEntriesIntent): EntryMergeExecutionResult {
        if (intent.entryIds.isEmpty()) return applied(intent.subject, EntryMergeFollowUp.COMPLETE)
        val profile = host.profile(intent.subject.profileId)
        val expected = profile.membership(intent.subject.entryId)
            ?: return applied(intent.subject, EntryMergeFollowUp.COMPLETE)
        if (!expected.orderedEntryIds.containsAll(intent.entryIds)) {
            return rejectedExecution(EntryMergeRejection.ENTRY_NOT_IN_EDITOR)
        }
        val replacementIds = expected.orderedEntryIds.filterNot(intent.entryIds::contains)
        return changeExistingGroup(
            subject = intent.subject,
            expected = expected,
            replacementIds = replacementIds,
            visibleEntryId = replacementIds.firstOrNull(),
            libraryRemovalIds = intent.entryIds.takeIf { intent.removeFromLibrary }.orEmpty(),
            removeDownloads = intent.removeDownloads || intent.removeFromLibrary,
        )
    }

    private suspend fun changeExistingGroup(
        subject: EntryMergeSubject,
        expected: EntryMergeMembershipSnapshot,
        replacementIds: List<Long>,
        visibleEntryId: Long?,
        libraryRemovalIds: Set<Long>,
        removeDownloads: Boolean,
    ): EntryMergeExecutionResult {
        val entries = host.profile(subject.profileId).entries(expected.orderedEntryIds)
        if (entries.map(Entry::id) != expected.orderedEntryIds) return EntryMergeExecutionResult.Conflict
        val type = entries.map(Entry::type).distinct().singleOrNull() ?: return EntryMergeExecutionResult.Conflict
        check(type in applicableTypes) { "Entry type $type was not composed into the Merge feature" }
        val operationId = UUID.randomUUID().toString()
        val consequenceRequests = libraryRemovalIds.flatMap { entryId ->
            buildList {
                add(consequence(entryId, EntryMergeBaseConsequence.COVER_CLEANUP.id.value))
                if (removeDownloads && type in downloadRemovalTypes) {
                    add(consequence(entryId, EntryMergeDownloadConsequence.REMOVAL.id.value))
                }
            }
        } + if (!removeDownloads || type !in downloadRemovalTypes) {
            emptyList()
        } else {
            (expected.orderedEntryIds.toSet() - libraryRemovalIds)
                .filter { it !in replacementIds }
                .map { consequence(it, EntryMergeDownloadConsequence.REMOVAL.id.value) }
        }
        val replacementTarget = when {
            replacementIds.size < 2 -> null
            expected.targetEntryId in replacementIds -> expected.targetEntryId
            else -> replacementIds.first()
        }
        return apply(
            operationId = operationId,
            profileId = subject.profileId,
            transition = EntryMergeHostTransition.ChangeExistingGroup(
                operationId = operationId,
                profileId = subject.profileId,
                expected = expected,
                replacementTargetEntryId = replacementTarget,
                replacementOrderedEntryIds = replacementIds.takeIf { it.size > 1 }.orEmpty(),
                visibleEntryId = replacementTarget ?: visibleEntryId,
                libraryRemovalEntryIds = libraryRemovalIds,
                consequenceRequests = consequenceRequests.distinctBy { it.entryId to it.artifactId },
            ),
        )
    }

    private suspend fun apply(
        operationId: String,
        profileId: Long,
        transition: EntryMergeHostTransition,
    ): EntryMergeExecutionResult {
        return when (val result = host.profile(profileId).applyTransition(transition)) {
            is EntryMergeHostTransitionResult.Applied -> {
                val followUp = consequences.deliverOperation(operationId)
                applied(result.visibleEntryId?.let { EntryMergeSubject(profileId, it) }, followUp)
            }
            EntryMergeHostTransitionResult.Conflict -> EntryMergeExecutionResult.Conflict
            is EntryMergeHostTransitionResult.OperationalFailure -> {
                EntryMergeExecutionResult.OperationalFailure(result.retryable)
            }
        }
    }

    private fun EntryMergeEditorEntryReference.toKeyOrNull(
        edit: FeatureEntryMergeEditReference,
    ): EntryMergeHostMemberKey? = try {
        requireFeatureReference(edit).key
    } catch (_: UnrecognizedEntryMergeReferenceException) {
        null
    }

    private fun Collection<EntryMergeEditorEntryReference>.toKeysOrNull(
        edit: FeatureEntryMergeEditReference,
    ): List<EntryMergeHostMemberKey>? = map { it.toKeyOrNull(edit) ?: return null }

    private fun consequence(key: EntryMergeHostMemberKey, artifactId: String) =
        EntryMergeConsequenceRequest(memberKey = key, entryId = null, artifactId = artifactId)

    private fun consequence(entryId: Long, artifactId: String) =
        EntryMergeConsequenceRequest(memberKey = null, entryId = entryId, artifactId = artifactId)

    private fun contentIdentity(entry: Entry): String =
        "${entry.profileId}:${entry.type}:${entry.source}:${entry.url}"

    private fun rejected(reason: EntryMergeRejection) = EntryMergePreparationResult.Rejected(reason)

    private fun rejectedExecution(reason: EntryMergeRejection) = EntryMergeExecutionResult.Rejected(reason)

    private fun applied(subject: EntryMergeSubject?, followUp: EntryMergeFollowUp) =
        EntryMergeExecutionResult.Applied(EntryMergeWorkflowOutcome(subject, followUp))
}
