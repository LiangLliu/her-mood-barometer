package com.lianglliu.hermoodbarometer.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.tracing.traceAsync
import androidx.work.CoroutineWorker
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkerParameters
import com.lianglliu.hermoodbarometer.core.common.concurrency.AppDispatchers
import com.lianglliu.hermoodbarometer.core.common.concurrency.Dispatcher
import com.lianglliu.hermoodbarometer.initializer.SyncConstraints
import com.lianglliu.hermoodbarometer.repository.UserDataRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

@HiltWorker
internal class DeleteOutdatedRatesWorker @AssistedInject constructor(
    @Assisted private val appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val userDataRepository: UserDataRepository,
    @Dispatcher(AppDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result = withContext(ioDispatcher) {
        traceAsync("DeleteOutdatedRates", 0) {
            Result.success()
        }
    }

    companion object {

        fun periodicDeleteOutdatedRatesWork() =
            PeriodicWorkRequestBuilder<DelegatingWorker>(
                repeatInterval = 1,
                repeatIntervalTimeUnit = TimeUnit.DAYS,
            )
                .setConstraints(SyncConstraints)
                .setInputData(DeleteOutdatedRatesWorker::class.delegatedData())
                .build()
    }
}