package mihon.entry.interactions.anime

import android.app.Application
import eu.kanade.tachiyomi.source.entry.EntryType
import mihon.entry.interactions.EntryDownloadLifecycleInteraction
import mihon.entry.interactions.EntryMediaCacheBucket
import mihon.entry.interactions.EntryMediaCacheBucketKeys
import mihon.entry.interactions.EntryPlayerCache
import mihon.entry.interactions.EntryTypeRuntimeContribution
import mihon.entry.interactions.EntryTypeRuntimeModule
import mihon.entry.interactions.anime.download.AnimeDownloadCache
import mihon.entry.interactions.anime.download.AnimeDownloadManager
import mihon.entry.interactions.anime.download.AnimeDownloadProvider
import mihon.entry.interactions.anime.download.AnimeDownloader
import mihon.entry.interactions.settings.AnimePlayerPreferences
import mihon.entry.interactions.settings.EntryInteractionPreferences
import tachiyomi.core.common.preference.PreferenceStore
import tachiyomi.domain.entry.repository.EntryProgressRepository
import uy.kohesive.injekt.api.InjektRegistrar
import uy.kohesive.injekt.api.addSingletonFactory
import uy.kohesive.injekt.api.get

fun animeEntryTypeRuntimeModule(profilePreferenceStore: PreferenceStore): EntryTypeRuntimeModule {
    return EntryTypeRuntimeModule(EntryType.ANIME) { app ->
        val warmup = addAnimeEntryInteractionRuntime(app)
        val viewerSettingsProvider = AnimePlayerPreferences(profilePreferenceStore)
        addSingletonFactory { viewerSettingsProvider }
        val progressRepository = get<EntryProgressRepository>()
        EntryTypeRuntimeContribution(
            plugin = animeEntryInteractionPlugin(
                AnimeEntryInteractionDependencies(
                    entryChapterRepository = get(),
                    getEntryWithChapters = get(),
                    entryProgressRepository = progressRepository,
                    playbackPreferencesRepository = get(),
                    downloadPreferences = get(),
                    filterEntryChaptersForDownload = get(),
                    downloadPreferencesRepository = get(),
                    sourceManager = get(),
                    entryRepository = get(),
                    downloadLifecycle = get<EntryDownloadLifecycleInteraction>(),
                    entryInteractionPreferences = get<EntryInteractionPreferences>(),
                    historyRepository = get(),
                ),
            ),
            libraryProgressCalculator = animeEntryLibraryProgressCalculator(progressRepository),
            viewerSettingsProviders = listOf(viewerSettingsProvider),
            mediaCacheBuckets = listOf(LazyAnimePlaybackCacheBucket { get<EntryPlayerCache>() }),
            warmups = listOf(warmup),
        )
    }
}

private class LazyAnimePlaybackCacheBucket(
    private val cache: () -> EntryPlayerCache,
) : EntryMediaCacheBucket {
    private val delegate by lazy(cache)
    override val key: String = EntryMediaCacheBucketKeys.ANIME_PLAYBACK
    override val readableSize: String get() = delegate.readableSize
    override fun clear(): Int = delegate.clear()
}

private fun InjektRegistrar.addAnimeEntryInteractionRuntime(app: Application): () -> Unit {
    addAnimePlayerRuntime(app)
    addSingletonFactory { AnimeDownloadProvider(app) }
    addSingletonFactory { AnimeDownloadCache(app) }
    addSingletonFactory { AnimeDownloader(get(), get(), get(), get(), get(), get()) }
    addSingletonFactory { AnimeDownloadManager(app, get(), get(), get(), get()) }

    return { get<AnimeDownloadManager>() }
}
