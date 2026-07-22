package eu.kanade.tachiyomi.ui.entry.track

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.readText

class TrackSelectionActionBoundaryTest {

    @Test
    fun `tracker selections are captured before non-cancellable dispatch`() {
        val sources = listOf(
            "TrackScoreSelectorScreen.kt",
            "TrackProgressSelectorScreen.kt",
            "TrackStatusSelectorScreen.kt",
        ).map { fileName ->
            repositoryRoot()
                .resolve("app/src/main/java/eu/kanade/tachiyomi/ui/entry/track/$fileName")
                .readText()
        }

        sources.forEach { source ->
            Regex(
                """val selection = state\.value\.selection\s+screenModelScope\.launchNonCancellable \{""",
            ).findAll(source).count() shouldBe 1
            assertFalse(
                Regex("""EntryTrackingMutation\.(Score|Progress|Status)\(state\.value\.selection\)""")
                    .containsMatchIn(source),
            )
        }
    }

    private fun repositoryRoot(): Path {
        return generateSequence(Path.of("").toAbsolutePath()) { it.parent }
            .first { Files.exists(it.resolve("settings.gradle.kts")) }
    }
}
