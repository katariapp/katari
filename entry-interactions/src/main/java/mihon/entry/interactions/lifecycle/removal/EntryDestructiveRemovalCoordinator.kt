package mihon.entry.interactions

import kotlinx.coroutines.CancellationException
import mihon.feature.graph.FeatureExecutionRuntime
import tachiyomi.domain.entry.model.Entry

internal class EntryDestructiveRemovalCoordinator(
    private val host: EntryDestructiveRemovalHost,
    private val executions: FeatureExecutionRuntime,
) : EntryDestructiveRemovalFeature {
    override suspend fun remove(entries: List<Entry>): EntryDestructiveRemovalResult {
        val requested = entries.distinctBy { it.profileId to it.id }
        if (requested.isEmpty()) return EntryDestructiveRemovalResult.NoChange
        return try {
            val collected = MutableEntryDestructiveRemovalOutcomes()
            when (
                val commit = host.remove(requested) { persisted ->
                    persisted.groupBy(Entry::type).forEach { (type, typedEntries) ->
                        val result = executions.execute(
                            point = ENTRY_DESTRUCTIVE_REMOVING_EXECUTION_POINT,
                            contentType = type.toContentTypeId(),
                            event = EntryDestructiveRemovingEvent(typedEntries, collected),
                        )
                        check(result.isSuccessful) {
                            "Transactional destructive-removal participants failed: " +
                                result.failures.joinToString { it.participant.value }
                        }
                    }
                }
            ) {
                is EntryDestructiveRemovalCommit.Applied -> {
                    val failures = commit.entries.groupBy(Entry::type).flatMap { (type, typedEntries) ->
                        executions.execute(
                            point = ENTRY_DESTRUCTIVE_REMOVED_EXECUTION_POINT,
                            contentType = type.toContentTypeId(),
                            event = EntryDestructiveRemovedEvent(typedEntries, collected.snapshot()),
                        ).toLifecycleFailures()
                    }
                    EntryDestructiveRemovalResult.Removed(commit.entries, failures)
                }
                EntryDestructiveRemovalCommit.NoChange -> EntryDestructiveRemovalResult.NoChange
                EntryDestructiveRemovalCommit.Conflict -> EntryDestructiveRemovalResult.Failed(
                    requested,
                    IllegalStateException("Entries changed before destructive removal"),
                )
            }
        } catch (error: CancellationException) {
            throw error
        } catch (error: Exception) {
            EntryDestructiveRemovalResult.Failed(requested, error)
        }
    }
}

private class MutableEntryDestructiveRemovalOutcomes : EntryDestructiveRemovalOutcomeSink {
    private val downloadOwners = linkedMapOf<Pair<Long, Long>, Entry>()

    override fun addDownloadPlan(plan: EntryDownloadRemovalPlan) {
        plan.owners.forEach { owner -> downloadOwners[owner.profileId to owner.id] = owner }
    }

    fun snapshot(): EntryDestructiveRemovalOutcomes {
        val owners = downloadOwners.values.toList()
        return EntryDestructiveRemovalOutcomes(
            downloadPlans = if (owners.isEmpty()) emptyList() else listOf(EntryDownloadRemovalPlan(owners)),
        )
    }
}
