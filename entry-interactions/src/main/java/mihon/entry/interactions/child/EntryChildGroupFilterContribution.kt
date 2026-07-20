package mihon.entry.interactions

import mihon.feature.graph.CapabilityExpression
import mihon.feature.graph.ContributionOwner
import mihon.feature.graph.FeatureArtifactId
import mihon.feature.graph.FeatureContribution
import mihon.feature.graph.FeatureGraphContributionSink
import mihon.feature.graph.FeatureGraphContributor
import mihon.feature.graph.FeatureGraphEvaluation
import mihon.feature.graph.FeatureId
import mihon.feature.graph.FeatureIntegration
import mihon.feature.graph.FeatureIntegrationId
import mihon.feature.graph.SharedFeatureConsequence

private val FEATURE_ID = FeatureId("entry.child-group-filter")
private val FEATURE_OWNER = ContributionOwner("entry-child-group-filter")
private val PROVIDER_INTEGRATION = FeatureIntegrationId("entry.child-group-filter.provider")

private enum class EntryChildGroupFilterConsequence(
    override val id: FeatureArtifactId,
) : SharedFeatureConsequence {
    STATE(FeatureArtifactId("entry.child-group-filter.state")),
    APPLY(FeatureArtifactId("entry.child-group-filter.apply")),
    PERSISTENCE(FeatureArtifactId("entry.child-group-filter.persistence")),
    BACKUP(FeatureArtifactId("entry.child-group-filter.backup")),
    SHARED_STORAGE(FeatureArtifactId("entry.child-group-filter.shared-storage-consumers")),
}

internal object EntryChildGroupFilterFeatureContributor : FeatureGraphContributor {
    override val owner = FEATURE_OWNER

    override fun contributeTo(sink: FeatureGraphContributionSink) {
        sink.add(
            FeatureContribution(
                feature = FEATURE_ID,
                owner = owner,
                integrations = listOf(
                    FeatureIntegration(
                        id = PROVIDER_INTEGRATION,
                        prerequisites = CapabilityExpression.Provided(EntryChildGroupFilterCapability.definition),
                        sharedConsequences = EntryChildGroupFilterConsequence.entries,
                    ),
                ),
            ),
        )
    }
}

internal fun FeatureGraphEvaluation.childGroupFilterProviderTypes() =
    EntryChildGroupFilterConsequence.entries
        .mapTo(mutableSetOf()) { consequence ->
            applicableProviderTypes<EntryChildGroupFilterProcessor>(
                feature = FEATURE_ID,
                integration = PROVIDER_INTEGRATION,
                consequence = consequence.id,
            )
        }
        .singleOrNull()
        ?: error("Child-group filtering consequences selected different provider sets")
