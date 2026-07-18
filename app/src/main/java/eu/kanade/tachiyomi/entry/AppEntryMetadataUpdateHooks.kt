package eu.kanade.tachiyomi.entry

import mihon.entry.interactions.EntryDownloadMaintenanceFeature
import tachiyomi.domain.entry.model.Entry
import tachiyomi.domain.entry.service.EntryMetadataUpdateHooks

class AppEntryMetadataUpdateHooks(
    private val downloadMaintenance: EntryDownloadMaintenanceFeature,
) : EntryMetadataUpdateHooks {
    override suspend fun onTitleChanged(entry: Entry, newTitle: String) {
        downloadMaintenance.renameEntry(entry, newTitle)
    }
}
