package mihon.entry.interactions

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import mihon.entry.interactions.host.EntryMergeHost
import tachiyomi.domain.entry.service.EntryChildOwnershipResolution

internal class EntryMergeChildOwnershipCoordinator(
    private val host: EntryMergeHost,
) : EntryMergeChildOwnershipProjection {
    override suspend fun resolveChildOwnership(profileId: Long, entryId: Long): EntryChildOwnershipResolution {
        val profile = host.profile(profileId)
        val membership = profile.membership(entryId)
        val ownerIds = membership?.orderedEntryIds ?: listOf(entryId)
        return EntryChildOwnershipResolution(
            profileId = profileId,
            requestedEntryId = entryId,
            visibleEntryId = membership?.targetEntryId ?: entryId,
            orderedOwners = profile.entries(ownerIds),
        )
    }

    override fun observeChildOwnership(profileId: Long, entryId: Long): Flow<EntryChildOwnershipResolution> {
        val profile = host.profile(profileId)
        return profile.observeMembership(entryId).map { membership ->
            val ownerIds = membership?.orderedEntryIds ?: listOf(entryId)
            EntryChildOwnershipResolution(
                profileId = profileId,
                requestedEntryId = entryId,
                visibleEntryId = membership?.targetEntryId ?: entryId,
                orderedOwners = profile.entries(ownerIds),
            )
        }
    }
}

internal class EntryMergeDownloadOwnershipCoordinator(
    private val host: EntryMergeHost,
) : EntryMergeDownloadOwnershipProjection {
    override suspend fun resolveDownloadOwners(subject: EntryMergeSubject): EntryMergeDownloadOwners {
        val membership = host.profile(subject.profileId).membership(subject.entryId)
        return EntryMergeDownloadOwners(
            profileId = subject.profileId,
            visibleEntryId = membership?.targetEntryId ?: subject.entryId,
            orderedOwnerEntryIds = membership?.orderedEntryIds ?: listOf(subject.entryId),
        )
    }

    override fun observeDownloadOwnerships(profileId: Long): Flow<List<EntryMergeDownloadOwners>> {
        return host.profile(profileId).observeMemberships().map { memberships ->
            memberships.map { membership ->
                EntryMergeDownloadOwners(
                    profileId = profileId,
                    visibleEntryId = membership.targetEntryId,
                    orderedOwnerEntryIds = membership.orderedEntryIds,
                )
            }
        }
    }
}
