package mihon.entry.interactions

import eu.kanade.tachiyomi.source.entry.EntryType
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

internal val ENTRY_LIBRARY_UPDATE_NOTIFICATION_FEATURE_ID = FeatureId("entry.library-update-notifications")
private val ENTRY_LIBRARY_UPDATE_NOTIFICATION_FEATURE_OWNER = ContributionOwner("entry-library-update-notifications")

internal val ENTRY_LIBRARY_UPDATE_NOTIFICATION_BASE_INTEGRATION_ID =
    FeatureIntegrationId("entry.library-update-notifications.participation")
internal val ENTRY_LIBRARY_UPDATE_NOTIFICATION_PRESENTATION_INTEGRATION_ID =
    FeatureIntegrationId("entry.library-update-notifications.presentation")
internal val ENTRY_LIBRARY_UPDATE_NOTIFICATION_OPEN_INTEGRATION_ID =
    FeatureIntegrationId("entry.library-update-notifications.open-child")
internal val ENTRY_LIBRARY_UPDATE_NOTIFICATION_CONSUMPTION_INTEGRATION_ID =
    FeatureIntegrationId("entry.library-update-notifications.mark-consumed")
internal val ENTRY_LIBRARY_UPDATE_NOTIFICATION_DOWNLOAD_INTEGRATION_ID =
    FeatureIntegrationId("entry.library-update-notifications.download")

internal val ENTRY_LIBRARY_UPDATE_NOTIFICATION_ROUTE_CONSEQUENCE_ID =
    FeatureArtifactId("entry.library-update-notifications.route")
internal val ENTRY_LIBRARY_UPDATE_NOTIFICATION_RENDER_CONSEQUENCE_ID =
    FeatureArtifactId("entry.library-update-notifications.render")
internal val ENTRY_LIBRARY_UPDATE_NOTIFICATION_PRESENTATION_CONSEQUENCE_ID =
    FeatureArtifactId("entry.library-update-notifications.presentation-vocabulary")
internal val ENTRY_LIBRARY_UPDATE_NOTIFICATION_OPEN_CONSEQUENCE_ID =
    FeatureArtifactId("entry.library-update-notifications.open-child-action")
internal val ENTRY_LIBRARY_UPDATE_NOTIFICATION_CONSUMPTION_CONSEQUENCE_ID =
    FeatureArtifactId("entry.library-update-notifications.mark-consumed-action")
internal val ENTRY_LIBRARY_UPDATE_NOTIFICATION_DOWNLOAD_CONSEQUENCE_ID =
    FeatureArtifactId("entry.library-update-notifications.download-action")
private val ENTRY_LIBRARY_UPDATE_NOTIFICATION_BEHAVIOR_CONTRACT_ID =
    FeatureArtifactId("entry.library-update-notifications.behavior")

private class NotificationConsequence(
    override val id: FeatureArtifactId,
) : SharedFeatureConsequence

private object EntryLibraryUpdateNotificationBehaviorContract : FeatureBehaviorContract {
    override val id = ENTRY_LIBRARY_UPDATE_NOTIFICATION_BEHAVIOR_CONTRACT_ID
}

internal object EntryLibraryUpdateNotificationFeatureContributor : FeatureGraphContributor {
    override val owner = ENTRY_LIBRARY_UPDATE_NOTIFICATION_FEATURE_OWNER

    override fun contributeTo(sink: FeatureGraphContributionSink) {
        sink.add(
            FeatureContribution(
                feature = ENTRY_LIBRARY_UPDATE_NOTIFICATION_FEATURE_ID,
                owner = owner,
                integrations = listOf(
                    FeatureIntegration(
                        id = ENTRY_LIBRARY_UPDATE_NOTIFICATION_BASE_INTEGRATION_ID,
                        prerequisites = CapabilityExpression.Always,
                        sharedConsequences = listOf(
                            NotificationConsequence(ENTRY_LIBRARY_UPDATE_NOTIFICATION_ROUTE_CONSEQUENCE_ID),
                            NotificationConsequence(ENTRY_LIBRARY_UPDATE_NOTIFICATION_RENDER_CONSEQUENCE_ID),
                        ),
                        behavioralContracts = listOf(EntryLibraryUpdateNotificationBehaviorContract),
                    ),
                    FeatureIntegration(
                        id = ENTRY_LIBRARY_UPDATE_NOTIFICATION_PRESENTATION_INTEGRATION_ID,
                        prerequisites = CapabilityExpression.Provided(EntryTypePresentationCapability.definition),
                        sharedConsequences = listOf(
                            NotificationConsequence(ENTRY_LIBRARY_UPDATE_NOTIFICATION_PRESENTATION_CONSEQUENCE_ID),
                        ),
                    ),
                    FeatureIntegration(
                        id = ENTRY_LIBRARY_UPDATE_NOTIFICATION_OPEN_INTEGRATION_ID,
                        prerequisites = CapabilityExpression.Provided(EntryOpenCapability.definition),
                        sharedConsequences = listOf(
                            NotificationConsequence(ENTRY_LIBRARY_UPDATE_NOTIFICATION_OPEN_CONSEQUENCE_ID),
                        ),
                    ),
                    FeatureIntegration(
                        id = ENTRY_LIBRARY_UPDATE_NOTIFICATION_CONSUMPTION_INTEGRATION_ID,
                        prerequisites = CapabilityExpression.Provided(EntryConsumptionCapability.definition),
                        sharedConsequences = listOf(
                            NotificationConsequence(ENTRY_LIBRARY_UPDATE_NOTIFICATION_CONSUMPTION_CONSEQUENCE_ID),
                        ),
                    ),
                    FeatureIntegration(
                        id = ENTRY_LIBRARY_UPDATE_NOTIFICATION_DOWNLOAD_INTEGRATION_ID,
                        prerequisites = CapabilityExpression.Provided(EntryDownloadCapability.definition),
                        sharedConsequences = listOf(
                            NotificationConsequence(ENTRY_LIBRARY_UPDATE_NOTIFICATION_DOWNLOAD_CONSEQUENCE_ID),
                        ),
                    ),
                ),
            ),
        )
    }
}

internal fun FeatureGraphEvaluation.libraryUpdateNotificationTypes(
    integration: FeatureIntegrationId,
    consequence: FeatureArtifactId,
): Set<EntryType> {
    val contentTypes = sharedConsequences.asSequence()
        .filter { applicability ->
            applicability.subject.feature == ENTRY_LIBRARY_UPDATE_NOTIFICATION_FEATURE_ID &&
                applicability.subject.integration == integration &&
                applicability.consequence.id == consequence
        }
        .mapTo(mutableSetOf()) { it.subject.contentType }
    return EntryType.entries.filterTo(mutableSetOf()) { it.toContentTypeId() in contentTypes }
}
