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

private val FEATURE_ID = FeatureId("entry.download.automatic")
private val FEATURE_OWNER = ContributionOwner("entry-automatic-download")
private val PROVIDER_INTEGRATION = FeatureIntegrationId("entry.download.automatic.provider")
private val CONTEXT_INTEGRATION = FeatureIntegrationId("entry.download.automatic.context")

private object EntryAutomaticDownloadProviderConsequence : SharedFeatureConsequence {
    override val id = FeatureArtifactId("entry.download.automatic.provider-dispatch")
}

private enum class EntryAutomaticDownloadConsequence(
    override val id: FeatureArtifactId,
) : SharedFeatureConsequence {
    POLICY(FeatureArtifactId("entry.download.automatic.policy")),
    LIBRARY_UPDATE(FeatureArtifactId("entry.download.automatic.library-update")),
    ENTRY_REFRESH(FeatureArtifactId("entry.download.automatic.entry-refresh")),
}

private val NEW_CHILDREN_CONTEXT = contextInputDefinition<Boolean>(
    ContextInputId("entry.download.automatic.new-children"),
    ContributionOwner("entry-selection"),
)
private val ENABLED_CONTEXT = contextInputDefinition<Boolean>(
    ContextInputId("entry.download.automatic.enabled"),
    ContributionOwner("entry-download-configuration"),
)
private val FAVORITE_CONTEXT = contextInputDefinition<Boolean>(
    ContextInputId("entry.download.automatic.library-membership"),
    ContributionOwner("entry-state"),
)
private val CATEGORY_ALLOWED_CONTEXT = contextInputDefinition<Boolean>(
    ContextInputId("entry.download.automatic.category-policy"),
    ContributionOwner("entry-category-policy"),
)
private val UNREAD_ONLY_CONTEXT = contextInputDefinition<Boolean>(
    ContextInputId("entry.download.automatic.unread-only"),
    ContributionOwner("entry-download-configuration"),
)
private val CANDIDATES_CONTEXT = contextInputDefinition<Boolean>(
    ContextInputId("entry.download.automatic.eligible-candidates"),
    ContributionOwner("entry-selection"),
)

private val EMPTY_SELECTION_BLOCKER = FeatureContextBlocker(
    FeatureArtifactId("entry.download.automatic.empty-selection"),
    listOf(NEW_CHILDREN_CONTEXT),
)
private val DISABLED_BLOCKER = FeatureContextBlocker(
    FeatureArtifactId("entry.download.automatic.disabled"),
    listOf(ENABLED_CONTEXT),
)
private val NOT_IN_LIBRARY_BLOCKER = FeatureContextBlocker(
    FeatureArtifactId("entry.download.automatic.not-in-library"),
    listOf(FAVORITE_CONTEXT),
)
private val CATEGORY_POLICY_BLOCKER = FeatureContextBlocker(
    FeatureArtifactId("entry.download.automatic.category-policy-rejected"),
    listOf(CATEGORY_ALLOWED_CONTEXT),
)
private val NO_UNREAD_CANDIDATES_BLOCKER = FeatureContextBlocker(
    FeatureArtifactId("entry.download.automatic.no-unread-candidates"),
    listOf(UNREAD_ONLY_CONTEXT, CANDIDATES_CONTEXT),
)

internal object EntryAutomaticDownloadFeatureContributor : FeatureGraphContributor {
    override val owner = FEATURE_OWNER

    override fun contributeTo(sink: FeatureGraphContributionSink) {
        val download = CapabilityExpression.Provided(EntryDownloadCapability.definition)
        sink.add(
            FeatureContribution(
                feature = FEATURE_ID,
                owner = owner,
                integrations = listOf(
                    FeatureIntegration(
                        id = PROVIDER_INTEGRATION,
                        prerequisites = download,
                        sharedConsequences = listOf(EntryAutomaticDownloadProviderConsequence),
                    ),
                    FeatureIntegration(
                        id = CONTEXT_INTEGRATION,
                        prerequisites = download,
                        contextInputs = listOf(
                            NEW_CHILDREN_CONTEXT,
                            ENABLED_CONTEXT,
                            FAVORITE_CONTEXT,
                            CATEGORY_ALLOWED_CONTEXT,
                            UNREAD_ONLY_CONTEXT,
                            CANDIDATES_CONTEXT,
                        ),
                        contextRule = featureContextRule(owner) { evidence ->
                            when {
                                !evidence.value(NEW_CHILDREN_CONTEXT) ->
                                    FeatureContextDecision.Blocked(listOf(EMPTY_SELECTION_BLOCKER))
                                !evidence.value(ENABLED_CONTEXT) ->
                                    FeatureContextDecision.Blocked(listOf(DISABLED_BLOCKER))
                                !evidence.value(FAVORITE_CONTEXT) ->
                                    FeatureContextDecision.Blocked(listOf(NOT_IN_LIBRARY_BLOCKER))
                                !evidence.value(CATEGORY_ALLOWED_CONTEXT) ->
                                    FeatureContextDecision.Blocked(listOf(CATEGORY_POLICY_BLOCKER))
                                evidence.value(UNREAD_ONLY_CONTEXT) && !evidence.value(CANDIDATES_CONTEXT) ->
                                    FeatureContextDecision.Blocked(listOf(NO_UNREAD_CANDIDATES_BLOCKER))
                                else -> FeatureContextDecision.Applicable
                            }
                        },
                        contextBlockers = listOf(
                            EMPTY_SELECTION_BLOCKER,
                            DISABLED_BLOCKER,
                            NOT_IN_LIBRARY_BLOCKER,
                            CATEGORY_POLICY_BLOCKER,
                            NO_UNREAD_CANDIDATES_BLOCKER,
                        ),
                        sharedConsequences = EntryAutomaticDownloadConsequence.entries,
                    ),
                ),
            ),
        )
    }
}

internal fun FeatureGraphEvaluation.automaticDownloadTypes(): Set<EntryType> =
    applicableProviderTypes<EntryDownloadProcessor>(
        feature = FEATURE_ID,
        integration = PROVIDER_INTEGRATION,
        consequence = EntryAutomaticDownloadProviderConsequence.id,
    )

internal fun FeatureGraphEvaluation.requireAutomaticDownloadContext(
    type: EntryType,
    decision: EntryAutomaticDownloadPolicyDecision,
) {
    requireEntryContextState(
        type = type,
        feature = FEATURE_ID,
        integration = CONTEXT_INTEGRATION,
        consequences = EntryAutomaticDownloadConsequence.entries.map(EntryAutomaticDownloadConsequence::id),
        evidence = listOf(
            contextEvidence(NEW_CHILDREN_CONTEXT, decision.hasNewChapters),
            contextEvidence(ENABLED_CONTEXT, decision.enabled),
            contextEvidence(FAVORITE_CONTEXT, decision.favorite),
            contextEvidence(CATEGORY_ALLOWED_CONTEXT, decision.categoryAllowed),
            contextEvidence(UNREAD_ONLY_CONTEXT, decision.unreadOnly),
            contextEvidence(CANDIDATES_CONTEXT, decision.candidates.isNotEmpty()),
        ),
        applicable = decision.blocker == null,
    )
}
