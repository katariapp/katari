package mihon.entry.interactions

import eu.kanade.tachiyomi.source.entry.EntryCatalogueSource
import eu.kanade.tachiyomi.source.entry.UnifiedSource
import eu.kanade.tachiyomi.source.entry.entryItemOrientation
import eu.kanade.tachiyomi.source.entry.supportedEntryTypes
import mihon.feature.graph.ApplicableFeatureContext
import mihon.feature.graph.BlockedFeatureContext
import mihon.feature.graph.CapabilityExpression
import mihon.feature.graph.ContextInputId
import mihon.feature.graph.ContributionOwner
import mihon.feature.graph.FeatureArtifactId
import mihon.feature.graph.FeatureContextBlocker
import mihon.feature.graph.FeatureContextDecision
import mihon.feature.graph.FeatureContribution
import mihon.feature.graph.FeatureGraphContributionSink
import mihon.feature.graph.FeatureGraphContributor
import mihon.feature.graph.FeatureGraphEvaluation
import mihon.feature.graph.FeatureId
import mihon.feature.graph.FeatureIntegration
import mihon.feature.graph.FeatureIntegrationId
import mihon.feature.graph.SharedFeatureConsequence
import mihon.feature.graph.contextEvidence
import mihon.feature.graph.contextInputDefinition
import mihon.feature.graph.featureContextRule
import mihon.feature.graph.resolveFeatureContext
import tachiyomi.domain.source.model.EntryCatalogueDescription
import tachiyomi.domain.source.model.EntrySourceDescription
import tachiyomi.domain.source.model.UnifiedStubSource

private val ENTRY_CATALOGUE_FEATURE_ID = FeatureId("entry.catalogue")
private val ENTRY_CATALOGUE_FEATURE_OWNER = ContributionOwner("entry-catalogue")
private val SOURCE_CONTRACT_OWNER = ContributionOwner("entry-source")

private val SOURCE_DESCRIPTION_INTEGRATION_ID = FeatureIntegrationId("entry.catalogue.source-description")
private val CATALOGUE_AVAILABILITY_INTEGRATION_ID = FeatureIntegrationId("entry.catalogue.availability")
private val LATEST_AVAILABILITY_INTEGRATION_ID = FeatureIntegrationId("entry.catalogue.latest")

private data class SourceDescriptionEvidence(
    val description: EntrySourceDescription,
)

private val SOURCE_DESCRIPTION_CONTEXT = contextInputDefinition<SourceDescriptionEvidence>(
    id = ContextInputId("entry.source.description"),
    owner = SOURCE_CONTRACT_OWNER,
)

private val CATALOGUE_UNAVAILABLE_BLOCKER = FeatureContextBlocker(
    id = FeatureArtifactId("entry.catalogue.unavailable"),
    inputs = listOf(SOURCE_DESCRIPTION_CONTEXT),
)
private val LATEST_UNAVAILABLE_BLOCKER = FeatureContextBlocker(
    id = FeatureArtifactId("entry.catalogue.latest.unavailable"),
    inputs = listOf(SOURCE_DESCRIPTION_CONTEXT),
)

private enum class EntryCatalogueConsequence(
    override val id: FeatureArtifactId,
) : SharedFeatureConsequence {
    SOURCE_DESCRIPTION(FeatureArtifactId("entry.catalogue.source-description.projection")),
    CATALOGUE_AVAILABILITY(FeatureArtifactId("entry.catalogue.availability.projection")),
    LATEST_AVAILABILITY(FeatureArtifactId("entry.catalogue.latest.projection")),
}

internal object EntryCatalogueFeatureContributor : FeatureGraphContributor {
    override val owner = ENTRY_CATALOGUE_FEATURE_OWNER

    override fun contributeTo(sink: FeatureGraphContributionSink) {
        sink.add(
            FeatureContribution(
                feature = ENTRY_CATALOGUE_FEATURE_ID,
                owner = owner,
                integrations = listOf(
                    FeatureIntegration(
                        id = SOURCE_DESCRIPTION_INTEGRATION_ID,
                        prerequisites = CapabilityExpression.Always,
                        contextInputs = listOf(SOURCE_DESCRIPTION_CONTEXT),
                        contextRule = featureContextRule(owner) { FeatureContextDecision.Applicable },
                        sharedConsequences = listOf(EntryCatalogueConsequence.SOURCE_DESCRIPTION),
                    ),
                    FeatureIntegration(
                        id = CATALOGUE_AVAILABILITY_INTEGRATION_ID,
                        prerequisites = CapabilityExpression.Always,
                        contextInputs = listOf(SOURCE_DESCRIPTION_CONTEXT),
                        contextRule = featureContextRule(owner) { evidence ->
                            if (evidence.value(SOURCE_DESCRIPTION_CONTEXT).description.catalogue != null) {
                                FeatureContextDecision.Applicable
                            } else {
                                FeatureContextDecision.Blocked(listOf(CATALOGUE_UNAVAILABLE_BLOCKER))
                            }
                        },
                        contextBlockers = listOf(CATALOGUE_UNAVAILABLE_BLOCKER),
                        sharedConsequences = listOf(EntryCatalogueConsequence.CATALOGUE_AVAILABILITY),
                    ),
                    FeatureIntegration(
                        id = LATEST_AVAILABILITY_INTEGRATION_ID,
                        prerequisites = CapabilityExpression.Always,
                        contextInputs = listOf(SOURCE_DESCRIPTION_CONTEXT),
                        contextRule = featureContextRule(owner) { evidence ->
                            if (evidence.value(SOURCE_DESCRIPTION_CONTEXT).description.catalogue?.supportsLatest ==
                                true
                            ) {
                                FeatureContextDecision.Applicable
                            } else {
                                FeatureContextDecision.Blocked(listOf(LATEST_UNAVAILABLE_BLOCKER))
                            }
                        },
                        contextBlockers = listOf(LATEST_UNAVAILABLE_BLOCKER),
                        sharedConsequences = listOf(EntryCatalogueConsequence.LATEST_AVAILABILITY),
                    ),
                ),
            ),
        )
    }
}

internal class DefaultEntryCatalogueFeature(
    private val evaluation: FeatureGraphEvaluation,
) : EntryCatalogueFeature {

    override fun describe(source: UnifiedSource): EntrySourceDescription {
        val catalogue = source as? EntryCatalogueSource
        val description = EntrySourceDescription(
            language = catalogue?.lang ?: (source as? UnifiedStubSource)?.lang.orEmpty(),
            supportedEntryTypes = source.supportedEntryTypes()?.toSet(),
            itemOrientation = source.entryItemOrientation(),
            catalogue = catalogue?.let { EntryCatalogueDescription(supportsLatest = it.supportsLatest) },
        )
        val evidence = contextEvidence(SOURCE_DESCRIPTION_CONTEXT, SourceDescriptionEvidence(description))

        requireUniformState(
            SOURCE_DESCRIPTION_INTEGRATION_ID,
            EntryCatalogueConsequence.SOURCE_DESCRIPTION,
            evidence,
            applicable = true,
        )
        requireUniformState(
            CATALOGUE_AVAILABILITY_INTEGRATION_ID,
            EntryCatalogueConsequence.CATALOGUE_AVAILABILITY,
            evidence,
            applicable = catalogue != null,
        )
        requireUniformState(
            LATEST_AVAILABILITY_INTEGRATION_ID,
            EntryCatalogueConsequence.LATEST_AVAILABILITY,
            evidence,
            applicable = catalogue?.supportsLatest == true,
        )

        return description
    }

    private fun requireUniformState(
        integration: FeatureIntegrationId,
        consequence: EntryCatalogueConsequence,
        evidence: mihon.feature.graph.ContextEvidence<SourceDescriptionEvidence>,
        applicable: Boolean,
    ) {
        val subjects = evaluation.integrations
            .map { it.subject }
            .filter { it.feature == ENTRY_CATALOGUE_FEATURE_ID && it.integration == integration }
        check(subjects.isNotEmpty()) { "Entry Catalogue integration $integration was not discovered" }

        subjects.forEach { subject ->
            val resolution = resolveFeatureContext(
                evaluation = evaluation,
                contentType = subject.contentType,
                feature = ENTRY_CATALOGUE_FEATURE_ID,
                integration = integration,
                evidence = listOf(evidence),
            )
            val resolved = resolution.integration
            val hasConsequence = resolution.sharedConsequences.any { it.consequence.id == consequence.id }
            check(
                if (applicable) {
                    resolved is ApplicableFeatureContext && hasConsequence
                } else {
                    resolved is BlockedFeatureContext && !hasConsequence
                },
            ) {
                "Entry Catalogue integration $integration resolved inconsistently for ${subject.contentType}: " +
                    "$resolved, consequences=${resolution.sharedConsequences}"
            }
        }
    }
}
