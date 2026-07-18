package mihon.entry.interactions.anime

import eu.kanade.tachiyomi.source.entry.EntryType
import mihon.domain.chapter.interactor.FilterEntryChaptersForDownload
import mihon.entry.interactions.EntryContinueCapability
import mihon.entry.interactions.EntryDownloadLifecycleInteraction
import mihon.entry.interactions.EntryInteractionPlugin
import mihon.entry.interactions.EntryInteractionRegistry
import mihon.entry.interactions.EntryOpenCapability
import mihon.entry.interactions.anime.download.AnimeDownloadCache
import mihon.entry.interactions.anime.download.AnimeDownloadManager
import mihon.entry.interactions.settings.EntryInteractionPreferences
import mihon.entry.interactions.toContentTypeId
import mihon.feature.graph.CapabilityProvider
import mihon.feature.graph.ContentTypeContribution
import mihon.feature.graph.ContributionOwner
import tachiyomi.domain.download.service.DownloadPreferences
import tachiyomi.domain.entry.interactor.GetEntryWithChapters
import tachiyomi.domain.entry.repository.DownloadPreferencesRepository
import tachiyomi.domain.entry.repository.EntryChapterRepository
import tachiyomi.domain.entry.repository.EntryProgressRepository
import tachiyomi.domain.entry.repository.EntryRepository
import tachiyomi.domain.entry.repository.PlaybackPreferencesRepository
import tachiyomi.domain.history.repository.HistoryRepository
import tachiyomi.domain.source.service.SourceManager
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get

fun animeEntryInteractionPlugin(
    dependencies: AnimeEntryInteractionDependencies,
): EntryInteractionPlugin {
    return animeEntryInteractionPlugin(
        AnimeEntryInteractionRuntimeDependencies(
            entryChapterRepository = dependencies.entryChapterRepository,
            getEntryWithChapters = dependencies.getEntryWithChapters,
            entryProgressRepository = dependencies.entryProgressRepository,
            playbackPreferencesRepository = dependencies.playbackPreferencesRepository,
            animeDownloadManager = Injekt.get(),
            animeDownloadCache = Injekt.get(),
            downloadPreferences = dependencies.downloadPreferences,
            filterEntryChaptersForDownload = dependencies.filterEntryChaptersForDownload,
            downloadPreferencesRepository = dependencies.downloadPreferencesRepository,
            sourceManager = dependencies.sourceManager,
            entryRepository = dependencies.entryRepository,
            downloadLifecycle = dependencies.downloadLifecycle,
            entryInteractionPreferences = dependencies.entryInteractionPreferences,
            historyRepository = dependencies.historyRepository,
        ),
    )
}

internal fun animeEntryInteractionPlugin(
    dependencies: AnimeEntryInteractionRuntimeDependencies,
): EntryInteractionPlugin {
    val openProcessor = AnimeOpenProcessor()
    val continueProcessor = AnimeContinueProcessor(
        getEntryWithChapters = dependencies.getEntryWithChapters,
        entryProgressRepository = dependencies.entryProgressRepository,
        openProcessor = openProcessor,
    )
    return object : EntryInteractionPlugin {
        override val type = EntryType.ANIME
        override val contentTypeContribution = ContentTypeContribution(
            contentType = type.toContentTypeId(),
            owner = ContributionOwner("entry-interactions.anime"),
            providers = listOf(
                CapabilityProvider(EntryOpenCapability, openProcessor),
                CapabilityProvider(EntryContinueCapability, continueProcessor),
            ),
        )

        override fun register(registry: EntryInteractionRegistry) {
            installContributedProviders(registry)
            registry.registerCapabilityProcessor(AnimeCapabilityProcessor())
            registry.registerChildListProcessor(
                AnimeChildListProcessor(
                    entryProgressRepository = dependencies.entryProgressRepository,
                ),
            )
            registry.registerDownloadProcessor(
                AnimeDownloadProcessor(
                    dependencies = dependencies,
                ),
            )
            registry.registerConsumptionProcessor(
                AnimeConsumptionProcessor(
                    entryProgressRepository = dependencies.entryProgressRepository,
                    downloadLifecycle = dependencies.downloadLifecycle,
                ),
            )
            registry.registerUpdateEligibilityProcessor(AnimeUpdateEligibilityProcessor())
            registry.registerProgressProcessor(
                AnimeProgressProcessor(
                    entryProgressRepository = dependencies.entryProgressRepository,
                    entryChapterRepository = dependencies.entryChapterRepository,
                ),
            )
            registry.registerPlaybackPreferencesProcessor(
                AnimePlaybackPreferencesProcessor(
                    playbackPreferencesRepository = dependencies.playbackPreferencesRepository,
                ),
            )
            registry.registerChildGroupFilterProcessor(AnimeChildGroupFilterProcessor())
            registry.registerLibraryFilterProcessor(AnimeLibraryFilterProcessor())
            registry.registerPreviewProcessor(
                AnimePreviewInteraction(
                    entryInteractionPreferences = dependencies.entryInteractionPreferences,
                    sourceManager = dependencies.sourceManager,
                ),
            )
            registry.registerImmersiveProcessor(
                AnimeImmersiveProcessor(
                    entryProgressRepository = dependencies.entryProgressRepository,
                    historyRepository = dependencies.historyRepository,
                    resolveVideoStream = { Injekt.get() },
                ),
            )
        }
    }
}

data class AnimeEntryInteractionDependencies(
    val entryChapterRepository: EntryChapterRepository,
    val getEntryWithChapters: GetEntryWithChapters,
    val entryProgressRepository: EntryProgressRepository,
    val playbackPreferencesRepository: PlaybackPreferencesRepository,
    val downloadPreferences: DownloadPreferences,
    val filterEntryChaptersForDownload: FilterEntryChaptersForDownload,
    val downloadPreferencesRepository: DownloadPreferencesRepository,
    val sourceManager: SourceManager,
    val entryRepository: EntryRepository,
    val downloadLifecycle: EntryDownloadLifecycleInteraction? = null,
    val entryInteractionPreferences: EntryInteractionPreferences,
    val historyRepository: HistoryRepository? = null,
)

internal data class AnimeEntryInteractionRuntimeDependencies(
    val entryChapterRepository: EntryChapterRepository,
    val getEntryWithChapters: GetEntryWithChapters,
    val entryProgressRepository: EntryProgressRepository,
    val playbackPreferencesRepository: PlaybackPreferencesRepository,
    val animeDownloadManager: AnimeDownloadManager,
    val animeDownloadCache: AnimeDownloadCache,
    val downloadPreferences: DownloadPreferences,
    val filterEntryChaptersForDownload: FilterEntryChaptersForDownload,
    val downloadPreferencesRepository: DownloadPreferencesRepository,
    val sourceManager: SourceManager,
    val entryRepository: EntryRepository,
    val downloadLifecycle: EntryDownloadLifecycleInteraction? = null,
    val entryInteractionPreferences: EntryInteractionPreferences,
    val historyRepository: HistoryRepository? = null,
)
