package mihon.entry.interactions

import android.app.Activity
import android.content.Context
import kotlinx.coroutines.flow.Flow
import tachiyomi.domain.entry.model.EntryChapter
import java.io.File

interface EntryPageImageCache {
    fun isImageInCache(imageUrl: String): Boolean
    fun getImageFile(imageUrl: String): File
}

object EntryMediaCacheBucketKeys {
    const val MANGA_PAGE_IMAGE: String = "manga_page_image"
    const val ANIME_PLAYBACK: String = "anime_playback"
    const val BOOK_MATERIALIZED: String = "book_materialized"
}

interface EntryMediaCacheBucket {
    val key: String
    val readableSize: String
    fun clear(): Int
}

interface EntryMediaCacheMaintenance {
    fun buckets(): List<EntryMediaCacheBucket>
    fun bucket(key: String): EntryMediaCacheBucket?
    fun clear(key: String): Int
}

fun interface EntryInteractionActivityTheme {
    /** Applies the host application's current theme before the activity creates any UI. */
    fun apply(activity: Activity)
}

interface EntryReaderIncognitoState {
    fun isIncognito(sourceId: Long?): Boolean
}

interface EntryReaderTracking {
    suspend fun updateChapterRead(context: Context, entryId: Long, chapterNumber: Double)
}

interface EntryPlayerCache : EntryMediaCacheBucket {
    override val key: String
        get() = EntryMediaCacheBucketKeys.ANIME_PLAYBACK
}

interface EntryChildGroupFilterDataSource {
    fun childrenChanged(entryIds: Collection<Long>): Flow<Unit>
    suspend fun children(entryIds: Collection<Long>): List<EntryChapter>
    fun excludedGroupsChanged(profileId: Long?, entryIds: Collection<Long>): Flow<Unit>
    suspend fun excludedGroups(profileId: Long?, entryIds: Collection<Long>): Map<Long, Set<String>>
    suspend fun setExcludedGroups(profileId: Long?, entryIds: Collection<Long>, excluded: Set<String>)
}
