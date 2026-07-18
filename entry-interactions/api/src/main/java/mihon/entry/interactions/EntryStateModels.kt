package mihon.entry.interactions

import tachiyomi.domain.entry.model.Entry
import tachiyomi.domain.entry.model.EntryChapter

data class EntryMergeCapabilityItem(
    val entry: Entry,
    val isMerged: Boolean,
)

data class EntryConsumptionStatus(
    val consumed: Boolean,
    val hasPartialProgress: Boolean,
)

fun EntryChapter.consumptionStatus(hasPartialProgress: Boolean = false): EntryConsumptionStatus {
    return EntryConsumptionStatus(
        consumed = read,
        hasPartialProgress = hasPartialProgress,
    )
}

data class EntryBookmarkStatus(
    val bookmarked: Boolean,
)

fun EntryChapter.bookmarkStatus(): EntryBookmarkStatus {
    return EntryBookmarkStatus(bookmarked = bookmark)
}
