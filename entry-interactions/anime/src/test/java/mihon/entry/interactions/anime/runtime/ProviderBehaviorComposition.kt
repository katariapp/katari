package mihon.entry.interactions.anime

import mihon.entry.interactions.EntryInteractionPlugin
import mihon.entry.interactions.EntryInteractions
import mihon.feature.graph.CapabilityExpression
import mihon.feature.graph.ContributionOwner
import mihon.feature.graph.FeatureArtifactId
import mihon.feature.graph.FeatureContribution
import mihon.feature.graph.FeatureGraphContributor
import mihon.feature.graph.FeatureId
import mihon.feature.graph.FeatureIntegration
import mihon.feature.graph.FeatureIntegrationId
import mihon.feature.graph.SharedFeatureConsequence
import mihon.feature.graph.featureGraphContributor

internal fun createEntryInteractions(plugins: List<EntryInteractionPlugin>): EntryInteractions {
    return mihon.entry.interactions.createEntryInteractions(
        plugins = plugins,
        featureContributors = listOf(providerBehaviorContributor(plugins)),
    )
}

private fun providerBehaviorContributor(plugins: List<EntryInteractionPlugin>): FeatureGraphContributor {
    val owner = ContributionOwner("test.anime-provider-behavior")
    val capabilities = plugins
        .flatMap(EntryInteractionPlugin::providerBindings)
        .map { it.graphProvider.capability }
        .distinctBy { it.id }
    return featureGraphContributor(owner) {
        add(
            FeatureContribution(
                feature = FeatureId("test.anime-provider-behavior"),
                owner = owner,
                integrations = capabilities.map { capability ->
                    val artifactId = FeatureArtifactId("test.${capability.id.value}.behavior")
                    FeatureIntegration(
                        id = FeatureIntegrationId("test.${capability.id.value}"),
                        prerequisites = CapabilityExpression.Provided(capability),
                        sharedConsequences = listOf(
                            object : SharedFeatureConsequence {
                                override val id = artifactId
                            },
                        ),
                    )
                },
            ),
        )
    }
}
