package mihon.entry.interactions

import eu.kanade.tachiyomi.source.entry.EntryType
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.types.shouldBeInstanceOf
import kotlinx.coroutines.test.runTest
import mihon.feature.graph.ContributionOwner
import org.junit.jupiter.api.Test
import tachiyomi.domain.entry.model.Entry

class EntryPlaybackPreferencesFeatureTest {
    private val sourceEntry = entry(id = 7L, type = EntryType.BOOK)
    private val targetEntry = entry(id = 8L, type = EntryType.BOOK)
    private val snapshot = EntryPlaybackPreferencesSnapshot(streamKey = "stream", updatedAt = 12L)

    @Test
    fun `provider absence is valid and every operation reports inapplicability`() = runTest {
        val feature = featureFor()

        feature.isApplicable(sourceEntry.type) shouldBe false
        feature.snapshot(sourceEntry) shouldBe EntryPlaybackPreferencesSnapshotResult.Inapplicable(sourceEntry.type)
        feature.restore(sourceEntry, snapshot) shouldBe
            EntryPlaybackPreferencesRestoreResult.Inapplicable(sourceEntry.type)
        feature.copy(sourceEntry, targetEntry) shouldBe
            EntryPlaybackPreferencesCopyResult.Inapplicable(setOf(sourceEntry.type))
        feature.copy(sourceEntry, entry(id = 9L, type = EntryType.ANIME)) shouldBe
            EntryPlaybackPreferencesCopyResult.TypeMismatch(EntryType.BOOK, EntryType.ANIME)
    }

    @Test
    fun `one provider activates backup restore and migration consequences`() = runTest {
        val processor = RecordingPlaybackPreferencesProcessor(snapshot)
        val feature = featureFor(EntryPlaybackPreferencesCapability.bind(processor))

        feature.isApplicable(sourceEntry.type) shouldBe true
        feature.snapshot(sourceEntry) shouldBe EntryPlaybackPreferencesSnapshotResult.Captured(snapshot)
        feature.restore(targetEntry, snapshot) shouldBe EntryPlaybackPreferencesRestoreResult.Applied
        feature.copy(sourceEntry, targetEntry) shouldBe EntryPlaybackPreferencesCopyResult.Copied

        processor.restored shouldBe (targetEntry to snapshot)
        processor.copied shouldBe (sourceEntry to targetEntry)
    }

    @Test
    fun `missing stored preferences are distinct from unsupported transfer`() = runTest {
        val processor = RecordingPlaybackPreferencesProcessor(snapshot = null, copyResult = false)
        val feature = featureFor(EntryPlaybackPreferencesCapability.bind(processor))

        feature.snapshot(sourceEntry) shouldBe EntryPlaybackPreferencesSnapshotResult.NoPreferences
        feature.copy(sourceEntry, targetEntry) shouldBe EntryPlaybackPreferencesCopyResult.NoPreferences
    }

    @Test
    fun `raw dispatch is strict because structured absence belongs to the feature`() = runTest {
        val composition = compositionFor()
        val interaction = composition.interactions.playbackPreferences

        val absentFailure = runCatching { interaction.snapshot(sourceEntry) }.exceptionOrNull()
        absentFailure.shouldBeInstanceOf<IllegalStateException>().message shouldContain
            "No playback preferences processor registered"

        val processor = RecordingPlaybackPreferencesProcessor(snapshot)
        val providedInteraction = compositionFor(EntryPlaybackPreferencesCapability.bind(processor))
            .interactions.playbackPreferences
        val mismatchFailure = runCatching {
            providedInteraction.copy(sourceEntry, entry(id = 9L, type = EntryType.ANIME))
        }.exceptionOrNull()
        mismatchFailure.shouldBeInstanceOf<IllegalArgumentException>().message shouldContain
            "requires matching Entry types"
    }

    private fun featureFor(
        vararg bindings: EntryInteractionProviderBinding<*>,
    ): EntryPlaybackPreferencesFeature {
        val composition = compositionFor(*bindings)
        return DefaultEntryPlaybackPreferencesFeature(
            evaluation = composition.featureGraphEvaluation,
            interaction = composition.interactions.playbackPreferences,
        )
    }

    private fun compositionFor(
        vararg bindings: EntryInteractionProviderBinding<*>,
    ): EntryInteractionComposition {
        return createEntryInteractionComposition(
            plugins = listOf(plugin(*bindings)),
            featureContributors = listOf(EntryPlaybackPreferencesFeatureContributor),
        )
    }

    private fun plugin(vararg bindings: EntryInteractionProviderBinding<*>): EntryInteractionPlugin {
        return object : EntryInteractionPlugin {
            override val type = EntryType.BOOK
            override val owner = ContributionOwner("test.playback-preferences-type")
            override val providerBindings = bindings.toList()
        }
    }

    private class RecordingPlaybackPreferencesProcessor(
        private val snapshot: EntryPlaybackPreferencesSnapshot?,
        private val copyResult: Boolean = true,
    ) : EntryPlaybackPreferencesProcessor {
        override val type = EntryType.BOOK
        var restored: Pair<Entry, EntryPlaybackPreferencesSnapshot>? = null
        var copied: Pair<Entry, Entry>? = null

        override suspend fun snapshot(entry: Entry): EntryPlaybackPreferencesSnapshot? = snapshot

        override suspend fun restore(entry: Entry, snapshot: EntryPlaybackPreferencesSnapshot) {
            restored = entry to snapshot
        }

        override suspend fun copy(sourceEntry: Entry, targetEntry: Entry): Boolean {
            copied = sourceEntry to targetEntry
            return copyResult
        }
    }

    private fun entry(id: Long, type: EntryType): Entry {
        return Entry.create().copy(id = id, type = type)
    }
}
