package mihon.entry.interactions

import eu.kanade.tachiyomi.source.entry.EntryType
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
import tachiyomi.core.common.preference.TriState

private val ENTRY_LIBRARY_FILTER_FEATURE_ID = FeatureId("entry.library-filtering")
private val ENTRY_LIBRARY_FILTER_FEATURE_OWNER = ContributionOwner("entry-library-filtering")
private val ENTRY_LIBRARY_FILTER_POLICY_INTEGRATION_ID =
    FeatureIntegrationId("entry.library-filtering.shared-policy")
private val ENTRY_LIBRARY_FILTER_BOOKMARK_INTEGRATION_ID =
    FeatureIntegrationId("entry.library-filtering.bookmark-control")
private val ENTRY_LIBRARY_FILTER_RELEASE_PERIOD_INTEGRATION_ID =
    FeatureIntegrationId("entry.library-filtering.outside-release-period")

private val ENTRY_LIBRARY_FILTER_POLICY_CONSEQUENCE_ID =
    FeatureArtifactId("entry.library-filtering.policy")
private val ENTRY_LIBRARY_FILTER_ACTIVE_STATE_CONSEQUENCE_ID =
    FeatureArtifactId("entry.library-filtering.active-state")
private val ENTRY_LIBRARY_FILTER_BOOKMARK_CONTROL_CONSEQUENCE_ID =
    FeatureArtifactId("entry.library-filtering.bookmark-control")
private val ENTRY_LIBRARY_FILTER_RELEASE_PERIOD_CONSEQUENCE_ID =
    FeatureArtifactId("entry.library-filtering.outside-release-period")
private val ENTRY_LIBRARY_FILTER_BEHAVIOR_CONTRACT_ID =
    FeatureArtifactId("entry.library-filtering.behavior")

private object EntryLibraryFilterPolicyConsequence : SharedFeatureConsequence {
    override val id = ENTRY_LIBRARY_FILTER_POLICY_CONSEQUENCE_ID
}

private object EntryLibraryFilterActiveStateConsequence : SharedFeatureConsequence {
    override val id = ENTRY_LIBRARY_FILTER_ACTIVE_STATE_CONSEQUENCE_ID
}

private object EntryLibraryFilterBookmarkControlConsequence : SharedFeatureConsequence {
    override val id = ENTRY_LIBRARY_FILTER_BOOKMARK_CONTROL_CONSEQUENCE_ID
}

private object EntryLibraryFilterReleasePeriodConsequence : SharedFeatureConsequence {
    override val id = ENTRY_LIBRARY_FILTER_RELEASE_PERIOD_CONSEQUENCE_ID
}

private object EntryLibraryFilterBehaviorContract : FeatureBehaviorContract {
    override val id = ENTRY_LIBRARY_FILTER_BEHAVIOR_CONTRACT_ID
}

internal object EntryLibraryFilterFeatureContributor : FeatureGraphContributor {
    override val owner = ENTRY_LIBRARY_FILTER_FEATURE_OWNER

    override fun contributeTo(sink: FeatureGraphContributionSink) {
        sink.add(
            FeatureContribution(
                feature = ENTRY_LIBRARY_FILTER_FEATURE_ID,
                owner = owner,
                integrations = listOf(
                    FeatureIntegration(
                        id = ENTRY_LIBRARY_FILTER_POLICY_INTEGRATION_ID,
                        prerequisites = CapabilityExpression.Always,
                        sharedConsequences = listOf(
                            EntryLibraryFilterPolicyConsequence,
                            EntryLibraryFilterActiveStateConsequence,
                        ),
                        behavioralContracts = listOf(EntryLibraryFilterBehaviorContract),
                    ),
                    FeatureIntegration(
                        id = ENTRY_LIBRARY_FILTER_BOOKMARK_INTEGRATION_ID,
                        prerequisites = CapabilityExpression.Provided(EntryBookmarkCapability.definition),
                        sharedConsequences = listOf(EntryLibraryFilterBookmarkControlConsequence),
                    ),
                    FeatureIntegration(
                        id = ENTRY_LIBRARY_FILTER_RELEASE_PERIOD_INTEGRATION_ID,
                        prerequisites = CapabilityExpression.Provided(
                            EntryOutsideReleasePeriodFilterCapability.definition,
                        ),
                        sharedConsequences = listOf(EntryLibraryFilterReleasePeriodConsequence),
                    ),
                ),
            ),
        )
    }
}

internal class DefaultEntryLibraryFilterFeature(
    evaluation: FeatureGraphEvaluation,
) : EntryLibraryFilterFeature {
    private val policyContentTypes = evaluation.selectedContentTypes(
        integration = ENTRY_LIBRARY_FILTER_POLICY_INTEGRATION_ID,
        consequence = ENTRY_LIBRARY_FILTER_POLICY_CONSEQUENCE_ID,
    )
    private val activeStateContentTypes = evaluation.selectedContentTypes(
        integration = ENTRY_LIBRARY_FILTER_POLICY_INTEGRATION_ID,
        consequence = ENTRY_LIBRARY_FILTER_ACTIVE_STATE_CONSEQUENCE_ID,
    )
    private val bookmarkTypes = evaluation.applicableProviderTypes<EntryBookmarkProcessor>(
        feature = ENTRY_LIBRARY_FILTER_FEATURE_ID,
        integration = ENTRY_LIBRARY_FILTER_BOOKMARK_INTEGRATION_ID,
        consequence = ENTRY_LIBRARY_FILTER_BOOKMARK_CONTROL_CONSEQUENCE_ID,
    )
    private val releasePeriodTypes = evaluation.applicableProviderTypes<EntryOutsideReleasePeriodFilterProvider>(
        feature = ENTRY_LIBRARY_FILTER_FEATURE_ID,
        integration = ENTRY_LIBRARY_FILTER_RELEASE_PERIOD_INTEGRATION_ID,
        consequence = ENTRY_LIBRARY_FILTER_RELEASE_PERIOD_CONSEQUENCE_ID,
    )

    init {
        check(policyContentTypes == activeStateContentTypes) {
            "Library filter policy and active-state consequences selected different content types"
        }
    }

    override fun filter(request: EntryLibraryFilterRequest): EntryLibraryFilterResult {
        val currentTypes = request.targets.mapTo(mutableSetOf(), EntryLibraryFilterTarget::type)
        val uncomposedTypes = currentTypes.filterTo(mutableSetOf()) {
            it.toContentTypeId() !in policyContentTypes
        }
        check(uncomposedTypes.isEmpty()) {
            "Entry types $uncomposedTypes were not contributed to the Library filtering feature graph"
        }

        val availability = EntryLibraryFilterAvailability(
            bookmarking = currentTypes.controlAvailability(bookmarkTypes),
            outsideReleasePeriod = currentTypes.controlAvailability(releasePeriodTypes),
        )
        val policy = request.policy
        val effectiveDownloaded = if (policy.downloadedOnly) TriState.ENABLED_IS else policy.downloaded
        val activeTracking = policy.tracking.filterValues { it != TriState.DISABLED }
        val bookmarkFilter = policy.bookmarked.takeIf { availability.bookmarking.isAvailable } ?: TriState.DISABLED
        val releasePeriodFilter = policy.outsideReleasePeriod.takeIf {
            policy.outsideReleasePeriodEnabled && availability.outsideReleasePeriod.isAvailable
        } ?: TriState.DISABLED

        val included = request.targets.mapIndexedNotNull { index, target ->
            index.takeIf {
                effectiveDownloaded.matches(target.isDownloadedOrLocal) &&
                    policy.unconsumed.matches(target.hasUnconsumed) &&
                    policy.notStarted.matches(!target.hasStarted) &&
                    bookmarkFilter.matches(target.hasBookmarks) &&
                    policy.completed.matches(target.isCompleted) &&
                    releasePeriodFilter.matchesReleasePeriod(target, releasePeriodTypes) &&
                    activeTracking.matches(target.trackerIds)
            }
        }

        return EntryLibraryFilterResult(
            includedTargetIndices = included,
            hasActiveFilters = listOf(
                effectiveDownloaded,
                policy.unconsumed,
                policy.notStarted,
                bookmarkFilter,
                policy.completed,
                releasePeriodFilter,
                *activeTracking.values.toTypedArray(),
            ).any { it != TriState.DISABLED },
            availability = availability,
        )
    }

    private fun FeatureGraphEvaluation.selectedContentTypes(
        integration: FeatureIntegrationId,
        consequence: FeatureArtifactId,
    ): Set<ContentTypeId> {
        return sharedConsequences
            .asSequence()
            .filter { applicability ->
                applicability.subject.feature == ENTRY_LIBRARY_FILTER_FEATURE_ID &&
                    applicability.subject.integration == integration &&
                    applicability.consequence.id == consequence
            }
            .mapTo(mutableSetOf()) { it.subject.contentType }
    }
}

private fun Set<EntryType>.controlAvailability(
    supportedTypes: Set<EntryType>,
): EntryLibraryFilterControlAvailability {
    return EntryLibraryFilterControlAvailability(
        applicableTypes = intersect(supportedTypes),
        inapplicableTypes = subtract(supportedTypes),
    )
}

private fun TriState.matches(value: Boolean): Boolean {
    return when (this) {
        TriState.DISABLED -> true
        TriState.ENABLED_IS -> value
        TriState.ENABLED_NOT -> !value
    }
}

private fun TriState.matchesReleasePeriod(
    target: EntryLibraryFilterTarget,
    applicableTypes: Set<EntryType>,
): Boolean {
    if (target.type !in applicableTypes) return true
    return matches(target.isOutsideReleasePeriod)
}

private fun Map<Long, TriState>.matches(targetTrackerIds: Set<Long>): Boolean {
    if (isEmpty()) return true
    val excluded = filterValues { it == TriState.ENABLED_NOT }.keys
    val included = filterValues { it == TriState.ENABLED_IS }.keys
    return excluded.intersect(targetTrackerIds).isEmpty() &&
        (included.isEmpty() || included.intersect(targetTrackerIds).isNotEmpty())
}
