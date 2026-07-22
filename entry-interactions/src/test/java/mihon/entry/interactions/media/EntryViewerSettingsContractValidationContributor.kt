package mihon.entry.interactions

import io.mockk.coEvery
import io.mockk.mockk
import mihon.entry.interactions.validation.contractExpectation
import mihon.entry.interactions.validation.productionSubjectEvaluation
import mihon.entry.interactions.validation.verifyFeatureContract
import mihon.entry.viewer.settings.ViewerSettingOverrideRepository
import mihon.feature.graph.FeatureBehaviorContract
import mihon.feature.graph.validation.FeatureContractExecutionInput
import mihon.feature.graph.validation.FeatureContractReference
import mihon.feature.graph.validation.FeatureContractVerifier
import mihon.feature.graph.validation.FeatureValidationContributionSink
import mihon.feature.graph.validation.FeatureValidationContributor
import tachiyomi.domain.entry.model.Entry

class EntryViewerSettingsContractValidationContributor : FeatureValidationContributor {
    override val owner = EntryViewerSettingsFeatureContributor.owner

    override fun contributeTo(sink: FeatureValidationContributionSink) {
        sink.addEntryBackupParticipationContract(
            ENTRY_VIEWER_SETTINGS_BACKUP_SNAPSHOT_PARTICIPANT,
            EntryViewerSettingsBehaviorContract,
            EntryViewerSettingsBackupState.serializer(),
            EntryViewerSettingsBackupState(
                listOf(EntryViewerSettingBackupValue("reader", "theme", "dark", 1)),
            ),
        )
        sink.addEntryBackupParticipationContract(
            ENTRY_VIEWER_SETTINGS_BACKUP_RESTORE_PARTICIPANT,
            EntryViewerSettingsBehaviorContract,
            EntryViewerSettingsBackupState.serializer(),
            EntryViewerSettingsBackupState(
                listOf(EntryViewerSettingBackupValue("reader", "theme", "dark", 1)),
            ),
        )
        viewerSettingsContracts.forEach { contract ->
            sink.add(
                FeatureContractVerifier(
                    FeatureContractReference(ENTRY_VIEWER_SETTINGS_FEATURE_ID, contract),
                    ::verifyViewerSettings,
                ),
            )
        }
    }
}

private val viewerSettingsContracts: List<FeatureBehaviorContract> = listOf(
    EntryViewerSettingsBehaviorContract,
    EntryViewerSettingsMigrationBehaviorContract,
)

private suspend fun verifyViewerSettings(input: FeatureContractExecutionInput) = verifyFeatureContract {
    val provider = input.provider(EntryViewerSettingsCapability.definition)
    val bindings = buildList {
        add(EntryViewerSettingsCapability.bind(provider))
        if (input.subject.integration == ENTRY_VIEWER_SETTINGS_MIGRATION_INTEGRATION_ID) {
            add(EntryMigrationCapability.bind(input.provider(EntryMigrationCapability.definition)))
        }
    }
    val evaluation = productionSubjectEvaluation(bindings, EntryViewerSettingsFeatureContributor)
    val repository = mockk<ViewerSettingOverrideRepository> {
        coEvery { getByEntryId(any()) } returns emptyList()
    }
    val feature = DefaultEntryViewerSettingsFeature(
        evaluation = evaluation,
        interaction = object : EntryViewerSettingsInteraction {
            override fun provider(type: eu.kanade.tachiyomi.source.entry.EntryType) =
                provider.takeIf { type == provider.type }
        },
        projections = provider.surfaces.map { surface ->
            object : EntryViewerSettingsScreenProjection {
                override val surfaceId = surface.id
            }
        },
        overrideRepository = repository,
        legacyMangaViewerFlagsReset = EntryLegacyMangaViewerFlagsReset { true },
        migrationStore = EntryViewerFlagsMigrationStore { _, _, _ -> true },
    )
    val source = Entry.create().copy(id = 94L, type = provider.type)

    contractExpectation(feature.isApplicable(provider.type), "Viewer Settings must be applicable")
    contractExpectation(
        feature.snapshot(source) == EntryViewerSettingsSnapshotResult.Available(emptyList()),
        "Viewer Settings must project a provider-backed snapshot",
    )
    if (input.subject.integration == ENTRY_VIEWER_SETTINGS_MIGRATION_INTEGRATION_ID) {
        val target = source.copy(id = 95L)
        contractExpectation(
            feature.copy(source, target) == EntryViewerSettingsCopyResult.Copied(0),
            "Viewer Settings must participate in Migration",
        )
    }
}
