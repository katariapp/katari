package mihon.entry.interactions

import eu.kanade.tachiyomi.source.entry.EntryType

/** Shared download consequences derived from authoritative fundamental capability evidence. */
object EntryDownloadCapabilityPolicy {
    fun supportsBookmarkedBulkDownloads(
        capabilityReport: EntryCapabilityReport,
        entryType: EntryType,
    ): Boolean {
        return capabilityReport.supportsTypeWide(entryType, EntryCapabilityCatalog.BULK_DOWNLOADS) &&
            capabilityReport.supportsTypeWide(entryType, EntryCapabilityCatalog.BOOKMARKING)
    }

    fun supportsBookmarkedBulkDownloads(
        capabilityReport: EntryCapabilityReport,
        entryTypes: Iterable<EntryType>,
    ): Boolean {
        val selectedTypes = entryTypes.toSet()
        return selectedTypes.isNotEmpty() && selectedTypes.all {
            supportsBookmarkedBulkDownloads(capabilityReport, it)
        }
    }

    fun protectsBookmarkedDownloads(
        capabilityReport: EntryCapabilityReport,
        entryType: EntryType,
    ): Boolean {
        return capabilityReport.supportsTypeWide(entryType, EntryCapabilityCatalog.BOOKMARKING)
    }
}
