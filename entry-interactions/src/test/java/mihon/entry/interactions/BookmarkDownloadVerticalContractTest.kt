package mihon.entry.interactions

import eu.kanade.tachiyomi.source.entry.EntryType
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import tachiyomi.core.common.preference.InMemoryPreferenceStore
import tachiyomi.domain.category.interactor.GetCategories
import tachiyomi.domain.download.service.DownloadPreferences
import tachiyomi.domain.entry.interactor.GetEntryWithChapters
import tachiyomi.domain.entry.model.Entry
import tachiyomi.domain.entry.model.EntryChapter
import tachiyomi.domain.entry.repository.EntryRepository

class BookmarkDownloadVerticalContractTest {

    @Test
    fun `one Anime bookmark registration activates every shared bookmark download consequence`() = runTest {
        val anime = entry()
        val normal = chapter(id = 11L, bookmark = false)
        val bookmarked = chapter(id = 12L, bookmark = true, read = true)
        val downloadProcessor = animeDownloadProcessor()
        val bookmarkProcessor = mockk<EntryBookmarkProcessor>(relaxed = true) {
            every { type } returns EntryType.ANIME
            every { canSetBookmarked(any(), any()) } returns true
        }
        val downloadPlugin = EntryInteractionPlugin { it.registerDownloadProcessor(downloadProcessor) }
        val syntheticBookmarkRegistration = EntryInteractionPlugin {
            it.registerBookmarkProcessor(bookmarkProcessor)
        }

        val composition = createEntryInteractionComposition(
            listOf(downloadPlugin, syntheticBookmarkRegistration),
        )
        val interactions = composition.interactions
        val report = composition.capabilityReport

        report.supportsTypeWide(EntryType.ANIME, EntryCapabilityCatalog.BOOKMARKING) shouldBe true
        report.type(EntryType.ANIME)
            .entry(EntryCapabilityCatalog.BOOKMARKING)
            .value.shouldBeInstanceOf<EntryCapabilityReportValue.Outcome>()
            .result.shouldBeInstanceOf<EntrySupportResult.Supported>()
        EntryDownloadCapabilityPolicy.supportsBookmarkedBulkDownloads(report, EntryType.ANIME) shouldBe true
        report.types.filter {
            EntryDownloadCapabilityPolicy.supportsBookmarkedBulkDownloads(report, it.entryType)
        }.map { it.entryType } shouldBe listOf(EntryType.ANIME)

        val status = EntryBookmarkStatus(bookmarked = false)
        interactions.bookmark.canSetBookmarked(EntryType.ANIME, status, bookmarked = true) shouldBe true
        interactions.bookmark.setBookmarked(anime, listOf(normal), bookmarked = true)
        coVerify(exactly = 1) { bookmarkProcessor.setBookmarked(anime, listOf(normal), true) }

        interactions.download.resolveBulkDownloadCandidates(
            entry = anime,
            action = EntryBulkDownloadAction.bookmarked,
            candidates = listOf(normal, bookmarked),
        ) shouldBe EntryBulkDownloadCandidateResult.Supported(listOf(bookmarked))
        coVerify(exactly = 1) {
            downloadProcessor.resolveBulkDownloadCandidatePool(anime, listOf(normal, bookmarked))
        }

        val preferences = DownloadPreferences(InMemoryPreferenceStore()).apply {
            removeAfterMarkedAsRead.set(true)
        }
        val manager = EntryDownloadLifecycleManager(
            downloadPreferences = preferences,
            getCategories = mockk<GetCategories> {
                coEvery { await(any()) } returns emptyList()
            },
            getEntryWithChapters = mockk<GetEntryWithChapters>(relaxed = true),
            entryRepository = mockk<EntryRepository>(relaxed = true),
            downloadInteraction = { interactions.download },
            capabilityReport = { report },
        )

        manager.onEvent(
            EntryDownloadLifecycleEvent.MarkedConsumed(
                visibleEntry = anime,
                children = listOf(normal, bookmarked),
            ),
        )

        coVerify(exactly = 1) { downloadProcessor.delete(anime, listOf(normal)) }
    }

    private fun animeDownloadProcessor(): EntryDownloadProcessor {
        return mockk(relaxed = true) {
            every { type } returns EntryType.ANIME
            every { settingCapabilities } returns emptySet()
            every { changes } returns emptyFlow()
            every { isInitializing } returns flowOf(false)
            every { isRunning } returns flowOf(false)
            every { queueState } returns flowOf(emptyList())
            every { events } returns emptyFlow()
            every { updates() } returns emptyFlow()
            every { queueStatusUpdates() } returns emptyFlow()
            every { queueProgressUpdates() } returns emptyFlow()
            coEvery { resolveBulkDownloadCandidatePool(any(), any()) } answers {
                secondArg<List<EntryChapter>?>().orEmpty()
            }
        }
    }

    private fun entry(): Entry = Entry.create().copy(
        id = 1L,
        profileId = 7L,
        source = 10L,
        type = EntryType.ANIME,
    )

    private fun chapter(id: Long, bookmark: Boolean, read: Boolean = false): EntryChapter {
        return EntryChapter.create().copy(
            id = id,
            entryId = 1L,
            chapterNumber = id.toDouble(),
            sourceOrder = id,
            bookmark = bookmark,
            read = read,
        )
    }
}
