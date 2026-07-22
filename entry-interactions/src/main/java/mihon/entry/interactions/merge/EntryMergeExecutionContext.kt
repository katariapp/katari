package mihon.entry.interactions

import eu.kanade.tachiyomi.source.entry.EntryType
import mihon.feature.graph.CapabilityExpression
import mihon.feature.graph.ContextInputDefinition
import mihon.feature.graph.ContextInputId
import mihon.feature.graph.ContributionOwner
import mihon.feature.graph.FeatureArtifactId
import mihon.feature.graph.FeatureBehaviorProjection
import mihon.feature.graph.FeatureContextBlocker
import mihon.feature.graph.FeatureContextDecision
import mihon.feature.graph.FeatureGraphEvaluation
import mihon.feature.graph.FeatureIntegration
import mihon.feature.graph.FeatureIntegrationId
import mihon.feature.graph.contextEvidence
import mihon.feature.graph.contextInputDefinition
import mihon.feature.graph.featureContextRule

internal val ENTRY_MERGE_EXISTING_GROUP_CONTEXT_INTEGRATION = FeatureIntegrationId("entry.merge.existing-group-context")
internal val ENTRY_MERGE_LIBRARY_INITIALIZATION_CONTEXT_INTEGRATION =
    FeatureIntegrationId("entry.merge.library-initialization-context")
internal val ENTRY_MERGE_COVER_CLEANUP_CONTEXT_INTEGRATION = FeatureIntegrationId("entry.merge.cover-cleanup-context")
internal val ENTRY_MERGE_DOWNLOAD_REMOVAL_CONTEXT_INTEGRATION =
    FeatureIntegrationId("entry.merge.download-removal-context")

private object EntryMergeExistingGroupBehavior : FeatureBehaviorProjection {
    override val id = FeatureArtifactId("entry.merge.existing-group-mutation")
}

internal object EntryMergeLibraryInitializationBehavior : FeatureBehaviorProjection {
    override val id = FeatureArtifactId("entry.merge.library-initialization.projection")
}

internal object EntryMergeCoverCleanupBehavior : FeatureBehaviorProjection {
    override val id = FeatureArtifactId("entry.merge.cover-cleanup.projection")
}

internal object EntryMergeDownloadRemovalBehavior : FeatureBehaviorProjection {
    override val id = FeatureArtifactId("entry.merge.download-removal.projection")
}

internal val ENTRY_MERGE_COMPLETE_ORDERED_MEMBERSHIP_CONTEXT = contextInputDefinition<Boolean>(
    ContextInputId("entry.merge.complete-ordered-membership"),
    ContributionOwner("entry-merge-membership"),
)
internal val ENTRY_MERGE_HOMOGENEOUS_MEMBERSHIP_TYPE_CONTEXT = contextInputDefinition<Boolean>(
    ContextInputId("entry.merge.homogeneous-membership-type"),
    ContributionOwner("entry-merge-membership"),
)
internal val ENTRY_MERGE_LIBRARY_INITIALIZATION_REQUIRED_CONTEXT = contextInputDefinition<Boolean>(
    ContextInputId("entry.merge.library-initialization-required"),
    ContributionOwner("entry-merge-workflow"),
)
internal val ENTRY_MERGE_COVER_CLEANUP_REQUIRED_CONTEXT = contextInputDefinition<Boolean>(
    ContextInputId("entry.merge.cover-cleanup-required"),
    ContributionOwner("entry-merge-workflow"),
)
internal val ENTRY_MERGE_DOWNLOAD_REMOVAL_REQUIRED_CONTEXT = contextInputDefinition<Boolean>(
    ContextInputId("entry.merge.download-removal-required"),
    ContributionOwner("entry-merge-workflow"),
)

private val INCOMPLETE_ORDERED_MEMBERSHIP_BLOCKER = FeatureContextBlocker(
    FeatureArtifactId("entry.merge.execution-membership-incomplete"),
    listOf(ENTRY_MERGE_COMPLETE_ORDERED_MEMBERSHIP_CONTEXT),
)
private val MIXED_MEMBERSHIP_TYPES_BLOCKER = FeatureContextBlocker(
    FeatureArtifactId("entry.merge.execution-membership-types-mixed"),
    listOf(ENTRY_MERGE_HOMOGENEOUS_MEMBERSHIP_TYPE_CONTEXT),
)
private val LIBRARY_INITIALIZATION_NOT_REQUIRED_BLOCKER = FeatureContextBlocker(
    FeatureArtifactId("entry.merge.library-initialization-not-required"),
    listOf(ENTRY_MERGE_LIBRARY_INITIALIZATION_REQUIRED_CONTEXT),
)
private val COVER_CLEANUP_NOT_REQUIRED_BLOCKER = FeatureContextBlocker(
    FeatureArtifactId("entry.merge.cover-cleanup-not-required"),
    listOf(ENTRY_MERGE_COVER_CLEANUP_REQUIRED_CONTEXT),
)
private val DOWNLOAD_REMOVAL_NOT_REQUIRED_BLOCKER = FeatureContextBlocker(
    FeatureArtifactId("entry.merge.download-removal-not-required"),
    listOf(ENTRY_MERGE_DOWNLOAD_REMOVAL_REQUIRED_CONTEXT),
)

internal fun entryMergeExecutionContextIntegrations(owner: ContributionOwner): List<FeatureIntegration> = listOf(
    FeatureIntegration(
        id = ENTRY_MERGE_EXISTING_GROUP_CONTEXT_INTEGRATION,
        prerequisites = CapabilityExpression.Always,
        contextInputs = listOf(
            ENTRY_MERGE_COMPLETE_ORDERED_MEMBERSHIP_CONTEXT,
            ENTRY_MERGE_HOMOGENEOUS_MEMBERSHIP_TYPE_CONTEXT,
        ),
        contextRule = featureContextRule(owner) { evidence ->
            when {
                !evidence.value(ENTRY_MERGE_COMPLETE_ORDERED_MEMBERSHIP_CONTEXT) ->
                    FeatureContextDecision.Blocked(listOf(INCOMPLETE_ORDERED_MEMBERSHIP_BLOCKER))
                !evidence.value(ENTRY_MERGE_HOMOGENEOUS_MEMBERSHIP_TYPE_CONTEXT) ->
                    FeatureContextDecision.Blocked(listOf(MIXED_MEMBERSHIP_TYPES_BLOCKER))
                else -> FeatureContextDecision.Applicable
            }
        },
        contextBlockers = listOf(INCOMPLETE_ORDERED_MEMBERSHIP_BLOCKER, MIXED_MEMBERSHIP_TYPES_BLOCKER),
        behaviorProjections = listOf(EntryMergeExistingGroupBehavior),
        behavioralContracts = listOf(EntryMergeBehaviorContract.EXISTING_GROUP),
    ),
    requiredBehaviorIntegration(
        id = ENTRY_MERGE_LIBRARY_INITIALIZATION_CONTEXT_INTEGRATION,
        owner = owner,
        prerequisites = CapabilityExpression.Always,
        input = ENTRY_MERGE_LIBRARY_INITIALIZATION_REQUIRED_CONTEXT,
        blocker = LIBRARY_INITIALIZATION_NOT_REQUIRED_BLOCKER,
        behaviorProjection = EntryMergeLibraryInitializationBehavior,
        contract = EntryMergeBehaviorContract.LIBRARY_INITIALIZATION,
    ),
    requiredBehaviorIntegration(
        id = ENTRY_MERGE_COVER_CLEANUP_CONTEXT_INTEGRATION,
        owner = owner,
        prerequisites = CapabilityExpression.Always,
        input = ENTRY_MERGE_COVER_CLEANUP_REQUIRED_CONTEXT,
        blocker = COVER_CLEANUP_NOT_REQUIRED_BLOCKER,
        behaviorProjection = EntryMergeCoverCleanupBehavior,
        contract = EntryMergeBehaviorContract.COVER_CLEANUP,
    ),
    requiredBehaviorIntegration(
        id = ENTRY_MERGE_DOWNLOAD_REMOVAL_CONTEXT_INTEGRATION,
        owner = owner,
        prerequisites = CapabilityExpression.Provided(EntryDownloadCapability.definition),
        input = ENTRY_MERGE_DOWNLOAD_REMOVAL_REQUIRED_CONTEXT,
        blocker = DOWNLOAD_REMOVAL_NOT_REQUIRED_BLOCKER,
        behaviorProjection = EntryMergeDownloadRemovalBehavior,
        contract = EntryMergeBehaviorContract.DOWNLOAD_REMOVAL,
    ),
)

private fun requiredBehaviorIntegration(
    id: FeatureIntegrationId,
    owner: ContributionOwner,
    prerequisites: CapabilityExpression,
    input: ContextInputDefinition<Boolean>,
    blocker: FeatureContextBlocker,
    behaviorProjection: FeatureBehaviorProjection,
    contract: EntryMergeBehaviorContract,
) = FeatureIntegration(
    id = id,
    prerequisites = prerequisites,
    contextInputs = listOf(input),
    contextRule = featureContextRule(owner) { evidence ->
        if (evidence.value(input)) {
            FeatureContextDecision.Applicable
        } else {
            FeatureContextDecision.Blocked(listOf(blocker))
        }
    },
    contextBlockers = listOf(blocker),
    behaviorProjections = listOf(behaviorProjection),
    behavioralContracts = listOf(contract),
)

internal fun FeatureGraphEvaluation.requireMergeExistingGroupContext(
    types: Set<EntryType>,
    completeOrderedMembership: Boolean,
    homogeneousMembershipType: Boolean,
) {
    types.forEach { type ->
        requireEntryContextState(
            type = type,
            feature = ENTRY_MERGE_FEATURE_ID,
            integration = ENTRY_MERGE_EXISTING_GROUP_CONTEXT_INTEGRATION,
            behaviorProjections = listOf(EntryMergeExistingGroupBehavior.id),
            evidence = listOf(
                contextEvidence(ENTRY_MERGE_COMPLETE_ORDERED_MEMBERSHIP_CONTEXT, completeOrderedMembership),
                contextEvidence(ENTRY_MERGE_HOMOGENEOUS_MEMBERSHIP_TYPE_CONTEXT, homogeneousMembershipType),
            ),
            applicable = completeOrderedMembership && homogeneousMembershipType,
        )
    }
}

internal fun FeatureGraphEvaluation.requireMergeFollowUpBehaviorContext(
    type: EntryType,
    libraryInitializationRequired: Boolean,
    coverCleanupRequired: Boolean,
    downloadRemovalRequired: Boolean?,
) {
    requireMergeFollowUpBehaviorContext(
        type,
        ENTRY_MERGE_LIBRARY_INITIALIZATION_CONTEXT_INTEGRATION,
        EntryMergeLibraryInitializationBehavior.id,
        ENTRY_MERGE_LIBRARY_INITIALIZATION_REQUIRED_CONTEXT,
        libraryInitializationRequired,
    )
    requireMergeFollowUpBehaviorContext(
        type,
        ENTRY_MERGE_COVER_CLEANUP_CONTEXT_INTEGRATION,
        EntryMergeCoverCleanupBehavior.id,
        ENTRY_MERGE_COVER_CLEANUP_REQUIRED_CONTEXT,
        coverCleanupRequired,
    )
    downloadRemovalRequired?.let { required ->
        requireMergeFollowUpBehaviorContext(
            type,
            ENTRY_MERGE_DOWNLOAD_REMOVAL_CONTEXT_INTEGRATION,
            EntryMergeDownloadRemovalBehavior.id,
            ENTRY_MERGE_DOWNLOAD_REMOVAL_REQUIRED_CONTEXT,
            required,
        )
    }
}

private fun FeatureGraphEvaluation.requireMergeFollowUpBehaviorContext(
    type: EntryType,
    integration: FeatureIntegrationId,
    behaviorProjection: FeatureArtifactId,
    input: ContextInputDefinition<Boolean>,
    required: Boolean,
) {
    requireEntryContextState(
        type = type,
        feature = ENTRY_MERGE_FEATURE_ID,
        integration = integration,
        behaviorProjections = listOf(behaviorProjection),
        evidence = listOf(contextEvidence(input, required)),
        applicable = required,
    )
}
