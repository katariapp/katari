package mihon.entry.interactions.book

import eu.kanade.tachiyomi.source.entry.EntryType
import mihon.entry.interactions.EntryBulkDownloadCandidateCapability
import mihon.entry.interactions.EntryChildListCapability
import mihon.entry.interactions.EntryChildProgressCapability
import mihon.entry.interactions.EntryConsumptionCapability
import mihon.entry.interactions.EntryContinueCapability
import mihon.entry.interactions.EntryDownloadCapability
import mihon.entry.interactions.EntryDownloadLifecycleInteraction
import mihon.entry.interactions.EntryInteractionPlugin
import mihon.entry.interactions.EntryInteractionProviderBinding
import mihon.entry.interactions.EntryOpenCapability
import mihon.entry.interactions.EntryOutsideReleasePeriodFilterCapability
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
    val downloadProcessor = if (dependencies.downloadsEnabled) {
        BookDownloadProcessor(
            BookDownloadProcessorDependencies(
                manager = Injekt.get<BookDownloadManager>(),
                cache = Injekt.get(),
                sourceManager = Injekt.get(),
                entryRepository = Injekt.get(),
                getEntryWithChapters = dependencies.getEntryWithChapters,
                mergedEntryRepository = Injekt.get(),
            ),
        )
    } else {
        null
    }
    val childListProcessor = BookChildListProcessor(dependencies.entryProgressRepository)
    val outsideReleasePeriodFilterProvider = BookOutsideReleasePeriodFilterProvider()
    return object : EntryInteractionPlugin {
        override val type = EntryType.BOOK
        override val owner = ContributionOwner("entry-interactions.book")
        override val providerBindings = buildList<EntryInteractionProviderBinding<*>> {
            add(EntryOpenCapability.bind(openProcessor))
            add(EntryContinueCapability.bind(continueProcessor))
            add(EntryConsumptionCapability.bind(consumptionProcessor))
            add(EntryProgressCapability.bind(progressProcessor))
            add(EntryChildListCapability.bind(childListProcessor))
            add(EntryChildProgressCapability.bind(childListProcessor))
            add(EntryOutsideReleasePeriodFilterCapability.bind(outsideReleasePeriodFilterProvider))
            if (downloadProcessor != null) {
                add(EntryDownloadCapability.bind(downloadProcessor))
                add(EntryBulkDownloadCandidateCapability.bind(downloadProcessor))
            }
        }
    }
}

data class BookEntryInteractionDependencies(
    val getEntryWithChapters: GetEntryWithChapters,
    val entryChapterRepository: EntryChapterRepository,
    val entryProgressRepository: EntryProgressRepository,
    val downloadLifecycle: EntryDownloadLifecycleInteraction? = null,
    val downloadsEnabled: Boolean = false,
)
