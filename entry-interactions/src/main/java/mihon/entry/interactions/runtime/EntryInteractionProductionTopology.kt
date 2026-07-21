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

/**
 * The application composition boundary for feature-owned contributions.
 *
 * This list installs independent contributors. It does not describe type support or contract applicability; those are
 * discovered from the contributors and the installed type modules by graph evaluation.
 */
internal fun productionEntryFeatureContributors(): List<FeatureGraphContributor> = listOf(
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
    EntryLibraryUpdateRefreshFeatureContributor,
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
    EntryTrackingFeatureContributor,
)
