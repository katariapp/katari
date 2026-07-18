package mihon.entry.interactions.book

import eu.kanade.tachiyomi.source.entry.EntryType
import mihon.domain.chapter.interactor.FilterEntryChaptersForDownload
import mihon.entry.interactions.EntryContinueCapability
import mihon.entry.interactions.EntryDownloadLifecycleInteraction
import mihon.entry.interactions.EntryInteractionPlugin
import mihon.entry.interactions.EntryInteractionRegistry
import mihon.entry.interactions.EntryOpenCapability
import mihon.entry.interactions.book.download.BookDownloadManager
import mihon.entry.interactions.toContentTypeId
import mihon.feature.graph.CapabilityProvider
import mihon.feature.graph.ContentTypeContribution
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
    return object : EntryInteractionPlugin {
        override val type = EntryType.BOOK
        override val contentTypeContribution = ContentTypeContribution(
            contentType = type.toContentTypeId(),
            owner = ContributionOwner("entry-interactions.book"),
            providers = listOf(
                CapabilityProvider(EntryOpenCapability, openProcessor),
                CapabilityProvider(EntryContinueCapability, continueProcessor),
            ),
        )

        override fun register(registry: EntryInteractionRegistry) {
            val downloadManager = if (dependencies.downloadsEnabled) Injekt.get<BookDownloadManager>() else null
            val downloadLifecycle = dependencies.downloadLifecycle
            installContributedProviders(registry)
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
            registry.registerConsumptionProcessor(
                BookConsumptionProcessor(
                    entryProgressRepository = dependencies.entryProgressRepository,
                    entryChapterRepository = dependencies.entryChapterRepository,
                    downloadLifecycle = downloadLifecycle,
                ),
            )
            registry.registerUpdateEligibilityProcessor(BookUpdateEligibilityProcessor())
            registry.registerProgressProcessor(
                BookProgressProcessor(
                    entryProgressRepository = dependencies.entryProgressRepository,
                    entryChapterRepository = dependencies.entryChapterRepository,
                ),
            )
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
