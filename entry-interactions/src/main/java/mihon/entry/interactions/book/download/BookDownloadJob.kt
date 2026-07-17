package mihon.entry.interactions.book.download

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import mihon.entry.interactions.EntryDownloadWorkController
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get

/** Hands persisted pre-unification BOOK work to the shared download runtime. */
internal class BookDownloadJob(context: Context, workerParams: WorkerParameters) : CoroutineWorker(
    context,
    workerParams,
) {
    override suspend fun doWork(): Result {
        Injekt.get<EntryDownloadWorkController>().start()
        return Result.success()
    }
}
