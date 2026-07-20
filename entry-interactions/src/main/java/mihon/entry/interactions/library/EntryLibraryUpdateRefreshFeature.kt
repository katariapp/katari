package mihon.entry.interactions

import mihon.feature.graph.CapabilityExpression
import mihon.feature.graph.ContentTypeId
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

private val FEATURE_ID = FeatureId("entry.library-update-refresh")
private val FEATURE_OWNER = ContributionOwner("entry-library-update")
private val REFRESH_INTEGRATION = FeatureIntegrationId("entry.library-update-refresh.source-refresh")

private enum class EntryLibraryUpdateRefreshConsequence(
    override val id: FeatureArtifactId,
) : SharedFeatureConsequence {
    SOURCE_REFRESH(FeatureArtifactId("entry.library-update-refresh.source-refresh")),
    NEW_CHILD_HANDOFF(FeatureArtifactId("entry.library-update-refresh.new-child-handoff")),
}

private object EntryLibraryUpdateRefreshBehaviorContract : FeatureBehaviorContract {
    override val id = FeatureArtifactId("entry.library-update-refresh.behavior")
}

internal object EntryLibraryUpdateRefreshFeatureContributor : FeatureGraphContributor {
    override val owner = FEATURE_OWNER

    override fun contributeTo(sink: FeatureGraphContributionSink) {
        sink.add(
            FeatureContribution(
                feature = FEATURE_ID,
                owner = owner,
                integrations = listOf(
                    FeatureIntegration(
                        id = REFRESH_INTEGRATION,
                        prerequisites = CapabilityExpression.Always,
                        sharedConsequences = EntryLibraryUpdateRefreshConsequence.entries,
                        behavioralContracts = listOf(EntryLibraryUpdateRefreshBehaviorContract),
                    ),
                ),
            ),
        )
    }
}

internal class DefaultEntryLibraryUpdateRefreshFeature(
    evaluation: FeatureGraphEvaluation,
    private val sourceRefresh: EntrySourceRefreshFeature,
) : EntryLibraryUpdateRefreshFeature {
    private val selectedTypes = evaluation.libraryUpdateRefreshContentTypes()

    override suspend fun refresh(request: EntryLibraryUpdateRefreshRequest): EntryLibraryUpdateRefreshResult {
        check(request.entry.type.toContentTypeId() in selectedTypes) {
            "Entry type ${request.entry.type} was not contributed to Library Update Refresh"
        }
        return when (
            val result = sourceRefresh.refresh(
                EntrySourceRefreshRequest(
                    entry = request.entry,
                    fetchDetails = request.fetchMetadata,
                    fetchChildren = true,
                    fetchWindow = EntrySourceRefreshWindow(
                        lowerBound = request.fetchWindowLowerBound,
                        upperBound = request.fetchWindowUpperBound,
                    ),
                ),
            )
        ) {
            is EntrySourceRefreshResult.Refreshed -> EntryLibraryUpdateRefreshResult.Updated(
                result.insertedChildren.sortedByDescending { it.sourceOrder },
            )
            is EntrySourceRefreshResult.SourceUnavailable -> EntryLibraryUpdateRefreshResult.SourceUnavailable
            is EntrySourceRefreshResult.Failed -> when (val reason = result.reason) {
                EntrySourceRefreshFailure.NoChildren -> EntryLibraryUpdateRefreshResult.NoChildren
                is EntrySourceRefreshFailure.Operation -> {
                    EntryLibraryUpdateRefreshResult.OperationalFailure(reason.error)
                }
            }
        }
    }
}

private fun FeatureGraphEvaluation.libraryUpdateRefreshContentTypes(): Set<ContentTypeId> =
    sharedConsequences
        .asSequence()
        .filter { applicability ->
            applicability.subject.feature == FEATURE_ID &&
                applicability.subject.integration == REFRESH_INTEGRATION &&
                applicability.consequence.id == EntryLibraryUpdateRefreshConsequence.SOURCE_REFRESH.id
        }
        .mapTo(mutableSetOf()) { it.subject.contentType }
