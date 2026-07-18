package mihon.entry.interactions

import eu.kanade.tachiyomi.source.entry.EntryType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import tachiyomi.domain.entry.model.Entry
import tachiyomi.domain.entry.model.EntryChapter
import tachiyomi.domain.entry.service.sortedForReading

internal class ProviderBackedEntryChildListInteraction(
    private val processors: Map<EntryType, EntryChildListProcessor>,
    private val progressProcessors: Map<EntryType, EntryChildProgressProcessor>,
) : EntryChildListInteraction {
    override fun sortedForReading(
        entry: Entry,
        chapters: List<EntryChapter>,
        memberIds: List<Long>,
    ): List<EntryChapter> {
        val processor = processors.requireProcessor("child list", entry.type)
        processor.requireMatchingEntryType("child list", entry, processors.keys)
        return processor.sortedForReading(entry, chapters, memberIds)
    }

    override fun sortedForDisplay(
        entry: Entry,
        chapters: List<EntryChapter>,
        memberIds: List<Long>,
    ): List<EntryChapter> {
        val processor = processors.requireProcessor("child list", entry.type)
        processor.requireMatchingEntryType("child list", entry, processors.keys)
        return processor.sortedForDisplay(entry, chapters, memberIds)
    }

    override fun buildDisplayList(request: EntryChildListRequest): List<EntryChildListRow> {
        val processor = processors.requireProcessor("child list", request.entry.type)
        processor.requireMatchingEntryType("child list", request.entry, processors.keys)
        return processor.buildDisplayList(request)
    }

    override fun progressLabels(request: EntryChildProgressRequest): Flow<Map<Long, EntryChildProgressLabel>> {
        val processor = progressProcessors[request.entry.type] ?: return flowOf(emptyMap())
        processor.requireMatchingEntryType("child progress", request.entry, progressProcessors.keys)
        return processor.progressLabels(request)
    }
}

internal class ProviderBackedEntryChildGroupFilterInteraction(
    private val processors: Map<EntryType, EntryChildGroupFilterProcessor>,
) : EntryChildGroupFilterInteraction {
    override fun supports(entry: Entry): Boolean {
        val processor = processors[entry.type] ?: return false
        processor.requireMatchingEntryType("child group filter", entry, processors.keys)
        return true
    }

    override fun shouldApplyFilter(entry: Entry): Boolean {
        val processor = processors[entry.type] ?: return false
        processor.requireMatchingEntryType("child group filter", entry, processors.keys)
        return true
    }

    override fun availableGroupsChanged(entryId: Long): Flow<Unit> {
        return processors.values.map { it.availableGroupsChanged(entryId) }.merged()
    }

    override suspend fun availableGroups(entry: Entry, memberIds: Collection<Long>): Set<String> {
        val processor = processors[entry.type] ?: return emptySet()
        processor.requireMatchingEntryType("child group filter", entry, processors.keys)
        return processor.availableGroups(entry, memberIds)
    }

    override fun excludedGroupsChanged(entryId: Long): Flow<Unit> {
        return processors.values.map { it.excludedGroupsChanged(entryId) }.merged()
    }

    override suspend fun excludedGroups(entry: Entry, memberIds: Collection<Long>): Set<String> {
        val processor = processors[entry.type] ?: return emptySet()
        processor.requireMatchingEntryType("child group filter", entry, processors.keys)
        return processor.excludedGroups(entry, memberIds)
    }

    override suspend fun setExcludedGroups(entry: Entry, memberIds: Collection<Long>, excluded: Set<String>) {
        val processor = processors[entry.type] ?: return
        processor.requireMatchingEntryType("child group filter", entry, processors.keys)
        processor.setExcludedGroups(entry, memberIds, excluded)
    }
}

internal class ProviderBackedEntryLibraryFilterInteraction(
    private val providers: Map<EntryType, EntryOutsideReleasePeriodFilterProvider>,
) : EntryLibraryFilterInteraction {
    override fun supportsOutsideReleasePeriodFilter(entry: Entry): Boolean {
        val provider = providers[entry.type] ?: return false
        provider.requireMatchingEntryType("outside release period filter", entry, providers.keys)
        return true
    }
}
