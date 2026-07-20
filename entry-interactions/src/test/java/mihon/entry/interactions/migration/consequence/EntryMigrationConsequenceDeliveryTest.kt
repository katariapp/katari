package mihon.entry.interactions

import eu.kanade.tachiyomi.source.entry.EntryType
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import mihon.entry.interactions.host.EntryMigrationConsequenceHost
import mihon.entry.interactions.host.EntryMigrationPendingConsequence
import org.junit.jupiter.api.Test
import tachiyomi.domain.entry.model.Entry

class EntryMigrationConsequenceDeliveryTest {
    private val owner = Entry.create().copy(id = 7, profileId = 3, type = EntryType.BOOK)
    private val codec = EntryMigrationConsequenceCodec()

    @Test
    fun `download consequence stays durable until every captured owner is verified absent`() = runTest {
        val host = mockk<EntryMigrationConsequenceHost>()
        val downloads = mockk<EntryDownloadMaintenanceFeature>()
        val consequence = consequence(
            artifactId = EntryMigrationConsequenceArtifact.DOWNLOAD_REMOVAL,
            payload = codec.encode(EntryDownloadRemovalPlan(listOf(owner))),
        )
        coEvery { host.pendingConsequences("operation", any()) } returns listOf(consequence)
        coEvery { host.pendingConsequenceCount("operation") } returns 1
        coEvery { host.recordConsequenceFailure(any(), any(), any()) } returns Unit
        coEvery { downloads.applyRemoval(any()) } returns EntryDownloadMaintenanceResult.Incomplete(listOf(owner))
        val delivery = delivery(host, downloads)

        delivery.deliverOperation("operation") shouldBe EntryMigrationFollowUp.INCOMPLETE

        coVerify(exactly = 0) { host.acknowledgeConsequence(any()) }
        coVerify(exactly = 1) { host.recordConsequenceFailure(consequence.id, any(), any()) }
    }

    @Test
    fun `verified download consequence is acknowledged`() = runTest {
        val host = mockk<EntryMigrationConsequenceHost>()
        val downloads = mockk<EntryDownloadMaintenanceFeature>()
        val consequence = consequence(
            artifactId = EntryMigrationConsequenceArtifact.DOWNLOAD_REMOVAL,
            payload = codec.encode(EntryDownloadRemovalPlan(listOf(owner))),
        )
        coEvery { host.pendingConsequences("operation", any()) } returns listOf(consequence)
        coEvery { host.pendingConsequenceCount("operation") } returns 0
        coEvery { host.acknowledgeConsequence(consequence.id) } returns Unit
        coEvery { downloads.applyRemoval(any()) } returns EntryDownloadMaintenanceResult.Performed

        delivery(host, downloads).deliverOperation("operation") shouldBe EntryMigrationFollowUp.COMPLETE

        coVerify(exactly = 1) { host.acknowledgeConsequence(consequence.id) }
        coVerify(exactly = 0) { host.recordConsequenceFailure(any(), any(), any()) }
    }

    private fun delivery(
        host: EntryMigrationConsequenceHost,
        downloads: EntryDownloadMaintenanceFeature,
    ): EntryMigrationConsequenceDelivery {
        return EntryMigrationConsequenceDelivery(
            host = host,
            progress = { mockk() },
            playbackPreferences = { mockk() },
            viewerSettings = { mockk() },
            downloads = { downloads },
            customCover = mockk(),
            codec = codec,
            clockMillis = { 1_000 },
        )
    }

    private fun consequence(artifactId: String, payload: String) = EntryMigrationPendingConsequence(
        id = "operation:$artifactId",
        operationId = "operation",
        profileId = 3,
        artifactId = artifactId,
        payload = payload,
        attempts = 0,
    )
}
