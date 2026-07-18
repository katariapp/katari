package eu.kanade.tachiyomi.ui.stats

import androidx.compose.ui.util.fastDistinctBy
import androidx.compose.ui.util.fastFilter
import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import eu.kanade.presentation.more.stats.StatsScreenState
import eu.kanade.presentation.more.stats.data.StatsData
import eu.kanade.tachiyomi.data.track.TrackerManager
import kotlinx.coroutines.flow.update
import mihon.entry.interactions.EntryDownloadRuntimeFeature
import mihon.entry.interactions.EntryUpdateEligibility
import mihon.entry.interactions.EntryUpdateEligibilityFeature
import mihon.entry.interactions.EntryUpdateEligibilityRequest
import tachiyomi.core.common.util.lang.launchIO
import tachiyomi.domain.entry.interactor.GetLibraryEntries
import tachiyomi.domain.entry.model.EntryStatus
import tachiyomi.domain.history.interactor.GetTotalReadDuration
import tachiyomi.domain.library.model.LibraryItem
import tachiyomi.domain.library.model.LibraryItemKey
import tachiyomi.domain.library.service.LibraryPreferences
import tachiyomi.domain.track.interactor.GetTracks
import tachiyomi.domain.track.model.EntryTrack
import tachiyomi.source.local.LocalSource
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get

class StatsScreenModel(
    private val downloadRuntime: EntryDownloadRuntimeFeature = Injekt.get(),
    private val entryUpdateEligibility: EntryUpdateEligibilityFeature = Injekt.get(),
    private val getLibraryEntries: GetLibraryEntries = Injekt.get(),
    private val getTotalReadDuration: GetTotalReadDuration = Injekt.get(),
    private val getTracks: GetTracks = Injekt.get(),
    private val preferences: LibraryPreferences = Injekt.get(),
    private val trackerManager: TrackerManager = Injekt.get(),
) : StateScreenModel<StatsScreenState>(StatsScreenState.Loading) {

    private val loggedInTrackers by lazy { trackerManager.loggedInTrackers() }

    init {
        screenModelScope.launchIO {
            val libraryEntries = getLibraryEntries.await()

            val distinctLibraryEntries = libraryEntries.fastDistinctBy { it.key }
            val hasCompleteProgressCoverage = distinctLibraryEntries.all(LibraryItem::hasProgressSummary)

            val entryTrackMap = getEntryTrackMap(distinctLibraryEntries)
            val scoredEntryTrackerMap = getScoredEntryTrackMap(entryTrackMap)

            val meanScore = getTrackMeanScore(scoredEntryTrackerMap)

            val overviewStatData = StatsData.Overview(
                libraryEntryCount = distinctLibraryEntries.size,
                completedEntryCount = distinctLibraryEntries.takeIf { hasCompleteProgressCoverage }?.count {
                    it.entry.status == EntryStatus.COMPLETED && it.unconsumedCount == 0L
                },
                totalReadDuration = getTotalReadDuration.await(),
            )

            val titlesStatData = StatsData.Titles(
                globalUpdateItemCount = getGlobalUpdateItemCount(libraryEntries),
                startedEntryCount = distinctLibraryEntries.takeIf { hasCompleteProgressCoverage }
                    ?.count { it.hasStarted == true },
                localEntryCount = distinctLibraryEntries.count { it.entry.source == LocalSource.ID },
            )

            val chaptersStatData = StatsData.Chapters(
                totalChapterCount = distinctLibraryEntries.takeIf { hasCompleteProgressCoverage }
                    ?.sumOf { checkNotNull(it.totalCount) }
                    ?.toInt(),
                readChapterCount = distinctLibraryEntries.takeIf { hasCompleteProgressCoverage }
                    ?.sumOf { checkNotNull(it.consumedCount) }
                    ?.toInt(),
                downloadCount = downloadRuntime.totalDownloadCount(),
            )

            val trackersStatData = StatsData.Trackers(
                trackedTitleCount = entryTrackMap.count { it.value.isNotEmpty() },
                meanScore = meanScore,
                trackerCount = loggedInTrackers.size,
            )

            mutableState.update {
                StatsScreenState.Success(
                    overview = overviewStatData,
                    titles = titlesStatData,
                    chapters = chaptersStatData,
                    trackers = trackersStatData,
                )
            }
        }
    }

    private fun getGlobalUpdateItemCount(libraryEntries: List<LibraryItem>): Int {
        val includedCategories = preferences.updateCategories.get().map { it.toLong() }
        val excludedCategories = preferences.updateCategoriesExclude.get().map { it.toLong() }
        return libraryEntries.filter {
            val included = includedCategories.isEmpty() || it.categories.intersect(includedCategories).isNotEmpty()
            val excluded = it.categories.intersect(excludedCategories).isNotEmpty()
            included && !excluded
        }
            .count {
                entryUpdateEligibility.evaluate(
                    EntryUpdateEligibilityRequest(
                        entry = it.entry,
                        totalCount = it.totalCount,
                        unconsumedCount = it.unconsumedCount,
                        hasStarted = it.hasStarted,
                    ),
                ) is EntryUpdateEligibility.Eligible
            }
    }

    private suspend fun getEntryTrackMap(libraryEntries: List<LibraryItem>): Map<LibraryItemKey, List<EntryTrack>> {
        val loggedInTrackerIds = loggedInTrackers.map { it.id }.toHashSet()
        return libraryEntries.associate { item ->
            val tracks = getTracks.await(item.entry.id)
                .fastFilter { it.trackerId in loggedInTrackerIds }

            item.key to tracks
        }
    }

    private fun getScoredEntryTrackMap(
        entryTrackMap: Map<LibraryItemKey, List<EntryTrack>>,
    ): Map<LibraryItemKey, List<EntryTrack>> {
        return entryTrackMap.mapNotNull { (entryId, tracks) ->
            val trackList = tracks.mapNotNull { track ->
                track.takeIf { it.score > 0.0 }
            }
            if (trackList.isEmpty()) return@mapNotNull null
            entryId to trackList
        }.toMap()
    }

    private fun getTrackMeanScore(scoredEntryTrackMap: Map<LibraryItemKey, List<EntryTrack>>): Double {
        return scoredEntryTrackMap
            .map { (_, tracks) ->
                tracks.map(::get10PointScore).average()
            }
            .fastFilter { !it.isNaN() }
            .average()
    }

    private fun get10PointScore(track: EntryTrack): Double {
        val service = trackerManager.get(track.trackerId)!!
        return service.get10PointScore(track)
    }
}
