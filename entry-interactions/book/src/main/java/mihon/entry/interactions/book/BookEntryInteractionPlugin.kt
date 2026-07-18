package mihon.entry.interactions.book

import eu.kanade.tachiyomi.source.entry.EntryType
import mihon.domain.chapter.interactor.FilterEntryChaptersForDownload
import mihon.entry.interactions.EntryConsumptionCapability
import mihon.entry.interactions.EntryContinueCapability
import mihon.entry.interactions.EntryDownloadLifecycleInteraction
import mihon.entry.interactions.EntryInteractionPlugin
import mihon.entry.interactions.EntryInteractionRegistry
import mihon.entry.interactions.EntryOpenCapability
import mihon.entry.interactions.EntryProgressCapability
import mihon.entry.interactions.book.download.BookDownloadManager
import mihon.feature.graph.ContributionOwner
import tachiyomi.domain.entry.interactor.GetEntryWithChapters
import tachiyomi.domain.entry.repository.EntryChapterRepository
import tachiyomi.domain.entry.repository.EntryProgressRepository
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get

fun bookEntryInteractionPlugin(
    dependencies: BookEntryInteractionDependencies,
): EntryInteractionPlugin {
    val openProcessor = BookOpenProcessor()
    val continueProcessor = BookContinueProcessor(
        getEntryWithChapters = dependencies.getEntryWithChapters,
        entryProgressRepository = dependencies.entryProgressRepository,
        openProcessor = openProcessor,
    )
    val consumptionProcessor = BookConsumptionProcessor(
        entryProgressRepository = dependencies.entryProgressRepository,
        entryChapterRepository = dependencies.entryChapterRepository,
        downloadLifecycle = dependencies.downloadLifecycle,
    )
    val progressProcessor = BookProgressProcessor(
        entryProgressRepository = dependencies.entryProgressRepository,
        entryChapterRepository = dependencies.entryChapterRepository,
    )
    return object : EntryInteractionPlugin {
        override val type = EntryType.BOOK
        override val owner = ContributionOwner("entry-interactions.book")
        override val providerBindings = listOf(
            EntryOpenCapability.bind(openProcessor),
            EntryContinueCapability.bind(continueProcessor),
            EntryConsumptionCapability.bind(consumptionProcessor),
            EntryProgressCapability.bind(progressProcessor),
        )

        override fun register(registry: EntryInteractionRegistry) {
            val downloadManager = if (dependencies.downloadsEnabled) Injekt.get<BookDownloadManager>() else null
            super<EntryInteractionPlugin>.register(registry)
            registry.registerCapabilityProcessor(BookCapabilityProcessor())
            registry.registerChildListProcessor(BookChildListProcessor(dependencies.entryProgressRepository))
            if (dependencies.downloadsEnabled) {
                registry.registerDownloadProcessor(
                    BookDownloadProcessor(
                        BookDownloadProcessorDependencies(
                            manager = checkNotNull(downloadManager),
                            cache = Injekt.get(),
                            sourceManager = Injekt.get(),
                            entryRepository = Injekt.get(),
                            getEntryWithChapters = dependencies.getEntryWithChapters,
                            filterEntryChaptersForDownload = checkNotNull(dependencies.filterEntryChaptersForDownload) {
                                "BOOK downloads require the automatic-download filter"
                            },
                            mergedEntryRepository = Injekt.get(),
                        ),
                    ),
                )
            }
            registry.registerUpdateEligibilityProcessor(BookUpdateEligibilityProcessor())
            registry.registerLibraryFilterProcessor(BookLibraryFilterProcessor())
        }
    }
}

data class BookEntryInteractionDependencies(
    val getEntryWithChapters: GetEntryWithChapters,
    val entryChapterRepository: EntryChapterRepository,
    val entryProgressRepository: EntryProgressRepository,
    val filterEntryChaptersForDownload: FilterEntryChaptersForDownload? = null,
    val downloadLifecycle: EntryDownloadLifecycleInteraction? = null,
    val downloadsEnabled: Boolean = false,
)
