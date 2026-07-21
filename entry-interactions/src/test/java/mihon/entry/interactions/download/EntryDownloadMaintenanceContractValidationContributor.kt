package mihon.entry.interactions

import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import mihon.entry.interactions.validation.contractExpectation
import mihon.entry.interactions.validation.productionSubjectEvaluation
import mihon.entry.interactions.validation.verifyFeatureContract
import mihon.feature.graph.validation.FeatureContractReference
import mihon.feature.graph.validation.FeatureContractVerifier
import mihon.feature.graph.validation.FeatureValidationContributionSink
import mihon.feature.graph.validation.FeatureValidationContributor
import tachiyomi.domain.entry.model.Entry

class EntryDownloadMaintenanceContractValidationContributor : FeatureValidationContributor {
    override val owner = EntryDownloadMaintenanceFeatureContributor.owner

    override fun contributeTo(sink: FeatureValidationContributionSink) {
        sink.add(
            FeatureContractVerifier(
                FeatureContractReference(
                    ENTRY_DOWNLOAD_MAINTENANCE_FEATURE_ID,
                    EntryDownloadMaintenanceBehaviorContract,
                ),
            ) { input ->
                verifyFeatureContract {
                    val provider = input.provider(EntryDownloadCapability.definition)
                    val evaluation = productionSubjectEvaluation(
                        EntryDownloadCapability.bind(provider),
                        EntryDownloadMaintenanceFeatureContributor,
                    )
                    val entry = Entry.create().copy(id = 75L, type = provider.type)
                    var cacheInvalidated = false
                    val interaction = recordingDownloadInteraction()
                    every { interaction.invalidateCaches() } answers { cacheInvalidated = true }
                    val ownership = mockk<EntryMergeDownloadOwnershipProjection> {
                        coEvery { resolveDownloadOwners(any()) } returns EntryMergeDownloadOwners(
                            profileId = entry.profileId,
                            visibleEntryId = entry.id,
                            orderedOwners = listOf(entry),
                        )
                    }
                    val feature = DefaultEntryDownloadMaintenanceFeature(evaluation, interaction, ownership)

                    contractExpectation(
                        feature.invalidateCaches() == EntryDownloadMaintenanceResult.Performed,
                        "Download Maintenance must accept a participating provider",
                    )
                    contractExpectation(
                        cacheInvalidated,
                        "Download Maintenance must dispatch cache invalidation through its shared boundary",
                    )
                }
            },
        )
    }
}
