package mihon.entry.interactions

import eu.kanade.tachiyomi.source.entry.EntryType
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.mockk
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import mihon.entry.interactions.host.EntryMergeHost
import mihon.entry.interactions.host.EntryMergeHostTransition
import mihon.entry.interactions.host.EntryMergeHostTransitionResult
import mihon.entry.interactions.host.EntryMergeMembershipSnapshot
import mihon.entry.interactions.host.EntryMergePendingConsequence
import mihon.entry.interactions.host.EntryMergeProfileHost
import mihon.feature.graph.ContributionOwner
import mihon.feature.graph.discoverAndAssembleFeatureGraph
import mihon.feature.graph.evaluateFeatureGraph
import org.junit.jupiter.api.Test
import tachiyomi.domain.entry.model.DuplicateEntryCandidate
import tachiyomi.domain.entry.model.Entry

class EntryMergeFeatureTest {
    @Test
    fun `migration replacement transfers one member and dissolves a depleted source group`() {
        val replacements = replacementGroups(
            currentEntryId = 2L,
            replacementEntryId = 3L,
            currentGroup = EntryMergeMembershipSnapshot(7L, 1L, listOf(1L, 2L)),
            replacementGroup = EntryMergeMembershipSnapshot(7L, 3L, listOf(3L, 4L)),
        )

        replacements shouldContainExactly listOf(
            EntryMergeMembershipSnapshot(7L, 1L, listOf(1L, 3L)),
        )
    }

    @Test
    fun `shared workflow prepares and commits Book entries without a type marker`() = runTest {
        val entries = listOf(entry(1L, "one"), entry(2L, "two"))
        val host = FakeEntryMergeHost(entries)
        val feature = feature(host)

        val ready = feature.prepare(EntryMergePrepareIntent(entries))
            .shouldBeInstanceOf<EntryMergePreparationResult.Ready>()
        val result = feature.execute(
            EntryMergeCommitIntent(
                editReference = ready.editor.editReference,
                target = ready.editor.target,
                orderedEntries = ready.editor.entries.map(EntryMergeEditorEntry::reference),
            ),
        )

        result.shouldBeInstanceOf<EntryMergeExecutionResult.Applied>()
            .outcome.followUp shouldBe EntryMergeFollowUp.COMPLETE
        host.transitions.single().shouldBeInstanceOf<EntryMergeHostTransition.CommitEditor>()
            .orderedEntries.size shouldBe 2
    }

    @Test
    fun `unpersisted selection remains read only until the owned commit transition`() = runTest {
        val persisted = entry(1L, "persisted")
        val remote = entry(-1L, "remote").copy(favorite = false)
        val host = FakeEntryMergeHost(listOf(persisted))
        val feature = feature(host)

        val ready = feature.prepare(
            EntryMergePrepareIntent(
                selectedEntries = listOf(persisted, remote),
                preparations = listOf(EntryMergeMemberPreparationIntent(remote, listOf(4L, 5L))),
            ),
        ).shouldBeInstanceOf<EntryMergePreparationResult.Ready>()

        host.transitions.shouldBeEmpty()

        feature.execute(
            EntryMergeCommitIntent(
                editReference = ready.editor.editReference,
                target = ready.editor.target,
                orderedEntries = ready.editor.entries.map(EntryMergeEditorEntry::reference),
            ),
        )

        val transition = host.transitions.single().shouldBeInstanceOf<EntryMergeHostTransition.CommitEditor>()
        transition.preparations.single().categoryIds shouldContainExactly listOf(4L, 5L)
        transition.expected.entries.single { it.persistedEntryId == null }.entry.url shouldBe remote.url
    }

    private fun feature(host: FakeEntryMergeHost): EntryMergeFeature {
        val plugin = object : EntryInteractionPlugin {
            override val type = EntryType.BOOK
            override val owner = ContributionOwner("test.book")
            override val providerBindings = emptyList<EntryInteractionProviderBinding<*>>()
        }
        val evaluation = evaluateFeatureGraph(
            discoverAndAssembleFeatureGraph(listOf(plugin, EntryMergeFeatureContributor)),
        )
        val delivery = EntryMergeConsequenceDelivery(
            host = host,
            libraryEntryInitializer = {},
            coverCleanup = {},
            downloadMaintenance = { mockk(relaxed = true) },
        )
        return EntryMergeWorkflowCoordinator(evaluation, host, delivery)
    }

    private fun entry(id: Long, suffix: String): Entry {
        return Entry.create().copy(
            id = id,
            profileId = 7L,
            type = EntryType.BOOK,
            source = 10L,
            url = "/$suffix",
            title = suffix,
            favorite = true,
        )
    }

    private class FakeEntryMergeHost(
        entries: List<Entry>,
    ) : EntryMergeHost {
        private val entriesById = entries.filter { it.id > 0L }.associateBy(Entry::id)
        val transitions = mutableListOf<EntryMergeHostTransition>()

        override fun profile(profileId: Long): EntryMergeProfileHost {
            return object : EntryMergeProfileHost {
                override val profileId = profileId

                override suspend fun entries(entryIds: List<Long>): List<Entry> = entryIds.mapNotNull(entriesById::get)

                override suspend fun resolveEntryIdentity(entry: Entry): Entry? {
                    return entriesById.values.firstOrNull {
                        it.profileId == profileId && it.type == entry.type && it.source == entry.source &&
                            it.url == entry.url
                    }
                }

                override suspend fun membership(entryId: Long): EntryMergeMembershipSnapshot? = null

                override fun observeMembership(entryId: Long): Flow<EntryMergeMembershipSnapshot?> = flowOf(null)

                override suspend fun memberships(): List<EntryMergeMembershipSnapshot> = emptyList()

                override fun observeMemberships(): Flow<List<EntryMergeMembershipSnapshot>> = flowOf(emptyList())

                override suspend fun duplicateCandidates(entry: Entry): List<DuplicateEntryCandidate> = emptyList()

                override fun observeDuplicateCandidates(
                    entry: Flow<Entry>,
                ): Flow<List<DuplicateEntryCandidate>> = emptyFlow()

                override suspend fun applyTransition(
                    transition: EntryMergeHostTransition,
                ): EntryMergeHostTransitionResult {
                    transitions += transition
                    val target = (transition as EntryMergeHostTransition.CommitEditor).target
                    val targetId = transition.expected.entries.single { it.key == target }.persistedEntryId ?: 99L
                    return EntryMergeHostTransitionResult.Applied(targetId)
                }
            }
        }

        override suspend fun pendingConsequences(limit: Int): List<EntryMergePendingConsequence> = emptyList()

        override suspend fun acknowledgeConsequence(consequenceId: String) = Unit

        override suspend fun recordConsequenceFailure(
            consequenceId: String,
            message: String,
            retryAtMillis: Long,
        ) = Unit

        override suspend fun pendingConsequenceCount(operationId: String): Long = 0L
    }
}
