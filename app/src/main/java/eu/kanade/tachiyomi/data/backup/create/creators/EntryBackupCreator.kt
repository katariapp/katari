package eu.kanade.tachiyomi.data.backup.create.creators

import eu.kanade.tachiyomi.data.backup.create.BackupOptions
import eu.kanade.tachiyomi.data.backup.models.BackupDownloadPreferences
import eu.kanade.tachiyomi.data.backup.models.BackupEntry
import eu.kanade.tachiyomi.data.backup.models.BackupHistory
import eu.kanade.tachiyomi.data.backup.models.BackupPlaybackPreferences
import eu.kanade.tachiyomi.data.backup.models.backupTrackMapper
import eu.kanade.tachiyomi.data.backup.models.toBackupChapter
import eu.kanade.tachiyomi.data.backup.models.toBackupEntry
import eu.kanade.tachiyomi.data.backup.models.toBackupEntryProgressState
import eu.kanade.tachiyomi.data.backup.models.toBackupViewerSettingOverride
import mihon.entry.interactions.EntryChildGroupFilterFeature
import mihon.entry.interactions.EntryChildGroupFilterSnapshotResult
import mihon.entry.interactions.EntryMergeBackupFeature
import mihon.entry.interactions.EntryMergeSubject
import mihon.entry.interactions.EntryPlaybackPreferencesFeature
import mihon.entry.interactions.EntryPlaybackPreferencesSnapshotResult
import mihon.entry.interactions.EntryPlaybackQualityMode
import mihon.entry.interactions.EntryProgressFeature
import mihon.entry.interactions.EntryProgressSnapshotResult
import mihon.entry.interactions.EntryViewerSettingsFeature
import mihon.entry.interactions.EntryViewerSettingsSnapshotResult
import tachiyomi.data.ActiveProfileProvider
import tachiyomi.data.DatabaseHandler
import tachiyomi.domain.category.model.Category
import tachiyomi.domain.entry.model.Entry
import tachiyomi.domain.entry.repository.DownloadPreferencesRepository
import tachiyomi.domain.entry.repository.EntryChapterRepository
import tachiyomi.domain.history.model.History
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get

class EntryBackupCreator(
    private val handler: DatabaseHandler = Injekt.get(),
    private val profileProvider: ActiveProfileProvider = Injekt.get(),
    private val mergeBackupFeature: EntryMergeBackupFeature = Injekt.get(),
    private val entryChapterRepository: EntryChapterRepository = Injekt.get(),
    private val downloadPreferencesRepository: DownloadPreferencesRepository = Injekt.get(),
    private val progressFeature: EntryProgressFeature = Injekt.get(),
    private val playbackPreferencesFeature: EntryPlaybackPreferencesFeature = Injekt.get(),
    private val childGroupFilterFeature: EntryChildGroupFilterFeature = Injekt.get(),
    private val viewerSettingsFeature: EntryViewerSettingsFeature = Injekt.get(),
) {

    suspend operator fun invoke(entries: List<Entry>, options: BackupOptions): List<BackupEntry> {
        return invoke(profileProvider.activeProfileId, entries, options)
    }

    suspend operator fun invoke(
        profileId: Long,
        entries: List<Entry>,
        options: BackupOptions,
    ): List<BackupEntry> {
        return entries.map { backupEntry(profileId, it, options) }
    }

    private suspend fun backupEntry(
        profileId: Long,
        entry: Entry,
        options: BackupOptions,
    ): BackupEntry {
        val entryObject = entry.toBackupEntry()
        entryObject.viewerSettingOverrides = when (val result = viewerSettingsFeature.snapshot(entry)) {
            is EntryViewerSettingsSnapshotResult.Available ->
                result.overrides.map { it.toBackupViewerSettingOverride() }
            is EntryViewerSettingsSnapshotResult.Inapplicable -> emptyList()
        }
        val playbackPreferencesSnapshot = when (val result = playbackPreferencesFeature.snapshot(entry)) {
            is EntryPlaybackPreferencesSnapshotResult.Captured -> result.snapshot
            EntryPlaybackPreferencesSnapshotResult.NoPreferences,
            is EntryPlaybackPreferencesSnapshotResult.Inapplicable,
            -> null
        }
        val progressSnapshot = if (options.chapters) {
            when (val result = progressFeature.snapshot(entry)) {
                is EntryProgressSnapshotResult.Available -> result.snapshot
                is EntryProgressSnapshotResult.Inapplicable -> null
            }
        } else {
            null
        }

        entryObject.excludedScanlators = when (val result = childGroupFilterFeature.snapshot(profileId, entry)) {
            is EntryChildGroupFilterSnapshotResult.Available -> result.excludedGroups.toList()
            is EntryChildGroupFilterSnapshotResult.Inapplicable -> emptyList()
        }

        if (options.chapters) {
            val chapters = entryChapterRepository.getChaptersByEntryIdAwait(entry.id, applyScanlatorFilter = false)
            if (chapters.isNotEmpty()) {
                entryObject.chapters = chapters.map { it.toBackupChapter() }
            }

            entryObject.progressStates = progressSnapshot?.states.orEmpty().map { it.toBackupEntryProgressState() }
        }

        if (options.chapters) {
            val downloadPreferences = downloadPreferencesRepository.getByEntryId(entry.id)
            if (downloadPreferences != null) {
                entryObject.downloadPreferences = BackupDownloadPreferences(
                    dubKey = downloadPreferences.dubKey,
                    streamKey = downloadPreferences.streamKey,
                    subtitleKey = downloadPreferences.subtitleKey,
                    qualityMode = downloadPreferences.qualityMode.name.lowercase(),
                    updatedAt = downloadPreferences.updatedAt,
                )
            }
        }

        val preferences = playbackPreferencesSnapshot
        if (preferences != null) {
            entryObject.playbackPreferences = BackupPlaybackPreferences(
                dubKey = preferences.dubKey,
                streamKey = preferences.streamKey,
                sourceQualityKey = preferences.sourceQualityKey,
                subtitleKey = preferences.subtitleKey,
                playerQualityMode = preferences.playerQualityMode.toBackupValue(),
                playerQualityHeight = preferences.playerQualityHeight,
                subtitleOffsetX = preferences.subtitleOffsetX,
                subtitleOffsetY = preferences.subtitleOffsetY,
                subtitleTextSize = preferences.subtitleTextSize,
                subtitleTextColor = preferences.subtitleTextColor,
                subtitleBackgroundColor = preferences.subtitleBackgroundColor,
                subtitleBackgroundOpacity = preferences.subtitleBackgroundOpacity,
                updatedAt = preferences.updatedAt,
            )
        }

        if (options.categories) {
            val categoriesForEntry = handler.awaitList {
                categoriesQueries.getCategoriesByEntryId(
                    profileId,
                    entry.id,
                ) { id, name, order, flags ->
                    Category(
                        id = id,
                        name = name,
                        order = order,
                        flags = flags,
                    )
                }
            }
            if (categoriesForEntry.isNotEmpty()) {
                entryObject.categories = categoriesForEntry.map { it.order }
            }
        }

        if (options.tracking) {
            val tracks = handler.awaitList {
                entry_syncQueries.getTracksByEntryId(profileId, entry.id, backupTrackMapper)
            }
            if (tracks.isNotEmpty()) {
                entryObject.tracking = tracks
            }
        }

        if (options.history) {
            val historyByEntryId = handler.awaitList {
                historyQueries.getHistoryByEntryId(entry.id) { _, chapterId, lastRead, timeRead ->
                    History(
                        id = 0,
                        chapterId = chapterId,
                        readAt = lastRead,
                        readDuration = timeRead,
                    )
                }
            }
            if (historyByEntryId.isNotEmpty()) {
                val history = historyByEntryId.mapNotNull { history ->
                    val chapter = entryChapterRepository.getChapterById(history.chapterId) ?: return@mapNotNull null
                    BackupHistory(chapter.url, history.readAt?.time ?: 0L, history.readDuration)
                }
                if (history.isNotEmpty()) {
                    entryObject.history = history
                }
            }
        }

        mergeBackupFeature.snapshotForBackup(EntryMergeSubject(profileId, entry.id))?.let { merge ->
            entryObject.mergeTargetSource = merge.target.sourceId
            entryObject.mergeTargetUrl = merge.target.url
            entryObject.mergeTargetType = merge.target.type
            entryObject.mergePosition = merge.position
        }

        return entryObject
    }
}

private fun EntryPlaybackQualityMode.toBackupValue(): String {
    return when (this) {
        EntryPlaybackQualityMode.AUTO -> "auto"
        EntryPlaybackQualityMode.SPECIFIC_HEIGHT -> "specific_height"
    }
}
