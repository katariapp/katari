package mihon.entry.interactions

import eu.kanade.tachiyomi.source.entry.EntryType
import eu.kanade.tachiyomi.source.entry.UnifiedSource
import eu.kanade.tachiyomi.source.entry.WebViewSource
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import tachiyomi.domain.entry.model.Entry
import tachiyomi.domain.source.service.SourceManager

class EntryWebViewFeatureTest {

    @Test
    fun `entry web view resolves canonical URL and headers through one result`() {
        val source = mockk<WebViewSource> {
            every { id } returns 7L
            every { getContentUrl(any()) } returns "https://example.test/entry"
            every { getWebViewHeaders() } returns mapOf("Authorization" to "token")
        }
        val sourceManager = mockk<SourceManager> { every { get(7L) } returns source }
        val feature = DefaultEntryWebViewFeature(
            sourceFeatureEvaluation(EntryWebViewFeatureContributor),
            sourceManager,
        )

        feature.resolveEntry(Entry.create().copy(source = 7L, type = EntryType.BOOK)) shouldBe
            EntryWebViewResolution.Available(
                sourceId = 7L,
                url = "https://example.test/entry",
                headers = mapOf("Authorization" to "token"),
            )
        feature.resolveHeaders(7L) shouldBe
            EntryWebViewHeadersResolution.Available(mapOf("Authorization" to "token"))
    }

    @Test
    fun `missing unsupported and failed web view sources remain distinct`() {
        val unsupported = mockk<UnifiedSource>()
        val error = IllegalStateException("URL failure")
        val failed = mockk<WebViewSource> {
            every { id } returns 2L
            every { getContentUrl(any()) } throws error
        }
        val sourceManager = mockk<SourceManager> {
            every { get(0L) } returns null
            every { get(1L) } returns unsupported
            every { get(2L) } returns failed
        }
        val feature = DefaultEntryWebViewFeature(
            sourceFeatureEvaluation(EntryWebViewFeatureContributor),
            sourceManager,
        )

        feature.resolveEntry(Entry.create().copy(source = 0L)) shouldBe EntryWebViewResolution.Missing(0L)
        feature.resolveEntry(Entry.create().copy(source = 1L)) shouldBe EntryWebViewResolution.Unsupported(1L)
        feature.resolveEntry(Entry.create().copy(source = 2L)) shouldBe EntryWebViewResolution.Failed(2L, error)
    }
}
