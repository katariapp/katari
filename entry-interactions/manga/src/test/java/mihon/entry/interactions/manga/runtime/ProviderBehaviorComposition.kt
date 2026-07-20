package mihon.entry.interactions.manga

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
        featureContributors = providerBehaviorContributors(plugins),
    )
}

private fun providerBehaviorContributors(plugins: List<EntryInteractionPlugin>): List<FeatureGraphContributor> {
    val owner = ContributionOwner("test.manga-provider-behavior")
    val capabilities = plugins
        .flatMap(EntryInteractionPlugin::providerBindings)
        .map { it.graphProvider.capability }
        .distinctBy { it.id }
    val specializedRequirements = plugins
        .flatMap(EntryInteractionPlugin::specializedAdapters)
        .map { it.definition }
        .distinctBy { it.id }
    val capabilityContributor = featureGraphContributor(owner) {
        add(
            FeatureContribution(
                feature = FeatureId("test.manga-provider-behavior"),
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
    val specializedContributors = specializedRequirements
        .groupBy { it.owner }
        .map { (requirementOwner, requirements) ->
            featureGraphContributor(requirementOwner) {
                add(
                    FeatureContribution(
                        feature = FeatureId("test.${requirementOwner.value}.specialized-behavior"),
                        owner = requirementOwner,
                        integrations = requirements.map { requirement ->
                            FeatureIntegration(
                                id = FeatureIntegrationId("test.${requirement.id.value}"),
                                prerequisites = CapabilityExpression.Always,
                                specializedRequirements = listOf(requirement),
                            )
                        },
                    ),
                )
            }
        }
    return listOf(capabilityContributor) + specializedContributors
}
