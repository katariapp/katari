package mihon.feature.graph

import kotlin.reflect.KClass

/** A positive capability expression. Missing prerequisites make a feature inapplicable and create no obligation. */
sealed interface CapabilityExpression {
    data object Always : CapabilityExpression

    data class Provided(
        val capability: CapabilityDefinition<*>,
    ) : CapabilityExpression

    data class AllOf(
        val terms: List<CapabilityExpression>,
    ) : CapabilityExpression {
        init {
            requireTerms("AllOf", terms)
        }
    }

    data class AnyOf(
        val terms: List<CapabilityExpression>,
    ) : CapabilityExpression {
        init {
            requireTerms("AnyOf", terms)
        }
    }
}

fun allOf(vararg terms: CapabilityExpression): CapabilityExpression = CapabilityExpression.AllOf(terms.toList())

fun anyOf(vararg terms: CapabilityExpression): CapabilityExpression = CapabilityExpression.AnyOf(terms.toList())

private fun requireTerms(label: String, terms: List<CapabilityExpression>) {
    require(terms.isNotEmpty()) {
        "$label requires at least one term; use Always for an unconditional feature"
    }
    require(terms.distinct().size == terms.size) {
        "$label cannot contain duplicate terms"
    }
}

/** Context whose value is supplied later without flattening it into type-wide capability support. */
data class ContextInputDefinition<C : Any>(
    val id: ContextInputId,
    val owner: ContributionOwner,
    val valueType: KClass<C>,
)

inline fun <reified C : Any> contextInputDefinition(
    id: ContextInputId,
    owner: ContributionOwner,
): ContextInputDefinition<C> = ContextInputDefinition(id, owner, C::class)

/** Shared executable behavior supplied by a feature when an integration applies. */
interface SharedFeatureConsequence {
    val id: FeatureArtifactId
}

/** Behavioral expectations selected automatically when an integration applies. */
interface FeatureBehaviorContract {
    val id: FeatureArtifactId

    /** Empty for shared contracts that can execute directly from matched providers and adapters. */
    val fixtureRequirements: List<ContractFixtureDefinition<*>>
        get() = emptyList()
}

/** Feature-owned requirement for one executable developer or user-facing projection. */
data class FeatureProjectionDefinition<P : Any>(
    val id: FeatureArtifactId,
    val owner: ContributionOwner,
    val projectionType: KClass<P>,
)

inline fun <reified P : Any> featureProjectionDefinition(
    id: FeatureArtifactId,
    owner: ContributionOwner,
): FeatureProjectionDefinition<P> = FeatureProjectionDefinition(id, owner, P::class)

/** Executable projection implementation supplied against a feature-owned requirement. */
data class FeatureProjection<P : Any>(
    val definition: FeatureProjectionDefinition<P>,
    val implementation: P,
) {
    init {
        require(definition.projectionType.isInstance(implementation)) {
            "Projection for ${definition.id} must implement ${definition.projectionType.qualifiedName}"
        }
    }
}

/**
 * One feature-owned relationship between provider-backed prerequisites and their consequences.
 *
 * Context inputs describe additional runtime information that later evaluation must retain. Specialized requirements
 * differ from prerequisites: they become obligations only after [prerequisites] are satisfied.
 */
data class FeatureIntegration(
    val id: FeatureIntegrationId,
    val prerequisites: CapabilityExpression,
    val contextInputs: List<ContextInputDefinition<*>> = emptyList(),
    val specializedRequirements: List<SpecializedAdapterDefinition<*>> = emptyList(),
    val sharedConsequences: List<SharedFeatureConsequence> = emptyList(),
    val behavioralContracts: List<FeatureBehaviorContract> = emptyList(),
    val projectionRequirements: List<FeatureProjectionDefinition<*>> = emptyList(),
    val projections: List<FeatureProjection<*>> = emptyList(),
) {
    init {
        requireUnique("Context inputs for $id", contextInputs.map { it.id.value })
        requireUnique("Specialized requirements for $id", specializedRequirements.map { it.id.value })
        requireUnique("Shared consequences for $id", sharedConsequences.map { it.id.value })
        requireUnique("Behavioral contracts for $id", behavioralContracts.map { it.id.value })
        behavioralContracts.forEach { contract ->
            requireUnique(
                "Fixture requirements for ${contract.id}",
                contract.fixtureRequirements.map { it.id.value },
            )
        }
        requireUnique("Projection requirements for $id", projectionRequirements.map { it.id.value })
        requireUnique("Projections for $id", projections.map { it.definition.id.value })
        val requirementIds = projectionRequirements.mapTo(mutableSetOf()) { it.id }
        projections.forEach { projection ->
            require(projection.definition.id in requirementIds) {
                "Projection ${projection.definition.id} on $id has no matching requirement"
            }
        }
    }
}

/**
 * Feature-owned integration knowledge.
 *
 * Content types never enumerate these integrations. Nested consequences, contracts, and projections inherit this
 * contribution's owner; specialized adapter contracts must be defined by the same owner.
 */
data class FeatureContribution(
    val feature: FeatureId,
    val owner: ContributionOwner,
    val integrations: List<FeatureIntegration>,
) {
    init {
        requireUnique("Integrations for $feature", integrations.map { it.id.value })
        integrations.flatMap { it.specializedRequirements }.forEach { requirement ->
            require(requirement.owner == owner) {
                "Feature $feature cannot own specialized requirement ${requirement.id} declared by ${requirement.owner}"
            }
        }
        integrations
            .flatMap { it.behavioralContracts }
            .flatMap { it.fixtureRequirements }
            .forEach { requirement ->
                require(requirement.owner == owner) {
                    "Feature $feature cannot own contract fixture ${requirement.id} declared by ${requirement.owner}"
                }
            }
        integrations.flatMap { it.projectionRequirements }.forEach { requirement ->
            require(requirement.owner == owner) {
                "Feature $feature cannot own projection requirement ${requirement.id} declared by ${requirement.owner}"
            }
        }
        integrations.flatMap { it.projections }.forEach { projection ->
            require(projection.definition.owner == owner) {
                "Feature $feature cannot supply projection ${projection.definition.id} declared by " +
                    projection.definition.owner
            }
        }
    }
}
