package mihon.entry.interactions

import eu.kanade.tachiyomi.source.entry.EntryType
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.shouldBe
import mihon.feature.graph.ContributionOwner
import mihon.feature.graph.discoverAndAssembleFeatureGraph
import mihon.feature.graph.evaluateFeatureGraph
import mihon.feature.graph.selectFeatureArtifacts
import org.junit.jupiter.api.Test
import tachiyomi.domain.entry.model.Entry

class EntryMigrationContributionTest {
    @Test
    fun `one migration provider selects every base consequence and behavior contract`() {
        val graph = discoverAndAssembleFeatureGraph(
            listOf(plugin(EntryMigrationCapability.bind(MigrationProvider())), EntryMigrationFeatureContributor),
        )
        val evaluation = evaluateFeatureGraph(graph)
        val artifacts = selectFeatureArtifacts(graph, evaluation)

        evaluation.sharedConsequences
            .filter { it.subject.feature == ENTRY_MIGRATION_FEATURE_ID }
            .map { it.consequence.id }
            .shouldContainExactlyInAnyOrder(EntryMigrationBaseConsequence.entries.map { it.id })
        artifacts.behavioralContracts.single().subject.contentType shouldBe EntryType.BOOK.toContentTypeId()
        artifacts.obligations shouldBe emptyList()
    }

    @Test
    fun `optional progress relationship requires both providers without changing base migration`() {
        val migrationOnly = evaluation(EntryMigrationCapability.bind(MigrationProvider()))
        val withProgress = evaluation(
            EntryMigrationCapability.bind(MigrationProvider()),
            EntryProgressCapability.bind(ProgressProvider()),
        )

        migrationOnly.sharedConsequences
            .any { it.consequence.id == EntryMigrationProgressConsequence.COPY.id } shouldBe false
        withProgress.sharedConsequences
            .single { it.consequence.id == EntryMigrationProgressConsequence.COPY.id }
            .subject.contentType shouldBe EntryType.BOOK.toContentTypeId()
        withProgress.sharedConsequences
            .filter { it.subject.integration == ENTRY_MIGRATION_BASE_INTEGRATION_ID }
            .map { it.consequence.id }
            .shouldContainExactlyInAnyOrder(EntryMigrationBaseConsequence.entries.map { it.id })
    }

    private fun evaluation(vararg bindings: EntryInteractionProviderBinding<*>) = evaluateFeatureGraph(
        discoverAndAssembleFeatureGraph(listOf(plugin(*bindings), EntryMigrationFeatureContributor)),
    )

    private fun plugin(vararg bindings: EntryInteractionProviderBinding<*>): EntryInteractionPlugin {
        return object : EntryInteractionPlugin {
            override val type = EntryType.BOOK
            override val owner = ContributionOwner("test.entry-migration")
            override val providerBindings = bindings.toList()
        }
    }

    private class MigrationProvider : EntryMigrationProvider {
        override val type = EntryType.BOOK
    }

    private class ProgressProvider : EntryProgressProcessor {
        override val type = EntryType.BOOK

        override suspend fun snapshot(entry: Entry) = EntryProgressSnapshot()

        override suspend fun restore(entry: Entry, snapshot: EntryProgressSnapshot) = Unit

        override suspend fun copy(
            sourceEntry: Entry,
            targetEntry: Entry,
            resourceMappings: List<EntryProgressResourceMapping>,
        ) = Unit
    }
}
