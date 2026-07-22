package mihon.entry.interactions

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import tachiyomi.domain.entry.service.EntryChildOwnershipResolutionPort
import tachiyomi.domain.entry.service.EntryLibraryGroupingResolutionPort
import uy.kohesive.injekt.api.addSingletonFactory
import uy.kohesive.injekt.api.get

internal val EntryMergeFeatureRuntimeModule = EntryFeatureRuntimeModule(
    id = "entry.merge",
    contributor = EntryMergeFeatureContributor,
) { context ->
    val dependencies = context.dependencies
    addSingletonFactory {
        EntryMergeConsequenceDelivery(
            host = dependencies.mergeHost,
            tracking = { get() },
            coverCleanup = dependencies.mergeCoverCleanup,
            downloadMaintenance = { get() },
        )
    }
    addSingletonFactory<EntryMergeFeature> {
        EntryMergeWorkflowCoordinator(
            evaluation = get<EntryInteractionComposition>().featureGraphEvaluation,
            host = dependencies.mergeHost,
            consequences = get(),
        )
    }
    addSingletonFactory<EntryMergeCandidateFeature> { EntryMergeCandidateCoordinator(dependencies.mergeHost) }
    addSingletonFactory<EntryMergeNavigationFeature> { EntryMergeNavigationCoordinator(dependencies.mergeHost) }
    addSingletonFactory { EntryMergeLibraryGroupingCoordinator(dependencies.mergeHost) }
    addSingletonFactory<EntryMergeLibraryGroupingFeature> { get<EntryMergeLibraryGroupingCoordinator>() }
    addSingletonFactory<EntryLibraryGroupingResolutionPort> { get<EntryMergeLibraryGroupingCoordinator>() }
    addSingletonFactory<EntryMergeBackupFeature> { EntryMergeBackupCoordinator(dependencies.mergeHost) }
    addSingletonFactory<EntryMergeLibraryLifecycleFeature> {
        EntryMergeLibraryLifecycleCoordinator(dependencies.mergeHost)
    }
    addSingletonFactory<EntryMergeMetadataRefreshFeature> {
        EntryMergeMetadataRefreshCoordinator(dependencies.mergeHost)
    }
    addSingletonFactory<EntryMergeProfileMoveFeature> { EntryMergeProfileMoveCoordinator(dependencies.mergeHost) }
    addSingletonFactory<EntryMergeConsequenceStatusFeature> {
        EntryMergeConsequenceStatusCoordinator(dependencies.mergeHost, get())
    }
    addSingletonFactory<EntryMergeMigrationFeature> { EntryMergeMigrationCoordinator(dependencies.mergeHost) }
    addSingletonFactory<EntryMergeChildOwnershipProjection> {
        EntryMergeChildOwnershipCoordinator(dependencies.mergeHost)
    }
    addSingletonFactory<EntryChildOwnershipResolutionPort> { get<EntryMergeChildOwnershipProjection>() }
    addSingletonFactory<EntryMergeDownloadOwnershipProjection> {
        EntryMergeDownloadOwnershipCoordinator(dependencies.mergeHost)
    }
    EntryFeatureRuntimeArtifacts(
        runtimeBoundaries = listOf(
            entryFeatureRuntimeBoundary { get<EntryMergeFeature>() },
            entryFeatureRuntimeBoundary { get<EntryMergeChildOwnershipProjection>() },
            entryFeatureRuntimeBoundary { get<EntryMergeDownloadOwnershipProjection>() },
            entryFeatureRuntimeBoundary { get<EntryLibraryGroupingResolutionPort>() },
        ),
        warmups = listOf {
            CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
                get<EntryMergeConsequenceDelivery>().runRetryLoop()
            }
        },
    )
}
