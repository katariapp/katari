package mihon.entry.interactions

import eu.kanade.tachiyomi.source.entry.EntryItemOrientation
import eu.kanade.tachiyomi.source.entry.EntryType
import eu.kanade.tachiyomi.source.entry.RelatedEntriesSource
import eu.kanade.tachiyomi.source.entry.entryItemOrientation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
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
import tachiyomi.domain.entry.adapter.toEntry
import tachiyomi.domain.entry.adapter.toSEntry
import tachiyomi.domain.entry.interactor.GetEntry
import tachiyomi.domain.entry.interactor.NetworkToLocalEntry
import tachiyomi.domain.entry.model.Entry
import tachiyomi.domain.entry.model.identity
import tachiyomi.domain.source.service.SourceManager

private val ENTRY_RELATED_ENTRIES_FEATURE_ID = FeatureId("entry.related-entries")
private val ENTRY_RELATED_ENTRIES_INTEGRATION_ID = FeatureIntegrationId("entry.related-entries.source-context")
private val ENTRY_RELATED_ENTRIES_FEATURE_OWNER = ContributionOwner("entry-related-entries")

private enum class EntryRelatedEntriesConsequence(
    override val id: FeatureArtifactId,
) : SharedFeatureConsequence {
    AVAILABILITY(FeatureArtifactId("entry.related-entries.availability")),
    FETCH(FeatureArtifactId("entry.related-entries.fetch")),
    PERSISTENCE(FeatureArtifactId("entry.related-entries.persistence")),
    LIBRARY_STATE(FeatureArtifactId("entry.related-entries.library-state")),
    ORIENTATION(FeatureArtifactId("entry.related-entries.orientation")),
    ENTRY_SURFACE(FeatureArtifactId("entry.related-entries.entry-surface")),
    DETAILS_NAVIGATION(FeatureArtifactId("entry.related-entries.details-navigation")),
}

private object EntryRelatedEntriesBehaviorContract : FeatureBehaviorContract {
    override val id = FeatureArtifactId("entry.related-entries.behavior")
}

internal object EntryRelatedEntriesFeatureContributor : FeatureGraphContributor {
    override val owner = ENTRY_RELATED_ENTRIES_FEATURE_OWNER

    override fun contributeTo(sink: FeatureGraphContributionSink) {
        sink.add(
            FeatureContribution(
                feature = ENTRY_RELATED_ENTRIES_FEATURE_ID,
                owner = owner,
                integrations = listOf(
                    FeatureIntegration(
                        id = ENTRY_RELATED_ENTRIES_INTEGRATION_ID,
                        prerequisites = CapabilityExpression.Always,
                        sharedConsequences = EntryRelatedEntriesConsequence.entries,
                        behavioralContracts = listOf(EntryRelatedEntriesBehaviorContract),
                    ),
                ),
            ),
        )
    }
}

internal class DefaultEntryRelatedEntriesFeature(
    evaluation: FeatureGraphEvaluation,
    private val sourceManager: SourceManager,
    private val networkToLocalEntry: NetworkToLocalEntry,
    private val getEntry: GetEntry,
) : EntryRelatedEntriesFeature {
    private val selectedTypesByConsequence = EntryRelatedEntriesConsequence.entries.associateWith { consequence ->
        evaluation.sharedConsequences
            .asSequence()
            .filter { applicability ->
                applicability.subject.feature == ENTRY_RELATED_ENTRIES_FEATURE_ID &&
                    applicability.subject.integration == ENTRY_RELATED_ENTRIES_INTEGRATION_ID &&
                    applicability.consequence.id == consequence.id
            }
            .mapTo(mutableSetOf()) { it.subject.contentType }
    }
    private val selectedTypes = selectedTypesByConsequence.getValue(EntryRelatedEntriesConsequence.AVAILABILITY)

    init {
        check(selectedTypesByConsequence.values.toSet().size == 1) {
            "Related Entries consequences selected different content types"
        }
    }

    override fun availability(context: EntryRelatedEntriesContext): EntryRelatedEntriesAvailability {
        requireComposedOrigin(context.entry.type)
        val source = context.source
            ?: return EntryRelatedEntriesAvailability.Unavailable(
                EntryRelatedEntriesUnavailableReason.SOURCE_MISSING,
            )
        check(source.id == context.entry.source) {
            "Related Entries context source ${source.id} does not own Entry source ${context.entry.source}"
        }
        return source.availability()
    }

    override suspend fun load(entryId: Long): EntryRelatedEntriesLoadResult {
        val entry = getEntry.await(entryId) ?: error("Entry $entryId was not found")
        requireComposedOrigin(entry.type)
        val source = sourceManager.get(entry.source)
            ?: return EntryRelatedEntriesLoadResult.Unavailable(
                EntryRelatedEntriesUnavailableReason.SOURCE_MISSING,
            )
        val availability = source.availability()
        if (availability is EntryRelatedEntriesAvailability.Unavailable) {
            return EntryRelatedEntriesLoadResult.Unavailable(availability.reason)
        }
        val orientation = (availability as EntryRelatedEntriesAvailability.Available).orientation
        val relatedSource = source as RelatedEntriesSource
        val entries = relatedSource.getRelatedEntries(entry.toSEntry())
            .map { it.toEntry(source.id) }
            .distinctBy(Entry::identity)
            .let { networkToLocalEntry(it) }
        return EntryRelatedEntriesLoadResult.Loaded(entries, orientation)
    }

    override fun observeEntry(entry: Entry): Flow<Entry> {
        return getEntry.subscribe(entry.url, entry.source, entry.type).filterNotNull()
    }

    private fun requireComposedOrigin(type: EntryType) {
        check(type.toContentTypeId() in selectedTypes) {
            "Entry type $type was not contributed to the Related Entries feature graph"
        }
    }

    private fun eu.kanade.tachiyomi.source.entry.UnifiedSource.availability(): EntryRelatedEntriesAvailability {
        return if (this is RelatedEntriesSource) {
            EntryRelatedEntriesAvailability.Available(entryItemOrientation())
        } else {
            EntryRelatedEntriesAvailability.Unavailable(
                EntryRelatedEntriesUnavailableReason.SOURCE_UNSUPPORTED,
            )
        }
    }
}
