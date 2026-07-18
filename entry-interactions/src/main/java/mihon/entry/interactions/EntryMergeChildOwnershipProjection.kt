package mihon.entry.interactions

import kotlinx.coroutines.flow.Flow
import tachiyomi.domain.entry.model.Entry

/** Internal Merge projection consumed by child-owning feature coordinators, never by application screens. */
internal interface EntryMergeChildOwnershipProjection {
    suspend fun resolveChildOwners(subject: EntryMergeSubject): EntryMergeChildOwners

    fun observeChildOwners(subject: EntryMergeSubject): Flow<EntryMergeChildOwners>
}

/** Standalone Entries contain one owner; merged Entries contain the authoritative ordered owners. */
internal data class EntryMergeChildOwners(
    val requestedSubject: EntryMergeSubject,
    val visibleEntryId: Long,
    val orderedOwners: List<Entry>,
)
