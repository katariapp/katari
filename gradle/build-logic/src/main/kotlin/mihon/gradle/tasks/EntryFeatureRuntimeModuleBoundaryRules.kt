package mihon.gradle.tasks

internal fun checkEntryFeatureRuntimeModuleBoundaries(
    sources: List<EntryFeatureRuntimeModuleBoundarySource>,
): List<EntryFeatureRuntimeModuleBoundaryFinding> {
    val findings = mutableListOf<EntryFeatureRuntimeModuleBoundaryFinding>()
    val productionSources = sources.filter { "/src/main/" in it.relativePath }
    val topology = productionSources.singleOrNull {
        it.relativePath.endsWith("/runtime/EntryInteractionProductionTopology.kt")
    } ?: return listOf(
        EntryFeatureRuntimeModuleBoundaryFinding(
            relativePath = "entry-interactions/src/main",
            lineNumber = null,
            reason = "Entry Feature production topology is missing",
        ),
    )

    val declaredModules = productionSources.flatMap { source ->
        FEATURE_MODULE_DECLARATION.findAll(source.content).map { match ->
            NamedSourceDeclaration(match.groupValues[1], source, source.lineNumber(match.range.first))
        }
    }
    productionSources.forEach { source ->
        FEATURE_MODULE_CONSTRUCTION.findAll(source.content).forEach { construction ->
            val lineStart = source.content.lastIndexOf('\n', construction.range.first).let { it + 1 }
            val line = source.content.substring(lineStart, construction.range.last + 1)
            if (!FEATURE_MODULE_DECLARATION.containsMatchIn(line)) {
                findings += EntryFeatureRuntimeModuleBoundaryFinding(
                    relativePath = source.relativePath,
                    lineNumber = source.lineNumber(construction.range.first),
                    reason = "EntryFeatureRuntimeModule must be declared as an internal *FeatureRuntimeModule value " +
                        "so production installation coverage can be enforced",
                )
            }
        }
    }
    declaredModules.groupBy(NamedSourceDeclaration::name).filterValues { it.size > 1 }.forEach { (name, duplicates) ->
        duplicates.forEach { duplicate ->
            findings += duplicate.finding("duplicate Entry Feature runtime module declaration: $name")
        }
    }

    val installedModules = productionTopologyModuleNames(topology.content)
    declaredModules.forEach { declaration ->
        if (installedModules.count { it == declaration.name } != 1) {
            findings += declaration.finding(
                "Entry Feature runtime module ${declaration.name} must be installed exactly once by " +
                    "productionEntryFeatureRuntimeModules",
            )
        }
    }
    installedModules.filter { installed -> declaredModules.none { it.name == installed } }.forEach { unknown ->
        findings += EntryFeatureRuntimeModuleBoundaryFinding(
            relativePath = topology.relativePath,
            lineNumber = topology.lineNumber(unknown),
            reason = "production topology references undeclared Entry Feature runtime module $unknown",
        )
    }

    val contributors = productionSources.flatMap { source ->
        FEATURE_CONTRIBUTOR_DECLARATION.findAll(source.content).map { match ->
            NamedSourceDeclaration(match.groupValues[1], source, source.lineNumber(match.range.first))
        }
    }
    val boundContributors = productionSources.flatMap { source ->
        CONTRIBUTOR_BINDING.findAll(source.content).map { match -> match.groupValues[1] }.toList()
    }
    contributors.forEach { contributor ->
        val bindingCount = boundContributors.count { it == contributor.name }
        if (bindingCount != 1) {
            findings += contributor.finding(
                "Feature graph contributor ${contributor.name} must belong to exactly one EntryFeatureRuntimeModule; " +
                    "found $bindingCount",
            )
        }
    }

    productionSources
        .filterNot { it.relativePath == topology.relativePath }
        .forEach { source ->
            source.content.lines().forEachIndexed { index, line ->
                if ("productionEntryFeatureGraphForValidation" in line) {
                    findings += EntryFeatureRuntimeModuleBoundaryFinding(
                        relativePath = source.relativePath,
                        lineNumber = index + 1,
                        reason = "graph-only production Feature view is validation-only; " +
                            "install runtime modules instead",
                    )
                }
            }
        }
    productionSources.forEach { source ->
        BEHAVIOR_ID_VALUE_REFERENCE.findAll(source.content).forEach { reference ->
            findings += EntryFeatureRuntimeModuleBoundaryFinding(
                relativePath = source.relativePath,
                lineNumber = source.lineNumber(reference.range.first),
                reason = "Feature Behavior projection ids are descriptive and cannot be used as runtime dispatch keys",
            )
        }
    }
    return findings
}

private fun productionTopologyModuleNames(content: String): List<String> {
    val body = PRODUCTION_TOPOLOGY.find(content)?.groupValues?.get(1).orEmpty()
    return FEATURE_MODULE_REFERENCE.findAll(body).map { it.value }.toList()
}

private fun EntryFeatureRuntimeModuleBoundarySource.lineNumber(offset: Int): Int {
    return content.take(offset).count { it == '\n' } + 1
}

private fun EntryFeatureRuntimeModuleBoundarySource.lineNumber(text: String): Int? {
    val offset = content.indexOf(text)
    return offset.takeIf { it >= 0 }?.let(::lineNumber)
}

private fun NamedSourceDeclaration.finding(reason: String): EntryFeatureRuntimeModuleBoundaryFinding {
    return EntryFeatureRuntimeModuleBoundaryFinding(source.relativePath, lineNumber, reason)
}

private data class NamedSourceDeclaration(
    val name: String,
    val source: EntryFeatureRuntimeModuleBoundarySource,
    val lineNumber: Int,
)

internal data class EntryFeatureRuntimeModuleBoundarySource(
    val relativePath: String,
    val content: String,
)

internal data class EntryFeatureRuntimeModuleBoundaryFinding(
    val relativePath: String,
    val lineNumber: Int?,
    val reason: String,
)

private val FEATURE_MODULE_DECLARATION = Regex(
    """internal\s+val\s+([A-Za-z_][A-Za-z0-9_]*FeatureRuntimeModule)\s*=\s*EntryFeatureRuntimeModule\s*\(""",
)
private val FEATURE_MODULE_CONSTRUCTION = Regex("""(?<!class\s)\bEntryFeatureRuntimeModule\s*\(""")
private val FEATURE_CONTRIBUTOR_DECLARATION = Regex(
    """(?:internal\s+)?(?:object|class)\s+([A-Za-z_][A-Za-z0-9_]*FeatureContributor)\s*:\s*FeatureGraphContributor""",
)
private val CONTRIBUTOR_BINDING = Regex("""contributor\s*=\s*([A-Za-z_][A-Za-z0-9_]*)""")
private val PRODUCTION_TOPOLOGY = Regex(
    """productionEntryFeatureRuntimeModules\(\)\s*:\s*List<EntryFeatureRuntimeModule>\s*=\s*listOf\((.*?)\n\)""",
    RegexOption.DOT_MATCHES_ALL,
)
private val FEATURE_MODULE_REFERENCE = Regex("""\b[A-Za-z_][A-Za-z0-9_]*FeatureRuntimeModule\b""")
private val BEHAVIOR_ID_VALUE_REFERENCE = Regex(
    """\b[A-Za-z_][A-Za-z0-9_]*Behavior(?:\.[A-Z][A-Z0-9_]*)?\.id\.value\b""",
)
