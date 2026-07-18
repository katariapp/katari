package mihon.entry.interactions

import android.app.PendingIntent
import android.content.Context
import eu.kanade.tachiyomi.source.entry.EntryType
import eu.kanade.tachiyomi.source.entry.UnifiedSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import mihon.feature.graph.CapabilityId
import mihon.feature.graph.ContentTypeContribution
import mihon.feature.graph.ContentTypeId
import mihon.feature.graph.ContributionOwner
import mihon.feature.graph.FeatureGraphContributionSink
import mihon.feature.graph.FeatureGraphContributor
import mihon.feature.graph.capabilityDefinition
import tachiyomi.domain.entry.model.Entry
import tachiyomi.domain.entry.model.EntryChapter

interface EntryInteractionProvider {
    val type: EntryType
}

interface EntryInteractionDispatchProvider : EntryInteractionProvider {
    fun install(registry: EntryInteractionRegistry)
}

interface EntryOpenProcessor : EntryInteractionDispatchProvider {
    override fun install(registry: EntryInteractionRegistry) {
        registry.registerOpenProcessor(this)
    }

    fun open(context: Context, entry: Entry, chapter: EntryChapter, options: EntryOpenOptions)
    fun pendingIntent(context: Context, entry: Entry, chapter: EntryChapter, options: EntryOpenOptions): PendingIntent
}

interface EntryContinueProcessor : EntryInteractionDispatchProvider {
    override fun install(registry: EntryInteractionRegistry) {
        registry.registerContinueProcessor(this)
    }

    suspend fun findNext(entry: Entry): EntryChapter?
    fun open(context: Context, entry: Entry, chapter: EntryChapter)
}

private val ENTRY_INTERACTION_CONTRACT_OWNER = ContributionOwner("entry-interactions")

fun EntryType.toContentTypeId(): ContentTypeId = ContentTypeId(name.lowercase())

val EntryOpenCapability = capabilityDefinition<EntryOpenProcessor>(
    id = CapabilityId("entry.open"),
    owner = ENTRY_INTERACTION_CONTRACT_OWNER,
)

val EntryContinueCapability = capabilityDefinition<EntryContinueProcessor>(
    id = CapabilityId("entry.continue"),
    owner = ENTRY_INTERACTION_CONTRACT_OWNER,
)

interface EntryDownloadProcessor : EntryInteractionProvider {
    val settingCapabilities: Set<EntryDownloadSettingCapability> get() = emptySet()
    val changes: Flow<Unit>
    val isInitializing: Flow<Boolean>
    val isRunning: Flow<Boolean>
    val queueState: Flow<List<EntryDownloadQueueGroup>>
    val events: Flow<EntryDownloadEvent>

    fun updates(): Flow<EntryDownloadStatus>
    fun queueStatusUpdates(): Flow<EntryDownloadQueueItem>
    fun queueProgressUpdates(): Flow<EntryDownloadQueueItem>

    /** Runs this media-specific downloader until its current queue is idle. */
    suspend fun runDownloadsUntilIdle()

    fun startDownloads()
    fun pauseDownloads()
    fun clearQueue()
    fun invalidateCache()
    fun renameSource(oldSource: UnifiedSource, newSource: UnifiedSource)
    suspend fun renameEntry(entry: Entry, newTitle: String) = Unit

    /** Reorders pending work without interrupting an unrelated active download. */
    fun reorderQueue(items: List<EntryDownloadQueueItem>)
    fun reorderSeries(entryId: Long, moveToTop: Boolean)

    /** Cancels only the selected work. Pending-item cancellation must not restart active work. */
    fun cancelQueuedDownloads(items: List<EntryDownloadQueueItem>)

    /** Adds work to the queue and starts processing when [autoStart] is true. */
    suspend fun queue(entry: Entry, chapters: List<EntryChapter>, autoStart: Boolean)

    /**
     * Adds work and starts processing it. When [startNow] is true, the new work is promoted ahead of
     * other pending work without interrupting an active download.
     */
    suspend fun download(entry: Entry, chapters: List<EntryChapter>, startNow: Boolean)
    suspend fun downloadWithOptions(
        entry: Entry,
        chapters: List<EntryChapter>,
        selection: EntryDownloadOptionSelection,
        startNow: Boolean,
    ) {
        download(entry, chapters, startNow)
    }
    fun supportsDownloadOptions(entry: Entry): Boolean = false
    suspend fun resolveDownloadOptions(
        context: Context,
        entry: Entry,
        chapter: EntryChapter,
    ): EntryDownloadOptions? = null

    /** Loads media-specific candidates before shared bulk-action selection is applied. */
    suspend fun resolveBulkDownloadCandidatePool(
        entry: Entry,
        candidates: List<EntryChapter>? = null,
    ): List<EntryChapter>
    suspend fun filterAutoDownloadCandidates(entry: Entry, chapters: List<EntryChapter>): List<EntryChapter>
    suspend fun delete(entry: Entry, chapters: List<EntryChapter>)
    suspend fun cleanup(entry: Entry, chapters: List<EntryChapter>) = delete(entry, chapters)
    suspend fun deleteEntryDownloads(entry: Entry)

    fun hasDownloads(entry: Entry): Boolean
    fun getDownloadCount(entry: Entry): Int
    fun getTotalDownloadCount(): Int
    fun isDownloaded(entry: Entry, chapter: EntryChapter, skipCache: Boolean = false): Boolean
    fun getStatus(
        chapterId: Long,
        chapterName: String,
        chapterScanlator: String?,
        chapterUrl: String,
        entryTitle: String,
        sourceId: Long,
    ): EntryDownloadStatus
    fun cancelQueuedDownload(chapterId: Long): EntryDownloadStatus?
}

interface EntryCapabilityProcessor : EntryInteractionProvider {

    fun supportsMigration(entry: Entry): Boolean = false

    fun supportsMerge(entry: Entry): Boolean = false
}

interface EntryConsumptionProcessor : EntryInteractionProvider {

    fun canSetConsumed(status: EntryConsumptionStatus, consumed: Boolean): Boolean {
        return when (consumed) {
            true -> !status.consumed
            false -> status.consumed || status.hasPartialProgress
        }
    }

    suspend fun setConsumed(entry: Entry, chapters: List<EntryChapter>, consumed: Boolean)
}

interface EntryBookmarkProcessor : EntryInteractionProvider {

    fun canSetBookmarked(status: EntryBookmarkStatus, bookmarked: Boolean): Boolean {
        return status.bookmarked != bookmarked
    }

    suspend fun setBookmarked(entry: Entry, chapters: List<EntryChapter>, bookmarked: Boolean)
}

interface EntryUpdateEligibilityProcessor : EntryInteractionProvider {
    fun evaluate(request: EntryUpdateEligibilityRequest): EntryUpdateEligibility
}

interface EntryProgressProcessor : EntryInteractionProvider {
    suspend fun snapshot(entry: Entry): EntryProgressSnapshot
    suspend fun restore(entry: Entry, snapshot: EntryProgressSnapshot)
    suspend fun copy(
        sourceEntry: Entry,
        targetEntry: Entry,
        resourceMappings: List<EntryProgressResourceMapping>,
    )
}

interface EntryPlaybackPreferencesProcessor : EntryInteractionProvider {
    suspend fun snapshot(entry: Entry): EntryPlaybackPreferencesSnapshot?
    suspend fun restore(entry: Entry, snapshot: EntryPlaybackPreferencesSnapshot)
    suspend fun copy(sourceEntry: Entry, targetEntry: Entry)
}

interface EntryImmersiveProcessor : EntryImmersiveInteraction, EntryInteractionProvider {
    override fun preloadRadius(entryType: EntryType): Int
}

interface EntryChildListProcessor : EntryInteractionProvider {
    fun sortedForReading(entry: Entry, chapters: List<EntryChapter>, memberIds: List<Long>): List<EntryChapter>
    fun sortedForDisplay(entry: Entry, chapters: List<EntryChapter>, memberIds: List<Long>): List<EntryChapter>
    fun buildDisplayList(request: EntryChildListRequest): List<EntryChildListRow>
    fun progressLabels(
        request: EntryChildProgressRequest,
    ): Flow<Map<Long, EntryChildProgressLabel>> = flowOf(emptyMap())
}

interface EntryChildGroupFilterProcessor : EntryInteractionProvider {

    fun supports(entry: Entry): Boolean
    fun shouldApplyFilter(entry: Entry): Boolean
    fun availableGroupsChanged(entryId: Long): Flow<Unit>
    suspend fun availableGroups(entry: Entry, memberIds: Collection<Long>): Set<String>
    fun excludedGroupsChanged(entryId: Long): Flow<Unit>
    suspend fun excludedGroups(entry: Entry, memberIds: Collection<Long>): Set<String>
    suspend fun setExcludedGroups(entry: Entry, memberIds: Collection<Long>, excluded: Set<String>)
}

interface EntryLibraryFilterProcessor : EntryInteractionProvider {
    fun supportsOutsideReleasePeriodFilter(entry: Entry): Boolean
}

interface EntryPreviewProcessor : EntryPreviewInteraction, EntryInteractionProvider

interface EntryInteractionPlugin : FeatureGraphContributor {
    val type: EntryType
    val contentTypeContribution: ContentTypeContribution

    override val owner: ContributionOwner
        get() = contentTypeContribution.owner

    override fun contributeTo(sink: FeatureGraphContributionSink) {
        sink.add(contentTypeContribution)
    }

    fun validateContribution() {
        require(contentTypeContribution.contentType == type.toContentTypeId()) {
            "Entry interaction plugin $type must contribute content type ${type.toContentTypeId()}, not " +
                contentTypeContribution.contentType
        }
        contentTypeContribution.providers.forEach { provider ->
            val interactionProvider = provider.implementation as? EntryInteractionProvider ?: return@forEach
            require(interactionProvider.type == type) {
                "Entry interaction plugin $type cannot contribute ${provider.capability.id} for " +
                    interactionProvider.type
            }
        }
    }

    fun installContributedProviders(registry: EntryInteractionRegistry) {
        contentTypeContribution.providers.forEach { provider ->
            (provider.implementation as? EntryInteractionDispatchProvider)?.install(registry)
        }
    }

    fun register(registry: EntryInteractionRegistry) {
        installContributedProviders(registry)
    }
}

interface EntryInteractionRegistry {
    fun registerOpenProcessor(processor: EntryOpenProcessor)
    fun registerContinueProcessor(processor: EntryContinueProcessor)
    fun registerDownloadProcessor(processor: EntryDownloadProcessor)
    fun registerCapabilityProcessor(processor: EntryCapabilityProcessor)
    fun registerConsumptionProcessor(processor: EntryConsumptionProcessor)
    fun registerBookmarkProcessor(processor: EntryBookmarkProcessor)
    fun registerUpdateEligibilityProcessor(processor: EntryUpdateEligibilityProcessor)
    fun registerProgressProcessor(processor: EntryProgressProcessor)
    fun registerPlaybackPreferencesProcessor(processor: EntryPlaybackPreferencesProcessor)
    fun registerChildListProcessor(processor: EntryChildListProcessor)
    fun registerChildGroupFilterProcessor(processor: EntryChildGroupFilterProcessor)
    fun registerLibraryFilterProcessor(processor: EntryLibraryFilterProcessor)
    fun registerPreviewProcessor(processor: EntryPreviewProcessor)
    fun registerImmersiveProcessor(processor: EntryImmersiveProcessor)
}
