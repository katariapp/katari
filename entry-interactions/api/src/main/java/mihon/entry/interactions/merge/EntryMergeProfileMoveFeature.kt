package mihon.entry.interactions

import tachiyomi.domain.entry.model.Entry

interface EntryMergeProfileMoveFeature {
    suspend fun prepare(
        sourceProfileId: Long,
        selectedVisibleEntryIds: List<Long>,
    ): EntryMergeProfileMovePreparationResult

    suspend fun inspectDestination(
        reference: EntryMergeProfileMoveReference,
        destinationProfileId: Long,
        destinationEntryIds: List<Long>,
    ): EntryMergeProfileMoveDestinationResult

    /**
     * Participates in the Profile Move transaction that invokes this function. The callback performs the non-Merge
     * database work between Merge's source detachment and destination reconstruction.
     */
    suspend fun execute(
        intent: EntryMergeProfileMoveIntent,
        moveEntries: suspend () -> Unit,
    ): EntryMergeProfileMoveExecutionResult
}

interface EntryMergeProfileMoveReference

data class EntryMergeProfileMoveUnit(
    val entries: List<Entry>,
)

sealed interface EntryMergeProfileMovePreparationResult {
    data class Ready(
        val reference: EntryMergeProfileMoveReference,
        val units: List<EntryMergeProfileMoveUnit>,
    ) : EntryMergeProfileMovePreparationResult

    data object Empty : EntryMergeProfileMovePreparationResult
}

sealed interface EntryMergeProfileMoveDestinationResult {
    data class Ready(
        val reference: EntryMergeProfileMoveReference,
        val mergeAffectedEntryIds: Set<Long>,
    ) : EntryMergeProfileMoveDestinationResult

    data object InvalidReference : EntryMergeProfileMoveDestinationResult
}

data class EntryMergeProfileMoveIntent(
    val reference: EntryMergeProfileMoveReference,
    val destinationProfileId: Long,
    val destinationEntryIdsBySourceEntryId: Map<Long, Long>,
    val destinationEntryIdsToDetach: Set<Long>,
)

sealed interface EntryMergeProfileMoveExecutionResult {
    data object Applied : EntryMergeProfileMoveExecutionResult
    data object Conflict : EntryMergeProfileMoveExecutionResult

    data class OperationalFailure(
        val retryable: Boolean,
    ) : EntryMergeProfileMoveExecutionResult
}
