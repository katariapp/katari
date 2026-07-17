package mihon.entry.interactions

import eu.kanade.tachiyomi.source.entry.EntryType
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import org.junit.jupiter.api.Test

class EntryCapabilityReportTest {

    @Test
    fun `catalog is stable sorted and contains no derived feature combinations`() {
        val ids = EntryCapabilityCatalog.capabilities.map { it.id.value }

        ids shouldBe ids.sorted()
        ids.distinct().size shouldBe ids.size
        ("bookmark-downloads" in ids) shouldBe false
        ("update-eligibility" in ids) shouldBe false
    }

    @Test
    fun `report distinguishes supported conditional deliberate absence and unresolved`() {
        val evidence = EntryCapabilityEvidenceSnapshot(
            listOf(
                providerEvidence(EntryType.MANGA, EntryCapabilityCatalog.DOWNLOADS),
                providerEvidence(EntryType.ANIME, EntryCapabilityCatalog.PREVIEW),
            ),
        )
        val outcomes = EntryCapabilityOutcomeSnapshot(
            listOf(
                EntryCapabilityOutcomeDeclaration(
                    EntryType.ANIME,
                    EntryCapabilityCatalog.BOOKMARKING,
                    EntrySupportResult.IntentionallyUnsupported(owner, "Not in current product scope"),
                ),
                EntryCapabilityOutcomeDeclaration(
                    EntryType.BOOK,
                    EntryCapabilityCatalog.PLAYBACK_PREFERENCES,
                    EntrySupportResult.NotApplicable(owner, "Book reading does not use playback preferences"),
                ),
            ),
        )

        val report = createEntryCapabilityReport(
            registeredTypes = listOf(EntryType.BOOK, EntryType.MANGA, EntryType.ANIME),
            evidence = evidence,
            outcomes = outcomes,
        )

        report.types.map { it.entryType }.shouldContainExactly(EntryType.MANGA, EntryType.ANIME, EntryType.BOOK)
        report.type(EntryType.MANGA)
            .entry(EntryCapabilityCatalog.DOWNLOADS)
            .outcome()
            .shouldBeInstanceOf<EntrySupportResult.Supported>()
        report.type(EntryType.ANIME)
            .entry(EntryCapabilityCatalog.PREVIEW)
            .value
            .shouldBeInstanceOf<EntryCapabilityReportValue.Conditional>()
        report.type(EntryType.ANIME)
            .entry(EntryCapabilityCatalog.BOOKMARKING)
            .outcome()
            .shouldBeInstanceOf<EntrySupportResult.IntentionallyUnsupported>()
        report.type(EntryType.BOOK)
            .entry(EntryCapabilityCatalog.PLAYBACK_PREFERENCES)
            .outcome()
            .shouldBeInstanceOf<EntrySupportResult.NotApplicable>()
        report.type(EntryType.BOOK)
            .entry(EntryCapabilityCatalog.MERGE)
            .outcome()
            .shouldBeInstanceOf<EntrySupportResult.Unresolved>()
    }

    @Test
    fun `report ordering and values do not depend on registration order`() {
        val records = listOf(
            providerEvidence(EntryType.ANIME, EntryCapabilityCatalog.PREVIEW),
            providerEvidence(EntryType.MANGA, EntryCapabilityCatalog.DOWNLOADS),
            providerEvidence(EntryType.BOOK, EntryCapabilityCatalog.OPEN),
        )
        val declarations = listOf(
            EntryCapabilityOutcomeDeclaration(
                EntryType.BOOK,
                EntryCapabilityCatalog.BOOKMARKING,
                EntrySupportResult.IntentionallyUnsupported(owner, "Not in current product scope"),
            ),
        )

        val first = createEntryCapabilityReport(
            registeredTypes = EntryType.entries,
            evidence = EntryCapabilityEvidenceSnapshot(records),
            outcomes = EntryCapabilityOutcomeSnapshot(declarations),
        )
        val second = createEntryCapabilityReport(
            registeredTypes = EntryType.entries.reversed(),
            evidence = EntryCapabilityEvidenceSnapshot(records.reversed()),
            outcomes = EntryCapabilityOutcomeSnapshot(declarations.reversed()),
        )

        first shouldBe second
        first.types.forEach { report ->
            report.entries.map { it.capability.id.value } shouldBe
                EntryCapabilityCatalog.capabilities.map { it.id.value }
        }
    }

    @Test
    fun `positive evidence cannot coexist with explicit absence`() {
        val evidence = EntryCapabilityEvidenceSnapshot(
            listOf(providerEvidence(EntryType.MANGA, EntryCapabilityCatalog.DOWNLOADS)),
        )
        val outcomes = EntryCapabilityOutcomeSnapshot(
            listOf(
                EntryCapabilityOutcomeDeclaration(
                    EntryType.MANGA,
                    EntryCapabilityCatalog.DOWNLOADS,
                    EntrySupportResult.IntentionallyUnsupported(owner, "Contradictory synthetic absence"),
                ),
            ),
        )

        shouldThrow<IllegalStateException> {
            createEntryCapabilityReport(listOf(EntryType.MANGA), evidence, outcomes)
        }
    }

    @Test
    fun `report rejects evidence outside the reviewed catalog`() {
        val unknown =
            EntryFundamentalCapability(EntryCapabilityId("unknown-capability"), EntryCapabilityScope.TYPE_WIDE)

        shouldThrow<IllegalStateException> {
            createEntryCapabilityReport(
                registeredTypes = listOf(EntryType.MANGA),
                evidence = EntryCapabilityEvidenceSnapshot(listOf(providerEvidence(EntryType.MANGA, unknown))),
                outcomes = EntryCapabilityOutcomeSnapshot(emptyList()),
            )
        }
    }

    @Test
    fun `explicit type outcomes cannot declare support or migration uncertainty`() {
        val providerEvidence = providerEvidence(EntryType.MANGA, EntryCapabilityCatalog.DOWNLOADS).evidence
        shouldThrow<IllegalArgumentException> {
            EntryCapabilityOutcomeDeclaration(
                EntryType.MANGA,
                EntryCapabilityCatalog.DOWNLOADS,
                EntrySupportResult.Supported(listOf(providerEvidence)),
            )
        }
        shouldThrow<IllegalArgumentException> {
            EntryCapabilityOutcomeDeclaration(
                EntryType.MANGA,
                EntryCapabilityCatalog.DOWNLOADS,
                EntrySupportResult.Unresolved(owner, "Must be inferred by the report"),
            )
        }
        shouldThrow<IllegalArgumentException> {
            EntryCapabilityOutcomeDeclaration(
                EntryType.MANGA,
                EntryCapabilityCatalog.DOWNLOADS,
                EntrySupportResult.IntentionallyUnsupported(
                    owner = owner,
                    reason = "Contradictory synthetic absence",
                    evidence = listOf(providerEvidence),
                ),
            )
        }
    }

    private fun providerEvidence(
        entryType: EntryType,
        capability: EntryFundamentalCapability,
    ): EntryCapabilityEvidenceRecord {
        return EntryCapabilityEvidenceRecord(
            entryType = entryType,
            capability = capability,
            evidence = EntryCapabilityEvidence.ProviderRegistration(owner, "test-provider"),
        )
    }

    private fun EntryCapabilityReportEntry.outcome(): EntrySupportResult {
        return (value as EntryCapabilityReportValue.Outcome).result
    }

    private companion object {
        val owner = EntryCapabilityOwner("entry-interactions.test")
    }
}
