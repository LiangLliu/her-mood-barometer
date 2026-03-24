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

@AndroidEntryPoint
internal class ReminderBroadcastReceiver : BroadcastReceiver() {

    @Inject lateinit var reminderSchedulerImpl: ReminderSchedulerImpl

    @Inject lateinit var notifier: Notifier

    @Inject lateinit var userDataRepository: UserDataRepository

    @Inject @ApplicationScope lateinit var appScope: CoroutineScope

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            ACTION_MOOD_REMINDER -> {
                // Post daily reminder notification
                appScope.launch {
                    withTimeoutOrNull(4500L) {
                        notifier.postDailyReminderNotification()
                        // Reschedule for next day since we're using setExact instead of
                        // setRepeating
                        rescheduleDailyReminders()
                    }
                }
            }
            Intent.ACTION_BOOT_COMPLETED -> {
                // Reschedule reminders after device boot
                appScope.launch { withTimeoutOrNull(4500L) { rescheduleDailyReminders() } }
            }
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
