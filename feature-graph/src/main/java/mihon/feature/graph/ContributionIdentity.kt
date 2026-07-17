package mihon.feature.graph

private val STABLE_ID_PATTERN = Regex("[a-z][a-z0-9]*(?:[.-][a-z0-9]+)*")

/** Stable identity of the module or feature responsible for a contribution. */
@JvmInline
value class ContributionOwner(val value: String) {
    init {
        validateStableId("Contribution owner", value)
    }

    override fun toString(): String = value
}

/** Stable product identity of a content type, independent of any capabilities it currently supports. */
@JvmInline
value class ContentTypeId(val value: String) {
    init {
        validateStableId("Content type id", value)
    }

    override fun toString(): String = value
}

/** Stable identity of a provider-backed fundamental capability. */
@JvmInline
value class CapabilityId(val value: String) {
    init {
        validateStableId("Capability id", value)
    }

    override fun toString(): String = value
}

/** Stable identity of a feature that consumes capabilities. */
@JvmInline
value class FeatureId(val value: String) {
    init {
        validateStableId("Feature id", value)
    }

    override fun toString(): String = value
}

/** Stable identity of one applicability relationship owned by a feature. */
@JvmInline
value class FeatureIntegrationId(val value: String) {
    init {
        validateStableId("Feature integration id", value)
    }

    override fun toString(): String = value
}

/** Stable identity within the consequence, contract, or projection namespace of a feature. */
@JvmInline
value class FeatureArtifactId(val value: String) {
    init {
        validateStableId("Feature artifact id", value)
    }

    override fun toString(): String = value
}

/** Stable identity of contextual information consumed while evaluating a feature. */
@JvmInline
value class ContextInputId(val value: String) {
    init {
        validateStableId("Context input id", value)
    }

    override fun toString(): String = value
}

/** Stable identity of media-specific work required only after a feature becomes applicable. */
@JvmInline
value class SpecializedAdapterId(val value: String) {
    init {
        validateStableId("Specialized adapter id", value)
    }

    override fun toString(): String = value
}

private fun validateStableId(label: String, value: String) {
    require(STABLE_ID_PATTERN.matches(value)) {
        "$label must be a stable lowercase identifier: $value"
    }
}
