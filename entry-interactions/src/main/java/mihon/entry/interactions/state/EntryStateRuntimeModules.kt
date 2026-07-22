package mihon.entry.interactions

import tachiyomi.domain.library.service.LibraryPreferences
import uy.kohesive.injekt.api.addSingletonFactory
import uy.kohesive.injekt.api.get

internal val EntryConsumptionFeatureRuntimeModule = EntryFeatureRuntimeModule(
    id = "entry.consumption",
    contributor = EntryConsumptionFeatureContributor,
) {
    addSingletonFactory<EntryConsumptionFeature> {
        val composition = get<EntryInteractionComposition>()
        DefaultEntryConsumptionFeature(
            evaluation = composition.featureGraphEvaluation,
            interaction = composition.interactions.consumption,
            downloadLifecycle = get(),
        )
    }
    EntryFeatureRuntimeArtifacts(
        runtimeBoundaries = listOf(entryFeatureRuntimeBoundary { get<EntryConsumptionFeature>() }),
    )
}

internal val EntryBookmarkFeatureRuntimeModule = EntryFeatureRuntimeModule(
    id = "entry.bookmarking",
    contributor = EntryBookmarkFeatureContributor,
) {
    addSingletonFactory<EntryBookmarkFeature> {
        val composition = get<EntryInteractionComposition>()
        DefaultEntryBookmarkFeature(
            evaluation = composition.featureGraphEvaluation,
            interaction = composition.interactions.bookmark,
        )
    }
    EntryFeatureRuntimeArtifacts(
        runtimeBoundaries = listOf(entryFeatureRuntimeBoundary { get<EntryBookmarkFeature>() }),
    )
}

internal val EntryUpdateEligibilityFeatureRuntimeModule = EntryFeatureRuntimeModule(
    id = "entry.update-eligibility",
    contributor = EntryUpdateEligibilityFeatureContributor,
) {
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
    EntryFeatureRuntimeArtifacts(
        runtimeBoundaries = listOf(entryFeatureRuntimeBoundary { get<EntryUpdateEligibilityFeature>() }),
    )
}

internal val EntryProgressFeatureRuntimeModule = EntryFeatureRuntimeModule(
    id = "entry.progress-transfer",
    contributor = EntryProgressFeatureContributor,
) {
    addSingletonFactory<EntryProgressFeature> {
        val composition = get<EntryInteractionComposition>()
        DefaultEntryProgressFeature(
            evaluation = composition.featureGraphEvaluation,
            interaction = composition.interactions.progress,
        )
    }
    EntryFeatureRuntimeArtifacts(
        runtimeBoundaries = listOf(entryFeatureRuntimeBoundary { get<EntryProgressFeature>() }),
    )
}

internal fun Set<String>.toEntryUpdateEligibilityPolicy(): EntryUpdateEligibilityPolicy {
    return EntryUpdateEligibilityPolicy(
        skipCompleted = LibraryPreferences.ENTRY_NON_COMPLETED in this,
        skipWhenUnconsumed = LibraryPreferences.ENTRY_HAS_UNCONSUMED in this,
        skipWhenNotStarted = LibraryPreferences.ENTRY_NON_STARTED in this,
        skipOutsideReleasePeriod = LibraryPreferences.ENTRY_OUTSIDE_RELEASE_PERIOD in this,
    )
}
