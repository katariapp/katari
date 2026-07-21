package mihon.entry.interactions

import eu.kanade.tachiyomi.source.entry.EntryType
import mihon.feature.graph.ContextEvidence
import mihon.feature.graph.FeatureArtifactId
import mihon.feature.graph.FeatureGraphEvaluation
import mihon.feature.graph.FeatureIntegrationId
import mihon.feature.graph.contextEvidence

internal fun FeatureGraphEvaluation.downloadLifecycleTypes(): Set<EntryType> =
    applicableProviderTypes<EntryDownloadProcessor>(
        feature = ENTRY_DOWNLOAD_LIFECYCLE_FEATURE_ID,
        integration = ENTRY_DOWNLOAD_LIFECYCLE_PROVIDER_INTEGRATION,
        consequence = EntryDownloadLifecycleProviderConsequence.PROVIDER_DISPATCH.id,
    )

internal fun FeatureGraphEvaluation.downloadLifecycleBookmarkProtectionTypes(): Set<EntryType> =
    applicableProviderTypes<EntryBookmarkProcessor>(
        feature = ENTRY_DOWNLOAD_LIFECYCLE_FEATURE_ID,
        integration = ENTRY_DOWNLOAD_BOOKMARK_PROTECTION_PROVIDER_INTEGRATION,
        consequence = EntryDownloadLifecycleBookmarkProtectionProviderConsequence.id,
    )

internal fun FeatureGraphEvaluation.requireMarkedConsumedCleanupContext(type: EntryType, enabled: Boolean) {
    requireLifecycleContext(
        type,
        ENTRY_DOWNLOAD_MARKED_CONSUMED_INTEGRATION,
        listOf(EntryDownloadLifecycleMarkedConsumedConsequence.id),
        listOf(contextEvidence(ENTRY_DOWNLOAD_REMOVE_MARKED_CONSUMED_CONTEXT, enabled)),
        enabled,
    )
}

internal fun FeatureGraphEvaluation.requireCompletionCleanupContext(type: EntryType, enabled: Boolean) {
    requireLifecycleContext(
        type,
        ENTRY_DOWNLOAD_COMPLETION_INTEGRATION,
        listOf(EntryDownloadLifecycleCompletionConsequence.id),
        listOf(contextEvidence(ENTRY_DOWNLOAD_COMPLETION_CLEANUP_CONTEXT, enabled)),
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
        ENTRY_DOWNLOAD_AHEAD_INTEGRATION,
        listOf(EntryDownloadLifecycleDownloadAheadConsequence.id),
        listOf(
            contextEvidence(ENTRY_DOWNLOAD_VIEWER_PROGRESS_CONTEXT, progressEligible),
            contextEvidence(ENTRY_DOWNLOAD_AHEAD_CONTEXT, enabled),
        ),
        progressEligible && enabled,
    )
}

internal fun FeatureGraphEvaluation.requireCleanupOwnerContext(type: EntryType, categoryAllowed: Boolean) {
    requireLifecycleContext(
        type,
        ENTRY_DOWNLOAD_CLEANUP_OWNER_INTEGRATION,
        EntryDownloadLifecycleCleanupConsequence.entries.map(EntryDownloadLifecycleCleanupConsequence::id),
        listOf(contextEvidence(ENTRY_DOWNLOAD_CLEANUP_CATEGORY_ALLOWED_CONTEXT, categoryAllowed)),
        categoryAllowed,
    )
}

internal fun FeatureGraphEvaluation.requireBookmarkProtectionContext(type: EntryType, removeBookmarked: Boolean) {
    requireLifecycleContext(
        type,
        ENTRY_DOWNLOAD_BOOKMARK_PROTECTION_CONTEXT_INTEGRATION,
        listOf(EntryDownloadLifecycleBookmarkProtectionConsequence.id),
        listOf(contextEvidence(ENTRY_DOWNLOAD_REMOVE_BOOKMARKED_CONTEXT, removeBookmarked)),
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
    requireEntryContextState(type, ENTRY_DOWNLOAD_LIFECYCLE_FEATURE_ID, integration, consequences, evidence, applicable)
}
