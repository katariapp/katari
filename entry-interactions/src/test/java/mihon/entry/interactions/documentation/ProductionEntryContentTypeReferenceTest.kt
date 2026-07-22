package mihon.entry.interactions.documentation

import io.kotest.matchers.shouldBe
import mihon.entry.interactions.validation.ProductionEntryInteractionValidationEnvironment
import mihon.feature.graph.validation.projection.classifyFeatureProjectionParticipation
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File

class ProductionEntryContentTypeReferenceTest {
    @TempDir
    lateinit var temporaryDirectory: File

    private lateinit var environment: ProductionEntryInteractionValidationEnvironment

    @BeforeEach
    fun setUp() {
        environment = ProductionEntryInteractionValidationEnvironment(temporaryDirectory)
    }

    @AfterEach
    fun tearDown() {
        environment.close()
    }

    @Test
    fun `every discovered Feature owns its content reference participation`() {
        val composition = environment.composition()

        val result = classifyFeatureProjectionParticipation(
            graph = composition.featureGraph,
            projectionType = EntryContentTypeReferenceProjection::class,
        )

        result.isComplete shouldBe true
        result.participation.size shouldBe composition.featureGraph.features.size
    }
}
