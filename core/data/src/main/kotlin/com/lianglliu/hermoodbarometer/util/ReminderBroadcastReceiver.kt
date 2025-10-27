package com.lianglliu.hermoodbarometer.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.lianglliu.hermoodbarometer.core.network.di.ApplicationScope
import com.lianglliu.hermoodbarometer.core.notifications.Notifier
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import javax.inject.Inject

@AndroidEntryPoint
internal class ReminderBroadcastReceiver : BroadcastReceiver() {

    @Inject
    lateinit var reminderSchedulerImpl: ReminderSchedulerImpl

    @Inject
    lateinit var notifier: Notifier

    @Inject @ApplicationScope
    lateinit var appScope: CoroutineScope

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            ACTION_MOOD_REMINDER -> {
                // Post daily reminder notification
                appScope.launch {
                    withTimeoutOrNull(4500L) {
                        notifier.postDailyReminderNotification()
                        // Reschedule for next day since we're using setExact instead of setRepeating
                        reminderSchedulerImpl.scheduleDailyReminder()
                    }
                }
            }
            Intent.ACTION_BOOT_COMPLETED -> {
                // Reschedule reminders after device boot
                appScope.launch {
                    withTimeoutOrNull(4500L) {
                        rescheduleDailyReminders()
                    }
                }
            }
        }
    }

    private suspend fun rescheduleDailyReminders() {
        // Reschedule daily reminders after device restart
        reminderSchedulerImpl.scheduleDailyReminder()
    }

    companion object {
        const val ACTION_MOOD_REMINDER = "com.lianglliu.hermoodbarometer.ACTION_MOOD_REMINDER"
    }
}