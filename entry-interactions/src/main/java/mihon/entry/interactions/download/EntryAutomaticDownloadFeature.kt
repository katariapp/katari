package mihon.entry.interactions

import eu.kanade.tachiyomi.source.entry.EntryType
import mihon.feature.graph.CapabilityExpression
import mihon.feature.graph.ContributionOwner
import mihon.feature.graph.FeatureArtifactId
import mihon.feature.graph.FeatureContribution
import mihon.feature.graph.FeatureGraphContributionSink
import mihon.feature.graph.FeatureGraphContributor
import mihon.feature.graph.FeatureGraphEvaluation
import mihon.feature.graph.FeatureId
import mihon.feature.graph.FeatureIntegration
import mihon.feature.graph.FeatureIntegrationId
import mihon.feature.graph.SharedFeatureConsequence
import tachiyomi.domain.entry.model.Entry
import tachiyomi.domain.entry.model.EntryChapter
import java.util.concurrent.atomic.AtomicBoolean

private val ENTRY_AUTOMATIC_DOWNLOAD_FEATURE_ID = FeatureId("entry.download.automatic")
private val ENTRY_AUTOMATIC_DOWNLOAD_INTEGRATION_ID = FeatureIntegrationId("entry.download.automatic.providers")
private val ENTRY_AUTOMATIC_DOWNLOAD_FEATURE_OWNER = ContributionOwner("entry-automatic-download")
private val ENTRY_AUTOMATIC_DOWNLOAD_POLICY_CONSEQUENCE_ID =
    FeatureArtifactId("entry.download.automatic.policy")
private val ENTRY_AUTOMATIC_DOWNLOAD_LIBRARY_UPDATE_CONSEQUENCE_ID =
    FeatureArtifactId("entry.download.automatic.library-update")
private val ENTRY_AUTOMATIC_DOWNLOAD_ENTRY_REFRESH_CONSEQUENCE_ID =
    FeatureArtifactId("entry.download.automatic.entry-refresh")

private object EntryAutomaticDownloadPolicyConsequence : SharedFeatureConsequence {
    override val id = ENTRY_AUTOMATIC_DOWNLOAD_POLICY_CONSEQUENCE_ID
}

private object EntryAutomaticDownloadLibraryUpdateConsequence : SharedFeatureConsequence {
    override val id = ENTRY_AUTOMATIC_DOWNLOAD_LIBRARY_UPDATE_CONSEQUENCE_ID
}

private object EntryAutomaticDownloadEntryRefreshConsequence : SharedFeatureConsequence {
    override val id = ENTRY_AUTOMATIC_DOWNLOAD_ENTRY_REFRESH_CONSEQUENCE_ID
}

internal object EntryAutomaticDownloadFeatureContributor : FeatureGraphContributor {
    override val owner = ENTRY_AUTOMATIC_DOWNLOAD_FEATURE_OWNER

    override fun contributeTo(sink: FeatureGraphContributionSink) {
        sink.add(
            FeatureContribution(
                feature = ENTRY_AUTOMATIC_DOWNLOAD_FEATURE_ID,
                owner = owner,
                integrations = listOf(
                    FeatureIntegration(
                        id = ENTRY_AUTOMATIC_DOWNLOAD_INTEGRATION_ID,
                        prerequisites = CapabilityExpression.Provided(EntryDownloadCapability.definition),
                        sharedConsequences = listOf(
                            EntryAutomaticDownloadPolicyConsequence,
                            EntryAutomaticDownloadLibraryUpdateConsequence,
                            EntryAutomaticDownloadEntryRefreshConsequence,
                        ),
                    ),
                ),
            ),
        )
    }
}

internal class DefaultEntryAutomaticDownloadFeature(
    evaluation: FeatureGraphEvaluation,
    private val interaction: EntryDownloadInteraction,
    private val sharedPolicy: EntryAutomaticDownloadPolicy,
) : EntryAutomaticDownloadFeature {
    private val applicableTypes = evaluation.applicableProviderTypes<EntryDownloadProcessor>(
        feature = ENTRY_AUTOMATIC_DOWNLOAD_FEATURE_ID,
        integration = ENTRY_AUTOMATIC_DOWNLOAD_INTEGRATION_ID,
        consequence = ENTRY_AUTOMATIC_DOWNLOAD_POLICY_CONSEQUENCE_ID,
    )

    override fun isApplicable(type: EntryType): Boolean = type in applicableTypes

    override fun newLibraryUpdateBatch(): EntryAutomaticDownloadBatch {
        return DefaultEntryAutomaticDownloadBatch(::scheduleForLibraryUpdate, interaction::startDownloads)
    }

    override suspend fun downloadAfterEntryRefresh(
        entry: Entry,
        newChapters: List<EntryChapter>,
    ): EntryAutomaticDownloadResult {
        return when (val candidates = selectCandidates(entry, newChapters)) {
            is SelectedCandidates.Empty -> candidates.result
            is SelectedCandidates.Selected -> {
                interaction.download(entry, candidates.chapters)
                EntryAutomaticDownloadResult.Scheduled(candidates.chapters.size)
            }
        }
    }

    private suspend fun scheduleForLibraryUpdate(
        entry: Entry,
        newChapters: List<EntryChapter>,
    ): EntryAutomaticDownloadResult {
        return when (val candidates = selectCandidates(entry, newChapters)) {
            is SelectedCandidates.Empty -> candidates.result
            is SelectedCandidates.Selected -> {
                interaction.queue(entry, candidates.chapters, autoStart = false)
                EntryAutomaticDownloadResult.Scheduled(candidates.chapters.size)
            }
        }
    }

    private suspend fun selectCandidates(
        entry: Entry,
        newChapters: List<EntryChapter>,
    ): SelectedCandidates {
        if (!isApplicable(entry.type)) {
            return SelectedCandidates.Empty(EntryAutomaticDownloadResult.Inapplicable)
        }

        val policyCandidates = sharedPolicy.select(entry, newChapters)
        if (policyCandidates.isEmpty()) {
            return SelectedCandidates.Empty(EntryAutomaticDownloadResult.NoCandidates)
        }

        return SelectedCandidates.Selected(policyCandidates)
    }

    private sealed interface SelectedCandidates {
        data class Empty(val result: EntryAutomaticDownloadResult) : SelectedCandidates

        data class Selected(val chapters: List<EntryChapter>) : SelectedCandidates
    }
}

private class DefaultEntryAutomaticDownloadBatch(
    private val schedule: suspend (Entry, List<EntryChapter>) -> EntryAutomaticDownloadResult,
    private val startDownloads: () -> Unit,
) : EntryAutomaticDownloadBatch {
    private val queuedWork = AtomicBoolean(false)
    private val completed = AtomicBoolean(false)

    override suspend fun enqueue(
        entry: Entry,
        newChapters: List<EntryChapter>,
    ): EntryAutomaticDownloadResult {
        check(!completed.get()) { "Cannot enqueue automatic downloads after the library-update batch completed" }
        return schedule(entry, newChapters).also { result ->
            if (result is EntryAutomaticDownloadResult.Scheduled) queuedWork.set(true)
        }
    }

    override fun complete() {
        if (completed.compareAndSet(false, true) && queuedWork.get()) {
            startDownloads()
        }
    }
}
