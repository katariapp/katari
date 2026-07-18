package mihon.entry.interactions

import android.content.Context
import eu.kanade.tachiyomi.source.entry.EntryType
import eu.kanade.tachiyomi.source.entry.UnifiedSource
import kotlinx.coroutines.flow.Flow
import tachiyomi.domain.entry.model.Entry
import tachiyomi.domain.entry.model.EntryChapter

interface EntryPreviewInteraction {
    fun isSupported(entry: Entry): Boolean
    fun requiresChapter(entry: Entry): Boolean
    fun config(entry: Entry): EntryPreviewConfig
    fun configChanges(entry: Entry): Flow<EntryPreviewConfig>
    suspend fun loadPreview(
        context: Context,
        entry: Entry,
        chapter: EntryChapter?,
        source: UnifiedSource,
        pageCount: Int,
    ): EntryPreviewHandle
    suspend fun loadPage(handle: EntryPreviewHandle, pageIndex: Int)
    fun release(handle: EntryPreviewHandle)
}

interface EntryImmersiveInteraction {
    fun isSupported(entry: Entry): Boolean

    fun preloadRadius(entryType: EntryType): Int

    suspend fun load(
        context: Context,
        entry: Entry,
        chapter: EntryChapter,
        source: UnifiedSource,
    ): EntryImmersiveHandle

    fun renderer(handle: EntryImmersiveHandle): EntryImmersiveRenderer

    suspend fun persistProgress(handle: EntryImmersiveHandle, progress: EntryImmersiveProgress)

    fun release(handle: EntryImmersiveHandle)
}
