package com.lianglliu.hermoodbarometer.startup

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.startup.Initializer
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.flow.take
import com.lianglliu.hermoodbarometer.R

/**
 * 使用 AndroidX App Startup 在应用进程启动时创建通知渠道。
 * 注意：创建渠道是幂等操作，重复创建不会产生副作用。
 */
class NotificationInitializer : Initializer<Unit> {

    override fun create(context: Context) {
        val systemNotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channelId = "mood_reminder_channel"
        val channelName = context.getString(R.string.notification_title)
        val channelDescription = context.getString(R.string.daily_reminder_description)

        val channel = NotificationChannel(
            channelId,
            channelName,
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = channelDescription
            enableLights(true)
            enableVibration(true)
        }

        systemNotificationManager.createNotificationChannel(channel)

        // 根据偏好恢复提醒调度（轻量 IO，同步一次）
        try {
            val preferences = com.lianglliu.hermoodbarometer.data.preferences.PreferencesManager(context)
            val isEnabled = kotlinx.coroutines.runBlocking {
                preferences.isReminderEnabled
                    .take(1)
                    .single()
            }
            if (isEnabled) {
                val time = kotlinx.coroutines.runBlocking {
                    preferences.reminderTime
                        .take(1)
                        .single()
                        .toString()
                }
                val repo = com.lianglliu.hermoodbarometer.data.repository.NotificationRepositoryImpl(context)
                repo.scheduleDailyReminder(time)
            }
        } catch (_: Exception) { /* ignore */ }
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()
}


