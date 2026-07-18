package mihon.entry.interactions.manga

import eu.kanade.tachiyomi.source.entry.EntryType
import mihon.entry.interactions.EntryMergeProvider
import mihon.entry.interactions.EntryMigrationProvider

internal class MangaMigrationMergeProvider : EntryMigrationProvider, EntryMergeProvider {
    override val type: EntryType = EntryType.MANGA
}
