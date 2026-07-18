package mihon.entry.interactions.manga

import eu.kanade.tachiyomi.source.entry.EntryType
import mihon.domain.chapter.interactor.FilterEntryChaptersForDownload
import mihon.entry.interactions.EntryAutomaticDownloadFilterCapability
import mihon.entry.interactions.EntryBookmarkCapability
import mihon.entry.interactions.EntryBulkDownloadCandidateCapability
import mihon.entry.interactions.EntryChildGroupFilterDataSource
import mihon.entry.interactions.EntryConsumptionCapability
import mihon.entry.interactions.EntryContinueCapability
import mihon.entry.interactions.EntryDownloadArchivePackagingCapability
import mihon.entry.interactions.EntryDownloadCapability
import mihon.entry.interactions.EntryDownloadLifecycleInteraction
import mihon.entry.interactions.EntryDownloadParallelItemTransfersCapability
import mihon.entry.interactions.EntryDownloadParallelSourceTransfersCapability
import mihon.entry.interactions.EntryDownloadTallImageSplittingCapability
import mihon.entry.interactions.EntryInteractionPlugin
import mihon.entry.interactions.EntryInteractionRegistry
import mihon.entry.interactions.EntryMergeCapability
import mihon.entry.interactions.EntryMigrationCapability
import mihon.entry.interactions.EntryOpenCapability
import mihon.entry.interactions.EntryProgressCapability
import mihon.entry.interactions.EntryReaderIncognitoState
import mihon.entry.interactions.EntryReaderTracking
import mihon.entry.interactions.manga.download.DownloadCache
import mihon.entry.interactions.manga.download.DownloadManager
import mihon.entry.interactions.settings.EntryInteractionPreferences
import mihon.feature.graph.ContributionOwner
import tachiyomi.domain.download.service.DownloadPreferences
import tachiyomi.domain.entry.interactor.GetEntryWithChapters
import tachiyomi.domain.entry.repository.EntryChapterRepository
import tachiyomi.domain.entry.repository.EntryProgressRepository
import tachiyomi.domain.entry.repository.EntryRepository
import tachiyomi.domain.history.repository.HistoryRepository
import tachiyomi.domain.source.service.SourceManager
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get

fun mangaEntryInteractionPlugin(
    dependencies: MangaEntryInteractionDependencies,
): EntryInteractionPlugin {
    return mangaEntryInteractionPlugin(
        MangaEntryInteractionRuntimeDependencies(
            getEntryWithChapters = dependencies.getEntryWithChapters,
            entryChapterRepository = dependencies.entryChapterRepository,
            entryProgressRepository = dependencies.entryProgressRepository,
            filterEntryChaptersForDownload = dependencies.filterEntryChaptersForDownload,
            childGroupFilterDataSource = dependencies.childGroupFilterDataSource,
            downloadPreferences = dependencies.downloadPreferences,
            downloadManager = Injekt.get(),
            downloadCache = Injekt.get(),
            sourceManager = dependencies.sourceManager,
            entryRepository = Injekt.get(),
            downloadLifecycle = dependencies.downloadLifecycle,
            entryInteractionPreferences = dependencies.entryInteractionPreferences,
        ),
    )
}

internal fun mangaEntryInteractionPlugin(
    dependencies: MangaEntryInteractionRuntimeDependencies,
): EntryInteractionPlugin {
    val openProcessor = MangaOpenProcessor()
    val continueProcessor = MangaContinueProcessor(
        getEntryWithChapters = dependencies.getEntryWithChapters,
        entryProgressRepository = dependencies.entryProgressRepository,
        openProcessor = openProcessor,
    )
    val consumptionProcessor = MangaConsumptionProcessor(
        entryChapterRepository = dependencies.entryChapterRepository,
        entryProgressRepository = dependencies.entryProgressRepository,
        downloadLifecycle = dependencies.downloadLifecycle,
    )
    val progressProcessor = MangaProgressProcessor(
        entryProgressRepository = dependencies.entryProgressRepository,
        entryChapterRepository = dependencies.entryChapterRepository,
    )
    val downloadProcessor = MangaDownloadProcessor(dependencies)
    val migrationMergeProvider = MangaMigrationMergeProvider()
    return object : EntryInteractionPlugin {
        override val type = EntryType.MANGA
        override val owner = ContributionOwner("entry-interactions.manga")
        override val providerBindings = listOf(
            EntryOpenCapability.bind(openProcessor),
            EntryContinueCapability.bind(continueProcessor),
            EntryConsumptionCapability.bind(consumptionProcessor),
            EntryBookmarkCapability.bind(consumptionProcessor),
            EntryProgressCapability.bind(progressProcessor),
            EntryDownloadCapability.bind(downloadProcessor),
            EntryDownloadArchivePackagingCapability.bind(downloadProcessor),
            EntryDownloadTallImageSplittingCapability.bind(downloadProcessor),
            EntryDownloadParallelSourceTransfersCapability.bind(downloadProcessor),
            EntryDownloadParallelItemTransfersCapability.bind(downloadProcessor),
            EntryBulkDownloadCandidateCapability.bind(downloadProcessor),
            EntryAutomaticDownloadFilterCapability.bind(downloadProcessor),
            EntryMigrationCapability.bind(migrationMergeProvider),
            EntryMergeCapability.bind(migrationMergeProvider),
        )

        override fun register(registry: EntryInteractionRegistry) {
            super<EntryInteractionPlugin>.register(registry)
            registry.registerChildListProcessor(MangaChildListProcessor(dependencies.entryProgressRepository))
            registry.registerUpdateEligibilityProcessor(MangaUpdateEligibilityProcessor())
            registry.registerChildGroupFilterProcessor(
                MangaChildGroupFilterProcessor(
                    dataSource = dependencies.childGroupFilterDataSource,
                ),
            )
            registry.registerLibraryFilterProcessor(MangaLibraryFilterProcessor())
            val previewInteraction = MangaPreviewInteraction(
                entryInteractionPreferences = dependencies.entryInteractionPreferences,
            )
            registry.registerPreviewProcessor(previewInteraction)
            registry.registerImmersiveProcessor(
                MangaImmersiveProcessor(
                    entryChapterRepository = dependencies.entryChapterRepository,
                    entryProgressRepository = dependencies.entryProgressRepository,
                    historyRepository = runCatching { Injekt.get<HistoryRepository>() }.getOrNull(),
                    readerIncognitoState = runCatching { Injekt.get<EntryReaderIncognitoState>() }.getOrNull(),
                    readerTracking = runCatching { Injekt.get<EntryReaderTracking>() }.getOrNull(),
                ),
            )
        }
    }
}

data class MangaEntryInteractionDependencies(
    val getEntryWithChapters: GetEntryWithChapters,
    val entryChapterRepository: EntryChapterRepository,
    val entryProgressRepository: EntryProgressRepository,
    val filterEntryChaptersForDownload: FilterEntryChaptersForDownload,
    val childGroupFilterDataSource: EntryChildGroupFilterDataSource,
    val downloadPreferences: DownloadPreferences,
    val sourceManager: SourceManager,
    val downloadLifecycle: EntryDownloadLifecycleInteraction? = null,
    val entryInteractionPreferences: EntryInteractionPreferences,
)

internal data class MangaEntryInteractionRuntimeDependencies(
    val getEntryWithChapters: GetEntryWithChapters,
    val entryChapterRepository: EntryChapterRepository,
    val entryProgressRepository: EntryProgressRepository,
    val filterEntryChaptersForDownload: FilterEntryChaptersForDownload,
    val childGroupFilterDataSource: EntryChildGroupFilterDataSource,
    val downloadPreferences: DownloadPreferences,
    val downloadManager: DownloadManager,
    val downloadCache: DownloadCache,
    val sourceManager: SourceManager,
    val entryRepository: EntryRepository,
    val downloadLifecycle: EntryDownloadLifecycleInteraction? = null,
    val entryInteractionPreferences: EntryInteractionPreferences,
)
