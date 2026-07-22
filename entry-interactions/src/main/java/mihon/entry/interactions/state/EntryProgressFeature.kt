package mihon.entry.interactions

import eu.kanade.tachiyomi.source.entry.EntryType
import mihon.entry.interactions.documentation.EntryContentTypeReferenceSection
import mihon.entry.interactions.documentation.entryContentTypeReferenceContribution
import mihon.feature.graph.CapabilityExpression
import mihon.feature.graph.ContributionOwner
import mihon.feature.graph.FeatureArtifactId
import mihon.feature.graph.FeatureBehaviorContract
import mihon.feature.graph.FeatureContribution
import mihon.feature.graph.FeatureGraphContributionSink
import mihon.feature.graph.FeatureGraphContributor
import mihon.feature.graph.FeatureGraphEvaluation
import mihon.feature.graph.FeatureId
import mihon.feature.graph.FeatureIntegration
import mihon.feature.graph.FeatureIntegrationId
import mihon.feature.graph.SharedFeatureConsequence
import mihon.feature.graph.allOf
import tachiyomi.domain.entry.model.Entry

internal val ENTRY_PROGRESS_FEATURE_ID = FeatureId("entry.progress-transfer")
internal val ENTRY_PROGRESS_INTEGRATION_ID = FeatureIntegrationId("entry.progress-transfer.provider")
private val ENTRY_PROGRESS_MIGRATION_INTEGRATION_ID = FeatureIntegrationId("entry.progress-transfer.migration")
private val ENTRY_PROGRESS_FEATURE_OWNER = ContributionOwner("entry-progress-transfer")
private val ENTRY_PROGRESS_REFERENCE = entryContentTypeReferenceContribution(
    id = "progress-transfer",
    owner = ENTRY_PROGRESS_FEATURE_OWNER,
    section = EntryContentTypeReferenceSection.ENTRY_INTERACTIONS,
    label = "Preserve progress through backup and migration",
    order = 350,
)

internal enum class EntryProgressConsequence(
    override val id: FeatureArtifactId,
) : SharedFeatureConsequence {
    DISPATCH(FeatureArtifactId("entry.progress-transfer.dispatch")),
    BACKUP_CREATE(FeatureArtifactId("entry.progress-transfer.backup-create")),
    BACKUP_RESTORE(FeatureArtifactId("entry.progress-transfer.backup-restore")),
}

private object EntryProgressMigrationConsequence : SharedFeatureConsequence {
    override val id = FeatureArtifactId("entry.progress-transfer.migration-copy")
}

internal object EntryProgressBehaviorContract : FeatureBehaviorContract {
    override val id = FeatureArtifactId("entry.progress-transfer.behavior")
}

internal object EntryProgressFeatureContributor : FeatureGraphContributor {
    override val owner = ENTRY_PROGRESS_FEATURE_OWNER

    override fun contributeTo(sink: FeatureGraphContributionSink) {
        sink.add(
            FeatureContribution(
                feature = ENTRY_PROGRESS_FEATURE_ID,
                owner = owner,
                integrations = listOf(
                    FeatureIntegration(
                        id = ENTRY_PROGRESS_INTEGRATION_ID,
                        prerequisites = CapabilityExpression.Provided(EntryProgressCapability.definition),
                        sharedConsequences = EntryProgressConsequence.entries,
                        behavioralContracts = listOf(EntryProgressBehaviorContract),
                        projectionRequirements = listOf(ENTRY_PROGRESS_REFERENCE.requirement),
                        projections = listOf(ENTRY_PROGRESS_REFERENCE.projection),
                    ),
                    FeatureIntegration(
                        id = ENTRY_PROGRESS_MIGRATION_INTEGRATION_ID,
                        prerequisites = allOf(
                            CapabilityExpression.Provided(EntryProgressCapability.definition),
                            CapabilityExpression.Provided(EntryMigrationCapability.definition),
                        ),
                        sharedConsequences = listOf(EntryProgressMigrationConsequence),
                    ),
                ),
            ),
        )
    }
}

internal class DefaultEntryProgressFeature(
    evaluation: FeatureGraphEvaluation,
    private val interaction: EntryProgressInteraction,
) : EntryProgressFeature {
    private val applicableTypes = EntryProgressConsequence.entries
        .map { consequence ->
            evaluation.applicableProviderTypes<EntryProgressProcessor>(
                feature = ENTRY_PROGRESS_FEATURE_ID,
                integration = ENTRY_PROGRESS_INTEGRATION_ID,
                consequence = consequence.id,
            )
        }
        .also { selectedTypes ->
            check(selectedTypes.distinct().size <= 1) {
                "Progress-transfer consequences selected different provider sets: $selectedTypes"
            }
        }
        .firstOrNull()
        .orEmpty()
    private val migrationTypes = evaluation.applicableProviderTypes<EntryProgressProcessor>(
        feature = ENTRY_PROGRESS_FEATURE_ID,
        integration = ENTRY_PROGRESS_MIGRATION_INTEGRATION_ID,
        consequence = EntryProgressMigrationConsequence.id,
    )

    override fun isApplicable(type: EntryType): Boolean = type in applicableTypes

    override suspend fun snapshot(entry: Entry): EntryProgressSnapshotResult {
        if (!isApplicable(entry.type)) return EntryProgressSnapshotResult.Inapplicable(entry.type)
        return EntryProgressSnapshotResult.Available(interaction.snapshot(entry))
    }

    override suspend fun restore(
        entry: Entry,
        snapshot: EntryProgressSnapshot,
    ): EntryProgressRestoreResult {
        if (!isApplicable(entry.type)) return EntryProgressRestoreResult.Inapplicable(entry.type)
        interaction.restore(entry, snapshot)
        return EntryProgressRestoreResult.Applied
    }

    override suspend fun copy(
        sourceEntry: Entry,
        targetEntry: Entry,
        resourceMappings: List<EntryProgressResourceMapping>,
    ): EntryProgressCopyResult {
        if (sourceEntry.type != targetEntry.type) {
            return EntryProgressCopyResult.IncompatibleTypes(sourceEntry.type, targetEntry.type)
        }

        val inapplicableTypes = setOf(sourceEntry.type, targetEntry.type) - migrationTypes
        if (inapplicableTypes.isNotEmpty()) return EntryProgressCopyResult.Inapplicable(inapplicableTypes)

        interaction.copy(sourceEntry, targetEntry, resourceMappings)
        return EntryProgressCopyResult.Applied
    }

    override suspend fun prepareMigration(
        sourceEntry: Entry,
        targetEntry: Entry,
        resourceMappings: List<EntryProgressResourceMapping>,
    ): EntryProgressMigrationPreparation {
        if (sourceEntry.type != targetEntry.type) {
            return EntryProgressMigrationPreparation.IncompatibleTypes(sourceEntry.type, targetEntry.type)
        }
        val inapplicableTypes = setOf(sourceEntry.type, targetEntry.type) - migrationTypes
        if (inapplicableTypes.isNotEmpty()) return EntryProgressMigrationPreparation.Inapplicable(inapplicableTypes)

        val sourceStates = interaction.snapshot(sourceEntry).states
            .associateBy { it.contentKey to it.resourceKey }
        val targetStates = resourceMappings.mapNotNull { mapping ->
            sourceStates[mapping.sourceContentKey to mapping.sourceResourceKey]?.copy(
                contentKey = mapping.targetContentKey,
                resourceKey = mapping.targetResourceKey,
                sourceChildKey = mapping.targetResourceKey,
            )
        }
        return EntryProgressMigrationPreparation.Prepared(
            EntryProgressMigrationPayload(targetEntry, EntryProgressSnapshot(targetStates)),
        )
    }

    override suspend fun applyMigration(payload: EntryProgressMigrationPayload): EntryProgressRestoreResult {
        return restore(payload.target, payload.snapshot)
    }
}
