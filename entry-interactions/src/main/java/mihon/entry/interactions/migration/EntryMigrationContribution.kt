package mihon.entry.interactions

import mihon.feature.graph.CapabilityExpression
import mihon.feature.graph.ContributionOwner
import mihon.feature.graph.FeatureArtifactId
import mihon.feature.graph.FeatureBehaviorContract
import mihon.feature.graph.FeatureContribution
import mihon.feature.graph.FeatureGraphContributionSink
import mihon.feature.graph.FeatureGraphContributor
import mihon.feature.graph.FeatureId
import mihon.feature.graph.FeatureIntegration
import mihon.feature.graph.FeatureIntegrationId
import mihon.feature.graph.SharedFeatureConsequence
import mihon.feature.graph.allOf
import mihon.feature.graph.anyOf

internal val ENTRY_MIGRATION_FEATURE_ID = FeatureId("entry.migration")
internal val ENTRY_MIGRATION_BASE_INTEGRATION_ID = FeatureIntegrationId("entry.migration.provider")
internal val ENTRY_MIGRATION_CONSUMPTION_INTEGRATION_ID = FeatureIntegrationId("entry.migration.consumption")
internal val ENTRY_MIGRATION_BOOKMARK_INTEGRATION_ID = FeatureIntegrationId("entry.migration.bookmarking")
internal val ENTRY_MIGRATION_CHILD_STATE_OPTION_INTEGRATION_ID =
    FeatureIntegrationId("entry.migration.child-state-option")
internal val ENTRY_MIGRATION_DOWNLOAD_INTEGRATION_ID = FeatureIntegrationId("entry.migration.downloads")

private val ENTRY_MIGRATION_FEATURE_OWNER = ContributionOwner("entry-migration")

internal enum class EntryMigrationBaseConsequence(
    override val id: FeatureArtifactId,
) : SharedFeatureConsequence {
    PROVIDER_DISPATCH(FeatureArtifactId("entry.migration.provider-dispatch")),
    TARGET_SEARCH(FeatureArtifactId("entry.migration.target-search")),
    CONFIGURATION(FeatureArtifactId("entry.migration.configuration")),
    EXECUTION_COORDINATION(FeatureArtifactId("entry.migration.execution-coordination")),
    TARGET_SYNCHRONIZATION(FeatureArtifactId("entry.migration.target-synchronization")),
    ENTRY_STATE_TRANSFER(FeatureArtifactId("entry.migration.entry-state-transfer")),
    CATEGORY_TRANSFER(FeatureArtifactId("entry.migration.category-transfer")),
    TRACKING_TRANSFER(FeatureArtifactId("entry.migration.tracking-transfer")),
    CUSTOM_COVER_TRANSFER(FeatureArtifactId("entry.migration.custom-cover-transfer")),
    MERGE_REPLACEMENT(FeatureArtifactId("entry.migration.merge-replacement")),
}

internal enum class EntryMigrationConsumptionConsequence(
    override val id: FeatureArtifactId,
) : SharedFeatureConsequence {
    TRANSFER(FeatureArtifactId("entry.migration.consumption-transfer")),
}

internal enum class EntryMigrationBookmarkConsequence(
    override val id: FeatureArtifactId,
) : SharedFeatureConsequence {
    TRANSFER(FeatureArtifactId("entry.migration.bookmark-transfer")),
}

internal enum class EntryMigrationProgressConsequence(
    override val id: FeatureArtifactId,
) : SharedFeatureConsequence {
    COPY(FeatureArtifactId("entry.migration.progress-copy")),
}

internal enum class EntryMigrationPlaybackPreferencesConsequence(
    override val id: FeatureArtifactId,
) : SharedFeatureConsequence {
    COPY(FeatureArtifactId("entry.migration.playback-preferences-copy")),
}

internal enum class EntryMigrationViewerSettingsConsequence(
    override val id: FeatureArtifactId,
) : SharedFeatureConsequence {
    COPY(FeatureArtifactId("entry.migration.viewer-settings-copy")),
}

internal enum class EntryMigrationDownloadConsequence(
    override val id: FeatureArtifactId,
) : SharedFeatureConsequence {
    PARTICIPATION(FeatureArtifactId("entry.migration.download-participation")),
    CLEANUP(FeatureArtifactId("entry.migration.download-cleanup")),
}

private object EntryMigrationBehaviorContract : FeatureBehaviorContract {
    override val id = FeatureArtifactId("entry.migration.behavior")
}

private object EntryMigrationProgressCooperationContract : FeatureBehaviorContract {
    override val id = FeatureArtifactId("entry.migration.progress-cooperation")
}

private object EntryMigrationViewerSettingsCooperationContract : FeatureBehaviorContract {
    override val id = FeatureArtifactId("entry.migration.viewer-settings-cooperation")
}

private object EntryMigrationDownloadCooperationContract : FeatureBehaviorContract {
    override val id = FeatureArtifactId("entry.migration.download-cooperation")
}

internal object EntryMigrationFeatureContributor : FeatureGraphContributor {
    override val owner = ENTRY_MIGRATION_FEATURE_OWNER

    override fun contributeTo(sink: FeatureGraphContributionSink) {
        val migration = CapabilityExpression.Provided(EntryMigrationCapability.definition)
        val consumption = CapabilityExpression.Provided(EntryConsumptionCapability.definition)
        val bookmark = CapabilityExpression.Provided(EntryBookmarkCapability.definition)
        val download = CapabilityExpression.Provided(EntryDownloadCapability.definition)
        val migrationDownload = allOf(migration, download)
        sink.add(
            FeatureContribution(
                feature = ENTRY_MIGRATION_FEATURE_ID,
                owner = owner,
                integrations = listOf(
                    FeatureIntegration(
                        id = ENTRY_MIGRATION_BASE_INTEGRATION_ID,
                        prerequisites = migration,
                        sharedConsequences = EntryMigrationBaseConsequence.entries,
                        behavioralContracts = listOf(EntryMigrationBehaviorContract),
                    ),
                    entryMigrationSourceContextIntegration(owner, migration),
                    entryMigrationSelectionContextIntegration(owner, migration),
                    FeatureIntegration(
                        id = ENTRY_MIGRATION_CONSUMPTION_INTEGRATION_ID,
                        prerequisites = allOf(migration, consumption),
                        sharedConsequences = EntryMigrationConsumptionConsequence.entries,
                    ),
                    FeatureIntegration(
                        id = ENTRY_MIGRATION_BOOKMARK_INTEGRATION_ID,
                        prerequisites = allOf(migration, bookmark),
                        sharedConsequences = EntryMigrationBookmarkConsequence.entries,
                    ),
                    FeatureIntegration(
                        id = ENTRY_MIGRATION_CHILD_STATE_OPTION_INTEGRATION_ID,
                        prerequisites = allOf(migration, anyOf(consumption, bookmark)),
                        sharedConsequences = listOf(EntryMigrationChildStateOptionConsequence),
                    ),
                    FeatureIntegration(
                        id = FeatureIntegrationId("entry.migration.progress"),
                        prerequisites = allOf(
                            migration,
                            CapabilityExpression.Provided(EntryProgressCapability.definition),
                        ),
                        sharedConsequences = EntryMigrationProgressConsequence.entries,
                        behavioralContracts = listOf(EntryMigrationProgressCooperationContract),
                    ),
                    FeatureIntegration(
                        id = FeatureIntegrationId("entry.migration.playback-preferences"),
                        prerequisites = allOf(
                            migration,
                            CapabilityExpression.Provided(EntryPlaybackPreferencesCapability.definition),
                        ),
                        sharedConsequences = EntryMigrationPlaybackPreferencesConsequence.entries,
                    ),
                    FeatureIntegration(
                        id = FeatureIntegrationId("entry.migration.viewer-settings"),
                        prerequisites = allOf(
                            migration,
                            CapabilityExpression.Provided(EntryViewerSettingsCapability.definition),
                        ),
                        sharedConsequences = EntryMigrationViewerSettingsConsequence.entries,
                        behavioralContracts = listOf(EntryMigrationViewerSettingsCooperationContract),
                    ),
                    FeatureIntegration(
                        id = ENTRY_MIGRATION_DOWNLOAD_INTEGRATION_ID,
                        prerequisites = migrationDownload,
                        sharedConsequences = EntryMigrationDownloadConsequence.entries,
                        behavioralContracts = listOf(EntryMigrationDownloadCooperationContract),
                    ),
                ) +
                    entryMigrationPreparationContextIntegrations(owner, migration) +
                    entryMigrationOptionContextIntegrations(owner, migration, migrationDownload) +
                    listOf(entryMigrationExecutionContextIntegration(owner, migration)),
            ),
        )
    }
}
