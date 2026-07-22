package mihon.entry.interactions

import mihon.feature.graph.FeatureExecutionHandler
import mihon.feature.graph.FeatureExecutionParticipantBinding
import uy.kohesive.injekt.api.addSingletonFactory
import uy.kohesive.injekt.api.get

internal val EntryTrackingFeatureRuntimeModule = EntryFeatureRuntimeModule(
    id = "entry.tracking",
    contributor = EntryTrackingFeatureContributor,
    additionalContributors = listOf(EntryTrackingLibraryMembershipContributor),
) { context ->
    addSingletonFactory<EntryTrackingFeature> {
        DefaultEntryTrackingFeature(
            evaluation = get<EntryInteractionComposition>().featureGraphEvaluation,
            host = context.dependencies.trackingHost,
        )
    }
    EntryFeatureRuntimeArtifacts(
        executionBindings = listOf(
            FeatureExecutionParticipantBinding(
                definition = ENTRY_TRACKING_LIBRARY_ADDITION_PARTICIPANT,
                handler = FeatureExecutionHandler { event ->
                    get<EntryTrackingFeature>().bindAutomatically(event.entry)
                },
            ),
        ),
        runtimeBoundaries = listOf(entryFeatureRuntimeBoundary { get<EntryTrackingFeature>() }),
    )
}
