package mihon.entry.interactions

import eu.kanade.tachiyomi.source.entry.EntryType
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class EntryCapabilityModelTest {

    @Test
    fun `fundamental capability identity uses a stable lowercase id`() {
        EntryCapabilityId("downloads.options").value shouldBe "downloads.options"

        shouldThrow<IllegalArgumentException> { EntryCapabilityId("Downloads") }
        shouldThrow<IllegalArgumentException> { EntryCapabilityId(" downloads") }
        shouldThrow<IllegalArgumentException> { EntryCapabilityId("") }
    }

    @Test
    fun `type-wide and contextual subjects cannot be interchanged`() {
        EntryCapabilityQuery(typeWideCapability, EntryCapabilitySubject.Type(EntryType.MANGA))
        EntryCapabilityQuery(contextualCapability, ContextualEntrySubject(EntryType.ANIME, entryId = 1L))

        shouldThrow<IllegalArgumentException> {
            EntryCapabilityQuery(typeWideCapability, ContextualEntrySubject(EntryType.MANGA, entryId = 2L))
        }
        shouldThrow<IllegalArgumentException> {
            EntryCapabilityQuery(contextualCapability, EntryCapabilitySubject.Type(EntryType.ANIME))
        }
    }

    @Test
    fun `supported result requires authoritative evidence`() {
        shouldThrow<IllegalArgumentException> { EntrySupportResult.Supported(emptyList()) }

        val evidence = EntryCapabilityEvidence.ProviderRegistration(owner, "EntryDownloadProcessor")
        EntrySupportResult.Supported(listOf(evidence)).evidence shouldBe listOf(evidence)
    }

    @Test
    fun `deliberate absence records an owner and reason`() {
        EntrySupportResult.IntentionallyUnsupported(owner, "Not in current product scope")
        EntrySupportResult.NotApplicable(owner, "Playback does not apply to image-page media")

        shouldThrow<IllegalArgumentException> {
            EntrySupportResult.IntentionallyUnsupported(owner, " ")
        }
        shouldThrow<IllegalArgumentException> {
            EntrySupportResult.NotApplicable(owner, "untrimmed ")
        }
        shouldThrow<IllegalArgumentException> { EntryCapabilityOwner("") }
    }

    @Test
    fun `contextual unavailability requires a contextual query`() {
        val result = EntrySupportResult.ContextuallyUnavailable(
            blocker = EntryCapabilityBlocker(owner, "Source does not provide preview media"),
        )

        EntryCapabilityAssessment(
            query = EntryCapabilityQuery(
                contextualCapability,
                ContextualEntrySubject(EntryType.ANIME, entryId = 3L),
            ),
            result = result,
        )

        shouldThrow<IllegalArgumentException> {
            EntryCapabilityAssessment(
                query = EntryCapabilityQuery(typeWideCapability, EntryCapabilitySubject.Type(EntryType.MANGA)),
                result = result,
            )
        }
    }

    @Test
    fun `missing obligation cannot replace fundamental support`() {
        val result = EntrySupportResult.MissingObligation(
            obligation = EntryCapabilityObligation(owner, "Provide bookmark selection fixture"),
        )

        shouldThrow<IllegalArgumentException> {
            EntryCapabilityAssessment(
                query = EntryCapabilityQuery(typeWideCapability, EntryCapabilitySubject.Type(EntryType.MANGA)),
                result = result,
            )
        }
    }

    @Test
    fun `unresolved result is explicit and explained`() {
        EntrySupportResult.Unresolved(owner, "Product outcome has not been classified")

        shouldThrow<IllegalArgumentException> {
            EntrySupportResult.Unresolved(owner, "")
        }
    }

    @Test
    fun `evidence blockers and obligations reject implicit empty meaning`() {
        shouldThrow<IllegalArgumentException> {
            EntryCapabilityEvidence.ProviderRegistration(owner, "")
        }
        shouldThrow<IllegalArgumentException> {
            EntryCapabilityEvidence.Intrinsic(owner, " ")
        }
        shouldThrow<IllegalArgumentException> {
            EntryCapabilityBlocker(owner, "")
        }
        shouldThrow<IllegalArgumentException> {
            EntryCapabilityObligation(owner, "missing ")
        }
    }

    @Test
    fun `external and contextual evidence cannot establish type-wide support`() {
        val evidence = EntryCapabilityEvidence.External(owner, "Source implements preview contract")

        shouldThrow<IllegalArgumentException> {
            EntryCapabilityEvidenceRecord(
                entryType = EntryType.ANIME,
                capability = typeWideCapability,
                evidence = evidence,
            )
        }

        EntryCapabilityEvidenceRecord(
            entryType = EntryType.ANIME,
            capability = contextualCapability,
            evidence = evidence,
        ).evidence shouldBe evidence
    }

    private data class ContextualEntrySubject(
        override val entryType: EntryType,
        val entryId: Long,
    ) : EntryCapabilitySubject.Contextual

    private companion object {
        val owner = EntryCapabilityOwner("entry-interactions")
        val typeWideCapability = EntryFundamentalCapability(
            id = EntryCapabilityId("downloads"),
            scope = EntryCapabilityScope.TYPE_WIDE,
        )
        val contextualCapability = EntryFundamentalCapability(
            id = EntryCapabilityId("preview"),
            scope = EntryCapabilityScope.CONTEXTUAL,
        )
    }
}
