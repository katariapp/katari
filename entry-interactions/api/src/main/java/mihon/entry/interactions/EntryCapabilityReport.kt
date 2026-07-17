package mihon.entry.interactions

import eu.kanade.tachiyomi.source.entry.EntryType

data class EntryCapabilityOutcomeDeclaration(
    val entryType: EntryType,
    val capability: EntryFundamentalCapability,
    val result: EntrySupportResult,
) {
    init {
        require(
            result is EntrySupportResult.IntentionallyUnsupported || result is EntrySupportResult.NotApplicable,
        ) {
            "Explicit type outcomes are reserved for intentional unsupported and not-applicable decisions"
        }
        require(result.evidence.isEmpty()) {
            "Explicit absence declarations cannot carry positive capability evidence"
        }
    }
}

class EntryCapabilityOutcomeSnapshot(
    declarations: Iterable<EntryCapabilityOutcomeDeclaration>,
) {
    val declarations: List<EntryCapabilityOutcomeDeclaration> = declarations
        .toList()
        .also(::validateDefinitions)
        .also(::validateAuthorities)
        .sortedWith(compareBy({ it.entryType.ordinal }, { it.capability.id.value }))

    private fun validateDefinitions(declarations: List<EntryCapabilityOutcomeDeclaration>) {
        declarations.groupBy { it.capability.id }
            .forEach { (id, definitions) ->
                val scopes = definitions.mapTo(mutableSetOf()) { it.capability.scope }
                check(scopes.size == 1) {
                    "Contradictory capability outcome definitions for $id: $scopes"
                }
            }
    }

    private fun validateAuthorities(declarations: List<EntryCapabilityOutcomeDeclaration>) {
        declarations.groupBy { it.entryType to it.capability.id }
            .filterValues { it.size > 1 }
            .forEach { (key, _) ->
                error("Duplicate capability outcome for ${key.second} on EntryType ${key.first}")
            }
    }
}

sealed interface EntryCapabilityReportValue {
    data class Outcome(
        val result: EntrySupportResult,
    ) : EntryCapabilityReportValue {
        init {
            require(result !is EntrySupportResult.ContextuallyUnavailable) {
                "A deterministic type report cannot evaluate runtime context"
            }
            require(result !is EntrySupportResult.MissingObligation) {
                "Feature obligations are reported separately from fundamental capability support"
            }
        }
    }

    data class Conditional(
        val evidence: List<EntryCapabilityEvidence>,
    ) : EntryCapabilityReportValue {
        init {
            require(evidence.isNotEmpty()) {
                "Conditional capability reports require provider evidence"
            }
        }
    }
}

data class EntryCapabilityReportEntry(
    val capability: EntryFundamentalCapability,
    val value: EntryCapabilityReportValue,
) {
    init {
        when (value) {
            is EntryCapabilityReportValue.Conditional -> {
                require(capability.scope == EntryCapabilityScope.CONTEXTUAL) {
                    "Only contextual capabilities can be reported as conditional: ${capability.id}"
                }
            }
            is EntryCapabilityReportValue.Outcome -> {
                if (value.result is EntrySupportResult.Supported) {
                    require(capability.scope == EntryCapabilityScope.TYPE_WIDE) {
                        "Contextual capabilities cannot be reported as unconditional support: ${capability.id}"
                    }
                }
            }
        }
    }
}

data class EntryCapabilityTypeReport(
    val entryType: EntryType,
    val entries: List<EntryCapabilityReportEntry>,
) {
    fun entry(capability: EntryFundamentalCapability): EntryCapabilityReportEntry {
        return entries.single { it.capability == capability }
    }
}

data class EntryCapabilityReport(
    val types: List<EntryCapabilityTypeReport>,
) {
    fun type(entryType: EntryType): EntryCapabilityTypeReport {
        return types.single { it.entryType == entryType }
    }

    fun supportsTypeWide(entryType: EntryType, capability: EntryFundamentalCapability): Boolean {
        require(capability.scope == EntryCapabilityScope.TYPE_WIDE) {
            "Type-wide support queries cannot evaluate contextual capability ${capability.id}"
        }
        val value = type(entryType).entry(capability).value
        return (value as? EntryCapabilityReportValue.Outcome)?.result is EntrySupportResult.Supported
    }
}

fun createEntryCapabilityReport(
    registeredTypes: Iterable<EntryType>,
    evidence: EntryCapabilityEvidenceSnapshot,
    outcomes: EntryCapabilityOutcomeSnapshot,
    catalog: List<EntryFundamentalCapability> = EntryCapabilityCatalog.capabilities,
): EntryCapabilityReport {
    validateCatalog(catalog, evidence, outcomes)

    val evidenceByKey = evidence.records.associateBy { it.entryType to it.capability.id }
    val outcomesByKey = outcomes.declarations.associateBy { it.entryType to it.capability.id }
    val conflictingKeys = evidenceByKey.keys intersect outcomesByKey.keys
    check(conflictingKeys.isEmpty()) {
        "Capability facts cannot have both positive evidence and an explicit absence: $conflictingKeys"
    }

    val reports = (
        registeredTypes +
            evidence.records.map { it.entryType } +
            outcomes.declarations.map { it.entryType }
        )
        .toSet()
        .sortedBy(EntryType::ordinal)
        .map { entryType ->
            EntryCapabilityTypeReport(
                entryType = entryType,
                entries = catalog.sortedBy { it.id.value }.map { capability ->
                    val key = entryType to capability.id
                    EntryCapabilityReportEntry(
                        capability = capability,
                        value = when (val record = evidenceByKey[key]) {
                            null -> outcomesByKey[key]?.let { EntryCapabilityReportValue.Outcome(it.result) }
                                ?: unresolved(capability)
                            else -> when (capability.scope) {
                                EntryCapabilityScope.TYPE_WIDE -> EntryCapabilityReportValue.Outcome(
                                    EntrySupportResult.Supported(listOf(record.evidence)),
                                )
                                EntryCapabilityScope.CONTEXTUAL -> EntryCapabilityReportValue.Conditional(
                                    listOf(record.evidence),
                                )
                            }
                        },
                    )
                },
            )
        }
    return EntryCapabilityReport(reports)
}

private fun unresolved(capability: EntryFundamentalCapability): EntryCapabilityReportValue {
    return EntryCapabilityReportValue.Outcome(
        EntrySupportResult.Unresolved(
            owner = EntryCapabilityOwner("entry-interactions.capability-report"),
            reason = "No authoritative evidence or accepted absence for ${capability.id}",
        ),
    )
}

private fun validateCatalog(
    catalog: List<EntryFundamentalCapability>,
    evidence: EntryCapabilityEvidenceSnapshot,
    outcomes: EntryCapabilityOutcomeSnapshot,
) {
    val catalogById = catalog.associateBy { it.id }
    check(catalogById.size == catalog.size) {
        "Capability report catalog ids must be unique"
    }
    (evidence.records.map { it.capability } + outcomes.declarations.map { it.capability })
        .forEach { definition ->
            check(catalogById[definition.id] == definition) {
                "Capability evidence must use the reviewed catalog definition for ${definition.id}"
            }
        }
}
