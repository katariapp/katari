package mihon.entry.interactions.manga

import mihon.entry.interactions.EntryInteractionPlugin
import mihon.entry.interactions.EntryInteractions
import mihon.entry.interactions.productionEntryFeatureGraphForValidation

internal fun createEntryInteractions(plugins: List<EntryInteractionPlugin>): EntryInteractions {
    return mihon.entry.interactions.createEntryInteractions(
        plugins = plugins,
        featureContributors = productionEntryFeatureGraphForValidation(),
    )
}
