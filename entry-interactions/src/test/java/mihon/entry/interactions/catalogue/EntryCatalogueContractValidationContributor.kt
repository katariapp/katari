package mihon.entry.interactions

import eu.kanade.tachiyomi.source.entry.EntryCatalogueSource
import eu.kanade.tachiyomi.source.entry.EntryItemOrientation
import eu.kanade.tachiyomi.source.entry.EntryType
import io.mockk.every
import io.mockk.mockk
import mihon.entry.interactions.validation.contractExpectation
import mihon.entry.interactions.validation.productionSubjectEvaluation
import mihon.entry.interactions.validation.verifyFeatureContract
import mihon.feature.graph.FeatureContractScenarioId
import mihon.feature.graph.contextEvidence
import mihon.feature.graph.validation.FeatureContractReference
import mihon.feature.graph.validation.FeatureContractScenario
import mihon.feature.graph.validation.FeatureContractVerifier
import mihon.feature.graph.validation.FeatureValidationContributionSink
import mihon.feature.graph.validation.FeatureValidationContributor
import tachiyomi.domain.source.model.EntryCatalogueDescription
import tachiyomi.domain.source.model.EntrySourceDescription

class EntryCatalogueContractValidationContributor : FeatureValidationContributor {
    override val owner = EntryCatalogueFeatureContributor.owner

    override fun contributeTo(sink: FeatureValidationContributionSink) {
        contracts.forEach { contract ->
            val reference = FeatureContractReference(ENTRY_CATALOGUE_FEATURE_ID, contract.contract)
            sink.add(
                FeatureContractVerifier(reference) { input ->
                    verifyFeatureContract {
                        val type = EntryType.entries.single { it.toContentTypeId() == input.subject.contentType }
                        val source = mockk<EntryCatalogueSource> {
                            every { lang } returns "en"
                            every { supportsLatest } returns true
                        }
                        val description = DefaultEntryCatalogueFeature(
                            productionSubjectEvaluation(type, EntryCatalogueFeatureContributor),
                        ).describe(source)
                        when (contract.integration) {
                            SOURCE_DESCRIPTION_INTEGRATION_ID -> contractExpectation(
                                description.language == "en",
                                "Catalogue must project source description",
                            )
                            CATALOGUE_AVAILABILITY_INTEGRATION_ID -> contractExpectation(
                                description.catalogue != null,
                                "Catalogue must project source availability",
                            )
                            LATEST_AVAILABILITY_INTEGRATION_ID -> contractExpectation(
                                description.catalogue?.supportsLatest == true,
                                "Catalogue must project latest availability",
                            )
                        }
                    }
                },
            )
            sink.add(
                FeatureContractScenario(
                    FeatureContractScenarioId("${contract.integration.value}.applicable"),
                    reference,
                    contract.integration,
                ) {
                    listOf(contextEvidence(SOURCE_DESCRIPTION_CONTEXT, SourceDescriptionEvidence(description)))
                },
            )
        }
    }

    private data class Contract(
        val integration: mihon.feature.graph.FeatureIntegrationId,
        val contract: mihon.feature.graph.FeatureBehaviorContract,
    )

    private companion object {
        val description = EntrySourceDescription(
            language = "en",
            supportedEntryTypes = null,
            itemOrientation = EntryItemOrientation.VERTICAL,
            catalogue = EntryCatalogueDescription(supportsLatest = true),
        )
        val contracts = listOf(
            Contract(SOURCE_DESCRIPTION_INTEGRATION_ID, EntrySourceDescriptionBehaviorContract),
            Contract(CATALOGUE_AVAILABILITY_INTEGRATION_ID, EntryCatalogueAvailabilityBehaviorContract),
            Contract(LATEST_AVAILABILITY_INTEGRATION_ID, EntryLatestAvailabilityBehaviorContract),
        )
    }
}
