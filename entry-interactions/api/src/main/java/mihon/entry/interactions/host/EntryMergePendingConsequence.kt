package mihon.entry.interactions.host

data class EntryMergePendingConsequence(
    val id: String,
    val operationId: String,
    val profileId: Long,
    val entryId: Long,
    val artifactId: String,
    val payload: String,
    val attempts: Long,
)
