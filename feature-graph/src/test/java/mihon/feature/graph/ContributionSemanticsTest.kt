package mihon.feature.graph

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class ContributionSemanticsTest {

    private val typeOwner = ContributionOwner("example.type")
    private val featureOwner = ContributionOwner("example.feature")
    private val alpha = capabilityDefinition<AlphaProvider>(CapabilityId("example.alpha"), featureOwner)
    private val beta = capabilityDefinition<BetaProvider>(CapabilityId("example.beta"), featureOwner)

    @Test
    fun `a content type with no providers is valid`() {
        val contribution = ContentTypeContribution(
            contentType = ContentTypeId("example"),
            owner = typeOwner,
        )

        contribution.providers shouldBe emptyList()
        contribution.specializedAdapters shouldBe emptyList()
    }

    @Test
    fun `a content type with one provider is valid without absence declarations`() {
        val alphaProvider = AlphaProvider()
        val contribution = ContentTypeContribution(
            contentType = ContentTypeId("example"),
            owner = typeOwner,
            providers = listOf(CapabilityProvider(alpha, alphaProvider)),
        )

        contribution.providers.map { it.capability.id } shouldContainExactly listOf(alpha.id)
        contribution.providers.single().implementation shouldBe alphaProvider
    }

    @Test
    fun `a content type cannot contribute the same provider identity twice`() {
        val duplicateDefinition = capabilityDefinition<AlphaProvider>(alpha.id, ContributionOwner("other.contract"))

        shouldThrow<IllegalArgumentException> {
            ContentTypeContribution(
                contentType = ContentTypeId("example"),
                owner = typeOwner,
                providers = listOf(
                    CapabilityProvider(alpha, AlphaProvider()),
                    CapabilityProvider(duplicateDefinition, AlphaProvider()),
                ),
            )
        }
    }

    @Test
    fun `feature relationship separates applicability from specialized obligations`() {
        val context = contextInputDefinition<ExampleContext>(
            id = ContextInputId("example.context"),
            owner = ContributionOwner("example.context-owner"),
        )
        val adapter = specializedAdapterDefinition<ExampleAdapter>(
            id = SpecializedAdapterId("combination.adapter"),
            owner = featureOwner,
        )
        val consequence = consequence("combination.shared-policy")
        val contract = contract("combination.behavior")
        val projection = projection("combination.reference")
        val integration = FeatureIntegration(
            id = FeatureIntegrationId("combination.available"),
            prerequisites = allOf(
                CapabilityExpression.Provided(alpha),
                CapabilityExpression.Provided(beta),
            ),
            contextInputs = listOf(context),
            specializedRequirements = listOf(adapter),
            sharedConsequences = listOf(consequence),
            behavioralContracts = listOf(contract),
            projections = listOf(projection),
        )

        val feature = FeatureContribution(
            feature = FeatureId("combination"),
            owner = featureOwner,
            integrations = listOf(integration),
        )

        feature.integrations.single().prerequisites shouldBe integration.prerequisites
        feature.integrations.single().contextInputs shouldContainExactly listOf(context)
        feature.integrations.single().specializedRequirements shouldContainExactly listOf(adapter)
        feature.integrations.single().sharedConsequences shouldContainExactly listOf(consequence)
        feature.integrations.single().behavioralContracts shouldContainExactly listOf(contract)
        feature.integrations.single().projections shouldContainExactly listOf(projection)
    }

    @Test
    fun `feature cannot claim another owner's specialized requirement`() {
        val foreignRequirement = specializedAdapterDefinition<ExampleAdapter>(
            id = SpecializedAdapterId("foreign.adapter"),
            owner = ContributionOwner("other.feature"),
        )

        shouldThrow<IllegalArgumentException> {
            FeatureContribution(
                feature = FeatureId("combination"),
                owner = featureOwner,
                integrations = listOf(
                    FeatureIntegration(
                        id = FeatureIntegrationId("combination.available"),
                        prerequisites = CapabilityExpression.Provided(beta),
                        specializedRequirements = listOf(foreignRequirement),
                    ),
                ),
            )
        }
    }

    @Test
    fun `content type can supply an applicable feature adapter without adding unrelated providers`() {
        val adapterDefinition = specializedAdapterDefinition<ExampleAdapter>(
            id = SpecializedAdapterId("combination.adapter"),
            owner = featureOwner,
        )
        val adapter = ExampleAdapter()

        val contribution = ContentTypeContribution(
            contentType = ContentTypeId("example"),
            owner = typeOwner,
            providers = listOf(CapabilityProvider(alpha, AlphaProvider())),
            specializedAdapters = listOf(SpecializedAdapter(adapterDefinition, adapter)),
        )

        contribution.providers.map { it.capability.id } shouldContainExactly listOf(alpha.id)
        contribution.specializedAdapters.single().implementation shouldBe adapter
    }

    @Test
    fun `empty capability expression is rejected in favor of explicit always`() {
        shouldThrow<IllegalArgumentException> { allOf() }
        shouldThrow<IllegalArgumentException> { anyOf() }

        FeatureIntegration(
            id = FeatureIntegrationId("example.unconditional"),
            prerequisites = CapabilityExpression.Always,
        ).prerequisites shouldBe CapabilityExpression.Always
    }

    @Test
    fun `feature integration identities are unique within their owner`() {
        val integration = FeatureIntegration(
            id = FeatureIntegrationId("example.integration"),
            prerequisites = CapabilityExpression.Always,
        )

        shouldThrow<IllegalArgumentException> {
            FeatureContribution(
                feature = FeatureId("example"),
                owner = featureOwner,
                integrations = listOf(integration, integration),
            )
        }
    }

    @Test
    fun `identities reject unstable values`() {
        shouldThrow<IllegalArgumentException> { ContentTypeId("Example Type") }
        shouldThrow<IllegalArgumentException> { CapabilityId("entry/open") }
        shouldThrow<IllegalArgumentException> { ContributionOwner(" example.owner") }
    }

    private fun consequence(id: String) = object : SharedFeatureConsequence {
        override val id = FeatureArtifactId(id)
    }

    private fun contract(id: String) = object : FeatureBehaviorContract {
        override val id = FeatureArtifactId(id)
    }

    private fun projection(id: String) = object : FeatureProjection {
        override val id = FeatureArtifactId(id)
    }

    private class AlphaProvider

    private class BetaProvider

    private class ExampleAdapter

    private class ExampleContext
}
