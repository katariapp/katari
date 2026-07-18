package mihon.entry.interactions

import eu.kanade.tachiyomi.source.entry.EntryType
import mihon.entry.viewer.settings.ViewerSettingId
import mihon.entry.viewer.settings.ViewerSettingOverride
import mihon.entry.viewer.settings.ViewerSettingsCategory
import tachiyomi.domain.entry.model.Entry

/** App-owned screen implementation matched to one provider-owned viewer-settings surface. */
interface EntryViewerSettingsScreenProjection {
    val surfaceId: String
}

data class EntryViewerSettingsDestination(
    val type: EntryType,
    val surfaceId: String,
    val category: ViewerSettingsCategory,
    val displayName: String,
    val description: String?,
    val origin: String?,
    val projection: EntryViewerSettingsScreenProjection,
)

data class EntryViewerSettingsPreferenceOwnership(
    val profileKeys: Set<String>,
    val appStateKeys: Set<String>,
    val privateKeys: Set<String>,
)

sealed interface EntryViewerSettingsSnapshotResult {
    data class Available(val overrides: List<ViewerSettingOverride>) : EntryViewerSettingsSnapshotResult
    data class Inapplicable(val type: EntryType) : EntryViewerSettingsSnapshotResult
}

sealed interface EntryViewerSettingsRestoreResult {
    data class Restored(
        val restoredCount: Int,
        val rejectedSettingIds: Set<ViewerSettingId>,
    ) : EntryViewerSettingsRestoreResult

    data class Inapplicable(val type: EntryType) : EntryViewerSettingsRestoreResult
}

sealed interface EntryViewerSettingsCopyResult {
    data class Copied(val copiedCount: Int) : EntryViewerSettingsCopyResult
    data class Inapplicable(val sourceType: EntryType, val targetType: EntryType) : EntryViewerSettingsCopyResult
}

/** Feature-owned boundary for discovery, UI projection, preferences, overrides, backup, and migration. */
interface EntryViewerSettingsFeature {
    val destinations: List<EntryViewerSettingsDestination>
    val preferenceOwnership: EntryViewerSettingsPreferenceOwnership

    fun isApplicable(type: EntryType): Boolean

    suspend fun snapshot(entry: Entry): EntryViewerSettingsSnapshotResult
    suspend fun restore(entry: Entry, overrides: List<ViewerSettingOverride>): EntryViewerSettingsRestoreResult
    suspend fun copy(source: Entry, target: Entry): EntryViewerSettingsCopyResult
    suspend fun resetProfileOverrides(profileId: Long): EntryViewerSettingsResetResult
}

sealed interface EntryViewerSettingsResetResult {
    data object Reset : EntryViewerSettingsResetResult
    data object LegacyViewerFlagsFailed : EntryViewerSettingsResetResult
}
