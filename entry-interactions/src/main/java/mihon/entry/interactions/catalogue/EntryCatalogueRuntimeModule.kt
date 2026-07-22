package mihon.entry.interactions

import tachiyomi.domain.source.service.EntrySourceDescriptionResolutionPort
import uy.kohesive.injekt.api.addSingletonFactory
import uy.kohesive.injekt.api.get

internal val EntryCatalogueFeatureRuntimeModule = EntryFeatureRuntimeModule(
    id = "entry.catalogue",
    contributor = EntryCatalogueFeatureContributor,
) {
    addSingletonFactory<EntryCatalogueFeature> {
        DefaultEntryCatalogueFeature(get<EntryInteractionComposition>().featureGraphEvaluation)
    }
    addSingletonFactory<EntrySourceDescriptionResolutionPort> { get<EntryCatalogueFeature>() }
    EntryFeatureRuntimeArtifacts(
        runtimeBoundaries = listOf(entryFeatureRuntimeBoundary { get<EntryCatalogueFeature>() }),
    )
}
