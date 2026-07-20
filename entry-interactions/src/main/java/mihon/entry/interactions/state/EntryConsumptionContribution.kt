package mihon.entry.interactions

import eu.kanade.tachiyomi.source.entry.EntryType
import mihon.feature.graph.CapabilityExpression
import mihon.feature.graph.ContextInputId
import mihon.feature.graph.ContributionOwner
import mihon.feature.graph.FeatureArtifactId
import mihon.feature.graph.FeatureContextBlocker
import mihon.feature.graph.FeatureContextDecision
import mihon.feature.graph.FeatureContribution
import mihon.feature.graph.FeatureGraphContributionSink
import mihon.feature.graph.FeatureGraphContributor
import mihon.feature.graph.FeatureGraphEvaluation
import mihon.feature.graph.FeatureId
import mihon.feature.graph.FeatureIntegration
import mihon.feature.graph.FeatureIntegrationId
import mihon.feature.graph.SharedFeatureConsequence
import mihon.feature.graph.contextEvidence
import mihon.feature.graph.contextInputDefinition
import mihon.feature.graph.featureContextRule

private val FEATURE_ID = FeatureId("entry.consumption")
private val FEATURE_OWNER = ContributionOwner("entry-consumption")
private val PROVIDER_INTEGRATION = FeatureIntegrationId("entry.consumption.provider")
private val ELIGIBILITY_INTEGRATION = FeatureIntegrationId("entry.consumption.eligibility")
private val MUTATION_RESULT_INTEGRATION = FeatureIntegrationId("entry.consumption.mutation-result")
private val LIFECYCLE_INTEGRATION = FeatureIntegrationId("entry.consumption.download-lifecycle")

private enum class EntryConsumptionProviderConsequence(
    override val id: FeatureArtifactId,
) : SharedFeatureConsequence {
    TYPE_APPLICABILITY(FeatureArtifactId("entry.consumption.type-applicability")),
    PROVIDER_DISPATCH(FeatureArtifactId("entry.consumption.provider-dispatch")),
}

private enum class EntryConsumptionEligibilityConsequence(
    override val id: FeatureArtifactId,
) : SharedFeatureConsequence {
    ENTRY_ACTIONS(FeatureArtifactId("entry.consumption.entry-actions")),
    LIBRARY_ACTIONS(FeatureArtifactId("entry.consumption.library-actions")),
    UPDATE_ACTIONS(FeatureArtifactId("entry.consumption.update-actions")),
    NOTIFICATION_ACTION(FeatureArtifactId("entry.consumption.notification-action")),
    TRACKING_SYNC(FeatureArtifactId("entry.consumption.tracking-sync")),
}

private object EntryConsumptionLifecycleConsequence : SharedFeatureConsequence {
    override val id = FeatureArtifactId("entry.consumption.download-lifecycle-event")
}

private object EntryConsumptionMutationConsequence : SharedFeatureConsequence {
    override val id = FeatureArtifactId("entry.consumption.mutation")
}

private val STATE_CHANGE_CONTEXT = contextInputDefinition<Boolean>(
    ContextInputId("entry.consumption.state-change"),
    ContributionOwner("entry-state"),
)
private val CHANGED_CHILDREN_CONTEXT = contextInputDefinition<Boolean>(
    ContextInputId("entry.consumption.changed-children"),
    ContributionOwner("entry-state"),
)
private val REQUESTED_CONSUMED_CONTEXT = contextInputDefinition<Boolean>(
    ContextInputId("entry.consumption.requested-consumed"),
    ContributionOwner("entry-selection"),
)
private val STATE_NO_CHANGE_BLOCKER = FeatureContextBlocker(
    FeatureArtifactId("entry.consumption.state-no-change"),
    listOf(STATE_CHANGE_CONTEXT),
)
private val NO_CHANGED_CHILDREN_BLOCKER = FeatureContextBlocker(
    FeatureArtifactId("entry.consumption.no-changed-children"),
    listOf(CHANGED_CHILDREN_CONTEXT),
)
private val LIFECYCLE_REQUIRES_CONSUMED_BLOCKER = FeatureContextBlocker(
    FeatureArtifactId("entry.consumption.lifecycle-requires-consumed"),
    listOf(REQUESTED_CONSUMED_CONTEXT),
)

internal object EntryConsumptionFeatureContributor : FeatureGraphContributor {
    override val owner = FEATURE_OWNER

    override fun contributeTo(sink: FeatureGraphContributionSink) {
        val consumption = CapabilityExpression.Provided(EntryConsumptionCapability.definition)
        sink.add(
            FeatureContribution(
                feature = FEATURE_ID,
                owner = owner,
                integrations = listOf(
                    FeatureIntegration(
                        id = PROVIDER_INTEGRATION,
                        prerequisites = consumption,
                        sharedConsequences = EntryConsumptionProviderConsequence.entries,
                    ),
                    FeatureIntegration(
                        id = ELIGIBILITY_INTEGRATION,
                        prerequisites = consumption,
                        contextInputs = listOf(STATE_CHANGE_CONTEXT),
                        contextRule = featureContextRule(owner) { evidence ->
                            if (evidence.value(STATE_CHANGE_CONTEXT)) {
                                FeatureContextDecision.Applicable
                            } else {
                                FeatureContextDecision.Blocked(listOf(STATE_NO_CHANGE_BLOCKER))
                            }
                        },
                        contextBlockers = listOf(STATE_NO_CHANGE_BLOCKER),
                        sharedConsequences = EntryConsumptionEligibilityConsequence.entries,
                    ),
                    FeatureIntegration(
                        id = MUTATION_RESULT_INTEGRATION,
                        prerequisites = consumption,
                        contextInputs = listOf(CHANGED_CHILDREN_CONTEXT),
                        contextRule = featureContextRule(owner) { evidence ->
                            if (evidence.value(CHANGED_CHILDREN_CONTEXT)) {
                                FeatureContextDecision.Applicable
                            } else {
                                FeatureContextDecision.Blocked(listOf(NO_CHANGED_CHILDREN_BLOCKER))
                            }
                        },
                        contextBlockers = listOf(NO_CHANGED_CHILDREN_BLOCKER),
                        sharedConsequences = listOf(EntryConsumptionMutationConsequence),
                    ),
                    FeatureIntegration(
                        id = LIFECYCLE_INTEGRATION,
                        prerequisites = consumption,
                        contextInputs = listOf(CHANGED_CHILDREN_CONTEXT, REQUESTED_CONSUMED_CONTEXT),
                        contextRule = featureContextRule(owner) { evidence ->
                            when {
                                !evidence.value(CHANGED_CHILDREN_CONTEXT) ->
                                    FeatureContextDecision.Blocked(listOf(NO_CHANGED_CHILDREN_BLOCKER))
                                !evidence.value(REQUESTED_CONSUMED_CONTEXT) ->
                                    FeatureContextDecision.Blocked(listOf(LIFECYCLE_REQUIRES_CONSUMED_BLOCKER))
                                else -> FeatureContextDecision.Applicable
                            }
                        },
                        contextBlockers = listOf(NO_CHANGED_CHILDREN_BLOCKER, LIFECYCLE_REQUIRES_CONSUMED_BLOCKER),
                        sharedConsequences = listOf(EntryConsumptionLifecycleConsequence),
                    ),
                ),
            ),
        )
    }
}

internal fun FeatureGraphEvaluation.consumptionTypes(): Set<EntryType> =
    applicableProviderTypes<EntryConsumptionProcessor>(
        feature = FEATURE_ID,
        integration = PROVIDER_INTEGRATION,
        consequence = EntryConsumptionProviderConsequence.PROVIDER_DISPATCH.id,
    )

internal fun FeatureGraphEvaluation.requireConsumptionEligibilityContext(type: EntryType, canChange: Boolean) {
    requireEntryContextState(
        type = type,
        feature = FEATURE_ID,
        integration = ELIGIBILITY_INTEGRATION,
        consequences = EntryConsumptionEligibilityConsequence.entries.map(EntryConsumptionEligibilityConsequence::id),
        evidence = listOf(contextEvidence(STATE_CHANGE_CONTEXT, canChange)),
        applicable = canChange,
    )
}

internal fun FeatureGraphEvaluation.requireConsumptionLifecycleContext(
    type: EntryType,
    changed: Boolean,
    consumed: Boolean,
) {
    requireEntryContextState(
        type = type,
        feature = FEATURE_ID,
        integration = LIFECYCLE_INTEGRATION,
        consequences = listOf(EntryConsumptionLifecycleConsequence.id),
        evidence = listOf(
            contextEvidence(CHANGED_CHILDREN_CONTEXT, changed),
            contextEvidence(REQUESTED_CONSUMED_CONTEXT, consumed),
        ),
        applicable = changed && consumed,
    )
}

internal fun FeatureGraphEvaluation.requireConsumptionMutationContext(type: EntryType, changed: Boolean) {
    requireEntryContextState(
        type = type,
        feature = FEATURE_ID,
        integration = MUTATION_RESULT_INTEGRATION,
        consequences = listOf(EntryConsumptionMutationConsequence.id),
        evidence = listOf(contextEvidence(CHANGED_CHILDREN_CONTEXT, changed)),
        applicable = changed,
    )
}
