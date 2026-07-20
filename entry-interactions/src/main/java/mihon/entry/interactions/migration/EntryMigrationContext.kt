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

private val SOURCE_CONTEXT_INTEGRATION = FeatureIntegrationId("entry.migration.source-context")
private val SELECTION_CONTEXT_INTEGRATION = FeatureIntegrationId("entry.migration.selection-context")

private enum class EntryMigrationSourceConsequence(
    override val id: FeatureArtifactId,
) : SharedFeatureConsequence {
    AVAILABILITY(FeatureArtifactId("entry.migration.availability")),
    ENTRY_ACTION(FeatureArtifactId("entry.migration.entry-action")),
    BROWSE_SOURCE_LIST(FeatureArtifactId("entry.migration.browse-source-list")),
    SOURCE_ENTRY_SELECTION(FeatureArtifactId("entry.migration.source-entry-selection")),
}

private object EntryMigrationSelectionConsequence : SharedFeatureConsequence {
    override val id = FeatureArtifactId("entry.migration.library-selection")
}

internal val ENTRY_MIGRATION_PERSISTED_CONTEXT = contextInputDefinition<Boolean>(
    ContextInputId("entry.migration.persisted"),
    ContributionOwner("entry-state"),
)
internal val ENTRY_MIGRATION_LIBRARY_MEMBERSHIP_CONTEXT = contextInputDefinition<Boolean>(
    ContextInputId("entry.migration.library-membership"),
    ContributionOwner("entry-library-state"),
)
private val SINGLE_PROFILE_CONTEXT = contextInputDefinition<Boolean>(
    ContextInputId("entry.migration.single-profile-selection"),
    ContributionOwner("entry-selection"),
)

private val UNPERSISTED_BLOCKER = FeatureContextBlocker(
    FeatureArtifactId("entry.migration.unpersisted"),
    listOf(ENTRY_MIGRATION_PERSISTED_CONTEXT),
)
private val NOT_IN_LIBRARY_BLOCKER = FeatureContextBlocker(
    FeatureArtifactId("entry.migration.not-in-library"),
    listOf(ENTRY_MIGRATION_LIBRARY_MEMBERSHIP_CONTEXT),
)
private val MIXED_PROFILES_BLOCKER = FeatureContextBlocker(
    FeatureArtifactId("entry.migration.mixed-selection-profiles"),
    listOf(SINGLE_PROFILE_CONTEXT),
)

internal fun entryMigrationSourceContextIntegration(
    owner: ContributionOwner,
    migration: CapabilityExpression,
) = FeatureIntegration(
    id = SOURCE_CONTEXT_INTEGRATION,
    prerequisites = migration,
    contextInputs = listOf(ENTRY_MIGRATION_PERSISTED_CONTEXT, ENTRY_MIGRATION_LIBRARY_MEMBERSHIP_CONTEXT),
    contextRule = featureContextRule(owner) { evidence ->
        when {
            !evidence.value(ENTRY_MIGRATION_PERSISTED_CONTEXT) ->
                FeatureContextDecision.Blocked(listOf(UNPERSISTED_BLOCKER))
            !evidence.value(
                ENTRY_MIGRATION_LIBRARY_MEMBERSHIP_CONTEXT,
            ) -> FeatureContextDecision.Blocked(listOf(NOT_IN_LIBRARY_BLOCKER))
            else -> FeatureContextDecision.Applicable
        }
    },
    contextBlockers = listOf(UNPERSISTED_BLOCKER, NOT_IN_LIBRARY_BLOCKER),
    sharedConsequences = EntryMigrationSourceConsequence.entries,
)

internal fun entryMigrationSelectionContextIntegration(
    owner: ContributionOwner,
    migration: CapabilityExpression,
) = FeatureIntegration(
    id = SELECTION_CONTEXT_INTEGRATION,
    prerequisites = migration,
    contextInputs = listOf(
        ENTRY_MIGRATION_PERSISTED_CONTEXT,
        ENTRY_MIGRATION_LIBRARY_MEMBERSHIP_CONTEXT,
        SINGLE_PROFILE_CONTEXT,
    ),
    contextRule = featureContextRule(owner) { evidence ->
        when {
            !evidence.value(SINGLE_PROFILE_CONTEXT) -> FeatureContextDecision.Blocked(listOf(MIXED_PROFILES_BLOCKER))
            !evidence.value(ENTRY_MIGRATION_PERSISTED_CONTEXT) ->
                FeatureContextDecision.Blocked(listOf(UNPERSISTED_BLOCKER))
            !evidence.value(
                ENTRY_MIGRATION_LIBRARY_MEMBERSHIP_CONTEXT,
            ) -> FeatureContextDecision.Blocked(listOf(NOT_IN_LIBRARY_BLOCKER))
            else -> FeatureContextDecision.Applicable
        }
    },
    contextBlockers = listOf(MIXED_PROFILES_BLOCKER, UNPERSISTED_BLOCKER, NOT_IN_LIBRARY_BLOCKER),
    sharedConsequences = listOf(EntryMigrationSelectionConsequence),
)

internal fun FeatureGraphEvaluation.requireMigrationSourceContext(
    type: EntryType,
    persisted: Boolean,
    inLibrary: Boolean,
) {
    requireEntryContextState(
        type = type,
        feature = ENTRY_MIGRATION_FEATURE_ID,
        integration = SOURCE_CONTEXT_INTEGRATION,
        consequences = EntryMigrationSourceConsequence.entries.map(EntryMigrationSourceConsequence::id),
        evidence = listOf(
            contextEvidence(ENTRY_MIGRATION_PERSISTED_CONTEXT, persisted),
            contextEvidence(ENTRY_MIGRATION_LIBRARY_MEMBERSHIP_CONTEXT, inLibrary),
        ),
        applicable = persisted && inLibrary,
    )
}

internal fun FeatureGraphEvaluation.requireMigrationSelectionContext(
    type: EntryType,
    persisted: Boolean,
    inLibrary: Boolean,
    singleProfile: Boolean,
) {
    requireEntryContextState(
        type = type,
        feature = ENTRY_MIGRATION_FEATURE_ID,
        integration = SELECTION_CONTEXT_INTEGRATION,
        consequences = listOf(EntryMigrationSelectionConsequence.id),
        evidence = listOf(
            contextEvidence(ENTRY_MIGRATION_PERSISTED_CONTEXT, persisted),
            contextEvidence(ENTRY_MIGRATION_LIBRARY_MEMBERSHIP_CONTEXT, inLibrary),
            contextEvidence(SINGLE_PROFILE_CONTEXT, singleProfile),
        ),
        applicable = singleProfile && persisted && inLibrary,
    )
}
