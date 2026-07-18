package mihon.entry.interactions.anime

import eu.kanade.tachiyomi.source.entry.EntryType
import mihon.entry.interactions.EntryBulkDownloadCandidateCapability
import mihon.entry.interactions.EntryChildListCapability
import mihon.entry.interactions.EntryChildProgressCapability
import mihon.entry.interactions.EntryConsumptionCapability
import mihon.entry.interactions.EntryContinueCapability
import mihon.entry.interactions.EntryDownloadCapability
import mihon.entry.interactions.EntryDownloadLifecycleEventSink
import mihon.entry.interactions.EntryDownloadOptionsCapability
import mihon.entry.interactions.EntryImmersiveCapability
import mihon.entry.interactions.EntryInteractionPlugin
import mihon.entry.interactions.EntryMergeCapability
import mihon.entry.interactions.EntryMigrationCapability
import mihon.entry.interactions.EntryOpenCapability
import mihon.entry.interactions.EntryPlaybackPreferencesCapability
import mihon.entry.interactions.EntryPreviewCapability
import mihon.entry.interactions.EntryProgressCapability
import mihon.entry.interactions.anime.download.AnimeDownloadCache
import mihon.entry.interactions.anime.download.AnimeDownloadManager
import mihon.entry.interactions.settings.EntryInteractionPreferences
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
    val consumptionProcessor = AnimeConsumptionProcessor(
        entryProgressRepository = dependencies.entryProgressRepository,
        downloadLifecycle = dependencies.downloadLifecycle,
    )
    val progressProcessor = AnimeProgressProcessor(
        entryProgressRepository = dependencies.entryProgressRepository,
        entryChapterRepository = dependencies.entryChapterRepository,
    )
    val playbackPreferencesProcessor = AnimePlaybackPreferencesProcessor(
        playbackPreferencesRepository = dependencies.playbackPreferencesRepository,
    )
    val downloadProcessor = AnimeDownloadProcessor(dependencies)
    val migrationMergeProvider = AnimeMigrationMergeProvider()
    val childListProcessor = AnimeChildListProcessor(dependencies.entryProgressRepository)
    val previewProcessor = AnimePreviewInteraction(
        entryInteractionPreferences = dependencies.entryInteractionPreferences,
        sourceManager = dependencies.sourceManager,
    )
    val immersiveProcessor = AnimeImmersiveProcessor(
        entryProgressRepository = dependencies.entryProgressRepository,
        historyRepository = dependencies.historyRepository,
        resolveVideoStream = { Injekt.get() },
    )
    return object : EntryInteractionPlugin {
        override val type = EntryType.ANIME
        override val owner = ContributionOwner("entry-interactions.anime")
        override val providerBindings = listOf(
            EntryOpenCapability.bind(openProcessor),
            EntryContinueCapability.bind(continueProcessor),
            EntryConsumptionCapability.bind(consumptionProcessor),
            EntryProgressCapability.bind(progressProcessor),
            EntryPlaybackPreferencesCapability.bind(playbackPreferencesProcessor),
            EntryDownloadCapability.bind(downloadProcessor),
            EntryDownloadOptionsCapability.bind(downloadProcessor),
            EntryBulkDownloadCandidateCapability.bind(downloadProcessor),
            EntryMigrationCapability.bind(migrationMergeProvider),
            EntryMergeCapability.bind(migrationMergeProvider),
            EntryChildListCapability.bind(childListProcessor),
            EntryChildProgressCapability.bind(childListProcessor),
            EntryPreviewCapability.bind(previewProcessor),
            EntryImmersiveCapability.bind(immersiveProcessor),
        )
    }
}

data class AnimeEntryInteractionDependencies(
    val entryChapterRepository: EntryChapterRepository,
    val getEntryWithChapters: GetEntryWithChapters,
    val entryProgressRepository: EntryProgressRepository,
    val playbackPreferencesRepository: PlaybackPreferencesRepository,
    val downloadPreferences: DownloadPreferences,
    val downloadPreferencesRepository: DownloadPreferencesRepository,
    val sourceManager: SourceManager,
    val entryRepository: EntryRepository,
    val downloadLifecycle: EntryDownloadLifecycleEventSink,
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
    val downloadPreferencesRepository: DownloadPreferencesRepository,
    val sourceManager: SourceManager,
    val entryRepository: EntryRepository,
    val downloadLifecycle: EntryDownloadLifecycleEventSink,
    val entryInteractionPreferences: EntryInteractionPreferences,
    val historyRepository: HistoryRepository? = null,
)
