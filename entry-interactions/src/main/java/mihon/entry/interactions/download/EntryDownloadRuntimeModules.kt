package mihon.entry.interactions

import mihon.feature.graph.FeatureExecutionHandler
import mihon.feature.graph.FeatureExecutionParticipantBinding
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.addSingletonFactory
import uy.kohesive.injekt.api.get

internal val EntryDownloadRuntimeFeatureRuntimeModule = EntryFeatureRuntimeModule(
    id = "entry.download.runtime",
    contributor = EntryDownloadRuntimeFeatureContributor,
) { context ->
    addSingletonFactory<EntryDownloadWorkController> {
        DefaultEntryDownloadWorkController(context.application)
    }
    addSingletonFactory<EntryDownloadRuntimeCoordinator> {
        val composition = get<EntryInteractionComposition>()
        DefaultEntryDownloadRuntimeFeature(
            evaluation = composition.featureGraphEvaluation,
            interaction = composition.interactions.download,
        )
    }
    addSingletonFactory<EntryDownloadRuntimeFeature> { get<EntryDownloadRuntimeCoordinator>() }
    EntryFeatureRuntimeArtifacts(
        runtimeBoundaries = listOf(entryFeatureRuntimeBoundary { get<EntryDownloadRuntimeFeature>() }),
    )
}

internal val EntryDownloadActionFeatureRuntimeModule = EntryFeatureRuntimeModule(
    id = "entry.download.actions",
    contributor = EntryDownloadActionFeatureContributor,
) { context ->
    addSingletonFactory<EntryDownloadActionFeature> {
        val composition = get<EntryInteractionComposition>()
        DefaultEntryDownloadActionFeature(
            evaluation = composition.featureGraphEvaluation,
            interaction = composition.interactions.download,
        )
    }
    addSingletonFactory {
        EntryDownloadNotificationManager(
            context = context.application,
            downloads = get<EntryDownloadRuntimeCoordinator>(),
            actions = context.dependencies.notificationActions,
            ownership = get(),
        )
    }
    addSingletonFactory<EntryDownloadForegroundNotificationProvider> { get<EntryDownloadNotificationManager>() }
    EntryFeatureRuntimeArtifacts(
        runtimeBoundaries = listOf(entryFeatureRuntimeBoundary { get<EntryDownloadActionFeature>() }),
        warmups = listOf { get<EntryDownloadNotificationManager>().start() },
    )
}

internal val EntryAutomaticDownloadFeatureRuntimeModule = EntryFeatureRuntimeModule(
    id = "entry.download.automatic",
    contributor = EntryAutomaticDownloadFeatureContributor,
) {
    addSingletonFactory { EntryAutomaticDownloadPolicy(get(), get(), get()) }
    addSingletonFactory<EntryAutomaticDownloadFeature> {
        val composition = get<EntryInteractionComposition>()
        DefaultEntryAutomaticDownloadFeature(
            evaluation = composition.featureGraphEvaluation,
            interaction = composition.interactions.download,
            sharedPolicy = get(),
        )
    }
    EntryFeatureRuntimeArtifacts(
        runtimeBoundaries = listOf(entryFeatureRuntimeBoundary { get<EntryAutomaticDownloadFeature>() }),
    )
}

internal val EntryDownloadLifecycleFeatureRuntimeModule = EntryFeatureRuntimeModule(
    id = "entry.download.lifecycle",
    contributor = EntryDownloadLifecycleFeatureContributor,
) {
    addSingletonFactory<EntryDownloadLifecycleEventSink> {
        EntryDownloadLifecycleEventSink { event ->
            Injekt.get<EntryDownloadLifecycleFeature>().onEvent(event)
        }
    }
    addSingletonFactory<EntryDownloadLifecycleFeature> {
        val composition = get<EntryInteractionComposition>()
        DefaultEntryDownloadLifecycleFeature(
            evaluation = composition.featureGraphEvaluation,
            downloadPreferences = get(),
            getCategories = get(),
            getEntryWithChapters = get(),
            entryRepository = get(),
            downloads = composition.interactions.download,
        )
    }
    EntryFeatureRuntimeArtifacts(
        runtimeBoundaries = listOf(entryFeatureRuntimeBoundary { get<EntryDownloadLifecycleFeature>() }),
    )
}

internal val EntryDownloadConfigurationFeatureRuntimeModule = EntryFeatureRuntimeModule(
    id = "entry.download.configuration",
    contributor = EntryDownloadConfigurationFeatureContributor,
) {
    addSingletonFactory<EntryDownloadOptionsFeature> {
        val composition = get<EntryInteractionComposition>()
        DefaultEntryDownloadOptionsFeature(
            evaluation = composition.featureGraphEvaluation,
            interaction = composition.interactions.download,
        )
    }
    addSingletonFactory<EntryDownloadSettingsFeature> {
        DefaultEntryDownloadSettingsFeature(get<EntryInteractionComposition>().featureGraphEvaluation)
    }
    EntryFeatureRuntimeArtifacts(
        runtimeBoundaries = listOf(
            entryFeatureRuntimeBoundary { get<EntryDownloadOptionsFeature>() },
            entryFeatureRuntimeBoundary { get<EntryDownloadSettingsFeature>() },
        ),
    )
}

internal val EntryDownloadMaintenanceFeatureRuntimeModule = EntryFeatureRuntimeModule(
    id = "entry.download.maintenance",
    contributor = EntryDownloadMaintenanceFeatureContributor,
    additionalContributors = listOf(
        EntryDownloadLibraryMembershipContributor,
        EntryDownloadMetadataLifecycleContributor,
        EntryDownloadDestructiveRemovalContributor,
        EntryDownloadProfileMoveContributor,
    ),
) {
    addSingletonFactory<EntryDownloadMaintenanceFeature> {
        val composition = get<EntryInteractionComposition>()
        DefaultEntryDownloadMaintenanceFeature(
            evaluation = composition.featureGraphEvaluation,
            interaction = composition.interactions.download,
            ownership = get<EntryMergeDownloadOwnershipProjection>(),
        )
    }
    EntryFeatureRuntimeArtifacts(
        executionBindings = listOf(
            FeatureExecutionParticipantBinding(
                definition = ENTRY_DOWNLOAD_LIBRARY_REMOVAL_PARTICIPANT,
                handler = FeatureExecutionHandler { event ->
                    event.entries.forEach { entry ->
                        if (get<EntryDownloadMaintenanceFeature>().inspectEntry(entry) ==
                            EntryDownloadMaintenanceInspection.HasDownloads
                        ) {
                            event.outcomes.requireDownloadDecision(entry)
                        }
                    }
                },
            ),
            FeatureExecutionParticipantBinding(
                definition = ENTRY_DOWNLOAD_METADATA_CHANGE_PARTICIPANT,
                handler = FeatureExecutionHandler { event ->
                    if (event.previous.title != event.current.title) {
                        get<EntryDownloadMaintenanceFeature>().renameEntry(event.previous, event.current.title)
                    }
                },
            ),
            FeatureExecutionParticipantBinding(
                definition = ENTRY_DOWNLOAD_DESTRUCTIVE_REMOVAL_PREPARATION_PARTICIPANT,
                handler = FeatureExecutionHandler { event ->
                    event.entries.forEach { entry ->
                        when (val preparation = get<EntryDownloadMaintenanceFeature>().prepareRemoval(entry)) {
                            is EntryDownloadRemovalPreparation.Prepared -> event.outcomes.addDownloadPlan(
                                preparation.plan,
                            )
                            EntryDownloadRemovalPreparation.NothingToRemove,
                            is EntryDownloadRemovalPreparation.Inapplicable,
                            -> Unit
                        }
                    }
                },
            ),
            FeatureExecutionParticipantBinding(
                definition = ENTRY_DOWNLOAD_DESTRUCTIVE_REMOVAL_PARTICIPANT,
                handler = FeatureExecutionHandler { event ->
                    val type = event.entries.firstOrNull()?.type ?: return@FeatureExecutionHandler
                    val owners = event.outcomes.downloadPlans
                        .flatMap(EntryDownloadRemovalPlan::owners)
                        .filter { owner -> owner.type == type }
                        .distinctBy { owner -> owner.profileId to owner.id }
                    if (owners.isNotEmpty()) {
                        check(
                            get<EntryDownloadMaintenanceFeature>().applyRemoval(EntryDownloadRemovalPlan(owners)) ==
                                EntryDownloadMaintenanceResult.Performed,
                        ) { "Download cleanup was incomplete after destructive Entry removal" }
                    }
                },
            ),
            FeatureExecutionParticipantBinding(
                definition = ENTRY_DOWNLOAD_PROFILE_MOVE_PREPARATION_PARTICIPANT,
                handler = FeatureExecutionHandler { event ->
                    event.plan.removedEntries.forEach { entry ->
                        when (val preparation = get<EntryDownloadMaintenanceFeature>().prepareRemoval(entry)) {
                            is EntryDownloadRemovalPreparation.Prepared -> event.outcomes.addDownloadPlan(
                                preparation.plan,
                            )
                            EntryDownloadRemovalPreparation.NothingToRemove,
                            is EntryDownloadRemovalPreparation.Inapplicable,
                            -> Unit
                        }
                    }
                },
            ),
            FeatureExecutionParticipantBinding(
                definition = ENTRY_DOWNLOAD_PROFILE_MOVE_PARTICIPANT,
                handler = FeatureExecutionHandler { event ->
                    val owners = event.outcomes.downloadPlans
                        .flatMap(EntryDownloadRemovalPlan::owners)
                        .filter { owner -> owner.type == event.type }
                        .distinctBy { owner -> owner.profileId to owner.id }
                    if (owners.isNotEmpty()) {
                        check(
                            get<EntryDownloadMaintenanceFeature>().applyRemoval(EntryDownloadRemovalPlan(owners)) ==
                                EntryDownloadMaintenanceResult.Performed,
                        ) { "Download cleanup was incomplete after Profile movement" }
                    }
                },
            ),
        ),
        runtimeBoundaries = listOf(entryFeatureRuntimeBoundary { get<EntryDownloadMaintenanceFeature>() }),
    )
}
