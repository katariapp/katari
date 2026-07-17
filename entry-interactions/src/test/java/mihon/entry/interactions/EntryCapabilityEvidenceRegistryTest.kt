package mihon.entry.interactions

import eu.kanade.tachiyomi.source.entry.EntryType
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test

class EntryCapabilityEvidenceRegistryTest {

    @Test
    fun `empty plugin composition has no inferred capability evidence`() {
        val composition = createEntryInteractionComposition(emptyList())

        composition.capabilityEvidence.records shouldBe emptyList()
    }

    @Test
    fun `provider registration contributes type-wide capability evidence`() {
        val processor = mockk<EntryProgressProcessor> {
            every { type } returns EntryType.BOOK
        }

        val composition = createEntryInteractionComposition(
            listOf(EntryInteractionPlugin { it.registerProgressProcessor(processor) }),
        )

        val record = composition.capabilityEvidence.records.single()
        record.entryType shouldBe EntryType.BOOK
        record.capability shouldBe capability("progress")
        record.evidence.shouldBeInstanceOf<EntryCapabilityEvidence.ProviderRegistration>()
    }

    @Test
    fun `contextual provider registration remains contextual evidence`() {
        val processor = mockk<EntryPreviewProcessor> {
            every { type } returns EntryType.ANIME
        }

        val composition = createEntryInteractionComposition(
            listOf(EntryInteractionPlugin { it.registerPreviewProcessor(processor) }),
        )

        val record = composition.capabilityEvidence.records.single()
        record.capability shouldBe capability("preview", EntryCapabilityScope.CONTEXTUAL)
        shouldThrow<IllegalArgumentException> {
            EntryCapabilityQuery(record.capability, EntryCapabilitySubject.Type(EntryType.ANIME))
        }
    }

    @Test
    fun `intrinsic declaration contributes explicit type-wide evidence`() {
        val declaration = intrinsicDeclaration("publication-resources")

        val composition = createEntryInteractionComposition(
            listOf(EntryInteractionPlugin { it.declareIntrinsicCapability(declaration) }),
        )

        val record = composition.capabilityEvidence.records.single()
        record.entryType shouldBe EntryType.BOOK
        record.capability shouldBe declaration.capability
        record.evidence shouldBe EntryCapabilityEvidence.Intrinsic(owner, declaration.reason)
    }

    @Test
    fun `intrinsic declarations reject contextual capability claims`() {
        shouldThrow<IllegalArgumentException> {
            EntryIntrinsicCapabilityDeclaration(
                entryType = EntryType.ANIME,
                capability = capability("preview", EntryCapabilityScope.CONTEXTUAL),
                owner = owner,
                reason = "Preview has runtime source requirements",
            )
        }
    }

    @Test
    fun `duplicate intrinsic authority fails composition`() {
        val declaration = intrinsicDeclaration("publication-resources")

        val error = shouldThrow<IllegalStateException> {
            createEntryInteractionComposition(
                listOf(
                    EntryInteractionPlugin { it.declareIntrinsicCapability(declaration) },
                    EntryInteractionPlugin { it.declareIntrinsicCapability(declaration) },
                ),
            )
        }

        error.message shouldBe
            "Duplicate capability evidence for publication-resources on EntryType BOOK: [Intrinsic]"
    }

    @Test
    fun `provider and intrinsic authority for the same fact contradict`() {
        val processor = mockk<EntryProgressProcessor> {
            every { type } returns EntryType.BOOK
        }

        val error = shouldThrow<IllegalStateException> {
            createEntryInteractionComposition(
                listOf(
                    EntryInteractionPlugin { registry ->
                        registry.registerProgressProcessor(processor)
                        registry.declareIntrinsicCapability(intrinsicDeclaration("progress"))
                    },
                ),
            )
        }

        error.message shouldBe
            "Contradictory capability evidence for progress on EntryType BOOK: [ProviderRegistration, Intrinsic]"
    }

    @Test
    fun `one capability id cannot have type-wide and contextual definitions`() {
        val processor = mockk<EntryPreviewProcessor> {
            every { type } returns EntryType.ANIME
        }

        val error = shouldThrow<IllegalStateException> {
            createEntryInteractionComposition(
                listOf(
                    EntryInteractionPlugin { registry ->
                        registry.registerPreviewProcessor(processor)
                        registry.declareIntrinsicCapability(
                            EntryIntrinsicCapabilityDeclaration(
                                entryType = EntryType.ANIME,
                                capability = capability("preview"),
                                owner = owner,
                                reason = "Contradictory synthetic declaration",
                            ),
                        )
                    },
                ),
            )
        }

        error.message shouldBe
            "Contradictory capability definitions for preview: [CONTEXTUAL, TYPE_WIDE]"
    }

    @Test
    fun `registration alone does not promote sub-capability or universal policy processors`() {
        val capabilityProcessor = mockk<EntryCapabilityProcessor> {
            every { type } returns EntryType.ANIME
        }
        val updateProcessor = mockk<EntryUpdateEligibilityProcessor> {
            every { type } returns EntryType.ANIME
        }
        val childGroupProcessor = mockk<EntryChildGroupFilterProcessor> {
            every { type } returns EntryType.ANIME
        }
        val libraryFilterProcessor = mockk<EntryLibraryFilterProcessor> {
            every { type } returns EntryType.ANIME
        }

        val composition = createEntryInteractionComposition(
            listOf(
                EntryInteractionPlugin { registry ->
                    registry.registerCapabilityProcessor(capabilityProcessor)
                    registry.registerUpdateEligibilityProcessor(updateProcessor)
                    registry.registerChildGroupFilterProcessor(childGroupProcessor)
                    registry.registerLibraryFilterProcessor(libraryFilterProcessor)
                },
            ),
        )

        composition.capabilityEvidence.records shouldBe emptyList()
    }

    private fun intrinsicDeclaration(id: String): EntryIntrinsicCapabilityDeclaration {
        return EntryIntrinsicCapabilityDeclaration(
            entryType = EntryType.BOOK,
            capability = capability(id),
            owner = owner,
            reason = "Stable type-composition fact with no operational provider",
        )
    }

    private fun capability(
        id: String,
        scope: EntryCapabilityScope = EntryCapabilityScope.TYPE_WIDE,
    ): EntryFundamentalCapability {
        return EntryFundamentalCapability(EntryCapabilityId(id), scope)
    }

    private companion object {
        val owner = EntryCapabilityOwner("entry-interactions.book")
    }
}
