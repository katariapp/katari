package mihon.entry.interactions

import eu.kanade.tachiyomi.source.entry.EntryType
import mihon.entry.interactions.documentation.EntryContentTypeReferenceSection
import mihon.entry.interactions.documentation.entryContentTypeReferenceContribution
import mihon.feature.graph.CapabilityExpression
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

internal val ENTRY_BOOKMARK_FEATURE_ID = FeatureId("entry.bookmarking")
private val FEATURE_OWNER = ContributionOwner("entry-bookmarking")
private val ENTRY_BOOKMARK_REFERENCE = entryContentTypeReferenceContribution(
    id = "bookmarking",
    owner = FEATURE_OWNER,
    section = EntryContentTypeReferenceSection.ENTRY_INTERACTIONS,
    label = "Bookmark individual child items",
    order = 600,
)
internal val ENTRY_BOOKMARK_PROVIDER_INTEGRATION = FeatureIntegrationId("entry.bookmarking.provider")
internal val ENTRY_BOOKMARK_AVAILABILITY_INTEGRATION = FeatureIntegrationId("entry.bookmarking.availability")
internal val ENTRY_BOOKMARK_MUTATION_INTEGRATION = FeatureIntegrationId("entry.bookmarking.mutation")

internal object EntryBookmarkProviderBehaviorContract : FeatureBehaviorContract {
    override val id = FeatureArtifactId("entry.bookmarking.provider-behavior")
}

internal object EntryBookmarkAvailabilityBehaviorContract : FeatureBehaviorContract {
    override val id = FeatureArtifactId("entry.bookmarking.availability-behavior")
}

internal object EntryBookmarkMutationBehaviorContract : FeatureBehaviorContract {
    override val id = FeatureArtifactId("entry.bookmarking.mutation-behavior")
}

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

internal val ENTRY_BOOKMARK_SELECTION_CHANGE_CONTEXT = contextInputDefinition<Boolean>(
    ContextInputId("entry.bookmarking.selection-change"),
    ContributionOwner("entry-selection"),
)
internal val ENTRY_BOOKMARK_MUTATION_CHANGE_CONTEXT = contextInputDefinition<Boolean>(
    ContextInputId("entry.bookmarking.mutation-change"),
    ContributionOwner("entry-state"),
)
private val SELECTION_NO_CHANGE_BLOCKER = FeatureContextBlocker(
    FeatureArtifactId("entry.bookmarking.selection-no-change"),
    listOf(ENTRY_BOOKMARK_SELECTION_CHANGE_CONTEXT),
)
private val MUTATION_NO_CHANGE_BLOCKER = FeatureContextBlocker(
    FeatureArtifactId("entry.bookmarking.mutation-no-change"),
    listOf(ENTRY_BOOKMARK_MUTATION_CHANGE_CONTEXT),
)

internal object EntryBookmarkFeatureContributor : FeatureGraphContributor {
    override val owner = FEATURE_OWNER

    override fun contributeTo(sink: FeatureGraphContributionSink) {
        val bookmark = CapabilityExpression.Provided(EntryBookmarkCapability.definition)
        sink.add(
            FeatureContribution(
                feature = ENTRY_BOOKMARK_FEATURE_ID,
                owner = owner,
                integrations = listOf(
                    FeatureIntegration(
                        id = ENTRY_BOOKMARK_PROVIDER_INTEGRATION,
                        prerequisites = bookmark,
                        sharedConsequences = EntryBookmarkProviderConsequence.entries,
                        behavioralContracts = listOf(EntryBookmarkProviderBehaviorContract),
                        projectionRequirements = listOf(ENTRY_BOOKMARK_REFERENCE.requirement),
                        projections = listOf(ENTRY_BOOKMARK_REFERENCE.projection),
                    ),
                    FeatureIntegration(
                        id = ENTRY_BOOKMARK_AVAILABILITY_INTEGRATION,
                        prerequisites = bookmark,
                        contextInputs = listOf(ENTRY_BOOKMARK_SELECTION_CHANGE_CONTEXT),
                        contextRule = featureContextRule(owner) { evidence ->
                            if (evidence.value(ENTRY_BOOKMARK_SELECTION_CHANGE_CONTEXT)) {
                                FeatureContextDecision.Applicable
                            } else {
                                FeatureContextDecision.Blocked(listOf(SELECTION_NO_CHANGE_BLOCKER))
                            }
                        },
                        contextBlockers = listOf(SELECTION_NO_CHANGE_BLOCKER),
                        sharedConsequences = listOf(EntryBookmarkAvailabilityConsequence),
                        behavioralContracts = listOf(EntryBookmarkAvailabilityBehaviorContract),
                    ),
                    FeatureIntegration(
                        id = ENTRY_BOOKMARK_MUTATION_INTEGRATION,
                        prerequisites = bookmark,
                        contextInputs = listOf(ENTRY_BOOKMARK_MUTATION_CHANGE_CONTEXT),
                        contextRule = featureContextRule(owner) { evidence ->
                            if (evidence.value(ENTRY_BOOKMARK_MUTATION_CHANGE_CONTEXT)) {
                                FeatureContextDecision.Applicable
                            } else {
                                FeatureContextDecision.Blocked(listOf(MUTATION_NO_CHANGE_BLOCKER))
                            }
                        },
                        contextBlockers = listOf(MUTATION_NO_CHANGE_BLOCKER),
                        sharedConsequences = listOf(EntryBookmarkMutationConsequence),
                        behavioralContracts = listOf(EntryBookmarkMutationBehaviorContract),
                    ),
                ),
            ),
        )
    }
}

internal fun FeatureGraphEvaluation.bookmarkTypes(): Set<EntryType> =
    applicableProviderTypes<EntryBookmarkProcessor>(
        feature = ENTRY_BOOKMARK_FEATURE_ID,
        integration = ENTRY_BOOKMARK_PROVIDER_INTEGRATION,
        consequence = EntryBookmarkProviderConsequence.PROVIDER_DISPATCH.id,
    )

internal fun FeatureGraphEvaluation.requireBookmarkAvailabilityContext(type: EntryType, canChange: Boolean) {
    requireEntryContextState(
        type = type,
        feature = ENTRY_BOOKMARK_FEATURE_ID,
        integration = ENTRY_BOOKMARK_AVAILABILITY_INTEGRATION,
        consequences = listOf(EntryBookmarkAvailabilityConsequence.id),
        evidence = listOf(contextEvidence(ENTRY_BOOKMARK_SELECTION_CHANGE_CONTEXT, canChange)),
        applicable = canChange,
    )
}

internal fun FeatureGraphEvaluation.requireBookmarkMutationContext(type: EntryType, canChange: Boolean) {
    requireEntryContextState(
        type = type,
        feature = ENTRY_BOOKMARK_FEATURE_ID,
        integration = ENTRY_BOOKMARK_MUTATION_INTEGRATION,
        consequences = listOf(EntryBookmarkMutationConsequence.id),
        evidence = listOf(contextEvidence(ENTRY_BOOKMARK_MUTATION_CHANGE_CONTEXT, canChange)),
        applicable = canChange,
    )
}
