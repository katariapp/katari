package mihon.entry.interactions

import android.content.Context
import eu.kanade.tachiyomi.source.entry.EntryType
import eu.kanade.tachiyomi.source.entry.UnifiedSource
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import tachiyomi.domain.entry.model.Entry
import tachiyomi.domain.entry.model.EntryChapter
import tachiyomi.domain.entry.service.sortedForReading

internal class EntryDownloadInteractionDispatch(
    private val processors: Map<EntryType, EntryDownloadProcessor>,
    private val optionsProcessors: Map<EntryType, EntryDownloadOptionsProcessor>,
    private val settingCapabilities: Map<EntryType, Set<EntryDownloadSettingCapability>>,
    private val bulkCandidateProcessors: Map<EntryType, EntryBulkDownloadCandidateProcessor>,
    private val automaticFilterProcessors: Map<EntryType, EntryAutomaticDownloadFilterProcessor>,
    private val bookmarkProviderTypes: Set<EntryType>,
) : EntryDownloadInteraction {
    private val paused = MutableStateFlow(false)

    override val changes: Flow<Unit> = processors.values.map { it.changes }.merged()
    override val isInitializing: Flow<Boolean> = processors.values.map { it.isInitializing }.combinedAny()
    override val isRunning: Flow<Boolean> = processors.values.map { it.isRunning }.combinedAny()
    override val isPaused: Flow<Boolean> = paused.asStateFlow()
    override val queueState: Flow<List<EntryDownloadQueueGroup>> = processors.values
        .map { it.queueState }
        .combinedFlatten()

    override fun updates(): Flow<EntryDownloadStatus> {
        return processors.values.map { it.updates() }.merged()
    }

    override fun queueStatusUpdates(): Flow<EntryDownloadQueueItem> {
        return processors.values.map { it.queueStatusUpdates() }.merged()
    }

    override fun queueProgressUpdates(): Flow<EntryDownloadQueueItem> {
        return processors.values.map { it.queueProgressUpdates() }.merged()
    }

    override fun events(): Flow<EntryDownloadEvent> {
        return processors.values.map { it.events }.merged()
    }

    override suspend fun runDownloadsUntilIdle() = coroutineScope {
        processors.values
            .map { processor -> async { processor.runDownloadsUntilIdle() } }
            .awaitAll()
        Unit
    }

    override fun startDownloads() {
        paused.value = false
        processors.values.forEach { it.startDownloads() }
    }

    override fun pauseDownloads() {
        processors.values.forEach { it.pauseDownloads() }
        paused.value = true
    }

    override fun clearQueue() {
        processors.values.forEach { it.clearQueue() }
        paused.value = false
    }

    override fun invalidateCaches() {
        processors.values.forEach { it.invalidateCache() }
    }

    override fun renameSource(oldSource: UnifiedSource, newSource: UnifiedSource) {
        processors.values.forEach { it.renameSource(oldSource, newSource) }
    }

    override suspend fun renameEntry(entry: Entry, newTitle: String) {
        val processor = processors[entry.type] ?: return
        processor.requireMatchingEntryType("download", entry, processors.keys)
        processor.renameEntry(entry, newTitle)
    }

    override fun reorderQueue(items: List<EntryDownloadQueueItem>) {
        items.groupBy { it.entryType }
            .forEach { (type, typedItems) ->
                processors.requireProcessor("download", type).reorderQueue(typedItems)
            }
    }

    override fun reorderSeries(entryType: EntryType, entryId: Long, moveToTop: Boolean) {
        processors.requireProcessor("download", entryType).reorderSeries(entryId, moveToTop)
    }

    override fun cancelQueuedDownloads(items: List<EntryDownloadQueueItem>) {
        items.groupBy { it.entryType }
            .forEach { (type, typedItems) ->
                processors.requireProcessor("download", type).cancelQueuedDownloads(typedItems)
            }
    }

    override fun settingCapabilities(): Map<EntryType, Set<EntryDownloadSettingCapability>> {
        return settingCapabilities
    }

    override suspend fun queue(entry: Entry, chapters: List<EntryChapter>, autoStart: Boolean) {
        val processor = processors.requireProcessor("download", entry.type)
        processor.requireMatchingEntryType("download", entry, processors.keys)
        processor.queue(entry, chapters, autoStart)
        if (autoStart) paused.value = false
    }

    override suspend fun download(entry: Entry, chapters: List<EntryChapter>, startNow: Boolean) {
        val processor = processors.requireProcessor("download", entry.type)
        processor.requireMatchingEntryType("download", entry, processors.keys)
        processor.download(entry, chapters, startNow)
        paused.value = false
    }

    override suspend fun downloadWithOptions(
        entry: Entry,
        chapters: List<EntryChapter>,
        selection: EntryDownloadOptionSelection,
        startNow: Boolean,
    ) {
        val processor = optionsProcessors.requireProcessor("download options", entry.type)
        processor.requireMatchingEntryType("download options", entry, optionsProcessors.keys)
        processor.downloadWithOptions(entry, chapters, selection, startNow)
        paused.value = false
    }

    override fun supportsDownloadOptions(entry: Entry): Boolean {
        val processor = optionsProcessors[entry.type] ?: return false
        processor.requireMatchingEntryType("download options", entry, optionsProcessors.keys)
        return true
    }

    override suspend fun resolveDownloadOptions(
        context: Context,
        entry: Entry,
        chapter: EntryChapter,
    ): EntryDownloadOptions? {
        val processor = optionsProcessors[entry.type] ?: return null
        processor.requireMatchingEntryType("download options", entry, optionsProcessors.keys)
        return processor.resolveDownloadOptions(context, entry, chapter)
    }

    override suspend fun resolveBulkDownloadCandidates(
        entry: Entry,
        action: EntryBulkDownloadAction,
        candidates: List<EntryChapter>?,
        memberEntryIds: List<Long>,
    ): EntryBulkDownloadCandidateResult {
        val processor = bulkCandidateProcessors[entry.type] ?: return EntryBulkDownloadCandidateResult.Unsupported
        processor.requireMatchingEntryType("bulk download candidates", entry, bulkCandidateProcessors.keys)
        if (
            action.type == EntryBulkDownloadActionType.BOOKMARKED &&
            entry.type !in bookmarkProviderTypes
        ) {
            return EntryBulkDownloadCandidateResult.Unsupported
        }
        val pool = processor.resolveBulkDownloadCandidatePool(entry, candidates)
        return EntryBulkDownloadCandidateResult.Supported(
            pool.selectBulkDownloadCandidates(entry, action, memberEntryIds),
        )
    }

    override suspend fun filterAutoDownloadCandidates(
        entry: Entry,
        chapters: List<EntryChapter>,
    ): List<EntryChapter> {
        val processor = automaticFilterProcessors[entry.type] ?: return emptyList()
        processor.requireMatchingEntryType("automatic download filter", entry, automaticFilterProcessors.keys)
        return processor.filterAutoDownloadCandidates(entry, chapters)
    }

    override suspend fun delete(entry: Entry, chapters: List<EntryChapter>) {
        val processor = processors[entry.type] ?: return
        processor.requireMatchingEntryType("download", entry, processors.keys)
        processor.delete(entry, chapters)
    }

    override suspend fun cleanup(entry: Entry, chapters: List<EntryChapter>) {
        val processor = processors[entry.type] ?: return
        processor.requireMatchingEntryType("download", entry, processors.keys)
        processor.cleanup(entry, chapters)
    }

    override suspend fun deleteEntryDownloads(entry: Entry) {
        val processor = processors[entry.type] ?: return
        processor.requireMatchingEntryType("download", entry, processors.keys)
        processor.deleteEntryDownloads(entry)
    }

    override fun hasDownloads(entry: Entry): Boolean {
        val processor = processors[entry.type] ?: return false
        processor.requireMatchingEntryType("download", entry, processors.keys)
        return processor.hasDownloads(entry)
    }

    override fun getDownloadCount(entry: Entry): Int {
        val processor = processors[entry.type] ?: return 0
        processor.requireMatchingEntryType("download", entry, processors.keys)
        return processor.getDownloadCount(entry)
    }

    override fun getTotalDownloadCount(): Int {
        return processors.values.sumOf { it.getTotalDownloadCount() }
    }

    override fun isDownloaded(entry: Entry, chapter: EntryChapter, skipCache: Boolean): Boolean {
        val processor = processors[entry.type] ?: return false
        processor.requireMatchingEntryType("download", entry, processors.keys)
        return processor.isDownloaded(entry, chapter, skipCache)
    }

    override fun getStatus(
        entryType: EntryType,
        chapterId: Long,
        chapterName: String,
        chapterScanlator: String?,
        chapterUrl: String,
        entryTitle: String,
        sourceId: Long,
    ): EntryDownloadStatus {
        val processor = processors[entryType]
            ?: return EntryDownloadStatus(entryType, chapterId, EntryDownloadState.NOT_DOWNLOADED)
        return processor.getStatus(
            chapterId = chapterId,
            chapterName = chapterName,
            chapterScanlator = chapterScanlator,
            chapterUrl = chapterUrl,
            entryTitle = entryTitle,
            sourceId = sourceId,
        )
    }

    override fun cancelQueuedDownload(entryType: EntryType, chapterId: Long): EntryDownloadStatus? {
        return processors[entryType]?.cancelQueuedDownload(chapterId)
    }
}

private fun List<EntryChapter>.selectBulkDownloadCandidates(
    entry: Entry,
    action: EntryBulkDownloadAction,
    memberEntryIds: List<Long>,
): List<EntryChapter> {
    return when (action.type) {
        EntryBulkDownloadActionType.NEXT -> filterNot(EntryChapter::read)
            .sortedForReading(entry, memberEntryIds.ifEmpty { map(EntryChapter::entryId).distinct() })
            .let { chapters -> action.limit?.let(chapters::take) ?: chapters }
        EntryBulkDownloadActionType.UNREAD -> filterNot(EntryChapter::read)
        EntryBulkDownloadActionType.BOOKMARKED -> filter(EntryChapter::bookmark)
    }
}
