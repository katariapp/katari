package mihon.entry.interactions

import eu.kanade.tachiyomi.source.entry.EntryType
import tachiyomi.domain.entry.model.Entry
import tachiyomi.domain.entry.model.EntryChapter

/** Feature-owned boundary for user-initiated individual and bulk download actions. */
interface EntryDownloadActionFeature {
    fun individualAvailability(target: EntryDownloadActionTarget): EntryDownloadActionAvailability

    fun individualSelectionAvailability(
        targets: List<EntryDownloadActionTarget>,
    ): EntryDownloadActionAvailability

    fun bulkAvailability(
        targets: List<EntryDownloadActionTarget>,
        action: EntryBulkDownloadAction,
    ): EntryDownloadActionAvailability

    fun notificationAvailability(
        target: EntryDownloadActionTarget,
        childCount: Int,
    ): EntryDownloadActionAvailability

    suspend fun download(
        target: EntryDownloadActionTarget,
        entry: Entry,
        chapters: List<EntryChapter>,
        startNow: Boolean = false,
    ): EntryDownloadActionResult

    suspend fun delete(
        target: EntryDownloadActionTarget,
        entry: Entry,
        chapters: List<EntryChapter>,
    ): EntryDownloadActionResult

    fun cancel(
        target: EntryDownloadActionTarget,
        chapterId: Long,
    ): EntryDownloadCancellationResult

    /** Restarts processing after a user retries selected failed downloads. */
    fun retry(targets: List<EntryDownloadActionTarget>): EntryDownloadActionResult

    suspend fun resolveBulkDownloadCandidates(
        target: EntryDownloadActionTarget,
        entry: Entry,
        action: EntryBulkDownloadAction,
        candidates: List<EntryChapter>? = null,
        memberEntryIds: List<Long> = emptyList(),
    ): EntryBulkDownloadResolutionResult
}

data class EntryBulkDownloadAction(
    val type: EntryBulkDownloadActionType,
    val limit: Int? = null,
) {
    companion object {
        fun next(limit: Int): EntryBulkDownloadAction = EntryBulkDownloadAction(EntryBulkDownloadActionType.NEXT, limit)
        val unread: EntryBulkDownloadAction = EntryBulkDownloadAction(EntryBulkDownloadActionType.UNREAD)
        val bookmarked: EntryBulkDownloadAction = EntryBulkDownloadAction(EntryBulkDownloadActionType.BOOKMARKED)
    }
}

enum class EntryBulkDownloadActionType {
    NEXT,
    UNREAD,
    BOOKMARKED,
}

data class EntryDownloadActionTarget(
    val type: EntryType,
    val sourceAccess: EntryDownloadSourceAccess,
)

enum class EntryDownloadSourceAccess {
    REMOTE,
    LOCAL_OR_STUB,
}

enum class EntryDownloadActionBlocker {
    EMPTY_SELECTION,
    LOCAL_OR_STUB,
    NOTIFICATION_SELECTION_TOO_LARGE,
}

sealed interface EntryDownloadActionAvailability {
    data object Available : EntryDownloadActionAvailability

    data class Inapplicable(
        val types: Set<EntryType>,
    ) : EntryDownloadActionAvailability

    data class Blocked(
        val blockers: Set<EntryDownloadActionBlocker>,
    ) : EntryDownloadActionAvailability
}

sealed interface EntryDownloadActionResult {
    data object Performed : EntryDownloadActionResult

    data class Inapplicable(
        val types: Set<EntryType>,
    ) : EntryDownloadActionResult

    data class Blocked(
        val blockers: Set<EntryDownloadActionBlocker>,
    ) : EntryDownloadActionResult
}

sealed interface EntryDownloadCancellationResult {
    data class Cancelled(
        val status: EntryDownloadStatus,
    ) : EntryDownloadCancellationResult

    data object NotQueued : EntryDownloadCancellationResult

    data class Inapplicable(
        val types: Set<EntryType>,
    ) : EntryDownloadCancellationResult

    data class Blocked(
        val blockers: Set<EntryDownloadActionBlocker>,
    ) : EntryDownloadCancellationResult
}

sealed interface EntryBulkDownloadResolutionResult {
    data class Candidates(
        val chapters: List<EntryChapter>,
    ) : EntryBulkDownloadResolutionResult

    data object NoCandidates : EntryBulkDownloadResolutionResult

    data class Inapplicable(
        val types: Set<EntryType>,
    ) : EntryBulkDownloadResolutionResult

    data class Blocked(
        val blockers: Set<EntryDownloadActionBlocker>,
    ) : EntryBulkDownloadResolutionResult
}
