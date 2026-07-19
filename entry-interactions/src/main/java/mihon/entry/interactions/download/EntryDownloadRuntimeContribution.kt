package mihon.entry.interactions

import mihon.feature.graph.CapabilityExpression
import mihon.feature.graph.ContributionOwner
import mihon.feature.graph.FeatureArtifactId
import mihon.feature.graph.FeatureContribution
import mihon.feature.graph.FeatureGraphContributionSink
import mihon.feature.graph.FeatureGraphContributor
import mihon.feature.graph.FeatureId
import mihon.feature.graph.FeatureIntegration
import mihon.feature.graph.FeatureIntegrationId
import mihon.feature.graph.SharedFeatureConsequence

internal val ENTRY_DOWNLOAD_RUNTIME_FEATURE_ID = FeatureId("entry.download.runtime")
internal val ENTRY_DOWNLOAD_RUNTIME_INTEGRATION_ID = FeatureIntegrationId("entry.download.runtime.provider")
private val ENTRY_DOWNLOAD_RUNTIME_FEATURE_OWNER = ContributionOwner("entry-download-runtime")

internal enum class EntryDownloadRuntimeConsequence(
    override val id: FeatureArtifactId,
) : SharedFeatureConsequence {
    STATUS(FeatureArtifactId("entry.download.runtime.status")),
    QUEUE(FeatureArtifactId("entry.download.runtime.queue")),
    LIBRARY_COUNTS(FeatureArtifactId("entry.download.runtime.library-counts")),
    INITIALIZATION(FeatureArtifactId("entry.download.runtime.initialization")),
    JOB(FeatureArtifactId("entry.download.runtime.job")),
    NOTIFICATIONS(FeatureArtifactId("entry.download.runtime.notifications")),
}

internal object EntryDownloadRuntimeFeatureContributor : FeatureGraphContributor {
    override val owner = ENTRY_DOWNLOAD_RUNTIME_FEATURE_OWNER

    override fun contributeTo(sink: FeatureGraphContributionSink) {
        sink.add(
            FeatureContribution(
                feature = ENTRY_DOWNLOAD_RUNTIME_FEATURE_ID,
                owner = owner,
                integrations = listOf(
                    FeatureIntegration(
                        id = ENTRY_DOWNLOAD_RUNTIME_INTEGRATION_ID,
                        prerequisites = CapabilityExpression.Provided(EntryDownloadCapability.definition),
                        sharedConsequences = EntryDownloadRuntimeConsequence.entries,
                    ),
                ),
            ),
        )
    }
}
