package mihon.entry.interactions

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import eu.kanade.tachiyomi.source.entry.EntryType
import eu.kanade.tachiyomi.source.entry.VideoStream
import eu.kanade.tachiyomi.source.entry.VideoSubtitle

fun interface EntryImmersiveRenderer {
    @Composable
    fun Content(
        modifier: Modifier,
        active: Boolean,
        controlsVisible: Boolean,
        controlsBottomInset: Dp,
        onToggleControls: () -> Unit,
        onZoomStateChange: (Boolean) -> Unit,
        onProgress: (EntryImmersiveProgress) -> Unit,
    )
}

sealed interface EntryImmersiveProgress {
    data class ImagePage(
        val pageIndex: Int,
        val pageCount: Int,
        val sessionDurationMs: Long,
    ) : EntryImmersiveProgress

    data class Playback(
        val positionMs: Long,
        val durationMs: Long,
        val resetSession: Boolean = false,
    ) : EntryImmersiveProgress
}

sealed interface EntryImmersiveHandle {
    val entryType: EntryType
    val chapterId: Long

    data class ImagePages(
        override val entryType: EntryType,
        override val chapterId: Long,
        val delegate: Any,
    ) : EntryImmersiveHandle

    data class Playback(
        override val entryType: EntryType,
        override val chapterId: Long,
        val stream: VideoStream,
        val subtitles: List<VideoSubtitle>,
        val resumePositionMs: Long,
        val delegate: Any? = null,
    ) : EntryImmersiveHandle
}
