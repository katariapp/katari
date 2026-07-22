package mihon.entry.interactions

import mihon.feature.graph.FeatureExecutionHandler
import mihon.feature.graph.FeatureExecutionParticipantBinding
import uy.kohesive.injekt.api.addSingletonFactory
import uy.kohesive.injekt.api.get

internal val EntryTrackingFeatureRuntimeModule = EntryFeatureRuntimeModule(
    id = "entry.tracking",
    contributor = EntryTrackingFeatureContributor,
    additionalContributors = listOf(
        EntryTrackingLibraryMembershipContributor,
        EntryTrackingProfileMoveContributor,
    ),
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
            FeatureExecutionParticipantBinding(
                definition = ENTRY_TRACKING_PROFILE_MOVE_PARTICIPANT,
                handler = FeatureExecutionHandler { event ->
                    context.dependencies.profileMoveTrackingStateHost.move(event.plan.stateRequest())
                },
            ),
        ),
        runtimeBoundaries = listOf(entryFeatureRuntimeBoundary { get<EntryTrackingFeature>() }),
    )
}
