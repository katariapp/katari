package mihon.entry.interactions

import android.app.Application
import coil3.ComponentRegistry
import eu.kanade.tachiyomi.source.entry.EntryType
import mihon.entry.viewer.settings.ViewerSettingsProvider
import tachiyomi.domain.entry.service.EntryLibraryProgressCalculator
import uy.kohesive.injekt.api.InjektRegistrar

class EntryTypeRuntimeModule(
    val type: EntryType,
    val install: InjektRegistrar.(Application) -> EntryTypeRuntimeContribution,
)

data class EntryTypeRuntimeContribution(
    val plugin: EntryInteractionPlugin,
    val libraryProgressCalculator: EntryLibraryProgressCalculator,
    val viewerSettingsProviders: List<ViewerSettingsProvider> = emptyList(),
    val mediaCacheBuckets: List<EntryMediaCacheBucket> = emptyList(),
    val warmups: List<() -> Unit> = emptyList(),
    val imageComponentInstallers: List<EntryImageComponentInstaller> = emptyList(),
) {
    fun validate(expectedType: EntryType) {
        require(plugin.type == expectedType) {
            "Runtime module $expectedType produced plugin for ${plugin.type}"
        }
        require(libraryProgressCalculator.entryType == expectedType) {
            "Runtime module $expectedType produced library progress for ${libraryProgressCalculator.entryType}"
        }
    }
}

fun interface EntryImageComponentInstaller {
    fun install(builder: ComponentRegistry.Builder)
}

data class EntryImageComponentInstallers(
    val values: List<EntryImageComponentInstaller>,
)
