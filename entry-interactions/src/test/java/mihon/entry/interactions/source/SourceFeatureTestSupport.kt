package mihon.entry.interactions

import eu.kanade.tachiyomi.source.entry.EntryType
import mihon.feature.graph.ContributionOwner
import mihon.feature.graph.FeatureGraphContributor
import mihon.feature.graph.FeatureGraphEvaluation

internal fun sourceFeatureEvaluation(vararg contributors: FeatureGraphContributor): FeatureGraphEvaluation {
    val plugin = object : EntryInteractionPlugin {
        override val type = EntryType.BOOK
        override val owner = ContributionOwner("test.source-feature")
        override val providerBindings = emptyList<EntryInteractionProviderBinding<*>>()
    }
    return createEntryInteractionComposition(
        plugins = listOf(plugin),
        featureContributors = contributors.toList(),
    ).featureGraphEvaluation
}
