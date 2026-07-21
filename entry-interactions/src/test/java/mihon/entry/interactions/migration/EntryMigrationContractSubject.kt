package mihon.entry.interactions

import eu.kanade.tachiyomi.source.entry.EntryType
import io.mockk.coEvery
import io.mockk.mockk
import mihon.entry.interactions.validation.productionSubjectEvaluation
import mihon.feature.graph.validation.FeatureContractExecutionInput

internal class EntryMigrationContractExecutionRecord {
    var progress = false
    var playback = false
    var viewerSettings = false
    var downloadRemoval = false
}

internal fun entryMigrationContractFeature(
    input: FeatureContractExecutionInput,
    type: EntryType,
    host: RecordingEntryMigrationHost,
    record: EntryMigrationContractExecutionRecord,
): EntryMigrationFeature {
    val bindings = buildList {
        add(EntryMigrationCapability.bind(input.provider(EntryMigrationCapability.definition)))
        input.providerOrNull(EntryConsumptionCapability.definition)?.let { add(EntryConsumptionCapability.bind(it)) }
        input.providerOrNull(EntryBookmarkCapability.definition)?.let { add(EntryBookmarkCapability.bind(it)) }
        input.providerOrNull(EntryProgressCapability.definition)?.let { add(EntryProgressCapability.bind(it)) }
        input.providerOrNull(EntryPlaybackPreferencesCapability.definition)
            ?.let { add(EntryPlaybackPreferencesCapability.bind(it)) }
        input.providerOrNull(EntryViewerSettingsCapability.definition)
            ?.let { add(EntryViewerSettingsCapability.bind(it)) }
        input.providerOrNull(EntryDownloadCapability.definition)?.let { add(EntryDownloadCapability.bind(it)) }
    }
    val progress = mockk<EntryProgressFeature> {
        coEvery { prepareMigration(any(), any(), any()) } answers {
            record.progress = true
            EntryProgressMigrationPreparation.Inapplicable(setOf(type))
        }
    }
    val playback = mockk<EntryPlaybackPreferencesFeature> {
        coEvery { prepareMigration(any(), any()) } answers {
            record.playback = true
            EntryPlaybackPreferencesMigrationPreparation.Inapplicable(setOf(type))
        }
    }
    val viewer = mockk<EntryViewerSettingsFeature> {
        coEvery { prepareMigration(any(), any()) } answers {
            record.viewerSettings = true
            EntryViewerSettingsMigrationPreparation.Inapplicable(setOf(type))
        }
    }
    val downloads = mockk<EntryDownloadMaintenanceFeature> {
        coEvery { inspectEntry(any()) } returns EntryDownloadMaintenanceInspection.HasDownloads
        coEvery { prepareRemoval(any()) } answers {
            record.downloadRemoval = true
            EntryDownloadRemovalPreparation.NothingToRemove
        }
    }
    return DefaultEntryMigrationFeature(
        productionSubjectEvaluation(bindings, EntryMigrationFeatureContributor),
        host,
        host,
        mockk { coEvery { refresh(any()) } returns refreshedResult },
        mockk {
            coEvery { participateInReplacementTransaction(any()) } returns
                EntryMergeMigrationReplacementResult.Applied
        },
        progress,
        playback,
        viewer,
        downloads,
        mockk {
            coEvery { prepareMigrationTracks(any(), any(), any()) } returns
                EntryTrackingMigrationPreparationResult.Prepared(emptyList())
        },
        mockk(relaxed = true),
        mockk { coEvery { deliverOperation(any()) } returns EntryMigrationFollowUp.COMPLETE },
        clockMillis = { 999L },
    )
}

private val refreshedResult = EntrySourceRefreshResult.Refreshed(emptyList(), 0, 0, 0, false)
