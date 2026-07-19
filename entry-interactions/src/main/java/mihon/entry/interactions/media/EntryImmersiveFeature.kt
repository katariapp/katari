package mihon.entry.interactions

import eu.kanade.tachiyomi.source.entry.EntryCatalogueSource
import eu.kanade.tachiyomi.source.entry.EntryType
import eu.kanade.tachiyomi.source.entry.UnifiedSource
import eu.kanade.tachiyomi.source.entry.supportedEntryTypes
import kotlinx.coroutines.CancellationException
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

private val ENTRY_IMMERSIVE_FEATURE_ID = FeatureId("entry.immersive")
private val ENTRY_IMMERSIVE_FEATURE_OWNER = ContributionOwner("entry-immersive")
private val ENTRY_IMMERSIVE_PROVIDER_INTEGRATION_ID = FeatureIntegrationId("entry.immersive.provider")
private val ENTRY_IMMERSIVE_CHILD_INTEGRATION_ID = FeatureIntegrationId("entry.immersive.first-reading-child")
private val ENTRY_IMMERSIVE_OPEN_INTEGRATION_ID = FeatureIntegrationId("entry.immersive.open-target")

private enum class EntryImmersiveConsequence(
    override val id: FeatureArtifactId,
) : SharedFeatureConsequence {
    SOURCE_AVAILABILITY(FeatureArtifactId("entry.immersive.source-availability")),
    ENTRY_AVAILABILITY(FeatureArtifactId("entry.immersive.entry-availability")),
    CATALOGUE_SURFACE(FeatureArtifactId("entry.immersive.catalogue-surface")),
    FEED_SURFACE(FeatureArtifactId("entry.immersive.feed-surface")),
    LONG_PRESS(FeatureArtifactId("entry.immersive.long-press")),
    LOAD(FeatureArtifactId("entry.immersive.load")),
    PRELOAD(FeatureArtifactId("entry.immersive.preload")),
    RENDER(FeatureArtifactId("entry.immersive.render")),
    PROGRESS(FeatureArtifactId("entry.immersive.progress")),
    LIFECYCLE(FeatureArtifactId("entry.immersive.lifecycle")),
}

private object EntryImmersiveChildConsequence : SharedFeatureConsequence {
    override val id = FeatureArtifactId("entry.immersive.first-reading-child.selection")
}

private object EntryImmersiveOpenConsequence : SharedFeatureConsequence {
    override val id = FeatureArtifactId("entry.immersive.open-target.dispatch")
}

private object EntryImmersiveBehaviorContract : FeatureBehaviorContract {
    override val id = FeatureArtifactId("entry.immersive.behavior")
}

internal object EntryImmersiveFeatureContributor : FeatureGraphContributor {
    override val owner = ENTRY_IMMERSIVE_FEATURE_OWNER

    override fun contributeTo(sink: FeatureGraphContributionSink) {
        sink.add(
            FeatureContribution(
                feature = ENTRY_IMMERSIVE_FEATURE_ID,
                owner = owner,
                integrations = listOf(
                    FeatureIntegration(
                        id = ENTRY_IMMERSIVE_PROVIDER_INTEGRATION_ID,
                        prerequisites = CapabilityExpression.Provided(EntryImmersiveCapability.definition),
                        sharedConsequences = EntryImmersiveConsequence.entries,
                        behavioralContracts = listOf(EntryImmersiveBehaviorContract),
                    ),
                    FeatureIntegration(
                        id = ENTRY_IMMERSIVE_CHILD_INTEGRATION_ID,
                        prerequisites = allOf(
                            CapabilityExpression.Provided(EntryImmersiveCapability.definition),
                            CapabilityExpression.Provided(EntryChildListCapability.definition),
                        ),
                        sharedConsequences = listOf(EntryImmersiveChildConsequence),
                    ),
                    FeatureIntegration(
                        id = ENTRY_IMMERSIVE_OPEN_INTEGRATION_ID,
                        prerequisites = allOf(
                            CapabilityExpression.Provided(EntryImmersiveCapability.definition),
                            CapabilityExpression.Provided(EntryOpenCapability.definition),
                        ),
                        sharedConsequences = listOf(EntryImmersiveOpenConsequence),
                    ),
                ),
            ),
        )
    }
}

internal class DefaultEntryImmersiveFeature(
    evaluation: FeatureGraphEvaluation,
    private val interaction: EntryImmersiveInteraction,
    private val childList: EntryChildListFeature,
) : EntryImmersiveFeature {
    private val immersiveTypes = EntryImmersiveConsequence.entries
        .map { consequence ->
            evaluation.applicableProviderTypes<EntryImmersiveProcessor>(
                feature = ENTRY_IMMERSIVE_FEATURE_ID,
                integration = ENTRY_IMMERSIVE_PROVIDER_INTEGRATION_ID,
                consequence = consequence.id,
            )
        }
        .also { selected ->
            check(selected.distinct().size <= 1) {
                "Immersive consequences selected different provider sets: $selected"
            }
        }
        .firstOrNull()
        .orEmpty()
    private val childTypes = evaluation.applicableProviderTypes<EntryChildListProcessor>(
        feature = ENTRY_IMMERSIVE_FEATURE_ID,
        integration = ENTRY_IMMERSIVE_CHILD_INTEGRATION_ID,
        consequence = EntryImmersiveChildConsequence.id,
    )
    private val openTypes = evaluation.applicableProviderTypes<EntryOpenProcessor>(
        feature = ENTRY_IMMERSIVE_FEATURE_ID,
        integration = ENTRY_IMMERSIVE_OPEN_INTEGRATION_ID,
        consequence = EntryImmersiveOpenConsequence.id,
    )

    init {
        val missingChildList = immersiveTypes.filter { type ->
            interaction.processor(type)?.loadMode == EntryImmersiveLoadMode.FIRST_READING_CHILD && type !in childTypes
        }
        check(missingChildList.isEmpty()) {
            "Child-backed Immersive providers require the Immersive + Child List relationship; " +
                "missing EntryChildList providers for $missingChildList"
        }
        immersiveTypes.forEach { type ->
            val radius = requireNotNull(interaction.processor(type)).preloadRadius
            require(radius >= 0) { "Immersive preload radius must be non-negative for $type: $radius" }
        }
    }

    override fun sourceAvailability(source: UnifiedSource?): EntryImmersiveSourceAvailability {
        source ?: return EntryImmersiveSourceAvailability.SourceUnavailable
        val catalogue = source as? EntryCatalogueSource
            ?: return EntryImmersiveSourceAvailability.SourceOptedOut
        if (!catalogue.supportsImmersiveFeed) return EntryImmersiveSourceAvailability.SourceOptedOut
        if (immersiveTypes.isEmpty()) return EntryImmersiveSourceAvailability.NoRuntimeType

        val declaredTypes = source.supportedEntryTypes()
        return if (declaredTypes != null && declaredTypes.none(immersiveTypes::contains)) {
            EntryImmersiveSourceAvailability.NoCompatibleDeclaredType(declaredTypes)
        } else {
            EntryImmersiveSourceAvailability.Available
        }
    }

    override fun availability(context: EntryImmersiveContext): EntryImmersiveAvailability {
        val processor = interaction.processor(context.entry.type)
            ?: return EntryImmersiveAvailability.Inapplicable(context.entry.type)
        return when (val reason = sourceAvailabilityForEntry(context.source)) {
            null -> EntryImmersiveAvailability.Available(
                preloadRadius = processor.preloadRadius,
                childRequirement = processor.loadMode.toChildRequirement(),
            )
            else -> EntryImmersiveAvailability.ContextuallyUnavailable(reason)
        }
    }

    override fun preloadRadius(type: EntryType): EntryImmersivePreloadRadiusResult {
        val processor = interaction.processor(type)
            ?: return EntryImmersivePreloadRadiusResult.Inapplicable(type)
        return EntryImmersivePreloadRadiusResult.Available(processor.preloadRadius)
    }

    override suspend fun load(request: EntryImmersiveLoadRequest): EntryImmersiveLoadResult {
        val processor = interaction.processor(request.entry.type)
            ?: return EntryImmersiveLoadResult.Inapplicable(request.entry.type)
        sourceAvailabilityForEntry(request.source)?.let {
            return EntryImmersiveLoadResult.ContextuallyUnavailable(it)
        }
        val source = requireNotNull(request.source)
        val child = when (processor.loadMode) {
            EntryImmersiveLoadMode.ENTRY -> null
            EntryImmersiveLoadMode.FIRST_READING_CHILD -> {
                when (
                    val result = childList.firstReadingChild(
                        entry = request.entry,
                        chapters = request.children,
                        memberIds = request.memberIds,
                    )
                ) {
                    is EntryFirstChildResult.Available -> result.chapter
                    is EntryFirstChildResult.Inapplicable -> error(
                        "Immersive graph selected child-backed ${request.entry.type} without an applicable Child List feature",
                    )
                } ?: return EntryImmersiveLoadResult.ContextuallyUnavailable(
                    EntryImmersiveUnavailableReason.NoReadingChild,
                )
            }
        }
        return try {
            val handle = interaction.load(request.context, request.entry, child, source)
            check(handle.entryType == request.entry.type) {
                "Immersive provider for ${request.entry.type} returned handle for ${handle.entryType}"
            }
            check(handle.chapterId == child?.id) {
                "Immersive provider for ${request.entry.type} returned child ${handle.chapterId}; expected ${child?.id}"
            }
            EntryImmersiveLoadResult.Loaded(handle, child)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Throwable) {
            EntryImmersiveLoadResult.Failed(e)
        }
    }

    override fun renderer(handle: EntryImmersiveHandle): EntryImmersiveRenderer = interaction.renderer(handle)

    override suspend fun persistProgress(handle: EntryImmersiveHandle, progress: EntryImmersiveProgress) {
        interaction.persistProgress(handle, progress)
    }

    override fun openTarget(handle: EntryImmersiveHandle): EntryImmersiveOpenTargetResult {
        if (handle.entryType !in openTypes) return EntryImmersiveOpenTargetResult.Inapplicable(handle.entryType)
        val childId = handle.chapterId ?: return EntryImmersiveOpenTargetResult.NotOpenable
        return EntryImmersiveOpenTargetResult.Available(childId)
    }

    override fun release(handle: EntryImmersiveHandle) {
        interaction.release(handle)
    }

    /** Returned Entry.type is authoritative; descriptive source metadata never rejects an actual entry. */
    private fun sourceAvailabilityForEntry(source: UnifiedSource?): EntryImmersiveUnavailableReason? {
        source ?: return EntryImmersiveUnavailableReason.SourceUnavailable
        val catalogue = source as? EntryCatalogueSource ?: return EntryImmersiveUnavailableReason.SourceOptedOut
        return if (catalogue.supportsImmersiveFeed) null else EntryImmersiveUnavailableReason.SourceOptedOut
    }
}

private fun EntryImmersiveLoadMode.toChildRequirement(): EntryImmersiveChildRequirement {
    return when (this) {
        EntryImmersiveLoadMode.ENTRY -> EntryImmersiveChildRequirement.NONE
        EntryImmersiveLoadMode.FIRST_READING_CHILD -> EntryImmersiveChildRequirement.FIRST_READING_CHILD
    }
}
