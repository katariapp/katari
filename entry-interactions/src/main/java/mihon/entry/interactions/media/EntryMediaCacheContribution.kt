package mihon.entry.interactions

import mihon.feature.graph.CapabilityExpression
import mihon.feature.graph.ContributionOwner
import mihon.feature.graph.FeatureArtifactId
import mihon.feature.graph.FeatureBehaviorContract
import mihon.feature.graph.FeatureContribution
import mihon.feature.graph.FeatureGraphContributionSink
import mihon.feature.graph.FeatureGraphContributor
import mihon.feature.graph.FeatureId
import mihon.feature.graph.FeatureIntegration
import mihon.feature.graph.FeatureIntegrationId
import mihon.feature.graph.SharedFeatureConsequence

internal val ENTRY_MEDIA_CACHE_FEATURE_ID = FeatureId("entry.media-cache")
internal val ENTRY_MEDIA_CACHE_INTEGRATION_ID = FeatureIntegrationId("entry.media-cache.provider")

internal val ENTRY_MEDIA_CACHE_DISCOVERY_CONSEQUENCE_ID = FeatureArtifactId("entry.media-cache.discovery")
internal val ENTRY_MEDIA_CACHE_SETTINGS_CONSEQUENCE_ID = FeatureArtifactId("entry.media-cache.settings")
internal val ENTRY_MEDIA_CACHE_MANUAL_CLEAR_CONSEQUENCE_ID = FeatureArtifactId("entry.media-cache.manual-clear")
internal val ENTRY_MEDIA_CACHE_LAUNCH_CLEAR_CONSEQUENCE_ID = FeatureArtifactId("entry.media-cache.launch-clear")
internal val ENTRY_MEDIA_CACHE_PREFERENCES_CONSEQUENCE_ID = FeatureArtifactId("entry.media-cache.preferences")
internal val ENTRY_MEDIA_CACHE_INVALIDATION_CONSEQUENCE_ID = FeatureArtifactId("entry.media-cache.invalidation")
internal val ENTRY_MEDIA_CACHE_ERRORS_CONSEQUENCE_ID = FeatureArtifactId("entry.media-cache.errors")

private val ENTRY_MEDIA_CACHE_FEATURE_OWNER = ContributionOwner("entry-media-cache")
private val ENTRY_MEDIA_CACHE_BEHAVIOR_CONTRACT_ID = FeatureArtifactId("entry.media-cache.behavior")

private data class MediaCacheConsequence(override val id: FeatureArtifactId) : SharedFeatureConsequence

internal object EntryMediaCacheBehaviorContract : FeatureBehaviorContract {
    override val id = ENTRY_MEDIA_CACHE_BEHAVIOR_CONTRACT_ID
}

internal object EntryMediaCacheFeatureContributor : FeatureGraphContributor {
    override val owner = ENTRY_MEDIA_CACHE_FEATURE_OWNER

    override fun contributeTo(sink: FeatureGraphContributionSink) {
        sink.add(
            FeatureContribution(
                feature = ENTRY_MEDIA_CACHE_FEATURE_ID,
                owner = owner,
                integrations = listOf(
                    FeatureIntegration(
                        id = ENTRY_MEDIA_CACHE_INTEGRATION_ID,
                        prerequisites = CapabilityExpression.Provided(EntryMediaCacheCapability.definition),
                        sharedConsequences = listOf(
                            ENTRY_MEDIA_CACHE_DISCOVERY_CONSEQUENCE_ID,
                            ENTRY_MEDIA_CACHE_SETTINGS_CONSEQUENCE_ID,
                            ENTRY_MEDIA_CACHE_MANUAL_CLEAR_CONSEQUENCE_ID,
                            ENTRY_MEDIA_CACHE_LAUNCH_CLEAR_CONSEQUENCE_ID,
                            ENTRY_MEDIA_CACHE_PREFERENCES_CONSEQUENCE_ID,
                            ENTRY_MEDIA_CACHE_INVALIDATION_CONSEQUENCE_ID,
                            ENTRY_MEDIA_CACHE_ERRORS_CONSEQUENCE_ID,
                        ).map(::MediaCacheConsequence),
                        behavioralContracts = listOf(EntryMediaCacheBehaviorContract),
                    ),
                ),
            ),
        )
    }
}
