package mihon.entry.interactions

import eu.kanade.tachiyomi.source.entry.EntryType
import mihon.feature.graph.FeatureGraphEvaluation
import tachiyomi.domain.entry.model.Entry
import tachiyomi.domain.entry.model.EntryChapter
import tachiyomi.domain.entry.service.sortedForReading

internal class DefaultEntryDownloadActionFeature(
    private val evaluation: FeatureGraphEvaluation,
    private val interaction: EntryDownloadInteraction,
) : EntryDownloadActionFeature {
    private val individualTypes = evaluation.downloadIndividualTypes()
    private val bulkTypes = evaluation.downloadBulkTypes()
    private val bookmarkedBulkTypes = evaluation.downloadBookmarkedBulkTypes()

    override fun individualAvailability(target: EntryDownloadActionTarget): EntryDownloadActionAvailability {
        return availability(listOf(target), individualTypes, evaluation::requireDownloadIndividualContext)
    }

    override fun individualSelectionAvailability(
        targets: List<EntryDownloadActionTarget>,
    ): EntryDownloadActionAvailability {
        return availability(targets, individualTypes, evaluation::requireDownloadIndividualContext)
    }

    override fun bulkAvailability(
        targets: List<EntryDownloadActionTarget>,
        action: EntryBulkDownloadAction,
    ): EntryDownloadActionAvailability {
        val bookmarked = action.type == EntryBulkDownloadActionType.BOOKMARKED
        val applicableTypes = if (bookmarked) bookmarkedBulkTypes else bulkTypes
        return availability(targets, applicableTypes) { target ->
            evaluation.requireDownloadBulkContext(target, bookmarked)
        }
    }

    override fun notificationAvailability(
        target: EntryDownloadActionTarget,
        childCount: Int,
    ): EntryDownloadActionAvailability {
        if (target.type !in individualTypes) {
            return EntryDownloadActionAvailability.Inapplicable(setOf(target.type))
        }
        val selectionState = when {
            childCount <= 0 -> EntryDownloadSelectionState.EMPTY
            childCount > NOTIFICATION_DOWNLOAD_SELECTION_LIMIT ->
                EntryDownloadSelectionState.NOTIFICATION_LIMIT_EXCEEDED
            else -> EntryDownloadSelectionState.ACTIONABLE
        }
        evaluation.requireDownloadNotificationContext(target, selectionState)
        return when {
            target.sourceAccess == EntryDownloadSourceAccess.LOCAL_OR_STUB ->
                blockedBy(EntryDownloadActionBlocker.LOCAL_OR_STUB)
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
        return when (val availability = individualSelectionAvailability(targets)) {
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
        if (target.type !in individualTypes) {
            return EntryDownloadActionAvailability.Inapplicable(setOf(target.type))
        }
        val selectionState = if (chapters.isEmpty()) {
            EntryDownloadSelectionState.EMPTY
        } else {
            EntryDownloadSelectionState.ACTIONABLE
        }
        evaluation.requireDownloadIndividualOperationContext(target, selectionState)
        return when {
            target.sourceAccess == EntryDownloadSourceAccess.LOCAL_OR_STUB ->
                blockedBy(EntryDownloadActionBlocker.LOCAL_OR_STUB)
            selectionState == EntryDownloadSelectionState.EMPTY ->
                blockedBy(EntryDownloadActionBlocker.EMPTY_SELECTION)
            else -> EntryDownloadActionAvailability.Available
        }
    }

    private fun availability(
        targets: List<EntryDownloadActionTarget>,
        applicableTypes: Set<EntryType>,
        requireContext: (EntryDownloadActionTarget) -> Unit,
    ): EntryDownloadActionAvailability {
        if (targets.isEmpty()) return blockedBy(EntryDownloadActionBlocker.EMPTY_SELECTION)

        val inapplicableTypes = targets.map(
            EntryDownloadActionTarget::type,
        ).filterNot(applicableTypes::contains).toSet()
        if (inapplicableTypes.isNotEmpty()) {
            return EntryDownloadActionAvailability.Inapplicable(inapplicableTypes)
        }

        targets.forEach(requireContext)
        return if (targets.any { it.sourceAccess == EntryDownloadSourceAccess.LOCAL_OR_STUB }) {
            blockedBy(EntryDownloadActionBlocker.LOCAL_OR_STUB)
        } else {
            EntryDownloadActionAvailability.Available
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
