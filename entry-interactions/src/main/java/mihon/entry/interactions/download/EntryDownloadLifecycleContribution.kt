package mihon.entry.interactions

import eu.kanade.tachiyomi.source.entry.EntryType
import mihon.feature.graph.CapabilityExpression
import mihon.feature.graph.ContextEvidence
import mihon.feature.graph.ContextInputDefinition
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
import mihon.feature.graph.allOf
import mihon.feature.graph.contextEvidence
import mihon.feature.graph.contextInputDefinition
import mihon.feature.graph.featureContextRule

private val FEATURE_ID = FeatureId("entry.download.lifecycle")
private val FEATURE_OWNER = ContributionOwner("entry-download-lifecycle")
private val PROVIDER_INTEGRATION = FeatureIntegrationId("entry.download.lifecycle.provider")
private val MARKED_CONSUMED_INTEGRATION = FeatureIntegrationId("entry.download.lifecycle.marked-consumed")
private val COMPLETION_INTEGRATION = FeatureIntegrationId("entry.download.lifecycle.completion")
private val DOWNLOAD_AHEAD_INTEGRATION = FeatureIntegrationId("entry.download.lifecycle.download-ahead")
private val CLEANUP_OWNER_INTEGRATION = FeatureIntegrationId("entry.download.lifecycle.cleanup-owner")
private val BOOKMARK_PROTECTION_PROVIDER_INTEGRATION =
    FeatureIntegrationId("entry.download.lifecycle.bookmark-protection.provider")
private val BOOKMARK_PROTECTION_CONTEXT_INTEGRATION =
    FeatureIntegrationId("entry.download.lifecycle.bookmark-protection.context")

private enum class EntryDownloadLifecycleProviderConsequence(
    override val id: FeatureArtifactId,
) : SharedFeatureConsequence {
    TYPE_APPLICABILITY(FeatureArtifactId("entry.download.lifecycle.type-applicability")),
    EVENT_ACCEPTANCE(FeatureArtifactId("entry.download.lifecycle.events")),
    PROVIDER_DISPATCH(FeatureArtifactId("entry.download.lifecycle.provider-dispatch")),
}

private object EntryDownloadLifecycleMarkedConsumedConsequence : SharedFeatureConsequence {
    override val id = FeatureArtifactId("entry.download.lifecycle.marked-consumed-cleanup")
}

private object EntryDownloadLifecycleCompletionConsequence : SharedFeatureConsequence {
    override val id = FeatureArtifactId("entry.download.lifecycle.completion-cleanup")
}

private object EntryDownloadLifecycleDownloadAheadConsequence : SharedFeatureConsequence {
    override val id = FeatureArtifactId("entry.download.lifecycle.download-ahead")
}

private enum class EntryDownloadLifecycleCleanupConsequence(
    override val id: FeatureArtifactId,
) : SharedFeatureConsequence {
    CATEGORY_POLICY(FeatureArtifactId("entry.download.lifecycle.category-exclusions")),
    PHYSICAL_DISPATCH(FeatureArtifactId("entry.download.lifecycle.physical-dispatch")),
}

private object EntryDownloadLifecycleBookmarkProtectionProviderConsequence : SharedFeatureConsequence {
    override val id = FeatureArtifactId("entry.download.lifecycle.bookmark-protection.provider")
}

private object EntryDownloadLifecycleBookmarkProtectionConsequence : SharedFeatureConsequence {
    override val id = FeatureArtifactId("entry.download.lifecycle.bookmark-protection")
}

private val REMOVE_MARKED_CONSUMED_CONTEXT = contextInputDefinition<Boolean>(
    ContextInputId("entry.download.lifecycle.remove-marked-consumed"),
    ContributionOwner("entry-download-configuration"),
)
private val COMPLETION_CLEANUP_CONTEXT = contextInputDefinition<Boolean>(
    ContextInputId("entry.download.lifecycle.completion-cleanup-enabled"),
    ContributionOwner("entry-download-configuration"),
)
private val VIEWER_PROGRESS_CONTEXT = contextInputDefinition<Boolean>(
    ContextInputId("entry.download.lifecycle.viewer-progress-eligible"),
    ContributionOwner("entry-viewer-state"),
)
private val DOWNLOAD_AHEAD_CONTEXT = contextInputDefinition<Boolean>(
    ContextInputId("entry.download.lifecycle.download-ahead-enabled"),
    ContributionOwner("entry-download-configuration"),
)
private val CATEGORY_ALLOWED_CONTEXT = contextInputDefinition<Boolean>(
    ContextInputId("entry.download.lifecycle.category-allowed"),
    ContributionOwner("entry-category-policy"),
)
private val REMOVE_BOOKMARKED_CONTEXT = contextInputDefinition<Boolean>(
    ContextInputId("entry.download.lifecycle.remove-bookmarked"),
    ContributionOwner("entry-download-configuration"),
)

private val MARKED_CONSUMED_DISABLED = FeatureContextBlocker(
    FeatureArtifactId("entry.download.lifecycle.marked-consumed-disabled"),
    listOf(REMOVE_MARKED_CONSUMED_CONTEXT),
)
private val COMPLETION_DISABLED = FeatureContextBlocker(
    FeatureArtifactId("entry.download.lifecycle.completion-disabled"),
    listOf(COMPLETION_CLEANUP_CONTEXT),
)
private val VIEWER_PROGRESS_INELIGIBLE = FeatureContextBlocker(
    FeatureArtifactId("entry.download.lifecycle.viewer-progress-ineligible"),
    listOf(VIEWER_PROGRESS_CONTEXT),
)
private val DOWNLOAD_AHEAD_DISABLED = FeatureContextBlocker(
    FeatureArtifactId("entry.download.lifecycle.download-ahead-disabled"),
    listOf(DOWNLOAD_AHEAD_CONTEXT),
)
private val CATEGORY_EXCLUDED = FeatureContextBlocker(
    FeatureArtifactId("entry.download.lifecycle.category-excluded"),
    listOf(CATEGORY_ALLOWED_CONTEXT),
)
private val BOOKMARK_PROTECTION_OVERRIDDEN = FeatureContextBlocker(
    FeatureArtifactId("entry.download.lifecycle.bookmark-protection-overridden"),
    listOf(REMOVE_BOOKMARKED_CONTEXT),
)

internal object EntryDownloadLifecycleFeatureContributor : FeatureGraphContributor {
    override val owner = FEATURE_OWNER

    override fun contributeTo(sink: FeatureGraphContributionSink) {
        val download = CapabilityExpression.Provided(EntryDownloadCapability.definition)
        val downloadAndBookmark = allOf(
            download,
            CapabilityExpression.Provided(EntryBookmarkCapability.definition),
        )
        sink.add(
            FeatureContribution(
                feature = FEATURE_ID,
                owner = owner,
                integrations = listOf(
                    FeatureIntegration(
                        id = PROVIDER_INTEGRATION,
                        prerequisites = download,
                        sharedConsequences = EntryDownloadLifecycleProviderConsequence.entries,
                    ),
                    FeatureIntegration(
                        id = MARKED_CONSUMED_INTEGRATION,
                        prerequisites = download,
                        contextInputs = listOf(REMOVE_MARKED_CONSUMED_CONTEXT),
                        contextRule = booleanRule(owner, REMOVE_MARKED_CONSUMED_CONTEXT, MARKED_CONSUMED_DISABLED),
                        contextBlockers = listOf(MARKED_CONSUMED_DISABLED),
                        sharedConsequences = listOf(EntryDownloadLifecycleMarkedConsumedConsequence),
                    ),
                    FeatureIntegration(
                        id = COMPLETION_INTEGRATION,
                        prerequisites = download,
                        contextInputs = listOf(COMPLETION_CLEANUP_CONTEXT),
                        contextRule = booleanRule(owner, COMPLETION_CLEANUP_CONTEXT, COMPLETION_DISABLED),
                        contextBlockers = listOf(COMPLETION_DISABLED),
                        sharedConsequences = listOf(EntryDownloadLifecycleCompletionConsequence),
                    ),
                    FeatureIntegration(
                        id = DOWNLOAD_AHEAD_INTEGRATION,
                        prerequisites = download,
                        contextInputs = listOf(VIEWER_PROGRESS_CONTEXT, DOWNLOAD_AHEAD_CONTEXT),
                        contextRule = featureContextRule(owner) { evidence ->
                            when {
                                !evidence.value(VIEWER_PROGRESS_CONTEXT) ->
                                    FeatureContextDecision.Blocked(listOf(VIEWER_PROGRESS_INELIGIBLE))
                                !evidence.value(DOWNLOAD_AHEAD_CONTEXT) ->
                                    FeatureContextDecision.Blocked(listOf(DOWNLOAD_AHEAD_DISABLED))
                                else -> FeatureContextDecision.Applicable
                            }
                        },
                        contextBlockers = listOf(VIEWER_PROGRESS_INELIGIBLE, DOWNLOAD_AHEAD_DISABLED),
                        sharedConsequences = listOf(EntryDownloadLifecycleDownloadAheadConsequence),
                    ),
                    FeatureIntegration(
                        id = CLEANUP_OWNER_INTEGRATION,
                        prerequisites = download,
                        contextInputs = listOf(CATEGORY_ALLOWED_CONTEXT),
                        contextRule = booleanRule(owner, CATEGORY_ALLOWED_CONTEXT, CATEGORY_EXCLUDED),
                        contextBlockers = listOf(CATEGORY_EXCLUDED),
                        sharedConsequences = EntryDownloadLifecycleCleanupConsequence.entries,
                    ),
                    FeatureIntegration(
                        id = BOOKMARK_PROTECTION_PROVIDER_INTEGRATION,
                        prerequisites = downloadAndBookmark,
                        sharedConsequences = listOf(EntryDownloadLifecycleBookmarkProtectionProviderConsequence),
                    ),
                    FeatureIntegration(
                        id = BOOKMARK_PROTECTION_CONTEXT_INTEGRATION,
                        prerequisites = downloadAndBookmark,
                        contextInputs = listOf(REMOVE_BOOKMARKED_CONTEXT),
                        contextRule = featureContextRule(owner) { evidence ->
                            if (evidence.value(REMOVE_BOOKMARKED_CONTEXT)) {
                                FeatureContextDecision.Blocked(listOf(BOOKMARK_PROTECTION_OVERRIDDEN))
                            } else {
                                FeatureContextDecision.Applicable
                            }
                        },
                        contextBlockers = listOf(BOOKMARK_PROTECTION_OVERRIDDEN),
                        sharedConsequences = listOf(EntryDownloadLifecycleBookmarkProtectionConsequence),
                    ),
                ),
            ),
        )
    }
}

private fun booleanRule(
    owner: ContributionOwner,
    input: ContextInputDefinition<Boolean>,
    blocker: FeatureContextBlocker,
) = featureContextRule(owner) { evidence ->
    if (evidence.value(input)) FeatureContextDecision.Applicable else FeatureContextDecision.Blocked(listOf(blocker))
}

internal fun FeatureGraphEvaluation.downloadLifecycleTypes(): Set<EntryType> =
    applicableProviderTypes<EntryDownloadProcessor>(
        feature = FEATURE_ID,
        integration = PROVIDER_INTEGRATION,
        consequence = EntryDownloadLifecycleProviderConsequence.PROVIDER_DISPATCH.id,
    )

internal fun FeatureGraphEvaluation.downloadLifecycleBookmarkProtectionTypes(): Set<EntryType> =
    applicableProviderTypes<EntryBookmarkProcessor>(
        feature = FEATURE_ID,
        integration = BOOKMARK_PROTECTION_PROVIDER_INTEGRATION,
        consequence = EntryDownloadLifecycleBookmarkProtectionProviderConsequence.id,
    )

internal fun FeatureGraphEvaluation.requireMarkedConsumedCleanupContext(type: EntryType, enabled: Boolean) {
    requireLifecycleContext(
        type,
        MARKED_CONSUMED_INTEGRATION,
        listOf(EntryDownloadLifecycleMarkedConsumedConsequence.id),
        listOf(contextEvidence(REMOVE_MARKED_CONSUMED_CONTEXT, enabled)),
        enabled,
    )
}

internal fun FeatureGraphEvaluation.requireCompletionCleanupContext(type: EntryType, enabled: Boolean) {
    requireLifecycleContext(
        type,
        COMPLETION_INTEGRATION,
        listOf(EntryDownloadLifecycleCompletionConsequence.id),
        listOf(contextEvidence(COMPLETION_CLEANUP_CONTEXT, enabled)),
        enabled,
    )
}

internal fun FeatureGraphEvaluation.requireDownloadAheadContext(
    type: EntryType,
    progressEligible: Boolean,
    enabled: Boolean,
) {
    requireLifecycleContext(
        type,
        DOWNLOAD_AHEAD_INTEGRATION,
        listOf(EntryDownloadLifecycleDownloadAheadConsequence.id),
        listOf(
            contextEvidence(VIEWER_PROGRESS_CONTEXT, progressEligible),
            contextEvidence(DOWNLOAD_AHEAD_CONTEXT, enabled),
        ),
        progressEligible && enabled,
    )
}

internal fun FeatureGraphEvaluation.requireCleanupOwnerContext(type: EntryType, categoryAllowed: Boolean) {
    requireLifecycleContext(
        type,
        CLEANUP_OWNER_INTEGRATION,
        EntryDownloadLifecycleCleanupConsequence.entries.map(EntryDownloadLifecycleCleanupConsequence::id),
        listOf(contextEvidence(CATEGORY_ALLOWED_CONTEXT, categoryAllowed)),
        categoryAllowed,
    )
}

internal fun FeatureGraphEvaluation.requireBookmarkProtectionContext(type: EntryType, removeBookmarked: Boolean) {
    requireLifecycleContext(
        type,
        BOOKMARK_PROTECTION_CONTEXT_INTEGRATION,
        listOf(EntryDownloadLifecycleBookmarkProtectionConsequence.id),
        listOf(contextEvidence(REMOVE_BOOKMARKED_CONTEXT, removeBookmarked)),
        !removeBookmarked,
    )
}

private fun FeatureGraphEvaluation.requireLifecycleContext(
    type: EntryType,
    integration: FeatureIntegrationId,
    consequences: Collection<FeatureArtifactId>,
    evidence: List<ContextEvidence<*>>,
    applicable: Boolean,
) {
    requireEntryContextState(type, FEATURE_ID, integration, consequences, evidence, applicable)
}
