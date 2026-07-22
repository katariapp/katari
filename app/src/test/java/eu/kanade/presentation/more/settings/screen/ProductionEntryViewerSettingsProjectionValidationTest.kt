package eu.kanade.presentation.more.settings.screen

import mihon.entry.interactions.validation.ProductionEntryInteractionValidationEnvironment
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File

class ProductionEntryViewerSettingsProjectionValidationTest {
    @TempDir
    lateinit var temporaryDirectory: File

    @Test
    fun `production screens match every contributed viewer surface`() {
        ProductionEntryInteractionValidationEnvironment(
            temporaryDirectory,
            productionEntryViewerSettingsScreenProjectionResolver(),
        ).use { environment ->
            environment.composition()
        }
    }
}
