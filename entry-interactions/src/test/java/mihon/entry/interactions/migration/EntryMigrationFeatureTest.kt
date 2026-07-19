package mihon.entry.interactions

import eu.kanade.tachiyomi.source.entry.EntryType
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.test.runTest
import mihon.entry.interactions.host.EntryMigrationExecutionHost
import mihon.entry.interactions.host.EntryMigrationExecutionInspectionResult
import mihon.entry.interactions.host.EntryMigrationExecutionProfileHost
import mihon.entry.interactions.host.EntryMigrationHostInspectionResult
import mihon.entry.interactions.host.EntryMigrationHostOperation
import mihon.entry.interactions.host.EntryMigrationHostReplayResult
import mihon.entry.interactions.host.EntryMigrationHostTransition
import mihon.entry.interactions.host.EntryMigrationHostTransitionResult
import mihon.entry.interactions.host.EntryMigrationPreparationHost
import mihon.entry.interactions.host.EntryMigrationPreparationProfileHost
import mihon.entry.interactions.host.EntryMigrationTargetSynchronizationResult
import mihon.feature.graph.ContributionOwner
import org.junit.jupiter.api.Test
import tachiyomi.domain.entry.model.Entry
import tachiyomi.domain.entry.model.EntryChapter

class EntryMigrationFeatureTest {
    private val source = entry(id = 10, sourceId = 100, favorite = true, dateAdded = 50)
    private val target = entry(id = 20, sourceId = 200, favorite = false)
    private val sourceChild = child(id = 11, entryId = source.id, read = true, bookmark = false, dateFetch = 90)
    private val targetChild = child(id = 21, entryId = target.id, read = false, bookmark = true, dateFetch = 10)

    @Test
    fun `provider absence is valid and makes migration unavailable`() {
        val feature = feature(RecordingMigrationHost(source, target), bindings = emptyList())

        feature.availability(source) shouldBe
            EntryMigrationAvailability.Unavailable(EntryMigrationRejection.UNSUPPORTED_SOURCE_TYPE)
    }

    @Test
    fun `preparation derives options from current state and optional relationships`() = runTest {
        val preparedSource = source.copy(notes = "keep")
        val host = RecordingMigrationHost(preparedSource, target).apply {
            categories = listOf(3, 7)
        }
        val feature = feature(
            host,
            bindings = listOf(
                EntryMigrationCapability.bind(MigrationProvider()),
                EntryConsumptionCapability.bind(ConsumptionProvider()),
                EntryBookmarkCapability.bind(BookmarkProvider()),
            ),
        )

        val result = feature.prepare(EntryMigrationPrepareIntent(preparedSource, target))
            .shouldBeInstanceOf<EntryMigrationPreparationResult.Ready>()

        result.availableOptions.shouldContainExactlyInAnyOrder(
            EntryMigrationOption.CHILD_STATE,
            EntryMigrationOption.CATEGORIES,
            EntryMigrationOption.NOTES,
        )
    }

    @Test
    fun `replace commits shared primary state and delegates merge membership`() = runTest {
        val preparedSource = source.copy(notes = "keep", chapterFlags = 42)
        val host = RecordingMigrationHost(preparedSource, target).apply {
            categories = listOf(3, 7)
            sourceChildren = listOf(sourceChild)
            targetChildren = listOf(targetChild)
        }
        val merge = RecordingMergeMigrationFeature()
        val feature = feature(
            host,
            merge,
            listOf(
                EntryMigrationCapability.bind(MigrationProvider()),
                EntryConsumptionCapability.bind(ConsumptionProvider()),
                EntryBookmarkCapability.bind(BookmarkProvider()),
            ),
        )
        val preparation = feature.prepare(EntryMigrationPrepareIntent(preparedSource, target))
            .shouldBeInstanceOf<EntryMigrationPreparationResult.Ready>()

        val result = feature.execute(
            EntryMigrationExecuteIntent(
                preparation.reference,
                EntryMigrationMode.REPLACE,
                setOf(EntryMigrationOption.CHILD_STATE, EntryMigrationOption.CATEGORIES, EntryMigrationOption.NOTES),
            ),
        ).shouldBeInstanceOf<EntryMigrationExecutionResult.Applied>()

        result.outcome.followUp shouldBe EntryMigrationFollowUp.COMPLETE
        host.synchronizations shouldBe 1
        val transition = host.transitions.single()
        transition.sourceUpdate shouldBe preparedSource.copy(favorite = false, dateAdded = 0)
        transition.targetUpdate shouldBe target.copy(
            favorite = true,
            chapterFlags = 42,
            dateAdded = 50,
            notes = "keep",
        )
        transition.targetCategoryIds.shouldContainExactly(3, 7)
        transition.childUpdates.single().updated shouldBe targetChild.copy(
            read = true,
            bookmark = false,
            dateFetch = 90,
        )
        merge.intents.single() shouldBe EntryMergeMigrationReplacementIntent(preparedSource, target)
    }

    @Test
    fun `committed operation replay bypasses changed source state and target synchronization`() = runTest {
        val host = RecordingMigrationHost(source, target)
        val feature = feature(host)
        val preparation = feature.prepare(EntryMigrationPrepareIntent(source, target))
            .shouldBeInstanceOf<EntryMigrationPreparationResult.Ready>()
        host.replayResult = EntryMigrationHostReplayResult.Applied(hasPendingConsequences = true)
        host.preparationSource = source.copy(favorite = false)

        val result = feature.execute(
            EntryMigrationExecuteIntent(preparation.reference, EntryMigrationMode.REPLACE, emptySet()),
        ).shouldBeInstanceOf<EntryMigrationExecutionResult.Applied>()

        result.outcome.followUp shouldBe EntryMigrationFollowUp.INCOMPLETE
        host.synchronizations shouldBe 0
        host.transitions shouldBe emptyList()
    }

    @Test
    fun `strict synchronization failure cannot be reported as applied`() = runTest {
        val host = RecordingMigrationHost(source, target).apply {
            synchronizationResult = EntryMigrationTargetSynchronizationResult.OperationalFailure(retryable = false)
        }
        val feature = feature(host)
        val preparation = feature.prepare(EntryMigrationPrepareIntent(source, target))
            .shouldBeInstanceOf<EntryMigrationPreparationResult.Ready>()

        feature.execute(
            EntryMigrationExecuteIntent(preparation.reference, EntryMigrationMode.COPY, emptySet()),
        ) shouldBe EntryMigrationExecutionResult.OperationalFailure(retryable = false)
        host.transitions shouldBe emptyList()
    }

    @Test
    fun `execution cancellation propagates`() = runTest {
        val host = RecordingMigrationHost(source, target)
        val feature = feature(host)
        val preparation = feature.prepare(EntryMigrationPrepareIntent(source, target))
            .shouldBeInstanceOf<EntryMigrationPreparationResult.Ready>()
        host.synchronizationFailure = CancellationException("cancelled")

        shouldThrow<CancellationException> {
            feature.execute(
                EntryMigrationExecuteIntent(preparation.reference, EntryMigrationMode.COPY, emptySet()),
            )
        }
    }

    private fun feature(
        host: RecordingMigrationHost,
        merge: RecordingMergeMigrationFeature = RecordingMergeMigrationFeature(),
        bindings: List<EntryInteractionProviderBinding<*>> = listOf(
            EntryMigrationCapability.bind(MigrationProvider()),
        ),
    ): EntryMigrationFeature {
        val composition = createEntryInteractionComposition(
            plugins = listOf(
                object : EntryInteractionPlugin {
                    override val type = EntryType.BOOK
                    override val owner = ContributionOwner("test.migration-type")
                    override val providerBindings = bindings
                },
            ),
            featureContributors = listOf(EntryMigrationFeatureContributor),
        )
        return DefaultEntryMigrationFeature(
            evaluation = composition.featureGraphEvaluation,
            preparationHost = host,
            executionHost = host,
            mergeMigration = merge,
            clockMillis = { 999 },
        )
    }

    private fun entry(id: Long, sourceId: Long, favorite: Boolean, dateAdded: Long = 0): Entry {
        return Entry.create().copy(
            id = id,
            profileId = 4,
            source = sourceId,
            url = "entry-$id",
            title = "Entry $id",
            favorite = favorite,
            dateAdded = dateAdded,
            type = EntryType.BOOK,
        )
    }

    private fun child(
        id: Long,
        entryId: Long,
        read: Boolean,
        bookmark: Boolean,
        dateFetch: Long,
    ): EntryChapter {
        return EntryChapter.create().copy(
            id = id,
            entryId = entryId,
            url = "child-$id",
            name = "Child",
            chapterNumber = 1.0,
            read = read,
            bookmark = bookmark,
            dateFetch = dateFetch,
        )
    }

    private class MigrationProvider : EntryMigrationProvider {
        override val type = EntryType.BOOK
    }

    private class ConsumptionProvider : EntryConsumptionProcessor {
        override val type = EntryType.BOOK

        override suspend fun setConsumed(
            entry: Entry,
            chapters: List<EntryChapter>,
            consumed: Boolean,
        ): List<EntryChapter> = emptyList()
    }

    private class BookmarkProvider : EntryBookmarkProcessor {
        override val type = EntryType.BOOK

        override suspend fun setBookmarked(entry: Entry, chapters: List<EntryChapter>, bookmarked: Boolean) = Unit
    }
}

private class RecordingMergeMigrationFeature : EntryMergeMigrationFeature {
    val intents = mutableListOf<EntryMergeMigrationReplacementIntent>()

    override suspend fun participateInReplacementTransaction(
        intent: EntryMergeMigrationReplacementIntent,
    ): EntryMergeMigrationReplacementResult {
        intents += intent
        return EntryMergeMigrationReplacementResult.Applied
    }
}

private class RecordingMigrationHost(
    source: Entry,
    private val target: Entry,
) : EntryMigrationPreparationHost, EntryMigrationExecutionHost {
    var preparationSource = source
    var categories = emptyList<Long>()
    var sourceChildren = emptyList<EntryChapter>()
    var targetChildren = emptyList<EntryChapter>()
    var replayResult: EntryMigrationHostReplayResult = EntryMigrationHostReplayResult.NotApplied
    var synchronizationResult: EntryMigrationTargetSynchronizationResult =
        EntryMigrationTargetSynchronizationResult.Synchronized
    var synchronizationFailure: Throwable? = null
    var transitionResult: EntryMigrationHostTransitionResult =
        EntryMigrationHostTransitionResult.Applied(replayed = false, hasPendingConsequences = false)
    var synchronizations = 0
    val transitions = mutableListOf<EntryMigrationHostTransition>()

    override fun profile(profileId: Long): ProfileHost = ProfileHost()

    inner class ProfileHost : EntryMigrationPreparationProfileHost, EntryMigrationExecutionProfileHost {
        override suspend fun inspectPair(
            sourceEntryId: Long,
            targetEntryId: Long,
        ): EntryMigrationHostInspectionResult {
            return EntryMigrationHostInspectionResult.Ready(
                source = preparationSource,
                target = target,
                sourceCategoryIds = categories,
                sourceHasCustomCover = false,
            )
        }

        override suspend fun replay(operation: EntryMigrationHostOperation): EntryMigrationHostReplayResult {
            return replayResult
        }

        override suspend fun synchronizeTarget(targetEntryId: Long): EntryMigrationTargetSynchronizationResult {
            synchronizations += 1
            synchronizationFailure?.let { throw it }
            return synchronizationResult
        }

        override suspend fun inspectExecution(
            sourceEntryId: Long,
            targetEntryId: Long,
        ): EntryMigrationExecutionInspectionResult.Ready {
            return EntryMigrationExecutionInspectionResult.Ready(
                source = preparationSource,
                target = target,
                sourceChildren = sourceChildren,
                targetChildren = targetChildren,
                sourceCategoryIds = categories,
                sourceTracks = emptyList(),
                preparedTracks = emptyList(),
            )
        }

        override suspend fun applyTransition(
            transition: EntryMigrationHostTransition,
            participateMergeReplacement: (suspend () -> EntryMergeMigrationReplacementResult)?,
        ): EntryMigrationHostTransitionResult {
            transitions += transition
            participateMergeReplacement?.invoke()
            return transitionResult
        }
    }
}
