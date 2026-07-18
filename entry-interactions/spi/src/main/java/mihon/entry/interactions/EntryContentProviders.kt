package mihon.entry.interactions

import eu.kanade.tachiyomi.source.entry.EntryType
import kotlinx.coroutines.flow.Flow
import mihon.feature.graph.CapabilityId
import tachiyomi.domain.entry.model.Entry
import tachiyomi.domain.entry.model.EntryChapter

interface EntryImmersiveProcessor : EntryImmersiveInteraction, EntryInteractionProvider {
    override fun preloadRadius(entryType: EntryType): Int
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
    fun availableGroupsChanged(entryId: Long): Flow<Unit>
    suspend fun availableGroups(entry: Entry, memberIds: Collection<Long>): Set<String>
    fun excludedGroupsChanged(entryId: Long): Flow<Unit>
    suspend fun excludedGroups(entry: Entry, memberIds: Collection<Long>): Set<String>
    suspend fun setExcludedGroups(entry: Entry, memberIds: Collection<Long>, excluded: Set<String>)
}

val EntryChildGroupFilterCapability = entryInteractionCapability<EntryChildGroupFilterProcessor>(
    id = CapabilityId("entry.child-group-filter"),
)

interface EntryOutsideReleasePeriodFilterProvider : EntryInteractionProvider

val EntryOutsideReleasePeriodFilterCapability = entryInteractionCapability<EntryOutsideReleasePeriodFilterProvider>(
    id = CapabilityId("entry.outside-release-period-filter"),
)

interface EntryPreviewProcessor : EntryPreviewInteraction, EntryInteractionProvider

val EntryPreviewCapability = entryInteractionCapability<EntryPreviewProcessor>(
    id = CapabilityId("entry.preview"),
)
