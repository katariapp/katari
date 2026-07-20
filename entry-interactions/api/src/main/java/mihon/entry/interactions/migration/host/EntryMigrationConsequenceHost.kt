package mihon.entry.interactions.host

import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.Serializable
import tachiyomi.domain.entry.model.Entry

interface EntryMigrationConsequenceHost {
    suspend fun pendingConsequences(limit: Int): List<EntryMigrationPendingConsequence>
    suspend fun pendingConsequences(operationId: String, limit: Int): List<EntryMigrationPendingConsequence>
    suspend fun acknowledgeConsequence(consequenceId: String)
    suspend fun recordConsequenceFailure(consequenceId: String, message: String, retryAtMillis: Long)
    suspend fun pendingConsequenceCount(operationId: String): Long
    suspend fun consequencePayloads(artifactId: String): List<String>
    fun observeConsequenceStatus(): Flow<EntryMigrationConsequenceStatusSnapshot>
    suspend fun makeConsequencesRetryable()
}

data class EntryMigrationPendingConsequence(
    val id: String,
    val operationId: String,
    val profileId: Long,
    val artifactId: String,
    val payload: String,
    val attempts: Long,
)

data class EntryMigrationConsequenceStatusSnapshot(
    val pendingCount: Long,
    val failedCount: Long,
    val lastFailure: String?,
)

interface EntryMigrationCustomCoverHost {
    suspend fun stage(operationId: String, source: Entry, target: Entry): EntryMigrationCustomCoverPayload?
    suspend fun promote(payload: EntryMigrationCustomCoverPayload)
    suspend fun discard(payload: EntryMigrationCustomCoverPayload)
    suspend fun cleanupOrphans(activeStageIds: Set<String>, olderThanMillis: Long, limit: Int)
}

@Serializable
data class EntryMigrationCustomCoverPayload(
    val stageId: String,
    val targetEntryId: Long,
)
