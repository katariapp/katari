package mihon.entry.interactions

import tachiyomi.domain.entry.model.Entry

interface EntryWebViewFeature {
    fun resolveEntry(entry: Entry): EntryWebViewResolution
    fun resolveHeaders(sourceId: Long): EntryWebViewHeadersResolution
}

sealed interface EntryWebViewResolution {
    val sourceId: Long

    data class Available(
        override val sourceId: Long,
        val url: String,
        val headers: Map<String, String>,
    ) : EntryWebViewResolution

    data class Missing(override val sourceId: Long) : EntryWebViewResolution
    data class Unsupported(override val sourceId: Long) : EntryWebViewResolution
    data class Failed(override val sourceId: Long, val cause: Throwable) : EntryWebViewResolution
}

sealed interface EntryWebViewHeadersResolution {
    data class Available(val headers: Map<String, String>) : EntryWebViewHeadersResolution
    data object Missing : EntryWebViewHeadersResolution
    data object Unsupported : EntryWebViewHeadersResolution
    data class Failed(val cause: Throwable) : EntryWebViewHeadersResolution
}
