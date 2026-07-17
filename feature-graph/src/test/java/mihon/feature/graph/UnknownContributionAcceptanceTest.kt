package mihon.feature.graph

import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class UnknownContributionAcceptanceTest {

    @Test
    fun `unknown contributions enter consequences obligations contracts and projections without curated edits`() {
        val contractOwner = ContributionOwner("example.contract")
        val typesOwner = ContributionOwner("example.types")
        val featureOwner = ContributionOwner("example.feature")
        val alpha = capabilityDefinition<AlphaProvider>(CapabilityId("example.alpha"), contractOwner)
        val adapterDefinition = specializedAdapterDefinition<ExampleAdapter>(
            id = SpecializedAdapterId("example.adapter"),
            owner = featureOwner,
        )
        val consequence = consequence("example.consequence")
        val contract = RecordingContract()
        val projectionDefinition = featureProjectionDefinition<RecordingProjection>(
            id = FeatureArtifactId("example.projection"),
            owner = featureOwner,
        )
        val projectionImplementation = RecordingProjection()
        val projection = FeatureProjection(projectionDefinition, projectionImplementation)
        val types = mutableListOf(
            contentType("existing", typesOwner, alpha, adapterDefinition),
            ContentTypeContribution(ContentTypeId("partial"), typesOwner),
        )
        val typeContributor = featureGraphContributor(typesOwner) { types.forEach(::add) }
        val featureContributor = featureGraphContributor(featureOwner) {
            add(
                FeatureContribution(
                    feature = FeatureId("example"),
                    owner = featureOwner,
                    integrations = listOf(
                        FeatureIntegration(
                            id = FeatureIntegrationId("example.integration"),
                            prerequisites = CapabilityExpression.Provided(alpha),
                            specializedRequirements = listOf(adapterDefinition),
                            sharedConsequences = listOf(consequence),
                            behavioralContracts = listOf(contract),
                            projectionRequirements = listOf(projectionDefinition),
                            projections = listOf(projection),
                        ),
                    ),
                ),
            )
        }
        val contributors = listOf(typeContributor, featureContributor)

        val initial = discoverAndAssembleFeatureGraph(contributors)
            .let { graph -> graph to evaluateFeatureGraph(graph) }
        initial.second.sharedConsequences.map { it.subject.contentType.value } shouldContainExactly listOf("existing")
        initial.second.obligations shouldBe emptyList()

        types += contentType("future-complete", typesOwner, alpha, adapterDefinition)
        types += ContentTypeContribution(
            contentType = ContentTypeId("future-incomplete"),
            owner = typesOwner,
            providers = listOf(CapabilityProvider(alpha, AlphaProvider())),
        )

        val expandedGraph = discoverAndAssembleFeatureGraph(contributors)
        val expandedEvaluation = evaluateFeatureGraph(expandedGraph)
        val selected = selectFeatureArtifacts(expandedGraph, expandedEvaluation)

        expandedEvaluation.sharedConsequences.map { it.subject.contentType.value } shouldContainExactly listOf(
            "existing",
            "future-complete",
        )
        expandedEvaluation.sharedConsequences.all { it.consequence === consequence } shouldBe true
        expandedEvaluation.obligations.map { it.subject.contentType.value } shouldContainExactly listOf(
            "future-incomplete",
        )
        selected.behavioralContracts.map { it.subject.contentType.value } shouldContainExactly listOf(
            "existing",
            "future-complete",
        )
        selected.projections.map { it.subject.contentType.value } shouldContainExactly listOf(
            "existing",
            "future-complete",
        )
        selected.behavioralContracts.forEach { (it.contract as RecordingContract).execute(it.subject) }
        selected.projections.forEach {
            (it.projection.implementation as RecordingProjection).project(it.subject)
        }
        contract.subjects shouldContainExactly listOf(ContentTypeId("existing"), ContentTypeId("future-complete"))
        projectionImplementation.subjects shouldContainExactly listOf(
            ContentTypeId("existing"),
            ContentTypeId("future-complete"),
        )
    }

    private fun contentType(
        id: String,
        owner: ContributionOwner,
        capability: CapabilityDefinition<AlphaProvider>,
        adapter: SpecializedAdapterDefinition<ExampleAdapter>,
    ): ContentTypeContribution {
        return ContentTypeContribution(
            contentType = ContentTypeId(id),
            owner = owner,
            providers = listOf(CapabilityProvider(capability, AlphaProvider())),
            specializedAdapters = listOf(SpecializedAdapter(adapter, ExampleAdapter())),
        )
    }

    private fun consequence(id: String) = object : SharedFeatureConsequence {
        override val id = FeatureArtifactId(id)
    }

    private class RecordingContract : FeatureBehaviorContract {
        override val id = FeatureArtifactId("example.contract")
        val subjects = mutableListOf<ContentTypeId>()

        fun execute(subject: FeatureIntegrationSubject) {
            subjects += subject.contentType
        }
    }

    private class RecordingProjection {
        val subjects = mutableListOf<ContentTypeId>()

        fun project(subject: FeatureIntegrationSubject) {
            subjects += subject.contentType
        }
    }

    private class AlphaProvider

    private class ExampleAdapter
}
