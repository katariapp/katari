package eu.kanade.tachiyomi.ui.updates

import eu.kanade.tachiyomi.source.entry.EntryType
import io.kotest.matchers.shouldBe
import mihon.entry.interactions.EntryBookmarkStatus
import mihon.entry.interactions.EntryCapabilityCatalog
import mihon.entry.interactions.EntryCapabilityEvidence
import mihon.entry.interactions.EntryCapabilityEvidenceRecord
import mihon.entry.interactions.EntryCapabilityEvidenceSnapshot
import mihon.entry.interactions.EntryCapabilityOutcomeSnapshot
import mihon.entry.interactions.EntryCapabilityOwner
import mihon.entry.interactions.EntryCapabilityReport
import mihon.entry.interactions.EntryConsumptionStatus
import mihon.entry.interactions.EntryDownloadState
import mihon.entry.interactions.createEntryCapabilityReport
import org.junit.jupiter.api.Test
import tachiyomi.domain.entry.model.EntryCover
import tachiyomi.domain.updates.model.UpdateItem
import tachiyomi.domain.updates.model.UpdatesWithRelations

class UpdatesSelectionActionsTest {

    private val productionBookmarkReport = bookmarkReport(EntryType.MANGA)

    @Test
    fun `bookmark action is only available for manga selection`() {
        val mangaOnly = listOf(updateItem(6, EntryType.MANGA, bookmark = false))
        val animeOnly = listOf(updateItem(7, EntryType.ANIME, bookmark = false))
        val mixed = listOf(
            updateItem(8, EntryType.MANGA, bookmark = false),
            updateItem(9, EntryType.ANIME, bookmark = false),
        )

        mangaOnly.hasBookmarkAction(
            bookmark = true,
            capabilityReport = productionBookmarkReport,
            canSetBookmarked = ::canSetBookmarked,
        ) shouldBe true
        animeOnly.hasBookmarkAction(
            bookmark = true,
            capabilityReport = productionBookmarkReport,
            canSetBookmarked = ::canSetBookmarked,
        ) shouldBe false
        mixed.hasBookmarkAction(
            bookmark = true,
            capabilityReport = productionBookmarkReport,
            canSetBookmarked = ::canSetBookmarked,
        ) shouldBe false
    }

    @Test
    fun `remove bookmark action is only available for fully bookmarked manga selection`() {
        val bookmarkedManga = listOf(
            updateItem(10, EntryType.MANGA, bookmark = true),
            updateItem(11, EntryType.MANGA, bookmark = true),
        )
        val partiallyBookmarkedManga = listOf(
            updateItem(12, EntryType.MANGA, bookmark = true),
            updateItem(13, EntryType.MANGA, bookmark = false),
        )
        val mixed = listOf(
            updateItem(14, EntryType.MANGA, bookmark = true),
            updateItem(15, EntryType.ANIME, bookmark = true),
        )

        bookmarkedManga.hasBookmarkAction(
            bookmark = false,
            capabilityReport = productionBookmarkReport,
            canSetBookmarked = ::canSetBookmarked,
        ) shouldBe true
        partiallyBookmarkedManga.hasBookmarkAction(
            bookmark = false,
            capabilityReport = productionBookmarkReport,
            canSetBookmarked = ::canSetBookmarked,
        ) shouldBe false
        mixed.hasBookmarkAction(
            bookmark = false,
            capabilityReport = productionBookmarkReport,
            canSetBookmarked = ::canSetBookmarked,
        ) shouldBe false
    }

    @Test
    fun `anime bookmark evidence activates the shared selection action`() {
        val animeOnly = listOf(updateItem(20, EntryType.ANIME, bookmark = false))
        val syntheticReport = bookmarkReport(EntryType.MANGA, EntryType.ANIME)

        animeOnly.hasBookmarkAction(
            bookmark = true,
            capabilityReport = syntheticReport,
            canSetBookmarked = ::canSetBookmarked,
        ) shouldBe true
    }

    @Test
    fun `mark consumed actions use entry interaction capability`() {
        val unreadAnime = listOf(updateItem(16, EntryType.ANIME, read = false))
        val readAnime = listOf(updateItem(17, EntryType.ANIME, read = true))
        val partialAnime = listOf(updateItem(18, EntryType.ANIME, read = false, started = true))
        val partialManga = listOf(updateItem(19, EntryType.MANGA, read = false, started = true))
        val partialBook = listOf(updateItem(20, EntryType.BOOK, read = false, started = true))

        unreadAnime.hasConsumedAction(consumed = true, canSetConsumed = ::canSetConsumed) shouldBe true
        readAnime.hasConsumedAction(consumed = true, canSetConsumed = ::canSetConsumed) shouldBe false
        readAnime.hasConsumedAction(consumed = false, canSetConsumed = ::canSetConsumed) shouldBe true
        partialAnime.hasConsumedAction(consumed = false, canSetConsumed = ::canSetConsumed) shouldBe true
        partialManga.hasConsumedAction(consumed = false, canSetConsumed = ::canSetConsumed) shouldBe true
        partialBook.hasConsumedAction(consumed = false, canSetConsumed = ::canSetConsumed) shouldBe true
    }

    private fun canSetBookmarked(
        @Suppress("UNUSED_PARAMETER") entryType: EntryType,
        status: EntryBookmarkStatus,
        bookmarked: Boolean,
    ): Boolean {
        return status.bookmarked != bookmarked
    }

    private fun bookmarkReport(vararg supportedTypes: EntryType): EntryCapabilityReport {
        return createEntryCapabilityReport(
            registeredTypes = EntryType.entries,
            evidence = EntryCapabilityEvidenceSnapshot(
                supportedTypes.map { entryType ->
                    EntryCapabilityEvidenceRecord(
                        entryType = entryType,
                        capability = EntryCapabilityCatalog.BOOKMARKING,
                        evidence = EntryCapabilityEvidence.ProviderRegistration(
                            owner = EntryCapabilityOwner("updates-test"),
                            provider = "bookmark-provider",
                        ),
                    )
                },
            ),
            outcomes = EntryCapabilityOutcomeSnapshot(emptyList()),
        )
    }

    private fun canSetConsumed(
        @Suppress("UNUSED_PARAMETER") entryType: EntryType,
        status: EntryConsumptionStatus,
        consumed: Boolean,
    ): Boolean {
        return when (consumed) {
            true -> !status.consumed
            false -> status.consumed || status.hasPartialProgress
        }
    }

    private fun updateItem(
        chapterId: Long,
        entryType: EntryType,
        read: Boolean = false,
        bookmark: Boolean = false,
        started: Boolean = read,
    ): UpdatesItem {
        val update = UpdatesWithRelations(
            entryId = chapterId * 10,
            entryType = entryType,
            entryTitle = "Entry $chapterId",
            chapterId = chapterId,
            chapterName = "Chapter $chapterId",
            scanlator = null,
            chapterUrl = "/chapter/$chapterId",
            read = read,
            bookmark = bookmark,
            started = started,
            progressPosition = 0,
            sourceId = 1,
            dateFetch = 0,
            coverData = EntryCover(
                entryId = chapterId * 10,
                sourceId = 1,
                isFavorite = true,
                url = null,
                lastModified = 0,
            ),
        )

        return UpdatesItem(
            update = UpdateItem.EntryUpdate(update, entryType),
            visibleEntryId = update.entryId,
            visibleEntryTitle = update.entryTitle,
            visibleCoverData = update.coverData,
            downloadStateProvider = { EntryDownloadState.NOT_DOWNLOADED },
            downloadProgressProvider = { 0 },
            selected = true,
        )
    }
}
