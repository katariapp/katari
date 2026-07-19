package mihon.entry.interactions

import eu.kanade.tachiyomi.source.entry.EntryType
import eu.kanade.tachiyomi.source.entry.UnifiedSource
import kotlinx.coroutines.flow.Flow
import mihon.feature.graph.CapabilityId
import tachiyomi.domain.entry.model.Entry
import tachiyomi.domain.entry.model.EntryChapter

enum class EntryImmersiveLoadMode {
    ENTRY,
    FIRST_READING_CHILD,
}

interface EntryImmersiveProcessor : EntryInteractionProvider {
    val loadMode: EntryImmersiveLoadMode
    val preloadRadius: Int

    suspend fun load(
        context: android.content.Context,
        entry: Entry,
        chapter: EntryChapter?,
        source: UnifiedSource,
    ): EntryImmersiveHandle

    fun renderer(handle: EntryImmersiveHandle): EntryImmersiveRenderer

    suspend fun persistProgress(handle: EntryImmersiveHandle, progress: EntryImmersiveProgress)

    fun release(handle: EntryImmersiveHandle)
}

val EntryImmersiveCapability = entryInteractionCapability<EntryImmersiveProcessor>(
    id = CapabilityId("entry.immersive"),
)

interface EntryChildListProcessor : EntryInteractionProvider {
    fun sortedForReading(entry: Entry, chapters: List<EntryChapter>, memberIds: List<Long>): List<EntryChapter>
    fun sortedForDisplay(entry: Entry, chapters: List<EntryChapter>, memberIds: List<Long>): List<EntryChapter>
    fun buildDisplayList(request: EntryChildListRequest): EntryChildListDisplay
}

val EntryChildListCapability = entryInteractionCapability<EntryChildListProcessor>(
    id = CapabilityId("entry.child-list"),
)

interface EntryChildProgressProcessor : EntryInteractionProvider {
    fun progressLabels(request: EntryChildProgressRequest): Flow<Map<Long, EntryChildProgressLabel>>
}

val EntryChildProgressCapability = entryInteractionCapability<EntryChildProgressProcessor>(
    id = CapabilityId("entry.child-progress-labels"),
)

interface EntryChildGroupFilterProcessor : EntryInteractionProvider {
    fun groupFor(entry: Entry, chapter: EntryChapter): String?
    fun normalizeGroup(entry: Entry, group: String): String?
}

val EntryChildGroupFilterCapability = entryInteractionCapability<EntryChildGroupFilterProcessor>(
    id = CapabilityId("entry.child-group-filter"),
)

interface EntryOutsideReleasePeriodFilterProvider : EntryInteractionProvider

val EntryOutsideReleasePeriodFilterCapability = entryInteractionCapability<EntryOutsideReleasePeriodFilterProvider>(
    id = CapabilityId("entry.outside-release-period-filter"),
)

enum class EntryPreviewLoadMode {
    ENTRY,
    FIRST_READING_CHILD,
}

sealed interface EntryPreviewContextResult {
    data object Available : EntryPreviewContextResult
    data class Unavailable(val reason: EntryPreviewUnavailableReason) : EntryPreviewContextResult
}

interface EntryPreviewProcessor : EntryInteractionProvider {
    val loadMode: EntryPreviewLoadMode

    fun contextAvailability(entry: Entry, source: UnifiedSource): EntryPreviewContextResult

    suspend fun loadPreview(
        context: android.content.Context,
        entry: Entry,
        chapter: EntryChapter?,
        source: UnifiedSource,
        pageCount: Int,
    ): EntryPreviewHandle

    suspend fun loadPage(handle: EntryPreviewHandle, pageIndex: Int)
    fun release(handle: EntryPreviewHandle)
}

val EntryPreviewCapability = entryInteractionCapability<EntryPreviewProcessor>(
    id = CapabilityId("entry.preview"),
)

interface EntryPreviewConfigurationProvider : EntryInteractionProvider {
    val settings: EntryPreviewSettings
    fun config(): EntryPreviewConfig
    fun configChanges(): Flow<EntryPreviewConfig>
}

val EntryPreviewConfigurationCapability = entryInteractionCapability<EntryPreviewConfigurationProvider>(
    id = CapabilityId("entry.preview.configuration"),
)

interface EntryTypePresentationProvider : EntryInteractionProvider {
    val presentation: EntryTypePresentation
}

val EntryTypePresentationCapability = entryInteractionCapability<EntryTypePresentationProvider>(
    id = CapabilityId("entry.type-presentation"),
)
