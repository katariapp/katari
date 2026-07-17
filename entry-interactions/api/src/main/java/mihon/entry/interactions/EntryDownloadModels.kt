package mihon.entry.interactions

import dev.icerock.moko.resources.StringResource
import eu.kanade.tachiyomi.source.entry.EntryType
import tachiyomi.domain.entry.model.Entry
import tachiyomi.domain.entry.model.EntryChapter

enum class EntryDownloadState(val value: Int) {
    NOT_DOWNLOADED(0),
    QUEUE(1),
    DOWNLOADING(2),
    DOWNLOADED(3),
    ERROR(4),
}

data class EntryDownloadStatus(
    val entryType: EntryType,
    val chapterId: Long,
    val state: EntryDownloadState,
    val progress: Int = 0,
)

data class EntryDownloadQueueGroup(
    val sourceId: Long,
    val sourceName: String,
    val entryType: EntryType,
    val items: List<EntryDownloadQueueItem>,
)

data class EntryDownloadIdentity(
    val profileId: Long,
    val entryType: EntryType,
    val entryId: Long,
    val sourceId: Long,
    val childId: Long,
) {
    companion object {
        fun from(entry: Entry, child: EntryChapter): EntryDownloadIdentity {
            require(child.entryId == entry.id) {
                "Download child ${child.id} belongs to entry ${child.entryId}, not ${entry.id}"
            }
            return EntryDownloadIdentity(
                profileId = entry.profileId,
                entryType = entry.type,
                entryId = entry.id,
                sourceId = entry.source,
                childId = child.id,
            )
        }
    }
}

data class EntryDownloadQueueItem(
    val identity: EntryDownloadIdentity,
    val state: EntryDownloadState,
    val title: String,
    val subtitle: String,
    val dateUpload: Long,
    val chapterNumber: Double,
    val progress: Int,
    val progressMax: Int,
    val progressText: String,
) {
    val profileId: Long get() = identity.profileId
    val entryType: EntryType get() = identity.entryType
    val entryId: Long get() = identity.entryId
    val sourceId: Long get() = identity.sourceId
    val childId: Long get() = identity.childId
}

sealed interface EntryDownloadMessage {
    data class Text(val value: String) : EntryDownloadMessage

    data class Resource(
        val resource: StringResource,
        val args: List<Any> = emptyList(),
    ) : EntryDownloadMessage
}

sealed interface EntryDownloadEvent {
    data class Error(
        val entryType: EntryType,
        val entryId: Long?,
        val title: String?,
        val subtitle: String?,
        val message: EntryDownloadMessage,
    ) : EntryDownloadEvent

    data class Warning(
        val message: EntryDownloadMessage,
        val timeoutMillis: Long? = null,
        val helpUrl: String? = null,
    ) : EntryDownloadEvent
}
