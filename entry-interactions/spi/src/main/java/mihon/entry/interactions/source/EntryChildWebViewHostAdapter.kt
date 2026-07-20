package mihon.entry.interactions

import eu.kanade.tachiyomi.source.entry.EntryType
import mihon.feature.graph.ContributionOwner
import mihon.feature.graph.SpecializedAdapter
import mihon.feature.graph.SpecializedAdapterId
import mihon.feature.graph.specializedAdapterDefinition

/**
 * Type-owned proof that a media host routes canonical child actions through [EntryWebViewFeature].
 *
 * The adapter has no dispatch method: application behavior must continue through the Feature result. It is contributed
 * beside the reader/player UI so contextual graph resolution can report genuinely missing host integration.
 */
interface EntryChildWebViewHostAdapter : EntryInteractionSpecializedAdapter {
    override val type: EntryType
}

object EntryChildWebViewHostRequirement {
    val definition = specializedAdapterDefinition<EntryChildWebViewHostAdapter>(
        id = SpecializedAdapterId("entry.web-view.child-host"),
        owner = ContributionOwner("entry-web-view"),
    )

    fun bind(adapter: EntryChildWebViewHostAdapter): SpecializedAdapter<EntryChildWebViewHostAdapter> =
        SpecializedAdapter(definition, adapter)
}
