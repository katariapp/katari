package mihon.gradle.tasks

import io.kotest.matchers.string.shouldContain
import org.gradle.api.GradleException
import org.gradle.testfixtures.ProjectBuilder
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.nio.file.Path
import kotlin.io.path.createDirectories
import kotlin.io.path.writeText

class EntryInteractionBoundaryCheckTaskTest {

    @TempDir
    lateinit var tempDir: Path

    @Test
    fun `valid interaction layout passes`() {
        createBaseFixture()

        runBoundaryCheck()
    }

    @Test
    fun `app cannot depend on type modules or interaction infrastructure directly`() {
        createBaseFixture(
            appBuildGradle = """
                dependencies {
                    implementation(projects.entryInteractions)
                    implementation(projects.entryInteractions.manga)
                    implementation(projects.entryInteractions.api)
                    implementation(projects.entryInteractions.spi)
                    implementation(projects.featureGraph)
                }
            """.trimIndent(),
        )

        val error = assertThrows(GradleException::class.java) { runBoundaryCheck() }

        error.message shouldContain "projects.entryInteractions.manga"
        error.message shouldContain "projects.entryInteractions.api"
        error.message shouldContain "projects.entryInteractions.spi"
        error.message shouldContain "projects.featureGraph"
    }

    @Test
    fun `application-facing api cannot export the feature graph`() {
        createBaseFixture(
            additionalFiles = mapOf(
                "entry-interactions/api/build.gradle.kts" to
                    """
                        dependencies {
                            api(projects.featureGraph)
                        }
                    """.trimIndent(),
            ),
        )

        val error = assertThrows(GradleException::class.java) { runBoundaryCheck() }

        error.message shouldContain "must not export the internal feature graph"
    }

    @Test
    fun `application-facing api cannot declare raw interaction dispatch`() {
        createBaseFixture(
            additionalFiles = mapOf(
                "entry-interactions/api/src/main/java/mihon/entry/interactions/navigation/EntryOpenInteraction.kt" to
                    """
                        package mihon.entry.interactions

                        interface EntryOpenInteraction
                    """.trimIndent(),
            ),
        )

        val error = assertThrows(GradleException::class.java) { runBoundaryCheck() }

        error.message shouldContain "raw Entry interaction dispatch must live in provider SPI"
        error.message shouldContain "EntryOpenInteraction"
    }

    @Test
    fun `application consumers cannot bypass Library Progress Feature through domain port`() {
        createBaseFixture(
            appSource = """
                package app

                import tachiyomi.domain.entry.service.EntryLibraryProgressResolutionPort

                class AppFeature(private val progress: EntryLibraryProgressResolutionPort)
            """.trimIndent(),
        )

        val error = assertThrows(GradleException::class.java) { runBoundaryCheck() }

        error.message shouldContain "application consumers must use EntryLibraryProgressFeature"
    }

    @Test
    fun `application consumers cannot bypass Catalogue Feature through raw source description`() {
        createBaseFixture(
            appSource = """
                package app

                import eu.kanade.tachiyomi.source.entry.EntryCatalogueSource
                import eu.kanade.tachiyomi.source.sourceItemOrientation
                import tachiyomi.domain.source.service.EntrySourceDescriptionResolutionPort

                class AppFeature(
                    private val source: EntryCatalogueSource,
                    private val description: EntrySourceDescriptionResolutionPort,
                ) {
                    val orientation = source.sourceItemOrientation()
                }
            """.trimIndent(),
        )

        val error = assertThrows(GradleException::class.java) { runBoundaryCheck() }

        error.message shouldContain "application consumers must use EntryCatalogueFeature"
        error.message shouldContain "raw source contract"
        error.message shouldContain "EntryCatalogueSource"
        error.message shouldContain "sourceItemOrientation"
    }

    @Test
    fun `application consumers cannot bypass source action Features through raw contracts`() {
        createBaseFixture(
            appSource = """
                package app

                import eu.kanade.tachiyomi.source.entry.ConfigurableSource
                import eu.kanade.tachiyomi.source.entry.SourceHomePage
                import eu.kanade.tachiyomi.source.entry.WebViewSource
                import eu.kanade.tachiyomi.source.entry.ResolvableSource
                import eu.kanade.tachiyomi.source.entry.EntryPreviewSource
                import eu.kanade.tachiyomi.source.entry.RelatedEntriesSource
                import eu.kanade.tachiyomi.source.entry.EntryImageSource

                class AppFeature(private val source: Any) {
                    val immersive = source.supportsImmersiveFeed
                }
            """.trimIndent(),
        )

        val error = assertThrows(GradleException::class.java) { runBoundaryCheck() }

        error.message shouldContain "application source actions must use their Entry Feature boundary"
        error.message shouldContain "ConfigurableSource"
        error.message shouldContain "SourceHomePage"
        error.message shouldContain "WebViewSource"
        error.message shouldContain "ResolvableSource"
        error.message shouldContain "EntryPreviewSource"
        error.message shouldContain "RelatedEntriesSource"
        error.message shouldContain "EntryImageSource"
        error.message shouldContain "Immersive source opt-in must be interpreted by EntryImmersiveFeature"
    }

    @Test
    fun `root module cannot export the provider spi`() {
        createBaseFixture(
            additionalFiles = mapOf(
                "entry-interactions/build.gradle.kts" to
                    """
                        dependencies {
                            api(projects.entryInteractions.api)
                            api(projects.entryInteractions.spi)
                        }
                    """.trimIndent(),
            ),
        )

        val error = assertThrows(GradleException::class.java) { runBoundaryCheck() }

        error.message shouldContain "must not export the provider SPI"
    }

    @Test
    fun `generic code cannot import a type module package`() {
        createBaseFixture(
            appSource = """
                package app

                import mihon.entry.interactions.manga.MangaOpenProcessor

                class AppFeature
            """.trimIndent(),
        )

        val error = assertThrows(GradleException::class.java) { runBoundaryCheck() }

        error.message shouldContain "direct import across Entry interaction boundary"
        error.message shouldContain "mihon.entry.interactions.manga."
    }

    @Test
    fun `generic code cannot reference a concrete processor by simple name`() {
        createBaseFixture(
            appSource = """
                package app

                class AppFeature {
                    private val processorName = MangaOpenProcessor::class.simpleName
                }
            """.trimIndent(),
        )

        val error = assertThrows(GradleException::class.java) { runBoundaryCheck() }

        error.message shouldContain "direct concrete manga processor reference"
        error.message shouldContain "MangaOpenProcessor"
    }

    @Test
    fun `generic code cannot reference interaction spi contracts`() {
        createBaseFixture(
            appSource = """
                package app

                class AppFeature {
                    private val pluginName = EntryInteractionPlugin::class.simpleName
                    private val openName = EntryOpenInteraction::class.simpleName
                }
            """.trimIndent(),
        )

        val error = assertThrows(GradleException::class.java) { runBoundaryCheck() }

        error.message shouldContain "EntryInteractionPlugin is root/type-module Entry interaction internals"
        error.message shouldContain "EntryOpenInteraction is root/type-module Entry interaction internals"
    }

    @Test
    fun `type module processor implementations must remain internal`() {
        createBaseFixture(
            mangaProcessorSource = """
                package mihon.entry.interactions.manga

                import mihon.entry.interactions.EntryOpenProcessor

                class MangaOpenProcessor : EntryOpenProcessor
            """.trimIndent(),
        )

        val error = assertThrows(GradleException::class.java) { runBoundaryCheck() }

        error.message shouldContain "type-module processor must remain internal"
        error.message shouldContain "MangaOpenProcessor"
    }

    @Test
    fun `type module reader and player implementation classes must remain internal`() {
        createBaseFixture(
            additionalFiles = mapOf(
                "entry-interactions/manga/src/main/java/eu/kanade/tachiyomi/ui/reader/ReaderViewModel.kt" to
                    """
                        package eu.kanade.tachiyomi.ui.reader

                        class ReaderViewModel
                    """.trimIndent(),
                "entry-interactions/anime/src/main/java/eu/kanade/tachiyomi/ui/video/player/VideoPlaybackUiState.kt" to
                    """
                        package eu.kanade.tachiyomi.ui.video.player

                        data class VideoPlaybackUiState(val isPlaying: Boolean)
                    """.trimIndent(),
            ),
        )

        val error = assertThrows(GradleException::class.java) { runBoundaryCheck() }

        error.message shouldContain "unexpected public type-module class: ReaderViewModel"
        error.message shouldContain "unexpected public type-module class: VideoPlaybackUiState"
    }

    @Test
    fun `type module launch intent factories must remain internal`() {
        createBaseFixture(
            additionalFiles = mapOf(
                "entry-interactions/manga/src/main/java/mihon/entry/interactions/manga/MangaReaderLaunch.kt" to
                    """
                        package mihon.entry.interactions.manga

                        fun mangaReaderIntent() = Unit
                    """.trimIndent(),
            ),
        )

        val error = assertThrows(GradleException::class.java) { runBoundaryCheck() }

        error.message shouldContain "unexpected public type-module function: mangaReaderIntent"
    }

    @Test
    fun `framework instantiated type module components may remain public`() {
        createBaseFixture(
            additionalFiles = mapOf(
                "entry-interactions/manga/src/main/java/eu/kanade/tachiyomi/ui/reader/ReaderActivity.kt" to
                    """
                        package eu.kanade.tachiyomi.ui.reader

                        class ReaderActivity
                    """.trimIndent(),
                "entry-interactions/manga/src/main/java/mihon/entry/interactions/manga/download/DownloadJob.kt" to
                    """
                        package mihon.entry.interactions.manga.download

                        class DownloadJob
                    """.trimIndent(),
            ),
        )

        runBoundaryCheck()
    }

    @Test
    fun `generic code cannot reference runtime entry points directly`() {
        createBaseFixture(
            additionalFiles = mapOf(
                "entry-interactions/manga/src/main/java/eu/kanade/tachiyomi/ui/reader/ReaderActivity.kt" to
                    """
                        package eu.kanade.tachiyomi.ui.reader

                        class ReaderActivity
                    """.trimIndent(),
            ),
            appSource = """
                package app

                class AppFeature {
                    fun open() = ReaderActivity::class.simpleName
                }
            """.trimIndent(),
        )

        val error = assertThrows(GradleException::class.java) { runBoundaryCheck() }

        error.message shouldContain "direct ReaderActivity reference"
    }

    @Test
    fun `generic code cannot reference download runtime classes directly`() {
        createBaseFixture(
            additionalFiles = mapOf(
                "entry-interactions/manga/src/main/java/mihon/entry/interactions/manga/download/DownloadManager.kt" to
                    """
                        package mihon.entry.interactions.manga.download

                        class DownloadManager
                    """.trimIndent(),
            ),
            appSource = """
                package app

                class AppFeature {
                    private val downloadManager = DownloadManager::class.simpleName
                }
            """.trimIndent(),
        )

        val error = assertThrows(GradleException::class.java) { runBoundaryCheck() }

        error.message shouldContain "direct DownloadManager download runtime reference"
    }

    @Test
    fun `root composition cannot import concrete type module runtime classes`() {
        createBaseFixture(
            additionalFiles = mapOf(
                "entry-interactions/manga/src/main/java/mihon/entry/interactions/manga/" +
                    "MangaEntryInteractionRuntimeModule.kt" to
                    """
                        package mihon.entry.interactions.manga

                        import mihon.entry.interactions.EntryTypeRuntimeModule

                        fun mangaEntryTypeRuntimeModule(): EntryTypeRuntimeModule = EntryTypeRuntimeModule()
                    """.trimIndent(),
                "entry-interactions/manga/src/main/java/mihon/entry/interactions/manga/download/DownloadManager.kt" to
                    """
                        package mihon.entry.interactions.manga.download

                        class DownloadManager
                    """.trimIndent(),
                "entry-interactions/src/main/java/mihon/entry/interactions/runtime/EntryInteractionRuntime.kt" to
                    """
                        package mihon.entry.interactions

                        import mihon.entry.interactions.manga.mangaEntryTypeRuntimeModule
                        import mihon.entry.interactions.manga.download.DownloadManager

                        class EntryInteractionRuntime(
                            private val downloadManager: DownloadManager,
                        )
                    """.trimIndent(),
            ),
        )

        val error = assertThrows(GradleException::class.java) { runBoundaryCheck() }

        error.message shouldContain
            "root Entry interaction composition may import only the public manga runtime-module bridge"
        error.message shouldContain "mihon.entry.interactions.manga.download.DownloadManager"
    }

    @Test
    fun `root composition accepts a discovered type runtime module bridge`() {
        createBaseFixture(
            additionalFiles = mapOf(
                "entry-interactions/manga/src/main/java/mihon/entry/interactions/manga/" +
                    "MangaEntryInteractionRuntimeModule.kt" to
                    """
                        package mihon.entry.interactions.manga

                        import mihon.entry.interactions.EntryTypeRuntimeModule

                        fun mangaEntryTypeRuntimeModule(): EntryTypeRuntimeModule = EntryTypeRuntimeModule()
                    """.trimIndent(),
                "entry-interactions/src/main/java/mihon/entry/interactions/runtime/EntryInteractionRuntime.kt" to
                    """
                        package mihon.entry.interactions

                        import mihon.entry.interactions.manga.mangaEntryTypeRuntimeModule

                        class EntryInteractionRuntime
                    """.trimIndent(),
            ),
        )

        runBoundaryCheck()
    }

    @Test
    fun `type module public api parser ignores class literals in annotations`() {
        createBaseFixture(
            additionalFiles = mapOf(
                "entry-interactions/manga/src/main/java/mihon/entry/interactions/manga/download/DownloadCache.kt" to
                    """
                        package mihon.entry.interactions.manga.download

                        internal class DownloadCache

                        private class RootDirectory(
                            @Serializable(with = UniFileAsStringSerializer::class)
                            val dir: String?,
                        )

                        private object UniFileAsStringSerializer
                    """.trimIndent(),
            ),
        )

        runBoundaryCheck()
    }

    @Test
    fun `generic code cannot reference legacy manga source media resolution directly`() {
        createBaseFixture(
            appSource = """
                package app

                import eu.kanade.tachiyomi.source.model.Page

                class AppFeature {
                    suspend fun load(source: LegacyMangaSource, chapter: SChapter): List<Page> {
                        return source.getPageList(chapter)
                    }
                }
            """.trimIndent(),
        )

        val error = assertThrows(GradleException::class.java) { runBoundaryCheck() }

        error.message shouldContain "not legacy Page"
        error.message shouldContain "not legacy getPageList"
    }

    @Test
    fun `generic code cannot reference anime player resolver internals directly`() {
        createBaseFixture(
            additionalFiles = mapOf(
                "entry-interactions/anime/src/main/java/eu/kanade/tachiyomi/ui/video/player/VideoPlayerActivity.kt" to
                    """
                        package eu.kanade.tachiyomi.ui.video.player

                        class VideoPlayerActivity
                    """.trimIndent(),
                "entry-interactions/anime/src/main/java/eu/kanade/tachiyomi/ui/video/player/VideoStreamResolver.kt" to
                    """
                        package eu.kanade.tachiyomi.ui.video.player

                        internal interface VideoStreamResolver
                    """.trimIndent(),
            ),
            appSource = """
                package app

                class AppFeature {
                    private val resolverName = VideoStreamResolver::class.simpleName
                }
            """.trimIndent(),
        )

        val error = assertThrows(GradleException::class.java) { runBoundaryCheck() }

        error.message shouldContain "direct VideoStreamResolver runtime media resolution reference"
    }

    @Test
    fun `settings UI cannot reference concrete media cache implementations directly`() {
        createBaseFixture(
            additionalFiles = mapOf(
                "app/src/main/java/eu/kanade/presentation/more/settings/screen/SettingsDataScreen.kt" to
                    """
                        package eu.kanade.presentation.more.settings.screen

                        import eu.kanade.tachiyomi.data.cache.MangaPageCache

                        class SettingsDataScreen(
                            private val mangaPageCache: MangaPageCache,
                        )
                    """.trimIndent(),
            ),
        )

        val error = assertThrows(GradleException::class.java) { runBoundaryCheck() }

        error.message shouldContain "settings/UI cache maintenance must use EntryMediaCacheFeature"
        error.message shouldContain "MangaPageCache"
    }

    @Test
    fun `settings UI cannot bypass media cache feature through host ports`() {
        createBaseFixture(
            additionalFiles = mapOf(
                "app/src/main/java/eu/kanade/presentation/more/settings/screen/SettingsDataScreen.kt" to
                    """
                        package eu.kanade.presentation.more.settings.screen

                        import mihon.entry.interactions.EntryPageImageCache
                        import mihon.entry.interactions.EntryPlayerCache

                        class SettingsDataScreen(
                            private val pageCache: EntryPageImageCache,
                            private val playerCache: EntryPlayerCache,
                        )
                    """.trimIndent(),
            ),
        )

        val error = assertThrows(GradleException::class.java) { runBoundaryCheck() }

        error.message shouldContain "settings/UI cache maintenance must use EntryMediaCacheFeature"
        error.message shouldContain "EntryPageImageCache"
        error.message shouldContain "EntryPlayerCache"
    }

    @Test
    fun `type modules may reference media resolution and cache internals`() {
        createBaseFixture(
            additionalFiles = mapOf(
                "entry-interactions/manga/src/main/java/mihon/entry/interactions/manga/MangaPreviewSupport.kt" to
                    """
                        package mihon.entry.interactions.manga

                        import eu.kanade.tachiyomi.source.model.Page

                        internal class MangaPreviewSupport {
                            suspend fun load(source: LegacyMangaSource, chapter: SChapter): List<Page> {
                                return source.getPageList(chapter)
                            }
                        }
                    """.trimIndent(),
                "entry-interactions/anime/src/main/java/eu/kanade/tachiyomi/ui/video/player/VideoPlayerActivity.kt" to
                    """
                        package eu.kanade.tachiyomi.ui.video.player

                        class VideoPlayerActivity
                    """.trimIndent(),
                "entry-interactions/anime/src/main/java/eu/kanade/tachiyomi/ui/video/player/VideoStreamResolver.kt" to
                    """
                        package eu.kanade.tachiyomi.ui.video.player

                        internal interface VideoStreamResolver
                    """.trimIndent(),
                "entry-interactions/anime/src/main/java/eu/kanade/tachiyomi/ui/video/player/VideoPlayerMediaCache.kt" to
                    """
                        package eu.kanade.tachiyomi.ui.video.player

                        internal class VideoPlayerMediaCache
                    """.trimIndent(),
                "entry-interactions/anime/src/main/java/mihon/entry/interactions/anime/AnimePlayerSupport.kt" to
                    """
                        package mihon.entry.interactions.anime

                        import eu.kanade.tachiyomi.ui.video.player.VideoPlayerMediaCache
                        import eu.kanade.tachiyomi.ui.video.player.VideoStreamResolver

                        internal class AnimePlayerSupport(
                            private val resolver: VideoStreamResolver,
                            private val cache: VideoPlayerMediaCache,
                        )
                    """.trimIndent(),
            ),
        )

        runBoundaryCheck()
    }

    @Test
    fun `type modules cannot depend on root interaction module`() {
        createBaseFixture(
            mangaBuildGradle = """
                dependencies {
                    implementation(projects.entryInteractions)
                }
            """.trimIndent(),
        )

        val error = assertThrows(GradleException::class.java) { runBoundaryCheck() }

        error.message shouldContain "type interaction modules must depend on projects.entryInteractions.spi"
    }

    @Test
    fun `generic code cannot add exhaustive manga anime presentation mapping`() {
        createBaseFixture(
            appSource = """
                package app

                class AppFeature {
                    fun label(type: EntryType): String {
                        return when (type) {
                            EntryType.MANGA -> "Chapters"
                            EntryType.ANIME -> "Episodes"
                        }
                    }
                }
            """.trimIndent(),
        )

        val error = assertThrows(GradleException::class.java) { runBoundaryCheck() }

        error.message shouldContain "generic EntryType MANGA/ANIME mapping must use EntryTypePresentationFeature"
    }

    @Test
    fun `application presentation cannot own an exhaustive manga anime mapping`() {
        createBaseFixture(
            additionalFiles = mapOf(
                "app/src/main/java/eu/kanade/presentation/entry/EntryTypePresentation.kt" to
                    """
                        package eu.kanade.presentation.entry

                        class EntryTypePresentation

                        fun presentation(type: EntryType): EntryTypePresentation {
                            return when (type) {
                                EntryType.MANGA -> EntryTypePresentation()
                                EntryType.ANIME -> EntryTypePresentation()
                            }
                        }
                    """.trimIndent(),
            ),
        )

        val error = assertThrows(GradleException::class.java) { runBoundaryCheck() }

        error.message shouldContain "generic EntryType MANGA/ANIME mapping must use EntryTypePresentationFeature"
    }

    @Test
    fun `runtime preference owner cannot bypass installed ownership handle`() {
        createBaseFixture(
            additionalFiles = mapOf(
                "app/src/main/java/app/PreferenceModule.kt" to
                    """
                        package app

                        class PreferenceModule {
                            fun install() {
                                addSingletonFactory { NewPreferences(get<ProfileStore>().profileStore()) }
                            }
                        }
                    """.trimIndent(),
            ),
        )

        val error = assertThrows(GradleException::class.java) { runBoundaryCheck() }

        error.message shouldContain "runtime preference owners must be created from an installed handle"
    }

    private fun createBaseFixture(
        appBuildGradle: String = """
            dependencies {
                implementation(projects.entryInteractions)
            }
        """.trimIndent(),
        mangaBuildGradle: String = """
            dependencies {
                implementation(projects.entryInteractions.spi)
            }
        """.trimIndent(),
        mangaProcessorSource: String = """
            package mihon.entry.interactions.manga

            import mihon.entry.interactions.EntryOpenProcessor

            internal class MangaOpenProcessor : EntryOpenProcessor
        """.trimIndent(),
        appSource: String = """
            package app

            class AppFeature
        """.trimIndent(),
        additionalFiles: Map<String, String> = emptyMap(),
    ) {
        write(
            "entry-interactions/spi/src/main/java/mihon/entry/interactions/runtime/EntryInteractionPlugin.kt",
            """
                package mihon.entry.interactions

                interface EntryOpenProcessor

                interface EntryOpenInteraction

                interface EntryInteractionPlugin

                interface EntryInteractionRegistry

                fun createEntryInteractions() = Unit
            """.trimIndent(),
        )
        write(
            "entry-interactions/manga/build.gradle.kts",
            mangaBuildGradle,
        )
        write(
            "entry-interactions/manga/src/main/java/mihon/entry/interactions/manga/MangaOpenProcessor.kt",
            mangaProcessorSource,
        )
        write(
            "app/build.gradle.kts",
            appBuildGradle,
        )
        write(
            "app/src/main/java/app/AppFeature.kt",
            appSource,
        )
        additionalFiles.forEach { (path, content) ->
            write(path, content)
        }
    }

    private fun runBoundaryCheck() {
        val project = ProjectBuilder.builder().build()
        val task = project.tasks.register(
            "checkEntryInteractionBoundaries",
            EntryInteractionBoundaryCheckTask::class.java,
        ) {
            repositoryRoot.set(tempDir.toFile())
        }.get()

        task.action()
    }

    private fun write(relativePath: String, content: String) {
        val file = tempDir.resolve(relativePath)
        file.parent.createDirectories()
        file.writeText(content.trimIndent() + "\n")
    }
}
