package mihon.entry.interactions

import mihon.feature.graph.CapabilityExpression
import mihon.feature.graph.FeatureContextDecision
import mihon.feature.graph.FeatureIntegration
import mihon.feature.graph.featureContextRule

internal fun entryTrackingIntegrations(): List<FeatureIntegration> = listOf(
    FeatureIntegration(
        id = EntryTrackingIntegration.REGISTRY.id,
        prerequisites = CapabilityExpression.Always,
        sharedConsequences = listOf(
            EntryTrackingConsequence.SERVICE_REGISTRY,
            EntryTrackingConsequence.SERVICE_PRESENTATION,
            EntryTrackingConsequence.ACCOUNT_SETTINGS,
            EntryTrackingConsequence.BACKUP_DIAGNOSTICS,
        ),
        behavioralContracts = listOf(EntryTrackingBehaviorContract),
    ),
    FeatureIntegration(
        id = EntryTrackingIntegration.AVAILABILITY.id,
        prerequisites = CapabilityExpression.Always,
        contextInputs = listOf(ENTRY_TRACKING_REGISTERED_SUPPORT),
        contextRule = featureContextRule(ENTRY_TRACKING_OWNER) { evidence ->
            if (evidence.value(ENTRY_TRACKING_REGISTERED_SUPPORT)) {
                FeatureContextDecision.Applicable
            } else {
                FeatureContextDecision.Blocked(listOf(NO_REGISTERED_TRACKER))
            }
        },
        contextBlockers = listOf(NO_REGISTERED_TRACKER),
        sharedConsequences = listOf(
            EntryTrackingConsequence.ENTRY_ACTION,
            EntryTrackingConsequence.DOCUMENTATION,
        ),
    ),
    FeatureIntegration(
        id = EntryTrackingIntegration.SESSION.id,
        prerequisites = CapabilityExpression.Always,
        contextInputs = listOf(
            ENTRY_TRACKING_REGISTERED_SUPPORT,
            ENTRY_TRACKING_AUTHENTICATED_SUPPORT,
            ENTRY_TRACKING_SOURCE_ACCEPTED,
        ),
        contextRule = featureContextRule(ENTRY_TRACKING_OWNER) { evidence ->
            when {
                !evidence.value(ENTRY_TRACKING_REGISTERED_SUPPORT) ->
                    FeatureContextDecision.Blocked(listOf(NO_REGISTERED_TRACKER))
                !evidence.value(ENTRY_TRACKING_AUTHENTICATED_SUPPORT) ->
                    FeatureContextDecision.Blocked(listOf(NO_AUTHENTICATED_TRACKER))
                !evidence.value(ENTRY_TRACKING_SOURCE_ACCEPTED) ->
                    FeatureContextDecision.Blocked(listOf(SOURCE_NOT_ACCEPTED))
                else -> FeatureContextDecision.Applicable
            }
        },
        contextBlockers = listOf(NO_REGISTERED_TRACKER, NO_AUTHENTICATED_TRACKER, SOURCE_NOT_ACCEPTED),
        sharedConsequences = listOf(
            EntryTrackingConsequence.ENTRY_SESSION,
            EntryTrackingConsequence.ENTRY_OPERATIONS,
        ),
    ),
    FeatureIntegration(
        id = EntryTrackingIntegration.AUTOMATIC_BINDING.id,
        prerequisites = CapabilityExpression.Always,
        contextInputs = listOf(
            ENTRY_TRACKING_REGISTERED_SUPPORT,
            ENTRY_TRACKING_AUTHENTICATED_SUPPORT,
            ENTRY_TRACKING_AUTOMATIC_SOURCE_ACCEPTED,
        ),
        contextRule = featureContextRule(ENTRY_TRACKING_OWNER) { evidence ->
            when {
                !evidence.value(ENTRY_TRACKING_REGISTERED_SUPPORT) ->
                    FeatureContextDecision.Blocked(listOf(NO_REGISTERED_TRACKER))
                !evidence.value(ENTRY_TRACKING_AUTHENTICATED_SUPPORT) ->
                    FeatureContextDecision.Blocked(listOf(NO_AUTHENTICATED_TRACKER))
                !evidence.value(ENTRY_TRACKING_AUTOMATIC_SOURCE_ACCEPTED) ->
                    FeatureContextDecision.Blocked(listOf(NO_AUTOMATIC_TRACKER))
                else -> FeatureContextDecision.Applicable
            }
        },
        contextBlockers = listOf(NO_REGISTERED_TRACKER, NO_AUTHENTICATED_TRACKER, NO_AUTOMATIC_TRACKER),
        sharedConsequences = listOf(EntryTrackingConsequence.AUTOMATIC_BINDING),
    ),
    FeatureIntegration(
        id = EntryTrackingIntegration.SYNCHRONIZATION.id,
        prerequisites = CapabilityExpression.Always,
        contextInputs = listOf(
            ENTRY_TRACKING_REGISTERED_SUPPORT,
            ENTRY_TRACKING_AUTHENTICATED_SUPPORT,
            ENTRY_TRACKING_AUTHENTICATED_TRACK,
        ),
        contextRule = featureContextRule(ENTRY_TRACKING_OWNER) { evidence ->
            when {
                !evidence.value(ENTRY_TRACKING_REGISTERED_SUPPORT) ->
                    FeatureContextDecision.Blocked(listOf(NO_REGISTERED_TRACKER))
                !evidence.value(ENTRY_TRACKING_AUTHENTICATED_SUPPORT) ->
                    FeatureContextDecision.Blocked(listOf(NO_AUTHENTICATED_TRACKER))
                !evidence.value(ENTRY_TRACKING_AUTHENTICATED_TRACK) ->
                    FeatureContextDecision.Blocked(listOf(NO_AUTHENTICATED_TRACK))
                else -> FeatureContextDecision.Applicable
            }
        },
        contextBlockers = listOf(NO_REGISTERED_TRACKER, NO_AUTHENTICATED_TRACKER, NO_AUTHENTICATED_TRACK),
        sharedConsequences = listOf(EntryTrackingConsequence.PROGRESS_SYNCHRONIZATION),
    ),
    authenticatedTypeIntegration(
        integration = EntryTrackingIntegration.LIBRARY,
        consequences = listOf(
            EntryTrackingConsequence.LIBRARY_FILTER_EVIDENCE,
            EntryTrackingConsequence.LIBRARY_SCORE_EVIDENCE,
        ),
    ),
    authenticatedTypeIntegration(
        integration = EntryTrackingIntegration.STATS,
        consequences = listOf(EntryTrackingConsequence.STATS_EVIDENCE),
    ),
)

private fun authenticatedTypeIntegration(
    integration: EntryTrackingIntegration,
    consequences: List<EntryTrackingConsequence>,
): FeatureIntegration = FeatureIntegration(
    id = integration.id,
    prerequisites = CapabilityExpression.Always,
    contextInputs = listOf(
        ENTRY_TRACKING_REGISTERED_SUPPORT,
        ENTRY_TRACKING_AUTHENTICATED_SUPPORT,
    ),
    contextRule = featureContextRule(ENTRY_TRACKING_OWNER) { evidence ->
        when {
            !evidence.value(ENTRY_TRACKING_REGISTERED_SUPPORT) ->
                FeatureContextDecision.Blocked(listOf(NO_REGISTERED_TRACKER))
            !evidence.value(ENTRY_TRACKING_AUTHENTICATED_SUPPORT) ->
                FeatureContextDecision.Blocked(listOf(NO_AUTHENTICATED_TRACKER))
            else -> FeatureContextDecision.Applicable
        }
    },
    contextBlockers = listOf(NO_REGISTERED_TRACKER, NO_AUTHENTICATED_TRACKER),
    sharedConsequences = consequences,
)
