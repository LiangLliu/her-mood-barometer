package com.lianglliu.hermoodbarometer.util

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.content.getSystemService
import com.lianglliu.hermoodbarometer.core.model.data.Reminder
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Calendar
import javax.inject.Inject

internal class ReminderSchedulerImpl @Inject constructor(
    @ApplicationContext private val context: Context,
) : ReminderScheduler {

    private val alarmManager: AlarmManager = checkNotNull(context.getSystemService())

    private fun createPendingIntent(reminderId: Int): PendingIntent {
        val intent = Intent(context, ReminderBroadcastReceiver::class.java).apply {
            putExtra(EXTRA_REMINDER_ID, reminderId)
        }

        return PendingIntent.getBroadcast(
            context,
            reminderId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )
    }

    override fun schedule(reminder: Reminder) {
        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            reminder.notificationDate?.toEpochMilliseconds() ?: 0L,
            reminder.repeatingInterval ?: 0,
            createPendingIntent(reminder.id ?: 0),
        )
    }

    override fun cancel(reminderId: Int) {
        alarmManager.cancel(
            createPendingIntent(reminderId)
        )
    }

    override fun scheduleDailyReminder() {
        val intent = Intent(context, ReminderBroadcastReceiver::class.java).apply {
            action = ReminderBroadcastReceiver.ACTION_MOOD_REMINDER
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            DAILY_REMINDER_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )

        // Get the preferred reminder time from settings (default to 8:00 PM)
        val calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, 20) // 8:00 PM
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)

            // If the time has already passed today, schedule for tomorrow
            if (timeInMillis <= System.currentTimeMillis()) {
                add(Calendar.DAY_OF_YEAR, 1)
            }
        }

        // Schedule repeating alarm
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }

    override fun cancelDailyReminder() {
        val intent = Intent(context, ReminderBroadcastReceiver::class.java).apply {
            action = ReminderBroadcastReceiver.ACTION_MOOD_REMINDER
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            DAILY_REMINDER_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )

        alarmManager.cancel(pendingIntent)
    }

    companion object {
        private const val DAILY_REMINDER_REQUEST_CODE = 999
    }
}

internal const val EXTRA_REMINDER_ID = "reminder-id"