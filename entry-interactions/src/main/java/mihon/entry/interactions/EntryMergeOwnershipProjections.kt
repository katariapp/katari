package mihon.entry.interactions

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import mihon.entry.interactions.host.EntryMergeHost

internal class EntryMergeChildOwnershipCoordinator(
    private val host: EntryMergeHost,
) : EntryMergeChildOwnershipProjection {
    override suspend fun resolveChildOwners(subject: EntryMergeSubject): EntryMergeChildOwners {
        val profile = host.profile(subject.profileId)
        val membership = profile.membership(subject.entryId)
        val ownerIds = membership?.orderedEntryIds ?: listOf(subject.entryId)
        return EntryMergeChildOwners(
            requestedSubject = subject,
            visibleEntryId = membership?.targetEntryId ?: subject.entryId,
            orderedOwners = profile.entries(ownerIds),
        )
    }

    override fun observeChildOwners(subject: EntryMergeSubject): Flow<EntryMergeChildOwners> {
        val profile = host.profile(subject.profileId)
        return profile.observeMembership(subject.entryId).map { membership ->
            val ownerIds = membership?.orderedEntryIds ?: listOf(subject.entryId)
            EntryMergeChildOwners(
                requestedSubject = subject,
                visibleEntryId = membership?.targetEntryId ?: subject.entryId,
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
