package mihon.entry.interactions.host.tracking

import eu.kanade.tachiyomi.data.track.DeletableTracker
import eu.kanade.tachiyomi.data.track.EnhancedTracker
import eu.kanade.tachiyomi.data.track.EntryTrackingSource
import eu.kanade.tachiyomi.data.track.Tracker
import eu.kanade.tachiyomi.data.track.TrackerManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import tachiyomi.domain.entry.model.Entry
import tachiyomi.domain.source.service.SourceManager
import tachiyomi.domain.track.interactor.GetTracks

class AppEntryTrackingHost(
    private val trackerManager: TrackerManager,
    private val sourceManager: SourceManager,
    private val getTracks: GetTracks,
) : EntryTrackingHost {

    override fun registeredServices(): List<EntryTrackingHostService> {
        return trackerManager.trackers.map(Tracker::toHostService)
    }

    override fun observeEntry(entry: Entry): Flow<EntryTrackingHostEntrySnapshot> {
        val source = sourceManager.getOrStub(entry.source)
        val trackingSource = EntryTrackingSource.from(source, sourceManager.getDisplayInfo(entry.source))
        return combine(
            getTracks.subscribe(entry.id),
            trackerManager.loggedInTrackersFlow(),
        ) { tracks, loggedInTrackers ->
            val tracksByService = tracks.associateBy { it.trackerId }
            val loggedInServiceIds = loggedInTrackers.mapTo(mutableSetOf(), Tracker::id)
            EntryTrackingHostEntrySnapshot(
                services = trackerManager.trackers.map { tracker ->
                    EntryTrackingHostEntryService(
                        service = tracker.toHostService(),
                        isLoggedIn = tracker.id in loggedInServiceIds,
                        acceptsSource = (tracker as? EnhancedTracker)?.accept(trackingSource) ?: true,
                        track = tracksByService[tracker.id],
                        displayScore = tracksByService[tracker.id]?.let(tracker::displayScore),
                    )
                },
            )
        }
    }
}

private fun Tracker.toHostService(): EntryTrackingHostService {
    return EntryTrackingHostService(
        id = id,
        name = name,
        logoResource = getLogo(),
        supportedEntryTypes = supportedEntryTypes,
        capabilities = EntryTrackingHostServiceCapabilities(
            statuses = getStatusList().map { status ->
                EntryTrackingHostStatus(status, getStatus(status))
            },
            scores = getScoreList(),
            supportsReadingDates = supportsReadingDates,
            supportsPrivateTracking = supportsPrivateTracking,
            supportsRemoteDeletion = this is DeletableTracker,
            supportsAutomaticBinding = this is EnhancedTracker,
        ),
    )
}
