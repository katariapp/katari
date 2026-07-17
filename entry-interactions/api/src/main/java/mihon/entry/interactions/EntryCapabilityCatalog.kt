package mihon.entry.interactions

/** Fundamental product capabilities reviewed in the Phase 0 capability atlas. */
object EntryCapabilityCatalog {
    val OPEN = typeWide("open")
    val CONTINUE = typeWide("continue")
    val DOWNLOADS = typeWide("downloads")
    val BULK_DOWNLOADS = typeWide("bulk-downloads")
    val DOWNLOAD_ARCHIVE_PACKAGING = typeWide("download.archive-packaging")
    val DOWNLOAD_TALL_IMAGE_SPLITTING = typeWide("download.tall-image-splitting")
    val DOWNLOAD_PARALLEL_SOURCE_TRANSFERS = typeWide("download.parallel-source-transfers")
    val DOWNLOAD_PARALLEL_ITEM_TRANSFERS = typeWide("download.parallel-item-transfers")
    val DOWNLOAD_OPTIONS = contextual("download-options")
    val CONSUMPTION = typeWide("consumption")
    val BOOKMARKING = typeWide("bookmarking")
    val PROGRESS = typeWide("progress")
    val PLAYBACK_PREFERENCES = typeWide("playback-preferences")
    val LIBRARY_PROGRESS = typeWide("library-progress")
    val CHILD_LIST = typeWide("child-list")
    val CHILD_GROUP_FILTERING = typeWide("child-group-filtering")
    val OUTSIDE_RELEASE_PERIOD_FILTERING = typeWide("outside-release-period-filtering")
    val MERGE = typeWide("merge")
    val MIGRATION = typeWide("migration")
    val PREVIEW = contextual("preview")
    val IMMERSIVE = contextual("immersive")

    val capabilities: List<EntryFundamentalCapability> = listOf(
        OPEN,
        CONTINUE,
        DOWNLOADS,
        BULK_DOWNLOADS,
        DOWNLOAD_ARCHIVE_PACKAGING,
        DOWNLOAD_TALL_IMAGE_SPLITTING,
        DOWNLOAD_PARALLEL_SOURCE_TRANSFERS,
        DOWNLOAD_PARALLEL_ITEM_TRANSFERS,
        DOWNLOAD_OPTIONS,
        CONSUMPTION,
        BOOKMARKING,
        PROGRESS,
        PLAYBACK_PREFERENCES,
        LIBRARY_PROGRESS,
        CHILD_LIST,
        CHILD_GROUP_FILTERING,
        OUTSIDE_RELEASE_PERIOD_FILTERING,
        MERGE,
        MIGRATION,
        PREVIEW,
        IMMERSIVE,
    ).sortedBy { it.id.value }

    init {
        check(capabilities.map { it.id }.distinct().size == capabilities.size) {
            "Capability catalog ids must be unique"
        }
    }

    private fun typeWide(id: String): EntryFundamentalCapability {
        return EntryFundamentalCapability(EntryCapabilityId(id), EntryCapabilityScope.TYPE_WIDE)
    }

    private fun contextual(id: String): EntryFundamentalCapability {
        return EntryFundamentalCapability(EntryCapabilityId(id), EntryCapabilityScope.CONTEXTUAL)
    }
}
