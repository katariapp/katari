package mihon.entry.interactions

import eu.kanade.tachiyomi.source.entry.EntryType
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import mihon.feature.graph.CapabilityDefinition
import mihon.feature.graph.CapabilityExpression
import mihon.feature.graph.ContributionOwner
import mihon.feature.graph.FeatureArtifactId
import mihon.feature.graph.FeatureContribution
import mihon.feature.graph.FeatureGraphContributor
import mihon.feature.graph.FeatureId
import mihon.feature.graph.FeatureIntegration
import mihon.feature.graph.FeatureIntegrationId
import mihon.feature.graph.SharedFeatureConsequence
import mihon.feature.graph.featureGraphContributor
import org.junit.jupiter.api.Test
import tachiyomi.domain.entry.model.Entry
import tachiyomi.domain.entry.model.EntryChapter

class BookmarkDownloadVerticalContractTest {

    @Test
    fun `bookmark provider activates shared bookmark and bulk download behavior`() = runTest {
        val anime = entry()
        val normal = chapter(id = 11L, bookmark = false)
        val bookmarked = chapter(id = 12L, bookmark = true, read = true)
        val downloadProcessor = downloadProcessor()
        val bulkProcessor = mockk<EntryBulkDownloadCandidateProcessor> {
            every { type } returns EntryType.ANIME
            coEvery { resolveBulkDownloadCandidatePool(any(), any()) } answers {
                secondArg<List<EntryChapter>?>().orEmpty()
            }
        }
        val bookmarkProcessor = mockk<EntryBookmarkProcessor> {
            every { type } returns EntryType.ANIME
            every { canSetBookmarked(any(), any()) } returns true
            coEvery { setBookmarked(any(), any(), any()) } returns Unit
        }
        val plugin = object : EntryInteractionPlugin {
            override val type = EntryType.ANIME
            override val owner = ContributionOwner("test.anime")
            override val providerBindings = listOf(
                EntryDownloadCapability.bind(downloadProcessor),
                EntryBulkDownloadCandidateCapability.bind(bulkProcessor),
                EntryBookmarkCapability.bind(bookmarkProcessor),
            )
        }
        val interactions = createEntryInteractions(
            plugins = listOf(plugin),
            featureContributors = listOf(
                featureUsing(EntryDownloadCapability.definition),
                featureUsing(EntryBulkDownloadCandidateCapability.definition),
                featureUsing(EntryBookmarkCapability.definition),
            ),
        )

        val status = EntryBookmarkStatus(bookmarked = false)
        interactions.bookmark.canSetBookmarked(EntryType.ANIME, status, bookmarked = true) shouldBe true
        interactions.bookmark.setBookmarked(anime, listOf(normal), bookmarked = true)
        coVerify(exactly = 1) { bookmarkProcessor.setBookmarked(anime, listOf(normal), true) }

        interactions.download.resolveBulkDownloadCandidates(
            entry = anime,
            action = EntryBulkDownloadAction.bookmarked,
            candidates = listOf(normal, bookmarked),
        ) shouldBe EntryBulkDownloadCandidateResult.Supported(listOf(bookmarked))
        coVerify(exactly = 1) {
            bulkProcessor.resolveBulkDownloadCandidatePool(anime, listOf(normal, bookmarked))
        }
    }

    private fun downloadProcessor(): EntryDownloadProcessor {
        return mockk(relaxed = true) {
            every { type } returns EntryType.ANIME
            every { changes } returns emptyFlow()
            every { isInitializing } returns flowOf(false)
            every { isRunning } returns flowOf(false)
            every { queueState } returns flowOf(emptyList())
            every { events } returns emptyFlow()
            every { updates() } returns emptyFlow()
            every { queueStatusUpdates() } returns emptyFlow()
            every { queueProgressUpdates() } returns emptyFlow()
        }
    }

    private fun featureUsing(capability: CapabilityDefinition<*>): FeatureGraphContributor {
        val owner = ContributionOwner("test.feature.${capability.id.value}")
        val suffix = capability.id.value
        return featureGraphContributor(owner) {
            add(
                FeatureContribution(
                    feature = FeatureId("test.$suffix"),
                    owner = owner,
                    integrations = listOf(
                        FeatureIntegration(
                            id = FeatureIntegrationId("test.$suffix.integration"),
                            prerequisites = CapabilityExpression.Provided(capability),
                            sharedConsequences = listOf(
                                object : SharedFeatureConsequence {
                                    override val id = FeatureArtifactId("test.$suffix.consequence")
                                },
                            ),
                        ),
                    ),
                ),
            )
        }
    }

    private fun entry(): Entry = Entry.create().copy(
        id = 1L,
        profileId = 7L,
        source = 10L,
        type = EntryType.ANIME,
    )

    private fun chapter(id: Long, bookmark: Boolean, read: Boolean = false): EntryChapter {
        return EntryChapter.create().copy(
            id = id,
            entryId = 1L,
            chapterNumber = id.toDouble(),
            sourceOrder = id,
            bookmark = bookmark,
            read = read,
        )
    }
}
