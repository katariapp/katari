package mihon.entry.interactions.manga.download

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import mihon.entry.interactions.EntryDownloadWorkController
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get

/** Hands persisted pre-unification Manga work to the shared download runtime. */
class DownloadJob(context: Context, workerParams: WorkerParameters) : CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result {
        Injekt.get<EntryDownloadWorkController>().start()
        return Result.success()
    }
}
