package mihon.entry.interactions

import eu.kanade.tachiyomi.source.entry.EntryType
import mihon.feature.graph.CapabilityExpression
import mihon.feature.graph.ContentTypeId
import mihon.feature.graph.ContextInputId
import mihon.feature.graph.ContributionOwner
import mihon.feature.graph.FeatureArtifactId
import mihon.feature.graph.FeatureBehaviorContract
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

private val FEATURE_ID = FeatureId("entry.update-eligibility")
private val FEATURE_OWNER = ContributionOwner("entry-update-eligibility")
private val POLICY_INTEGRATION = FeatureIntegrationId("entry.update-eligibility.shared-policy")
private val DECISION_INTEGRATION = FeatureIntegrationId("entry.update-eligibility.decision")

private enum class EntryUpdateEligibilityPolicyConsequence(
    override val id: FeatureArtifactId,
) : SharedFeatureConsequence {
    TYPE_PARTICIPATION(FeatureArtifactId("entry.update-eligibility.type-participation")),
    POLICY_AVAILABILITY(FeatureArtifactId("entry.update-eligibility.policy-availability")),
    SMART_UPDATE_SETTINGS(FeatureArtifactId("entry.update-eligibility.smart-update-settings")),
}

private enum class EntryUpdateEligibilityDecisionConsequence(
    override val id: FeatureArtifactId,
) : SharedFeatureConsequence {
    POLICY_DECISION(FeatureArtifactId("entry.update-eligibility.policy")),
    LIBRARY_UPDATE(FeatureArtifactId("entry.update-eligibility.library-update")),
    STATS(FeatureArtifactId("entry.update-eligibility.stats")),
}

private object EntryUpdateEligibilityBehaviorContract : FeatureBehaviorContract {
    override val id = FeatureArtifactId("entry.update-eligibility.behavior")
}

internal data class EntryUpdateEligibilityPolicy(
    val skipCompleted: Boolean,
    val skipWhenUnconsumed: Boolean,
    val skipWhenNotStarted: Boolean,
    val skipOutsideReleasePeriod: Boolean,
)

internal data class EntryUpdateEligibilityContext(
    val oneShotAlreadyFetched: Boolean,
    val completed: Boolean,
    val hasUnconsumed: Boolean,
    val notStartedWithChildren: Boolean,
    val outsideReleasePeriod: Boolean,
)

private val POLICY_CONTEXT = contextInputDefinition<EntryUpdateEligibilityPolicy>(
    ContextInputId("entry.update-eligibility.configuration"),
    ContributionOwner("entry-update-configuration"),
)
private val ONE_SHOT_CONTEXT = contextInputDefinition<Boolean>(
    ContextInputId("entry.update-eligibility.one-shot-already-fetched"),
    ContributionOwner("entry-state"),
)
private val COMPLETED_CONTEXT = contextInputDefinition<Boolean>(
    ContextInputId("entry.update-eligibility.completed"),
    ContributionOwner("entry-state"),
)
private val UNCONSUMED_CONTEXT = contextInputDefinition<Boolean>(
    ContextInputId("entry.update-eligibility.has-unconsumed"),
    ContributionOwner("entry-state"),
)
private val NOT_STARTED_CONTEXT = contextInputDefinition<Boolean>(
    ContextInputId("entry.update-eligibility.not-started-with-children"),
    ContributionOwner("entry-state"),
)
private val RELEASE_PERIOD_CONTEXT = contextInputDefinition<Boolean>(
    ContextInputId("entry.update-eligibility.outside-release-period"),
    ContributionOwner("entry-update-window"),
)

private val NOT_ALWAYS_UPDATE_BLOCKER = FeatureContextBlocker(
    FeatureArtifactId("entry.update-eligibility.not-always-update"),
    listOf(ONE_SHOT_CONTEXT),
)
private val COMPLETED_BLOCKER = FeatureContextBlocker(
    FeatureArtifactId("entry.update-eligibility.skip-completed"),
    listOf(POLICY_CONTEXT, COMPLETED_CONTEXT),
)
private val NOT_CAUGHT_UP_BLOCKER = FeatureContextBlocker(
    FeatureArtifactId("entry.update-eligibility.not-caught-up"),
    listOf(POLICY_CONTEXT, UNCONSUMED_CONTEXT),
)
private val NOT_STARTED_BLOCKER = FeatureContextBlocker(
    FeatureArtifactId("entry.update-eligibility.not-started"),
    listOf(POLICY_CONTEXT, NOT_STARTED_CONTEXT),
)
private val OUTSIDE_RELEASE_PERIOD_BLOCKER = FeatureContextBlocker(
    FeatureArtifactId("entry.update-eligibility.outside-release-period-blocker"),
    listOf(POLICY_CONTEXT, RELEASE_PERIOD_CONTEXT),
)

internal object EntryUpdateEligibilityFeatureContributor : FeatureGraphContributor {
    override val owner = FEATURE_OWNER

    override fun contributeTo(sink: FeatureGraphContributionSink) {
        sink.add(
            FeatureContribution(
                feature = FEATURE_ID,
                owner = owner,
                integrations = listOf(
                    FeatureIntegration(
                        id = POLICY_INTEGRATION,
                        prerequisites = CapabilityExpression.Always,
                        sharedConsequences = EntryUpdateEligibilityPolicyConsequence.entries,
                        behavioralContracts = listOf(EntryUpdateEligibilityBehaviorContract),
                    ),
                    FeatureIntegration(
                        id = DECISION_INTEGRATION,
                        prerequisites = CapabilityExpression.Always,
                        contextInputs = listOf(
                            POLICY_CONTEXT,
                            ONE_SHOT_CONTEXT,
                            COMPLETED_CONTEXT,
                            UNCONSUMED_CONTEXT,
                            NOT_STARTED_CONTEXT,
                            RELEASE_PERIOD_CONTEXT,
                        ),
                        contextRule = featureContextRule(owner) { evidence ->
                            val policy = evidence.value(POLICY_CONTEXT)
                            when {
                                evidence.value(ONE_SHOT_CONTEXT) ->
                                    FeatureContextDecision.Blocked(listOf(NOT_ALWAYS_UPDATE_BLOCKER))
                                policy.skipCompleted && evidence.value(COMPLETED_CONTEXT) ->
                                    FeatureContextDecision.Blocked(listOf(COMPLETED_BLOCKER))
                                policy.skipWhenUnconsumed && evidence.value(UNCONSUMED_CONTEXT) ->
                                    FeatureContextDecision.Blocked(listOf(NOT_CAUGHT_UP_BLOCKER))
                                policy.skipWhenNotStarted && evidence.value(NOT_STARTED_CONTEXT) ->
                                    FeatureContextDecision.Blocked(listOf(NOT_STARTED_BLOCKER))
                                policy.skipOutsideReleasePeriod && evidence.value(RELEASE_PERIOD_CONTEXT) ->
                                    FeatureContextDecision.Blocked(listOf(OUTSIDE_RELEASE_PERIOD_BLOCKER))
                                else -> FeatureContextDecision.Applicable
                            }
                        },
                        contextBlockers = listOf(
                            NOT_ALWAYS_UPDATE_BLOCKER,
                            COMPLETED_BLOCKER,
                            NOT_CAUGHT_UP_BLOCKER,
                            NOT_STARTED_BLOCKER,
                            OUTSIDE_RELEASE_PERIOD_BLOCKER,
                        ),
                        sharedConsequences = EntryUpdateEligibilityDecisionConsequence.entries,
                    ),
                ),
            ),
        )
    }
}

internal fun FeatureGraphEvaluation.updateEligibilityContentTypes(): Set<ContentTypeId> =
    sharedConsequences
        .asSequence()
        .filter { applicability ->
            applicability.subject.feature == FEATURE_ID &&
                applicability.subject.integration == POLICY_INTEGRATION &&
                applicability.consequence.id == EntryUpdateEligibilityPolicyConsequence.TYPE_PARTICIPATION.id
        }
        .mapTo(mutableSetOf()) { it.subject.contentType }

internal fun FeatureGraphEvaluation.requireUpdateEligibilityContext(
    type: EntryType,
    policy: EntryUpdateEligibilityPolicy,
    context: EntryUpdateEligibilityContext,
    applicable: Boolean,
) {
    requireEntryContextState(
        type = type,
        feature = FEATURE_ID,
        integration = DECISION_INTEGRATION,
        consequences = EntryUpdateEligibilityDecisionConsequence.entries.map(
            EntryUpdateEligibilityDecisionConsequence::id,
        ),
        evidence = listOf(
            contextEvidence(POLICY_CONTEXT, policy),
            contextEvidence(ONE_SHOT_CONTEXT, context.oneShotAlreadyFetched),
            contextEvidence(COMPLETED_CONTEXT, context.completed),
            contextEvidence(UNCONSUMED_CONTEXT, context.hasUnconsumed),
            contextEvidence(NOT_STARTED_CONTEXT, context.notStartedWithChildren),
            contextEvidence(RELEASE_PERIOD_CONTEXT, context.outsideReleasePeriod),
        ),
        applicable = applicable,
    )
}
