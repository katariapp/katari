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

internal val ENTRY_MERGE_FEATURE_ID = FeatureId("entry.merge")
internal val ENTRY_MERGE_BASE_INTEGRATION_ID = FeatureIntegrationId("entry.merge.shared-workflow")
internal val ENTRY_MERGE_DOWNLOAD_INTEGRATION_ID = FeatureIntegrationId("entry.merge.download")
internal val ENTRY_MERGE_MIGRATION_INTEGRATION_ID = FeatureIntegrationId("entry.merge.migration")

private val ENTRY_MERGE_FEATURE_OWNER = ContributionOwner("entry-merge")
private val ENTRY_MERGE_REFERENCE = entryContentTypeReferenceContribution(
    id = "merge",
    owner = ENTRY_MERGE_FEATURE_OWNER,
    section = EntryContentTypeReferenceSection.ENTRY_INTERACTIONS,
    label = "Merge versions from different sources",
    order = 500,
)

internal enum class EntryMergeBaseConsequence(
    override val id: FeatureArtifactId,
) : SharedFeatureConsequence {
    WORKFLOW_COORDINATION(FeatureArtifactId("entry.merge.workflow-coordination")),
    CANDIDATES(FeatureArtifactId("entry.merge.candidates")),
    NAVIGATION(FeatureArtifactId("entry.merge.navigation")),
    LIBRARY_GROUPING(FeatureArtifactId("entry.merge.library-grouping")),
    LIBRARY_LIFECYCLE(FeatureArtifactId("entry.merge.library-lifecycle")),
    METADATA_REFRESH(FeatureArtifactId("entry.merge.metadata-refresh")),
    BACKUP(FeatureArtifactId("entry.merge.backup")),
    PROFILE_MOVE(FeatureArtifactId("entry.merge.profile-move")),
    PROFILE_CASCADE_CLEANUP(FeatureArtifactId("entry.merge.profile-cascade-cleanup")),
    CHILD_OWNERSHIP(FeatureArtifactId("entry.merge.child-ownership")),
    PERSISTENCE(FeatureArtifactId("entry.merge.persistence")),
    CONSEQUENCE_DELIVERY(FeatureArtifactId("entry.merge.consequence-delivery")),
    CONSEQUENCE_STATUS(FeatureArtifactId("entry.merge.consequence-status")),
}

internal enum class EntryMergeDownloadConsequence(
    override val id: FeatureArtifactId,
) : SharedFeatureConsequence {
    OWNERSHIP(FeatureArtifactId("entry.merge.download-ownership")),
}

private object EntryMergeMigrationReplacementConsequence : SharedFeatureConsequence {
    override val id = FeatureArtifactId("entry.merge.migration-replacement")
}

internal enum class EntryMergeBehaviorContract(
    override val id: FeatureArtifactId,
) : FeatureBehaviorContract {
    WORKFLOW(FeatureArtifactId("entry.merge.behavior")),
    DOWNLOAD_OWNERSHIP(FeatureArtifactId("entry.merge.download-ownership.behavior")),
    MIGRATION_REPLACEMENT(FeatureArtifactId("entry.merge.migration-replacement.behavior")),
    PREPARATION_SELECTION(FeatureArtifactId("entry.merge.preparation-selection.behavior")),
    PREPARATION_AUTHORITY(FeatureArtifactId("entry.merge.preparation-authority.behavior")),
    PREPARATION_MEMBERSHIP(FeatureArtifactId("entry.merge.preparation-membership.behavior")),
    EXISTING_GROUP(FeatureArtifactId("entry.merge.existing-group.behavior")),
    LIBRARY_INITIALIZATION(FeatureArtifactId("entry.merge.library-initialization.behavior")),
    COVER_CLEANUP(FeatureArtifactId("entry.merge.cover-cleanup.behavior")),
    DOWNLOAD_REMOVAL(FeatureArtifactId("entry.merge.download-removal.behavior")),
}

internal object EntryMergeFeatureContributor : FeatureGraphContributor {
    override val owner = ENTRY_MERGE_FEATURE_OWNER

    override fun contributeTo(sink: FeatureGraphContributionSink) {
        sink.add(
            FeatureContribution(
                feature = ENTRY_MERGE_FEATURE_ID,
                owner = owner,
                integrations = listOf(
                    FeatureIntegration(
                        id = ENTRY_MERGE_BASE_INTEGRATION_ID,
                        prerequisites = CapabilityExpression.Always,
                        sharedConsequences = EntryMergeBaseConsequence.entries,
                        behavioralContracts = listOf(EntryMergeBehaviorContract.WORKFLOW),
                        projectionRequirements = listOf(ENTRY_MERGE_REFERENCE.requirement),
                        projections = listOf(ENTRY_MERGE_REFERENCE.projection),
                    ),
                    FeatureIntegration(
                        id = ENTRY_MERGE_DOWNLOAD_INTEGRATION_ID,
                        prerequisites = CapabilityExpression.Provided(EntryDownloadCapability.definition),
                        sharedConsequences = EntryMergeDownloadConsequence.entries,
                        behavioralContracts = listOf(EntryMergeBehaviorContract.DOWNLOAD_OWNERSHIP),
                    ),
                    FeatureIntegration(
                        id = ENTRY_MERGE_MIGRATION_INTEGRATION_ID,
                        prerequisites = CapabilityExpression.Provided(EntryMigrationCapability.definition),
                        sharedConsequences = listOf(EntryMergeMigrationReplacementConsequence),
                        behavioralContracts = listOf(EntryMergeBehaviorContract.MIGRATION_REPLACEMENT),
                    ),
                ) + entryMergePreparationContextIntegrations(owner) + entryMergeExecutionContextIntegrations(owner),
            ),
        )
    }
}

internal fun FeatureGraphEvaluation.mergeTypes(
    integration: FeatureIntegrationId,
    consequence: FeatureArtifactId,
): Set<EntryType> {
    val contentTypes = sharedConsequences.asSequence()
        .filter { applicability ->
            applicability.subject.feature == ENTRY_MERGE_FEATURE_ID &&
                applicability.subject.integration == integration &&
                applicability.consequence.id == consequence
        }
        .mapTo(mutableSetOf()) { it.subject.contentType }
    return EntryType.entries.filterTo(mutableSetOf()) { it.toContentTypeId() in contentTypes }
}
