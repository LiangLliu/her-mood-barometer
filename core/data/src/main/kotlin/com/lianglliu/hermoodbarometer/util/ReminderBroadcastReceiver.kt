package com.lianglliu.hermoodbarometer.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.lianglliu.hermoodbarometer.core.common.concurrency.di.ApplicationScope
import com.lianglliu.hermoodbarometer.core.notifications.Notifier
import com.lianglliu.hermoodbarometer.repository.UserDataRepository
import dagger.hilt.android.AndroidEntryPoint
import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import timber.log.Timber

@AndroidEntryPoint
internal class ReminderBroadcastReceiver : BroadcastReceiver() {

    @Inject lateinit var reminderSchedulerImpl: ReminderSchedulerImpl

    @Inject lateinit var notifier: Notifier

    @Inject lateinit var userDataRepository: UserDataRepository

    @Inject @ApplicationScope lateinit var appScope: CoroutineScope

    override fun onReceive(context: Context, intent: Intent) {
        // goAsync() tells the system this receiver is still doing work after onReceive() returns.
        // Without it, the system may kill the process before the coroutine completes.
        val pendingResult = goAsync()

        when (intent.action) {
            ACTION_MOOD_REMINDER -> {
                Timber.d("Mood reminder alarm fired")
                appScope.launch {
                    try {
                        withTimeoutOrNull(9000L) {
                            notifier.postDailyReminderNotification()
                            Timber.d("Notification posted, rescheduling for next day")
                            rescheduleDailyReminders()
                        }
                    } finally {
                        pendingResult.finish()
                    }
                }
            }
            Intent.ACTION_BOOT_COMPLETED -> {
                Timber.d("Boot completed, rescheduling reminders")
                appScope.launch {
                    try {
                        withTimeoutOrNull(9000L) { rescheduleDailyReminders() }
                    } finally {
                        pendingResult.finish()
                    }
                }
            }
            else -> pendingResult.finish()
        }
    }

    private suspend fun rescheduleDailyReminders() {
        val userData = userDataRepository.userData.first()
        if (!userData.reminderStatus) return

        val timeParts = userData.reminderTime.ifEmpty { "09:00" }.split(":")
        val hour = timeParts.getOrNull(0)?.toIntOrNull() ?: 9
        val minute = timeParts.getOrNull(1)?.toIntOrNull() ?: 0
        reminderSchedulerImpl.scheduleDailyReminder(hour, minute)
    }

    companion object {
        const val ACTION_MOOD_REMINDER = "com.lianglliu.hermoodbarometer.ACTION_MOOD_REMINDER"
    }
}
