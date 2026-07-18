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

data class EntryUpdateEligibilityRequest(
    val entry: Entry,
    val totalCount: Long,
    val unconsumedCount: Long,
    val hasStarted: Boolean,
    val restrictions: Set<EntryUpdateRestriction>,
    val fetchWindowUpperBound: Long? = null,
)

enum class EntryUpdateRestriction {
    NON_COMPLETED,
    HAS_UNCONSUMED,
    NON_STARTED,
    OUTSIDE_RELEASE_PERIOD,
}

sealed interface EntryUpdateEligibility {
    data object Eligible : EntryUpdateEligibility
    data class Skipped(val reason: EntryUpdateSkipReason) : EntryUpdateEligibility
}

enum class EntryUpdateSkipReason {
    COMPLETED,
    NOT_CAUGHT_UP,
    NOT_STARTED,
    OUTSIDE_RELEASE_PERIOD,
}
