package mihon.entry.interactions

import mihon.feature.graph.FeatureExecutionDelivery
import mihon.feature.graph.FeatureExecutionFailurePolicy
import mihon.feature.graph.FeatureExecutionPointId
import mihon.feature.graph.featureExecutionPointDefinition
import tachiyomi.domain.entry.model.Entry
import tachiyomi.domain.entry.model.EntryChapter

internal val ENTRY_SOURCE_REFRESH_NEW_CHILDREN_EXECUTION_POINT =
    featureExecutionPointDefinition<EntrySourceRefreshNewChildrenEvent>(
        id = FeatureExecutionPointId("entry.source-refresh.new-children"),
        owner = ENTRY_SOURCE_REFRESH_OWNER,
        delivery = FeatureExecutionDelivery.AFTER_COMMIT,
        failurePolicy = FeatureExecutionFailurePolicy.FAIL_FAST,
    )

internal data class EntrySourceRefreshNewChildrenEvent(
    val entry: Entry,
    val newChildren: List<EntryChapter>,
)
