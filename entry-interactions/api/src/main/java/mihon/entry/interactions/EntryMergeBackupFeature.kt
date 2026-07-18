package mihon.entry.interactions

/** Read-only, purpose-specific representation used by backup creation; restore is a separate workflow. */
interface EntryMergeBackupFeature {
    suspend fun snapshotForBackup(subject: EntryMergeSubject): EntryMergeBackupProjection?
}

data class EntryMergeBackupProjection(
    val profileId: Long,
    val targetEntryId: Long,
    val orderedEntryIds: List<Long>,
)
