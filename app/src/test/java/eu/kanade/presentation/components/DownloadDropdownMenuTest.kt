package eu.kanade.presentation.components

import eu.kanade.presentation.entry.DownloadAction
import eu.kanade.tachiyomi.source.entry.EntryType
import io.kotest.matchers.collections.shouldContainExactly
import mihon.entry.interactions.EntryCapabilityCatalog
import mihon.entry.interactions.EntryCapabilityEvidence
import mihon.entry.interactions.EntryCapabilityEvidenceRecord
import mihon.entry.interactions.EntryCapabilityEvidenceSnapshot
import mihon.entry.interactions.EntryCapabilityOutcomeSnapshot
import mihon.entry.interactions.EntryCapabilityOwner
import mihon.entry.interactions.EntryDownloadCapabilityPolicy
import mihon.entry.interactions.EntryFundamentalCapability
import mihon.entry.interactions.createEntryCapabilityReport
import org.junit.jupiter.api.Test

class DownloadDropdownMenuTest {

    @Test
    fun `menu actions follow capability-derived bookmarked download policy`() {
        val productionReport = report(bookmarks = setOf(EntryType.MANGA))
        val animeWithBookmarksReport = report(bookmarks = setOf(EntryType.MANGA, EntryType.ANIME))

        downloadActions(
            EntryDownloadCapabilityPolicy.supportsBookmarkedBulkDownloads(productionReport, EntryType.ANIME),
        ).shouldContainExactly(baseActions)
        downloadActions(
            EntryDownloadCapabilityPolicy.supportsBookmarkedBulkDownloads(animeWithBookmarksReport, EntryType.ANIME),
        ).shouldContainExactly(baseActions + DownloadAction.BOOKMARKED_CHAPTERS)
    }

    private fun report(bookmarks: Set<EntryType>) = createEntryCapabilityReport(
        registeredTypes = EntryType.entries,
        evidence = EntryCapabilityEvidenceSnapshot(
            buildList {
                EntryType.entries.forEach { add(providerEvidence(it, EntryCapabilityCatalog.DOWNLOADS)) }
                bookmarks.forEach { add(providerEvidence(it, EntryCapabilityCatalog.BOOKMARKING)) }
            },
        ),
        outcomes = EntryCapabilityOutcomeSnapshot(emptyList()),
    )

    private fun providerEvidence(
        entryType: EntryType,
        capability: EntryFundamentalCapability,
    ): EntryCapabilityEvidenceRecord {
        return EntryCapabilityEvidenceRecord(
            entryType = entryType,
            capability = capability,
            evidence = EntryCapabilityEvidence.ProviderRegistration(
                owner = EntryCapabilityOwner("download-menu-test"),
                provider = "synthetic-provider",
            ),
        )
    }

    private companion object {
        val baseActions = listOf(
            DownloadAction.NEXT_1_CHAPTER,
            DownloadAction.NEXT_5_CHAPTERS,
            DownloadAction.NEXT_10_CHAPTERS,
            DownloadAction.NEXT_25_CHAPTERS,
            DownloadAction.UNREAD_CHAPTERS,
        )
    }
}
