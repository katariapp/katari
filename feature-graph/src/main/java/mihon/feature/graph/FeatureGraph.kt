package mihon.feature.graph

/**
 * Deterministic structural graph of discovered contributions.
 *
 * This graph contains relationships but does not evaluate applicability or materialize obligations.
 */
data class FeatureGraph(
    val contentTypes: List<ContentTypeContribution>,
    val features: List<FeatureContribution>,
    val capabilities: List<CapabilityDefinition<*>>,
    val contextInputs: List<ContextInputDefinition<*>>,
    val specializedAdapters: List<SpecializedAdapterDefinition<*>>,
    val contractFixtures: List<ContractFixtureDefinition<*>>,
    val projections: List<FeatureProjectionDefinition<*>>,
)

fun assembleFeatureGraph(
    discovered: DiscoveredFeatureGraphContributions,
): FeatureGraph {
    validateUniqueContentTypes(discovered.contentTypes)
    validateUniqueFeatures(discovered.features)

    val capabilityDefinitions = buildList {
        discovered.contentTypes.flatMapTo(this) { type -> type.providers.map { it.capability } }
        discovered.features.flatMapTo(this) { feature ->
            feature.integrations.flatMap { integration -> integration.prerequisites.capabilities() }
        }
    }
    val contextInputDefinitions = discovered.features.flatMap { feature ->
        feature.integrations.flatMap { it.contextInputs }
    }
    val specializedAdapterDefinitions = buildList {
        discovered.contentTypes.flatMapTo(this) { type ->
            type.specializedAdapters.map { it.definition }
        }
        discovered.features.flatMapTo(this) { feature ->
            feature.integrations.flatMap { it.specializedRequirements }
        }
    }
    val contractFixtureDefinitions = buildList {
        discovered.contentTypes.flatMapTo(this) { type ->
            type.contractFixtures.map { it.definition }
        }
        discovered.features.flatMapTo(this) { feature ->
            feature.integrations
                .flatMap { it.behavioralContracts }
                .flatMap { it.fixtureRequirements }
        }
    }
    val projectionDefinitions = buildList {
        discovered.features.flatMapTo(this) { feature ->
            feature.integrations.flatMap { integration ->
                integration.projectionRequirements + integration.projections.map { it.definition }
            }
        }
    }

    val capabilities = consistentDefinitions(
        label = "capability",
        definitions = capabilityDefinitions,
        id = { it.id.value },
    )
    val contextInputs = consistentDefinitions(
        label = "context input",
        definitions = contextInputDefinitions,
        id = { it.id.value },
    )
    val specializedAdapters = consistentDefinitions(
        label = "specialized adapter",
        definitions = specializedAdapterDefinitions,
        id = { it.id.value },
    )
    val contractFixtures = consistentDefinitions(
        label = "contract fixture",
        definitions = contractFixtureDefinitions,
        id = { it.id.value },
    )
    val projections = consistentDefinitions(
        label = "projection",
        definitions = projectionDefinitions,
        id = { "${it.owner.value}:${it.id.value}" },
    )

    validateReachability(discovered)

    return FeatureGraph(
        contentTypes = discovered.contentTypes.sortedBy { it.contentType.value },
        features = discovered.features.sortedBy { it.feature.value },
        capabilities = capabilities.sortedBy { it.id.value },
        contextInputs = contextInputs.sortedBy { it.id.value },
        specializedAdapters = specializedAdapters.sortedBy { it.id.value },
        contractFixtures = contractFixtures.sortedBy { it.id.value },
        projections = projections.sortedWith(compareBy({ it.owner.value }, { it.id.value })),
    )
}

fun discoverAndAssembleFeatureGraph(
    contributors: Iterable<FeatureGraphContributor>,
): FeatureGraph = assembleFeatureGraph(discoverFeatureGraphContributions(contributors))

private fun validateUniqueContentTypes(contributions: List<ContentTypeContribution>) {
    contributions.groupBy { it.contentType }
        .filterValues { it.size > 1 }
        .forEach { (id, duplicates) ->
            val owners = duplicates.map { it.owner }.distinct().sortedBy { it.value }
            error("Duplicate content-type contribution $id from owners $owners")
        }
}

private fun validateUniqueFeatures(contributions: List<FeatureContribution>) {
    contributions.groupBy { it.feature }
        .filterValues { it.size > 1 }
        .forEach { (id, duplicates) ->
            val owners = duplicates.map { it.owner }.distinct().sortedBy { it.value }
            error("Duplicate feature contribution $id from owners $owners")
        }
}

private fun <D> consistentDefinitions(
    label: String,
    definitions: List<D>,
    id: (D) -> String,
): List<D> {
    return definitions.groupBy(id)
        .map { (definitionId, matchingDefinitions) ->
            check(matchingDefinitions.distinct().size == 1) {
                "Contradictory $label definition $definitionId: ${matchingDefinitions.distinct()}"
            }
            matchingDefinitions.first()
        }
}

private fun validateReachability(discovered: DiscoveredFeatureGraphContributions) {
    val consumedCapabilities = discovered.features
        .flatMap { it.integrations }
        .flatMap { it.prerequisites.capabilities() }
        .mapTo(mutableSetOf()) { it.id }
    val requiredAdapters = discovered.features
        .flatMap { it.integrations }
        .flatMap { it.specializedRequirements }
        .mapTo(mutableSetOf()) { it.id }
    val requiredFixtures = discovered.features
        .flatMap { it.integrations }
        .flatMap { it.behavioralContracts }
        .flatMap { it.fixtureRequirements }
        .mapTo(mutableSetOf()) { it.id }

    discovered.contentTypes.forEach { type ->
        type.providers.forEach { provider ->
            check(provider.capability.id in consumedCapabilities) {
                "Unreachable capability provider ${provider.capability.id} on ${type.contentType}: " +
                    "no feature integration consumes it"
            }
        }
        type.specializedAdapters.forEach { adapter ->
            check(adapter.definition.id in requiredAdapters) {
                "Unreachable specialized adapter ${adapter.definition.id} on ${type.contentType}: " +
                    "no feature integration requires it"
            }
        }
        type.contractFixtures.forEach { fixture ->
            check(fixture.definition.id in requiredFixtures) {
                "Unreachable contract fixture ${fixture.definition.id} on ${type.contentType}: " +
                    "no behavioral contract requires it"
            }
        }
    }

    discovered.features.forEach { feature ->
        feature.integrations.forEach { integration ->
            val hasEffect = integration.specializedRequirements.isNotEmpty() ||
                integration.sharedConsequences.isNotEmpty() ||
                integration.behavioralContracts.isNotEmpty() ||
                integration.projectionRequirements.isNotEmpty()
            check(hasEffect) {
                "Unreachable feature integration ${integration.id} on ${feature.feature}: " +
                    "it contributes no consequence, specialized requirement, contract, or projection"
            }
        }
    }
}

private fun CapabilityExpression.capabilities(): List<CapabilityDefinition<*>> {
    return when (this) {
        CapabilityExpression.Always -> emptyList()
        is CapabilityExpression.Provided -> listOf(capability)
        is CapabilityExpression.AllOf -> terms.flatMap { it.capabilities() }
        is CapabilityExpression.AnyOf -> terms.flatMap { it.capabilities() }
    }
}
