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

internal val ENTRY_DOWNLOAD_CONFIGURATION_FEATURE_ID = FeatureId("entry.download.configuration")
private val ENTRY_DOWNLOAD_CONFIGURATION_FEATURE_OWNER = ContributionOwner("entry-download-configuration")

internal val ENTRY_DOWNLOAD_OPTIONS_INTEGRATION_ID = FeatureIntegrationId("entry.download.configuration.options")
internal val ENTRY_DOWNLOAD_OPTIONS_CONSEQUENCE_ID = FeatureArtifactId("entry.download.configuration.options.dispatch")

internal val ENTRY_DOWNLOAD_ARCHIVE_PACKAGING_INTEGRATION_ID =
    FeatureIntegrationId("entry.download.configuration.setting.archive-packaging")
internal val ENTRY_DOWNLOAD_ARCHIVE_PACKAGING_CONSEQUENCE_ID =
    FeatureArtifactId("entry.download.configuration.setting.archive-packaging.visibility")

internal val ENTRY_DOWNLOAD_TALL_IMAGE_SPLITTING_INTEGRATION_ID =
    FeatureIntegrationId("entry.download.configuration.setting.tall-image-splitting")
internal val ENTRY_DOWNLOAD_TALL_IMAGE_SPLITTING_CONSEQUENCE_ID =
    FeatureArtifactId("entry.download.configuration.setting.tall-image-splitting.visibility")

internal val ENTRY_DOWNLOAD_PARALLEL_SOURCE_TRANSFERS_INTEGRATION_ID =
    FeatureIntegrationId("entry.download.configuration.setting.parallel-source-transfers")
internal val ENTRY_DOWNLOAD_PARALLEL_SOURCE_TRANSFERS_CONSEQUENCE_ID =
    FeatureArtifactId("entry.download.configuration.setting.parallel-source-transfers.visibility")

internal val ENTRY_DOWNLOAD_PARALLEL_ITEM_TRANSFERS_INTEGRATION_ID =
    FeatureIntegrationId("entry.download.configuration.setting.parallel-item-transfers")
internal val ENTRY_DOWNLOAD_PARALLEL_ITEM_TRANSFERS_CONSEQUENCE_ID =
    FeatureArtifactId("entry.download.configuration.setting.parallel-item-transfers.visibility")

private class EntryDownloadConfigurationConsequence(
    override val id: FeatureArtifactId,
) : SharedFeatureConsequence

internal object EntryDownloadConfigurationFeatureContributor : FeatureGraphContributor {
    override val owner = ENTRY_DOWNLOAD_CONFIGURATION_FEATURE_OWNER

    override fun contributeTo(sink: FeatureGraphContributionSink) {
        sink.add(
            FeatureContribution(
                feature = ENTRY_DOWNLOAD_CONFIGURATION_FEATURE_ID,
                owner = owner,
                integrations = listOf(
                    integration(
                        ENTRY_DOWNLOAD_OPTIONS_INTEGRATION_ID,
                        EntryDownloadOptionsCapability,
                        ENTRY_DOWNLOAD_OPTIONS_CONSEQUENCE_ID,
                    ),
                    integration(
                        ENTRY_DOWNLOAD_ARCHIVE_PACKAGING_INTEGRATION_ID,
                        EntryDownloadArchivePackagingCapability,
                        ENTRY_DOWNLOAD_ARCHIVE_PACKAGING_CONSEQUENCE_ID,
                    ),
                    integration(
                        ENTRY_DOWNLOAD_TALL_IMAGE_SPLITTING_INTEGRATION_ID,
                        EntryDownloadTallImageSplittingCapability,
                        ENTRY_DOWNLOAD_TALL_IMAGE_SPLITTING_CONSEQUENCE_ID,
                    ),
                    integration(
                        ENTRY_DOWNLOAD_PARALLEL_SOURCE_TRANSFERS_INTEGRATION_ID,
                        EntryDownloadParallelSourceTransfersCapability,
                        ENTRY_DOWNLOAD_PARALLEL_SOURCE_TRANSFERS_CONSEQUENCE_ID,
                    ),
                    integration(
                        ENTRY_DOWNLOAD_PARALLEL_ITEM_TRANSFERS_INTEGRATION_ID,
                        EntryDownloadParallelItemTransfersCapability,
                        ENTRY_DOWNLOAD_PARALLEL_ITEM_TRANSFERS_CONSEQUENCE_ID,
                    ),
                ),
            ),
        )
    }

    private fun integration(
        id: FeatureIntegrationId,
        capability: EntryInteractionCapability<*>,
        consequence: FeatureArtifactId,
    ): FeatureIntegration {
        return FeatureIntegration(
            id = id,
            prerequisites = CapabilityExpression.Provided(capability.definition),
            sharedConsequences = listOf(EntryDownloadConfigurationConsequence(consequence)),
        )
    }
}
