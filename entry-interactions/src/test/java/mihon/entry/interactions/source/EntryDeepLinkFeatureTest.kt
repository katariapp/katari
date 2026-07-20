package mihon.entry.interactions

import eu.kanade.tachiyomi.source.entry.EntryType
import eu.kanade.tachiyomi.source.entry.EntryUriType
import eu.kanade.tachiyomi.source.entry.ResolvableSource
import eu.kanade.tachiyomi.source.entry.SEntry
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import tachiyomi.domain.entry.interactor.NetworkToLocalEntry
import tachiyomi.domain.entry.interactor.SyncEntryWithSource
import tachiyomi.domain.entry.model.Entry
import tachiyomi.domain.entry.repository.EntryChapterRepository
import tachiyomi.domain.source.service.SourceManager

class EntryDeepLinkFeatureTest {

    @Test
    fun `resolved persisted entry remains authoritative`() = runTest {
        val networkEntry = SEntry.create().apply {
            url = "/entry"
            title = "Network title"
            type = EntryType.ANIME
        }
        val persistedEntry = Entry.create().copy(
            id = 9L,
            source = 4L,
            title = "Persisted title",
            type = EntryType.BOOK,
        )
        val resolver = mockk<ResolvableSource> {
            every { id } returns 4L
            every { getUriType("https://example.test/entry") } returns EntryUriType.Entry
            coEvery { getEntry("https://example.test/entry") } returns networkEntry
        }
        val sourceManager = mockk<SourceManager> { every { getAll() } returns listOf(resolver) }
        val networkToLocalEntry = mockk<NetworkToLocalEntry> {
            coEvery { this@mockk.invoke(any<Entry>()) } returns persistedEntry
        }
        val feature = DefaultEntryDeepLinkFeature(
            evaluation = sourceFeatureEvaluation(EntryDeepLinkFeatureContributor),
            sourceManager = sourceManager,
            networkToLocalEntry = networkToLocalEntry,
            entryChapterRepository = mockk(),
            syncEntryWithSource = mockk(),
        )

        feature.resolve("https://example.test/entry") shouldBe
            EntryDeepLinkResolution.Resolved(persistedEntry)
    }

    @Test
    fun `no resolver no match and resolver failure are distinct outcomes`() = runTest {
        val sourceManager = mockk<SourceManager>()
        val feature = feature(sourceManager)

        every { sourceManager.getAll() } returns emptyList()
        feature.resolve("unknown") shouldBe EntryDeepLinkResolution.NoMatch

        val resolver = mockk<ResolvableSource> {
            every { getUriType("unknown") } returns EntryUriType.Unknown
        }
        every { sourceManager.getAll() } returns listOf(resolver)
        feature.resolve("unknown") shouldBe EntryDeepLinkResolution.NoMatch

        val error = IllegalStateException("resolver failure")
        every { resolver.getUriType("unknown") } throws error
        feature.resolve("unknown") shouldBe EntryDeepLinkResolution.Failed(error)
    }

    private fun feature(sourceManager: SourceManager) = DefaultEntryDeepLinkFeature(
        evaluation = sourceFeatureEvaluation(EntryDeepLinkFeatureContributor),
        sourceManager = sourceManager,
        networkToLocalEntry = mockk<NetworkToLocalEntry>(),
        entryChapterRepository = mockk<EntryChapterRepository>(),
        syncEntryWithSource = mockk<SyncEntryWithSource>(),
    )
}
