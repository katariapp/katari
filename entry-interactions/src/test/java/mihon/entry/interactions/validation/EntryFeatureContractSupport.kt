package mihon.entry.interactions.validation

import eu.kanade.tachiyomi.source.entry.EntryType
import mihon.entry.interactions.EntryInteractionPlugin
import mihon.entry.interactions.EntryInteractionProviderBinding
import mihon.entry.interactions.createEntryInteractionComposition
import mihon.feature.graph.ContributionOwner
import mihon.feature.graph.FeatureGraphContributor
import mihon.feature.graph.FeatureGraphEvaluation
import mihon.feature.graph.SpecializedAdapter
import mihon.feature.graph.validation.FeatureContractFailure
import mihon.feature.graph.validation.FeatureContractVerificationResult

/**
 * Builds the smallest owner graph needed to execute a shared Feature coordinator for an already selected production
 * provider. The verifier cannot name or enroll a content type; production graph selection supplied the binding first.
 */
internal fun productionSubjectEvaluation(
    binding: EntryInteractionProviderBinding<*>,
    feature: FeatureGraphContributor,
): FeatureGraphEvaluation = productionSubjectEvaluation(listOf(binding), feature)

/** Multi-provider equivalent for a selected integration whose prerequisite expression joins capabilities. */
internal fun productionSubjectEvaluation(
    bindings: List<EntryInteractionProviderBinding<*>>,
    feature: FeatureGraphContributor,
): FeatureGraphEvaluation {
    require(bindings.isNotEmpty()) { "A provider-backed contract subject needs at least one binding" }
    val type = bindings.first().implementation.type
    require(bindings.all { it.implementation.type == type }) {
        "A contract subject cannot combine providers from different entry types"
    }
    val plugin = object : EntryInteractionPlugin {
        override val type = type
        override val owner = ContributionOwner("contract-subject.${type.name.lowercase()}")
        override val providerBindings = bindings
    }
    return createEntryInteractionComposition(listOf(plugin), listOf(feature)).featureGraphEvaluation
}

/** Provider-free equivalent for an unconditional Feature and an already selected production subject. */
internal fun productionSubjectEvaluation(
    type: EntryType,
    feature: FeatureGraphContributor,
    specializedAdapters: List<SpecializedAdapter<*>> = emptyList(),
): FeatureGraphEvaluation {
    val plugin = object : EntryInteractionPlugin {
        override val type = type
        override val owner = ContributionOwner("contract-subject.${type.name.lowercase()}")
        override val providerBindings = emptyList<EntryInteractionProviderBinding<*>>()
        override val specializedAdapters = specializedAdapters
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
