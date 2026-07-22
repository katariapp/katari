package eu.kanade.tachiyomi.ui.browse.catalog

import eu.kanade.tachiyomi.source.entry.EntryFilter
import eu.kanade.tachiyomi.source.entry.EntryFilterList
import eu.kanade.tachiyomi.source.entry.EntryItemOrientation
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import mihon.entry.interactions.EntryCatalogueFeature
import mihon.entry.interactions.EntryCatalogueFiltersResult
import mihon.entry.interactions.EntryCatalogueSourceInfo
import mihon.entry.interactions.EntryCatalogueSourceResolution
import org.junit.jupiter.api.Test

class CatalogFilterLoaderTest {
    @Test
    fun `loader resolves Feature filters and async loading state`() = runTest {
        val filters = EntryFilterList(EntryFilter.Header("Entry filter"))
        val feature = mockk<EntryCatalogueFeature> {
            every { source(1L) } returns EntryCatalogueSourceResolution.Available(
                EntryCatalogueSourceInfo(
                    id = 1L,
                    name = "Source",
                    language = "en",
                    supportedEntryTypes = null,
                    itemOrientation = EntryItemOrientation.VERTICAL,
                    supportsLatest = true,
                    usesAsyncFilters = true,
                ),
            )
            coEvery { filters(1L) } returns EntryCatalogueFiltersResult.Available(filters)
        }

        val loader = CatalogFilterLoader(feature)

        loader.usesAsyncFilters(1L) shouldBe true
        loader.load(1L).first().name shouldBe "Entry filter"
    }
}
