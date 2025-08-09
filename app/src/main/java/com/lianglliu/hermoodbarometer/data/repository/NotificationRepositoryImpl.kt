package com.lianglliu.hermoodbarometer.data.repository

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.lianglliu.hermoodbarometer.data.notification.ReminderReceiver
import com.lianglliu.hermoodbarometer.domain.repository.NotificationRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 通知调度仓库实现
 * 使用 WorkManager 周期任务调度每日提醒（24h）。
 */
@Singleton
class NotificationRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
) : NotificationRepository {

    override fun scheduleDailyReminder(time: String) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val triggerAtMillis = computeNextTriggerUtcMillis(time)
        val pi = createPendingIntent()
        alarmManager.cancel(pi)
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAtMillis, pi)
    }

    override fun cancelDailyReminder() {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(createPendingIntent())
    }

    private fun createPendingIntent(): PendingIntent {
        val intent = Intent(context, ReminderReceiver::class.java)
        return PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    companion object {
        fun computeNextTriggerUtcMillis(
            hhmm: String,
            zoneId: ZoneId = ZoneId.systemDefault()
        ): Long {
            val target = LocalTime.parse(hhmm)
            val today = LocalDate.now(zoneId)
            val now = LocalDateTime.now(zoneId)
            var candidate = LocalDateTime.of(today, target)
            if (!candidate.isAfter(now)) candidate = candidate.plusDays(1)
            return candidate.atZone(zoneId).toInstant().toEpochMilli()
        }
    }
}


