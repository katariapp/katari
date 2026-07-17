package mihon.entry.interactions

import eu.kanade.tachiyomi.source.entry.EntryType
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import tachiyomi.core.common.preference.InMemoryPreferenceStore
import tachiyomi.domain.category.interactor.GetCategories
import tachiyomi.domain.category.model.Category
import tachiyomi.domain.download.service.DownloadPreferences
import tachiyomi.domain.entry.interactor.GetEntryWithChapters
import tachiyomi.domain.entry.model.Entry
import tachiyomi.domain.entry.model.EntryChapter
import tachiyomi.domain.entry.repository.EntryRepository

class EntryDownloadLifecycleManagerTest {
    @Test
    fun `marked consumed cleanup applies owner categories and bookmark protection for every type`() = runTest {
        EntryType.entries.forEach { type ->
            val visible = entry(id = 1L, type = type)
            val member = entry(id = 2L, type = type, sourceId = 20L)
            val normal = chapter(id = 11L, entryId = visible.id)
            val bookmarked = chapter(id = 12L, entryId = visible.id, bookmark = true)
            val excludedMember = chapter(id = 21L, entryId = member.id)
            val fixture = fixture(
                memberEntries = listOf(member),
                categories = mapOf(member.id to listOf(category(9L))),
            )
            fixture.preferences.removeAfterMarkedAsRead.set(true)
            fixture.preferences.removeExcludeCategories.set(setOf("9"))

            fixture.manager.onEvent(
                EntryDownloadLifecycleEvent.MarkedConsumed(
                    visibleEntry = visible,
                    children = listOf(normal, bookmarked, excludedMember),
                ),
            )

            coVerify(exactly = 1) { fixture.downloads.delete(visible, listOf(normal)) }
            coVerify(exactly = 0) { fixture.downloads.delete(member, any()) }
        }
    }

    @Test
    fun `completion cleanup follows shared reading order and configured slots`() = runTest {
        val visible = entry(id = 1L, type = EntryType.BOOK)
        val previous = chapter(id = 11L, entryId = visible.id, number = 1.0)
        val current = chapter(id = 12L, entryId = visible.id, number = 2.0)
        val fixture = fixture(readingOrder = listOf(current, previous))
        fixture.preferences.removeAfterReadSlots.set(1)

        fixture.manager.onEvent(EntryDownloadLifecycleEvent.Completed(visible, current))

        coVerify(exactly = 1) { fixture.downloads.cleanup(visible, listOf(previous)) }
    }

    @Test
    fun `download ahead queues missing children once after continuity is established`() = runTest {
        val visible = entry(id = 1L, type = EntryType.ANIME)
        val current = chapter(id = 11L, entryId = visible.id, number = 1.0)
        val next = chapter(id = 12L, entryId = visible.id, number = 2.0)
        val third = chapter(id = 13L, entryId = visible.id, number = 3.0)
        val fourth = chapter(id = 14L, entryId = visible.id, number = 4.0)
        val fixture = fixture(readingOrder = listOf(fourth, third, next, current))
        fixture.preferences.autoDownloadWhileReading.set(2)
        every { fixture.downloads.isDownloaded(visible, current, any()) } returns true
        every { fixture.downloads.isDownloaded(visible, next, any()) } returns true
        every { fixture.downloads.isDownloaded(visible, third, any()) } returns false
        every { fixture.downloads.isDownloaded(visible, fourth, any()) } returns false
        val event = EntryDownloadLifecycleEvent.Progressed(visible, current, fraction = 0.5)

        fixture.manager.onEvent(event)
        fixture.manager.onEvent(event)

        coVerify(exactly = 1) {
            fixture.downloads.queue(visible, listOf(third, fourth), autoStart = false)
        }
        verify(exactly = 1) { fixture.downloads.startDownloads() }
    }

    @Test
    fun `download ahead honors a viewer duplicate filtering capability`() = runTest {
        val visible = entry(id = 1L, type = EntryType.MANGA)
        val current = chapter(id = 11L, entryId = visible.id, number = 1.0, scanlator = "A")
        val currentDuplicate = chapter(id = 12L, entryId = visible.id, number = 1.0, scanlator = "B")
        val next = chapter(id = 21L, entryId = visible.id, number = 2.0, scanlator = "A")
        val nextDuplicate = chapter(id = 22L, entryId = visible.id, number = 2.0, scanlator = "B")
        val missing = chapter(id = 31L, entryId = visible.id, number = 3.0, scanlator = "A")
        val fixture = fixture(readingOrder = listOf(missing, nextDuplicate, next, currentDuplicate, current))
        fixture.preferences.autoDownloadWhileReading.set(1)
        every { fixture.downloads.isDownloaded(visible, current, any()) } returns true
        every { fixture.downloads.isDownloaded(visible, next, any()) } returns true
        every { fixture.downloads.isDownloaded(visible, missing, any()) } returns false

        fixture.manager.onEvent(
            EntryDownloadLifecycleEvent.Progressed(
                visibleEntry = visible,
                child = current,
                fraction = 0.5,
                deduplicateByNumber = true,
            ),
        )

        coVerify(exactly = 1) { fixture.downloads.queue(visible, listOf(missing), autoStart = false) }
    }

    private fun fixture(
        memberEntries: List<Entry> = emptyList(),
        readingOrder: List<EntryChapter> = emptyList(),
        categories: Map<Long, List<Category>> = emptyMap(),
    ): Fixture {
        val preferences = DownloadPreferences(InMemoryPreferenceStore())
        val downloads = mockk<EntryDownloadInteraction>(relaxed = true)
        val entryRepository = mockk<EntryRepository>(relaxed = true) {
            coEvery { getEntryById(any()) } answers {
                memberEntries.firstOrNull { it.id == firstArg<Long>() }
            }
        }
        val getCategories = mockk<GetCategories> {
            coEvery { await(any()) } answers { categories[firstArg<Long>()].orEmpty() }
        }
        val getEntryWithChapters = mockk<GetEntryWithChapters> {
            coEvery { awaitChapters(any(), any()) } returns readingOrder
        }
        return Fixture(
            preferences = preferences,
            downloads = downloads,
            manager = EntryDownloadLifecycleManager(
                downloadPreferences = preferences,
                getCategories = getCategories,
                getEntryWithChapters = getEntryWithChapters,
                entryRepository = entryRepository,
                downloadInteraction = { downloads },
            ),
        )
    }

    private fun entry(id: Long, type: EntryType, sourceId: Long = 10L): Entry = Entry.create().copy(
        id = id,
        profileId = 7L,
        source = sourceId,
        type = type,
    )

    private fun chapter(
        id: Long,
        entryId: Long,
        number: Double = 1.0,
        bookmark: Boolean = false,
        scanlator: String? = null,
    ): EntryChapter = EntryChapter.create().copy(
        id = id,
        entryId = entryId,
        chapterNumber = number,
        sourceOrder = id,
        bookmark = bookmark,
        scanlator = scanlator,
    )

    private fun category(id: Long) = Category(id = id, name = "Category $id", order = id, flags = 0L)

    private data class Fixture(
        val preferences: DownloadPreferences,
        val downloads: EntryDownloadInteraction,
        val manager: EntryDownloadLifecycleManager,
    )
}
