package mihon.feature.graph

/**
 * Owning composition boundary for one or more content-type or feature contributions.
 *
 * The environment supplies contributors to discovery. The graph kernel never maintains a list of concrete types,
 * capabilities, or features.
 */
interface FeatureGraphContributor {
    val owner: ContributionOwner

    fun contributeTo(sink: FeatureGraphContributionSink)
}

fun featureGraphContributor(
    owner: ContributionOwner,
    contribute: FeatureGraphContributionSink.() -> Unit,
): FeatureGraphContributor {
    return object : FeatureGraphContributor {
        override val owner = owner

        override fun contributeTo(sink: FeatureGraphContributionSink) {
            sink.contribute()
        }
    }
}

/** The only surface through which independently owned contributions enter discovery. */
class FeatureGraphContributionSink internal constructor(
    private val owner: ContributionOwner,
) {
    private val contentTypes = mutableListOf<ContentTypeContribution>()
    private val features = mutableListOf<FeatureContribution>()

    fun add(contribution: ContentTypeContribution) {
        require(contribution.owner == owner) {
            "Contributor $owner cannot submit content type ${contribution.contentType} owned by ${contribution.owner}"
        }
        contentTypes += contribution
    }

    fun add(contribution: FeatureContribution) {
        require(contribution.owner == owner) {
            "Contributor $owner cannot submit feature ${contribution.feature} owned by ${contribution.owner}"
        }
        features += contribution
    }

    internal fun snapshot(): DiscoveredFeatureGraphContributions {
        return DiscoveredFeatureGraphContributions(
            contentTypes = contentTypes.toList(),
            features = features.toList(),
        )
    }
}

/** Raw discovered contributions before cross-owner graph validation. */
data class DiscoveredFeatureGraphContributions(
    val contentTypes: List<ContentTypeContribution>,
    val features: List<FeatureContribution>,
)

fun discoverFeatureGraphContributions(
    contributors: Iterable<FeatureGraphContributor>,
): DiscoveredFeatureGraphContributions {
    val snapshots = contributors.map { contributor ->
        FeatureGraphContributionSink(contributor.owner)
            .also(contributor::contributeTo)
            .snapshot()
    }
    return DiscoveredFeatureGraphContributions(
        contentTypes = snapshots.flatMap { it.contentTypes },
        features = snapshots.flatMap { it.features },
    )
}
