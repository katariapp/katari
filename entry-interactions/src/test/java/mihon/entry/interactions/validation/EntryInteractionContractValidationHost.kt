package mihon.entry.interactions.validation

import mihon.entry.interactions.EntryInteractionComposition
import mihon.feature.graph.validation.FeatureContractValidationResult
import mihon.feature.graph.validation.discoverAndPlanFeatureContractValidation
import mihon.feature.graph.validation.validateFeatureContracts

/** Validation-only entry point over the exact composition produced by the application boundary. */
internal suspend fun validateEntryInteractionContracts(
    composition: EntryInteractionComposition,
    classLoader: ClassLoader = Thread.currentThread().contextClassLoader,
): FeatureContractValidationResult {
    val plan = discoverAndPlanFeatureContractValidation(
        graph = composition.featureGraph,
        evaluation = composition.featureGraphEvaluation,
        classLoader = classLoader,
    )
    return validateFeatureContracts(plan)
}
