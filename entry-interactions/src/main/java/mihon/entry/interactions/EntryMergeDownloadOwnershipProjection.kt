package mihon.entry.interactions

import kotlinx.coroutines.flow.Flow

/** Internal Merge projection consumed only by Download feature coordinators. */
internal interface EntryMergeDownloadOwnershipProjection {
    suspend fun resolveDownloadOwners(subject: EntryMergeSubject): EntryMergeDownloadOwners

    fun observeDownloadOwnerships(profileId: Long): Flow<List<EntryMergeDownloadOwners>>
}

internal data class EntryMergeDownloadOwners(
    val profileId: Long,
    val visibleEntryId: Long,
    val orderedOwnerEntryIds: List<Long>,
)
