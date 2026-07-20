package mihon.entry.interactions

import mihon.feature.graph.ApplicableFeatureContext
import mihon.feature.graph.BlockedFeatureContext
import mihon.feature.graph.ContextEvidence
import mihon.feature.graph.FeatureArtifactId
import mihon.feature.graph.FeatureGraphEvaluation
import mihon.feature.graph.FeatureId
import mihon.feature.graph.FeatureIntegrationId
import mihon.feature.graph.resolveFeatureContext

internal fun FeatureGraphEvaluation.requireSourceContextState(
    feature: FeatureId,
    integration: FeatureIntegrationId,
    consequence: FeatureArtifactId,
    evidence: List<ContextEvidence<*>>,
    applicable: Boolean,
) {
    val subjects = integrations
        .map { it.subject }
        .filter { it.feature == feature && it.integration == integration }
    check(subjects.isNotEmpty()) { "Source integration $feature:$integration was not discovered" }

    subjects.forEach { subject ->
        val resolution = resolveFeatureContext(
            evaluation = this,
            contentType = subject.contentType,
            feature = feature,
            integration = integration,
            evidence = evidence,
        )
        val hasConsequence = resolution.sharedConsequences.any { it.consequence.id == consequence }
        val matches = if (applicable) {
            resolution.integration is ApplicableFeatureContext && hasConsequence
        } else {
            resolution.integration is BlockedFeatureContext && !hasConsequence
        }
        check(matches) {
            "Source integration $feature:$integration resolved inconsistently for ${subject.contentType}: " +
                "${resolution.integration}, consequences=${resolution.sharedConsequences}"
        }
    }
}
