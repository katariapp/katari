package mihon.entry.interactions

import kotlinx.coroutines.CancellationException
import mihon.feature.graph.CapabilityExpression
import mihon.feature.graph.ContextInputId
import mihon.feature.graph.ContributionOwner
import mihon.feature.graph.FeatureArtifactId
import mihon.feature.graph.FeatureBehaviorContract
import mihon.feature.graph.FeatureContextBlocker
import mihon.feature.graph.FeatureContextDecision
import mihon.feature.graph.FeatureContribution
import mihon.feature.graph.FeatureGraphContributionSink
import mihon.feature.graph.FeatureGraphContributor
import mihon.feature.graph.FeatureGraphEvaluation
import mihon.feature.graph.FeatureId
import mihon.feature.graph.FeatureIntegration
import mihon.feature.graph.FeatureIntegrationId
import mihon.feature.graph.SharedFeatureConsequence
import mihon.feature.graph.contextEvidence
import mihon.feature.graph.contextInputDefinition
import mihon.feature.graph.featureContextRule
import tachiyomi.domain.chapter.model.NoChaptersException
import tachiyomi.domain.entry.interactor.SyncEntryWithSource
import tachiyomi.domain.source.service.SourceManager

private val ENTRY_SOURCE_REFRESH_FEATURE_ID = FeatureId("entry.source-refresh")
private val ENTRY_SOURCE_REFRESH_OWNER = ContributionOwner("entry-source-refresh")
private val ENTRY_SOURCE_REFRESH_INTEGRATION_ID = FeatureIntegrationId("entry.source-refresh.execution")

private val ENTRY_SOURCE_REFRESH_SOURCE_CONTEXT = contextInputDefinition<Boolean>(
    ContextInputId("entry.source-refresh.source-state"),
    ContributionOwner("entry-source"),
)
private val ENTRY_SOURCE_REFRESH_SOURCE_MISSING = FeatureContextBlocker(
    FeatureArtifactId("entry.source-refresh.source-missing"),
    listOf(ENTRY_SOURCE_REFRESH_SOURCE_CONTEXT),
)
private enum class EntrySourceRefreshConsequence(
    override val id: FeatureArtifactId,
) : SharedFeatureConsequence {
    SOURCE_ACCESS(FeatureArtifactId("entry.source-refresh.source-access")),
    DETAILS(FeatureArtifactId("entry.source-refresh.details")),
    CHILDREN(FeatureArtifactId("entry.source-refresh.children")),
    PERSISTENCE(FeatureArtifactId("entry.source-refresh.persistence")),
    RESULT(FeatureArtifactId("entry.source-refresh.result")),
}

private object EntrySourceRefreshBehaviorContract : FeatureBehaviorContract {
    override val id = FeatureArtifactId("entry.source-refresh.behavior")
}

internal object EntrySourceRefreshFeatureContributor : FeatureGraphContributor {
    override val owner = ENTRY_SOURCE_REFRESH_OWNER

    override fun contributeTo(sink: FeatureGraphContributionSink) {
        sink.add(
            FeatureContribution(
                feature = ENTRY_SOURCE_REFRESH_FEATURE_ID,
                owner = owner,
                integrations = listOf(
                    FeatureIntegration(
                        id = ENTRY_SOURCE_REFRESH_INTEGRATION_ID,
                        prerequisites = CapabilityExpression.Always,
                        contextInputs = listOf(ENTRY_SOURCE_REFRESH_SOURCE_CONTEXT),
                        contextRule = featureContextRule(owner) { evidence ->
                            if (evidence.value(ENTRY_SOURCE_REFRESH_SOURCE_CONTEXT)) {
                                FeatureContextDecision.Applicable
                            } else {
                                FeatureContextDecision.Blocked(listOf(ENTRY_SOURCE_REFRESH_SOURCE_MISSING))
                            }
                        },
                        contextBlockers = listOf(ENTRY_SOURCE_REFRESH_SOURCE_MISSING),
                        sharedConsequences = EntrySourceRefreshConsequence.entries,
                        behavioralContracts = listOf(EntrySourceRefreshBehaviorContract),
                    ),
                ),
            ),
        )
    }
}

internal class DefaultEntrySourceRefreshFeature(
    private val evaluation: FeatureGraphEvaluation,
    private val sourceManager: SourceManager,
    private val syncEntryWithSource: SyncEntryWithSource,
    private val updateLibraryTitles: (profileId: Long) -> Boolean,
) : EntrySourceRefreshFeature {

    override suspend fun refresh(request: EntrySourceRefreshRequest): EntrySourceRefreshResult {
        require(request.fetchDetails || request.fetchChildren) {
            "Source refresh must request details, children, or both"
        }

        val sourceInstalled = sourceManager.get(request.entry.source) != null
        requireState(request, sourceInstalled)
        if (!sourceInstalled) return EntrySourceRefreshResult.SourceUnavailable(request.entry.source)

        return try {
            syncEntryWithSource.syncStrictly(
                entry = request.entry,
                profileId = request.entry.profileId,
                updateLibraryTitles = updateLibraryTitles(request.entry.profileId),
                fetchDetails = request.fetchDetails,
                fetchChapters = request.fetchChildren,
                manualFetch = request.manual,
                fetchWindow = request.fetchWindow.let { it.lowerBound to it.upperBound },
            ).toRefreshResult()
        } catch (error: CancellationException) {
            throw error
        } catch (_: NoChaptersException) {
            EntrySourceRefreshResult.Failed(EntrySourceRefreshFailure.NoChildren)
        } catch (error: Throwable) {
            EntrySourceRefreshResult.Failed(EntrySourceRefreshFailure.Operation(error))
        }
    }

    private fun requireState(request: EntrySourceRefreshRequest, sourceInstalled: Boolean) {
        EntrySourceRefreshConsequence.entries.forEach { consequence ->
            evaluation.requireSourceContextState(
                feature = ENTRY_SOURCE_REFRESH_FEATURE_ID,
                integration = ENTRY_SOURCE_REFRESH_INTEGRATION_ID,
                consequence = consequence.id,
                evidence = listOf(contextEvidence(ENTRY_SOURCE_REFRESH_SOURCE_CONTEXT, sourceInstalled)),
                applicable = sourceInstalled,
                contentType = request.entry.type,
            )
        }
    }
}

private fun SyncEntryWithSource.SyncResult.toRefreshResult(): EntrySourceRefreshResult.Refreshed {
    return EntrySourceRefreshResult.Refreshed(
        insertedChildren = insertedChapters,
        insertedChildrenTotal = insertedChaptersTotal,
        updatedChildren = updatedChapters,
        removedChildren = removedChapters,
        metadataChanged = hasMetadataChanges,
    )
}
