package mihon.entry.interactions

import mihon.entry.interactions.anime.animeEntryTypeRuntimeModule
import mihon.entry.interactions.book.bookEntryTypeRuntimeModule
import mihon.entry.interactions.manga.mangaEntryTypeRuntimeModule
import mihon.feature.graph.FeatureGraphContributor
import tachiyomi.core.common.preference.ProfilePreferenceOwnerInstaller

/** The application composition boundary through which installed entry types and Features enter the graph. */
internal fun productionEntryTypeRuntimeModules(
    profilePreferenceOwners: ProfilePreferenceOwnerInstaller,
): List<EntryTypeRuntimeModule> = listOf(
    mangaEntryTypeRuntimeModule(profilePreferenceOwners),
    animeEntryTypeRuntimeModule(profilePreferenceOwners),
    bookEntryTypeRuntimeModule(profilePreferenceOwners),
)

/** The sole production installation boundary for Feature graph declarations and their runtime artifacts. */
internal fun productionEntryFeatureRuntimeModules(): List<EntryFeatureRuntimeModule> = listOf(
    EntryOpenFeatureRuntimeModule,
    EntryContinueFeatureRuntimeModule,
    EntryDownloadRuntimeFeatureRuntimeModule,
    EntryDownloadActionFeatureRuntimeModule,
    EntryAutomaticDownloadFeatureRuntimeModule,
    EntryDownloadLifecycleFeatureRuntimeModule,
    EntryDownloadConfigurationFeatureRuntimeModule,
    EntryDownloadMaintenanceFeatureRuntimeModule,
    EntryConsumptionFeatureRuntimeModule,
    EntryBookmarkFeatureRuntimeModule,
    EntryUpdateEligibilityFeatureRuntimeModule,
    EntryProgressFeatureRuntimeModule,
    EntryPlaybackPreferencesFeatureRuntimeModule,
    EntryChildListFeatureRuntimeModule,
    EntryLibraryFilterFeatureRuntimeModule,
    EntryChildGroupFilterFeatureRuntimeModule,
    EntryPreviewFeatureRuntimeModule,
    EntryImmersiveFeatureRuntimeModule,
    EntryRelatedEntriesFeatureRuntimeModule,
    EntryLibraryProgressFeatureRuntimeModule,
    EntryLibraryUpdateRefreshFeatureRuntimeModule,
    EntryTypePresentationFeatureRuntimeModule,
    EntryLibraryUpdateNotificationFeatureRuntimeModule,
    EntryViewerSettingsFeatureRuntimeModule,
    EntryMediaCacheFeatureRuntimeModule,
    EntryMergeFeatureRuntimeModule,
    EntryMigrationFeatureRuntimeModule,
    EntryCatalogueFeatureRuntimeModule,
    EntrySourceSettingsFeatureRuntimeModule,
    EntrySourceHomeFeatureRuntimeModule,
    EntryCoverNetworkFeatureRuntimeModule,
    EntrySourceRefreshFeatureRuntimeModule,
    EntryWebViewFeatureRuntimeModule,
    EntryDeepLinkFeatureRuntimeModule,
    EntryTrackerSourceAdapterFeatureRuntimeModule,
    EntryTrackingFeatureRuntimeModule,
)

/** Graph-only view for cross-module content-type validation. Production runtime must install the modules themselves. */
fun productionEntryFeatureGraphForValidation(): List<FeatureGraphContributor> {
    return productionEntryFeatureRuntimeModules().map(EntryFeatureRuntimeModule::contributor)
}
