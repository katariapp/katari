package mihon.entry.interactions

import eu.kanade.tachiyomi.source.entry.EntryType

private val CAPABILITY_ID_PATTERN = Regex("[a-z][a-z0-9]*(?:[.-][a-z0-9]+)*")

@JvmInline
value class EntryCapabilityId(val value: String) {
    init {
        require(CAPABILITY_ID_PATTERN.matches(value)) {
            "Capability id must be a stable lowercase identifier: $value"
        }
    }

    override fun toString(): String = value
}

@JvmInline
value class EntryCapabilityOwner(val value: String) {
    init {
        requireNonBlank("Capability owner", value)
    }

    override fun toString(): String = value
}

enum class EntryCapabilityScope {
    TYPE_WIDE,
    CONTEXTUAL,
}

/** Identifies a capability implemented by a content type, never a derived feature combination. */
data class EntryFundamentalCapability(
    val id: EntryCapabilityId,
    val scope: EntryCapabilityScope,
)

sealed interface EntryCapabilitySubject {
    val entryType: EntryType

    data class Type(
        override val entryType: EntryType,
    ) : EntryCapabilitySubject

    /** Implemented by feature-specific subjects that retain the runtime inputs needed by their query. */
    interface Contextual : EntryCapabilitySubject
}

data class EntryCapabilityQuery(
    val capability: EntryFundamentalCapability,
    val subject: EntryCapabilitySubject,
) {
    init {
        val subjectScope = when (subject) {
            is EntryCapabilitySubject.Type -> EntryCapabilityScope.TYPE_WIDE
            is EntryCapabilitySubject.Contextual -> EntryCapabilityScope.CONTEXTUAL
        }
        require(capability.scope == subjectScope) {
            "${capability.id} requires a ${capability.scope.name.lowercase()} subject, " +
                "but received ${subjectScope.name.lowercase()}"
        }
    }
}

sealed interface EntryCapabilityEvidence {
    val owner: EntryCapabilityOwner

    data class ProviderRegistration(
        override val owner: EntryCapabilityOwner,
        val provider: String,
    ) : EntryCapabilityEvidence {
        init {
            requireNonBlank("Capability provider", provider)
        }
    }

    data class Intrinsic(
        override val owner: EntryCapabilityOwner,
        val reason: String,
    ) : EntryCapabilityEvidence {
        init {
            requireNonBlank("Intrinsic capability reason", reason)
        }
    }

    data class External(
        override val owner: EntryCapabilityOwner,
        val fact: String,
    ) : EntryCapabilityEvidence {
        init {
            requireNonBlank("External capability fact", fact)
        }
    }

    data class Context(
        override val owner: EntryCapabilityOwner,
        val condition: String,
    ) : EntryCapabilityEvidence {
        init {
            requireNonBlank("Contextual capability condition", condition)
        }
    }
}

data class EntryIntrinsicCapabilityDeclaration(
    val entryType: EntryType,
    val capability: EntryFundamentalCapability,
    val owner: EntryCapabilityOwner,
    val reason: String,
) {
    init {
        require(capability.scope == EntryCapabilityScope.TYPE_WIDE) {
            "Intrinsic capability declarations must describe stable type-wide facts: ${capability.id}"
        }
        requireNonBlank("Intrinsic capability reason", reason)
    }

    fun evidenceRecord(): EntryCapabilityEvidenceRecord {
        return EntryCapabilityEvidenceRecord(
            entryType = entryType,
            capability = capability,
            evidence = EntryCapabilityEvidence.Intrinsic(owner, reason),
        )
    }
}

data class EntryCapabilityEvidenceRecord(
    val entryType: EntryType,
    val capability: EntryFundamentalCapability,
    val evidence: EntryCapabilityEvidence,
) {
    init {
        if (evidence is EntryCapabilityEvidence.Intrinsic) {
            require(capability.scope == EntryCapabilityScope.TYPE_WIDE) {
                "Intrinsic capability evidence must describe a stable type-wide fact: ${capability.id}"
            }
        }
        if (evidence is EntryCapabilityEvidence.External || evidence is EntryCapabilityEvidence.Context) {
            require(capability.scope == EntryCapabilityScope.CONTEXTUAL) {
                "External and contextual evidence cannot establish unconditional type support: ${capability.id}"
            }
        }
    }
}

class EntryCapabilityEvidenceSnapshot(
    records: Iterable<EntryCapabilityEvidenceRecord>,
) {
    val records: List<EntryCapabilityEvidenceRecord> = records
        .toList()
        .also(::validateDefinitions)
        .also(::validateAuthorities)
        .sortedWith(compareBy({ it.entryType.ordinal }, { it.capability.id.value }))

    private fun validateDefinitions(records: List<EntryCapabilityEvidenceRecord>) {
        records.groupBy { it.capability.id }
            .forEach { (id, definitions) ->
                val scopes = definitions.mapTo(mutableSetOf()) { it.capability.scope }
                check(scopes.size == 1) {
                    "Contradictory capability definitions for $id: $scopes"
                }
            }
    }

    private fun validateAuthorities(records: List<EntryCapabilityEvidenceRecord>) {
        records.groupBy { it.entryType to it.capability.id }
            .filterValues { it.size > 1 }
            .forEach { (key, authorities) ->
                val evidenceKinds = authorities.map { it.evidence.kind }.toSet()
                val conflict = if (evidenceKinds.size == 1) "Duplicate" else "Contradictory"
                error("$conflict capability evidence for ${key.second} on EntryType ${key.first}: $evidenceKinds")
            }
    }
}

private val EntryCapabilityEvidence.kind: String
    get() = when (this) {
        is EntryCapabilityEvidence.ProviderRegistration -> "ProviderRegistration"
        is EntryCapabilityEvidence.Intrinsic -> "Intrinsic"
        is EntryCapabilityEvidence.External -> "External"
        is EntryCapabilityEvidence.Context -> "Context"
    }

data class EntryCapabilityBlocker(
    val owner: EntryCapabilityOwner,
    val condition: String,
) {
    init {
        requireNonBlank("Capability blocker", condition)
    }
}

data class EntryCapabilityObligation(
    val owner: EntryCapabilityOwner,
    val requirement: String,
) {
    init {
        requireNonBlank("Capability obligation", requirement)
    }
}

sealed interface EntrySupportResult {
    val evidence: List<EntryCapabilityEvidence>

    data class Supported(
        override val evidence: List<EntryCapabilityEvidence>,
    ) : EntrySupportResult {
        init {
            require(evidence.isNotEmpty()) {
                "Supported capability results require authoritative evidence"
            }
        }
    }

    data class IntentionallyUnsupported(
        val owner: EntryCapabilityOwner,
        val reason: String,
        override val evidence: List<EntryCapabilityEvidence> = emptyList(),
    ) : EntrySupportResult {
        init {
            requireNonBlank("Intentional unsupported reason", reason)
        }
    }

    data class NotApplicable(
        val owner: EntryCapabilityOwner,
        val reason: String,
        override val evidence: List<EntryCapabilityEvidence> = emptyList(),
    ) : EntrySupportResult {
        init {
            requireNonBlank("Not-applicable reason", reason)
        }
    }

    data class ContextuallyUnavailable(
        val blocker: EntryCapabilityBlocker,
        override val evidence: List<EntryCapabilityEvidence> = emptyList(),
    ) : EntrySupportResult

    data class MissingObligation(
        val obligation: EntryCapabilityObligation,
        override val evidence: List<EntryCapabilityEvidence> = emptyList(),
    ) : EntrySupportResult

    data class Unresolved(
        val owner: EntryCapabilityOwner,
        val reason: String,
        override val evidence: List<EntryCapabilityEvidence> = emptyList(),
    ) : EntrySupportResult {
        init {
            requireNonBlank("Unresolved capability reason", reason)
        }
    }
}

data class EntryCapabilityAssessment(
    val query: EntryCapabilityQuery,
    val result: EntrySupportResult,
) {
    init {
        require(
            result !is EntrySupportResult.ContextuallyUnavailable || query.subject is EntryCapabilitySubject.Contextual,
        ) {
            "Contextually unavailable is valid only for a contextual capability query"
        }
        require(result !is EntrySupportResult.MissingObligation) {
            "A missing feature obligation must not replace a fundamental capability result"
        }
    }
}

private fun requireNonBlank(label: String, value: String) {
    require(value.isNotBlank() && value == value.trim()) {
        "$label must be non-blank and trimmed"
    }
}
