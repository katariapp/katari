package mihon.entry.interactions

import android.content.Context
import eu.kanade.tachiyomi.source.entry.EntryType
import eu.kanade.tachiyomi.source.entry.UnifiedSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import tachiyomi.domain.entry.model.Entry
import tachiyomi.domain.entry.model.EntryChapter

internal class ProviderBackedEntryPreviewInteraction(
    private val processors: Map<EntryType, EntryPreviewInteraction>,
) : EntryPreviewInteraction {
    override fun isSupported(entry: Entry): Boolean {
        return processors[entry.type]?.isSupported(entry) ?: false
    }

    override fun requiresChapter(entry: Entry): Boolean {
        return processors[entry.type]?.requiresChapter(entry) ?: true
    }

    override fun config(entry: Entry): EntryPreviewConfig {
        return processors[entry.type]?.config(entry) ?: EntryPreviewConfig.Disabled
    }

    override fun configChanges(entry: Entry): Flow<EntryPreviewConfig> {
        return processors[entry.type]?.configChanges(entry) ?: flowOf(EntryPreviewConfig.Disabled)
    }

    override suspend fun loadPreview(
        context: Context,
        entry: Entry,
        chapter: EntryChapter?,
        source: UnifiedSource,
        pageCount: Int,
    ): EntryPreviewHandle {
        val processor = processors.requireProcessor("preview", entry.type)
        return processor.loadPreview(context, entry, chapter, source, pageCount)
    }

    override suspend fun loadPage(handle: EntryPreviewHandle, pageIndex: Int) {
        val processor = processors.requireProcessor("preview", handle.entryType)
        processor.loadPage(handle, pageIndex)
    }

    override fun release(handle: EntryPreviewHandle) {
        processors[handle.entryType]?.release(handle)
    }
}

internal class ProviderBackedEntryImmersiveInteraction(
    private val processors: Map<EntryType, EntryImmersiveProcessor>,
) : EntryImmersiveInteraction {
    override fun isSupported(entry: Entry): Boolean {
        val processor = processors[entry.type] ?: return false
        processor.requireMatchingEntryType("immersive feed", entry, processors.keys)
        return processor.isSupported(entry)
    }

    override fun preloadRadius(entryType: EntryType): Int {
        return processors[entryType]?.preloadRadius(entryType) ?: 0
    }

    override suspend fun load(
        context: Context,
        entry: Entry,
        chapter: EntryChapter,
        source: UnifiedSource,
    ): EntryImmersiveHandle {
        val processor = processors.requireProcessor("immersive feed", entry.type)
        processor.requireMatchingEntryType("immersive feed", entry, processors.keys)
        return processor.load(context, entry, chapter, source)
    }

    override fun renderer(handle: EntryImmersiveHandle): EntryImmersiveRenderer {
        return processors.requireProcessor("immersive feed", handle.entryType).renderer(handle)
    }

    override suspend fun persistProgress(
        handle: EntryImmersiveHandle,
        progress: EntryImmersiveProgress,
    ) {
        processors.requireProcessor("immersive feed", handle.entryType)
            .persistProgress(handle, progress)
    }

    override fun release(handle: EntryImmersiveHandle) {
        processors[handle.entryType]?.release(handle)
    }
}
