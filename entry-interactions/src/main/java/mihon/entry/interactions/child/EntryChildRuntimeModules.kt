package mihon.entry.interactions

import uy.kohesive.injekt.api.addSingletonFactory
import uy.kohesive.injekt.api.get

internal val EntryChildListFeatureRuntimeModule = EntryFeatureRuntimeModule(
    id = "entry.child-list",
    contributor = EntryChildListFeatureContributor,
) {
    addSingletonFactory<EntryChildListFeature> {
        val composition = get<EntryInteractionComposition>()
        DefaultEntryChildListFeature(
            evaluation = composition.featureGraphEvaluation,
            childList = composition.interactions.childList,
            childProgress = composition.interactions.childProgress,
            missingChildGap = composition.interactions.missingChildGap,
        )
    }
    EntryFeatureRuntimeArtifacts(
        runtimeBoundaries = listOf(entryFeatureRuntimeBoundary { get<EntryChildListFeature>() }),
    )
}

internal val EntryChildGroupFilterFeatureRuntimeModule = EntryFeatureRuntimeModule(
    id = "entry.child-group-filter",
    contributor = EntryChildGroupFilterFeatureContributor,
) {
    addSingletonFactory<EntryChildGroupFilterFeature> {
        val composition = get<EntryInteractionComposition>()
        DefaultEntryChildGroupFilterFeature(
            evaluation = composition.featureGraphEvaluation,
            interaction = composition.interactions.childGroupFilter,
            dataSource = get(),
        )
    }
    EntryFeatureRuntimeArtifacts(
        runtimeBoundaries = listOf(entryFeatureRuntimeBoundary { get<EntryChildGroupFilterFeature>() }),
    )
}

internal val EntryRelatedEntriesFeatureRuntimeModule = EntryFeatureRuntimeModule(
    id = "entry.related-entries",
    contributor = EntryRelatedEntriesFeatureContributor,
) {
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
    EntryFeatureRuntimeArtifacts(
        runtimeBoundaries = listOf(entryFeatureRuntimeBoundary { get<EntryRelatedEntriesFeature>() }),
    )
}
