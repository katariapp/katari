package mihon.entry.interactions

import eu.kanade.tachiyomi.source.entry.EntryType
import mihon.feature.graph.CapabilityExpression
import mihon.feature.graph.ContributionOwner
import mihon.feature.graph.FeatureArtifactId
import mihon.feature.graph.FeatureContribution
import mihon.feature.graph.FeatureGraphContributionSink
import mihon.feature.graph.FeatureGraphContributor
import mihon.feature.graph.FeatureGraphEvaluation
import mihon.feature.graph.FeatureId
import mihon.feature.graph.FeatureIntegration
import mihon.feature.graph.FeatureIntegrationId
import mihon.feature.graph.SharedFeatureConsequence
import tachiyomi.domain.entry.model.Entry

private val ENTRY_PLAYBACK_PREFERENCES_FEATURE_ID = FeatureId("entry.playback-preferences-transfer")
private val ENTRY_PLAYBACK_PREFERENCES_FEATURE_OWNER = ContributionOwner("entry-playback-preferences-transfer")
private val ENTRY_PLAYBACK_PREFERENCES_INTEGRATION_ID =
    FeatureIntegrationId("entry.playback-preferences-transfer.provider")

internal enum class EntryPlaybackPreferencesConsequence(
    override val id: FeatureArtifactId,
) : SharedFeatureConsequence {
    BACKUP_SNAPSHOT(FeatureArtifactId("entry.playback-preferences-transfer.backup-snapshot")),
    BACKUP_RESTORE(FeatureArtifactId("entry.playback-preferences-transfer.backup-restore")),
    MIGRATION_COPY(FeatureArtifactId("entry.playback-preferences-transfer.migration-copy")),
}

internal object EntryPlaybackPreferencesFeatureContributor : FeatureGraphContributor {
    override val owner = ENTRY_PLAYBACK_PREFERENCES_FEATURE_OWNER

    override fun contributeTo(sink: FeatureGraphContributionSink) {
        sink.add(
            FeatureContribution(
                feature = ENTRY_PLAYBACK_PREFERENCES_FEATURE_ID,
                owner = owner,
                integrations = listOf(
                    FeatureIntegration(
                        id = ENTRY_PLAYBACK_PREFERENCES_INTEGRATION_ID,
                        prerequisites = CapabilityExpression.Provided(EntryPlaybackPreferencesCapability.definition),
                        sharedConsequences = EntryPlaybackPreferencesConsequence.entries,
                    ),
                ),
            ),
        )
    }
}

internal class DefaultEntryPlaybackPreferencesFeature(
    evaluation: FeatureGraphEvaluation,
    private val interaction: EntryPlaybackPreferencesInteraction,
) : EntryPlaybackPreferencesFeature {
    private val applicableTypesByConsequence =
        EntryPlaybackPreferencesConsequence.entries.associateWith { consequence ->
            evaluation.applicableProviderTypes<EntryPlaybackPreferencesProcessor>(
                feature = ENTRY_PLAYBACK_PREFERENCES_FEATURE_ID,
                integration = ENTRY_PLAYBACK_PREFERENCES_INTEGRATION_ID,
                consequence = consequence.id,
            )
        }

    private val applicableTypes = applicableTypesByConsequence.values
        .also { selectedTypes ->
            check(selectedTypes.distinct().size <= 1) {
                "Playback-preference consequences selected different provider sets: $applicableTypesByConsequence"
            }
        }
        .firstOrNull()
        .orEmpty()

    override fun isApplicable(type: EntryType): Boolean = type in applicableTypes

    override suspend fun snapshot(entry: Entry): EntryPlaybackPreferencesSnapshotResult {
        if (!isApplicable(entry.type)) return EntryPlaybackPreferencesSnapshotResult.Inapplicable(entry.type)
        return interaction.snapshot(entry)
            ?.let(EntryPlaybackPreferencesSnapshotResult::Captured)
            ?: EntryPlaybackPreferencesSnapshotResult.NoPreferences
    }

    override suspend fun restore(
        entry: Entry,
        snapshot: EntryPlaybackPreferencesSnapshot,
    ): EntryPlaybackPreferencesRestoreResult {
        if (!isApplicable(entry.type)) return EntryPlaybackPreferencesRestoreResult.Inapplicable(entry.type)
        interaction.restore(entry, snapshot)
        return EntryPlaybackPreferencesRestoreResult.Applied
    }

    override suspend fun copy(
        sourceEntry: Entry,
        targetEntry: Entry,
    ): EntryPlaybackPreferencesCopyResult {
        if (sourceEntry.type != targetEntry.type) {
            return EntryPlaybackPreferencesCopyResult.TypeMismatch(sourceEntry.type, targetEntry.type)
        }
        val inapplicableTypes = setOf(sourceEntry.type, targetEntry.type) - applicableTypes
        if (inapplicableTypes.isNotEmpty()) {
            return EntryPlaybackPreferencesCopyResult.Inapplicable(inapplicableTypes)
        }
        return if (interaction.copy(sourceEntry, targetEntry)) {
            EntryPlaybackPreferencesCopyResult.Copied
        } else {
            EntryPlaybackPreferencesCopyResult.NoPreferences
        }
    }

    override suspend fun prepareMigration(
        sourceEntry: Entry,
        targetEntry: Entry,
    ): EntryPlaybackPreferencesMigrationPreparation {
        if (sourceEntry.type != targetEntry.type) {
            return EntryPlaybackPreferencesMigrationPreparation.TypeMismatch(sourceEntry.type, targetEntry.type)
        }
        val inapplicableTypes = setOf(sourceEntry.type, targetEntry.type) - applicableTypes
        if (inapplicableTypes.isNotEmpty()) {
            return EntryPlaybackPreferencesMigrationPreparation.Inapplicable(inapplicableTypes)
        }
        return interaction.snapshot(sourceEntry)
            ?.let {
                EntryPlaybackPreferencesMigrationPreparation.Prepared(
                    EntryPlaybackPreferencesMigrationPayload(targetEntry, it),
                )
            }
            ?: EntryPlaybackPreferencesMigrationPreparation.NoPreferences
    }

    override suspend fun applyMigration(
        payload: EntryPlaybackPreferencesMigrationPayload,
    ): EntryPlaybackPreferencesRestoreResult = restore(payload.target, payload.snapshot)
}
