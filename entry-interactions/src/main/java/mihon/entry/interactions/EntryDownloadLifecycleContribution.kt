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
import mihon.feature.graph.allOf

internal val ENTRY_DOWNLOAD_LIFECYCLE_FEATURE_ID = FeatureId("entry.download.lifecycle")
internal val ENTRY_DOWNLOAD_LIFECYCLE_BASE_INTEGRATION_ID =
    FeatureIntegrationId("entry.download.lifecycle.base")
internal val ENTRY_DOWNLOAD_LIFECYCLE_BOOKMARK_PROTECTION_INTEGRATION_ID =
    FeatureIntegrationId("entry.download.lifecycle.bookmark-protection")
private val ENTRY_DOWNLOAD_LIFECYCLE_FEATURE_OWNER = ContributionOwner("entry-download-lifecycle")

internal enum class EntryDownloadLifecycleBaseConsequence(
    override val id: FeatureArtifactId,
) : SharedFeatureConsequence {
    EVENTS(FeatureArtifactId("entry.download.lifecycle.events")),
    MARKED_CONSUMED_CLEANUP(FeatureArtifactId("entry.download.lifecycle.marked-consumed-cleanup")),
    COMPLETION_CLEANUP(FeatureArtifactId("entry.download.lifecycle.completion-cleanup")),
    DOWNLOAD_AHEAD(FeatureArtifactId("entry.download.lifecycle.download-ahead")),
    CATEGORY_EXCLUSIONS(FeatureArtifactId("entry.download.lifecycle.category-exclusions")),
    PHYSICAL_DISPATCH(FeatureArtifactId("entry.download.lifecycle.physical-dispatch")),
}

internal val ENTRY_DOWNLOAD_LIFECYCLE_BOOKMARK_PROTECTION_CONSEQUENCE_ID =
    FeatureArtifactId("entry.download.lifecycle.bookmark-protection")

private object EntryDownloadLifecycleBookmarkProtectionConsequence : SharedFeatureConsequence {
    override val id = ENTRY_DOWNLOAD_LIFECYCLE_BOOKMARK_PROTECTION_CONSEQUENCE_ID
}

internal object EntryDownloadLifecycleFeatureContributor : FeatureGraphContributor {
    override val owner = ENTRY_DOWNLOAD_LIFECYCLE_FEATURE_OWNER

    override fun contributeTo(sink: FeatureGraphContributionSink) {
        sink.add(
            FeatureContribution(
                feature = ENTRY_DOWNLOAD_LIFECYCLE_FEATURE_ID,
                owner = owner,
                integrations = listOf(
                    FeatureIntegration(
                        id = ENTRY_DOWNLOAD_LIFECYCLE_BASE_INTEGRATION_ID,
                        prerequisites = CapabilityExpression.Provided(EntryDownloadCapability.definition),
                        sharedConsequences = EntryDownloadLifecycleBaseConsequence.entries,
                    ),
                    FeatureIntegration(
                        id = ENTRY_DOWNLOAD_LIFECYCLE_BOOKMARK_PROTECTION_INTEGRATION_ID,
                        prerequisites = allOf(
                            CapabilityExpression.Provided(EntryDownloadCapability.definition),
                            CapabilityExpression.Provided(EntryBookmarkCapability.definition),
                        ),
                        sharedConsequences = listOf(EntryDownloadLifecycleBookmarkProtectionConsequence),
                    ),
                ),
            ),
        )
    }
}
