package mihon.entry.interactions

import android.app.PendingIntent
import android.content.Context
import eu.kanade.tachiyomi.source.entry.EntryType
import mihon.feature.graph.ApplicableFeatureIntegration
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

private val ENTRY_OPEN_FEATURE_ID = FeatureId("entry.open")
private val ENTRY_OPEN_INTEGRATION_ID = FeatureIntegrationId("entry.open.provider")
private val ENTRY_OPEN_FEATURE_OWNER = ContributionOwner("entry-open")
private val ENTRY_OPEN_DISPATCH_CONSEQUENCE_ID = FeatureArtifactId("entry.open.dispatch")

private object EntryOpenDispatchConsequence : SharedFeatureConsequence {
    override val id = ENTRY_OPEN_DISPATCH_CONSEQUENCE_ID
}

internal object EntryOpenFeatureContributor : FeatureGraphContributor {
    override val owner = ENTRY_OPEN_FEATURE_OWNER

    override fun contributeTo(sink: FeatureGraphContributionSink) {
        sink.add(
            FeatureContribution(
                feature = ENTRY_OPEN_FEATURE_ID,
                owner = owner,
                integrations = listOf(
                    FeatureIntegration(
                        id = ENTRY_OPEN_INTEGRATION_ID,
                        prerequisites = CapabilityExpression.Provided(EntryOpenCapability.definition),
                        sharedConsequences = listOf(EntryOpenDispatchConsequence),
                    ),
                ),
            ),
        )
    }
}

internal class DefaultEntryOpenFeature(
    evaluation: FeatureGraphEvaluation,
    private val interaction: EntryOpenInteraction,
) : EntryOpenFeature {
    private val applicableSubjects = evaluation.sharedConsequences
        .asSequence()
        .filter { applicability ->
            applicability.subject.feature == ENTRY_OPEN_FEATURE_ID &&
                applicability.subject.integration == ENTRY_OPEN_INTEGRATION_ID &&
                applicability.consequence.id == ENTRY_OPEN_DISPATCH_CONSEQUENCE_ID
        }
        .map { it.subject }
        .toSet()

    private val applicableTypes = evaluation.integrations
        .asSequence()
        .filterIsInstance<ApplicableFeatureIntegration>()
        .filter { it.subject in applicableSubjects }
        .flatMap { evaluated -> evaluated.matchedProviders.asSequence() }
        .mapNotNull { provider -> provider.implementation as? EntryOpenProcessor }
        .mapTo(mutableSetOf(), EntryOpenProcessor::type)

    override fun isApplicable(type: EntryType): Boolean = type in applicableTypes

    override fun open(
        context: Context,
        entry: Entry,
        chapter: EntryChapter,
        options: EntryOpenOptions,
    ): Boolean {
        if (!isApplicable(entry.type)) return false
        interaction.open(context, entry, chapter, options)
        return true
    }

    override fun pendingIntent(
        context: Context,
        entry: Entry,
        chapter: EntryChapter,
        options: EntryOpenOptions,
    ): PendingIntent? {
        if (!isApplicable(entry.type)) return null
        return interaction.pendingIntent(context, entry, chapter, options)
    }
}
