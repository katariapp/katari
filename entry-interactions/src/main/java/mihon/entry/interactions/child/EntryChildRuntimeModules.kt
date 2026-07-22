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
    additionalContributors = listOf(EntryChildGroupFilterProfileMoveContributor),
) { context ->
    addSingletonFactory<EntryChildGroupFilterFeature> {
        val composition = get<EntryInteractionComposition>()
        DefaultEntryChildGroupFilterFeature(
            evaluation = composition.featureGraphEvaluation,
            interaction = composition.interactions.childGroupFilter,
            dataSource = get(),
        )
    }
    EntryFeatureRuntimeArtifacts(
        executionBindings = listOf(
            mihon.feature.graph.FeatureExecutionParticipantBinding(
                definition = ENTRY_CHILD_GROUP_FILTER_PROFILE_MOVE_PARTICIPANT,
                handler = mihon.feature.graph.FeatureExecutionHandler { event ->
                    context.dependencies.profileMoveChildGroupFilterStateHost.move(event.plan.stateRequest())
                },
            ),
        ),
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
