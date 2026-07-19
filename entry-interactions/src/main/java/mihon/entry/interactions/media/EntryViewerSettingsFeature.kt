package mihon.entry.interactions

import eu.kanade.tachiyomi.source.entry.EntryType
import mihon.entry.viewer.settings.ViewerSettingDefinition
import mihon.entry.viewer.settings.ViewerSettingId
import mihon.entry.viewer.settings.ViewerSettingOverride
import mihon.entry.viewer.settings.ViewerSettingOverrideRepository
import mihon.entry.viewer.settings.ViewerSettingScope
import mihon.entry.viewer.settings.ViewerSettingsProvider
import mihon.feature.graph.CapabilityExpression
import mihon.feature.graph.ContributionOwner
import mihon.feature.graph.FeatureArtifactId
import mihon.feature.graph.FeatureBehaviorContract
import mihon.feature.graph.FeatureContribution
import mihon.feature.graph.FeatureGraphContributionSink
import mihon.feature.graph.FeatureGraphContributor
import mihon.feature.graph.FeatureGraphEvaluation
import mihon.feature.graph.FeatureId
import mihon.feature.graph.FeatureIntegration
import mihon.feature.graph.FeatureIntegrationId
import mihon.feature.graph.SharedFeatureConsequence
import tachiyomi.domain.entry.model.Entry

private val ENTRY_VIEWER_SETTINGS_FEATURE_ID = FeatureId("entry.viewer-settings")
private val ENTRY_VIEWER_SETTINGS_FEATURE_OWNER = ContributionOwner("entry-viewer-settings")
private val ENTRY_VIEWER_SETTINGS_PROVIDER_INTEGRATION_ID = FeatureIntegrationId("entry.viewer-settings.provider")

private enum class EntryViewerSettingsConsequence(
    override val id: FeatureArtifactId,
) : SharedFeatureConsequence {
    DISCOVERY(FeatureArtifactId("entry.viewer-settings.discovery")),
    SETTINGS_HUB(FeatureArtifactId("entry.viewer-settings.settings-hub")),
    SCREEN_PROJECTION(FeatureArtifactId("entry.viewer-settings.screen-projection")),
    SEARCH_INDEX(FeatureArtifactId("entry.viewer-settings.search-index")),
    ENTRY_OVERRIDE(FeatureArtifactId("entry.viewer-settings.entry-override")),
    PREFERENCE_OWNERSHIP(FeatureArtifactId("entry.viewer-settings.preference-ownership")),
    RESET(FeatureArtifactId("entry.viewer-settings.reset")),
    BACKUP(FeatureArtifactId("entry.viewer-settings.backup")),
    MIGRATION(FeatureArtifactId("entry.viewer-settings.migration")),
}

private object EntryViewerSettingsBehaviorContract : FeatureBehaviorContract {
    override val id = FeatureArtifactId("entry.viewer-settings.behavior")
}

internal object EntryViewerSettingsFeatureContributor : FeatureGraphContributor {
    override val owner = ENTRY_VIEWER_SETTINGS_FEATURE_OWNER

    override fun contributeTo(sink: FeatureGraphContributionSink) {
        sink.add(
            FeatureContribution(
                feature = ENTRY_VIEWER_SETTINGS_FEATURE_ID,
                owner = owner,
                integrations = listOf(
                    FeatureIntegration(
                        id = ENTRY_VIEWER_SETTINGS_PROVIDER_INTEGRATION_ID,
                        prerequisites = CapabilityExpression.Provided(EntryViewerSettingsCapability.definition),
                        sharedConsequences = EntryViewerSettingsConsequence.entries,
                        behavioralContracts = listOf(EntryViewerSettingsBehaviorContract),
                    ),
                ),
            ),
        )
    }
}

internal fun interface EntryLegacyMangaViewerFlagsReset {
    suspend fun reset(): Boolean
}

internal class DefaultEntryViewerSettingsFeature(
    evaluation: FeatureGraphEvaluation,
    interaction: EntryViewerSettingsInteraction,
    projections: Collection<EntryViewerSettingsScreenProjection>,
    private val overrideRepository: ViewerSettingOverrideRepository,
    private val legacyMangaViewerFlagsReset: EntryLegacyMangaViewerFlagsReset,
) : EntryViewerSettingsFeature {
    private val applicableTypes = EntryViewerSettingsConsequence.entries
        .map { consequence ->
            evaluation.applicableProviderTypes<EntryViewerSettingsProvider>(
                feature = ENTRY_VIEWER_SETTINGS_FEATURE_ID,
                integration = ENTRY_VIEWER_SETTINGS_PROVIDER_INTEGRATION_ID,
                consequence = consequence.id,
            )
        }
        .also { selected ->
            check(selected.distinct().size <= 1) {
                "Viewer Settings consequences selected different provider sets: $selected"
            }
        }
        .firstOrNull()
        .orEmpty()

    private val providersByType = applicableTypes.associateWith { type ->
        requireNotNull(interaction.provider(type)) {
            "Viewer Settings graph selected $type without an operational provider"
        }
    }
    private val surfacesById = providersByType.values
        .flatMap { provider -> provider.surfaces.map { surface -> provider.type to surface } }
        .also { surfaces ->
            val duplicateIds = surfaces.groupingBy { it.second.id }.eachCount().filterValues { it > 1 }.keys
            check(duplicateIds.isEmpty()) { "Duplicate Viewer Settings surface IDs: $duplicateIds" }
        }
        .associateBy { it.second.id }
    private val projectionsById = projections
        .also { values ->
            val duplicateIds = values.groupingBy(EntryViewerSettingsScreenProjection::surfaceId)
                .eachCount()
                .filterValues { it > 1 }
                .keys
            check(duplicateIds.isEmpty()) { "Duplicate Viewer Settings screen projections: $duplicateIds" }
        }
        .associateBy(EntryViewerSettingsScreenProjection::surfaceId)

    override val destinations: List<EntryViewerSettingsDestination>

    init {
        val missingProjections = surfacesById.keys - projectionsById.keys
        check(missingProjections.isEmpty()) {
            "Viewer Settings providers are missing app screen projections: ${missingProjections.sorted()}"
        }
        val orphanProjections = projectionsById.keys - surfacesById.keys
        check(orphanProjections.isEmpty()) {
            "Viewer Settings screen projections have no provider surface: ${orphanProjections.sorted()}"
        }

        destinations = surfacesById.values
            .map { (type, surface) -> surface.toDestination(type, projectionsById.getValue(surface.id)) }
            .sortedWith(compareBy({ it.category.name }, { it.type.name }, { it.displayName }, { it.surfaceId }))
    }

    override fun isApplicable(type: EntryType): Boolean = type in applicableTypes

    override suspend fun snapshot(entry: Entry): EntryViewerSettingsSnapshotResult {
        val definitions = overrideDefinitions(entry.type)
            ?: return EntryViewerSettingsSnapshotResult.Inapplicable(entry.type)
        val overrides = overrideRepository.getByEntryId(entry.id)
            .filter { override -> definitions[override.settingId]?.accepts(override.encodedValue) == true }
        return EntryViewerSettingsSnapshotResult.Available(overrides)
    }

    override suspend fun restore(
        entry: Entry,
        overrides: List<ViewerSettingOverride>,
    ): EntryViewerSettingsRestoreResult {
        val definitions = overrideDefinitions(entry.type)
            ?: return EntryViewerSettingsRestoreResult.Inapplicable(entry.type)
        val accepted = linkedMapOf<ViewerSettingId, ViewerSettingOverride>()
        val rejected = linkedSetOf<ViewerSettingId>()
        overrides.forEach { override ->
            val definition = definitions[override.settingId]
            if (definition?.accepts(override.encodedValue) == true) {
                accepted[override.settingId] = override.copy(entryId = entry.id)
            } else {
                rejected += override.settingId
            }
        }
        accepted.values.forEach { overrideRepository.upsert(it) }
        return EntryViewerSettingsRestoreResult.Restored(
            restoredCount = accepted.size,
            rejectedSettingIds = rejected,
        )
    }

    override suspend fun copy(source: Entry, target: Entry): EntryViewerSettingsCopyResult {
        val sourceDefinitions = overrideDefinitions(source.type)
            ?: return EntryViewerSettingsCopyResult.Inapplicable(source.type, target.type)
        val targetDefinitions = overrideDefinitions(target.type)
            ?: return EntryViewerSettingsCopyResult.Inapplicable(source.type, target.type)
        val sharedDefinitions = sourceDefinitions.keys intersect targetDefinitions.keys
        val accepted = overrideRepository.getByEntryId(source.id)
            .filter { override ->
                override.settingId in sharedDefinitions &&
                    sourceDefinitions.getValue(override.settingId).accepts(override.encodedValue) &&
                    targetDefinitions.getValue(override.settingId).accepts(override.encodedValue)
            }
            .associateBy { it.settingId }
            .values
        accepted.forEach { override -> overrideRepository.upsert(override.copy(entryId = target.id)) }
        return EntryViewerSettingsCopyResult.Copied(accepted.size)
    }

    override suspend fun resetProfileOverrides(profileId: Long): EntryViewerSettingsResetResult {
        surfacesById.keys.forEach { surfaceId ->
            overrideRepository.deleteByProviderForProfile(surfaceId, profileId)
        }
        return if (legacyMangaViewerFlagsReset.reset()) {
            EntryViewerSettingsResetResult.Reset
        } else {
            EntryViewerSettingsResetResult.LegacyViewerFlagsFailed
        }
    }

    private fun overrideDefinitions(type: EntryType): Map<ViewerSettingId, ViewerSettingDefinition<*>>? {
        val provider = providersByType[type] ?: return null
        return provider.surfaces
            .flatMap(ViewerSettingsProvider::settings)
            .filter { it.scope == ViewerSettingScope.PROFILE_WITH_ENTRY_OVERRIDE }
            .associateBy(ViewerSettingDefinition<*>::id)
    }
}

private fun ViewerSettingDefinition<*>.accepts(encodedValue: String): Boolean {
    val decoded = decode(encodedValue) ?: return false
    return validateDecoded(decoded)
}

@Suppress("UNCHECKED_CAST")
private fun ViewerSettingDefinition<*>.decode(encodedValue: String): Any? =
    (codec as mihon.entry.viewer.settings.ViewerSettingCodec<Any>).decode(encodedValue)

@Suppress("UNCHECKED_CAST")
private fun ViewerSettingDefinition<*>.validateDecoded(value: Any): Boolean =
    (validate as (Any) -> Boolean)(value)

private fun ViewerSettingsProvider.toDestination(
    type: EntryType,
    projection: EntryViewerSettingsScreenProjection,
) = EntryViewerSettingsDestination(
    type = type,
    surfaceId = id,
    category = category,
    displayName = displayName,
    description = description,
    origin = origin,
    projection = projection,
)
