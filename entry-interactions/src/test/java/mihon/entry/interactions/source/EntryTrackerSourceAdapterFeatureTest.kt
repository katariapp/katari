package mihon.entry.interactions

import android.content.SharedPreferences
import eu.kanade.tachiyomi.source.entry.EntryImageSource
import eu.kanade.tachiyomi.source.entry.UnifiedSource
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import okhttp3.OkHttpClient
import org.junit.jupiter.api.Test
import tachiyomi.domain.source.service.SourceManager

class EntryTrackerSourceAdapterFeatureTest {

    @Test
    fun `tracker adapter composes only explicitly available source relationships`() {
        val preferences = mockk<SharedPreferences>()
        val client = mockk<OkHttpClient>()
        val source = mockk<EntryImageSource> {
            every { this@mockk.client } returns client
        }
        val settings = mockk<EntrySourceSettingsFeature> {
            every { resolve(5L) } returns EntrySourceSettingsResolution.Available(
                sourceId = 5L,
                preferences = preferences,
                populateScreen = {},
            )
        }
        val home = mockk<EntrySourceHomeFeature> {
            every { resolve(5L) } returns
                EntrySourceHomeResolution.Available(5L, "Source", "https://example.test")
        }
        val sourceManager = mockk<SourceManager> { every { get(5L) } returns source }
        val feature = DefaultEntryTrackerSourceAdapterFeature(
            evaluation = sourceFeatureEvaluation(EntryTrackerSourceAdapterFeatureContributor),
            sourceManager = sourceManager,
            settings = settings,
            home = home,
        )

        feature.resolve(5L) shouldBe EntryTrackerSourceAdapterResolution.Available(
            sourceId = 5L,
            preferences = preferences,
            homeUrl = "https://example.test",
            imageClient = client,
        )

        every { sourceManager.get(5L) } returns mockk<UnifiedSource>()
        feature.resolve(5L) shouldBe EntryTrackerSourceAdapterResolution.Unavailable(
            sourceId = 5L,
            reasons = setOf(EntryTrackerSourceAdapterUnavailableReason.IMAGE_CLIENT),
        )
    }
}
