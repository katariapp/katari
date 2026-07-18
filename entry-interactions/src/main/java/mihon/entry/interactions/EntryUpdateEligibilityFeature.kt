package mihon.entry.interactions

import eu.kanade.tachiyomi.source.entry.EntryUpdateStrategy
import mihon.feature.graph.CapabilityExpression
import mihon.feature.graph.ContributionOwner
import mihon.feature.graph.FeatureArtifactId
import mihon.feature.graph.FeatureBehaviorContract
import mihon.feature.graph.FeatureContribution
import mihon.feature.graph.FeatureGraphContributionSink
import mihon.feature.graph.FeatureGraphContributor
import mihon.feature.graph.FeatureGraphEvaluation
import mihon.feature.graph.FeatureId
import mihon.feature.graph.FeatureIntegration
import mihon.feature.graph.FeatureIntegrationId
import mihon.feature.graph.SharedFeatureConsequence
import tachiyomi.domain.entry.model.EntryStatus

private val ENTRY_UPDATE_ELIGIBILITY_FEATURE_ID = FeatureId("entry.update-eligibility")
private val ENTRY_UPDATE_ELIGIBILITY_INTEGRATION_ID =
    FeatureIntegrationId("entry.update-eligibility.shared-policy")
private val ENTRY_UPDATE_ELIGIBILITY_FEATURE_OWNER = ContributionOwner("entry-update-eligibility")

private val ENTRY_UPDATE_ELIGIBILITY_POLICY_CONSEQUENCE_ID =
    FeatureArtifactId("entry.update-eligibility.policy")
private val ENTRY_UPDATE_ELIGIBILITY_LIBRARY_UPDATE_CONSEQUENCE_ID =
    FeatureArtifactId("entry.update-eligibility.library-update")
private val ENTRY_UPDATE_ELIGIBILITY_STATS_CONSEQUENCE_ID =
    FeatureArtifactId("entry.update-eligibility.stats")
private val ENTRY_UPDATE_ELIGIBILITY_SETTINGS_CONSEQUENCE_ID =
    FeatureArtifactId("entry.update-eligibility.smart-update-settings")
private val ENTRY_UPDATE_ELIGIBILITY_BEHAVIOR_CONTRACT_ID =
    FeatureArtifactId("entry.update-eligibility.behavior")

private object EntryUpdateEligibilityPolicyConsequence : SharedFeatureConsequence {
    override val id = ENTRY_UPDATE_ELIGIBILITY_POLICY_CONSEQUENCE_ID
}

private object EntryUpdateEligibilityLibraryUpdateConsequence : SharedFeatureConsequence {
    override val id = ENTRY_UPDATE_ELIGIBILITY_LIBRARY_UPDATE_CONSEQUENCE_ID
}

private object EntryUpdateEligibilityStatsConsequence : SharedFeatureConsequence {
    override val id = ENTRY_UPDATE_ELIGIBILITY_STATS_CONSEQUENCE_ID
}

private object EntryUpdateEligibilitySettingsConsequence : SharedFeatureConsequence {
    override val id = ENTRY_UPDATE_ELIGIBILITY_SETTINGS_CONSEQUENCE_ID
}

private object EntryUpdateEligibilityBehaviorContract : FeatureBehaviorContract {
    override val id = ENTRY_UPDATE_ELIGIBILITY_BEHAVIOR_CONTRACT_ID
}

internal object EntryUpdateEligibilityFeatureContributor : FeatureGraphContributor {
    override val owner = ENTRY_UPDATE_ELIGIBILITY_FEATURE_OWNER

    override fun contributeTo(sink: FeatureGraphContributionSink) {
        sink.add(
            FeatureContribution(
                feature = ENTRY_UPDATE_ELIGIBILITY_FEATURE_ID,
                owner = owner,
                integrations = listOf(
                    FeatureIntegration(
                        id = ENTRY_UPDATE_ELIGIBILITY_INTEGRATION_ID,
                        prerequisites = CapabilityExpression.Always,
                        sharedConsequences = listOf(
                            EntryUpdateEligibilityPolicyConsequence,
                            EntryUpdateEligibilityLibraryUpdateConsequence,
                            EntryUpdateEligibilityStatsConsequence,
                            EntryUpdateEligibilitySettingsConsequence,
                        ),
                        behavioralContracts = listOf(EntryUpdateEligibilityBehaviorContract),
                    ),
                ),
            ),
        )
    }
}

internal data class EntryUpdateEligibilityPolicy(
    val skipCompleted: Boolean,
    val skipWhenUnconsumed: Boolean,
    val skipWhenNotStarted: Boolean,
    val skipOutsideReleasePeriod: Boolean,
)

internal class DefaultEntryUpdateEligibilityFeature(
    evaluation: FeatureGraphEvaluation,
    private val currentPolicy: () -> EntryUpdateEligibilityPolicy,
) : EntryUpdateEligibilityFeature {
    private val selectedTypesByConsequence = listOf(
        ENTRY_UPDATE_ELIGIBILITY_POLICY_CONSEQUENCE_ID,
        ENTRY_UPDATE_ELIGIBILITY_LIBRARY_UPDATE_CONSEQUENCE_ID,
        ENTRY_UPDATE_ELIGIBILITY_STATS_CONSEQUENCE_ID,
        ENTRY_UPDATE_ELIGIBILITY_SETTINGS_CONSEQUENCE_ID,
    ).associateWith { consequence ->
        evaluation.sharedConsequences
            .asSequence()
            .filter { applicability ->
                applicability.subject.feature == ENTRY_UPDATE_ELIGIBILITY_FEATURE_ID &&
                    applicability.subject.integration == ENTRY_UPDATE_ELIGIBILITY_INTEGRATION_ID &&
                    applicability.consequence.id == consequence
            }
            .mapTo(mutableSetOf()) { it.subject.contentType }
    }
    private val selectedTypes = selectedTypesByConsequence.getValue(ENTRY_UPDATE_ELIGIBILITY_POLICY_CONSEQUENCE_ID)

    init {
        check(selectedTypesByConsequence.values.toSet().size == 1) {
            "Update eligibility consequences selected different content types"
        }
    }

    override fun evaluate(request: EntryUpdateEligibilityRequest): EntryUpdateEligibility {
        val contentType = request.entry.type.toContentTypeId()
        check(contentType in selectedTypes) {
            "Entry type ${request.entry.type} was not contributed to the update eligibility feature graph"
        }

        val policy = currentPolicy()
        val fetchWindowUpperBound = request.fetchWindowUpperBound
        return when {
            request.entry.updateStrategy == EntryUpdateStrategy.ONLY_FETCH_ONCE &&
                request.totalCount?.let { it > 0L } == true -> {
                EntryUpdateEligibility.Skipped(EntryUpdateSkipReason.NOT_ALWAYS_UPDATE)
            }
            policy.skipCompleted && request.entry.status == EntryStatus.COMPLETED -> {
                EntryUpdateEligibility.Skipped(EntryUpdateSkipReason.COMPLETED)
            }
            policy.skipWhenUnconsumed && request.unconsumedCount?.let { it != 0L } == true -> {
                EntryUpdateEligibility.Skipped(EntryUpdateSkipReason.NOT_CAUGHT_UP)
            }
            policy.skipWhenNotStarted &&
                request.totalCount?.let { it > 0L } == true &&
                request.hasStarted == false -> {
                EntryUpdateEligibility.Skipped(EntryUpdateSkipReason.NOT_STARTED)
            }
            policy.skipOutsideReleasePeriod &&
                fetchWindowUpperBound != null &&
                request.entry.nextUpdate > fetchWindowUpperBound -> {
                EntryUpdateEligibility.Skipped(EntryUpdateSkipReason.OUTSIDE_RELEASE_PERIOD)
            }
            else -> EntryUpdateEligibility.Eligible
        }
    }
}
