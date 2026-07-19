package mihon.entry.interactions

import eu.kanade.tachiyomi.source.entry.EntryType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onStart
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

private val ENTRY_CHILD_GROUP_FILTER_FEATURE_ID = FeatureId("entry.child-group-filter")
private val ENTRY_CHILD_GROUP_FILTER_FEATURE_OWNER = ContributionOwner("entry-child-group-filter")
private val ENTRY_CHILD_GROUP_FILTER_INTEGRATION_ID = FeatureIntegrationId("entry.child-group-filter.provider")
private val ENTRY_CHILD_GROUP_FILTER_STATE_CONSEQUENCE_ID =
    FeatureArtifactId("entry.child-group-filter.state")
private val ENTRY_CHILD_GROUP_FILTER_APPLY_CONSEQUENCE_ID =
    FeatureArtifactId("entry.child-group-filter.apply")
private val ENTRY_CHILD_GROUP_FILTER_PERSISTENCE_CONSEQUENCE_ID =
    FeatureArtifactId("entry.child-group-filter.persistence")
private val ENTRY_CHILD_GROUP_FILTER_BACKUP_CONSEQUENCE_ID =
    FeatureArtifactId("entry.child-group-filter.backup")
private val ENTRY_CHILD_GROUP_FILTER_SHARED_STORAGE_CONSEQUENCE_ID =
    FeatureArtifactId("entry.child-group-filter.shared-storage-consumers")

private object EntryChildGroupFilterStateConsequence : SharedFeatureConsequence {
    override val id = ENTRY_CHILD_GROUP_FILTER_STATE_CONSEQUENCE_ID
}

private object EntryChildGroupFilterApplyConsequence : SharedFeatureConsequence {
    override val id = ENTRY_CHILD_GROUP_FILTER_APPLY_CONSEQUENCE_ID
}

private object EntryChildGroupFilterPersistenceConsequence : SharedFeatureConsequence {
    override val id = ENTRY_CHILD_GROUP_FILTER_PERSISTENCE_CONSEQUENCE_ID
}

private object EntryChildGroupFilterBackupConsequence : SharedFeatureConsequence {
    override val id = ENTRY_CHILD_GROUP_FILTER_BACKUP_CONSEQUENCE_ID
}

private object EntryChildGroupFilterSharedStorageConsequence : SharedFeatureConsequence {
    override val id = ENTRY_CHILD_GROUP_FILTER_SHARED_STORAGE_CONSEQUENCE_ID
}

internal object EntryChildGroupFilterFeatureContributor : FeatureGraphContributor {
    override val owner = ENTRY_CHILD_GROUP_FILTER_FEATURE_OWNER

    override fun contributeTo(sink: FeatureGraphContributionSink) {
        sink.add(
            FeatureContribution(
                feature = ENTRY_CHILD_GROUP_FILTER_FEATURE_ID,
                owner = owner,
                integrations = listOf(
                    FeatureIntegration(
                        id = ENTRY_CHILD_GROUP_FILTER_INTEGRATION_ID,
                        prerequisites = CapabilityExpression.Provided(EntryChildGroupFilterCapability.definition),
                        sharedConsequences = listOf(
                            EntryChildGroupFilterStateConsequence,
                            EntryChildGroupFilterApplyConsequence,
                            EntryChildGroupFilterPersistenceConsequence,
                            EntryChildGroupFilterBackupConsequence,
                            EntryChildGroupFilterSharedStorageConsequence,
                        ),
                    ),
                ),
            ),
        )
    }
}

internal class DefaultEntryChildGroupFilterFeature(
    evaluation: FeatureGraphEvaluation,
    private val interaction: EntryChildGroupFilterInteraction,
    private val dataSource: EntryChildGroupFilterDataSource,
) : EntryChildGroupFilterFeature {
    private val applicableTypes = evaluation.applicableProviderTypes<EntryChildGroupFilterProcessor>(
        feature = ENTRY_CHILD_GROUP_FILTER_FEATURE_ID,
        integration = ENTRY_CHILD_GROUP_FILTER_INTEGRATION_ID,
        consequence = ENTRY_CHILD_GROUP_FILTER_STATE_CONSEQUENCE_ID,
    )
    private val filterTypes = evaluation.applicableProviderTypes<EntryChildGroupFilterProcessor>(
        feature = ENTRY_CHILD_GROUP_FILTER_FEATURE_ID,
        integration = ENTRY_CHILD_GROUP_FILTER_INTEGRATION_ID,
        consequence = ENTRY_CHILD_GROUP_FILTER_APPLY_CONSEQUENCE_ID,
    )
    private val persistenceTypes = evaluation.applicableProviderTypes<EntryChildGroupFilterProcessor>(
        feature = ENTRY_CHILD_GROUP_FILTER_FEATURE_ID,
        integration = ENTRY_CHILD_GROUP_FILTER_INTEGRATION_ID,
        consequence = ENTRY_CHILD_GROUP_FILTER_PERSISTENCE_CONSEQUENCE_ID,
    )
    private val backupTypes = evaluation.applicableProviderTypes<EntryChildGroupFilterProcessor>(
        feature = ENTRY_CHILD_GROUP_FILTER_FEATURE_ID,
        integration = ENTRY_CHILD_GROUP_FILTER_INTEGRATION_ID,
        consequence = ENTRY_CHILD_GROUP_FILTER_BACKUP_CONSEQUENCE_ID,
    )
    private val sharedStorageTypes = evaluation.applicableProviderTypes<EntryChildGroupFilterProcessor>(
        feature = ENTRY_CHILD_GROUP_FILTER_FEATURE_ID,
        integration = ENTRY_CHILD_GROUP_FILTER_INTEGRATION_ID,
        consequence = ENTRY_CHILD_GROUP_FILTER_SHARED_STORAGE_CONSEQUENCE_ID,
    )

    init {
        check(setOf(applicableTypes, filterTypes, persistenceTypes, backupTypes, sharedStorageTypes).size == 1) {
            "Child-group filtering consequences selected different provider sets"
        }
    }

    override fun isApplicable(type: EntryType): Boolean = type in applicableTypes

    override suspend fun state(scope: EntryChildGroupFilterScope): EntryChildGroupFilterStateResult {
        if (scope.entry.type !in applicableTypes) {
            return EntryChildGroupFilterStateResult.Inapplicable(scope.entry.type)
        }
        return EntryChildGroupFilterStateResult.Available(loadState(scope, profileId = null))
    }

    override fun observe(scope: EntryChildGroupFilterScope): EntryChildGroupFilterObservationResult {
        if (scope.entry.type !in applicableTypes) {
            return EntryChildGroupFilterObservationResult.Inapplicable(scope.entry.type)
        }
        val memberIds = scope.normalizedMemberIds()
        val states: Flow<EntryChildGroupFilterState> = merge(
            dataSource.childrenChanged(memberIds),
            dataSource.excludedGroupsChanged(profileId = null, entryIds = memberIds),
        )
            .onStart { emit(Unit) }
            .mapLatest { loadState(scope, profileId = null) }
            .distinctUntilChanged()
        return EntryChildGroupFilterObservationResult.Available(states)
    }

    override fun filter(
        entry: Entry,
        chapters: List<EntryChapter>,
        excludedGroups: Set<String>,
    ): EntryChildGroupFilterResult {
        if (entry.type !in filterTypes) return EntryChildGroupFilterResult.Inapplicable(entry.type)
        val normalizedExcluded = normalizeGroups(entry, excludedGroups)
        return EntryChildGroupFilterResult.Available(
            chapters.filterNot { chapter ->
                interaction.groupFor(entry, chapter) in normalizedExcluded
            },
        )
    }

    override suspend fun setExcludedGroups(
        scope: EntryChildGroupFilterScope,
        excludedGroups: Set<String>,
    ): EntryChildGroupFilterMutationResult {
        if (scope.entry.type !in persistenceTypes) {
            return EntryChildGroupFilterMutationResult.Inapplicable(scope.entry.type)
        }
        val memberIds = scope.normalizedMemberIds()
        val normalized = normalizeGroups(scope.entry, excludedGroups)
        val currentByMember = dataSource.excludedGroups(profileId = null, entryIds = memberIds)
            .mapValues { (_, groups) -> normalizeGroups(scope.entry, groups) }
        if (memberIds.all { currentByMember[it].orEmpty() == normalized }) {
            return EntryChildGroupFilterMutationResult.NoChange
        }
        dataSource.setExcludedGroups(profileId = null, entryIds = memberIds, excluded = normalized)
        return EntryChildGroupFilterMutationResult.Applied(memberIds.size)
    }

    override suspend fun snapshot(profileId: Long, entry: Entry): EntryChildGroupFilterSnapshotResult {
        if (entry.type !in backupTypes) return EntryChildGroupFilterSnapshotResult.Inapplicable(entry.type)
        val excluded = dataSource.excludedGroups(profileId = profileId, entryIds = listOf(entry.id))[entry.id].orEmpty()
        return EntryChildGroupFilterSnapshotResult.Available(normalizeGroups(entry, excluded))
    }

    override suspend fun restore(entry: Entry, excludedGroups: Set<String>): EntryChildGroupFilterRestoreResult {
        if (entry.type !in backupTypes) return EntryChildGroupFilterRestoreResult.Inapplicable(entry.type)
        if (excludedGroups.isEmpty()) return EntryChildGroupFilterRestoreResult.NoChange
        val memberIds = listOf(entry.id)
        val current = normalizeGroups(
            entry,
            dataSource.excludedGroups(profileId = null, entryIds = memberIds)[entry.id].orEmpty(),
        )
        val restored = current + normalizeGroups(entry, excludedGroups)
        if (restored == current) return EntryChildGroupFilterRestoreResult.NoChange
        dataSource.setExcludedGroups(profileId = null, entryIds = memberIds, excluded = restored)
        return EntryChildGroupFilterRestoreResult.Restored(memberIds.size)
    }

    private suspend fun loadState(
        scope: EntryChildGroupFilterScope,
        profileId: Long?,
    ): EntryChildGroupFilterState {
        val memberIds = scope.normalizedMemberIds()
        val available = dataSource.children(memberIds)
            .mapNotNullTo(linkedSetOf()) { interaction.groupFor(scope.entry, it) }
        val excluded = dataSource.excludedGroups(profileId = profileId, entryIds = memberIds)
            .values
            .flatten()
            .let { normalizeGroups(scope.entry, it) }
        return EntryChildGroupFilterState(availableGroups = available, excludedGroups = excluded)
    }

    private fun normalizeGroups(entry: Entry, groups: Collection<String>): Set<String> {
        return groups.mapNotNullTo(linkedSetOf()) { interaction.normalizeGroup(entry, it) }
    }

    private fun EntryChildGroupFilterScope.normalizedMemberIds(): List<Long> {
        return memberIds.distinct().ifEmpty { listOf(entry.id) }
    }
}
