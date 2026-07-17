package mihon.entry.interactions.manga.download

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.MutableStateFlow
import mihon.entry.interactions.manga.download.model.DownloadState
import mihon.entry.interactions.manga.download.model.MangaDownload
import org.junit.jupiter.api.Test
import tachiyomi.domain.category.interactor.GetCategories
import tachiyomi.domain.download.service.DownloadPreferences
import tachiyomi.domain.entry.model.Entry
import tachiyomi.domain.entry.model.EntryChapter
import tachiyomi.domain.source.service.SourceManager

class DownloadManagerTest {

    @Test
    fun `cancelling pending work does not interrupt an unrelated active download`() {
        val active = download(chapterId = 1L, status = DownloadState.DOWNLOADING)
        val pending = download(chapterId = 2L, status = DownloadState.QUEUE)
        val queue = MutableStateFlow(listOf(active, pending))
        val downloader = mockk<Downloader>(relaxed = true) {
            every { queueState } returns queue
            every { isRunning } returns true
            every { removeFromQueue(listOf(pending.chapter)) } answers {
                queue.value = listOf(active)
            }
        }
        val manager = DownloadManager(
            context = mockk(relaxed = true),
            provider = mockk(),
            cache = mockk(),
            getCategories = mockk<GetCategories>(),
            sourceManager = mockk<SourceManager>(),
            downloadPreferences = mockk<DownloadPreferences>(),
            downloader = downloader,
            pendingDeleter = mockk<DownloadPendingDeleter>(),
        )

        manager.cancelQueuedDownloads(listOf(pending))

        verify(exactly = 1) { downloader.removeFromQueue(listOf(pending.chapter)) }
        verify(exactly = 0) { downloader.pause() }
        verify(exactly = 0) { downloader.start() }
        verify(exactly = 0) { downloader.stop(any()) }
    }

    private fun download(chapterId: Long, status: DownloadState): MangaDownload {
        val download = MangaDownload(
            source = mockk(relaxed = true),
            entry = Entry.create().copy(id = 1L),
            chapter = EntryChapter.create().copy(id = chapterId, entryId = 1L),
        )
        download.status = status
        return download
    }
}
