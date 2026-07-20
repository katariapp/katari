package mihon.entry.interactions

import android.app.Application
import coil3.ComponentRegistry
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import mihon.entry.interactions.anime.animeEntryTypeRuntimeModule
import mihon.entry.interactions.book.bookEntryTypeRuntimeModule
import mihon.entry.interactions.host.EntryMergeHost
import mihon.entry.interactions.host.EntryMigrationConsequenceHost
import mihon.entry.interactions.host.EntryMigrationCustomCoverHost
import mihon.entry.interactions.host.EntryMigrationExecutionHost
import mihon.entry.interactions.host.EntryMigrationPreparationHost
import mihon.entry.interactions.manga.mangaEntryTypeRuntimeModule
import mihon.entry.interactions.reader.settings.ReaderBasePreferences
import mihon.entry.interactions.settings.DefaultViewerSettingBinder
import mihon.entry.interactions.settings.EntryInteractionPreferences
import mihon.entry.viewer.settings.ViewerSettingBinder
import mihon.entry.viewer.settings.ViewerSettingOverrideRepository
import tachiyomi.core.common.preference.PreferenceStore
import tachiyomi.core.common.preference.ProfilePreferenceOwnerId
import tachiyomi.core.common.preference.ProfilePreferenceOwnerInstaller
import tachiyomi.domain.entry.model.Entry
import tachiyomi.domain.entry.repository.EntryRepository
import tachiyomi.domain.entry.service.EntryChildOwnershipResolutionPort
import tachiyomi.domain.entry.service.EntryLibraryGroupingResolutionPort
import tachiyomi.domain.entry.service.EntryLibraryProgressResolutionPort
import tachiyomi.domain.library.service.LibraryPreferences
import tachiyomi.domain.source.service.EntrySourceDescriptionResolutionPort
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
    val basePreferenceStore: PreferenceStore,
    val profilePreferenceOwners: ProfilePreferenceOwnerInstaller,
    val viewerSettingsScreenProjections: List<EntryViewerSettingsScreenProjection>,
    val sourceRefreshUpdateLibraryTitles: (profileId: Long) -> Boolean,
    val mergeHost: EntryMergeHost,
    val mergeLibraryEntryInitializer: suspend (Entry) -> Unit,
    val mergeCoverCleanup: suspend (Entry) -> Unit,
    val migrationPreparationHost: EntryMigrationPreparationHost,
    val migrationExecutionHost: EntryMigrationExecutionHost,
    val migrationConsequenceHost: EntryMigrationConsequenceHost,
    val migrationCustomCoverHost: EntryMigrationCustomCoverHost,
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
    val entryInteractionPreferencesOwner = dependencies.profilePreferenceOwners.register(
        ProfilePreferenceOwnerId("entry-interactions.preview"),
        factory = ::EntryInteractionPreferences,
    )
    addSingletonFactory { entryInteractionPreferencesOwner.create() }
    addSingletonFactory<ViewerSettingBinder> {
        DefaultViewerSettingBinder(
            overrideRepository = get<ViewerSettingOverrideRepository>(),
            scope = CoroutineScope(SupervisorJob() + Dispatchers.Default),
        )
    }
    val typeRuntimeContributions = listOf(
        mangaEntryTypeRuntimeModule(dependencies.profilePreferenceOwners),
        animeEntryTypeRuntimeModule(dependencies.profilePreferenceOwners),
        bookEntryTypeRuntimeModule(dependencies.profilePreferenceOwners),
    ).map { module ->
        module.install(this, app).also { it.validate(module.type) }
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
                EntryTypePresentationFeatureContributor,
                EntryLibraryUpdateNotificationFeatureContributor,
                EntryViewerSettingsFeatureContributor,
                EntryMediaCacheFeatureContributor,
                EntryMergeFeatureContributor,
                EntryMigrationFeatureContributor,
                EntryCatalogueFeatureContributor,
                EntrySourceSettingsFeatureContributor,
                EntrySourceHomeFeatureContributor,
                EntryCoverNetworkFeatureContributor,
                EntrySourceRefreshFeatureContributor,
                EntryWebViewFeatureContributor,
                EntryDeepLinkFeatureContributor,
                EntryTrackerSourceAdapterFeatureContributor,
            ),
        )
    }
    addSingletonFactory<EntryCatalogueFeature> {
        DefaultEntryCatalogueFeature(get<EntryInteractionComposition>().featureGraphEvaluation)
    }
    addSingletonFactory<EntrySourceDescriptionResolutionPort> { get<EntryCatalogueFeature>() }
    addSingletonFactory<EntrySourceSettingsFeature> {
        DefaultEntrySourceSettingsFeature(
            evaluation = get<EntryInteractionComposition>().featureGraphEvaluation,
            sourceManager = get(),
        )
    }
    addSingletonFactory<EntrySourceHomeFeature> {
        DefaultEntrySourceHomeFeature(
            evaluation = get<EntryInteractionComposition>().featureGraphEvaluation,
            sourceManager = get(),
        )
    }
    addSingletonFactory<EntryCoverNetworkFeature> {
        DefaultEntryCoverNetworkFeature(
            evaluation = get<EntryInteractionComposition>().featureGraphEvaluation,
            sourceManager = get(),
        )
    }
    addSingletonFactory<EntrySourceRefreshFeature> {
        DefaultEntrySourceRefreshFeature(
            evaluation = get<EntryInteractionComposition>().featureGraphEvaluation,
            sourceManager = get(),
            syncEntryWithSource = get(),
            updateLibraryTitles = dependencies.sourceRefreshUpdateLibraryTitles,
        )
    }
    addSingletonFactory<EntryWebViewFeature> {
        DefaultEntryWebViewFeature(
            evaluation = get<EntryInteractionComposition>().featureGraphEvaluation,
            sourceManager = get(),
        )
    }
    addSingletonFactory<EntryDeepLinkFeature> {
        DefaultEntryDeepLinkFeature(
            evaluation = get<EntryInteractionComposition>().featureGraphEvaluation,
            sourceManager = get(),
            networkToLocalEntry = get(),
            entryChapterRepository = get(),
            sourceRefresh = get(),
        )
    }
    addSingletonFactory<EntryTrackerSourceAdapterFeature> {
        DefaultEntryTrackerSourceAdapterFeature(
            evaluation = get<EntryInteractionComposition>().featureGraphEvaluation,
            sourceManager = get(),
            settings = get(),
            home = get(),
        )
    }
    addSingletonFactory {
        EntryMergeConsequenceDelivery(
            host = dependencies.mergeHost,
            libraryEntryInitializer = dependencies.mergeLibraryEntryInitializer,
            coverCleanup = dependencies.mergeCoverCleanup,
            downloadMaintenance = { get() },
        )
    }
    addSingletonFactory<EntryMergeFeature> {
        EntryMergeWorkflowCoordinator(
            evaluation = get<EntryInteractionComposition>().featureGraphEvaluation,
            host = dependencies.mergeHost,
            consequences = get(),
        )
    }
    addSingletonFactory<EntryMergeCandidateFeature> { EntryMergeCandidateCoordinator(dependencies.mergeHost) }
    addSingletonFactory<EntryMergeNavigationFeature> { EntryMergeNavigationCoordinator(dependencies.mergeHost) }
    addSingletonFactory { EntryMergeLibraryGroupingCoordinator(dependencies.mergeHost) }
    addSingletonFactory<EntryMergeLibraryGroupingFeature> { get<EntryMergeLibraryGroupingCoordinator>() }
    addSingletonFactory<EntryLibraryGroupingResolutionPort> { get<EntryMergeLibraryGroupingCoordinator>() }
    addSingletonFactory<EntryMergeBackupFeature> { EntryMergeBackupCoordinator(dependencies.mergeHost) }
    addSingletonFactory<EntryMergeLibraryLifecycleFeature> {
        EntryMergeLibraryLifecycleCoordinator(dependencies.mergeHost)
    }
    addSingletonFactory<EntryMergeMetadataRefreshFeature> {
        EntryMergeMetadataRefreshCoordinator(dependencies.mergeHost)
    }
    addSingletonFactory<EntryMergeProfileMoveFeature> { EntryMergeProfileMoveCoordinator(dependencies.mergeHost) }
    addSingletonFactory<EntryMergeConsequenceStatusFeature> {
        EntryMergeConsequenceStatusCoordinator(dependencies.mergeHost, get())
    }
    addSingletonFactory<EntryMergeMigrationFeature> { EntryMergeMigrationCoordinator(dependencies.mergeHost) }
    addSingletonFactory {
        EntryMigrationConsequenceDelivery(
            host = dependencies.migrationConsequenceHost,
            progress = { get() },
            playbackPreferences = { get() },
            viewerSettings = { get() },
            downloads = { get() },
            customCover = dependencies.migrationCustomCoverHost,
        )
    }
    addSingletonFactory<EntryMigrationConsequenceStatusFeature> {
        EntryMigrationConsequenceStatusCoordinator(dependencies.migrationConsequenceHost, get())
    }
    addSingletonFactory<EntryMigrationFeature> {
        DefaultEntryMigrationFeature(
            evaluation = get<EntryInteractionComposition>().featureGraphEvaluation,
            preparationHost = dependencies.migrationPreparationHost,
            executionHost = dependencies.migrationExecutionHost,
            mergeMigration = get(),
            progress = get(),
            playbackPreferences = get(),
            viewerSettings = get(),
            downloads = get(),
            customCover = dependencies.migrationCustomCoverHost,
            consequences = get(),
        )
    }
    addSingletonFactory<EntryMergeChildOwnershipProjection> {
        EntryMergeChildOwnershipCoordinator(dependencies.mergeHost)
    }
    addSingletonFactory<EntryChildOwnershipResolutionPort> { get<EntryMergeChildOwnershipProjection>() }
    addSingletonFactory<EntryMergeDownloadOwnershipProjection> {
        EntryMergeDownloadOwnershipCoordinator(dependencies.mergeHost)
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
    addSingletonFactory<EntryViewerSettingsFeature> {
        val composition = get<EntryInteractionComposition>()
        DefaultEntryViewerSettingsFeature(
            evaluation = composition.featureGraphEvaluation,
            interaction = composition.interactions.viewerSettings,
            projections = dependencies.viewerSettingsScreenProjections,
            overrideRepository = get<ViewerSettingOverrideRepository>(),
            legacyMangaViewerFlagsReset = EntryLegacyMangaViewerFlagsReset {
                get<EntryRepository>().resetViewerFlags()
            },
            migrationStore = EntryViewerFlagsMigrationStore { entryId, profileId, viewerFlags ->
                val repository = get<EntryRepository>()
                repository.getEntryById(entryId, profileId)
                    ?.let { current -> repository.update(current.copy(viewerFlags = viewerFlags), profileId) }
                    ?: false
            },
        )
    }
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
            ownership = get(),
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
            sourceRefresh = get(),
        )
    }
    addSingletonFactory<EntryRelatedEntriesFeature> {
        val composition = get<EntryInteractionComposition>()
        DefaultEntryRelatedEntriesFeature(
            evaluation = composition.featureGraphEvaluation,
            sourceManager = get(),
            networkToLocalEntry = get(),
            getEntry = get(),
            sourceDescription = get(),
        )
    }
    addSingletonFactory<EntryTypePresentationFeature> {
        val composition = get<EntryInteractionComposition>()
        DefaultEntryTypePresentationFeature(
            evaluation = composition.featureGraphEvaluation,
            interaction = composition.interactions.typePresentation,
        )
    }
    addSingletonFactory<EntryLibraryUpdateNotificationFeature> {
        val composition = get<EntryInteractionComposition>()
        DefaultEntryLibraryUpdateNotificationFeature(
            evaluation = composition.featureGraphEvaluation,
            presentationFeature = get(),
            openFeature = get(),
            consumptionFeature = get(),
            downloadActionFeature = get(),
            resolveVisibleEntry = { entry ->
                val visibleEntryId = get<EntryMergeNavigationFeature>()
                    .resolveNavigation(EntryMergeSubject(entry.profileId, entry.id))
                    .visibleEntryId
                get<EntryRepository>().getEntryById(visibleEntryId, entry.profileId) ?: entry
            },
        )
    }
    addSingletonFactory<EntryMediaCacheFeature> {
        val composition = get<EntryInteractionComposition>()
        DefaultEntryMediaCacheFeature(
            evaluation = composition.featureGraphEvaluation,
            interaction = composition.interactions.mediaCache,
            preferenceStore = dependencies.basePreferenceStore,
        )
    }
    addSingletonFactory {
        EntryDownloadNotificationManager(
            context = app,
            downloads = get<EntryDownloadRuntimeCoordinator>(),
            actions = dependencies.notificationActions,
            ownership = get(),
        )
    }
    addSingletonFactory<EntryDownloadForegroundNotificationProvider> { get<EntryDownloadNotificationManager>() }
    addSingletonFactory<EntryInteractionRuntimeWarmup> {
        EntryInteractionRuntimeWarmup {
            get<EntryViewerSettingsFeature>()
            typeRuntimeContributions.flatMap(EntryTypeRuntimeContribution::warmups).forEach { it() }
            get<EntryDownloadNotificationManager>().start()
            CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
                get<EntryMergeConsequenceDelivery>().runRetryLoop()
            }
            CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
                get<EntryMigrationConsequenceDelivery>().runRetryLoop()
            }
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
