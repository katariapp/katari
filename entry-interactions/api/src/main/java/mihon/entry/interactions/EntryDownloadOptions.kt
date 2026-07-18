package mihon.entry.interactions

import tachiyomi.domain.entry.model.Entry
import tachiyomi.domain.entry.model.EntryChapter

data class EntryDownloadOption(
    val key: String,
    val label: String,
)

data class EntryDownloadOptionGroup(
    val key: String,
    val label: String,
    val options: List<EntryDownloadOption>,
    val selectedKey: String? = null,
    val defaultLabel: String? = null,
    val required: Boolean = false,
)

data class EntryDownloadOptions(
    val groups: List<EntryDownloadOptionGroup>,
)

data class EntryDownloadOptionSelection(
    val values: Map<String, String?>,
)

sealed interface EntryDownloadLifecycleEvent {
    data class MarkedConsumed(
        val visibleEntry: Entry,
        val children: List<EntryChapter>,
    ) : EntryDownloadLifecycleEvent

    data class Progressed(
        val visibleEntry: Entry,
        val child: EntryChapter,
        val fraction: Double,
        val deduplicateByNumber: Boolean = false,
    ) : EntryDownloadLifecycleEvent

    data class Completed(
        val visibleEntry: Entry,
        val child: EntryChapter,
        val deduplicateByNumber: Boolean = false,
    ) : EntryDownloadLifecycleEvent
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

enum class EntryDownloadSettingCapability {
    ARCHIVE_PACKAGING,
    TALL_IMAGE_SPLITTING,
    PARALLEL_SOURCE_TRANSFERS,
    PARALLEL_ITEM_TRANSFERS,
}

sealed interface EntryBulkDownloadCandidateResult {
    data class Supported(val chapters: List<EntryChapter>) : EntryBulkDownloadCandidateResult
    data object Unsupported : EntryBulkDownloadCandidateResult
}
