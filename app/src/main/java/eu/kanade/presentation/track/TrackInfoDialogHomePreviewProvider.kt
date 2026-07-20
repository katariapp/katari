package eu.kanade.presentation.track

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import eu.kanade.test.DummyTracker
import mihon.entry.interactions.EntryTrackingServiceCapabilities
import mihon.entry.interactions.EntryTrackingServiceDescriptor
import mihon.entry.interactions.EntryTrackingServiceId
import mihon.entry.interactions.EntryTrackingSessionService
import tachiyomi.domain.track.model.EntryTrack
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

internal class TrackInfoDialogHomePreviewProvider :
    PreviewParameterProvider<@Composable () -> Unit> {

    private val aTrack = EntryTrack(
        id = 1L,
        entryId = 2L,
        trackerId = 3L,
        remoteId = 4L,
        libraryId = null,
        title = "Manage Name On Tracker Site",
        progress = 2.0,
        total = 12L,
        status = 1L,
        score = 2.0,
        remoteUrl = "https://example.com",
        startDate = 0L,
        finishDate = 0L,
        private = false,
    )
    private val privateTrack = aTrack.copy(private = true)
    private val trackItemWithoutTrack = EntryTrackingSessionService(
        track = null,
        service = DummyTracker(
            id = 1L,
            name = "Example Tracker",
        ).descriptor(),
        displayScore = null,
    )
    private val trackItemWithTrack = EntryTrackingSessionService(
        track = aTrack,
        service = DummyTracker(
            id = 2L,
            name = "Example Tracker 2",
        ).descriptor(),
        displayScore = "2",
    )
    private val trackItemWithPrivateTrack = EntryTrackingSessionService(
        track = privateTrack,
        service = DummyTracker(
            id = 2L,
            name = "Example Tracker 2",
        ).descriptor(supportsPrivateTracking = true),
        displayScore = "2",
    )

    private val trackersWithAndWithoutTrack = @Composable {
        TrackInfoDialogHome(
            trackItems = listOf(
                trackItemWithoutTrack,
                trackItemWithTrack,
            ),
            dateFormat = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM),
            onStatusClick = {},
            onChapterClick = {},
            onScoreClick = {},
            onStartDateEdit = {},
            onEndDateEdit = {},
            onNewSearch = {},
            onOpenInBrowser = {},
            onRemoved = {},
            onCopyLink = {},
            onTogglePrivate = {},
        )
    }

    private val noTrackers = @Composable {
        TrackInfoDialogHome(
            trackItems = listOf(),
            dateFormat = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM),
            onStatusClick = {},
            onChapterClick = {},
            onScoreClick = {},
            onStartDateEdit = {},
            onEndDateEdit = {},
            onNewSearch = {},
            onOpenInBrowser = {},
            onRemoved = {},
            onCopyLink = {},
            onTogglePrivate = {},
        )
    }

    private val trackerWithPrivateTracking = @Composable {
        TrackInfoDialogHome(
            trackItems = listOf(trackItemWithPrivateTrack),
            dateFormat = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM),
            onStatusClick = {},
            onChapterClick = {},
            onScoreClick = {},
            onStartDateEdit = {},
            onEndDateEdit = {},
            onNewSearch = {},
            onOpenInBrowser = {},
            onRemoved = {},
            onCopyLink = {},
            onTogglePrivate = {},
        )
    }

    override val values: Sequence<@Composable () -> Unit>
        get() = sequenceOf(
            trackersWithAndWithoutTrack,
            noTrackers,
            trackerWithPrivateTracking,
        )
}

private fun DummyTracker.descriptor(
    supportsPrivateTracking: Boolean = false,
) = EntryTrackingServiceDescriptor(
    id = EntryTrackingServiceId(id),
    name = name,
    logoResource = getLogo(),
    capabilities = EntryTrackingServiceCapabilities(
        statuses = emptyList(),
        scores = getScoreList(),
        supportsReadingDates = supportsReadingDates,
        supportsPrivateTracking = supportsPrivateTracking,
        supportsRemoteDeletion = false,
        supportsAutomaticBinding = false,
    ),
)
