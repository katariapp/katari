package mihon.entry.interactions.book

import android.app.Application
import eu.kanade.tachiyomi.source.entry.EntryType
import mihon.entry.interactions.EntryDownloadLifecycleInteraction
import mihon.entry.interactions.EntryMediaCacheBucket
import mihon.entry.interactions.EntryReaderIncognitoState
import mihon.entry.interactions.EntryTypeRuntimeContribution
import mihon.entry.interactions.EntryTypeRuntimeModule
import mihon.entry.interactions.book.download.BookDownloadCache
import mihon.entry.interactions.book.download.BookDownloadIndexStore
import mihon.entry.interactions.book.download.BookDownloadManager
import mihon.entry.interactions.book.download.BookDownloadProvider
import mihon.entry.interactions.book.download.BookDownloadStore
import mihon.entry.interactions.book.download.BookDownloader
import mihon.entry.interactions.book.epub.ReadiumEpubProcessor
import mihon.entry.interactions.book.prose.HtmlProseChapterProcessor
import mihon.entry.interactions.settings.HtmlProseSettingsProvider
import mihon.entry.interactions.settings.ReadiumEpubSettingsProvider
import mihon.entry.viewer.settings.ViewerSettingsProvider
import tachiyomi.core.common.preference.PreferenceStore
import tachiyomi.domain.entry.repository.EntryProgressRepository
import tachiyomi.domain.storage.service.StorageManager
import uy.kohesive.injekt.api.InjektRegistrar
import uy.kohesive.injekt.api.addSingletonFactory
import uy.kohesive.injekt.api.get

fun bookEntryTypeRuntimeModule(profilePreferenceStore: PreferenceStore): EntryTypeRuntimeModule {
    return EntryTypeRuntimeModule(EntryType.BOOK) { app ->
        val runtime = addBookEntryInteractionRuntime(app, profilePreferenceStore)
        val progressRepository = get<EntryProgressRepository>()
        EntryTypeRuntimeContribution(
            plugin = bookEntryInteractionPlugin(
                BookEntryInteractionDependencies(
                    getEntryWithChapters = get(),
                    entryChapterRepository = get(),
                    entryProgressRepository = progressRepository,
                    downloadLifecycle = get<EntryDownloadLifecycleInteraction>(),
                    downloadsEnabled = true,
                ),
            ),
            libraryProgressCalculator = bookEntryLibraryProgressCalculator(progressRepository),
            viewerSettingsProviders = runtime.viewerSettingsProviders,
            mediaCacheBuckets = runtime.mediaCacheBuckets,
        )
    }
}

/** Installs generic BOOK host services. Built-in format processors are registered here when ready. */
private fun InjektRegistrar.addBookEntryInteractionRuntime(
    app: Application,
    profilePreferenceStore: PreferenceStore,
): BookRuntimeArtifacts {
    val materializationCache = BookMaterializationCache(app)
    val readiumSettingsProvider = ReadiumEpubSettingsProvider(profilePreferenceStore)
    val proseSettingsProvider = HtmlProseSettingsProvider(profilePreferenceStore)
    addSingletonFactory<BookMaterializationStore> { materializationCache }
    addSingletonFactory { BookDownloadProvider(get<StorageManager>()) }
    addSingletonFactory {
        val storageManager = get<StorageManager>()
        BookDownloadCache(
            provider = get(),
            indexStore = BookDownloadIndexStore(app),
            storageChanges = storageManager.changes,
        )
    }
    addSingletonFactory { BookDownloadStore(app) }
    addSingletonFactory { BookReaderSessionRegistry() }
    addSingletonFactory { BookChapterNavigationResolver(get()) }
    addSingletonFactory { readiumSettingsProvider }
    addSingletonFactory { proseSettingsProvider }
    addSingletonFactory {
        BookProcessorRegistry(
            processors = listOf(
                ReadiumEpubProcessor(),
                HtmlProseChapterProcessor(),
            ),
        )
    }
    addSingletonFactory {
        BookDownloader(
            application = app,
            provider = get(),
            cache = get(),
            sourceManager = get(),
            networkHelper = get(),
            materializationStore = get(),
            processorRegistry = get(),
        )
    }
    addSingletonFactory {
        BookDownloadManager(
            context = app,
            cache = get(),
            provider = get(),
            downloader = get(),
            sourceManager = get(),
            store = get(),
        )
    }
    addSingletonFactory { BookProcessorPreferences(profilePreferenceStore) }
    addSingletonFactory {
        BookProcessorSelectionCoordinator(
            registry = get(),
            preferences = get(),
        )
    }
    addSingletonFactory {
        BookReaderHostResolver(
            sessionFactory = get(),
            selectionCoordinator = get(),
        )
    }
    addSingletonFactory {
        BookReaderSessionFactory(
            entryRepository = get(),
            entryChapterRepository = get(),
            entryProgressRepository = get(),
            historyRepository = get(),
            sourceManager = get(),
            processorRegistry = get(),
            networkHelper = get(),
            incognitoState = get<EntryReaderIncognitoState>(),
            materializationStore = get(),
            downloadCache = get(),
            downloadLifecycle = get(),
        )
    }
    return BookRuntimeArtifacts(
        mediaCacheBuckets = listOf(materializationCache),
        viewerSettingsProviders = listOf(readiumSettingsProvider, proseSettingsProvider),
    )
}

private data class BookRuntimeArtifacts(
    val mediaCacheBuckets: List<EntryMediaCacheBucket>,
    val viewerSettingsProviders: List<ViewerSettingsProvider>,
)
