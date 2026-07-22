package mihon.entry.interactions

import mihon.entry.interactions.validation.contractExpectation
import mihon.entry.interactions.validation.productionSubjectEvaluation
import mihon.entry.interactions.validation.verifyFeatureContract
import mihon.feature.graph.validation.FeatureContractReference
import mihon.feature.graph.validation.FeatureContractVerifier
import mihon.feature.graph.validation.FeatureValidationContributionSink
import mihon.feature.graph.validation.FeatureValidationContributor
import tachiyomi.domain.entry.model.Entry

class EntryProgressContractValidationContributor : FeatureValidationContributor {
    override val owner = EntryProgressFeatureContributor.owner

    override fun contributeTo(sink: FeatureValidationContributionSink) {
        sink.addEntryBackupParticipationContract(
            ENTRY_PROGRESS_BACKUP_SNAPSHOT_PARTICIPANT,
            EntryProgressBehaviorContract,
            EntryProgressSnapshot.serializer(),
            EntryProgressSnapshot(),
        )
        sink.addEntryBackupParticipationContract(
            ENTRY_PROGRESS_BACKUP_RESTORE_PARTICIPANT,
            EntryProgressBehaviorContract,
            EntryProgressSnapshot.serializer(),
            EntryProgressSnapshot(),
        )
        sink.add(
            FeatureContractVerifier(
                FeatureContractReference(ENTRY_PROGRESS_FEATURE_ID, EntryProgressBehaviorContract),
            ) { input ->
                verifyFeatureContract {
                    val provider = input.provider(EntryProgressCapability.definition)
                    val evaluation = productionSubjectEvaluation(
                        EntryProgressCapability.bind(provider),
                        EntryProgressFeatureContributor,
                    )
                    val entry = Entry.create().copy(id = 53L, type = provider.type)
                    val snapshot = EntryProgressSnapshot()
                    val restored = mutableListOf<EntryProgressSnapshot>()
                    val feature = DefaultEntryProgressFeature(
                        evaluation = evaluation,
                        interaction = object : EntryProgressInteraction {
                            override suspend fun snapshot(entry: Entry): EntryProgressSnapshot = snapshot
                            override suspend fun restore(entry: Entry, snapshot: EntryProgressSnapshot) {
                                restored += snapshot
                            }

                            override suspend fun copy(
                                sourceEntry: Entry,
                                targetEntry: Entry,
                                resourceMappings: List<EntryProgressResourceMapping>,
                            ) = Unit
                        },
                    )

                    contractExpectation(feature.isApplicable(provider.type), "Progress transfer must be applicable")
                    contractExpectation(
                        feature.snapshot(entry) == EntryProgressSnapshotResult.Available(snapshot),
                        "Progress transfer must expose its snapshot",
                    )
                    contractExpectation(
                        feature.restore(entry, snapshot) == EntryProgressRestoreResult.Applied,
                        "Progress transfer must apply restoration",
                    )
                    contractExpectation(restored == listOf(snapshot), "Progress transfer restored the wrong snapshot")
                }
            },
        )
    }
}
