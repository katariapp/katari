package mihon.entry.interactions

import kotlinx.coroutines.flow.Flow
import tachiyomi.domain.entry.model.Entry
import tachiyomi.domain.entry.model.EntryChapter

interface EntryChildListInteraction {
    fun sortedForReading(entry: Entry, chapters: List<EntryChapter>, memberIds: List<Long>): List<EntryChapter>
    fun sortedForDisplay(entry: Entry, chapters: List<EntryChapter>, memberIds: List<Long>): List<EntryChapter>
    fun buildDisplayList(request: EntryChildListRequest): List<EntryChildListRow>
    fun progressLabels(request: EntryChildProgressRequest): Flow<Map<Long, EntryChildProgressLabel>>
}

interface EntryChildGroupFilterInteraction {
    fun supports(entry: Entry): Boolean
    fun shouldApplyFilter(entry: Entry): Boolean
    fun availableGroupsChanged(entryId: Long): Flow<Unit>
    suspend fun availableGroups(entry: Entry, memberIds: Collection<Long>): Set<String>
    fun excludedGroupsChanged(entryId: Long): Flow<Unit>
    suspend fun excludedGroups(entry: Entry, memberIds: Collection<Long>): Set<String>
    suspend fun setExcludedGroups(entry: Entry, memberIds: Collection<Long>, excluded: Set<String>)
}

interface EntryLibraryFilterInteraction {
    fun supportsOutsideReleasePeriodFilter(entry: Entry): Boolean
}
