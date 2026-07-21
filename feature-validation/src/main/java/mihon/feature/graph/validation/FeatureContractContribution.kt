package mihon.feature.graph.validation

import mihon.feature.graph.ContextEvidence
import mihon.feature.graph.ContributionOwner
import mihon.feature.graph.FeatureArtifactId
import mihon.feature.graph.FeatureContractScenarioId
import mihon.feature.graph.FeatureId
import mihon.feature.graph.FeatureIntegrationId
import java.util.ServiceLoader

/** Stable reference to one behavioral contract declared by a production feature contribution. */
data class FeatureContractReference(
    val feature: FeatureId,
    val contract: FeatureArtifactId,
)

/** Framework-neutral executable validation owned by the feature that declared [contract]. */
data class FeatureContractVerifier(
    val contract: FeatureContractReference,
    val verification: FeatureContractVerification,
)

fun interface FeatureContractVerification {
    suspend fun verify(input: FeatureContractExecutionInput): FeatureContractVerificationResult
}

/** Typed evidence factory for one context in which a conditional contract must become applicable. */
fun interface FeatureContractEvidenceFactory {
    fun create(input: FeatureContractScenarioInput): List<ContextEvidence<*>>
}

data class FeatureContractScenario(
    val id: FeatureContractScenarioId,
    val contract: FeatureContractReference,
    val integration: FeatureIntegrationId,
    val evidenceFactory: FeatureContractEvidenceFactory,
)

/** Validation-only owner contribution discovered from the validation classpath. */
interface FeatureValidationContributor {
    val owner: ContributionOwner

    fun contributeTo(sink: FeatureValidationContributionSink)
}

fun featureValidationContributor(
    owner: ContributionOwner,
    contribute: FeatureValidationContributionSink.() -> Unit,
): FeatureValidationContributor {
    return object : FeatureValidationContributor {
        override val owner = owner

        override fun contributeTo(sink: FeatureValidationContributionSink) {
            sink.contribute()
        }
    }
}

class FeatureValidationContributionSink internal constructor(
    private val owner: ContributionOwner,
) {
    private val verifiers = mutableListOf<OwnedFeatureContractVerifier>()
    private val scenarios = mutableListOf<OwnedFeatureContractScenario>()

    fun add(verifier: FeatureContractVerifier) {
        verifiers += OwnedFeatureContractVerifier(owner, verifier)
    }

    fun add(scenario: FeatureContractScenario) {
        scenarios += OwnedFeatureContractScenario(owner, scenario)
    }

    internal fun snapshot(): DiscoveredFeatureValidationContributions {
        return DiscoveredFeatureValidationContributions(verifiers.toList(), scenarios.toList())
    }
}

data class OwnedFeatureContractVerifier(
    val owner: ContributionOwner,
    val verifier: FeatureContractVerifier,
)

data class OwnedFeatureContractScenario(
    val owner: ContributionOwner,
    val scenario: FeatureContractScenario,
)

data class DiscoveredFeatureValidationContributions(
    val verifiers: List<OwnedFeatureContractVerifier>,
    val scenarios: List<OwnedFeatureContractScenario>,
)

fun discoverFeatureValidationContributions(
    contributors: Iterable<FeatureValidationContributor>,
): DiscoveredFeatureValidationContributions {
    val snapshots = contributors.map { contributor ->
        FeatureValidationContributionSink(contributor.owner)
            .also(contributor::contributeTo)
            .snapshot()
    }
    return DiscoveredFeatureValidationContributions(
        verifiers = snapshots.flatMap { it.verifiers },
        scenarios = snapshots.flatMap { it.scenarios },
    )
}

/** Discovers independently packaged validation contributors without a central contract or feature list. */
fun loadFeatureValidationContributors(
    classLoader: ClassLoader = Thread.currentThread().contextClassLoader,
): List<FeatureValidationContributor> {
    return ServiceLoader.load(FeatureValidationContributor::class.java, classLoader).toList()
}
