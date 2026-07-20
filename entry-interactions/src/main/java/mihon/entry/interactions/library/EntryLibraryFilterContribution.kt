package mihon.entry.interactions

import eu.kanade.tachiyomi.source.entry.EntryType
import mihon.feature.graph.CapabilityExpression
import mihon.feature.graph.ContentTypeId
import mihon.feature.graph.ContextInputId
import mihon.feature.graph.ContributionOwner
import mihon.feature.graph.FeatureArtifactId
import mihon.feature.graph.FeatureBehaviorContract
import mihon.feature.graph.FeatureContextDecision
import mihon.feature.graph.FeatureContribution
import mihon.feature.graph.FeatureGraphContributionSink
import mihon.feature.graph.FeatureGraphContributor
import mihon.feature.graph.FeatureGraphEvaluation
import mihon.feature.graph.FeatureId
import mihon.feature.graph.FeatureIntegration
import mihon.feature.graph.FeatureIntegrationId
import mihon.feature.graph.SharedFeatureConsequence
import mihon.feature.graph.allOf
import mihon.feature.graph.contextEvidence
import mihon.feature.graph.contextInputDefinition
import mihon.feature.graph.featureContextRule

private val FEATURE_ID = FeatureId("entry.library-filtering")
private val FEATURE_OWNER = ContributionOwner("entry-library-filtering")
private val PARTICIPATION_INTEGRATION = FeatureIntegrationId("entry.library-filtering.participation")
private val FILTER_CONTEXT_INTEGRATION = FeatureIntegrationId("entry.library-filtering.context")
private val BOOKMARK_INTEGRATION = FeatureIntegrationId("entry.library-filtering.bookmark-control")
private val PROGRESS_INTEGRATION = FeatureIntegrationId("entry.library-filtering.progress-controls")
private val RELEASE_PERIOD_INTEGRATION = FeatureIntegrationId("entry.library-filtering.outside-release-period")

private object EntryLibraryFilterParticipationConsequence : SharedFeatureConsequence {
    override val id = FeatureArtifactId("entry.library-filtering.type-participation")
}

private enum class EntryLibraryFilterContextConsequence(
    override val id: FeatureArtifactId,
) : SharedFeatureConsequence {
    POLICY(FeatureArtifactId("entry.library-filtering.policy")),
    MATCHING(FeatureArtifactId("entry.library-filtering.matching")),
    ACTIVE_STATE(FeatureArtifactId("entry.library-filtering.active-state")),
}

private object EntryLibraryFilterBookmarkControlConsequence : SharedFeatureConsequence {
    override val id = FeatureArtifactId("entry.library-filtering.bookmark-control")
}

private object EntryLibraryFilterProgressControlConsequence : SharedFeatureConsequence {
    override val id = FeatureArtifactId("entry.library-filtering.progress-controls")
}

private object EntryLibraryFilterReleasePeriodConsequence : SharedFeatureConsequence {
    override val id = FeatureArtifactId("entry.library-filtering.outside-release-period")
}

private object EntryLibraryFilterBehaviorContract : FeatureBehaviorContract {
    override val id = FeatureArtifactId("entry.library-filtering.behavior")
}

internal data class EntryLibraryFilterStateContext(
    val targetCount: Int,
    val hasUnknownProgress: Boolean,
    val hasUnknownBookmarks: Boolean,
    val hasDownloadedOrLocal: Boolean,
    val hasOutsideReleasePeriod: Boolean,
)

internal data class EntryLibraryTrackingFilterContext(
    val configuredTrackerIds: Set<Long>,
    val targetTrackerIds: Set<Long>,
)

private val POLICY_CONTEXT = contextInputDefinition<EntryLibraryFilterPolicy>(
    ContextInputId("entry.library-filtering.preferences"),
    ContributionOwner("entry-library-filter-configuration"),
)
private val LIBRARY_STATE_CONTEXT = contextInputDefinition<EntryLibraryFilterStateContext>(
    ContextInputId("entry.library-filtering.library-state"),
    ContributionOwner("entry-library-state"),
)
private val TRACKING_CONTEXT = contextInputDefinition<EntryLibraryTrackingFilterContext>(
    ContextInputId("entry.library-filtering.tracking"),
    ContributionOwner("entry-tracking-state"),
)

internal object EntryLibraryFilterFeatureContributor : FeatureGraphContributor {
    override val owner = FEATURE_OWNER

    override fun contributeTo(sink: FeatureGraphContributionSink) {
        sink.add(
            FeatureContribution(
                feature = FEATURE_ID,
                owner = owner,
                integrations = listOf(
                    FeatureIntegration(
                        id = PARTICIPATION_INTEGRATION,
                        prerequisites = CapabilityExpression.Always,
                        sharedConsequences = listOf(EntryLibraryFilterParticipationConsequence),
                        behavioralContracts = listOf(EntryLibraryFilterBehaviorContract),
                    ),
                    FeatureIntegration(
                        id = FILTER_CONTEXT_INTEGRATION,
                        prerequisites = CapabilityExpression.Always,
                        contextInputs = listOf(POLICY_CONTEXT, LIBRARY_STATE_CONTEXT, TRACKING_CONTEXT),
                        contextRule = featureContextRule(owner) { FeatureContextDecision.Applicable },
                        sharedConsequences = EntryLibraryFilterContextConsequence.entries,
                    ),
                    FeatureIntegration(
                        id = PROGRESS_INTEGRATION,
                        prerequisites = CapabilityExpression.Provided(EntryLibraryProgressCapability.definition),
                        sharedConsequences = listOf(EntryLibraryFilterProgressControlConsequence),
                    ),
                    FeatureIntegration(
                        id = BOOKMARK_INTEGRATION,
                        prerequisites = allOf(
                            CapabilityExpression.Provided(EntryLibraryProgressCapability.definition),
                            CapabilityExpression.Provided(EntryBookmarkCapability.definition),
                        ),
                        sharedConsequences = listOf(EntryLibraryFilterBookmarkControlConsequence),
                    ),
                    FeatureIntegration(
                        id = RELEASE_PERIOD_INTEGRATION,
                        prerequisites = CapabilityExpression.Provided(
                            EntryOutsideReleasePeriodFilterCapability.definition,
                        ),
                        sharedConsequences = listOf(EntryLibraryFilterReleasePeriodConsequence),
                    ),
                ),
            ),
        )
    }
}

internal data class EntryLibraryFilterGraphSelection(
    val participatingContentTypes: Set<ContentTypeId>,
    val bookmarkTypes: Set<EntryType>,
    val progressTypes: Set<EntryType>,
    val releasePeriodTypes: Set<EntryType>,
)

internal fun FeatureGraphEvaluation.libraryFilterSelection(): EntryLibraryFilterGraphSelection {
    return EntryLibraryFilterGraphSelection(
        participatingContentTypes = selectedContentTypes(
            PARTICIPATION_INTEGRATION,
            EntryLibraryFilterParticipationConsequence.id,
        ),
        bookmarkTypes = applicableProviderTypes<EntryBookmarkProcessor>(
            feature = FEATURE_ID,
            integration = BOOKMARK_INTEGRATION,
            consequence = EntryLibraryFilterBookmarkControlConsequence.id,
        ),
        progressTypes = applicableProviderTypes<EntryLibraryProgressProvider>(
            feature = FEATURE_ID,
            integration = PROGRESS_INTEGRATION,
            consequence = EntryLibraryFilterProgressControlConsequence.id,
        ),
        releasePeriodTypes = applicableProviderTypes<EntryOutsideReleasePeriodFilterProvider>(
            feature = FEATURE_ID,
            integration = RELEASE_PERIOD_INTEGRATION,
            consequence = EntryLibraryFilterReleasePeriodConsequence.id,
        ),
    )
}

internal fun FeatureGraphEvaluation.requireLibraryFilterContext(
    types: Set<EntryType>,
    policy: EntryLibraryFilterPolicy,
    state: EntryLibraryFilterStateContext,
    tracking: EntryLibraryTrackingFilterContext,
) {
    types.forEach { type ->
        requireEntryContextState(
            type = type,
            feature = FEATURE_ID,
            integration = FILTER_CONTEXT_INTEGRATION,
            consequences = EntryLibraryFilterContextConsequence.entries.map(EntryLibraryFilterContextConsequence::id),
            evidence = listOf(
                contextEvidence(POLICY_CONTEXT, policy),
                contextEvidence(LIBRARY_STATE_CONTEXT, state),
                contextEvidence(TRACKING_CONTEXT, tracking),
            ),
            applicable = true,
        )
    }
}

private fun FeatureGraphEvaluation.selectedContentTypes(
    integration: FeatureIntegrationId,
    consequence: FeatureArtifactId,
): Set<ContentTypeId> {
    return sharedConsequences
        .asSequence()
        .filter { applicability ->
            applicability.subject.feature == FEATURE_ID &&
                applicability.subject.integration == integration &&
                applicability.consequence.id == consequence
        }
        .mapTo(mutableSetOf()) { it.subject.contentType }
}
