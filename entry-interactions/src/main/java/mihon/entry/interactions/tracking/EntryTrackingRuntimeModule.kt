package mihon.entry.interactions

import uy.kohesive.injekt.api.addSingletonFactory
import uy.kohesive.injekt.api.get

internal val EntryTrackingFeatureRuntimeModule = EntryFeatureRuntimeModule(
    id = "entry.tracking",
    contributor = EntryTrackingFeatureContributor,
) { context ->
    addSingletonFactory<EntryTrackingFeature> {
        DefaultEntryTrackingFeature(
            evaluation = get<EntryInteractionComposition>().featureGraphEvaluation,
            host = context.dependencies.trackingHost,
        )
    }
    EntryFeatureRuntimeArtifacts(
        runtimeBoundaries = listOf(entryFeatureRuntimeBoundary { get<EntryTrackingFeature>() }),
    )
}
