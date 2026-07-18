package mihon.entry.interactions

import eu.kanade.tachiyomi.source.entry.EntryType
import mihon.feature.graph.CapabilityExpression
import mihon.feature.graph.ContributionOwner
import mihon.feature.graph.FeatureArtifactId
import mihon.feature.graph.FeatureContribution
import mihon.feature.graph.FeatureGraphContributionSink
import mihon.feature.graph.FeatureGraphContributor
import mihon.feature.graph.FeatureGraphEvaluation
import mihon.feature.graph.FeatureId
import mihon.feature.graph.FeatureIntegration
import mihon.feature.graph.FeatureIntegrationId
import mihon.feature.graph.SharedFeatureConsequence
import tachiyomi.domain.entry.model.Entry
import tachiyomi.domain.entry.model.EntryChapter

private val ENTRY_CONSUMPTION_FEATURE_ID = FeatureId("entry.consumption")
private val ENTRY_CONSUMPTION_INTEGRATION_ID = FeatureIntegrationId("entry.consumption.provider")
private val ENTRY_CONSUMPTION_FEATURE_OWNER = ContributionOwner("entry-consumption")

internal enum class EntryConsumptionConsequence(
    override val id: FeatureArtifactId,
) : SharedFeatureConsequence {
    DISPATCH_AND_ELIGIBILITY(FeatureArtifactId("entry.consumption.dispatch-and-eligibility")),
    ENTRY_ACTIONS(FeatureArtifactId("entry.consumption.entry-actions")),
    LIBRARY_ACTIONS(FeatureArtifactId("entry.consumption.library-actions")),
    UPDATE_ACTIONS(FeatureArtifactId("entry.consumption.update-actions")),
    NOTIFICATION_ACTION(FeatureArtifactId("entry.consumption.notification-action")),
    TRACKING_SYNC(FeatureArtifactId("entry.consumption.tracking-sync")),
    DOWNLOAD_LIFECYCLE_EVENT(FeatureArtifactId("entry.consumption.download-lifecycle-event")),
}

internal object EntryConsumptionFeatureContributor : FeatureGraphContributor {
    override val owner = ENTRY_CONSUMPTION_FEATURE_OWNER

    override fun contributeTo(sink: FeatureGraphContributionSink) {
        sink.add(
            FeatureContribution(
                feature = ENTRY_CONSUMPTION_FEATURE_ID,
                owner = owner,
                integrations = listOf(
                    FeatureIntegration(
                        id = ENTRY_CONSUMPTION_INTEGRATION_ID,
                        prerequisites = CapabilityExpression.Provided(EntryConsumptionCapability.definition),
                        sharedConsequences = EntryConsumptionConsequence.entries,
                    ),
                ),
            ),
        )
    }
}

internal class DefaultEntryConsumptionFeature(
    evaluation: FeatureGraphEvaluation,
    private val interaction: EntryConsumptionInteraction,
    private val downloadLifecycle: EntryDownloadLifecycleEventSink,
) : EntryConsumptionFeature {
    private val applicableTypes = EntryConsumptionConsequence.entries
        .map { consequence ->
            evaluation.applicableProviderTypes<EntryConsumptionProcessor>(
                feature = ENTRY_CONSUMPTION_FEATURE_ID,
                integration = ENTRY_CONSUMPTION_INTEGRATION_ID,
                consequence = consequence.id,
            )
        }
        .also { selectedTypes ->
            check(selectedTypes.distinct().size <= 1) {
                "Consumption consequences selected different provider sets: $selectedTypes"
            }
        }
        .firstOrNull()
        .orEmpty()

    override fun isApplicable(type: EntryType): Boolean = type in applicableTypes

    override fun canSetConsumed(
        type: EntryType,
        status: EntryConsumptionStatus,
        consumed: Boolean,
    ): Boolean {
        return isApplicable(type) && shouldChangeConsumption(status, consumed)
    }

    override suspend fun setConsumed(
        entry: Entry,
        children: List<EntryChapter>,
        consumed: Boolean,
    ): EntryConsumptionResult {
        if (!isApplicable(entry.type)) return EntryConsumptionResult.Inapplicable(entry.type)

        val changed = interaction.setConsumed(entry, children, consumed)
        if (changed.isEmpty()) return EntryConsumptionResult.NoChange

        if (consumed) {
            downloadLifecycle.onEvent(EntryDownloadLifecycleEvent.MarkedConsumed(entry, changed))
        }
        return EntryConsumptionResult.Changed(changed)
    }
}
