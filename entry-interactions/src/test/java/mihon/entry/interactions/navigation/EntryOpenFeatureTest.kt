package mihon.entry.interactions

import android.app.PendingIntent
import android.content.Context
import eu.kanade.tachiyomi.source.entry.EntryType
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.nulls.shouldBeNull
import io.mockk.mockk
import mihon.feature.graph.ContributionOwner
import org.junit.jupiter.api.Test
import tachiyomi.domain.entry.model.Entry
import tachiyomi.domain.entry.model.EntryChapter

class EntryOpenFeatureTest {
    private val context = mockk<Context>(relaxed = true)
    private val chapter = EntryChapter.create().copy(id = 12L)

    @Test
    fun `a contributed Open provider activates shared dispatch automatically`() {
        val opened = mutableListOf<Long>()
        val processor = RecordingOpenProcessor(EntryType.BOOK, opened)
        val composition = createEntryInteractionComposition(
            plugins = listOf(plugin(EntryType.BOOK, EntryOpenCapability.bind(processor))),
            featureContributors = listOf(EntryOpenFeatureContributor),
        )
        val feature = DefaultEntryOpenFeature(
            evaluation = composition.featureGraphEvaluation,
            interaction = composition.interactions.open,
        )
        val entry = Entry.create().copy(id = 7L, type = EntryType.BOOK)

        feature.isApplicable(entry.type).shouldBeTrue()
        feature.open(context, entry, chapter).shouldBeTrue()

        opened.shouldContainExactly(entry.id)
    }

    @Test
    fun `missing Open provider is valid and exposes no Open action`() {
        val composition = createEntryInteractionComposition(
            plugins = emptyList(),
            featureContributors = listOf(EntryOpenFeatureContributor),
        )
        val feature = DefaultEntryOpenFeature(
            evaluation = composition.featureGraphEvaluation,
            interaction = composition.interactions.open,
        )
        val entry = Entry.create().copy(id = 7L, type = EntryType.BOOK)

        feature.isApplicable(entry.type).shouldBeFalse()
        feature.open(context, entry, chapter).shouldBeFalse()
        feature.pendingIntent(context, entry, chapter).shouldBeNull()
    }

    private fun plugin(
        type: EntryType,
        vararg bindings: EntryInteractionProviderBinding<*>,
    ): EntryInteractionPlugin {
        return object : EntryInteractionPlugin {
            override val type = type
            override val owner = ContributionOwner("test.type.${type.name.lowercase()}")
            override val providerBindings = bindings.toList()
        }
    }

    private class RecordingOpenProcessor(
        override val type: EntryType,
        private val opened: MutableList<Long>,
    ) : EntryOpenProcessor {
        override fun open(context: Context, entry: Entry, chapter: EntryChapter, options: EntryOpenOptions) {
            opened += entry.id
        }

        override fun pendingIntent(
            context: Context,
            entry: Entry,
            chapter: EntryChapter,
            options: EntryOpenOptions,
        ): PendingIntent = mockk(relaxed = true)
    }
}
