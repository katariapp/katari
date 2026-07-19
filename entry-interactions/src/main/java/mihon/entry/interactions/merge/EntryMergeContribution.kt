package mihon.entry.interactions

import eu.kanade.tachiyomi.source.entry.EntryType
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

private val ENTRY_MERGE_FEATURE_OWNER = ContributionOwner("entry-merge")

internal enum class EntryMergeBaseConsequence(
    override val id: FeatureArtifactId,
) : SharedFeatureConsequence {
    WORKFLOW(FeatureArtifactId("entry.merge.workflow")),
    EDITOR(FeatureArtifactId("entry.merge.editor")),
    CANDIDATES(FeatureArtifactId("entry.merge.candidates")),
    NAVIGATION(FeatureArtifactId("entry.merge.navigation")),
    LIBRARY_GROUPING(FeatureArtifactId("entry.merge.library-grouping")),
    BACKUP(FeatureArtifactId("entry.merge.backup")),
    CHILD_OWNERSHIP(FeatureArtifactId("entry.merge.child-ownership")),
    PERSISTENCE(FeatureArtifactId("entry.merge.persistence")),
    CONSEQUENCE_DELIVERY(FeatureArtifactId("entry.merge.consequence-delivery")),
    LIBRARY_INITIALIZATION(FeatureArtifactId("entry.merge.library-initialization")),
    COVER_CLEANUP(FeatureArtifactId("entry.merge.cover-cleanup")),
}

internal enum class EntryMergeDownloadConsequence(
    override val id: FeatureArtifactId,
) : SharedFeatureConsequence {
    OWNERSHIP(FeatureArtifactId("entry.merge.download-ownership")),
    REMOVAL(FeatureArtifactId("entry.merge.download-removal")),
}

private object EntryMergeBehaviorContract : FeatureBehaviorContract {
    override val id = FeatureArtifactId("entry.merge.behavior")
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
                        behavioralContracts = listOf(EntryMergeBehaviorContract),
                    ),
                    FeatureIntegration(
                        id = ENTRY_MERGE_DOWNLOAD_INTEGRATION_ID,
                        prerequisites = CapabilityExpression.Provided(EntryDownloadCapability.definition),
                        sharedConsequences = EntryMergeDownloadConsequence.entries,
                    ),
                ),
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
