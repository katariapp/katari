package mihon.entry.interactions

import mihon.feature.graph.ContributionOwner
import mihon.feature.graph.FeatureArtifactId
import mihon.feature.graph.FeatureBehaviorContract
import mihon.feature.graph.FeatureId
import mihon.feature.graph.FeatureIntegrationId
import mihon.feature.graph.SharedFeatureConsequence

internal val ENTRY_TRACKING_FEATURE_ID = FeatureId("entry.tracking")
internal val ENTRY_TRACKING_OWNER = ContributionOwner("entry-tracking")

internal enum class EntryTrackingIntegration(
    val id: FeatureIntegrationId,
) {
    REGISTRY(FeatureIntegrationId("entry.tracking.registry")),
    AVAILABILITY(FeatureIntegrationId("entry.tracking.availability")),
    SESSION(FeatureIntegrationId("entry.tracking.session")),
    AUTOMATIC_BINDING(FeatureIntegrationId("entry.tracking.automatic-binding")),
    SYNCHRONIZATION(FeatureIntegrationId("entry.tracking.synchronization")),
    LIBRARY(FeatureIntegrationId("entry.tracking.library")),
    STATS(FeatureIntegrationId("entry.tracking.stats")),
}

internal enum class EntryTrackingConsequence(
    override val id: FeatureArtifactId,
) : SharedFeatureConsequence {
    SERVICE_REGISTRY(FeatureArtifactId("entry.tracking.service-registry")),
    SERVICE_PRESENTATION(FeatureArtifactId("entry.tracking.service-presentation")),
    ACCOUNT_SETTINGS(FeatureArtifactId("entry.tracking.account-settings")),
    BACKUP_DIAGNOSTICS(FeatureArtifactId("entry.tracking.backup-diagnostics")),
    ENTRY_ACTION(FeatureArtifactId("entry.tracking.entry-action")),
    DOCUMENTATION(FeatureArtifactId("entry.tracking.documentation")),
    ENTRY_SESSION(FeatureArtifactId("entry.tracking.entry-session")),
    ENTRY_OPERATIONS(FeatureArtifactId("entry.tracking.entry-operations")),
    AUTOMATIC_BINDING(FeatureArtifactId("entry.tracking.automatic-binding")),
    PROGRESS_SYNCHRONIZATION(FeatureArtifactId("entry.tracking.progress-synchronization")),
    LIBRARY_FILTER_EVIDENCE(FeatureArtifactId("entry.tracking.library-filter-evidence")),
    LIBRARY_SCORE_EVIDENCE(FeatureArtifactId("entry.tracking.library-score-evidence")),
    STATS_EVIDENCE(FeatureArtifactId("entry.tracking.stats-evidence")),
}

internal object EntryTrackingBehaviorContract : FeatureBehaviorContract {
    override val id = FeatureArtifactId("entry.tracking.behavior")
}
