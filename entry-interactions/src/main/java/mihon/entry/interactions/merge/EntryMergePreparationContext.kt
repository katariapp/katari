package mihon.entry.interactions

import eu.kanade.tachiyomi.source.entry.EntryType
import mihon.feature.graph.CapabilityExpression
import mihon.feature.graph.ContextInputId
import mihon.feature.graph.ContributionOwner
import mihon.feature.graph.FeatureArtifactId
import mihon.feature.graph.FeatureContextBlocker
import mihon.feature.graph.FeatureContextDecision
import mihon.feature.graph.FeatureGraphEvaluation
import mihon.feature.graph.FeatureIntegration
import mihon.feature.graph.FeatureIntegrationId
import mihon.feature.graph.SharedFeatureConsequence
import mihon.feature.graph.contextEvidence
import mihon.feature.graph.contextInputDefinition
import mihon.feature.graph.featureContextRule

private val SELECTION_CONTEXT_INTEGRATION = FeatureIntegrationId("entry.merge.preparation-selection-context")
private val AUTHORITY_CONTEXT_INTEGRATION = FeatureIntegrationId("entry.merge.preparation-authority-context")
private val MEMBERSHIP_CONTEXT_INTEGRATION = FeatureIntegrationId("entry.merge.preparation-membership-context")

private object EntryMergeSelectionConsequence : SharedFeatureConsequence {
    override val id = FeatureArtifactId("entry.merge.preparation-selection")
}

private object EntryMergeAuthorityConsequence : SharedFeatureConsequence {
    override val id = FeatureArtifactId("entry.merge.preparation-authority")
}

private object EntryMergeEditorConsequence : SharedFeatureConsequence {
    override val id = FeatureArtifactId("entry.merge.editor")
}

private val HOMOGENEOUS_TYPE_CONTEXT = contextInputDefinition<Boolean>(
    ContextInputId("entry.merge.homogeneous-selection-type"),
    ContributionOwner("entry-selection"),
)
private val HOMOGENEOUS_PROFILE_CONTEXT = contextInputDefinition<Boolean>(
    ContextInputId("entry.merge.homogeneous-selection-profile"),
    ContributionOwner("entry-selection"),
)
private val AUTHORITATIVE_SELECTION_CONTEXT = contextInputDefinition<Boolean>(
    ContextInputId("entry.merge.authoritative-selection-present"),
    ContributionOwner("entry-merge-host"),
)
private val AUTHORITATIVE_TYPE_CONTEXT = contextInputDefinition<Boolean>(
    ContextInputId("entry.merge.authoritative-selection-type-stable"),
    ContributionOwner("entry-merge-host"),
)
private val AUTHORITATIVE_PROFILE_CONTEXT = contextInputDefinition<Boolean>(
    ContextInputId("entry.merge.authoritative-selection-profile-stable"),
    ContributionOwner("entry-merge-host"),
)
private val SINGLE_EXISTING_GROUP_CONTEXT = contextInputDefinition<Boolean>(
    ContextInputId("entry.merge.single-existing-group"),
    ContributionOwner("entry-merge-membership"),
)
private val COMPLETE_EXISTING_GROUP_CONTEXT = contextInputDefinition<Boolean>(
    ContextInputId("entry.merge.complete-existing-group"),
    ContributionOwner("entry-merge-membership"),
)
private val SUFFICIENT_EDITOR_MEMBERS_CONTEXT = contextInputDefinition<Boolean>(
    ContextInputId("entry.merge.sufficient-editor-members"),
    ContributionOwner("entry-merge-membership"),
)

private val MIXED_TYPES_BLOCKER = FeatureContextBlocker(
    FeatureArtifactId("entry.merge.mixed-selection-types"),
    listOf(HOMOGENEOUS_TYPE_CONTEXT),
)
private val MIXED_PROFILES_BLOCKER = FeatureContextBlocker(
    FeatureArtifactId("entry.merge.mixed-selection-profiles"),
    listOf(HOMOGENEOUS_PROFILE_CONTEXT),
)
private val AUTHORITATIVE_SELECTION_MISSING_BLOCKER = FeatureContextBlocker(
    FeatureArtifactId("entry.merge.authoritative-selection-missing"),
    listOf(AUTHORITATIVE_SELECTION_CONTEXT),
)
private val AUTHORITATIVE_TYPE_CHANGED_BLOCKER = FeatureContextBlocker(
    FeatureArtifactId("entry.merge.authoritative-selection-type-changed"),
    listOf(AUTHORITATIVE_TYPE_CONTEXT),
)
private val AUTHORITATIVE_PROFILE_CHANGED_BLOCKER = FeatureContextBlocker(
    FeatureArtifactId("entry.merge.authoritative-selection-profile-changed"),
    listOf(AUTHORITATIVE_PROFILE_CONTEXT),
)
private val MULTIPLE_EXISTING_GROUPS_BLOCKER = FeatureContextBlocker(
    FeatureArtifactId("entry.merge.multiple-existing-groups"),
    listOf(SINGLE_EXISTING_GROUP_CONTEXT),
)
private val INCOMPLETE_EXISTING_GROUP_BLOCKER = FeatureContextBlocker(
    FeatureArtifactId("entry.merge.incomplete-existing-group"),
    listOf(COMPLETE_EXISTING_GROUP_CONTEXT),
)
private val TOO_FEW_EDITOR_MEMBERS_BLOCKER = FeatureContextBlocker(
    FeatureArtifactId("entry.merge.too-few-editor-members"),
    listOf(SUFFICIENT_EDITOR_MEMBERS_CONTEXT),
)

internal fun entryMergePreparationContextIntegrations(owner: ContributionOwner): List<FeatureIntegration> = listOf(
    FeatureIntegration(
        id = SELECTION_CONTEXT_INTEGRATION,
        prerequisites = CapabilityExpression.Always,
        contextInputs = listOf(HOMOGENEOUS_TYPE_CONTEXT, HOMOGENEOUS_PROFILE_CONTEXT),
        contextRule = featureContextRule(owner) { evidence ->
            when {
                !evidence.value(HOMOGENEOUS_TYPE_CONTEXT) ->
                    FeatureContextDecision.Blocked(listOf(MIXED_TYPES_BLOCKER))
                !evidence.value(HOMOGENEOUS_PROFILE_CONTEXT) ->
                    FeatureContextDecision.Blocked(listOf(MIXED_PROFILES_BLOCKER))
                else -> FeatureContextDecision.Applicable
            }
        },
        contextBlockers = listOf(MIXED_TYPES_BLOCKER, MIXED_PROFILES_BLOCKER),
        sharedConsequences = listOf(EntryMergeSelectionConsequence),
    ),
    FeatureIntegration(
        id = AUTHORITY_CONTEXT_INTEGRATION,
        prerequisites = CapabilityExpression.Always,
        contextInputs = listOf(
            AUTHORITATIVE_SELECTION_CONTEXT,
            AUTHORITATIVE_TYPE_CONTEXT,
            AUTHORITATIVE_PROFILE_CONTEXT,
        ),
        contextRule = featureContextRule(owner) { evidence ->
            when {
                !evidence.value(AUTHORITATIVE_SELECTION_CONTEXT) ->
                    FeatureContextDecision.Blocked(listOf(AUTHORITATIVE_SELECTION_MISSING_BLOCKER))
                !evidence.value(AUTHORITATIVE_TYPE_CONTEXT) ->
                    FeatureContextDecision.Blocked(listOf(AUTHORITATIVE_TYPE_CHANGED_BLOCKER))
                !evidence.value(AUTHORITATIVE_PROFILE_CONTEXT) ->
                    FeatureContextDecision.Blocked(listOf(AUTHORITATIVE_PROFILE_CHANGED_BLOCKER))
                else -> FeatureContextDecision.Applicable
            }
        },
        contextBlockers = listOf(
            AUTHORITATIVE_SELECTION_MISSING_BLOCKER,
            AUTHORITATIVE_TYPE_CHANGED_BLOCKER,
            AUTHORITATIVE_PROFILE_CHANGED_BLOCKER,
        ),
        sharedConsequences = listOf(EntryMergeAuthorityConsequence),
    ),
    FeatureIntegration(
        id = MEMBERSHIP_CONTEXT_INTEGRATION,
        prerequisites = CapabilityExpression.Always,
        contextInputs = listOf(
            SINGLE_EXISTING_GROUP_CONTEXT,
            COMPLETE_EXISTING_GROUP_CONTEXT,
            SUFFICIENT_EDITOR_MEMBERS_CONTEXT,
        ),
        contextRule = featureContextRule(owner) { evidence ->
            when {
                !evidence.value(SINGLE_EXISTING_GROUP_CONTEXT) ->
                    FeatureContextDecision.Blocked(listOf(MULTIPLE_EXISTING_GROUPS_BLOCKER))
                !evidence.value(COMPLETE_EXISTING_GROUP_CONTEXT) ->
                    FeatureContextDecision.Blocked(listOf(INCOMPLETE_EXISTING_GROUP_BLOCKER))
                !evidence.value(SUFFICIENT_EDITOR_MEMBERS_CONTEXT) ->
                    FeatureContextDecision.Blocked(listOf(TOO_FEW_EDITOR_MEMBERS_BLOCKER))
                else -> FeatureContextDecision.Applicable
            }
        },
        contextBlockers = listOf(
            MULTIPLE_EXISTING_GROUPS_BLOCKER,
            INCOMPLETE_EXISTING_GROUP_BLOCKER,
            TOO_FEW_EDITOR_MEMBERS_BLOCKER,
        ),
        sharedConsequences = listOf(EntryMergeEditorConsequence),
    ),
)

internal fun FeatureGraphEvaluation.requireMergeSelectionContext(
    types: Set<EntryType>,
    homogeneousType: Boolean,
    homogeneousProfile: Boolean,
) {
    types.forEach { type ->
        requireEntryContextState(
            type = type,
            feature = ENTRY_MERGE_FEATURE_ID,
            integration = SELECTION_CONTEXT_INTEGRATION,
            consequences = listOf(EntryMergeSelectionConsequence.id),
            evidence = listOf(
                contextEvidence(HOMOGENEOUS_TYPE_CONTEXT, homogeneousType),
                contextEvidence(HOMOGENEOUS_PROFILE_CONTEXT, homogeneousProfile),
            ),
            applicable = homogeneousType && homogeneousProfile,
        )
    }
}

internal fun FeatureGraphEvaluation.requireMergeAuthorityContext(
    type: EntryType,
    authoritativeSelectionPresent: Boolean,
    typeStable: Boolean,
    profileStable: Boolean,
) {
    requireEntryContextState(
        type = type,
        feature = ENTRY_MERGE_FEATURE_ID,
        integration = AUTHORITY_CONTEXT_INTEGRATION,
        consequences = listOf(EntryMergeAuthorityConsequence.id),
        evidence = listOf(
            contextEvidence(AUTHORITATIVE_SELECTION_CONTEXT, authoritativeSelectionPresent),
            contextEvidence(AUTHORITATIVE_TYPE_CONTEXT, typeStable),
            contextEvidence(AUTHORITATIVE_PROFILE_CONTEXT, profileStable),
        ),
        applicable = authoritativeSelectionPresent && typeStable && profileStable,
    )
}

internal fun FeatureGraphEvaluation.requireMergeMembershipContext(
    type: EntryType,
    singleExistingGroup: Boolean,
    completeExistingGroup: Boolean,
    sufficientEditorMembers: Boolean,
) {
    requireEntryContextState(
        type = type,
        feature = ENTRY_MERGE_FEATURE_ID,
        integration = MEMBERSHIP_CONTEXT_INTEGRATION,
        consequences = listOf(EntryMergeEditorConsequence.id),
        evidence = listOf(
            contextEvidence(SINGLE_EXISTING_GROUP_CONTEXT, singleExistingGroup),
            contextEvidence(COMPLETE_EXISTING_GROUP_CONTEXT, completeExistingGroup),
            contextEvidence(SUFFICIENT_EDITOR_MEMBERS_CONTEXT, sufficientEditorMembers),
        ),
        applicable = singleExistingGroup && completeExistingGroup && sufficientEditorMembers,
    )
}
