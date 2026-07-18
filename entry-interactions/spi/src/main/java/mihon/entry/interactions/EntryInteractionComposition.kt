package mihon.entry.interactions

import eu.kanade.tachiyomi.source.entry.EntryType
import mihon.feature.graph.FeatureArtifactSelection
import mihon.feature.graph.FeatureGraph
import mihon.feature.graph.FeatureGraphContributor
import mihon.feature.graph.FeatureGraphEvaluation
import mihon.feature.graph.discoverAndAssembleFeatureGraph
import mihon.feature.graph.evaluateFeatureGraph
import mihon.feature.graph.selectFeatureArtifacts
import tachiyomi.domain.entry.model.Entry

fun createEntryInteractions(
    plugins: List<EntryInteractionPlugin>,
    featureContributors: List<FeatureGraphContributor>,
): EntryInteractions = createEntryInteractionComposition(plugins, featureContributors).interactions

data class EntryInteractionComposition(
    val interactions: EntryInteractions,
    val featureGraph: FeatureGraph,
    val featureGraphEvaluation: FeatureGraphEvaluation,
    val featureArtifacts: FeatureArtifactSelection,
)

fun createEntryInteractionComposition(
    plugins: List<EntryInteractionPlugin>,
    featureContributors: List<FeatureGraphContributor>,
): EntryInteractionComposition {
    validateEntryInteractionPlugins(plugins)
    val featureGraph = discoverAndAssembleFeatureGraph(plugins + featureContributors)
    val featureGraphEvaluation = evaluateFeatureGraph(featureGraph)
    val featureArtifacts = selectFeatureArtifacts(featureGraph, featureGraphEvaluation)
    val providers = EntryInteractionProviderIndex(plugins)
    return EntryInteractionComposition(
        interactions = DefaultEntryInteractions(
            openProcessors = providers[EntryOpenCapability],
            continueProcessors = providers[EntryContinueCapability],
            downloadProcessors = providers[EntryDownloadCapability],
            downloadOptionsProcessors = providers[EntryDownloadOptionsCapability],
            bulkDownloadCandidateProcessors = providers[EntryBulkDownloadCandidateCapability],
            migrationProviders = providers[EntryMigrationCapability],
            mergeProviders = providers[EntryMergeCapability],
            consumptionProcessors = providers[EntryConsumptionCapability],
            bookmarkProcessors = providers[EntryBookmarkCapability],
            progressProcessors = providers[EntryProgressCapability],
            playbackPreferencesProcessors = providers[EntryPlaybackPreferencesCapability],
            childListProcessors = providers[EntryChildListCapability],
            childProgressProcessors = providers[EntryChildProgressCapability],
            childGroupFilterProcessors = providers[EntryChildGroupFilterCapability],
            outsideReleasePeriodFilterProviders = providers[EntryOutsideReleasePeriodFilterCapability],
            previewProcessors = providers[EntryPreviewCapability],
            immersiveProcessors = providers[EntryImmersiveCapability],
        ),
        featureGraph = featureGraph,
        featureGraphEvaluation = featureGraphEvaluation,
        featureArtifacts = featureArtifacts,
    )
}

private fun validateEntryInteractionPlugins(plugins: List<EntryInteractionPlugin>) {
    plugins.forEach(EntryInteractionPlugin::validateContribution)
    plugins.groupBy(EntryInteractionPlugin::type)
        .filterValues { it.size > 1 }
        .forEach { (type, duplicates) ->
            error(
                "Duplicate Entry interaction plugin for $type from " +
                    duplicates.map { it.owner }.distinct().sortedBy { it.value },
            )
        }
}

private class EntryInteractionProviderIndex(
    plugins: List<EntryInteractionPlugin>,
) {
    private val bindings = plugins.flatMap(EntryInteractionPlugin::providerBindings)

    operator fun <P : EntryInteractionProvider> get(
        capability: EntryInteractionCapability<P>,
    ): Map<EntryType, P> {
        return buildMap {
            bindings
                .filter { it.capability === capability }
                .forEach { binding ->
                    @Suppress("UNCHECKED_CAST")
                    val provider = binding.implementation as P
                    val previous = put(provider.type, provider)
                    check(previous == null) {
                        "Duplicate provider for ${capability.definition.id} and EntryType ${provider.type}"
                    }
                }
        }
    }
}

private class DefaultEntryInteractions(
    openProcessors: Map<EntryType, EntryOpenProcessor>,
    continueProcessors: Map<EntryType, EntryContinueProcessor>,
    downloadProcessors: Map<EntryType, EntryDownloadProcessor>,
    downloadOptionsProcessors: Map<EntryType, EntryDownloadOptionsProcessor>,
    bulkDownloadCandidateProcessors: Map<EntryType, EntryBulkDownloadCandidateProcessor>,
    migrationProviders: Map<EntryType, EntryMigrationProvider>,
    mergeProviders: Map<EntryType, EntryMergeProvider>,
    consumptionProcessors: Map<EntryType, EntryConsumptionProcessor>,
    bookmarkProcessors: Map<EntryType, EntryBookmarkProcessor>,
    progressProcessors: Map<EntryType, EntryProgressProcessor>,
    playbackPreferencesProcessors: Map<EntryType, EntryPlaybackPreferencesProcessor>,
    childListProcessors: Map<EntryType, EntryChildListProcessor>,
    childProgressProcessors: Map<EntryType, EntryChildProgressProcessor>,
    childGroupFilterProcessors: Map<EntryType, EntryChildGroupFilterProcessor>,
    outsideReleasePeriodFilterProviders: Map<EntryType, EntryOutsideReleasePeriodFilterProvider>,
    previewProcessors: Map<EntryType, EntryPreviewInteraction>,
    immersiveProcessors: Map<EntryType, EntryImmersiveProcessor>,
) : EntryInteractions {
    override val open: EntryOpenInteraction = ProviderBackedEntryOpenInteraction(openProcessors)
    override val continueEntry: EntryContinueInteraction = ProviderBackedEntryContinueInteraction(continueProcessors)
    override val download: EntryDownloadInteraction =
        EntryDownloadInteractionDispatch(
            processors = downloadProcessors,
            optionsProcessors = downloadOptionsProcessors,
            bulkCandidateProcessors = bulkDownloadCandidateProcessors,
        )
    override val capability: EntryCapabilityInteraction =
        ProviderBackedEntryCapabilityInteraction(migrationProviders, mergeProviders)
    override val consumption: EntryConsumptionInteraction =
        ProviderBackedEntryConsumptionInteraction(consumptionProcessors)
    override val bookmark: EntryBookmarkInteraction = ProviderBackedEntryBookmarkInteraction(bookmarkProcessors)
    override val progress: EntryProgressInteraction = ProviderBackedEntryProgressInteraction(progressProcessors)
    override val playbackPreferences: EntryPlaybackPreferencesInteraction =
        ProviderBackedEntryPlaybackPreferencesInteraction(playbackPreferencesProcessors)
    override val childList: EntryChildListInteraction =
        ProviderBackedEntryChildListInteraction(childListProcessors, childProgressProcessors)
    override val childGroupFilter: EntryChildGroupFilterInteraction =
        ProviderBackedEntryChildGroupFilterInteraction(childGroupFilterProcessors)
    override val libraryFilter: EntryLibraryFilterInteraction =
        ProviderBackedEntryLibraryFilterInteraction(outsideReleasePeriodFilterProviders)
    override val preview: EntryPreviewInteraction = ProviderBackedEntryPreviewInteraction(previewProcessors)
    override val immersive: EntryImmersiveInteraction =
        ProviderBackedEntryImmersiveInteraction(immersiveProcessors)
}
