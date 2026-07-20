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

private val FEATURE_ID = FeatureId("entry.bookmarking")
private val FEATURE_OWNER = ContributionOwner("entry-bookmarking")
private val PROVIDER_INTEGRATION = FeatureIntegrationId("entry.bookmarking.provider")
private val AVAILABILITY_INTEGRATION = FeatureIntegrationId("entry.bookmarking.availability")
private val MUTATION_INTEGRATION = FeatureIntegrationId("entry.bookmarking.mutation")

private enum class EntryBookmarkProviderConsequence(
    override val id: FeatureArtifactId,
) : SharedFeatureConsequence {
    TYPE_APPLICABILITY(FeatureArtifactId("entry.bookmarking.type-applicability")),
    PROVIDER_DISPATCH(FeatureArtifactId("entry.bookmarking.provider-dispatch")),
}

private object EntryBookmarkAvailabilityConsequence : SharedFeatureConsequence {
    override val id = FeatureArtifactId("entry.bookmarking.eligibility")
}

private object EntryBookmarkMutationConsequence : SharedFeatureConsequence {
    override val id = FeatureArtifactId("entry.bookmarking.mutation")
}

private val SELECTION_CHANGE_CONTEXT = contextInputDefinition<Boolean>(
    ContextInputId("entry.bookmarking.selection-change"),
    ContributionOwner("entry-selection"),
)
private val MUTATION_CHANGE_CONTEXT = contextInputDefinition<Boolean>(
    ContextInputId("entry.bookmarking.mutation-change"),
    ContributionOwner("entry-state"),
)
private val SELECTION_NO_CHANGE_BLOCKER = FeatureContextBlocker(
    FeatureArtifactId("entry.bookmarking.selection-no-change"),
    listOf(SELECTION_CHANGE_CONTEXT),
)
private val MUTATION_NO_CHANGE_BLOCKER = FeatureContextBlocker(
    FeatureArtifactId("entry.bookmarking.mutation-no-change"),
    listOf(MUTATION_CHANGE_CONTEXT),
)

internal object EntryBookmarkFeatureContributor : FeatureGraphContributor {
    override val owner = FEATURE_OWNER

    override fun contributeTo(sink: FeatureGraphContributionSink) {
        val bookmark = CapabilityExpression.Provided(EntryBookmarkCapability.definition)
        sink.add(
            FeatureContribution(
                feature = FEATURE_ID,
                owner = owner,
                integrations = listOf(
                    FeatureIntegration(
                        id = PROVIDER_INTEGRATION,
                        prerequisites = bookmark,
                        sharedConsequences = EntryBookmarkProviderConsequence.entries,
                    ),
                    FeatureIntegration(
                        id = AVAILABILITY_INTEGRATION,
                        prerequisites = bookmark,
                        contextInputs = listOf(SELECTION_CHANGE_CONTEXT),
                        contextRule = featureContextRule(owner) { evidence ->
                            if (evidence.value(SELECTION_CHANGE_CONTEXT)) {
                                FeatureContextDecision.Applicable
                            } else {
                                FeatureContextDecision.Blocked(listOf(SELECTION_NO_CHANGE_BLOCKER))
                            }
                        },
                        contextBlockers = listOf(SELECTION_NO_CHANGE_BLOCKER),
                        sharedConsequences = listOf(EntryBookmarkAvailabilityConsequence),
                    ),
                    FeatureIntegration(
                        id = MUTATION_INTEGRATION,
                        prerequisites = bookmark,
                        contextInputs = listOf(MUTATION_CHANGE_CONTEXT),
                        contextRule = featureContextRule(owner) { evidence ->
                            if (evidence.value(MUTATION_CHANGE_CONTEXT)) {
                                FeatureContextDecision.Applicable
                            } else {
                                FeatureContextDecision.Blocked(listOf(MUTATION_NO_CHANGE_BLOCKER))
                            }
                        },
                        contextBlockers = listOf(MUTATION_NO_CHANGE_BLOCKER),
                        sharedConsequences = listOf(EntryBookmarkMutationConsequence),
                    ),
                ),
            ),
        )
    }
}

internal fun FeatureGraphEvaluation.bookmarkTypes(): Set<EntryType> =
    applicableProviderTypes<EntryBookmarkProcessor>(
        feature = FEATURE_ID,
        integration = PROVIDER_INTEGRATION,
        consequence = EntryBookmarkProviderConsequence.PROVIDER_DISPATCH.id,
    )

internal fun FeatureGraphEvaluation.requireBookmarkAvailabilityContext(type: EntryType, canChange: Boolean) {
    requireEntryContextState(
        type = type,
        feature = FEATURE_ID,
        integration = AVAILABILITY_INTEGRATION,
        consequences = listOf(EntryBookmarkAvailabilityConsequence.id),
        evidence = listOf(contextEvidence(SELECTION_CHANGE_CONTEXT, canChange)),
        applicable = canChange,
    )
}

internal fun FeatureGraphEvaluation.requireBookmarkMutationContext(type: EntryType, canChange: Boolean) {
    requireEntryContextState(
        type = type,
        feature = FEATURE_ID,
        integration = MUTATION_INTEGRATION,
        consequences = listOf(EntryBookmarkMutationConsequence.id),
        evidence = listOf(contextEvidence(MUTATION_CHANGE_CONTEXT, canChange)),
        applicable = canChange,
    )
}
