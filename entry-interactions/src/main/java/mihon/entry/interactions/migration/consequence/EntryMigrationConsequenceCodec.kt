package mihon.entry.interactions

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import mihon.entry.interactions.host.EntryMigrationCustomCoverPayload

internal object EntryMigrationConsequenceArtifact {
    const val PROGRESS = "entry.migration.progress"
    const val PLAYBACK_PREFERENCES = "entry.migration.playback-preferences"
    const val VIEWER_SETTINGS = "entry.migration.viewer-settings"
    const val DOWNLOAD_REMOVAL = "entry.migration.download-removal"
    const val CUSTOM_COVER = "entry.migration.custom-cover"
}

internal class EntryMigrationConsequenceCodec(
    private val json: Json = Json {
        encodeDefaults = true
        ignoreUnknownKeys = true
    },
) {
    fun encode(payload: EntryProgressMigrationPayload): String = json.encodeToString(payload)
    fun encode(payload: EntryPlaybackPreferencesMigrationPayload): String = json.encodeToString(payload)
    fun encode(payload: EntryViewerSettingsMigrationPayload): String = json.encodeToString(payload)
    fun encode(payload: EntryDownloadRemovalPlan): String = json.encodeToString(payload)
    fun encode(payload: EntryMigrationCustomCoverPayload): String = json.encodeToString(payload)

    fun progress(value: String): EntryProgressMigrationPayload = json.decodeFromString(value)
    fun playback(value: String): EntryPlaybackPreferencesMigrationPayload = json.decodeFromString(value)
    fun viewerSettings(value: String): EntryViewerSettingsMigrationPayload = json.decodeFromString(value)
    fun downloadRemoval(value: String): EntryDownloadRemovalPlan = json.decodeFromString(value)
    fun customCover(value: String): EntryMigrationCustomCoverPayload = json.decodeFromString(value)
}
