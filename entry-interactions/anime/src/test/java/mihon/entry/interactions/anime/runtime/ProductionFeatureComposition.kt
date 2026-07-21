package mihon.entry.interactions.anime

import mihon.entry.interactions.EntryInteractionPlugin
import mihon.entry.interactions.EntryInteractions
import mihon.entry.interactions.productionEntryFeatureContributors

internal fun createEntryInteractions(plugins: List<EntryInteractionPlugin>): EntryInteractions {
    return mihon.entry.interactions.createEntryInteractions(
        plugins = plugins,
        featureContributors = productionEntryFeatureContributors(),
    )
}
