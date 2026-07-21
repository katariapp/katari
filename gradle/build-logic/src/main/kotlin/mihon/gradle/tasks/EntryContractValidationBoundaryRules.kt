package mihon.gradle.tasks

internal fun checkEntryContractValidationBoundaries(
    sources: List<EntryContractValidationBoundarySource>,
): List<EntryContractValidationBoundaryFinding> {
    val findings = mutableListOf<EntryContractValidationBoundaryFinding>()

    sources.forEach { source ->
        val isContractContributor = (
            source.fileName.contains("ContractValidation") ||
                source.content.contains("FeatureValidationContributor")
            ) &&
            !source.relativePath.startsWith(VALIDATION_HOST_ROOT)
        val isValidationHost = source.relativePath.startsWith(VALIDATION_HOST_ROOT)

        if (isContractContributor || isValidationHost) {
            CONCRETE_ENTRY_TYPE.findAll(source.content).forEach { match ->
                findings += source.finding(
                    match.range.first,
                    "contract validation must use graph-selected subjects, not a concrete EntryType",
                )
            }
        }

        if (!isContractContributor && !isValidationHost) {
            DECLARATION_ONLY_CONTRACT_REFERENCES.forEach { reference ->
                source.content.indexOf(reference)
                    .takeIf { it >= 0 }
                    ?.let { offset ->
                        findings += source.finding(
                            offset,
                            "ordinary tests cannot inspect or select contract declarations directly: $reference",
                        )
                    }
            }
        }

        CENTRAL_CONTRACT_SUITE_PATTERNS.forEach { pattern ->
            pattern.find(source.content)?.let { match ->
                findings += source.finding(
                    match.range.first,
                    "contract validation must be discovered from feature-owned contributors, not a central suite map",
                )
            }
        }

        if (isValidationHost) {
            CENTRAL_HOST_DISPATCH.find(source.content)?.let { match ->
                findings += source.finding(
                    match.range.first,
                    "the validation host cannot dispatch contracts or features through a central suite switch",
                )
            }
        }

        if (source.relativePath == PRODUCTION_VALIDATION_TEST) {
            BYPASS_VALIDATION_REFERENCES.forEach { reference ->
                source.content.indexOf(reference)
                    .takeIf { it >= 0 }
                    ?.let { offset ->
                        findings += source.finding(
                            offset,
                            "the production gate must call validateEntryInteractionContracts instead of bypassing " +
                                "evaluated selection through $reference",
                        )
                    }
            }
            if (!source.content.contains("validateEntryInteractionContracts(")) {
                findings += EntryContractValidationBoundaryFinding(
                    relativePath = source.relativePath,
                    lineNumber = null,
                    reason = "the production gate must execute the single evaluated Entry contract validation host",
                )
            }
        }
    }

    return findings.distinct()
}

private fun EntryContractValidationBoundarySource.finding(
    offset: Int,
    reason: String,
): EntryContractValidationBoundaryFinding {
    return EntryContractValidationBoundaryFinding(
        relativePath = relativePath,
        lineNumber = content.take(offset).count { it == '\n' } + 1,
        reason = reason,
    )
}

private val EntryContractValidationBoundarySource.fileName: String
    get() = relativePath.substringAfterLast("/")

internal data class EntryContractValidationBoundarySource(
    val relativePath: String,
    val content: String,
)

internal data class EntryContractValidationBoundaryFinding(
    val relativePath: String,
    val lineNumber: Int?,
    val reason: String,
)

private const val VALIDATION_HOST_ROOT =
    "entry-interactions/src/test/java/mihon/entry/interactions/validation/"

private const val PRODUCTION_VALIDATION_TEST =
    "entry-interactions/src/test/java/mihon/entry/interactions/validation/" +
        "ProductionEntryInteractionContractValidationTest.kt"

private val CONCRETE_ENTRY_TYPE = Regex("""\bEntryType\.(?:MANGA|ANIME|BOOK)\b""")

private val DECLARATION_ONLY_CONTRACT_REFERENCES = listOf(
    "behavioralContracts",
    "FeatureContractReference",
    "selectFeatureArtifacts",
    "selectContextualFeatureArtifacts",
)

private val CENTRAL_CONTRACT_SUITE_PATTERNS = listOf(
    Regex("""\bMap\s*<\s*(?:FeatureContractReference|FeatureArtifactId|FeatureId)\b"""),
    Regex("""\b(?:mapOf|mutableMapOf)\s*\([^)]*FeatureContractReference"""),
)

private val CENTRAL_HOST_DISPATCH = Regex("""\bwhen\s*\([^)]*(?:contract|feature)(?:\.id)?[^)]*\)""")

private val BYPASS_VALIDATION_REFERENCES = listOf(
    "discoverAndPlanFeatureContractValidation",
    "planFeatureContractValidation",
    "validateFeatureContracts",
    "selectFeatureArtifacts",
    "selectContextualFeatureArtifacts",
    "resolveFeatureContext",
)
