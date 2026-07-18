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
import mihon.feature.graph.allOf
import tachiyomi.domain.entry.model.Entry
import tachiyomi.domain.entry.model.EntryChapter
import tachiyomi.domain.entry.service.EntryLibraryContinueTarget
import tachiyomi.domain.entry.service.EntryLibraryProgressResolution
import tachiyomi.domain.entry.service.EntryLibraryProgressSummary

private val ENTRY_LIBRARY_PROGRESS_FEATURE_ID = FeatureId("entry.library-progress")
private val ENTRY_LIBRARY_PROGRESS_FEATURE_OWNER = ContributionOwner("entry-library-progress")
private val ENTRY_LIBRARY_PROGRESS_PROVIDER_INTEGRATION_ID =
    FeatureIntegrationId("entry.library-progress.provider")
private val ENTRY_LIBRARY_PROGRESS_CONTINUE_INTEGRATION_ID =
    FeatureIntegrationId("entry.library-progress.continue")
private val ENTRY_LIBRARY_PROGRESS_BOOKMARK_INTEGRATION_ID =
    FeatureIntegrationId("entry.library-progress.bookmark")

private val ENTRY_LIBRARY_PROGRESS_LOAD_CONSEQUENCE_ID = FeatureArtifactId("entry.library-progress.load")
private val ENTRY_LIBRARY_PROGRESS_MERGE_CONSEQUENCE_ID = FeatureArtifactId("entry.library-progress.merge")
private val ENTRY_LIBRARY_PROGRESS_BADGE_CONSEQUENCE_ID = FeatureArtifactId("entry.library-progress.badges")
private val ENTRY_LIBRARY_PROGRESS_SORT_CONSEQUENCE_ID = FeatureArtifactId("entry.library-progress.sort-inputs")
private val ENTRY_LIBRARY_PROGRESS_FILTER_CONSEQUENCE_ID = FeatureArtifactId("entry.library-progress.filter-inputs")
private val ENTRY_LIBRARY_PROGRESS_STATS_CONSEQUENCE_ID = FeatureArtifactId("entry.library-progress.stats-inputs")
private val ENTRY_LIBRARY_PROGRESS_UPDATE_CONSEQUENCE_ID = FeatureArtifactId("entry.library-progress.update-inputs")
private val ENTRY_LIBRARY_PROGRESS_CONTINUE_CONSEQUENCE_ID = FeatureArtifactId("entry.library-progress.continue-target")
private val ENTRY_LIBRARY_PROGRESS_BOOKMARK_CONSEQUENCE_ID = FeatureArtifactId("entry.library-progress.bookmarks")
private val ENTRY_LIBRARY_PROGRESS_BEHAVIOR_CONTRACT_ID = FeatureArtifactId("entry.library-progress.behavior")

private data class LibraryProgressConsequence(override val id: FeatureArtifactId) : SharedFeatureConsequence
private object EntryLibraryProgressBehaviorContract : FeatureBehaviorContract {
    override val id = ENTRY_LIBRARY_PROGRESS_BEHAVIOR_CONTRACT_ID
}

internal object EntryLibraryProgressFeatureContributor : FeatureGraphContributor {
    override val owner = ENTRY_LIBRARY_PROGRESS_FEATURE_OWNER

    override fun contributeTo(sink: FeatureGraphContributionSink) {
        sink.add(
            FeatureContribution(
                feature = ENTRY_LIBRARY_PROGRESS_FEATURE_ID,
                owner = owner,
                integrations = listOf(
                    FeatureIntegration(
                        id = ENTRY_LIBRARY_PROGRESS_PROVIDER_INTEGRATION_ID,
                        prerequisites = CapabilityExpression.Provided(EntryLibraryProgressCapability.definition),
                        sharedConsequences = listOf(
                            ENTRY_LIBRARY_PROGRESS_LOAD_CONSEQUENCE_ID,
                            ENTRY_LIBRARY_PROGRESS_MERGE_CONSEQUENCE_ID,
                            ENTRY_LIBRARY_PROGRESS_BADGE_CONSEQUENCE_ID,
                            ENTRY_LIBRARY_PROGRESS_SORT_CONSEQUENCE_ID,
                            ENTRY_LIBRARY_PROGRESS_FILTER_CONSEQUENCE_ID,
                            ENTRY_LIBRARY_PROGRESS_STATS_CONSEQUENCE_ID,
                            ENTRY_LIBRARY_PROGRESS_UPDATE_CONSEQUENCE_ID,
                        ).map(::LibraryProgressConsequence),
                        behavioralContracts = listOf(EntryLibraryProgressBehaviorContract),
                    ),
                    FeatureIntegration(
                        id = ENTRY_LIBRARY_PROGRESS_CONTINUE_INTEGRATION_ID,
                        prerequisites = allOf(
                            CapabilityExpression.Provided(EntryLibraryProgressCapability.definition),
                            CapabilityExpression.Provided(EntryContinueCapability.definition),
                        ),
                        sharedConsequences = listOf(
                            LibraryProgressConsequence(ENTRY_LIBRARY_PROGRESS_CONTINUE_CONSEQUENCE_ID),
                        ),
                    ),
                    FeatureIntegration(
                        id = ENTRY_LIBRARY_PROGRESS_BOOKMARK_INTEGRATION_ID,
                        prerequisites = allOf(
                            CapabilityExpression.Provided(EntryLibraryProgressCapability.definition),
                            CapabilityExpression.Provided(EntryBookmarkCapability.definition),
                        ),
                        sharedConsequences = listOf(
                            LibraryProgressConsequence(ENTRY_LIBRARY_PROGRESS_BOOKMARK_CONSEQUENCE_ID),
                        ),
                    ),
                ),
            ),
        )
    }
}

internal class DefaultEntryLibraryProgressFeature(
    evaluation: FeatureGraphEvaluation,
    private val interaction: EntryLibraryProgressInteraction,
    private val continueFeature: EntryContinueFeature,
) : EntryLibraryProgressFeature {
    private val baseTypesByConsequence = listOf(
        ENTRY_LIBRARY_PROGRESS_LOAD_CONSEQUENCE_ID,
        ENTRY_LIBRARY_PROGRESS_MERGE_CONSEQUENCE_ID,
        ENTRY_LIBRARY_PROGRESS_BADGE_CONSEQUENCE_ID,
        ENTRY_LIBRARY_PROGRESS_SORT_CONSEQUENCE_ID,
        ENTRY_LIBRARY_PROGRESS_FILTER_CONSEQUENCE_ID,
        ENTRY_LIBRARY_PROGRESS_STATS_CONSEQUENCE_ID,
        ENTRY_LIBRARY_PROGRESS_UPDATE_CONSEQUENCE_ID,
    ).associateWith { consequence ->
        evaluation.applicableProviderTypes<EntryLibraryProgressProvider>(
            feature = ENTRY_LIBRARY_PROGRESS_FEATURE_ID,
            integration = ENTRY_LIBRARY_PROGRESS_PROVIDER_INTEGRATION_ID,
            consequence = consequence,
        )
    }
    private val applicableTypes = baseTypesByConsequence.getValue(ENTRY_LIBRARY_PROGRESS_LOAD_CONSEQUENCE_ID)
    private val continueTypes = evaluation.applicableProviderTypes<EntryLibraryProgressProvider>(
        feature = ENTRY_LIBRARY_PROGRESS_FEATURE_ID,
        integration = ENTRY_LIBRARY_PROGRESS_CONTINUE_INTEGRATION_ID,
        consequence = ENTRY_LIBRARY_PROGRESS_CONTINUE_CONSEQUENCE_ID,
    )
    private val bookmarkTypes = evaluation.applicableProviderTypes<EntryLibraryProgressProvider>(
        feature = ENTRY_LIBRARY_PROGRESS_FEATURE_ID,
        integration = ENTRY_LIBRARY_PROGRESS_BOOKMARK_INTEGRATION_ID,
        consequence = ENTRY_LIBRARY_PROGRESS_BOOKMARK_CONSEQUENCE_ID,
    )

    init {
        check(baseTypesByConsequence.values.toSet().size == 1) {
            "Library progress consequences selected different provider sets"
        }
    }

    override fun isApplicable(type: EntryType): Boolean = type in applicableTypes

    override suspend fun calculate(
        entry: Entry,
        chapters: List<EntryChapter>,
        lastRead: Long,
    ): EntryLibraryProgressResolution {
        if (!isApplicable(entry.type)) return EntryLibraryProgressResolution.Inapplicable(entry.type)
        val evidence = interaction.evidence(entry, chapters)
        val continueTarget = if (entry.type in continueTypes) {
            when (val target = continueFeature.nextTarget(entry)) {
                is EntryContinueTargetResult.Available -> EntryLibraryContinueTarget.Available(target.chapter.id)
                EntryContinueTargetResult.NoNext -> EntryLibraryContinueTarget.NoNext
                EntryContinueTargetResult.Inapplicable -> error(
                    "Library progress selected Continue for ${entry.type}, but Continue returned inapplicable",
                )
            }
        } else {
            EntryLibraryContinueTarget.Inapplicable
        }
        return EntryLibraryProgressResolution.Available(
            EntryLibraryProgressSummary(
                totalCount = chapters.size.toLong(),
                consumedCount = chapters.count(EntryChapter::read).toLong(),
                hasStarted = chapters.any(EntryChapter::read) || evidence.hasMediaProgress,
                bookmarkCount = chapters.count(EntryChapter::bookmark).toLong().takeIf { entry.type in bookmarkTypes },
                inProgressItemId = evidence.inProgressItemId,
                inProgressFraction = evidence.inProgressFraction,
                lastRead = maxOf(lastRead, evidence.lastActivityAt),
                continueTarget = continueTarget,
            ),
        )
    }

    override fun merge(
        entryType: EntryType,
        members: List<EntryLibraryProgressSummary>,
    ): EntryLibraryProgressResolution {
        if (!isApplicable(entryType)) return EntryLibraryProgressResolution.Inapplicable(entryType)
        require(members.isNotEmpty()) { "Cannot merge an empty Library progress group" }

        val inProgress = members.firstOrNull { it.inProgressItemId != null }
        val continueTarget = when {
            entryType !in continueTypes -> EntryLibraryContinueTarget.Inapplicable
            else -> members.asSequence()
                .map(EntryLibraryProgressSummary::continueTarget)
                .filterIsInstance<EntryLibraryContinueTarget.Available>()
                .firstOrNull()
                ?: EntryLibraryContinueTarget.NoNext
        }
        val bookmarkCount = if (entryType in bookmarkTypes) {
            members.sumOf { member ->
                checkNotNull(member.bookmarkCount) {
                    "Library progress Bookmark consequence selected $entryType without bookmark summary evidence"
                }
            }
        } else {
            null
        }

        return EntryLibraryProgressResolution.Available(
            EntryLibraryProgressSummary(
                totalCount = members.sumOf(EntryLibraryProgressSummary::totalCount),
                consumedCount = members.sumOf(EntryLibraryProgressSummary::consumedCount),
                hasStarted = members.any(EntryLibraryProgressSummary::hasStarted),
                bookmarkCount = bookmarkCount,
                inProgressItemId = inProgress?.inProgressItemId,
                inProgressFraction = inProgress?.inProgressFraction,
                lastRead = members.maxOf(EntryLibraryProgressSummary::lastRead),
                continueTarget = continueTarget,
            ),
        )
    }
}
