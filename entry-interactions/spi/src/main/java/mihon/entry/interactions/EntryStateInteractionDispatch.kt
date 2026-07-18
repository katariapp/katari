package mihon.entry.interactions

import eu.kanade.tachiyomi.source.entry.EntryType
import tachiyomi.domain.entry.model.Entry
import tachiyomi.domain.entry.model.EntryChapter
import tachiyomi.domain.entry.model.EntryStatus

internal class ProviderBackedEntryCapabilityInteraction(
    private val migrationProviders: Map<EntryType, EntryMigrationProvider>,
    private val mergeProviders: Map<EntryType, EntryMergeProvider>,
) : EntryCapabilityInteraction {
    override fun supportsMigration(entry: Entry): Boolean {
        val provider = migrationProviders[entry.type] ?: return false
        provider.requireMatchingEntryType("migration", entry, migrationProviders.keys)
        return true
    }

    override fun canMigrate(entries: List<Entry>): Boolean {
        return entries.isNotEmpty() && entries.all(::supportsMigration)
    }

    override fun migrationEntries(entries: List<Entry>): List<Entry> {
        return entries.filter(::supportsMigration)
    }

    override fun supportsMerge(entry: Entry): Boolean {
        val provider = mergeProviders[entry.type] ?: return false
        provider.requireMatchingEntryType("merge", entry, mergeProviders.keys)
        return true
    }

    override fun canMergeSelection(selection: List<EntryMergeCapabilityItem>): Boolean {
        if (selection.size < 2) return false
        if (selection.map { it.entry.type }.distinct().size != 1) return false
        if (selection.count { it.isMerged } > 1) return false

        return selection.all { supportsMerge(it.entry) }
    }
}

internal class ProviderBackedEntryConsumptionInteraction(
    private val consumptionProcessors: Map<EntryType, EntryConsumptionProcessor>,
) : EntryConsumptionInteraction {
    override fun canSetConsumed(entryType: EntryType, status: EntryConsumptionStatus, consumed: Boolean): Boolean {
        val processor = consumptionProcessors.requireProcessor("consumption", entryType)
        return processor.canSetConsumed(status, consumed)
    }

    override suspend fun setConsumed(entry: Entry, chapters: List<EntryChapter>, consumed: Boolean) {
        val processor = consumptionProcessors.requireProcessor("consumption", entry.type)
        processor.requireMatchingEntryType("consumption", entry, consumptionProcessors.keys)
        processor.setConsumed(entry, chapters, consumed)
    }
}

internal class ProviderBackedEntryBookmarkInteraction(
    private val bookmarkProcessors: Map<EntryType, EntryBookmarkProcessor>,
) : EntryBookmarkInteraction {
    override fun canSetBookmarked(
        entryType: EntryType,
        status: EntryBookmarkStatus,
        bookmarked: Boolean,
    ): Boolean {
        return bookmarkProcessors[entryType]?.canSetBookmarked(status, bookmarked) ?: false
    }

    override suspend fun setBookmarked(entry: Entry, chapters: List<EntryChapter>, bookmarked: Boolean) {
        val processor = bookmarkProcessors[entry.type] ?: return
        processor.requireMatchingEntryType("bookmark", entry, bookmarkProcessors.keys)
        processor.setBookmarked(entry, chapters, bookmarked)
    }
}

internal class ProviderBackedEntryUpdateEligibilityInteraction : EntryUpdateEligibilityInteraction {
    override fun evaluate(request: EntryUpdateEligibilityRequest): EntryUpdateEligibility {
        val fetchWindowUpperBound = request.fetchWindowUpperBound
        return when {
            EntryUpdateRestriction.NON_COMPLETED in request.restrictions &&
                request.entry.status == EntryStatus.COMPLETED -> {
                EntryUpdateEligibility.Skipped(EntryUpdateSkipReason.COMPLETED)
            }
            EntryUpdateRestriction.HAS_UNCONSUMED in request.restrictions && request.unconsumedCount != 0L -> {
                EntryUpdateEligibility.Skipped(EntryUpdateSkipReason.NOT_CAUGHT_UP)
            }
            EntryUpdateRestriction.NON_STARTED in request.restrictions &&
                request.totalCount > 0L &&
                !request.hasStarted -> {
                EntryUpdateEligibility.Skipped(EntryUpdateSkipReason.NOT_STARTED)
            }
            EntryUpdateRestriction.OUTSIDE_RELEASE_PERIOD in request.restrictions &&
                fetchWindowUpperBound != null &&
                request.entry.nextUpdate > fetchWindowUpperBound -> {
                EntryUpdateEligibility.Skipped(EntryUpdateSkipReason.OUTSIDE_RELEASE_PERIOD)
            }
            else -> EntryUpdateEligibility.Eligible
        }
    }
}

internal class ProviderBackedEntryProgressInteraction(
    private val processors: Map<EntryType, EntryProgressProcessor>,
) : EntryProgressInteraction {
    override suspend fun snapshot(entry: Entry): EntryProgressSnapshot {
        val processor = processors[entry.type] ?: return EntryProgressSnapshot()
        processor.requireMatchingEntryType("progress", entry, processors.keys)
        return processor.snapshot(entry)
    }

    override suspend fun restore(entry: Entry, snapshot: EntryProgressSnapshot) {
        val processor = processors[entry.type] ?: return
        processor.requireMatchingEntryType("progress", entry, processors.keys)
        processor.restore(entry, snapshot)
    }

    override suspend fun copy(
        sourceEntry: Entry,
        targetEntry: Entry,
        resourceMappings: List<EntryProgressResourceMapping>,
    ) {
        if (sourceEntry.type != targetEntry.type) return
        val processor = processors[sourceEntry.type] ?: return
        processor.requireMatchingEntryType("progress", sourceEntry, processors.keys)
        processor.requireMatchingEntryType("progress", targetEntry, processors.keys)
        processor.copy(sourceEntry, targetEntry, resourceMappings)
    }
}

internal class ProviderBackedEntryPlaybackPreferencesInteraction(
    private val processors: Map<EntryType, EntryPlaybackPreferencesProcessor>,
) : EntryPlaybackPreferencesInteraction {
    override suspend fun snapshot(entry: Entry): EntryPlaybackPreferencesSnapshot? {
        val processor = processors[entry.type] ?: return null
        processor.requireMatchingEntryType("playback preferences", entry, processors.keys)
        return processor.snapshot(entry)
    }

    override suspend fun restore(entry: Entry, snapshot: EntryPlaybackPreferencesSnapshot) {
        val processor = processors[entry.type] ?: return
        processor.requireMatchingEntryType("playback preferences", entry, processors.keys)
        processor.restore(entry, snapshot)
    }

    override suspend fun copy(sourceEntry: Entry, targetEntry: Entry) {
        if (sourceEntry.type != targetEntry.type) return
        val processor = processors[sourceEntry.type] ?: return
        processor.requireMatchingEntryType("playback preferences", sourceEntry, processors.keys)
        processor.requireMatchingEntryType("playback preferences", targetEntry, processors.keys)
        processor.copy(sourceEntry, targetEntry)
    }
}
