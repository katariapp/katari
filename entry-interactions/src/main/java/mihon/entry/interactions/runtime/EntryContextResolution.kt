package mihon.entry.interactions

import eu.kanade.tachiyomi.source.entry.EntryType
import mihon.feature.graph.ApplicableFeatureContext
import mihon.feature.graph.BlockedFeatureContext
import mihon.feature.graph.ContextEvidence
import mihon.feature.graph.FeatureArtifactId
import mihon.feature.graph.FeatureGraphEvaluation
import mihon.feature.graph.FeatureId
import mihon.feature.graph.FeatureIntegrationId
import mihon.feature.graph.resolveFeatureContext

internal fun FeatureGraphEvaluation.requireEntryContextState(
    type: EntryType,
    feature: FeatureId,
    integration: FeatureIntegrationId,
    consequences: Collection<FeatureArtifactId>,
    evidence: List<ContextEvidence<*>>,
    applicable: Boolean,
) {
    val resolution = resolveFeatureContext(
        evaluation = this,
        contentType = type.toContentTypeId(),
        feature = feature,
        integration = integration,
        evidence = evidence,
    )
    val resolvedConsequences = resolution.sharedConsequences.mapTo(mutableSetOf()) { it.consequence.id }
    val expectedConsequences = consequences.toSet()
    val matches = if (applicable) {
        resolution.integration is ApplicableFeatureContext && resolvedConsequences == expectedConsequences
    } else {
        resolution.integration is BlockedFeatureContext && resolvedConsequences.isEmpty()
    }
    check(matches) {
        "Entry integration $feature:$integration resolved inconsistently for $type: " +
            "${resolution.integration}, consequences=$resolvedConsequences, expected=$expectedConsequences"
    }
}
