package mihon.entry.interactions

import android.app.Application
import coil3.ComponentRegistry
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import mihon.domain.chapter.interactor.FilterEntryChaptersForDownload
import mihon.entry.interactions.anime.animeEntryTypeRuntimeModule
import mihon.entry.interactions.book.bookEntryTypeRuntimeModule
import mihon.entry.interactions.manga.mangaEntryTypeRuntimeModule
import mihon.entry.interactions.reader.settings.ReaderBasePreferences
import mihon.entry.interactions.reader.settings.ReaderTrackPreferences
import mihon.entry.interactions.settings.DefaultViewerSettingBinder
import mihon.entry.interactions.settings.DefaultViewerSettingsInteraction
import mihon.entry.interactions.settings.EntryInteractionPreferences
import mihon.entry.interactions.settings.EntryMediaCachePreferences
import mihon.entry.viewer.settings.ViewerSettingBinder
import mihon.entry.viewer.settings.ViewerSettingOverrideRepository
import mihon.entry.viewer.settings.ViewerSettingsInteraction
import mihon.feature.graph.FeatureArtifactSelection
import mihon.feature.graph.FeatureGraph
import mihon.feature.graph.FeatureGraphEvaluation
import tachiyomi.core.common.preference.PreferenceStore
import tachiyomi.domain.entry.service.EntryLibraryProgressResolver
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.InjektRegistrar
import uy.kohesive.injekt.api.addSingletonFactory
import uy.kohesive.injekt.api.get

data class EntryInteractionRuntimeDependencies(
    val activityTheme: EntryInteractionActivityTheme,
    val notificationActions: EntryDownloadNotificationActions,
    val pageImageCache: EntryPageImageCache,
    val mangaChildGroupFilterDataSource: EntryChildGroupFilterDataSource,
    val readerIncognitoState: EntryReaderIncognitoState,
    val readerTracking: EntryReaderTracking,
    val profilePreferenceStore: PreferenceStore,
    val basePreferenceStore: PreferenceStore,
    val privatePreferenceStore: PreferenceStore,
    val mediaCacheBuckets: List<EntryMediaCacheBucket> = emptyList(),
)

fun interface EntryInteractionRuntimeWarmup {
    fun warmup()
}

fun InjektRegistrar.addEntryInteractionRuntime(
    app: Application,
    dependencies: EntryInteractionRuntimeDependencies,
) {
    addSingletonFactory<EntryInteractionActivityTheme> { dependencies.activityTheme }
    addSingletonFactory<EntryDownloadNotificationActions> { dependencies.notificationActions }
    addSingletonFactory<EntryPageImageCache> { dependencies.pageImageCache }
    addSingletonFactory<EntryReaderIncognitoState> { dependencies.readerIncognitoState }
    addSingletonFactory<EntryReaderTracking> { dependencies.readerTracking }
    addSingletonFactory<EntryChildGroupFilterDataSource> { dependencies.mangaChildGroupFilterDataSource }
    addSingletonFactory<EntryDownloadWorkController> { DefaultEntryDownloadWorkController(app) }
    addSingletonFactory { FilterEntryChaptersForDownload(get(), get(), get()) }
    addSingletonFactory<EntryDownloadLifecycleInteraction> {
        EntryDownloadLifecycleManager(
            downloadPreferences = get(),
            getCategories = get(),
            getEntryWithChapters = get(),
            entryRepository = get(),
            downloadInteraction = { Injekt.get() },
            capabilityReport = { Injekt.get() },
        )
    }

    addSingletonFactory { ReaderBasePreferences(dependencies.basePreferenceStore) }
    addSingletonFactory { ReaderTrackPreferences(dependencies.privatePreferenceStore) }
    addSingletonFactory { EntryInteractionPreferences(dependencies.profilePreferenceStore) }
    addSingletonFactory { EntryMediaCachePreferences(dependencies.basePreferenceStore) }
    addSingletonFactory<ViewerSettingBinder> {
        DefaultViewerSettingBinder(
            overrideRepository = get<ViewerSettingOverrideRepository>(),
            scope = CoroutineScope(SupervisorJob() + Dispatchers.Default),
        )
    }
    val typeRuntimeContributions = listOf(
        mangaEntryTypeRuntimeModule(dependencies.profilePreferenceStore),
        animeEntryTypeRuntimeModule(dependencies.profilePreferenceStore),
        bookEntryTypeRuntimeModule(dependencies.profilePreferenceStore),
    ).map { module ->
        module.install(this, app).also { it.validate(module.type) }
    }

    addSingletonFactory<ViewerSettingsInteraction> {
        DefaultViewerSettingsInteraction(
            providers = typeRuntimeContributions.flatMap(EntryTypeRuntimeContribution::viewerSettingsProviders),
        )
    }

    addSingletonFactory<EntryMediaCacheMaintenance> {
        DefaultEntryMediaCacheMaintenance(
            buckets = dependencies.mediaCacheBuckets +
                typeRuntimeContributions.flatMap(EntryTypeRuntimeContribution::mediaCacheBuckets),
        )
    }
    addSingletonFactory {
        EntryLibraryProgressResolver(
            typeRuntimeContributions.map(EntryTypeRuntimeContribution::libraryProgressCalculator),
        )
    }
    addSingletonFactory {
        EntryImageComponentInstallers(
            typeRuntimeContributions.flatMap(EntryTypeRuntimeContribution::imageComponentInstallers),
        )
    }

    addSingletonFactory<EntryInteractionComposition> {
        createEntryInteractionComposition(
            plugins = typeRuntimeContributions.map(EntryTypeRuntimeContribution::plugin),
            featureContributors = listOf(EntryOpenFeatureContributor),
        )
    }
    addSingletonFactory<FeatureGraph> { get<EntryInteractionComposition>().featureGraph }
    addSingletonFactory<FeatureGraphEvaluation> { get<EntryInteractionComposition>().featureGraphEvaluation }
    addSingletonFactory<FeatureArtifactSelection> { get<EntryInteractionComposition>().featureArtifacts }
    addSingletonFactory<EntryOpenFeature> {
        DefaultEntryOpenFeature(
            evaluation = get(),
            interaction = get<EntryInteractionComposition>().interactions.open,
        )
    }
    addSingletonFactory<EntryContinueInteraction> { get<EntryInteractionComposition>().interactions.continueEntry }
    addSingletonFactory<EntryDownloadInteraction> { get<EntryInteractionComposition>().interactions.download }
    addSingletonFactory<EntryCapabilityInteraction> { get<EntryInteractionComposition>().interactions.capability }
    addSingletonFactory<EntryConsumptionInteraction> { get<EntryInteractionComposition>().interactions.consumption }
    addSingletonFactory<EntryBookmarkInteraction> { get<EntryInteractionComposition>().interactions.bookmark }
    addSingletonFactory<EntryUpdateEligibilityInteraction> {
        get<EntryInteractionComposition>().interactions.updateEligibility
    }
    addSingletonFactory<EntryProgressInteraction> { get<EntryInteractionComposition>().interactions.progress }
    addSingletonFactory<EntryPlaybackPreferencesInteraction> {
        get<EntryInteractionComposition>().interactions.playbackPreferences
    }
    addSingletonFactory<EntryChildListInteraction> { get<EntryInteractionComposition>().interactions.childList }
    addSingletonFactory<EntryChildGroupFilterInteraction> {
        get<EntryInteractionComposition>().interactions.childGroupFilter
    }
    addSingletonFactory<EntryLibraryFilterInteraction> { get<EntryInteractionComposition>().interactions.libraryFilter }
    addSingletonFactory<EntryPreviewInteraction> { get<EntryInteractionComposition>().interactions.preview }
    addSingletonFactory<EntryImmersiveInteraction> { get<EntryInteractionComposition>().interactions.immersive }
    addSingletonFactory {
        EntryDownloadNotificationManager(
            context = app,
            downloads = get<EntryDownloadInteraction>(),
            actions = dependencies.notificationActions,
            getMergedEntry = get(),
        )
    }
    addSingletonFactory<EntryDownloadForegroundNotificationProvider> { get<EntryDownloadNotificationManager>() }
    addSingletonFactory<EntryInteractionRuntimeWarmup> {
        EntryInteractionRuntimeWarmup {
            typeRuntimeContributions.flatMap(EntryTypeRuntimeContribution::warmups).forEach { it() }
            get<EntryDownloadNotificationManager>().start()
        }
    }
}

fun ComponentRegistry.Builder.addEntryInteractionImageComponents(): ComponentRegistry.Builder {
    Injekt.get<EntryImageComponentInstallers>().values.forEach { it.install(this) }
    return this
}

private class DefaultEntryMediaCacheMaintenance(
    buckets: List<EntryMediaCacheBucket>,
) : EntryMediaCacheMaintenance {
    private val bucketsByKey = buckets.associateBy { it.key }

    init {
        check(bucketsByKey.size == buckets.size) {
            "Duplicate entry media cache bucket keys: ${buckets.groupingBy { it.key }.eachCount().duplicates()}"
        }
    }

    override fun buckets(): List<EntryMediaCacheBucket> {
        return bucketsByKey.values.toList()
    }

    override fun bucket(key: String): EntryMediaCacheBucket? {
        return bucketsByKey[key]
    }

    override fun clear(key: String): Int {
        return bucketsByKey[key]?.clear()
            ?: error("No entry media cache bucket registered for key $key")
    }
}

private fun Map<String, Int>.duplicates(): String {
    return entries
        .filter { it.value > 1 }
        .joinToString { it.key }
}
