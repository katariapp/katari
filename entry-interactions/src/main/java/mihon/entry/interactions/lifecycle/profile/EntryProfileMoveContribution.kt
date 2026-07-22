package mihon.entry.interactions

import mihon.entry.interactions.documentation.EntryContentTypeReferenceSection
import mihon.entry.interactions.documentation.entryContentTypeReferenceContribution
import mihon.feature.graph.CapabilityExpression
import mihon.feature.graph.ContributionOwner
import mihon.feature.graph.FeatureArtifactId
import mihon.feature.graph.FeatureBehaviorContract
import mihon.feature.graph.FeatureBehaviorProjection
import mihon.feature.graph.FeatureContribution
import mihon.feature.graph.FeatureExecutionDelivery
import mihon.feature.graph.FeatureExecutionFailurePolicy
import mihon.feature.graph.FeatureExecutionPointId
import mihon.feature.graph.FeatureGraphContributionSink
import mihon.feature.graph.FeatureGraphContributor
import mihon.feature.graph.FeatureId
import mihon.feature.graph.FeatureIntegration
import mihon.feature.graph.FeatureIntegrationId
import mihon.feature.graph.featureExecutionPointDefinition

internal val ENTRY_PROFILE_MOVE_OWNER = ContributionOwner("entry-profile-move")
private val ENTRY_PROFILE_MOVE_REFERENCE = entryContentTypeReferenceContribution(
    id = "profile-move",
    owner = ENTRY_PROFILE_MOVE_OWNER,
    section = EntryContentTypeReferenceSection.LIBRARY_AND_UPDATES,
    label = "Move Library entries between profiles",
    order = 110,
)

internal object EntryProfileMoveBehaviorContract : FeatureBehaviorContract {
    override val id = FeatureArtifactId("entry.profile-move.behavior")
}

private object EntryProfileMoveBehavior : FeatureBehaviorProjection {
    override val id = FeatureArtifactId("entry.profile-move.workflow")
}

internal val ENTRY_PROFILE_MOVE_PREPARING_EXECUTION_POINT =
    featureExecutionPointDefinition<EntryProfileMovePreparingEvent>(
        id = FeatureExecutionPointId("entry.profile-move.preparing"),
        owner = ENTRY_PROFILE_MOVE_OWNER,
        delivery = FeatureExecutionDelivery.IMMEDIATE,
        failurePolicy = FeatureExecutionFailurePolicy.FAIL_FAST,
    )

internal val ENTRY_PROFILE_MOVE_DESTINATION_INSPECTING_EXECUTION_POINT =
    featureExecutionPointDefinition<EntryProfileMoveDestinationInspectingEvent>(
        id = FeatureExecutionPointId("entry.profile-move.destination-inspecting"),
        owner = ENTRY_PROFILE_MOVE_OWNER,
        delivery = FeatureExecutionDelivery.IMMEDIATE,
        failurePolicy = FeatureExecutionFailurePolicy.FAIL_FAST,
    )

internal val ENTRY_PROFILE_MOVING_EXECUTION_POINT = featureExecutionPointDefinition<EntryProfileMovingEvent>(
    id = FeatureExecutionPointId("entry.profile-move.moving"),
    owner = ENTRY_PROFILE_MOVE_OWNER,
    delivery = FeatureExecutionDelivery.TRANSACTIONAL,
    failurePolicy = FeatureExecutionFailurePolicy.FAIL_FAST,
)

internal val ENTRY_PROFILE_STATE_MOVED_EXECUTION_POINT = featureExecutionPointDefinition<EntryProfileStateMovedEvent>(
    id = FeatureExecutionPointId("entry.profile-move.state-moved"),
    owner = ENTRY_PROFILE_MOVE_OWNER,
    delivery = FeatureExecutionDelivery.TRANSACTIONAL,
    failurePolicy = FeatureExecutionFailurePolicy.FAIL_FAST,
)

internal val ENTRY_PROFILE_MOVED_EXECUTION_POINT = featureExecutionPointDefinition<EntryProfileMovedEvent>(
    id = FeatureExecutionPointId("entry.profile-move.moved"),
    owner = ENTRY_PROFILE_MOVE_OWNER,
    delivery = FeatureExecutionDelivery.AFTER_COMMIT,
    failurePolicy = FeatureExecutionFailurePolicy.CONTINUE_AND_REPORT,
)

internal object EntryProfileMoveFeatureContributor : FeatureGraphContributor {
    override val owner = ENTRY_PROFILE_MOVE_OWNER

    override fun contributeTo(sink: FeatureGraphContributionSink) {
        sink.add(
            FeatureContribution(
                feature = FeatureId("entry.profile-move"),
                owner = owner,
                integrations = listOf(
                    FeatureIntegration(
                        id = FeatureIntegrationId("entry.profile-move.workflow"),
                        prerequisites = CapabilityExpression.Always,
                        behaviorProjections = listOf(EntryProfileMoveBehavior),
                        behavioralContracts = listOf(EntryProfileMoveBehaviorContract),
                        projectionRequirements = listOf(ENTRY_PROFILE_MOVE_REFERENCE.requirement),
                        projections = listOf(ENTRY_PROFILE_MOVE_REFERENCE.projection),
                    ),
                ),
            ),
        )
        sink.add(ENTRY_PROFILE_MOVE_PREPARING_EXECUTION_POINT)
        sink.add(ENTRY_PROFILE_MOVE_DESTINATION_INSPECTING_EXECUTION_POINT)
        sink.add(ENTRY_PROFILE_MOVING_EXECUTION_POINT)
        sink.add(ENTRY_PROFILE_STATE_MOVED_EXECUTION_POINT)
        sink.add(ENTRY_PROFILE_MOVED_EXECUTION_POINT)
    }
}
