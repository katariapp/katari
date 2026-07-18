package mihon.entry.interactions

import mihon.entry.interactions.host.EntryMergeHost

internal class EntryMergeBackupCoordinator(
    private val host: EntryMergeHost,
) : EntryMergeBackupFeature {
    override suspend fun snapshotForBackup(subject: EntryMergeSubject): EntryMergeBackupProjection? {
        val membership = host.profile(subject.profileId).membership(subject.entryId) ?: return null
        return EntryMergeBackupProjection(
            profileId = membership.profileId,
            targetEntryId = membership.targetEntryId,
            orderedEntryIds = membership.orderedEntryIds,
        )
    }
}
