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

internal val ENTRY_MIGRATION_FEATURE_ID = FeatureId("entry.migration")
internal val ENTRY_MIGRATION_BASE_INTEGRATION_ID = FeatureIntegrationId("entry.migration.provider")

private val ENTRY_MIGRATION_FEATURE_OWNER = ContributionOwner("entry-migration")

internal enum class EntryMigrationBaseConsequence(
    override val id: FeatureArtifactId,
) : SharedFeatureConsequence {
    AVAILABILITY(FeatureArtifactId("entry.migration.availability")),
    ENTRY_ACTION(FeatureArtifactId("entry.migration.entry-action")),
    LIBRARY_SELECTION(FeatureArtifactId("entry.migration.library-selection")),
    BROWSE_SOURCE_LIST(FeatureArtifactId("entry.migration.browse-source-list")),
    SOURCE_ENTRY_SELECTION(FeatureArtifactId("entry.migration.source-entry-selection")),
    TARGET_SEARCH(FeatureArtifactId("entry.migration.target-search")),
    CONFIGURATION(FeatureArtifactId("entry.migration.configuration")),
    EXECUTION(FeatureArtifactId("entry.migration.execution")),
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
    OPTION_AVAILABILITY(FeatureArtifactId("entry.migration.download-option-availability")),
    CLEANUP(FeatureArtifactId("entry.migration.download-cleanup")),
}

private object EntryMigrationBehaviorContract : FeatureBehaviorContract {
    override val id = FeatureArtifactId("entry.migration.behavior")
}

internal object EntryMigrationFeatureContributor : FeatureGraphContributor {
    override val owner = ENTRY_MIGRATION_FEATURE_OWNER

    override fun contributeTo(sink: FeatureGraphContributionSink) {
        val migration = CapabilityExpression.Provided(EntryMigrationCapability.definition)
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
                    FeatureIntegration(
                        id = FeatureIntegrationId("entry.migration.consumption"),
                        prerequisites = allOf(
                            migration,
                            CapabilityExpression.Provided(EntryConsumptionCapability.definition),
                        ),
                        sharedConsequences = EntryMigrationConsumptionConsequence.entries,
                    ),
                    FeatureIntegration(
                        id = FeatureIntegrationId("entry.migration.bookmarking"),
                        prerequisites = allOf(
                            migration,
                            CapabilityExpression.Provided(EntryBookmarkCapability.definition),
                        ),
                        sharedConsequences = EntryMigrationBookmarkConsequence.entries,
                    ),
                    FeatureIntegration(
                        id = FeatureIntegrationId("entry.migration.progress"),
                        prerequisites = allOf(
                            migration,
                            CapabilityExpression.Provided(EntryProgressCapability.definition),
                        ),
                        sharedConsequences = EntryMigrationProgressConsequence.entries,
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
                    ),
                    FeatureIntegration(
                        id = FeatureIntegrationId("entry.migration.downloads"),
                        prerequisites = allOf(
                            migration,
                            CapabilityExpression.Provided(EntryDownloadCapability.definition),
                        ),
                        sharedConsequences = EntryMigrationDownloadConsequence.entries,
                    ),
                ),
            ),
        )
    }
}
