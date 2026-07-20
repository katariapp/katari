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
import mihon.feature.graph.allOf
import mihon.feature.graph.contextEvidence
import mihon.feature.graph.contextInputDefinition
import mihon.feature.graph.featureContextRule

private val FEATURE_ID = FeatureId("entry.download.actions")
private val FEATURE_OWNER = ContributionOwner("entry-download-actions")

private val INDIVIDUAL_PROVIDER_INTEGRATION = FeatureIntegrationId("entry.download.actions.individual.provider")
private val BULK_PROVIDER_INTEGRATION = FeatureIntegrationId("entry.download.actions.bulk.provider")
private val BOOKMARKED_BULK_PROVIDER_INTEGRATION =
    FeatureIntegrationId("entry.download.actions.bulk.bookmarked.provider")
private val INDIVIDUAL_CONTEXT_INTEGRATION = FeatureIntegrationId("entry.download.actions.individual.context")
private val INDIVIDUAL_OPERATION_INTEGRATION = FeatureIntegrationId("entry.download.actions.individual.operation")
private val BULK_CONTEXT_INTEGRATION = FeatureIntegrationId("entry.download.actions.bulk.context")
private val BOOKMARKED_BULK_CONTEXT_INTEGRATION =
    FeatureIntegrationId("entry.download.actions.bulk.bookmarked.context")
private val NOTIFICATION_CONTEXT_INTEGRATION = FeatureIntegrationId("entry.download.actions.notification.context")

private object EntryDownloadIndividualProviderConsequence : SharedFeatureConsequence {
    override val id = FeatureArtifactId("entry.download.actions.individual.provider-dispatch")
}

private object EntryDownloadBulkProviderConsequence : SharedFeatureConsequence {
    override val id = FeatureArtifactId("entry.download.actions.bulk.provider-dispatch")
}

private object EntryDownloadBookmarkedBulkProviderConsequence : SharedFeatureConsequence {
    override val id = FeatureArtifactId("entry.download.actions.bulk.bookmarked.provider-dispatch")
}

private enum class EntryDownloadIndividualConsequence(
    override val id: FeatureArtifactId,
) : SharedFeatureConsequence {
    AVAILABILITY(FeatureArtifactId("entry.download.actions.individual.availability")),
    CANCEL(FeatureArtifactId("entry.download.actions.individual.cancel")),
    RETRY(FeatureArtifactId("entry.download.actions.individual.retry")),
}

private enum class EntryDownloadIndividualOperationConsequence(
    override val id: FeatureArtifactId,
) : SharedFeatureConsequence {
    DOWNLOAD(FeatureArtifactId("entry.download.actions.individual.download")),
    DELETE(FeatureArtifactId("entry.download.actions.individual.delete")),
}

private enum class EntryDownloadBulkConsequence(
    override val id: FeatureArtifactId,
) : SharedFeatureConsequence {
    AVAILABILITY(FeatureArtifactId("entry.download.actions.bulk.availability")),
    RESOLUTION(FeatureArtifactId("entry.download.actions.bulk.resolve")),
}

private enum class EntryDownloadBookmarkedBulkConsequence(
    override val id: FeatureArtifactId,
) : SharedFeatureConsequence {
    AVAILABILITY(FeatureArtifactId("entry.download.actions.bulk.bookmarked.availability")),
    RESOLUTION(FeatureArtifactId("entry.download.actions.bulk.bookmarked.resolve")),
}

private object EntryDownloadNotificationConsequence : SharedFeatureConsequence {
    override val id = FeatureArtifactId("entry.download.actions.notification.action")
}

internal enum class EntryDownloadSelectionState {
    ACTIONABLE,
    EMPTY,
    NOTIFICATION_LIMIT_EXCEEDED,
}

private val SOURCE_ACCESS_CONTEXT = contextInputDefinition<EntryDownloadSourceAccess>(
    ContextInputId("entry.download.actions.source-access"),
    ContributionOwner("entry-source"),
)
private val SELECTION_CONTEXT = contextInputDefinition<EntryDownloadSelectionState>(
    ContextInputId("entry.download.actions.selection"),
    ContributionOwner("entry-selection"),
)
private val LOCAL_OR_STUB_BLOCKER = FeatureContextBlocker(
    FeatureArtifactId("entry.download.actions.local-or-stub"),
    listOf(SOURCE_ACCESS_CONTEXT),
)
private val EMPTY_SELECTION_BLOCKER = FeatureContextBlocker(
    FeatureArtifactId("entry.download.actions.empty-selection"),
    listOf(SELECTION_CONTEXT),
)
private val NOTIFICATION_SELECTION_TOO_LARGE_BLOCKER = FeatureContextBlocker(
    FeatureArtifactId("entry.download.actions.notification-selection-too-large"),
    listOf(SELECTION_CONTEXT),
)

private fun sourceAccessRule(owner: ContributionOwner) = featureContextRule(owner) { evidence ->
    if (evidence.value(SOURCE_ACCESS_CONTEXT) == EntryDownloadSourceAccess.REMOTE) {
        FeatureContextDecision.Applicable
    } else {
        FeatureContextDecision.Blocked(listOf(LOCAL_OR_STUB_BLOCKER))
    }
}

private fun sourceAndNonEmptySelectionRule(owner: ContributionOwner) = featureContextRule(owner) { evidence ->
    when {
        evidence.value(SOURCE_ACCESS_CONTEXT) != EntryDownloadSourceAccess.REMOTE ->
            FeatureContextDecision.Blocked(listOf(LOCAL_OR_STUB_BLOCKER))
        evidence.value(SELECTION_CONTEXT) == EntryDownloadSelectionState.EMPTY ->
            FeatureContextDecision.Blocked(listOf(EMPTY_SELECTION_BLOCKER))
        else -> FeatureContextDecision.Applicable
    }
}

private fun sourceAndNotificationSelectionRule(owner: ContributionOwner) = featureContextRule(owner) { evidence ->
    when {
        evidence.value(SOURCE_ACCESS_CONTEXT) != EntryDownloadSourceAccess.REMOTE ->
            FeatureContextDecision.Blocked(listOf(LOCAL_OR_STUB_BLOCKER))
        evidence.value(SELECTION_CONTEXT) == EntryDownloadSelectionState.EMPTY ->
            FeatureContextDecision.Blocked(listOf(EMPTY_SELECTION_BLOCKER))
        evidence.value(SELECTION_CONTEXT) == EntryDownloadSelectionState.NOTIFICATION_LIMIT_EXCEEDED ->
            FeatureContextDecision.Blocked(listOf(NOTIFICATION_SELECTION_TOO_LARGE_BLOCKER))
        else -> FeatureContextDecision.Applicable
    }
}

internal object EntryDownloadActionFeatureContributor : FeatureGraphContributor {
    override val owner = FEATURE_OWNER

    override fun contributeTo(sink: FeatureGraphContributionSink) {
        val download = CapabilityExpression.Provided(EntryDownloadCapability.definition)
        val bulk = allOf(
            download,
            CapabilityExpression.Provided(EntryBulkDownloadCandidateCapability.definition),
        )
        val bookmarkedBulk = allOf(
            download,
            CapabilityExpression.Provided(EntryBulkDownloadCandidateCapability.definition),
            CapabilityExpression.Provided(EntryBookmarkCapability.definition),
        )
        sink.add(
            FeatureContribution(
                feature = FEATURE_ID,
                owner = owner,
                integrations = listOf(
                    FeatureIntegration(
                        id = INDIVIDUAL_PROVIDER_INTEGRATION,
                        prerequisites = download,
                        sharedConsequences = listOf(EntryDownloadIndividualProviderConsequence),
                    ),
                    FeatureIntegration(
                        id = BULK_PROVIDER_INTEGRATION,
                        prerequisites = bulk,
                        sharedConsequences = listOf(EntryDownloadBulkProviderConsequence),
                    ),
                    FeatureIntegration(
                        id = BOOKMARKED_BULK_PROVIDER_INTEGRATION,
                        prerequisites = bookmarkedBulk,
                        sharedConsequences = listOf(EntryDownloadBookmarkedBulkProviderConsequence),
                    ),
                    FeatureIntegration(
                        id = INDIVIDUAL_CONTEXT_INTEGRATION,
                        prerequisites = download,
                        contextInputs = listOf(SOURCE_ACCESS_CONTEXT),
                        contextRule = sourceAccessRule(owner),
                        contextBlockers = listOf(LOCAL_OR_STUB_BLOCKER),
                        sharedConsequences = EntryDownloadIndividualConsequence.entries,
                    ),
                    FeatureIntegration(
                        id = INDIVIDUAL_OPERATION_INTEGRATION,
                        prerequisites = download,
                        contextInputs = listOf(SOURCE_ACCESS_CONTEXT, SELECTION_CONTEXT),
                        contextRule = sourceAndNonEmptySelectionRule(owner),
                        contextBlockers = listOf(LOCAL_OR_STUB_BLOCKER, EMPTY_SELECTION_BLOCKER),
                        sharedConsequences = EntryDownloadIndividualOperationConsequence.entries,
                    ),
                    FeatureIntegration(
                        id = BULK_CONTEXT_INTEGRATION,
                        prerequisites = bulk,
                        contextInputs = listOf(SOURCE_ACCESS_CONTEXT),
                        contextRule = sourceAccessRule(owner),
                        contextBlockers = listOf(LOCAL_OR_STUB_BLOCKER),
                        sharedConsequences = EntryDownloadBulkConsequence.entries,
                    ),
                    FeatureIntegration(
                        id = BOOKMARKED_BULK_CONTEXT_INTEGRATION,
                        prerequisites = bookmarkedBulk,
                        contextInputs = listOf(SOURCE_ACCESS_CONTEXT),
                        contextRule = sourceAccessRule(owner),
                        contextBlockers = listOf(LOCAL_OR_STUB_BLOCKER),
                        sharedConsequences = EntryDownloadBookmarkedBulkConsequence.entries,
                    ),
                    FeatureIntegration(
                        id = NOTIFICATION_CONTEXT_INTEGRATION,
                        prerequisites = download,
                        contextInputs = listOf(SOURCE_ACCESS_CONTEXT, SELECTION_CONTEXT),
                        contextRule = sourceAndNotificationSelectionRule(owner),
                        contextBlockers = listOf(
                            LOCAL_OR_STUB_BLOCKER,
                            EMPTY_SELECTION_BLOCKER,
                            NOTIFICATION_SELECTION_TOO_LARGE_BLOCKER,
                        ),
                        sharedConsequences = listOf(EntryDownloadNotificationConsequence),
                    ),
                ),
            ),
        )
    }
}

internal fun FeatureGraphEvaluation.downloadIndividualTypes(): Set<EntryType> =
    applicableProviderTypes<EntryDownloadProcessor>(
        feature = FEATURE_ID,
        integration = INDIVIDUAL_PROVIDER_INTEGRATION,
        consequence = EntryDownloadIndividualProviderConsequence.id,
    )

internal fun FeatureGraphEvaluation.downloadBulkTypes(): Set<EntryType> =
    applicableProviderTypes<EntryBulkDownloadCandidateProcessor>(
        feature = FEATURE_ID,
        integration = BULK_PROVIDER_INTEGRATION,
        consequence = EntryDownloadBulkProviderConsequence.id,
    )

internal fun FeatureGraphEvaluation.downloadBookmarkedBulkTypes(): Set<EntryType> =
    applicableProviderTypes<EntryBookmarkProcessor>(
        feature = FEATURE_ID,
        integration = BOOKMARKED_BULK_PROVIDER_INTEGRATION,
        consequence = EntryDownloadBookmarkedBulkProviderConsequence.id,
    )

internal fun FeatureGraphEvaluation.requireDownloadIndividualContext(
    target: EntryDownloadActionTarget,
) {
    requireDownloadActionContext(
        target = target,
        integration = INDIVIDUAL_CONTEXT_INTEGRATION,
        consequences = EntryDownloadIndividualConsequence.entries.map(EntryDownloadIndividualConsequence::id),
    )
}

internal fun FeatureGraphEvaluation.requireDownloadIndividualOperationContext(
    target: EntryDownloadActionTarget,
    selectionState: EntryDownloadSelectionState,
) {
    requireDownloadActionContext(
        target = target,
        integration = INDIVIDUAL_OPERATION_INTEGRATION,
        consequences = EntryDownloadIndividualOperationConsequence.entries.map(
            EntryDownloadIndividualOperationConsequence::id,
        ),
        selectionState = selectionState,
    )
}

internal fun FeatureGraphEvaluation.requireDownloadBulkContext(
    target: EntryDownloadActionTarget,
    bookmarked: Boolean,
) {
    requireDownloadActionContext(
        target = target,
        integration = if (bookmarked) BOOKMARKED_BULK_CONTEXT_INTEGRATION else BULK_CONTEXT_INTEGRATION,
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
        integration = NOTIFICATION_CONTEXT_INTEGRATION,
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
        feature = FEATURE_ID,
        integration = integration,
        consequences = consequences,
        evidence = buildList {
            add(contextEvidence(SOURCE_ACCESS_CONTEXT, target.sourceAccess))
            selectionState?.let { add(contextEvidence(SELECTION_CONTEXT, it)) }
        },
        applicable = target.sourceAccess == EntryDownloadSourceAccess.REMOTE &&
            selectionState != EntryDownloadSelectionState.EMPTY &&
            selectionState != EntryDownloadSelectionState.NOTIFICATION_LIMIT_EXCEEDED,
    )
}
