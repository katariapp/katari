package mihon.entry.interactions.host

import kotlinx.coroutines.flow.Flow
import tachiyomi.domain.entry.model.DuplicateEntryCandidate
import tachiyomi.domain.entry.model.Entry

/**
 * Host boundary available to the Merge coordinator, not to application feature consumers.
 *
 * Selecting a profile produces an explicitly scoped host. No operation consults ambient active-profile state.
 */
interface EntryMergeHost {
    fun profile(profileId: Long): EntryMergeProfileHost
}

interface EntryMergeProfileHost {
    val profileId: Long

    suspend fun entries(entryIds: List<Long>): List<Entry>

    suspend fun membership(entryId: Long): EntryMergeMembershipSnapshot?

    fun observeMembership(entryId: Long): Flow<EntryMergeMembershipSnapshot?>

    suspend fun memberships(): List<EntryMergeMembershipSnapshot>

    fun observeMemberships(): Flow<List<EntryMergeMembershipSnapshot>>

    suspend fun duplicateCandidates(entry: Entry): List<DuplicateEntryCandidate>

    fun observeDuplicateCandidates(entry: Flow<Entry>): Flow<List<DuplicateEntryCandidate>>
}
