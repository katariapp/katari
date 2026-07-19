package mihon.feature.profiles.core

import app.cash.sqldelight.async.coroutines.awaitAsOneOrNull
import eu.kanade.domain.source.service.SourcePreferences
import mihon.entry.interactions.EntryMergeProfileMoveDestinationResult
import mihon.entry.interactions.EntryMergeProfileMoveExecutionResult
import mihon.entry.interactions.EntryMergeProfileMoveFeature
import mihon.entry.interactions.EntryMergeProfileMoveIntent
import mihon.entry.interactions.EntryMergeProfileMovePreparationResult
import mihon.entry.interactions.EntryMergeProfileMoveReference
import tachiyomi.data.Database
import tachiyomi.data.DatabaseHandler
import tachiyomi.data.entry.EntryMapper
import tachiyomi.domain.entry.model.Entry

data class EntryProfileMoveRequest(
    val sourceProfileId: Long,
    val destinationProfileId: Long,
    val destinationCategoryId: Long?,
    val selectedVisibleEntryIds: List<Long>,
)

data class EntryProfileMoveGroup(
    val entries: List<Entry>,
)

data class EntryProfileMoveConflict(
    val sourceEntry: Entry,
    val destinationEntry: Entry,
    val destinationMergeAffected: Boolean,
)

data class EntryProfileMovePreview(
    val request: EntryProfileMoveRequest,
    val mergeReference: EntryMergeProfileMoveReference,
    val groups: List<EntryProfileMoveGroup>,
    val conflicts: List<EntryProfileMoveConflict>,
)

enum class EntryProfileMoveConflictResolution {
    KEEP_SOURCE,
    OVERWRITE_DESTINATION,
    KEEP_DESTINATION_REMOVE_SOURCE,
}

internal fun Entry.sameProfileMoveIdentity(other: Entry): Boolean {
    return source == other.source && url == other.url && type == other.type
}

internal fun EntryProfileMoveGroup.shouldSkip(
    conflicts: List<EntryProfileMoveConflict>,
    resolutions: Map<Long, EntryProfileMoveConflictResolution>,
): Boolean {
    val entryIds = entries.map(Entry::id).toSet()
    return conflicts.any {
        it.sourceEntry.id in entryIds &&
            resolutions[it.sourceEntry.id] == EntryProfileMoveConflictResolution.KEEP_SOURCE
    }
}

data class EntryProfileMoveResult(
    val movedSelectedItemCount: Int,
    val skippedSelectedItemCount: Int,
    val overwrittenDuplicateCount: Int,
    val removedSourceDuplicateCount: Int,
)

class EntryProfileMoveService(
    private val handler: DatabaseHandler,
    private val profileStore: ProfileStore,
    private val mergeProfileMoveFeature: EntryMergeProfileMoveFeature,
) {

    suspend fun preview(request: EntryProfileMoveRequest): EntryProfileMovePreview {
        require(request.sourceProfileId != request.destinationProfileId)
        require(request.selectedVisibleEntryIds.isNotEmpty())

        handler.await { validateDestination(request) }
        val preparation = mergeProfileMoveFeature.prepare(
            sourceProfileId = request.sourceProfileId,
            selectedVisibleEntryIds = request.selectedVisibleEntryIds,
        )
        val ready = preparation as? EntryMergeProfileMovePreparationResult.Ready
            ?: error("No selected entries still belong to the source profile")
        val groups = ready.units.map { EntryProfileMoveGroup(it.entries) }
        val conflicts = handler.await {
            groups.flatMap { group ->
                group.entries.mapNotNull { sourceEntry ->
                    val destinationEntry = findDestinationEntry(request.destinationProfileId, sourceEntry)
                        ?: return@mapNotNull null
                    EntryProfileMoveConflict(
                        sourceEntry = sourceEntry,
                        destinationEntry = destinationEntry,
                        destinationMergeAffected = false,
                    )
                }
            }
        }
        val destination = mergeProfileMoveFeature.inspectDestination(
            reference = ready.reference,
            destinationProfileId = request.destinationProfileId,
            destinationEntryIds = conflicts.map { it.destinationEntry.id },
        ) as? EntryMergeProfileMoveDestinationResult.Ready
            ?: error("Merge Profile Move snapshot could not be prepared")
        return EntryProfileMovePreview(
            request = request,
            mergeReference = destination.reference,
            groups = groups,
            conflicts = conflicts.map { conflict ->
                conflict.copy(
                    destinationMergeAffected = conflict.destinationEntry.id in destination.mergeAffectedEntryIds,
                )
            },
        )
    }

    suspend fun execute(
        preview: EntryProfileMovePreview,
        resolutions: Map<Long, EntryProfileMoveConflictResolution>,
    ): EntryProfileMoveResult {
        val conflictIds = preview.conflicts.map { it.sourceEntry.id }.toSet()
        require(resolutions.keys.containsAll(conflictIds)) { "Every conflict must be resolved" }

        val (result, movedSourceIds) = handler.await(inTransaction = true) {
            val request = preview.request
            validateDestination(request)
            revalidatePreview(preview)

            var movedItems = 0
            var skippedItems = 0
            var overwritten = 0
            var removedSource = 0
            val movedSourceIds = mutableSetOf<Long>()

            val destinationIdsBySourceId = mutableMapOf<Long, Long>()
            val destinationEntryIdsToDetach = mutableSetOf<Long>()
            val groupsToMove = preview.groups.filter { group ->
                val groupConflicts = preview.conflicts.filter { conflict ->
                    conflict.sourceEntry.id in group.entries.map(Entry::id)
                }
                if (group.shouldSkip(groupConflicts, resolutions)) {
                    skippedItems++
                    false
                } else {
                    group.entries.forEach { sourceEntry ->
                        val conflict = groupConflicts.firstOrNull { it.sourceEntry.id == sourceEntry.id }
                        when (conflict?.let { resolutions[it.sourceEntry.id] }) {
                            EntryProfileMoveConflictResolution.OVERWRITE_DESTINATION -> {
                                destinationIdsBySourceId[sourceEntry.id] = sourceEntry.id
                                destinationEntryIdsToDetach += conflict.destinationEntry.id
                                overwritten++
                            }
                            EntryProfileMoveConflictResolution.KEEP_DESTINATION_REMOVE_SOURCE -> {
                                destinationIdsBySourceId[sourceEntry.id] = conflict.destinationEntry.id
                                destinationEntryIdsToDetach += conflict.destinationEntry.id
                                removedSource++
                            }
                            EntryProfileMoveConflictResolution.KEEP_SOURCE -> error(
                                "Skipped groups are handled before mutation",
                            )
                            null -> destinationIdsBySourceId[sourceEntry.id] = sourceEntry.id
                        }
                    }
                    movedSourceIds += group.entries.map(Entry::source)
                    movedItems++
                    true
                }
            }

            val mergeResult = mergeProfileMoveFeature.execute(
                intent = EntryMergeProfileMoveIntent(
                    reference = preview.mergeReference,
                    destinationProfileId = request.destinationProfileId,
                    destinationEntryIdsBySourceEntryId = destinationIdsBySourceId,
                    destinationEntryIdsToDetach = destinationEntryIdsToDetach,
                ),
            ) {
                groupsToMove.forEach { group ->
                    val groupConflicts = preview.conflicts.filter { conflict ->
                        conflict.sourceEntry.id in group.entries.map(Entry::id)
                    }
                    group.entries.forEach { sourceEntry ->
                        val conflict = groupConflicts.firstOrNull { it.sourceEntry.id == sourceEntry.id }
                        when (conflict?.let { resolutions[it.sourceEntry.id] }) {
                            EntryProfileMoveConflictResolution.OVERWRITE_DESTINATION -> {
                                entriesQueries.deleteById(request.destinationProfileId, conflict.destinationEntry.id)
                                moveEntry(request, sourceEntry.id)
                            }
                            EntryProfileMoveConflictResolution.KEEP_DESTINATION_REMOVE_SOURCE -> {
                                val destinationId = conflict.destinationEntry.id
                                entriesQueries.deleteById(request.sourceProfileId, sourceEntry.id)
                                assignDestinationCategory(request, destinationId)
                                entriesQueries.setFavoriteForProfile(true, request.destinationProfileId, destinationId)
                            }
                            EntryProfileMoveConflictResolution.KEEP_SOURCE -> error(
                                "Skipped groups are handled before mutation",
                            )
                            null -> moveEntry(request, sourceEntry.id)
                        }
                    }
                }
            }
            when (mergeResult) {
                EntryMergeProfileMoveExecutionResult.Applied -> Unit
                EntryMergeProfileMoveExecutionResult.Conflict -> error("Merge membership changed before the move")
                is EntryMergeProfileMoveExecutionResult.OperationalFailure -> error(
                    "Merge Profile Move failed before it could commit",
                )
            }

            EntryProfileMoveResult(
                movedSelectedItemCount = movedItems,
                skippedSelectedItemCount = skippedItems,
                overwrittenDuplicateCount = overwritten,
                removedSourceDuplicateCount = removedSource,
            ) to movedSourceIds
        }
        makeSourcesVisible(preview.request.destinationProfileId, movedSourceIds)
        return result
    }

    private suspend fun Database.validateDestination(request: EntryProfileMoveRequest) {
        val profile = profilesQueries.getProfileById(request.destinationProfileId).awaitAsOneOrNull()
            ?: error("Destination profile no longer exists")
        require(!profile.is_archived) { "Destination profile is archived" }
        require(request.sourceProfileId != request.destinationProfileId)

        request.destinationCategoryId?.let { categoryId ->
            val category = categoriesQueries.getCategory(categoryId, request.destinationProfileId).awaitAsOneOrNull()
            require(category != null && category.id != 0L) { "Destination category no longer exists" }
        }
    }

    private suspend fun Database.revalidatePreview(preview: EntryProfileMovePreview) {
        val destinationConflicts = preview.conflicts.associateBy { it.sourceEntry.id }
        preview.groups.flatMap(EntryProfileMoveGroup::entries).forEach { previewEntry ->
            val sourceEntry = entriesQueries
                .getEntryById(previewEntry.id, preview.request.sourceProfileId, EntryMapper::mapEntry)
                .awaitAsOneOrNull()
                ?: error("Selected entry changed before the move")
            require(sourceEntry.sameProfileMoveIdentity(previewEntry)) {
                "Selected entry identity changed before the move"
            }
            val currentDestination = findDestinationEntry(preview.request.destinationProfileId, sourceEntry)
            require(currentDestination?.id == destinationConflicts[sourceEntry.id]?.destinationEntry?.id) {
                "Destination entries changed before the move"
            }
        }
    }

    private suspend fun Database.findDestinationEntry(profileId: Long, sourceEntry: Entry): Entry? {
        return entriesQueries.getEntryByUrlAndSource(
            profileId,
            sourceEntry.url,
            sourceEntry.source,
            sourceEntry.type.name.lowercase(),
            EntryMapper::mapEntry,
        ).awaitAsOneOrNull()
    }

    private suspend fun Database.moveEntry(request: EntryProfileMoveRequest, entryId: Long) {
        entries_categoriesQueries.deleteByEntryId(request.sourceProfileId, entryId)
        entry_syncQueries.moveEntryToProfile(request.destinationProfileId, request.sourceProfileId, entryId)
        excluded_scanlatorsQueries.moveEntryToProfile(request.destinationProfileId, request.sourceProfileId, entryId)
        entry_cover_hashesQueries.moveEntryToProfile(request.destinationProfileId, request.sourceProfileId, entryId)
        entriesQueries.moveToProfile(request.destinationProfileId, request.sourceProfileId, entryId)
        assignDestinationCategory(request, entryId)
    }

    private suspend fun Database.assignDestinationCategory(request: EntryProfileMoveRequest, entryId: Long) {
        entries_categoriesQueries.deleteByEntryId(request.destinationProfileId, entryId)
        request.destinationCategoryId?.let { categoryId ->
            entries_categoriesQueries.insert(request.destinationProfileId, entryId, categoryId)
        }
    }

    private fun makeSourcesVisible(profileId: Long, sourceIds: Set<Long>) {
        if (sourceIds.isEmpty()) return
        val hiddenSources = profileStore.profileStore(profileId)
            .getStringSet(SourcePreferences.HIDDEN_SOURCES_KEY, emptySet())
        val updatedHiddenSources = hiddenSourcesAfterMove(hiddenSources.get(), sourceIds)
        if (updatedHiddenSources != hiddenSources.get()) {
            hiddenSources.set(updatedHiddenSources)
        }
    }
}

internal fun hiddenSourcesAfterMove(hiddenSources: Set<String>, movedSourceIds: Set<Long>): Set<String> {
    return hiddenSources - movedSourceIds.mapTo(mutableSetOf(), Long::toString)
}
