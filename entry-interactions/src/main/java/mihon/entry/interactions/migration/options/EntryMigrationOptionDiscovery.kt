package mihon.entry.interactions

import mihon.feature.graph.FeatureExecutionDelivery
import mihon.feature.graph.FeatureExecutionFailurePolicy
import mihon.feature.graph.FeatureExecutionPointId
import mihon.feature.graph.FeatureExecutionRuntime
import mihon.feature.graph.featureExecutionPointDefinition
import tachiyomi.domain.entry.model.Entry

internal data class EntryMigrationOptionDiscoveryEvent(
    val source: Entry,
    val options: EntryMigrationOptionSink,
)

internal fun interface EntryMigrationOptionSink {
    fun add(option: EntryMigrationOption)
}

internal val ENTRY_MIGRATION_OPTION_DISCOVERY_POINT =
    featureExecutionPointDefinition<EntryMigrationOptionDiscoveryEvent>(
        id = FeatureExecutionPointId("entry.migration.option-discovery"),
        owner = ENTRY_MIGRATION_FEATURE_OWNER,
        delivery = FeatureExecutionDelivery.IMMEDIATE,
        failurePolicy = FeatureExecutionFailurePolicy.FAIL_FAST,
    )

internal class EntryMigrationOptionDiscovery(
    private val executions: FeatureExecutionRuntime,
) {
    suspend fun discover(source: Entry): Set<EntryMigrationOption> {
        val discovered = linkedSetOf<EntryMigrationOption>()
        val result = executions.execute(
            point = ENTRY_MIGRATION_OPTION_DISCOVERY_POINT,
            contentType = source.type.toContentTypeId(),
            event = EntryMigrationOptionDiscoveryEvent(source, discovered::add),
        )
        check(result.isSuccessful) {
            "Migration option discovery failed: ${result.failures.joinToString { it.participant.value }}"
        }
        return discovered
    }
}
