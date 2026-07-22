package mihon.entry.interactions.validation

import android.app.Application
import eu.kanade.tachiyomi.network.NetworkHelper
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import kotlinx.serialization.json.Json
import mihon.entry.interactions.EntryDownloadLifecycleEventSink
import mihon.entry.interactions.EntryDownloadWorkController
import mihon.entry.interactions.EntryInteractionComposition
import mihon.entry.interactions.EntryPageImageCache
import mihon.entry.interactions.EntryReaderIncognitoState
import mihon.entry.interactions.createEntryInteractionComposition
import mihon.entry.interactions.productionEntryFeatureContributors
import mihon.entry.interactions.productionEntryTypeRuntimeModules
import mihon.entry.interactions.settings.EntryInteractionPreferences
import nl.adaptivity.xmlutil.serialization.XML
import okhttp3.OkHttpClient
import tachiyomi.core.common.preference.InMemoryPreferenceStore
import tachiyomi.core.common.preference.ProfilePreferenceOwnerInstaller
import tachiyomi.core.common.preference.ProfilePreferenceOwnerRegistry
import tachiyomi.domain.category.interactor.GetCategories
import tachiyomi.domain.download.service.DownloadPreferences
import tachiyomi.domain.entry.interactor.GetEntryWithChapters
import tachiyomi.domain.entry.repository.DownloadPreferencesRepository
import tachiyomi.domain.entry.repository.EntryChapterRepository
import tachiyomi.domain.entry.repository.EntryProgressRepository
import tachiyomi.domain.entry.repository.EntryRepository
import tachiyomi.domain.entry.repository.PlaybackPreferencesRepository
import tachiyomi.domain.history.repository.HistoryRepository
import tachiyomi.domain.library.service.GlobalLibraryPreferences
import tachiyomi.domain.source.service.SourceManager
import tachiyomi.domain.storage.service.StorageManager
import tachiyomi.domain.track.interactor.GetTracks
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.InjektScope
import uy.kohesive.injekt.api.addSingletonFactory
import uy.kohesive.injekt.registry.default.DefaultRegistrar
import java.io.File

internal class ProductionEntryInteractionValidationEnvironment(
    private val temporaryDirectory: File,
) : AutoCloseable {
    private val previousInjekt: InjektScope = Injekt

    init {
        Injekt = InjektScope(DefaultRegistrar())
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    fun composition(): EntryInteractionComposition {
        val application = application()
        val preferenceOwners = ProfilePreferenceOwnerInstaller(
            owners = ProfilePreferenceOwnerRegistry(),
            preferenceStore = ::InMemoryPreferenceStore,
        )
        installControlledRuntimeDependencies(application)
        val plugins = productionEntryTypeRuntimeModules(preferenceOwners).map { module ->
            module.install(Injekt, application).also { it.validate(module.type) }.plugin
        }
        return createEntryInteractionComposition(
            plugins = plugins,
            featureContributors = productionEntryFeatureContributors(),
        )
    }

    override fun close() {
        Dispatchers.resetMain()
        Injekt = previousInjekt
    }

    private fun application(): Application {
        return mockk<Application>(relaxed = true).also { application ->
            every { application.cacheDir } returns temporaryDirectory.resolve("cache").also(File::mkdirs)
            every { application.filesDir } returns temporaryDirectory.resolve("files").also(File::mkdirs)
        }
    }

    private fun installControlledRuntimeDependencies(application: Application) {
        val storageManager = mockk<StorageManager>(relaxed = true) {
            every { changes } returns MutableSharedFlow()
            every { getDownloadsDirectory() } returns null
        }
        val networkHelper = mockk<NetworkHelper>(relaxed = true) {
            every { client } returns OkHttpClient()
        }
        Injekt.addSingletonFactory<Application> { application }
        Injekt.addSingletonFactory<StorageManager> { storageManager }
        Injekt.addSingletonFactory<NetworkHelper> { networkHelper }
        Injekt.addSingletonFactory<Json> { Json }
        Injekt.addSingletonFactory<XML> { XML.v1 {} }
        Injekt.addSingletonFactory<GlobalLibraryPreferences> { mockk(relaxed = true) }
        Injekt.addSingletonFactory<SourceManager> { mockk(relaxed = true) }
        Injekt.addSingletonFactory<EntryDownloadWorkController> { mockk(relaxed = true) }
        Injekt.addSingletonFactory<EntryPageImageCache> { mockk(relaxed = true) }
        Injekt.addSingletonFactory<EntryReaderIncognitoState> { mockk(relaxed = true) }
        Injekt.addSingletonFactory<EntryDownloadLifecycleEventSink> { mockk(relaxed = true) }
        Injekt.addSingletonFactory<EntryInteractionPreferences> { mockk(relaxed = true) }
        Injekt.addSingletonFactory<GetEntryWithChapters> { mockk(relaxed = true) }
        Injekt.addSingletonFactory<EntryChapterRepository> { mockk(relaxed = true) }
        Injekt.addSingletonFactory<EntryProgressRepository> { mockk(relaxed = true) }
        Injekt.addSingletonFactory<EntryRepository> { mockk(relaxed = true) }
        Injekt.addSingletonFactory<DownloadPreferences> { mockk(relaxed = true) }
        Injekt.addSingletonFactory<DownloadPreferencesRepository> { mockk(relaxed = true) }
        Injekt.addSingletonFactory<PlaybackPreferencesRepository> { mockk(relaxed = true) }
        Injekt.addSingletonFactory<HistoryRepository> { mockk(relaxed = true) }
        Injekt.addSingletonFactory<GetCategories> { mockk(relaxed = true) }
        Injekt.addSingletonFactory<GetTracks> { mockk(relaxed = true) }
    }
}
