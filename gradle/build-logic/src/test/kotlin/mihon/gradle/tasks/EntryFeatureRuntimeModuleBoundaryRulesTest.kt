package mihon.gradle.tasks

import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.string.shouldContain
import org.junit.jupiter.api.Test

class EntryFeatureRuntimeModuleBoundaryRulesTest {
    @Test
    fun `installed module owns its contributor without a second production list`() {
        check(
            featureSource = """
                internal object ExampleFeatureContributor : FeatureGraphContributor
                internal val ExampleFeatureRuntimeModule = EntryFeatureRuntimeModule(
                    id = "example",
                    contributor = ExampleFeatureContributor,
                ) { runtime() }
            """.trimIndent(),
            topologySource = topology("ExampleFeatureRuntimeModule"),
        ).shouldBeEmpty()
    }

    @Test
    fun `declared module omitted from production topology fails`() {
        val findings = check(
            featureSource = """
                internal object ExampleFeatureContributor : FeatureGraphContributor
                internal val ExampleFeatureRuntimeModule = EntryFeatureRuntimeModule(
                    id = "example",
                    contributor = ExampleFeatureContributor,
                ) { runtime() }
            """.trimIndent(),
            topologySource = topology(),
        )

        findings.shouldHaveSize(1)
        findings.single().reason shouldContain "must be installed exactly once"
    }

    @Test
    fun `contributor outside a production module fails`() {
        val findings = check(
            featureSource = "internal object ForgottenFeatureContributor : FeatureGraphContributor",
            topologySource = topology(),
        )

        findings.shouldHaveSize(1)
        findings.single().reason shouldContain "must belong to exactly one EntryFeatureRuntimeModule"
    }

    @Test
    fun `module declared outside the enforceable shape fails`() {
        val findings = check(
            featureSource = """
                internal object ExampleFeatureContributor : FeatureGraphContributor
                internal val ExampleModule = EntryFeatureRuntimeModule(
                    id = "example",
                    contributor = ExampleFeatureContributor,
                ) { runtime() }
            """.trimIndent(),
            topologySource = topology(),
        )

        findings.single { "must be declared" in it.reason }.reason shouldContain "installation coverage can be enforced"
    }

    @Test
    fun `descriptive behavior id used as a runtime key fails`() {
        val findings = check(
            featureSource = "val key = ExampleBehavior.DELIVERY.id.value",
            topologySource = topology(),
        )

        findings.single().reason shouldContain "cannot be used as runtime dispatch keys"
    }

    private fun check(
        featureSource: String,
        topologySource: String,
    ): List<EntryFeatureRuntimeModuleBoundaryFinding> {
        return checkEntryFeatureRuntimeModuleBoundaries(
            listOf(
                EntryFeatureRuntimeModuleBoundarySource(
                    "entry-interactions/src/main/java/mihon/entry/interactions/example/Example.kt",
                    featureSource,
                ),
                EntryFeatureRuntimeModuleBoundarySource(
                    "entry-interactions/src/main/java/mihon/entry/interactions/runtime/" +
                        "EntryInteractionProductionTopology.kt",
                    topologySource,
                ),
            ),
        )
    }

    private fun topology(vararg modules: String): String {
        return """
            internal fun productionEntryFeatureRuntimeModules(): List<EntryFeatureRuntimeModule> = listOf(
                ${modules.joinToString(",\n    ")},
            )
        """.trimIndent()
    }
}
