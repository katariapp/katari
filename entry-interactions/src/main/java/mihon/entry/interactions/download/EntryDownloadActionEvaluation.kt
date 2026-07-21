package mihon.entry.interactions

import eu.kanade.tachiyomi.source.entry.EntryType
import mihon.feature.graph.FeatureArtifactId
import mihon.feature.graph.FeatureGraphEvaluation
import mihon.feature.graph.FeatureIntegrationId
import mihon.feature.graph.contextEvidence

internal fun FeatureGraphEvaluation.downloadIndividualTypes(): Set<EntryType> =
    applicableProviderTypes<EntryDownloadProcessor>(
        feature = ENTRY_DOWNLOAD_ACTION_FEATURE_ID,
        integration = ENTRY_DOWNLOAD_INDIVIDUAL_PROVIDER_INTEGRATION,
        consequence = EntryDownloadIndividualProviderConsequence.id,
    )

internal fun FeatureGraphEvaluation.downloadBulkTypes(): Set<EntryType> =
    applicableProviderTypes<EntryBulkDownloadCandidateProcessor>(
        feature = ENTRY_DOWNLOAD_ACTION_FEATURE_ID,
        integration = ENTRY_DOWNLOAD_BULK_PROVIDER_INTEGRATION,
        consequence = EntryDownloadBulkProviderConsequence.id,
    )

internal fun FeatureGraphEvaluation.downloadBookmarkedBulkTypes(): Set<EntryType> =
    applicableProviderTypes<EntryBookmarkProcessor>(
        feature = ENTRY_DOWNLOAD_ACTION_FEATURE_ID,
        integration = ENTRY_DOWNLOAD_BOOKMARKED_BULK_PROVIDER_INTEGRATION,
        consequence = EntryDownloadBookmarkedBulkProviderConsequence.id,
    )

internal fun FeatureGraphEvaluation.requireDownloadIndividualContext(target: EntryDownloadActionTarget) {
    requireDownloadActionContext(
        target = target,
        integration = ENTRY_DOWNLOAD_INDIVIDUAL_CONTEXT_INTEGRATION,
        consequences = EntryDownloadIndividualConsequence.entries.map(EntryDownloadIndividualConsequence::id),
    )
}

internal fun FeatureGraphEvaluation.requireDownloadIndividualOperationContext(
    target: EntryDownloadActionTarget,
    selectionState: EntryDownloadSelectionState,
) {
    requireDownloadActionContext(
        target = target,
        integration = ENTRY_DOWNLOAD_INDIVIDUAL_OPERATION_INTEGRATION,
        consequences = EntryDownloadIndividualOperationConsequence.entries.map(
            EntryDownloadIndividualOperationConsequence::id,
        ),
        selectionState = selectionState,
    )
}

internal fun FeatureGraphEvaluation.requireDownloadBulkContext(target: EntryDownloadActionTarget, bookmarked: Boolean) {
    requireDownloadActionContext(
        target = target,
        integration = if (bookmarked) {
            ENTRY_DOWNLOAD_BOOKMARKED_BULK_CONTEXT_INTEGRATION
        } else {
            ENTRY_DOWNLOAD_BULK_CONTEXT_INTEGRATION
        },
        consequences = if (bookmarked) {
            EntryDownloadBookmarkedBulkConsequence.entries.map(EntryDownloadBookmarkedBulkConsequence::id)
        } else {
            EntryDownloadBulkConsequence.entries.map(EntryDownloadBulkConsequence::id)
        },
    )
}

internal fun FeatureGraphEvaluation.requireDownloadNotificationContext(
    target: EntryDownloadActionTarget,
    selectionState: EntryDownloadSelectionState,
) {
    requireDownloadActionContext(
        target = target,
        integration = ENTRY_DOWNLOAD_NOTIFICATION_CONTEXT_INTEGRATION,
        consequences = listOf(EntryDownloadNotificationConsequence.id),
        selectionState = selectionState,
    )
}

private fun FeatureGraphEvaluation.requireDownloadActionContext(
    target: EntryDownloadActionTarget,
    integration: FeatureIntegrationId,
    consequences: Collection<FeatureArtifactId>,
    selectionState: EntryDownloadSelectionState? = null,
) {
    requireEntryContextState(
        type = target.type,
        feature = ENTRY_DOWNLOAD_ACTION_FEATURE_ID,
        integration = integration,
        consequences = consequences,
        evidence = buildList {
            add(contextEvidence(ENTRY_DOWNLOAD_SOURCE_ACCESS_CONTEXT, target.sourceAccess))
            selectionState?.let { add(contextEvidence(ENTRY_DOWNLOAD_SELECTION_CONTEXT, it)) }
        },
        applicable = target.sourceAccess == EntryDownloadSourceAccess.REMOTE &&
            selectionState != EntryDownloadSelectionState.EMPTY &&
            selectionState != EntryDownloadSelectionState.NOTIFICATION_LIMIT_EXCEEDED,
    )
}
