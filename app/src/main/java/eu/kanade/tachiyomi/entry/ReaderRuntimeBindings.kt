package eu.kanade.tachiyomi.entry

import android.content.Context
import eu.kanade.domain.source.interactor.GetIncognitoState
import eu.kanade.domain.track.service.TrackPreferences
import mihon.entry.interactions.EntryReaderIncognitoState
import mihon.entry.interactions.EntryReaderTracking
import mihon.entry.interactions.EntryTrackingFeature
import tachiyomi.domain.entry.interactor.GetEntry

class AppReaderIncognitoState(
    private val getIncognitoState: GetIncognitoState,
) : EntryReaderIncognitoState {
    override fun isIncognito(sourceId: Long?): Boolean {
        return getIncognitoState.await(sourceId)
    }
}

class AppReaderTracking(
    private val trackPreferences: TrackPreferences,
    private val getEntry: GetEntry,
    private val trackingFeature: () -> EntryTrackingFeature,
) : EntryReaderTracking {
    override fun isAutomaticTrackingEnabled(): Boolean = trackPreferences.autoUpdateTrack.get()

    override suspend fun updateChapterRead(context: Context, entryId: Long, chapterNumber: Double) {
        if (!isAutomaticTrackingEnabled()) return

        val entry = getEntry.await(entryId) ?: return
        trackingFeature().synchronizeProgress(entry, chapterNumber)
    }
}
