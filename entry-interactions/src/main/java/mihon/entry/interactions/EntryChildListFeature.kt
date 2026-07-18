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
import mihon.feature.graph.allOf
import tachiyomi.domain.entry.model.Entry
import tachiyomi.domain.entry.model.EntryChapter

private val ENTRY_CHILD_LIST_FEATURE_ID = FeatureId("entry.child-list")
private val ENTRY_CHILD_LIST_FEATURE_OWNER = ContributionOwner("entry-child-list")
private val ENTRY_CHILD_LIST_INTEGRATION_ID = FeatureIntegrationId("entry.child-list.provider")
private val ENTRY_CHILD_PROGRESS_INTEGRATION_ID = FeatureIntegrationId("entry.child-list.progress")
private val ENTRY_CHILD_LIST_ORDER_CONSEQUENCE_ID = FeatureArtifactId("entry.child-list.order")
private val ENTRY_CHILD_LIST_FIRST_CHILD_CONSEQUENCE_ID = FeatureArtifactId("entry.child-list.first-reading-child")
private val ENTRY_CHILD_LIST_DISPLAY_CONSEQUENCE_ID = FeatureArtifactId("entry.child-list.display")
private val ENTRY_CHILD_PROGRESS_CONSEQUENCE_ID = FeatureArtifactId("entry.child-list.progress-labels")

private object EntryChildListOrderConsequence : SharedFeatureConsequence {
    override val id = ENTRY_CHILD_LIST_ORDER_CONSEQUENCE_ID
}

private object EntryChildListDisplayConsequence : SharedFeatureConsequence {
    override val id = ENTRY_CHILD_LIST_DISPLAY_CONSEQUENCE_ID
}

private object EntryChildListFirstChildConsequence : SharedFeatureConsequence {
    override val id = ENTRY_CHILD_LIST_FIRST_CHILD_CONSEQUENCE_ID
}

private object EntryChildProgressConsequence : SharedFeatureConsequence {
    override val id = ENTRY_CHILD_PROGRESS_CONSEQUENCE_ID
}

internal object EntryChildListFeatureContributor : FeatureGraphContributor {
    override val owner = ENTRY_CHILD_LIST_FEATURE_OWNER

    override fun contributeTo(sink: FeatureGraphContributionSink) {
        sink.add(
            FeatureContribution(
                feature = ENTRY_CHILD_LIST_FEATURE_ID,
                owner = owner,
                integrations = listOf(
                    FeatureIntegration(
                        id = ENTRY_CHILD_LIST_INTEGRATION_ID,
                        prerequisites = CapabilityExpression.Provided(EntryChildListCapability.definition),
                        sharedConsequences = listOf(
                            EntryChildListOrderConsequence,
                            EntryChildListFirstChildConsequence,
                            EntryChildListDisplayConsequence,
                        ),
                    ),
                    FeatureIntegration(
                        id = ENTRY_CHILD_PROGRESS_INTEGRATION_ID,
                        prerequisites = allOf(
                            CapabilityExpression.Provided(EntryChildListCapability.definition),
                            CapabilityExpression.Provided(EntryChildProgressCapability.definition),
                        ),
                        sharedConsequences = listOf(EntryChildProgressConsequence),
                    ),
                ),
            ),
        )
    }
}

internal class DefaultEntryChildListFeature(
    evaluation: FeatureGraphEvaluation,
    private val childList: EntryChildListInteraction,
    private val childProgress: EntryChildProgressInteraction,
) : EntryChildListFeature {
    private val orderTypes = evaluation.applicableProviderTypes<EntryChildListProcessor>(
        feature = ENTRY_CHILD_LIST_FEATURE_ID,
        integration = ENTRY_CHILD_LIST_INTEGRATION_ID,
        consequence = ENTRY_CHILD_LIST_ORDER_CONSEQUENCE_ID,
    )
    private val displayTypes = evaluation.applicableProviderTypes<EntryChildListProcessor>(
        feature = ENTRY_CHILD_LIST_FEATURE_ID,
        integration = ENTRY_CHILD_LIST_INTEGRATION_ID,
        consequence = ENTRY_CHILD_LIST_DISPLAY_CONSEQUENCE_ID,
    )
    private val firstChildTypes = evaluation.applicableProviderTypes<EntryChildListProcessor>(
        feature = ENTRY_CHILD_LIST_FEATURE_ID,
        integration = ENTRY_CHILD_LIST_INTEGRATION_ID,
        consequence = ENTRY_CHILD_LIST_FIRST_CHILD_CONSEQUENCE_ID,
    )
    private val progressTypes = evaluation.applicableProviderTypes<EntryChildProgressProcessor>(
        feature = ENTRY_CHILD_LIST_FEATURE_ID,
        integration = ENTRY_CHILD_PROGRESS_INTEGRATION_ID,
        consequence = ENTRY_CHILD_PROGRESS_CONSEQUENCE_ID,
    )

    init {
        check(setOf(orderTypes, firstChildTypes, displayTypes).size == 1) {
            "Child-list order, first-child, and display consequences selected different provider sets"
        }
    }

    override fun isApplicable(type: EntryType): Boolean = type in orderTypes

    override fun readingOrder(
        entry: Entry,
        chapters: List<EntryChapter>,
        memberIds: List<Long>,
    ): EntryChildOrderResult {
        if (entry.type !in orderTypes) return EntryChildOrderResult.Inapplicable(entry.type)
        return EntryChildOrderResult.Available(childList.sortedForReading(entry, chapters, memberIds))
    }

    override fun displayOrder(
        entry: Entry,
        chapters: List<EntryChapter>,
        memberIds: List<Long>,
    ): EntryChildOrderResult {
        if (entry.type !in orderTypes) return EntryChildOrderResult.Inapplicable(entry.type)
        return EntryChildOrderResult.Available(childList.sortedForDisplay(entry, chapters, memberIds))
    }

    override fun firstReadingChild(
        entry: Entry,
        chapters: List<EntryChapter>,
        memberIds: List<Long>,
    ): EntryFirstChildResult {
        if (entry.type !in firstChildTypes) return EntryFirstChildResult.Inapplicable(entry.type)
        return EntryFirstChildResult.Available(
            childList.sortedForReading(entry, chapters, memberIds).firstOrNull(),
        )
    }

    override fun displayList(request: EntryChildListRequest): EntryChildListResult {
        if (request.entry.type !in displayTypes) return EntryChildListResult.Inapplicable(request.entry.type)
        return EntryChildListResult.Available(childList.buildDisplayList(request))
    }

    override fun progressLabels(request: EntryChildProgressRequest): EntryChildProgressResult {
        if (request.entry.type !in progressTypes) return EntryChildProgressResult.Inapplicable(request.entry.type)
        return EntryChildProgressResult.Available(childProgress.progressLabels(request))
    }
}
