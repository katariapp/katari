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
import tachiyomi.domain.entry.model.EntryChapter

private val ENTRY_BOOKMARK_FEATURE_ID = FeatureId("entry.bookmarking")
private val ENTRY_BOOKMARK_FEATURE_OWNER = ContributionOwner("entry-bookmarking")
private val ENTRY_BOOKMARK_PROVIDER_INTEGRATION_ID = FeatureIntegrationId("entry.bookmarking.provider")
private val ENTRY_BOOKMARK_APPLICABILITY_CONSEQUENCE_ID = FeatureArtifactId("entry.bookmarking.applicability")
private val ENTRY_BOOKMARK_ELIGIBILITY_CONSEQUENCE_ID = FeatureArtifactId("entry.bookmarking.eligibility")
private val ENTRY_BOOKMARK_MUTATION_CONSEQUENCE_ID = FeatureArtifactId("entry.bookmarking.mutation")

private object EntryBookmarkApplicabilityConsequence : SharedFeatureConsequence {
    override val id = ENTRY_BOOKMARK_APPLICABILITY_CONSEQUENCE_ID
}

private object EntryBookmarkEligibilityConsequence : SharedFeatureConsequence {
    override val id = ENTRY_BOOKMARK_ELIGIBILITY_CONSEQUENCE_ID
}

private object EntryBookmarkMutationConsequence : SharedFeatureConsequence {
    override val id = ENTRY_BOOKMARK_MUTATION_CONSEQUENCE_ID
}

internal object EntryBookmarkFeatureContributor : FeatureGraphContributor {
    override val owner = ENTRY_BOOKMARK_FEATURE_OWNER

    override fun contributeTo(sink: FeatureGraphContributionSink) {
        sink.add(
            FeatureContribution(
                feature = ENTRY_BOOKMARK_FEATURE_ID,
                owner = owner,
                integrations = listOf(
                    FeatureIntegration(
                        id = ENTRY_BOOKMARK_PROVIDER_INTEGRATION_ID,
                        prerequisites = CapabilityExpression.Provided(EntryBookmarkCapability.definition),
                        sharedConsequences = listOf(
                            EntryBookmarkApplicabilityConsequence,
                            EntryBookmarkEligibilityConsequence,
                            EntryBookmarkMutationConsequence,
                        ),
                    ),
                ),
            ),
        )
    }
}

internal class DefaultEntryBookmarkFeature(
    evaluation: FeatureGraphEvaluation,
    private val interaction: EntryBookmarkInteraction,
) : EntryBookmarkFeature {
    private val applicableTypes = evaluation.applicableProviderTypes<EntryBookmarkProcessor>(
        feature = ENTRY_BOOKMARK_FEATURE_ID,
        integration = ENTRY_BOOKMARK_PROVIDER_INTEGRATION_ID,
        consequence = ENTRY_BOOKMARK_APPLICABILITY_CONSEQUENCE_ID,
    )
    private val eligibilityTypes = evaluation.applicableProviderTypes<EntryBookmarkProcessor>(
        feature = ENTRY_BOOKMARK_FEATURE_ID,
        integration = ENTRY_BOOKMARK_PROVIDER_INTEGRATION_ID,
        consequence = ENTRY_BOOKMARK_ELIGIBILITY_CONSEQUENCE_ID,
    )
    private val mutationTypes = evaluation.applicableProviderTypes<EntryBookmarkProcessor>(
        feature = ENTRY_BOOKMARK_FEATURE_ID,
        integration = ENTRY_BOOKMARK_PROVIDER_INTEGRATION_ID,
        consequence = ENTRY_BOOKMARK_MUTATION_CONSEQUENCE_ID,
    )

    init {
        check(setOf(applicableTypes, eligibilityTypes, mutationTypes).size == 1) {
            "Bookmark consequences selected different provider sets"
        }
    }

    override fun isApplicable(type: EntryType): Boolean = type in applicableTypes

    override fun availability(
        target: EntryBookmarkTarget,
        bookmarked: Boolean,
    ): EntryBookmarkAvailability {
        return selectionAvailability(listOf(target), bookmarked)
    }

    override fun selectionAvailability(
        targets: List<EntryBookmarkTarget>,
        bookmarked: Boolean,
    ): EntryBookmarkAvailability {
        val unsupportedTypes = targets.mapTo(mutableSetOf(), EntryBookmarkTarget::type) - eligibilityTypes
        if (unsupportedTypes.isNotEmpty()) return EntryBookmarkAvailability.Inapplicable(unsupportedTypes)
        if (targets.isEmpty()) return EntryBookmarkAvailability.NoChange

        val canChange = when (bookmarked) {
            true -> targets.any { !it.status.bookmarked }
            false -> targets.all { it.status.bookmarked }
        }
        return if (canChange) EntryBookmarkAvailability.Available else EntryBookmarkAvailability.NoChange
    }

    override suspend fun setBookmarked(
        entry: Entry,
        chapters: List<EntryChapter>,
        bookmarked: Boolean,
    ): EntryBookmarkMutationResult {
        if (entry.type !in mutationTypes) return EntryBookmarkMutationResult.Inapplicable(entry.type)
        val changedChapters = chapters.filter { it.bookmark != bookmarked }
        if (changedChapters.isEmpty()) return EntryBookmarkMutationResult.NoChange

        interaction.setBookmarked(entry, changedChapters, bookmarked)
        return EntryBookmarkMutationResult.Applied(changedChapters.size)
    }
}
