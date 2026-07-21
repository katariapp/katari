package mihon.feature.graph.validation

import mihon.feature.graph.BehavioralContractSelection

sealed interface FeatureContractVerificationResult {
    data object Passed : FeatureContractVerificationResult

    data class Failed(
        val failures: List<FeatureContractFailure>,
    ) : FeatureContractVerificationResult {
        init {
            require(failures.isNotEmpty()) { "Failed contract verification requires at least one failure" }
        }
    }
}

data class FeatureContractFailure(
    val message: String,
) {
    init {
        require(message.isNotBlank()) { "Contract failure message cannot be blank" }
    }
}

sealed interface FeatureContractExecutionResult {
    val selection: FeatureContractExecutionSelection
}

data class CompletedFeatureContractExecution(
    override val selection: FeatureContractExecutionSelection,
    val verification: FeatureContractVerificationResult,
) : FeatureContractExecutionResult

data class CrashedFeatureContractExecution(
    override val selection: FeatureContractExecutionSelection,
    val cause: Throwable,
) : FeatureContractExecutionResult

data class FeatureContractExecutionSelection(
    val contractSelection: BehavioralContractSelection,
    val scenario: OwnedFeatureContractScenario?,
    val verifier: OwnedFeatureContractVerifier,
)

data class FeatureContractValidationResult(
    val plan: FeatureContractValidationPlan,
    val executions: List<FeatureContractExecutionResult>,
) {
    val isSuccessful: Boolean
        get() = plan.isComplete && executions.all(FeatureContractExecutionResult::isSuccessful)
}

suspend fun validateFeatureContracts(
    plan: FeatureContractValidationPlan,
): FeatureContractValidationResult {
    val executions = plan.executions.map { selection ->
        try {
            CompletedFeatureContractExecution(
                selection = selection,
                verification = selection.verifier.verifier.verification.verify(
                    FeatureContractExecutionInput(selection.contractSelection),
                ),
            )
        } catch (error: Throwable) {
            CrashedFeatureContractExecution(selection, error)
        }
    }
    return FeatureContractValidationResult(plan, executions)
}

private fun FeatureContractExecutionResult.isSuccessful(): Boolean {
    return this is CompletedFeatureContractExecution && verification == FeatureContractVerificationResult.Passed
}
