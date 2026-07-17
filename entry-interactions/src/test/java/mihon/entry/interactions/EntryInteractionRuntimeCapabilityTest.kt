package mihon.entry.interactions

import android.app.Application
import eu.kanade.tachiyomi.source.entry.EntryType
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkClass
import org.junit.jupiter.api.Test
import tachiyomi.core.common.preference.InMemoryPreferenceStore
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.InjektScope
import uy.kohesive.injekt.api.erasedType
import uy.kohesive.injekt.api.get
import uy.kohesive.injekt.registry.default.DefaultRegistrar
import java.lang.reflect.Type
import java.nio.file.Files

class EntryInteractionRuntimeCapabilityTest {

    @Test
    fun `production runtime exposes composed capabilities for every content type`() {
        val previousInjekt = Injekt
        val registrar = ProductionCompositionRegistrar()
        val cacheDirectory = Files.createTempDirectory("katari-entry-runtime-capabilities").toFile()
        val application = mockk<Application> {
            every { cacheDir } returns cacheDirectory
        }
        val preferenceStore = InMemoryPreferenceStore()
        Injekt = InjektScope(registrar)

        try {
            registrar.addEntryInteractionRuntime(
                app = application,
                dependencies = EntryInteractionRuntimeDependencies(
                    activityTheme = mockk(),
                    notificationActions = mockk(),
                    pageImageCache = mockk(),
                    mangaChildGroupFilterDataSource = mockk(),
                    readerIncognitoState = mockk(),
                    readerTracking = mockk(),
                    profilePreferenceStore = preferenceStore,
                    basePreferenceStore = preferenceStore,
                    privatePreferenceStore = preferenceStore,
                ),
            )

            val interactions = registrar.get<EntryInteractions>()
            val report = registrar.get<EntryCapabilityReport>()
            val bookmarkInteraction = registrar.get<EntryBookmarkInteraction>()

            (interactions.capabilityReport === report) shouldBe true
            (interactions.bookmark === bookmarkInteraction) shouldBe true
            report.types.map { it.entryType } shouldBe EntryType.entries
            EntryType.entries.forEach { entryType ->
                interactions.download.supportsDownloads(entryType) shouldBe true
                report.type(entryType)
                    .entry(EntryCapabilityCatalog.DOWNLOADS)
                    .supported shouldBe true
            }
            report.types.filter {
                report.supportsTypeWide(it.entryType, EntryCapabilityCatalog.BOOKMARKING)
            }.map { it.entryType } shouldBe listOf(EntryType.MANGA)
            report.types.filter {
                EntryDownloadCapabilityPolicy.supportsBookmarkedBulkDownloads(report, it.entryType)
            }.map { it.entryType } shouldBe listOf(EntryType.MANGA)
        } finally {
            Injekt = previousInjekt
            cacheDirectory.deleteRecursively()
        }
    }
}

/**
 * Resolves only the production composition boundary from its registered factory. Operational dependencies are relaxed
 * because this test verifies plugin assembly and capability evidence, not downloader construction or media behavior.
 */
private class ProductionCompositionRegistrar : DefaultRegistrar() {
    private val operationalMocks = mutableMapOf<Type, Any>()

    override fun <R : Any> getInstance(forType: Type): R {
        return if (forType.erasedType() in productionBoundaryTypes) {
            super.getInstance(forType)
        } else {
            operationalMock(forType)
        }
    }

    override fun <R : Any> getInstanceOrNull(forType: Type): R? {
        return getInstance(forType)
    }

    override fun <R : Any> getInstanceOrElse(forType: Type, default: R): R {
        return getInstance(forType)
    }

    override fun <R : Any> getInstanceOrElse(forType: Type, default: () -> R): R {
        return getInstance(forType)
    }

    @Suppress("UNCHECKED_CAST")
    private fun <R : Any> operationalMock(forType: Type): R {
        return operationalMocks.getOrPut(forType) {
            mockkClass(forType.erasedType().kotlin, relaxed = true)
        } as R
    }

    private companion object {
        val productionBoundaryTypes = setOf(
            EntryInteractions::class.java,
            EntryCapabilityReport::class.java,
            EntryBookmarkInteraction::class.java,
        )
    }
}

private val EntryCapabilityReportEntry.supported: Boolean
    get() = (value as? EntryCapabilityReportValue.Outcome)?.result is EntrySupportResult.Supported
