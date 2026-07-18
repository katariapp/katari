package mihon.entry.interactions

import eu.kanade.tachiyomi.source.entry.EntryType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
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
import mihon.feature.graph.allOf

private val ENTRY_PREVIEW_FEATURE_ID = FeatureId("entry.preview")
private val ENTRY_PREVIEW_FEATURE_OWNER = ContributionOwner("entry-preview")
private val ENTRY_PREVIEW_PROVIDER_INTEGRATION_ID = FeatureIntegrationId("entry.preview.provider")
private val ENTRY_PREVIEW_CONFIGURATION_INTEGRATION_ID = FeatureIntegrationId("entry.preview.configuration")
private val ENTRY_PREVIEW_CHILD_INTEGRATION_ID = FeatureIntegrationId("entry.preview.first-reading-child")
private val ENTRY_PREVIEW_OPEN_INTEGRATION_ID = FeatureIntegrationId("entry.preview.open-target")

private enum class EntryPreviewConsequence(
    override val id: FeatureArtifactId,
) : SharedFeatureConsequence {
    AVAILABILITY(FeatureArtifactId("entry.preview.availability")),
    LOAD(FeatureArtifactId("entry.preview.load")),
    PAGE_LOAD(FeatureArtifactId("entry.preview.page-load")),
    LIFECYCLE(FeatureArtifactId("entry.preview.lifecycle")),
    ENTRY_SURFACE(FeatureArtifactId("entry.preview.entry-surface")),
    BROWSE_SURFACE(FeatureArtifactId("entry.preview.browse-surface")),
}

private object EntryPreviewConfigurationConsequence : SharedFeatureConsequence {
    override val id = FeatureArtifactId("entry.preview.configuration.settings")
}

private object EntryPreviewChildConsequence : SharedFeatureConsequence {
    override val id = FeatureArtifactId("entry.preview.first-reading-child.selection")
}

private object EntryPreviewOpenConsequence : SharedFeatureConsequence {
    override val id = FeatureArtifactId("entry.preview.open-target.dispatch")
}

private object EntryPreviewBehaviorContract : FeatureBehaviorContract {
    override val id = FeatureArtifactId("entry.preview.behavior")
}

internal object EntryPreviewFeatureContributor : FeatureGraphContributor {
    override val owner = ENTRY_PREVIEW_FEATURE_OWNER

    override fun contributeTo(sink: FeatureGraphContributionSink) {
        sink.add(
            FeatureContribution(
                feature = ENTRY_PREVIEW_FEATURE_ID,
                owner = owner,
                integrations = listOf(
                    FeatureIntegration(
                        id = ENTRY_PREVIEW_PROVIDER_INTEGRATION_ID,
                        prerequisites = CapabilityExpression.Provided(EntryPreviewCapability.definition),
                        sharedConsequences = EntryPreviewConsequence.entries,
                        behavioralContracts = listOf(EntryPreviewBehaviorContract),
                    ),
                    FeatureIntegration(
                        id = ENTRY_PREVIEW_CONFIGURATION_INTEGRATION_ID,
                        prerequisites = allOf(
                            CapabilityExpression.Provided(EntryPreviewCapability.definition),
                            CapabilityExpression.Provided(EntryPreviewConfigurationCapability.definition),
                        ),
                        sharedConsequences = listOf(EntryPreviewConfigurationConsequence),
                    ),
                    FeatureIntegration(
                        id = ENTRY_PREVIEW_CHILD_INTEGRATION_ID,
                        prerequisites = allOf(
                            CapabilityExpression.Provided(EntryPreviewCapability.definition),
                            CapabilityExpression.Provided(EntryChildListCapability.definition),
                        ),
                        sharedConsequences = listOf(EntryPreviewChildConsequence),
                    ),
                    FeatureIntegration(
                        id = ENTRY_PREVIEW_OPEN_INTEGRATION_ID,
                        prerequisites = allOf(
                            CapabilityExpression.Provided(EntryPreviewCapability.definition),
                            CapabilityExpression.Provided(EntryOpenCapability.definition),
                        ),
                        sharedConsequences = listOf(EntryPreviewOpenConsequence),
                    ),
                ),
            ),
        )
    }
}

internal class DefaultEntryPreviewFeature(
    evaluation: FeatureGraphEvaluation,
    private val interaction: EntryPreviewInteraction,
    private val childList: EntryChildListFeature,
) : EntryPreviewFeature {
    private val previewTypes = EntryPreviewConsequence.entries
        .map { consequence ->
            evaluation.applicableProviderTypes<EntryPreviewProcessor>(
                feature = ENTRY_PREVIEW_FEATURE_ID,
                integration = ENTRY_PREVIEW_PROVIDER_INTEGRATION_ID,
                consequence = consequence.id,
            )
        }
        .also { selected ->
            check(selected.distinct().size <= 1) {
                "Preview consequences selected different provider sets: $selected"
            }
        }
        .firstOrNull()
        .orEmpty()
    private val configuredTypes = evaluation.applicableProviderTypes<EntryPreviewConfigurationProvider>(
        feature = ENTRY_PREVIEW_FEATURE_ID,
        integration = ENTRY_PREVIEW_CONFIGURATION_INTEGRATION_ID,
        consequence = EntryPreviewConfigurationConsequence.id,
    )
    private val childTypes = evaluation.applicableProviderTypes<EntryChildListProcessor>(
        feature = ENTRY_PREVIEW_FEATURE_ID,
        integration = ENTRY_PREVIEW_CHILD_INTEGRATION_ID,
        consequence = EntryPreviewChildConsequence.id,
    )
    private val openTypes = evaluation.applicableProviderTypes<EntryOpenProcessor>(
        feature = ENTRY_PREVIEW_FEATURE_ID,
        integration = ENTRY_PREVIEW_OPEN_INTEGRATION_ID,
        consequence = EntryPreviewOpenConsequence.id,
    )

    override val settings: List<EntryPreviewSettings> = configuredTypes
        .sortedBy(EntryType::name)
        .map { type -> requireNotNull(interaction.configuration(type)).settings }

    init {
        val missingChildList = previewTypes.filter { type ->
            interaction.processor(type)?.loadMode == EntryPreviewLoadMode.FIRST_READING_CHILD && type !in childTypes
        }
        check(missingChildList.isEmpty()) {
            "Child-backed Preview providers require the Preview + Child List relationship; " +
                "missing EntryChildList providers for $missingChildList"
        }
    }

    override fun isApplicable(type: EntryType): Boolean = type in previewTypes

    override fun isOpenApplicable(type: EntryType): Boolean = type in openTypes

    override fun availability(context: EntryPreviewContext): EntryPreviewAvailability {
        val processor = interaction.processor(context.entry.type)
            ?: return EntryPreviewAvailability.Inapplicable(context.entry.type)
        val config = config(context.entry.type)
        return when (val contextual = processor.contextAvailability(context.entry, context.source)) {
            EntryPreviewContextResult.Available -> if (config.enabled) {
                EntryPreviewAvailability.Available(config)
            } else {
                EntryPreviewAvailability.Disabled(config)
            }
            is EntryPreviewContextResult.Unavailable ->
                EntryPreviewAvailability.ContextuallyUnavailable(config, contextual.reason)
        }
    }

    override fun availabilityChanges(context: EntryPreviewContext): Flow<EntryPreviewAvailability> {
        if (!isApplicable(context.entry.type)) return flowOf(EntryPreviewAvailability.Inapplicable(context.entry.type))
        return configChanges(context.entry.type).map { config -> availability(context, config) }
    }

    override suspend fun load(request: EntryPreviewLoadRequest): EntryPreviewLoadResult {
        val entry = request.previewContext.entry
        val processor = interaction.processor(entry.type)
            ?: return EntryPreviewLoadResult.Inapplicable(entry.type)
        val config = config(entry.type)
        if (!config.enabled) return EntryPreviewLoadResult.Disabled(config)

        val child = when (processor.loadMode) {
            EntryPreviewLoadMode.ENTRY -> null
            EntryPreviewLoadMode.FIRST_READING_CHILD -> {
                val first = when (
                    val result = childList.firstReadingChild(
                        entry = entry,
                        chapters = request.children.map(EntryPreviewChildCandidate::child),
                        memberIds = request.memberIds,
                    )
                ) {
                    is EntryFirstChildResult.Available -> result.chapter
                    is EntryFirstChildResult.Inapplicable -> error(
                        "Preview graph selected child-backed ${entry.type} without an applicable Child List feature",
                    )
                } ?: return EntryPreviewLoadResult.ContextuallyUnavailable(
                    EntryPreviewUnavailableReason.NoReadingChild,
                )
                request.children.firstOrNull { it.child.id == first.id }
                    ?: error("F17 selected child ${first.id} outside the F19 candidate set")
            }
        }
        val source = child?.source ?: request.previewContext.source
        when (val contextual = processor.contextAvailability(entry, source)) {
            EntryPreviewContextResult.Available -> Unit
            is EntryPreviewContextResult.Unavailable ->
                return EntryPreviewLoadResult.ContextuallyUnavailable(contextual.reason)
        }
        return EntryPreviewLoadResult.Loaded(
            interaction.loadPreview(
                context = request.context,
                entry = entry,
                chapter = child?.child,
                source = source,
                pageCount = config.pageCount,
            ),
        )
    }

    override suspend fun loadPage(handle: EntryPreviewHandle, pageIndex: Int) {
        interaction.loadPage(handle, pageIndex)
    }

    override fun openTarget(handle: EntryPreviewHandle, pageIndex: Int): EntryPreviewOpenTargetResult {
        if (handle.entryType !in openTypes) return EntryPreviewOpenTargetResult.Inapplicable(handle.entryType)
        val page = handle.pages.firstOrNull { it.index == pageIndex } ?: return EntryPreviewOpenTargetResult.NotOpenable
        val childId = handle.chapterId ?: return EntryPreviewOpenTargetResult.NotOpenable
        return if (page.canOpen) {
            EntryPreviewOpenTargetResult.Available(childId, page.index)
        } else {
            EntryPreviewOpenTargetResult.NotOpenable
        }
    }

    override fun release(handle: EntryPreviewHandle) {
        interaction.release(handle)
    }

    private fun config(type: EntryType): EntryPreviewConfig =
        if (type in configuredTypes) {
            requireNotNull(interaction.configuration(type)).config()
        } else {
            EntryPreviewConfig.Default
        }

    private fun configChanges(type: EntryType): Flow<EntryPreviewConfig> =
        if (type in configuredTypes) {
            requireNotNull(interaction.configuration(type)).configChanges()
        } else {
            flowOf(EntryPreviewConfig.Default)
        }

    private fun availability(
        context: EntryPreviewContext,
        config: EntryPreviewConfig,
    ): EntryPreviewAvailability {
        val processor = requireNotNull(interaction.processor(context.entry.type))
        return when (val contextual = processor.contextAvailability(context.entry, context.source)) {
            EntryPreviewContextResult.Available -> if (config.enabled) {
                EntryPreviewAvailability.Available(config)
            } else {
                EntryPreviewAvailability.Disabled(config)
            }
            is EntryPreviewContextResult.Unavailable ->
                EntryPreviewAvailability.ContextuallyUnavailable(config, contextual.reason)
        }
    }
}
