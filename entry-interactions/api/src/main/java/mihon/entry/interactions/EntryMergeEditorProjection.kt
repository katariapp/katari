package mihon.entry.interactions

import eu.kanade.tachiyomi.source.entry.EntryType
import tachiyomi.domain.entry.model.Entry

/** Purpose-specific projection for Merge editors. It is not a reusable membership query. */
data class EntryMergeEditorProjection(
    val editReference: EntryMergeEditReference,
    val profileId: Long,
    val type: EntryType,
    val entries: List<EntryMergeEditorEntry>,
    val targetEntryId: Long,
    val targetLocked: Boolean,
)

data class EntryMergeEditorEntry(
    val entry: Entry,
    val origin: EntryMergeEditorEntryOrigin,
    val removable: Boolean,
    val removableFromLibrary: Boolean,
)

enum class EntryMergeEditorEntryOrigin {
    SELECTED,
    EXISTING_MEMBER,
    NEW_MEMBER,
}
