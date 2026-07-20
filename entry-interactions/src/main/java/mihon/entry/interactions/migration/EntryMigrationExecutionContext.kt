package mihon.entry.interactions

import eu.kanade.tachiyomi.source.entry.EntryType
import mihon.feature.graph.CapabilityExpression
import mihon.feature.graph.ContextInputId
import mihon.feature.graph.ContributionOwner
import mihon.feature.graph.FeatureArtifactId
import mihon.feature.graph.FeatureContextBlocker
import mihon.feature.graph.FeatureContextDecision
import mihon.feature.graph.FeatureGraphEvaluation
import mihon.feature.graph.FeatureIntegration
import mihon.feature.graph.FeatureIntegrationId
import mihon.feature.graph.SharedFeatureConsequence
import mihon.feature.graph.contextEvidence
import mihon.feature.graph.contextInputDefinition
import mihon.feature.graph.featureContextRule

private val EXECUTION_CONTEXT_INTEGRATION = FeatureIntegrationId("entry.migration.execution-context")

private object EntryMigrationExecutionConsequence : SharedFeatureConsequence {
    override val id = FeatureArtifactId("entry.migration.execution")
}

private val PAIR_PRESENT_CONTEXT = contextInputDefinition<Boolean>(
    ContextInputId("entry.migration.execution-pair-present"),
    ContributionOwner("entry-migration-host"),
)
private val AUTHORIZATION_STABLE_CONTEXT = contextInputDefinition<Boolean>(
    ContextInputId("entry.migration.execution-authorization-stable"),
    ContributionOwner("entry-migration-host"),
)
private val PAIR_MISSING_BLOCKER = FeatureContextBlocker(
    FeatureArtifactId("entry.migration.execution-pair-missing"),
    listOf(PAIR_PRESENT_CONTEXT),
)
private val AUTHORIZATION_CHANGED_BLOCKER = FeatureContextBlocker(
    FeatureArtifactId("entry.migration.execution-authorization-changed"),
    listOf(AUTHORIZATION_STABLE_CONTEXT),
)

internal fun entryMigrationExecutionContextIntegration(
    owner: ContributionOwner,
    migration: CapabilityExpression,
) = FeatureIntegration(
    id = EXECUTION_CONTEXT_INTEGRATION,
    prerequisites = migration,
    contextInputs = listOf(PAIR_PRESENT_CONTEXT, AUTHORIZATION_STABLE_CONTEXT),
    contextRule = featureContextRule(owner) { evidence ->
        when {
            !evidence.value(PAIR_PRESENT_CONTEXT) ->
                FeatureContextDecision.Blocked(listOf(PAIR_MISSING_BLOCKER))
            !evidence.value(AUTHORIZATION_STABLE_CONTEXT) ->
                FeatureContextDecision.Blocked(listOf(AUTHORIZATION_CHANGED_BLOCKER))
            else -> FeatureContextDecision.Applicable
        }
    },
    contextBlockers = listOf(PAIR_MISSING_BLOCKER, AUTHORIZATION_CHANGED_BLOCKER),
    sharedConsequences = listOf(EntryMigrationExecutionConsequence),
)

internal fun FeatureGraphEvaluation.requireMigrationExecutionContext(
    type: EntryType,
    pairPresent: Boolean,
    authorizationStable: Boolean,
) {
    requireEntryContextState(
        type = type,
        feature = ENTRY_MIGRATION_FEATURE_ID,
        integration = EXECUTION_CONTEXT_INTEGRATION,
        consequences = listOf(EntryMigrationExecutionConsequence.id),
        evidence = listOf(
            contextEvidence(PAIR_PRESENT_CONTEXT, pairPresent),
            contextEvidence(AUTHORIZATION_STABLE_CONTEXT, authorizationStable),
        ),
        applicable = pairPresent && authorizationStable,
    )
}
