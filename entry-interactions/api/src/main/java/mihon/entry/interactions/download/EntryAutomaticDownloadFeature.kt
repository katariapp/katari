package mihon.entry.interactions

import eu.kanade.tachiyomi.source.entry.EntryType
import tachiyomi.domain.entry.model.Entry
import tachiyomi.domain.entry.model.EntryChapter

/** Feature-owned automatic-download policy and orchestration boundary. */
interface EntryAutomaticDownloadFeature {
    fun isApplicable(type: EntryType): Boolean

    /** Creates one deferred-start batch for a library-update run. */
    fun newLibraryUpdateBatch(): EntryAutomaticDownloadBatch

    /** Applies automatic-download policy and immediately starts accepted work after an Entry refresh. */
    suspend fun downloadAfterEntryRefresh(
        entry: Entry,
        newChapters: List<EntryChapter>,
    ): EntryAutomaticDownloadResult
}

/**
 * Accumulates automatic downloads without starting them while a library update is using the same sources.
 * [complete] starts download processing only when this batch actually queued work.
 */
interface EntryAutomaticDownloadBatch {
    suspend fun enqueue(
        entry: Entry,
        newChapters: List<EntryChapter>,
    ): EntryAutomaticDownloadResult

    fun complete()
}

sealed interface EntryAutomaticDownloadResult {
    data object Inapplicable : EntryAutomaticDownloadResult

    data object NoCandidates : EntryAutomaticDownloadResult

    data class Scheduled(val count: Int) : EntryAutomaticDownloadResult
}
