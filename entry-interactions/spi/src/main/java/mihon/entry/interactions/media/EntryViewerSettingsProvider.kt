package mihon.entry.interactions

import eu.kanade.tachiyomi.source.entry.EntryType
import mihon.entry.viewer.settings.ViewerSettingsProvider
import mihon.feature.graph.CapabilityId

/** Type-owned collection of genuine viewer engines and their settings definitions. */
interface EntryViewerSettingsProvider : EntryInteractionProvider {
    val surfaces: List<ViewerSettingsProvider>
}

class DefaultEntryViewerSettingsProvider(
    override val type: EntryType,
    override val surfaces: List<ViewerSettingsProvider>,
) : EntryViewerSettingsProvider {
    init {
        require(surfaces.isNotEmpty()) { "A Viewer Settings provider must contribute at least one surface" }
        val duplicateSurfaceIds = surfaces.groupingBy(ViewerSettingsProvider::id)
            .eachCount()
            .filterValues { it > 1 }
            .keys
        require(duplicateSurfaceIds.isEmpty()) {
            "Duplicate Viewer Settings surfaces for $type: $duplicateSurfaceIds"
        }
        surfaces.forEach { surface ->
            require(surface.id.isNotBlank()) { "Viewer Settings surface ID must not be blank for $type" }
            val duplicateSettingIds = surface.settings.groupingBy { it.id }.eachCount().filterValues { it > 1 }.keys
            require(duplicateSettingIds.isEmpty()) {
                "Duplicate Viewer Settings definitions for ${surface.id}: $duplicateSettingIds"
            }
            surface.settings.forEach { setting ->
                require(setting.id.providerId == surface.id) {
                    "Viewer setting ${setting.id} must belong to surface ${surface.id}"
                }
            }
        }
    }
}

val EntryViewerSettingsCapability = entryInteractionCapability<EntryViewerSettingsProvider>(
    id = CapabilityId("entry.viewer-settings"),
)

interface EntryViewerSettingsInteraction {
    fun provider(type: EntryType): EntryViewerSettingsProvider?
}

internal class ProviderBackedEntryViewerSettingsInteraction(
    private val providers: Map<EntryType, EntryViewerSettingsProvider>,
) : EntryViewerSettingsInteraction {
    override fun provider(type: EntryType): EntryViewerSettingsProvider? = providers[type]
}
