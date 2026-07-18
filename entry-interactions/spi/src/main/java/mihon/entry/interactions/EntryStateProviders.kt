package mihon.entry.interactions

import mihon.feature.graph.CapabilityId
import tachiyomi.domain.entry.model.Entry
import tachiyomi.domain.entry.model.EntryChapter

interface EntryMigrationProvider : EntryInteractionProvider

interface EntryMergeProvider : EntryInteractionProvider

val EntryMigrationCapability = entryInteractionCapability<EntryMigrationProvider>(
    id = CapabilityId("entry.migration"),
)

val EntryMergeCapability = entryInteractionCapability<EntryMergeProvider>(
    id = CapabilityId("entry.merge"),
)

interface EntryConsumptionProcessor : EntryInteractionProvider {

    fun canSetConsumed(status: EntryConsumptionStatus, consumed: Boolean): Boolean {
        return when (consumed) {
            true -> !status.consumed
            false -> status.consumed || status.hasPartialProgress
        }
    }

    suspend fun setConsumed(entry: Entry, chapters: List<EntryChapter>, consumed: Boolean)
}

val EntryConsumptionCapability = entryInteractionCapability<EntryConsumptionProcessor>(
    id = CapabilityId("entry.consumption"),
)

interface EntryBookmarkProcessor : EntryInteractionProvider {

    fun canSetBookmarked(status: EntryBookmarkStatus, bookmarked: Boolean): Boolean {
        return status.bookmarked != bookmarked
    }

    suspend fun setBookmarked(entry: Entry, chapters: List<EntryChapter>, bookmarked: Boolean)
}

val EntryBookmarkCapability = entryInteractionCapability<EntryBookmarkProcessor>(
    id = CapabilityId("entry.bookmarking"),
)

interface EntryProgressProcessor : EntryInteractionProvider {
    suspend fun snapshot(entry: Entry): EntryProgressSnapshot
    suspend fun restore(entry: Entry, snapshot: EntryProgressSnapshot)
    suspend fun copy(
        sourceEntry: Entry,
        targetEntry: Entry,
        resourceMappings: List<EntryProgressResourceMapping>,
    )
}

val EntryProgressCapability = entryInteractionCapability<EntryProgressProcessor>(
    id = CapabilityId("entry.progress-transfer"),
)

interface EntryPlaybackPreferencesProcessor : EntryInteractionProvider {
    suspend fun snapshot(entry: Entry): EntryPlaybackPreferencesSnapshot?
    suspend fun restore(entry: Entry, snapshot: EntryPlaybackPreferencesSnapshot)
    suspend fun copy(sourceEntry: Entry, targetEntry: Entry)
}

val EntryPlaybackPreferencesCapability = entryInteractionCapability<EntryPlaybackPreferencesProcessor>(
    id = CapabilityId("entry.playback-preferences-transfer"),
)
