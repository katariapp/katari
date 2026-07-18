package mihon.entry.interactions

import eu.kanade.tachiyomi.source.entry.EntryType
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import mihon.feature.graph.ContributionOwner
import org.junit.jupiter.api.Test
import tachiyomi.i18n.MR

class EntryTypePresentationFeatureTest {

    @Test
    fun `discovered provider is projected without a feature type list`() {
        val projected = genericEntryTypePresentation.copy(displayNameLabel = MR.strings.entry_type_book)
        val provider = object : EntryTypePresentationProvider {
            override val type = EntryType.BOOK
            override val presentation = projected
        }
        val feature = feature(
            object : EntryInteractionPlugin {
                override val type = EntryType.BOOK
                override val owner = ContributionOwner("test.synthetic")
                override val providerBindings = listOf(EntryTypePresentationCapability.bind(provider))
            },
        )

        val result = feature.presentation(EntryType.BOOK)

        result.shouldBeInstanceOf<EntryTypePresentationResult.Contributed>()
        result.presentation shouldBe projected
    }

    @Test
    fun `provider absence stays valid and resolves as observable generic vocabulary`() {
        val feature = feature(
            object : EntryInteractionPlugin {
                override val type = EntryType.BOOK
                override val owner = ContributionOwner("test.partial")
                override val providerBindings = emptyList<EntryInteractionProviderBinding<*>>()
            },
        )

        val concrete = feature.presentation(EntryType.BOOK)
            .shouldBeInstanceOf<EntryTypePresentationResult.Generic>()
        val mixed = feature.presentation(null)
            .shouldBeInstanceOf<EntryTypePresentationResult.Generic>()

        concrete.type shouldBe EntryType.BOOK
        concrete.presentation shouldBe genericEntryTypePresentation
        mixed.type shouldBe null
    }

    private fun feature(plugin: EntryInteractionPlugin): EntryTypePresentationFeature {
        val composition = createEntryInteractionComposition(
            plugins = listOf(plugin),
            featureContributors = listOf(EntryTypePresentationFeatureContributor),
        )
        return DefaultEntryTypePresentationFeature(
            evaluation = composition.featureGraphEvaluation,
            interaction = composition.interactions.typePresentation,
        )
    }
}
