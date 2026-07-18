package mihon.entry.interactions.anime

import eu.kanade.tachiyomi.source.entry.EntryType
import mihon.entry.interactions.EntryMergeProvider
import mihon.entry.interactions.EntryMigrationProvider

internal class AnimeMigrationMergeProvider : EntryMigrationProvider, EntryMergeProvider {
    override val type: EntryType = EntryType.ANIME
}
