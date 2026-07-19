package mihon.entry.interactions

import eu.kanade.tachiyomi.source.entry.EntryType
import tachiyomi.domain.entry.model.Entry

/** Feature-owned boundary for portable progress snapshot, restore, and copy operations. */
interface EntryProgressFeature {
    fun isApplicable(type: EntryType): Boolean

    suspend fun snapshot(entry: Entry): EntryProgressSnapshotResult

    suspend fun restore(
        entry: Entry,
        snapshot: EntryProgressSnapshot,
    ): EntryProgressRestoreResult

    suspend fun copy(
        sourceEntry: Entry,
        targetEntry: Entry,
        resourceMappings: List<EntryProgressResourceMapping>,
    ): EntryProgressCopyResult
}

sealed interface EntryProgressSnapshotResult {
    data class Available(val snapshot: EntryProgressSnapshot) : EntryProgressSnapshotResult

    data class Inapplicable(val type: EntryType) : EntryProgressSnapshotResult
}

sealed interface EntryProgressRestoreResult {
    data object Applied : EntryProgressRestoreResult

    data class Inapplicable(val type: EntryType) : EntryProgressRestoreResult
}

sealed interface EntryProgressCopyResult {
    data object Applied : EntryProgressCopyResult

    data class Inapplicable(val types: Set<EntryType>) : EntryProgressCopyResult

    data class IncompatibleTypes(
        val sourceType: EntryType,
        val targetType: EntryType,
    ) : EntryProgressCopyResult
}
