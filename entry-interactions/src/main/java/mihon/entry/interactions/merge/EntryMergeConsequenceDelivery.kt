package mihon.entry.interactions

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import mihon.entry.interactions.host.EntryMergeHost
import mihon.entry.interactions.host.EntryMergePendingConsequence
import tachiyomi.domain.entry.model.Entry

internal class EntryMergeConsequenceDelivery(
    private val host: EntryMergeHost,
    private val tracking: () -> EntryTrackingFeature,
    private val coverCleanup: suspend (Entry) -> Unit,
    private val downloadMaintenance: () -> EntryDownloadMaintenanceFeature,
    private val clockMillis: () -> Long = System::currentTimeMillis,
) {
    suspend fun deliverOperation(operationId: String): EntryMergeFollowUp {
        deliverPending()
        return if (host.pendingConsequenceCount(operationId) == 0L) {
            EntryMergeFollowUp.COMPLETE
        } else {
            EntryMergeFollowUp.PENDING
        }
    }

    suspend fun deliverPending(limit: Int = DEFAULT_BATCH_SIZE) {
        host.pendingConsequences(limit).forEach { consequence ->
            try {
                deliver(consequence)
                host.acknowledgeConsequence(consequence.id)
            } catch (error: CancellationException) {
                throw error
            } catch (error: Exception) {
                host.recordConsequenceFailure(
                    consequenceId = consequence.id,
                    message = error.message ?: error::class.qualifiedName.orEmpty(),
                    retryAtMillis = clockMillis() + RETRY_DELAY_MILLIS,
                )
            }
        }
    }

    suspend fun runRetryLoop() {
        while (currentCoroutineContext().isActive) {
            try {
                deliverPending()
            } catch (error: CancellationException) {
                throw error
            } catch (_: Exception) {
                // A host-level failure leaves every record durable for the next pass.
            }
            delay(RETRY_DELAY_MILLIS)
        }
    }

    private suspend fun deliver(consequence: EntryMergePendingConsequence) {
        val entry = host.profile(consequence.profileId).entries(listOf(consequence.entryId)).singleOrNull()
            ?: return
        when (consequence.artifactId) {
            EntryMergeLibraryInitializationConsequence.id.value -> tracking().bindAutomatically(entry)
            EntryMergeCoverCleanupConsequence.id.value -> coverCleanup(entry)
            EntryMergeDownloadRemovalConsequence.id.value -> {
                check(
                    downloadMaintenance().removeEntryDownloads(entry) == EntryDownloadMaintenanceResult.Performed,
                ) { "Merge download removal was not verified" }
            }
            else -> error("No Merge consequence handler for ${consequence.artifactId}")
        }
    }

    private companion object {
        const val DEFAULT_BATCH_SIZE = 100
        const val RETRY_DELAY_MILLIS = 60_000L
    }
}
