package eu.kanade.tachiyomi.documentation

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File

class ProductionEntryContentTypeReferencePlanTest {
    @TempDir
    lateinit var temporaryDirectory: File

    private lateinit var environment: ProductionEntryDocumentationEnvironment

    @BeforeEach
    fun setUp() {
        environment = ProductionEntryDocumentationEnvironment(temporaryDirectory)
    }

    @AfterEach
    fun tearDown() {
        environment.close()
    }

    @Test
    fun `production reference plan resolves from production composition and registrations`() {
        environment.contentTypeReferencePlan().isComplete shouldBe true
    }
}
