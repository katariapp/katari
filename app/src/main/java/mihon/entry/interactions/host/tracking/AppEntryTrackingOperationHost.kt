package mihon.entry.interactions.host.tracking

import eu.kanade.domain.track.interactor.RefreshTracks
import eu.kanade.domain.track.model.toDbTrack
import eu.kanade.tachiyomi.data.track.DeletableTracker
import eu.kanade.tachiyomi.data.track.EnhancedTracker
import eu.kanade.tachiyomi.data.track.TrackerManager
import eu.kanade.tachiyomi.data.track.model.TrackSearch
import mihon.entry.interactions.EntryTrackingMutation
import mihon.entry.interactions.EntryTrackingSearchCandidate
import mihon.entry.interactions.EntryTrackingServiceId
import tachiyomi.domain.entry.model.Entry
import tachiyomi.domain.track.interactor.DeleteTrack
import tachiyomi.domain.track.model.EntryTrack

internal class AppEntryTrackingOperationHost(
    private val trackerManager: TrackerManager,
    private val refreshTracks: RefreshTracks,
    private val deleteTrack: DeleteTrack,
) : EntryTrackingOperationHost {

    override suspend fun refresh(entryId: Long): List<EntryTrackingHostRefreshFailure> {
        return refreshTracks.await(entryId).mapNotNull { (tracker, cause) ->
            tracker?.let {
                EntryTrackingHostRefreshFailure(
                    serviceId = it.id,
                    serviceName = it.name,
                    cause = cause,
                )
            }
        }
    }

    override suspend fun search(serviceId: Long, query: String): List<EntryTrackingSearchCandidate> {
        return requireService(serviceId).search(query).map(TrackSearch::toCandidate)
    }

    override suspend fun register(
        serviceId: Long,
        candidate: EntryTrackingSearchCandidate,
        entryId: Long,
        private: Boolean,
    ) {
        requireService(serviceId).register(candidate.toTrackSearch(serviceId, private), entryId)
    }

    override suspend fun registerAutomatically(serviceId: Long, entry: Entry): Boolean {
        val tracker = requireService(serviceId)
        val enhancedTracker = tracker as? EnhancedTracker ?: return false
        val match = enhancedTracker.match(entry) ?: return false
        tracker.register(match, entry.id)
        return true
    }

    override suspend fun mutate(serviceId: Long, track: EntryTrack, mutation: EntryTrackingMutation) {
        val tracker = requireService(serviceId)
        when (mutation) {
            is EntryTrackingMutation.Status -> tracker.setRemoteStatus(track.toDbTrack(), mutation.value)
            is EntryTrackingMutation.Progress -> tracker.setRemoteLastChapterRead(track.toDbTrack(), mutation.value)
            is EntryTrackingMutation.Score -> tracker.setRemoteScore(track.toDbTrack(), mutation.value)
            is EntryTrackingMutation.StartDate -> tracker.setRemoteStartDate(track.toDbTrack(), mutation.epochMillis)
            is EntryTrackingMutation.FinishDate -> tracker.setRemoteFinishDate(track.toDbTrack(), mutation.epochMillis)
            is EntryTrackingMutation.Private -> tracker.setRemotePrivate(track.toDbTrack(), mutation.enabled)
        }
    }

    override suspend fun unregister(entryId: Long, serviceId: Long) {
        deleteTrack.await(entryId, serviceId)
    }

    override suspend fun deleteRemote(serviceId: Long, track: EntryTrack) {
        val tracker = requireService(serviceId) as? DeletableTracker
            ?: error("Tracking service $serviceId does not support remote deletion")
        tracker.delete(track)
    }

    private fun requireService(serviceId: Long) = checkNotNull(trackerManager.get(serviceId)) {
        "Tracking service $serviceId is not registered"
    }
}

private fun TrackSearch.toCandidate() = EntryTrackingSearchCandidate(
    serviceId = EntryTrackingServiceId(tracker_id),
    localId = id,
    entryId = manga_id,
    remoteId = remote_id,
    libraryId = library_id,
    title = title,
    progress = progress,
    total = total,
    score = score,
    status = status,
    startDate = started_reading_date,
    finishDate = finished_reading_date,
    private = private,
    remoteUrl = tracking_url,
    authors = authors,
    artists = artists,
    coverUrl = cover_url,
    summary = summary,
    publishingStatus = publishing_status,
    publishingType = publishing_type,
    publicationStartDate = start_date,
)

private fun EntryTrackingSearchCandidate.toTrackSearch(
    serviceId: Long,
    private: Boolean,
) = TrackSearch.create(serviceId).also {
    it.id = localId
    it.manga_id = entryId
    it.remote_id = remoteId
    it.library_id = libraryId
    it.title = title
    it.progress = progress
    it.total = total
    it.score = score
    it.status = status
    it.started_reading_date = startDate
    it.finished_reading_date = finishDate
    it.private = private
    it.tracking_url = remoteUrl
    it.authors = authors
    it.artists = artists
    it.cover_url = coverUrl
    it.summary = summary
    it.publishing_status = publishingStatus
    it.publishing_type = publishingType
    it.start_date = publicationStartDate
}
