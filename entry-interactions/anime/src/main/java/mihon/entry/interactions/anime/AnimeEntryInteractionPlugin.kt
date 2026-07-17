package mihon.entry.interactions.anime

import eu.kanade.tachiyomi.source.entry.EntryType
import mihon.domain.chapter.interactor.FilterEntryChaptersForDownload
import mihon.entry.interactions.EntryCapabilityCatalog
import mihon.entry.interactions.EntryCapabilityOutcomeDeclaration
import mihon.entry.interactions.EntryCapabilityOwner
import mihon.entry.interactions.EntryDownloadLifecycleInteraction
import mihon.entry.interactions.EntryInteractionPlugin
import mihon.entry.interactions.EntrySupportResult
import mihon.entry.interactions.anime.download.AnimeDownloadCache
import mihon.entry.interactions.anime.download.AnimeDownloadManager
import mihon.entry.interactions.settings.EntryInteractionPreferences
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
    return EntryInteractionPlugin { registry ->
        registry.declareCapabilityOutcome(
            EntryCapabilityOutcomeDeclaration(
                entryType = EntryType.ANIME,
                capability = EntryCapabilityCatalog.BOOKMARKING,
                result = EntrySupportResult.IntentionallyUnsupported(
                    owner = ANIME_CAPABILITY_OWNER,
                    reason = "Anime bookmark persistence is not supported in the current product",
                ),
            ),
        )
        registry.declareCapabilityOutcome(
            EntryCapabilityOutcomeDeclaration(
                entryType = EntryType.ANIME,
                capability = EntryCapabilityCatalog.CHILD_GROUP_FILTERING,
                result = EntrySupportResult.IntentionallyUnsupported(
                    owner = ANIME_CAPABILITY_OWNER,
                    reason = "Anime child-group filtering is intentionally outside current product scope",
                ),
            ),
        )
        val openProcessor = AnimeOpenProcessor()
        registry.registerOpenProcessor(openProcessor)
        registry.registerCapabilityProcessor(AnimeCapabilityProcessor())
        registry.registerChildListProcessor(
            AnimeChildListProcessor(
                entryProgressRepository = dependencies.entryProgressRepository,
            ),
        )
        registry.registerContinueProcessor(
            AnimeContinueProcessor(
                getEntryWithChapters = dependencies.getEntryWithChapters,
                entryProgressRepository = dependencies.entryProgressRepository,
                openProcessor = openProcessor,
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

private val ANIME_CAPABILITY_OWNER = EntryCapabilityOwner("entry-interactions.anime")

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
