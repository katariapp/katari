package mihon.entry.interactions

import android.app.Application
import coil3.ComponentRegistry
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
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
import tachiyomi.core.common.preference.PreferenceStore
import tachiyomi.domain.entry.service.EntryLibraryProgressResolutionPort
import tachiyomi.domain.library.service.LibraryPreferences
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.InjektRegistrar
import uy.kohesive.injekt.api.addSingletonFactory
import uy.kohesive.injekt.api.get

data class EntryInteractionRuntimeDependencies(
    val activityTheme: EntryInteractionActivityTheme,
    val notificationActions: EntryDownloadNotificationActions,
    val pageImageCache: EntryPageImageCache,
    val childGroupFilterDataSource: EntryChildGroupFilterDataSource,
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
    addSingletonFactory<EntryChildGroupFilterDataSource> { dependencies.childGroupFilterDataSource }
    addSingletonFactory<EntryDownloadWorkController> { DefaultEntryDownloadWorkController(app) }
    addSingletonFactory { EntryAutomaticDownloadPolicy(get(), get(), get()) }
    addSingletonFactory<EntryDownloadLifecycleEventSink> {
        EntryDownloadLifecycleEventSink { event ->
            Injekt.get<EntryDownloadLifecycleFeature>().onEvent(event)
        }
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
        EntryImageComponentInstallers(
            typeRuntimeContributions.flatMap(EntryTypeRuntimeContribution::imageComponentInstallers),
        )
    }

    addSingletonFactory<EntryInteractionComposition> {
        createEntryInteractionComposition(
            plugins = typeRuntimeContributions.map(EntryTypeRuntimeContribution::plugin),
            featureContributors = listOf(
                EntryOpenFeatureContributor,
                EntryContinueFeatureContributor,
                EntryDownloadRuntimeFeatureContributor,
                EntryDownloadActionFeatureContributor,
                EntryAutomaticDownloadFeatureContributor,
                EntryDownloadLifecycleFeatureContributor,
                EntryDownloadConfigurationFeatureContributor,
                EntryDownloadMaintenanceFeatureContributor,
                EntryConsumptionFeatureContributor,
                EntryBookmarkFeatureContributor,
                EntryUpdateEligibilityFeatureContributor,
                EntryProgressFeatureContributor,
                EntryPlaybackPreferencesFeatureContributor,
                EntryChildListFeatureContributor,
                EntryLibraryFilterFeatureContributor,
                EntryChildGroupFilterFeatureContributor,
                EntryPreviewFeatureContributor,
                EntryImmersiveFeatureContributor,
                EntryRelatedEntriesFeatureContributor,
                EntryLibraryProgressFeatureContributor,
            ),
        )
    }
    addSingletonFactory<EntryOpenFeature> {
        val composition = get<EntryInteractionComposition>()
        DefaultEntryOpenFeature(
            evaluation = composition.featureGraphEvaluation,
            interaction = composition.interactions.open,
        )
    }
    addSingletonFactory<EntryContinueFeature> {
        val composition = get<EntryInteractionComposition>()
        DefaultEntryContinueFeature(
            evaluation = composition.featureGraphEvaluation,
            interaction = composition.interactions.continueEntry,
        )
    }
    addSingletonFactory<EntryLibraryProgressFeature> {
        val composition = get<EntryInteractionComposition>()
        DefaultEntryLibraryProgressFeature(
            evaluation = composition.featureGraphEvaluation,
            interaction = composition.interactions.libraryProgress,
            continueFeature = get(),
        )
    }
    addSingletonFactory<EntryLibraryProgressResolutionPort> { get<EntryLibraryProgressFeature>() }
    addSingletonFactory<EntryDownloadRuntimeCoordinator> {
        val composition = get<EntryInteractionComposition>()
        DefaultEntryDownloadRuntimeFeature(
            evaluation = composition.featureGraphEvaluation,
            interaction = composition.interactions.download,
        )
    }
    addSingletonFactory<EntryDownloadRuntimeFeature> { get<EntryDownloadRuntimeCoordinator>() }
    addSingletonFactory<EntryDownloadActionFeature> {
        val composition = get<EntryInteractionComposition>()
        DefaultEntryDownloadActionFeature(
            evaluation = composition.featureGraphEvaluation,
            interaction = composition.interactions.download,
        )
    }
    addSingletonFactory<EntryAutomaticDownloadFeature> {
        val composition = get<EntryInteractionComposition>()
        DefaultEntryAutomaticDownloadFeature(
            evaluation = composition.featureGraphEvaluation,
            interaction = composition.interactions.download,
            sharedPolicy = get(),
        )
    }
    addSingletonFactory<EntryDownloadLifecycleFeature> {
        val composition = get<EntryInteractionComposition>()
        DefaultEntryDownloadLifecycleFeature(
            evaluation = composition.featureGraphEvaluation,
            downloadPreferences = get(),
            getCategories = get(),
            getEntryWithChapters = get(),
            entryRepository = get(),
            downloads = composition.interactions.download,
        )
    }
    addSingletonFactory<EntryDownloadOptionsFeature> {
        val composition = get<EntryInteractionComposition>()
        DefaultEntryDownloadOptionsFeature(
            evaluation = composition.featureGraphEvaluation,
            interaction = composition.interactions.download,
        )
    }
    addSingletonFactory<EntryDownloadSettingsFeature> {
        val composition = get<EntryInteractionComposition>()
        DefaultEntryDownloadSettingsFeature(composition.featureGraphEvaluation)
    }
    addSingletonFactory<EntryDownloadMaintenanceFeature> {
        val composition = get<EntryInteractionComposition>()
        DefaultEntryDownloadMaintenanceFeature(
            evaluation = composition.featureGraphEvaluation,
            interaction = composition.interactions.download,
        )
    }
    addSingletonFactory<EntryConsumptionFeature> {
        val composition = get<EntryInteractionComposition>()
        DefaultEntryConsumptionFeature(
            evaluation = composition.featureGraphEvaluation,
            interaction = composition.interactions.consumption,
            downloadLifecycle = get(),
        )
    }
    addSingletonFactory<EntryBookmarkFeature> {
        val composition = get<EntryInteractionComposition>()
        DefaultEntryBookmarkFeature(
            evaluation = composition.featureGraphEvaluation,
            interaction = composition.interactions.bookmark,
        )
    }
    addSingletonFactory<EntryUpdateEligibilityFeature> {
        val composition = get<EntryInteractionComposition>()
        val preferences = get<LibraryPreferences>()
        DefaultEntryUpdateEligibilityFeature(
            evaluation = composition.featureGraphEvaluation,
            currentPolicy = {
                preferences.autoUpdateEntryRestrictions.get().toEntryUpdateEligibilityPolicy()
            },
        )
    }
    addSingletonFactory<EntryProgressFeature> {
        val composition = get<EntryInteractionComposition>()
        DefaultEntryProgressFeature(
            evaluation = composition.featureGraphEvaluation,
            interaction = composition.interactions.progress,
        )
    }
    addSingletonFactory<EntryPlaybackPreferencesFeature> {
        val composition = get<EntryInteractionComposition>()
        DefaultEntryPlaybackPreferencesFeature(
            evaluation = composition.featureGraphEvaluation,
            interaction = composition.interactions.playbackPreferences,
        )
    }
    addSingletonFactory<EntryChildListFeature> {
        val composition = get<EntryInteractionComposition>()
        DefaultEntryChildListFeature(
            evaluation = composition.featureGraphEvaluation,
            childList = composition.interactions.childList,
            childProgress = composition.interactions.childProgress,
        )
    }
    addSingletonFactory<EntryLibraryFilterFeature> {
        val composition = get<EntryInteractionComposition>()
        DefaultEntryLibraryFilterFeature(composition.featureGraphEvaluation)
    }
    addSingletonFactory<EntryChildGroupFilterFeature> {
        val composition = get<EntryInteractionComposition>()
        DefaultEntryChildGroupFilterFeature(
            evaluation = composition.featureGraphEvaluation,
            interaction = composition.interactions.childGroupFilter,
            dataSource = get(),
        )
    }
    addSingletonFactory<EntryPreviewFeature> {
        val composition = get<EntryInteractionComposition>()
        DefaultEntryPreviewFeature(
            evaluation = composition.featureGraphEvaluation,
            interaction = composition.interactions.preview,
            childList = get(),
        )
    }
    addSingletonFactory<EntryImmersiveFeature> {
        val composition = get<EntryInteractionComposition>()
        DefaultEntryImmersiveFeature(
            evaluation = composition.featureGraphEvaluation,
            interaction = composition.interactions.immersive,
            childList = get(),
        )
    }
    addSingletonFactory<EntryRelatedEntriesFeature> {
        val composition = get<EntryInteractionComposition>()
        DefaultEntryRelatedEntriesFeature(
            evaluation = composition.featureGraphEvaluation,
            sourceManager = get(),
            networkToLocalEntry = get(),
            getEntry = get(),
        )
    }
    addSingletonFactory {
        EntryDownloadNotificationManager(
            context = app,
            downloads = get<EntryDownloadRuntimeCoordinator>(),
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

private fun Set<String>.toEntryUpdateEligibilityPolicy(): EntryUpdateEligibilityPolicy {
    return EntryUpdateEligibilityPolicy(
        skipCompleted = LibraryPreferences.ENTRY_NON_COMPLETED in this,
        skipWhenUnconsumed = LibraryPreferences.ENTRY_HAS_UNCONSUMED in this,
        skipWhenNotStarted = LibraryPreferences.ENTRY_NON_STARTED in this,
        skipOutsideReleasePeriod = LibraryPreferences.ENTRY_OUTSIDE_RELEASE_PERIOD in this,
    )
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
