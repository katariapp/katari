package mihon.entry.interactions

import tachiyomi.domain.category.interactor.GetCategories
import tachiyomi.domain.download.service.DownloadPreferences
import tachiyomi.domain.entry.model.Entry
import tachiyomi.domain.entry.model.EntryChapter
import tachiyomi.domain.entry.repository.EntryChapterRepository

internal class EntryAutomaticDownloadPolicy(
    private val entryChapterRepository: EntryChapterRepository,
    private val downloadPreferences: DownloadPreferences,
    private val getCategories: GetCategories,
) {
    suspend fun select(entry: Entry, newChapters: List<EntryChapter>): List<EntryChapter> {
        if (
            newChapters.isEmpty() ||
            !downloadPreferences.downloadNewEntryChapters.get() ||
            !entry.shouldDownloadNewChapters()
        ) {
            return emptyList()
        }

        if (!downloadPreferences.downloadNewUnreadEntryChaptersOnly.get()) return newChapters

        val consumedChapterNumbers = entryChapterRepository.getChaptersByEntryIdAwait(entry.id)
            .asSequence()
            .filter { it.read && it.isRecognizedNumber }
            .map { it.chapterNumber }
            .toSet()

        return newChapters.filterNot { it.chapterNumber in consumedChapterNumbers }
    }

    private suspend fun Entry.shouldDownloadNewChapters(): Boolean {
        if (!favorite) return false

        val categories = getCategories.await(id).map { it.id }.ifEmpty { listOf(DEFAULT_CATEGORY_ID) }
        val includedCategories = downloadPreferences.downloadNewEntryChapterCategories.get().map { it.toLong() }
        val excludedCategories = downloadPreferences.downloadNewEntryChapterCategoriesExclude.get().map { it.toLong() }

        return when {
            includedCategories.isEmpty() && excludedCategories.isEmpty() -> true
            categories.any { it in excludedCategories } -> false
            includedCategories.isEmpty() -> true
            else -> categories.any { it in includedCategories }
        }
    }

    private companion object {
        const val DEFAULT_CATEGORY_ID = 0L
    }
}
