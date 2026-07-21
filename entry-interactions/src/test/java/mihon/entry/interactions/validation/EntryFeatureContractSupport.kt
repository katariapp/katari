package mihon.entry.interactions.validation

import eu.kanade.tachiyomi.source.entry.EntryType
import mihon.entry.interactions.EntryInteractionPlugin
import mihon.entry.interactions.EntryInteractionProviderBinding
import mihon.entry.interactions.createEntryInteractionComposition
import mihon.feature.graph.ContributionOwner
import mihon.feature.graph.FeatureGraphContributor
import mihon.feature.graph.FeatureGraphEvaluation
import mihon.feature.graph.validation.FeatureContractFailure
import mihon.feature.graph.validation.FeatureContractVerificationResult

/**
 * Builds the smallest owner graph needed to execute a shared Feature coordinator for an already selected production
 * provider. The verifier cannot name or enroll a content type; production graph selection supplied the binding first.
 */
internal fun productionSubjectEvaluation(
    binding: EntryInteractionProviderBinding<*>,
    feature: FeatureGraphContributor,
): FeatureGraphEvaluation {
    val provider = binding.implementation
    val plugin = object : EntryInteractionPlugin {
        override val type = provider.type
        override val owner = ContributionOwner("contract-subject.${provider.type.name.lowercase()}")
        override val providerBindings = listOf(binding)
    }
    return createEntryInteractionComposition(listOf(plugin), listOf(feature)).featureGraphEvaluation
}

/** Provider-free equivalent for an unconditional Feature and an already selected production subject. */
internal fun productionSubjectEvaluation(
    type: EntryType,
    feature: FeatureGraphContributor,
): FeatureGraphEvaluation {
    val plugin = object : EntryInteractionPlugin {
        override val type = type
        override val owner = ContributionOwner("contract-subject.${type.name.lowercase()}")
        override val providerBindings = emptyList<EntryInteractionProviderBinding<*>>()
    }
    return createEntryInteractionComposition(listOf(plugin), listOf(feature)).featureGraphEvaluation
}

internal suspend fun verifyFeatureContract(block: suspend () -> Unit): FeatureContractVerificationResult {
    return try {
        block()
        FeatureContractVerificationResult.Passed
    } catch (mismatch: FeatureContractMismatch) {
        FeatureContractVerificationResult.Failed(listOf(FeatureContractFailure(mismatch.message.orEmpty())))
    }
}

internal fun contractExpectation(condition: Boolean, message: String) {
    if (!condition) throw FeatureContractMismatch(message)
}

private class FeatureContractMismatch(message: String) : IllegalStateException(message)
