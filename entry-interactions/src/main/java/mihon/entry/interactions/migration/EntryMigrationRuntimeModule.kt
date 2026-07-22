package mihon.entry.interactions

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import uy.kohesive.injekt.api.addSingletonFactory
import uy.kohesive.injekt.api.get

internal val EntryMigrationFeatureRuntimeModule = EntryFeatureRuntimeModule(
    id = "entry.migration",
    contributor = EntryMigrationFeatureContributor,
) { context ->
    val dependencies = context.dependencies
    addSingletonFactory {
        EntryMigrationConsequenceDelivery(
            host = dependencies.migrationConsequenceHost,
            progress = { get() },
            playbackPreferences = { get() },
            viewerSettings = { get() },
            downloads = { get() },
            customCover = dependencies.migrationCustomCoverHost,
        )
    }
    addSingletonFactory<EntryMigrationConsequenceStatusFeature> {
        EntryMigrationConsequenceStatusCoordinator(dependencies.migrationConsequenceHost, get())
    }
    addSingletonFactory<EntryMigrationFeature> {
        DefaultEntryMigrationFeature(
            evaluation = get<EntryInteractionComposition>().featureGraphEvaluation,
            preparationHost = dependencies.migrationPreparationHost,
            executionHost = dependencies.migrationExecutionHost,
            sourceRefresh = get(),
            mergeMigration = get(),
            progress = get(),
            playbackPreferences = get(),
            viewerSettings = get(),
            downloads = get(),
            tracking = get(),
            customCover = dependencies.migrationCustomCoverHost,
            consequences = get(),
        )
    }
    EntryFeatureRuntimeArtifacts(
        runtimeBoundaries = listOf(entryFeatureRuntimeBoundary { get<EntryMigrationFeature>() }),
        warmups = listOf {
            CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
                get<EntryMigrationConsequenceDelivery>().runRetryLoop()
            }
        },
    )
}
