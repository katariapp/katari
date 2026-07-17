package mihon.entry.interactions

import eu.kanade.tachiyomi.source.entry.EntryType
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class EntryDownloadCapabilityPolicyTest {

    @Test
    fun `bookmarked bulk downloads require both fundamental capabilities`() {
        val report = report(
            downloads = setOf(EntryType.MANGA, EntryType.ANIME, EntryType.BOOK),
            bookmarks = setOf(EntryType.MANGA),
        )

        EntryDownloadCapabilityPolicy.supportsBookmarkedBulkDownloads(report, EntryType.MANGA) shouldBe true
        EntryDownloadCapabilityPolicy.supportsBookmarkedBulkDownloads(report, EntryType.ANIME) shouldBe false
        EntryDownloadCapabilityPolicy.supportsBookmarkedBulkDownloads(report, EntryType.BOOK) shouldBe false
    }

    @Test
    fun `new bookmark evidence activates shared single and mixed selection policy`() {
        val report = report(
            downloads = setOf(EntryType.MANGA, EntryType.ANIME),
            bookmarks = setOf(EntryType.MANGA, EntryType.ANIME),
        )

        EntryDownloadCapabilityPolicy.supportsBookmarkedBulkDownloads(report, EntryType.ANIME) shouldBe true
        EntryDownloadCapabilityPolicy.supportsBookmarkedBulkDownloads(
            report,
            listOf(EntryType.MANGA, EntryType.ANIME),
        ) shouldBe true
        EntryDownloadCapabilityPolicy.supportsBookmarkedBulkDownloads(report, emptyList()) shouldBe false
    }

    @Test
    fun `bookmark protection depends on bookmarking rather than downloader implementation`() {
        val report = report(
            downloads = setOf(EntryType.MANGA, EntryType.ANIME),
            bookmarks = setOf(EntryType.ANIME),
        )

        EntryDownloadCapabilityPolicy.protectsBookmarkedDownloads(report, EntryType.MANGA) shouldBe false
        EntryDownloadCapabilityPolicy.protectsBookmarkedDownloads(report, EntryType.ANIME) shouldBe true
    }

    private fun report(
        downloads: Set<EntryType>,
        bookmarks: Set<EntryType>,
    ): EntryCapabilityReport {
        val records = buildList {
            downloads.forEach { add(providerEvidence(it, EntryCapabilityCatalog.DOWNLOADS)) }
            bookmarks.forEach { add(providerEvidence(it, EntryCapabilityCatalog.BOOKMARKING)) }
        }
        return createEntryCapabilityReport(
            registeredTypes = EntryType.entries,
            evidence = EntryCapabilityEvidenceSnapshot(records),
            outcomes = EntryCapabilityOutcomeSnapshot(emptyList()),
        )
    }

    private fun providerEvidence(
        entryType: EntryType,
        capability: EntryFundamentalCapability,
    ): EntryCapabilityEvidenceRecord {
        return EntryCapabilityEvidenceRecord(
            entryType = entryType,
            capability = capability,
            evidence = EntryCapabilityEvidence.ProviderRegistration(
                owner = EntryCapabilityOwner("download-policy-test"),
                provider = "synthetic-provider",
            ),
        )
    }
}
