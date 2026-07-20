package mihon.entry.interactions

import eu.kanade.tachiyomi.source.entry.EntryCatalogueSource
import eu.kanade.tachiyomi.source.entry.EntryFilterList
import eu.kanade.tachiyomi.source.entry.EntryItemOrientation
import eu.kanade.tachiyomi.source.entry.EntryItemOrientationProvider
import eu.kanade.tachiyomi.source.entry.EntryMedia
import eu.kanade.tachiyomi.source.entry.EntryPageResult
import eu.kanade.tachiyomi.source.entry.EntryType
import eu.kanade.tachiyomi.source.entry.PlaybackSelection
import eu.kanade.tachiyomi.source.entry.SEntry
import eu.kanade.tachiyomi.source.entry.SEntryChapter
import eu.kanade.tachiyomi.source.entry.SourceMetadata
import eu.kanade.tachiyomi.source.entry.UnifiedSource
import io.kotest.matchers.shouldBe
import mihon.feature.graph.ContributionOwner
import org.junit.jupiter.api.Test

class EntryCatalogueFeatureTest {

    @Test
    fun `catalogue facts are projected together through the Feature boundary`() {
        val feature = feature()
        val source = CatalogueSource()

        val result = feature.describe(source)

        result.language shouldBe "en"
        result.catalogue?.supportsLatest shouldBe true
        result.supportedEntryTypes shouldBe setOf(EntryType.MANGA)
        result.itemOrientation shouldBe EntryItemOrientation.HORIZONTAL
    }

    @Test
    fun `a source without a catalogue remains valid and receives descriptive defaults`() {
        val result = feature().describe(PlainSource())

        result.language shouldBe ""
        result.catalogue shouldBe null
        result.supportedEntryTypes shouldBe null
        result.itemOrientation shouldBe EntryItemOrientation.VERTICAL
    }

    private fun feature(): EntryCatalogueFeature {
        val plugin = object : EntryInteractionPlugin {
            override val type = EntryType.BOOK
            override val owner = ContributionOwner("test.future-type")
            override val providerBindings = emptyList<EntryInteractionProviderBinding<*>>()
        }
        val composition = createEntryInteractionComposition(
            plugins = listOf(plugin),
            featureContributors = listOf(EntryCatalogueFeatureContributor),
        )
        return DefaultEntryCatalogueFeature(composition.featureGraphEvaluation)
    }

    private open class PlainSource : UnifiedSource {
        override val id = 1L
        override val name = "Source"

        override suspend fun getPopularContent(page: Int): EntryPageResult<SEntry> = unused()
        override suspend fun getLatestUpdates(page: Int): EntryPageResult<SEntry> = unused()
        override suspend fun getSearchContent(
            page: Int,
            query: String,
            filters: EntryFilterList,
        ): EntryPageResult<SEntry> = unused()

        override suspend fun getContentDetails(entry: SEntry): SEntry = entry
        override suspend fun getChapterList(entry: SEntry): List<SEntryChapter> = emptyList()
        override suspend fun getMedia(chapter: SEntryChapter, selection: PlaybackSelection): EntryMedia = unused()

        private fun <T> unused(): T = error("Not used")
    }

    private class CatalogueSource : PlainSource(), EntryCatalogueSource, SourceMetadata, EntryItemOrientationProvider {
        override val lang = "en"
        override val supportsLatest = true
        override val supportedEntryTypes = setOf(EntryType.MANGA)
        override val itemOrientation = EntryItemOrientation.HORIZONTAL
    }
}
