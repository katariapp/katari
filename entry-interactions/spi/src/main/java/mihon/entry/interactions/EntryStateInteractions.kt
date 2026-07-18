package mihon.entry.interactions

import eu.kanade.tachiyomi.source.entry.EntryType
import tachiyomi.domain.entry.model.Entry
import tachiyomi.domain.entry.model.EntryChapter

interface EntryCapabilityInteraction {
    fun supportsMigration(entry: Entry): Boolean
    fun canMigrate(entries: List<Entry>): Boolean
    fun migrationEntries(entries: List<Entry>): List<Entry>
    fun supportsMerge(entry: Entry): Boolean
    fun canMergeSelection(selection: List<EntryMergeCapabilityItem>): Boolean
}

interface EntryConsumptionInteraction {
    fun canSetConsumed(entryType: EntryType, status: EntryConsumptionStatus, consumed: Boolean): Boolean
    suspend fun setConsumed(entry: Entry, chapters: List<EntryChapter>, consumed: Boolean)
}

interface EntryBookmarkInteraction {
    fun canSetBookmarked(entryType: EntryType, status: EntryBookmarkStatus, bookmarked: Boolean): Boolean
    suspend fun setBookmarked(entry: Entry, chapters: List<EntryChapter>, bookmarked: Boolean)
}

interface EntryUpdateEligibilityInteraction {
    fun evaluate(request: EntryUpdateEligibilityRequest): EntryUpdateEligibility
}

interface EntryProgressInteraction {
    suspend fun snapshot(entry: Entry): EntryProgressSnapshot

    suspend fun restore(entry: Entry, snapshot: EntryProgressSnapshot)

    suspend fun copy(
        sourceEntry: Entry,
        targetEntry: Entry,
        resourceMappings: List<EntryProgressResourceMapping>,
    )
}

interface EntryPlaybackPreferencesInteraction {
    suspend fun snapshot(entry: Entry): EntryPlaybackPreferencesSnapshot?
    suspend fun restore(entry: Entry, snapshot: EntryPlaybackPreferencesSnapshot)
    suspend fun copy(sourceEntry: Entry, targetEntry: Entry)
}
