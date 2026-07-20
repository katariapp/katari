package mihon.entry.interactions

import eu.kanade.tachiyomi.source.entry.WebViewSource
import mihon.feature.graph.CapabilityExpression
import mihon.feature.graph.ContextInputId
import mihon.feature.graph.ContributionOwner
import mihon.feature.graph.FeatureArtifactId
import mihon.feature.graph.FeatureContextBlocker
import mihon.feature.graph.FeatureContextDecision
import mihon.feature.graph.FeatureContribution
import mihon.feature.graph.FeatureGraphContributionSink
import mihon.feature.graph.FeatureGraphContributor
import mihon.feature.graph.FeatureGraphEvaluation
import mihon.feature.graph.FeatureId
import mihon.feature.graph.FeatureIntegration
import mihon.feature.graph.FeatureIntegrationId
import mihon.feature.graph.SharedFeatureConsequence
import mihon.feature.graph.contextEvidence
import mihon.feature.graph.contextInputDefinition
import mihon.feature.graph.featureContextRule
import tachiyomi.domain.entry.adapter.toSEntry
import tachiyomi.domain.entry.model.Entry
import tachiyomi.domain.source.service.SourceManager

private val ENTRY_WEB_VIEW_FEATURE_ID = FeatureId("entry.web-view")
private val ENTRY_WEB_VIEW_OWNER = ContributionOwner("entry-web-view")
private val ENTRY_WEB_VIEW_INTEGRATION_ID = FeatureIntegrationId("entry.web-view.source")

private data class EntryWebViewContext(val installed: Boolean, val supported: Boolean)

private val ENTRY_WEB_VIEW_CONTEXT = contextInputDefinition<EntryWebViewContext>(
    ContextInputId("entry.web-view.source-context"),
    ContributionOwner("entry-source"),
)
private val ENTRY_WEB_VIEW_MISSING = FeatureContextBlocker(
    FeatureArtifactId("entry.web-view.source-missing"),
    listOf(ENTRY_WEB_VIEW_CONTEXT),
)
private val ENTRY_WEB_VIEW_UNSUPPORTED = FeatureContextBlocker(
    FeatureArtifactId("entry.web-view.unsupported"),
    listOf(ENTRY_WEB_VIEW_CONTEXT),
)
private enum class EntryWebViewConsequence(
    override val id: FeatureArtifactId,
) : SharedFeatureConsequence {
    AVAILABILITY(FeatureArtifactId("entry.web-view.availability")),
    CANONICAL_URL(FeatureArtifactId("entry.web-view.canonical-url")),
    NAVIGATION(FeatureArtifactId("entry.web-view.navigation")),
    SHARE(FeatureArtifactId("entry.web-view.share")),
    ASSIST(FeatureArtifactId("entry.web-view.assist")),
    RUNTIME_HEADERS(FeatureArtifactId("entry.web-view.runtime-headers")),
}

internal object EntryWebViewFeatureContributor : FeatureGraphContributor {
    override val owner = ENTRY_WEB_VIEW_OWNER

    override fun contributeTo(sink: FeatureGraphContributionSink) {
        sink.add(
            FeatureContribution(
                feature = ENTRY_WEB_VIEW_FEATURE_ID,
                owner = owner,
                integrations = listOf(
                    FeatureIntegration(
                        id = ENTRY_WEB_VIEW_INTEGRATION_ID,
                        prerequisites = CapabilityExpression.Always,
                        contextInputs = listOf(ENTRY_WEB_VIEW_CONTEXT),
                        contextRule = featureContextRule(owner) { evidence ->
                            val context = evidence.value(ENTRY_WEB_VIEW_CONTEXT)
                            when {
                                !context.installed -> FeatureContextDecision.Blocked(listOf(ENTRY_WEB_VIEW_MISSING))
                                !context.supported -> FeatureContextDecision.Blocked(listOf(ENTRY_WEB_VIEW_UNSUPPORTED))
                                else -> FeatureContextDecision.Applicable
                            }
                        },
                        contextBlockers = listOf(ENTRY_WEB_VIEW_MISSING, ENTRY_WEB_VIEW_UNSUPPORTED),
                        sharedConsequences = EntryWebViewConsequence.entries,
                    ),
                ),
            ),
        )
    }
}

internal class DefaultEntryWebViewFeature(
    private val evaluation: FeatureGraphEvaluation,
    private val sourceManager: SourceManager,
) : EntryWebViewFeature {

    override fun resolveEntry(entry: Entry): EntryWebViewResolution {
        val source = sourceManager.get(entry.source)
        val provider = source as? WebViewSource
        requireState(source != null, provider != null)
        if (source == null) return EntryWebViewResolution.Missing(entry.source)
        if (provider == null) return EntryWebViewResolution.Unsupported(entry.source)

        return runCatching {
            EntryWebViewResolution.Available(
                sourceId = source.id,
                url = provider.getContentUrl(entry.toSEntry()),
                headers = provider.getWebViewHeaders(),
            )
        }.getOrElse { EntryWebViewResolution.Failed(entry.source, it) }
    }

    override fun resolveHeaders(sourceId: Long): EntryWebViewHeadersResolution {
        val source = sourceManager.get(sourceId)
        val provider = source as? WebViewSource
        requireState(source != null, provider != null)
        if (source == null) return EntryWebViewHeadersResolution.Missing
        if (provider == null) return EntryWebViewHeadersResolution.Unsupported
        return runCatching { EntryWebViewHeadersResolution.Available(provider.getWebViewHeaders()) }
            .getOrElse(EntryWebViewHeadersResolution::Failed)
    }

    private fun requireState(installed: Boolean, supported: Boolean) {
        EntryWebViewConsequence.entries.forEach { consequence ->
            evaluation.requireSourceContextState(
                feature = ENTRY_WEB_VIEW_FEATURE_ID,
                integration = ENTRY_WEB_VIEW_INTEGRATION_ID,
                consequence = consequence.id,
                evidence = listOf(contextEvidence(ENTRY_WEB_VIEW_CONTEXT, EntryWebViewContext(installed, supported))),
                applicable = installed && supported,
            )
        }
    }
}
