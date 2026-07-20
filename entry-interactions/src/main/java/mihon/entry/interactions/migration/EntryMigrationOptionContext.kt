package mihon.entry.interactions

import eu.kanade.tachiyomi.source.entry.EntryType
import mihon.feature.graph.CapabilityExpression
import mihon.feature.graph.ContextInputDefinition
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

internal object EntryMigrationChildStateOptionConsequence : SharedFeatureConsequence {
    override val id = FeatureArtifactId("entry.migration.child-state-option-availability")
}

internal enum class EntryMigrationContextualOption(
    val integration: FeatureIntegrationId,
    val consequence: FeatureArtifactId,
    val input: ContextInputDefinition<Boolean>,
    val blockerId: FeatureArtifactId,
) {
    CATEGORIES(
        integration = FeatureIntegrationId("entry.migration.categories-option-context"),
        consequence = FeatureArtifactId("entry.migration.categories-option-availability"),
        input = contextInputDefinition(
            ContextInputId("entry.migration.has-categories"),
            ContributionOwner("entry-category-state"),
        ),
        blockerId = FeatureArtifactId("entry.migration.no-categories"),
    ),
    NOTES(
        integration = FeatureIntegrationId("entry.migration.notes-option-context"),
        consequence = FeatureArtifactId("entry.migration.notes-option-availability"),
        input = contextInputDefinition(
            ContextInputId("entry.migration.has-notes"),
            ContributionOwner("entry-state"),
        ),
        blockerId = FeatureArtifactId("entry.migration.no-notes"),
    ),
    CUSTOM_COVER(
        integration = FeatureIntegrationId("entry.migration.custom-cover-option-context"),
        consequence = FeatureArtifactId("entry.migration.custom-cover-option-availability"),
        input = contextInputDefinition(
            ContextInputId("entry.migration.has-custom-cover"),
            ContributionOwner("entry-cover-state"),
        ),
        blockerId = FeatureArtifactId("entry.migration.no-custom-cover"),
    ),
    DOWNLOADS(
        integration = FeatureIntegrationId("entry.migration.download-option-context"),
        consequence = FeatureArtifactId("entry.migration.download-option-availability"),
        input = contextInputDefinition(
            ContextInputId("entry.migration.has-downloads"),
            ContributionOwner("entry-download-state"),
        ),
        blockerId = FeatureArtifactId("entry.migration.no-downloads"),
    ),
}

private data class EntryMigrationOptionDefinition(
    val blocker: FeatureContextBlocker,
)

private val OPTION_DEFINITIONS = EntryMigrationContextualOption.entries.associateWith { option ->
    EntryMigrationOptionDefinition(
        blocker = FeatureContextBlocker(
            id = option.blockerId,
            inputs = listOf(option.input),
        ),
    )
}

internal fun entryMigrationOptionContextIntegrations(
    owner: ContributionOwner,
    migration: CapabilityExpression,
    download: CapabilityExpression,
): List<FeatureIntegration> = EntryMigrationContextualOption.entries.map { option ->
    val definition = OPTION_DEFINITIONS.getValue(option)
    FeatureIntegration(
        id = option.integration,
        prerequisites = if (option == EntryMigrationContextualOption.DOWNLOADS) download else migration,
        contextInputs = listOf(option.input),
        contextRule = featureContextRule(owner) { evidence ->
            if (evidence.value(option.input)) {
                FeatureContextDecision.Applicable
            } else {
                FeatureContextDecision.Blocked(listOf(definition.blocker))
            }
        },
        contextBlockers = listOf(definition.blocker),
        sharedConsequences = listOf(
            object : SharedFeatureConsequence {
                override val id = option.consequence
            },
        ),
    )
}

internal fun FeatureGraphEvaluation.requireMigrationOptionContext(
    type: EntryType,
    option: EntryMigrationContextualOption,
    available: Boolean,
) {
    requireEntryContextState(
        type = type,
        feature = ENTRY_MIGRATION_FEATURE_ID,
        integration = option.integration,
        consequences = listOf(option.consequence),
        evidence = listOf(contextEvidence(option.input, available)),
        applicable = available,
    )
}
