package mihon.entry.interactions.manga

import eu.kanade.tachiyomi.source.entry.EntryType
import mihon.entry.interactions.EntryBookmarkCapability
import mihon.entry.interactions.EntryBulkDownloadCandidateCapability
import mihon.entry.interactions.EntryChildGroupFilterCapability
import mihon.entry.interactions.EntryChildListCapability
import mihon.entry.interactions.EntryChildProgressCapability
import mihon.entry.interactions.EntryConsumptionCapability
import mihon.entry.interactions.EntryContinueCapability
import mihon.entry.interactions.EntryDownloadArchivePackagingCapability
import mihon.entry.interactions.EntryDownloadCapability
import mihon.entry.interactions.EntryDownloadLifecycleEventSink
import mihon.entry.interactions.EntryDownloadParallelItemTransfersCapability
import mihon.entry.interactions.EntryDownloadParallelSourceTransfersCapability
import mihon.entry.interactions.EntryDownloadTallImageSplittingCapability
import mihon.entry.interactions.EntryImmersiveCapability
import mihon.entry.interactions.EntryInteractionPlugin
import mihon.entry.interactions.EntryMergeCapability
import mihon.entry.interactions.EntryMigrationCapability
import mihon.entry.interactions.EntryOpenCapability
import mihon.entry.interactions.EntryOutsideReleasePeriodFilterCapability
import mihon.entry.interactions.EntryPreviewCapability
import mihon.entry.interactions.EntryPreviewConfigurationCapability
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
    )
    val progressProcessor = MangaProgressProcessor(
        entryProgressRepository = dependencies.entryProgressRepository,
        entryChapterRepository = dependencies.entryChapterRepository,
    )
    val downloadProcessor = MangaDownloadProcessor(dependencies)
    val migrationMergeProvider = MangaMigrationMergeProvider()
    val childListProcessor = MangaChildListProcessor(dependencies.entryProgressRepository)
    val childGroupFilterProcessor = MangaChildGroupFilterProcessor
    val outsideReleasePeriodFilterProvider = MangaOutsideReleasePeriodFilterProvider()
    val previewProcessor = MangaPreviewInteraction(dependencies.entryInteractionPreferences)
    val immersiveProcessor = MangaImmersiveProcessor(
        entryChapterRepository = dependencies.entryChapterRepository,
        entryProgressRepository = dependencies.entryProgressRepository,
        historyRepository = runCatching { Injekt.get<HistoryRepository>() }.getOrNull(),
        readerIncognitoState = runCatching { Injekt.get<EntryReaderIncognitoState>() }.getOrNull(),
        readerTracking = runCatching { Injekt.get<EntryReaderTracking>() }.getOrNull(),
    )
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
            EntryMigrationCapability.bind(migrationMergeProvider),
            EntryMergeCapability.bind(migrationMergeProvider),
            EntryChildListCapability.bind(childListProcessor),
            EntryChildProgressCapability.bind(childListProcessor),
            EntryChildGroupFilterCapability.bind(childGroupFilterProcessor),
            EntryOutsideReleasePeriodFilterCapability.bind(outsideReleasePeriodFilterProvider),
            EntryPreviewCapability.bind(previewProcessor),
            EntryPreviewConfigurationCapability.bind(previewProcessor),
            EntryImmersiveCapability.bind(immersiveProcessor),
        )
    }
}

data class MangaEntryInteractionDependencies(
    val getEntryWithChapters: GetEntryWithChapters,
    val entryChapterRepository: EntryChapterRepository,
    val entryProgressRepository: EntryProgressRepository,
    val downloadPreferences: DownloadPreferences,
    val sourceManager: SourceManager,
    val downloadLifecycle: EntryDownloadLifecycleEventSink,
    val entryInteractionPreferences: EntryInteractionPreferences,
)

internal data class MangaEntryInteractionRuntimeDependencies(
    val getEntryWithChapters: GetEntryWithChapters,
    val entryChapterRepository: EntryChapterRepository,
    val entryProgressRepository: EntryProgressRepository,
    val downloadPreferences: DownloadPreferences,
    val downloadManager: DownloadManager,
    val downloadCache: DownloadCache,
    val sourceManager: SourceManager,
    val entryRepository: EntryRepository,
    val downloadLifecycle: EntryDownloadLifecycleEventSink,
    val entryInteractionPreferences: EntryInteractionPreferences,
)
