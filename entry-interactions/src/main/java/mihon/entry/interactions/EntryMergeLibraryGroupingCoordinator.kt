package mihon.entry.interactions

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import mihon.entry.interactions.host.EntryMergeHost
import mihon.entry.interactions.host.EntryMergeMembershipSnapshot
import tachiyomi.domain.entry.model.Entry

internal class EntryMergeLibraryGroupingCoordinator(
    private val host: EntryMergeHost,
) : EntryMergeLibraryGroupingFeature {
    override suspend fun groupLibraryEntries(
        profileId: Long,
        entries: List<Entry>,
    ): EntryMergeLibraryGroupingProjection {
        return project(profileId, entries, host.profile(profileId).memberships())
    }

    override fun observeLibraryGroups(
        profileId: Long,
        entries: Flow<List<Entry>>,
    ): Flow<EntryMergeLibraryGroupingProjection> {
        return combine(entries, host.profile(profileId).observeMemberships()) { currentEntries, memberships ->
            project(profileId, currentEntries, memberships)
        }
    }

    private fun project(
        profileId: Long,
        entries: List<Entry>,
        memberships: List<EntryMergeMembershipSnapshot>,
    ): EntryMergeLibraryGroupingProjection {
        require(entries.all { it.profileId == profileId }) { "Library grouping cannot cross profiles" }
        val entriesById = entries.associateBy(Entry::id)
        val membershipByEntryId = memberships
            .flatMap { membership -> membership.orderedEntryIds.map { it to membership } }
            .toMap()
        val consumed = mutableSetOf<Long>()
        val groups = entries.mapNotNull { entry ->
            if (!consumed.add(entry.id)) return@mapNotNull null
            val membership = membershipByEntryId[entry.id]
            if (membership == null) {
                EntryMergeLibraryGroup(entry, listOf(entry))
            } else {
                val ordered = membership.orderedEntryIds.mapNotNull(entriesById::get)
                consumed += ordered.map(Entry::id)
                val visible = entriesById[membership.targetEntryId] ?: ordered.firstOrNull() ?: entry
                EntryMergeLibraryGroup(visible, ordered.ifEmpty { listOf(entry) })
            }
        }
        return EntryMergeLibraryGroupingProjection(profileId, groups)
    }
}
