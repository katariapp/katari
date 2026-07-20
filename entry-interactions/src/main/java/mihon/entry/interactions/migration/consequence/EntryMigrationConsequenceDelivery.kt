package mihon.entry.interactions

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import mihon.entry.interactions.host.EntryMigrationConsequenceHost
import mihon.entry.interactions.host.EntryMigrationCustomCoverHost
import mihon.entry.interactions.host.EntryMigrationPendingConsequence

internal class EntryMigrationConsequenceDelivery(
    private val host: EntryMigrationConsequenceHost,
    private val progress: () -> EntryProgressFeature,
    private val playbackPreferences: () -> EntryPlaybackPreferencesFeature,
    private val viewerSettings: () -> EntryViewerSettingsFeature,
    private val downloads: () -> EntryDownloadMaintenanceFeature,
    private val customCover: EntryMigrationCustomCoverHost,
    private val codec: EntryMigrationConsequenceCodec = EntryMigrationConsequenceCodec(),
    private val clockMillis: () -> Long = System::currentTimeMillis,
) {
    suspend fun deliverOperation(operationId: String): EntryMigrationFollowUp {
        host.pendingConsequences(operationId, DEFAULT_BATCH_SIZE).forEach { consequence ->
            deliverSafely(consequence)
        }
        return if (host.pendingConsequenceCount(operationId) == 0L) {
            EntryMigrationFollowUp.COMPLETE
        } else {
            EntryMigrationFollowUp.INCOMPLETE
        }
    }

    suspend fun deliverPending(limit: Int = DEFAULT_BATCH_SIZE) {
        host.pendingConsequences(limit).forEach { consequence -> deliverSafely(consequence) }
    }

    suspend fun runRetryLoop() {
        try {
            cleanupCoverOrphans()
        } catch (error: CancellationException) {
            throw error
        } catch (_: Exception) {
            // Delivery still starts when optional cleanup cannot inspect its durable records.
        }
        while (currentCoroutineContext().isActive) {
            try {
                deliverPending()
            } catch (error: CancellationException) {
                throw error
            } catch (_: Exception) {
                // Host-level failure leaves every record durable for the next pass.
            }
            delay(RETRY_DELAY_MILLIS)
        }
    }

    private suspend fun cleanupCoverOrphans() {
        val activeStageIds = host.consequencePayloads(EntryMigrationConsequenceArtifact.CUSTOM_COVER)
            .mapNotNull { payload -> runCatching { codec.customCover(payload).stageId }.getOrNull() }
            .toSet()
        customCover.cleanupOrphans(
            activeStageIds = activeStageIds,
            olderThanMillis = clockMillis() - ORPHAN_MINIMUM_AGE_MILLIS,
            limit = ORPHAN_CLEANUP_BATCH_SIZE,
        )
    }

    private suspend fun deliverSafely(consequence: EntryMigrationPendingConsequence) {
        try {
            deliver(consequence)
            host.acknowledgeConsequence(consequence.id)
            if (consequence.artifactId == EntryMigrationConsequenceArtifact.CUSTOM_COVER) {
                runCatching { customCover.discard(codec.customCover(consequence.payload)) }
            }
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

    private suspend fun deliver(consequence: EntryMigrationPendingConsequence) {
        when (consequence.artifactId) {
            EntryMigrationConsequenceArtifact.PROGRESS -> check(
                progress().applyMigration(codec.progress(consequence.payload)) is EntryProgressRestoreResult.Applied,
            )
            EntryMigrationConsequenceArtifact.PLAYBACK_PREFERENCES -> check(
                playbackPreferences().applyMigration(codec.playback(consequence.payload)) is
                    EntryPlaybackPreferencesRestoreResult.Applied,
            )
            EntryMigrationConsequenceArtifact.VIEWER_SETTINGS -> check(
                viewerSettings().applyMigration(codec.viewerSettings(consequence.payload)) is
                    EntryViewerSettingsRestoreResult.Restored,
            )
            EntryMigrationConsequenceArtifact.DOWNLOAD_REMOVAL -> check(
                downloads().applyRemoval(codec.downloadRemoval(consequence.payload)) ==
                    EntryDownloadMaintenanceResult.Performed,
            ) { "Migration download removal was not verified" }
            EntryMigrationConsequenceArtifact.CUSTOM_COVER -> customCover.promote(
                codec.customCover(consequence.payload),
            )
            else -> error("No Migration consequence handler for ${consequence.artifactId}")
        }
    }

    private companion object {
        const val DEFAULT_BATCH_SIZE = 100
        const val RETRY_DELAY_MILLIS = 60_000L
        const val ORPHAN_MINIMUM_AGE_MILLIS = 24 * 60 * 60 * 1_000L
        const val ORPHAN_CLEANUP_BATCH_SIZE = 100
    }
}
