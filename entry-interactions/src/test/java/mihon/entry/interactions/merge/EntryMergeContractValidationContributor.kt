package mihon.entry.interactions

import eu.kanade.tachiyomi.source.entry.EntryType
import io.mockk.mockk
import mihon.entry.interactions.host.EntryMergeHostTransition
import mihon.entry.interactions.host.EntryMergeMembershipSnapshot
import mihon.entry.interactions.validation.contractExpectation
import mihon.entry.interactions.validation.productionSubjectEvaluation
import mihon.entry.interactions.validation.verifyFeatureContract
import mihon.feature.graph.FeatureContractScenarioId
import mihon.feature.graph.contextEvidence
import mihon.feature.graph.validation.FeatureContractExecutionInput
import mihon.feature.graph.validation.FeatureContractReference
import mihon.feature.graph.validation.FeatureContractScenario
import mihon.feature.graph.validation.FeatureContractVerifier
import mihon.feature.graph.validation.FeatureValidationContributionSink
import mihon.feature.graph.validation.FeatureValidationContributor
import tachiyomi.domain.entry.model.Entry

class EntryMergeContractValidationContributor : FeatureValidationContributor {
    override val owner = EntryMergeFeatureContributor.owner

    override fun contributeTo(sink: FeatureValidationContributionSink) {
        contracts.forEach { item ->
            val reference = FeatureContractReference(ENTRY_MERGE_FEATURE_ID, item.contract)
            sink.add(FeatureContractVerifier(reference) { input -> verifyMerge(input, item.contract) })
            item.scenario?.let { scenario ->
                sink.add(
                    FeatureContractScenario(
                        FeatureContractScenarioId("${item.integration.value}.applicable"),
                        reference,
                        item.integration,
                    ) { scenario() },
                )
            }
        }
    }

    private suspend fun verifyMerge(
        input: FeatureContractExecutionInput,
        contract: EntryMergeBehaviorContract,
    ) = verifyFeatureContract {
        val type = EntryType.entries.single { it.toContentTypeId() == input.subject.contentType }
        val entries = listOf(entry(1L, type), entry(2L, type))
        val membership = EntryMergeMembershipSnapshot(7L, 1L, entries.map(Entry::id))
        when (contract) {
            EntryMergeBehaviorContract.DOWNLOAD_OWNERSHIP -> {
                input.provider(EntryDownloadCapability.definition)
                val owners = EntryMergeDownloadOwnershipCoordinator(
                    RecordingEntryMergeHost(entries, listOf(membership)),
                )
                    .resolveDownloadOwners(EntryMergeSubject(7L, 2L))
                contractExpectation(owners.orderedOwners == entries, "Merge must project ordered download owners")
            }
            EntryMergeBehaviorContract.MIGRATION_REPLACEMENT -> {
                input.provider(EntryMigrationCapability.definition)
                val host = RecordingEntryMergeHost(entries, listOf(membership))
                val result = EntryMergeMigrationCoordinator(host).participateInReplacementTransaction(
                    EntryMergeMigrationReplacementIntent(entries.first(), entry(3L, type)),
                )
                contractExpectation(
                    result == EntryMergeMigrationReplacementResult.Applied,
                    "Merge must participate in migration replacement",
                )
            }
            EntryMergeBehaviorContract.EXISTING_GROUP -> {
                val host = RecordingEntryMergeHost(entries, listOf(membership))
                val result = workflow(type, host).execute(
                    EntryMergeRemoveEntriesIntent(EntryMergeSubject(7L, 1L), setOf(2L), false, false),
                )
                contractExpectation(result is EntryMergeExecutionResult.Applied, "Merge must mutate an existing group")
            }
            EntryMergeBehaviorContract.LIBRARY_INITIALIZATION -> verifyInitialization(type)
            EntryMergeBehaviorContract.COVER_CLEANUP -> verifyRemoval(type, includeDownloads = false, input = input)
            EntryMergeBehaviorContract.DOWNLOAD_REMOVAL -> verifyRemoval(type, includeDownloads = true, input = input)
            else -> {
                val ready = workflow(type, RecordingEntryMergeHost(entries))
                    .prepare(EntryMergePrepareIntent(entries))
                contractExpectation(
                    ready is EntryMergePreparationResult.Ready,
                    "Merge must prepare its shared workflow",
                )
            }
        }
    }

    private suspend fun verifyInitialization(type: EntryType) {
        val persisted = entry(1L, type)
        val remote = entry(-1L, type).copy(favorite = false)
        val host = RecordingEntryMergeHost(listOf(persisted))
        val feature = workflow(type, host)
        val ready = feature.prepare(
            EntryMergePrepareIntent(
                listOf(persisted, remote),
                listOf(EntryMergeMemberPreparationIntent(remote, emptyList())),
            ),
        ) as EntryMergePreparationResult.Ready
        feature.execute(
            EntryMergeCommitIntent(
                ready.editor.editReference,
                ready.editor.target,
                ready.editor.entries.map(EntryMergeEditorEntry::reference),
            ),
        )
        val requests = (host.transitions.single() as EntryMergeHostTransition.CommitEditor).consequenceRequests
        contractExpectation(
            requests.any { it.artifactId == EntryMergeConsequenceArtifact.LIBRARY_INITIALIZATION },
            "Merge must request initialization for a new library member",
        )
    }

    private suspend fun verifyRemoval(
        type: EntryType,
        includeDownloads: Boolean,
        input: FeatureContractExecutionInput,
    ) {
        val bindings = if (includeDownloads) {
            listOf(EntryDownloadCapability.bind(input.provider(EntryDownloadCapability.definition)))
        } else {
            emptyList()
        }
        val entries = listOf(entry(1L, type), entry(2L, type))
        val membership = EntryMergeMembershipSnapshot(7L, 1L, entries.map(Entry::id))
        val host = RecordingEntryMergeHost(entries, listOf(membership))
        val feature = workflow(type, host, bindings)
        val ready = feature.prepare(
            EntryMergePrepareIntent(listOf(entries.first())),
        ) as EntryMergePreparationResult.Ready
        val removed = ready.editor.entries.single { it.entry.id == 2L }.reference
        feature.execute(
            EntryMergeCommitIntent(
                ready.editor.editReference,
                ready.editor.target,
                ready.editor.entries.map(EntryMergeEditorEntry::reference),
                libraryRemovalEntries = setOf(removed),
            ),
        )
        val ids = (host.transitions.single() as EntryMergeHostTransition.CommitEditor)
            .consequenceRequests.map { it.artifactId }
        contractExpectation(
            EntryMergeConsequenceArtifact.COVER_CLEANUP in ids,
            "Merge must request cover cleanup for library removal",
        )
        if (includeDownloads) {
            contractExpectation(
                EntryMergeConsequenceArtifact.DOWNLOAD_REMOVAL in ids,
                "Merge must request download cleanup when Download is selected",
            )
        }
    }

    private fun workflow(
        type: EntryType,
        host: RecordingEntryMergeHost,
        bindings: List<EntryInteractionProviderBinding<*>> = emptyList(),
    ): EntryMergeFeature {
        val evaluation = if (bindings.isEmpty()) {
            productionSubjectEvaluation(type, EntryMergeFeatureContributor)
        } else {
            productionSubjectEvaluation(bindings, EntryMergeFeatureContributor)
        }
        return EntryMergeWorkflowCoordinator(
            evaluation,
            host,
            EntryMergeConsequenceDelivery(host, { mockk(relaxed = true) }, {}, { mockk(relaxed = true) }),
        )
    }

    private fun entry(id: Long, type: EntryType) = Entry.create().copy(
        id = id,
        profileId = 7L,
        source = 10L + id,
        url = "/$id",
        title = "Entry $id",
        favorite = true,
        type = type,
    )

    private data class Contract(
        val integration: mihon.feature.graph.FeatureIntegrationId,
        val contract: EntryMergeBehaviorContract,
        val scenario: (() -> List<mihon.feature.graph.ContextEvidence<*>>)? = null,
    )

    private companion object {
        val contracts = listOf(
            Contract(ENTRY_MERGE_BASE_INTEGRATION_ID, EntryMergeBehaviorContract.WORKFLOW),
            Contract(ENTRY_MERGE_DOWNLOAD_INTEGRATION_ID, EntryMergeBehaviorContract.DOWNLOAD_OWNERSHIP),
            Contract(ENTRY_MERGE_MIGRATION_INTEGRATION_ID, EntryMergeBehaviorContract.MIGRATION_REPLACEMENT),
            Contract(ENTRY_MERGE_SELECTION_CONTEXT_INTEGRATION, EntryMergeBehaviorContract.PREPARATION_SELECTION) {
                listOf(
                    contextEvidence(ENTRY_MERGE_HOMOGENEOUS_TYPE_CONTEXT, true),
                    contextEvidence(ENTRY_MERGE_HOMOGENEOUS_PROFILE_CONTEXT, true),
                )
            },
            Contract(ENTRY_MERGE_AUTHORITY_CONTEXT_INTEGRATION, EntryMergeBehaviorContract.PREPARATION_AUTHORITY) {
                listOf(
                    contextEvidence(ENTRY_MERGE_AUTHORITATIVE_SELECTION_CONTEXT, true),
                    contextEvidence(ENTRY_MERGE_AUTHORITATIVE_TYPE_CONTEXT, true),
                    contextEvidence(ENTRY_MERGE_AUTHORITATIVE_PROFILE_CONTEXT, true),
                )
            },
            Contract(ENTRY_MERGE_MEMBERSHIP_CONTEXT_INTEGRATION, EntryMergeBehaviorContract.PREPARATION_MEMBERSHIP) {
                listOf(
                    contextEvidence(ENTRY_MERGE_SINGLE_EXISTING_GROUP_CONTEXT, true),
                    contextEvidence(ENTRY_MERGE_COMPLETE_EXISTING_GROUP_CONTEXT, true),
                    contextEvidence(ENTRY_MERGE_SUFFICIENT_EDITOR_MEMBERS_CONTEXT, true),
                )
            },
            Contract(ENTRY_MERGE_EXISTING_GROUP_CONTEXT_INTEGRATION, EntryMergeBehaviorContract.EXISTING_GROUP) {
                listOf(
                    contextEvidence(ENTRY_MERGE_COMPLETE_ORDERED_MEMBERSHIP_CONTEXT, true),
                    contextEvidence(ENTRY_MERGE_HOMOGENEOUS_MEMBERSHIP_TYPE_CONTEXT, true),
                )
            },
            Contract(
                ENTRY_MERGE_LIBRARY_INITIALIZATION_CONTEXT_INTEGRATION,
                EntryMergeBehaviorContract.LIBRARY_INITIALIZATION,
            ) { listOf(contextEvidence(ENTRY_MERGE_LIBRARY_INITIALIZATION_REQUIRED_CONTEXT, true)) },
            Contract(ENTRY_MERGE_COVER_CLEANUP_CONTEXT_INTEGRATION, EntryMergeBehaviorContract.COVER_CLEANUP) {
                listOf(contextEvidence(ENTRY_MERGE_COVER_CLEANUP_REQUIRED_CONTEXT, true))
            },
            Contract(ENTRY_MERGE_DOWNLOAD_REMOVAL_CONTEXT_INTEGRATION, EntryMergeBehaviorContract.DOWNLOAD_REMOVAL) {
                listOf(contextEvidence(ENTRY_MERGE_DOWNLOAD_REMOVAL_REQUIRED_CONTEXT, true))
            },
        )
    }
}
