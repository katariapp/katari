package eu.kanade.tachiyomi.data.track

import eu.kanade.tachiyomi.data.track.hikka.Hikka
import eu.kanade.tachiyomi.data.track.mangabaka.MangaBaka
import eu.kanade.tachiyomi.source.entry.EntryType
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class TrackerProfileIntegrationTest {

    @Test
    fun `new trackers keep stable ids and manga-only legacy capabilities`() {
        val hikka = Hikka(TrackerManager.HIKKA)
        val mangaBaka = MangaBaka(TrackerManager.MANGABAKA)

        hikka.id shouldBe 10L
        mangaBaka.id shouldBe 11L
        hikka.supportedEntryTypes shouldBe setOf(EntryType.MANGA)
        mangaBaka.supportedEntryTypes shouldBe setOf(EntryType.MANGA)
        LegacyEntryTrackerAdapter(hikka).supportedEntryTypes shouldBe setOf(EntryType.MANGA)
        LegacyEntryTrackerAdapter(mangaBaka).supportedEntryTypes shouldBe setOf(EntryType.MANGA)
    }
}
