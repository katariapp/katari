package mihon.entry.interactions.manga

import android.app.Application
import eu.kanade.tachiyomi.source.entry.EntryType
import mihon.entry.interactions.EntryDownloadLifecycleEventSink
import mihon.entry.interactions.EntryImageComponentInstaller
import mihon.entry.interactions.EntryTypeRuntimeContribution
import mihon.entry.interactions.EntryTypeRuntimeModule
import mihon.entry.interactions.manga.download.DownloadCache
import mihon.entry.interactions.manga.download.DownloadManager
import mihon.entry.interactions.manga.download.DownloadProvider
import mihon.entry.interactions.manga.reader.addMangaReaderImageComponents
import mihon.entry.interactions.reader.settings.MangaReaderSettingsProvider
import mihon.entry.interactions.settings.EntryInteractionPreferences
import tachiyomi.core.common.preference.PreferenceStore
import tachiyomi.domain.entry.repository.EntryProgressRepository
import uy.kohesive.injekt.api.InjektRegistrar
import uy.kohesive.injekt.api.addSingletonFactory
import uy.kohesive.injekt.api.get

fun mangaEntryTypeRuntimeModule(profilePreferenceStore: PreferenceStore): EntryTypeRuntimeModule {
    return EntryTypeRuntimeModule(EntryType.MANGA) { app ->
        val warmup = addMangaEntryInteractionRuntime(app)
        val viewerSettingsProvider = MangaReaderSettingsProvider(profilePreferenceStore)
        addSingletonFactory { viewerSettingsProvider }
        val progressRepository = get<EntryProgressRepository>()
        EntryTypeRuntimeContribution(
            plugin = mangaEntryInteractionPlugin(
                MangaEntryInteractionDependencies(
                    getEntryWithChapters = get(),
                    entryChapterRepository = get(),
                    entryProgressRepository = progressRepository,
                    downloadPreferences = get(),
                    sourceManager = get(),
                    downloadLifecycle = get<EntryDownloadLifecycleEventSink>(),
                    entryInteractionPreferences = get<EntryInteractionPreferences>(),
                ),
            ),
            viewerSettingsProviders = listOf(viewerSettingsProvider),
            warmups = listOf(warmup),
            imageComponentInstallers = listOf(EntryImageComponentInstaller(::addMangaReaderImageComponents)),
        )
    }
}

private fun InjektRegistrar.addMangaEntryInteractionRuntime(app: Application): () -> Unit {
    addSingletonFactory { DownloadProvider(app) }
    addSingletonFactory { DownloadManager(app) }
    addSingletonFactory { DownloadCache(app) }

    return { get<DownloadManager>() }
}
