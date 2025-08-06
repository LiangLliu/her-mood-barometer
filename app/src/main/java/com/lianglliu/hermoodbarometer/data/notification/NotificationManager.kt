package com.lianglliu.hermoodbarometer.data.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.lianglliu.hermoodbarometer.MainActivity
import com.lianglliu.hermoodbarometer.R
import java.time.LocalTime
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 通知管理器
 * 负责处理应用的通知功能
 */
@Singleton
class NotificationManager @Inject constructor(
    private val context: Context
) {
    
    private val systemNotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as android.app.NotificationManager
    
    companion object {
        const val CHANNEL_ID_MOOD_REMINDER = "mood_reminder_channel"
        const val NOTIFICATION_ID_MOOD_REMINDER = 1001
        const val WORK_TAG_MOOD_REMINDER = "mood_reminder_work"

        /**
         * 创建通知渠道
         */
        private fun createNotificationChannels(notificationManager: com.lianglliu.hermoodbarometer.data.notification.NotificationManager) {
            val moodReminderChannel = android.app.NotificationChannel(
                CHANNEL_ID_MOOD_REMINDER,
                notificationManager.context.getString(R.string.notification_title),
                android.app.NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description =
                    notificationManager.context.getString(R.string.daily_reminder_description)
                enableLights(true)
                enableVibration(true)
            }

            notificationManager.systemNotificationManager.createNotificationChannel(
                moodReminderChannel
            )
        }
    }
    
    init {
        createNotificationChannels(this)
    }

    /**
     * 设置每日提醒
     * @param time 提醒时间
     */
    fun scheduleDailyReminder(time: LocalTime) {
        // 取消现有的提醒
        cancelDailyReminder()
        
        // 计算延迟时间
        val now = LocalTime.now()
        var delayMillis = time.toSecondOfDay() * 1000L - now.toSecondOfDay() * 1000L
        
        // 如果今天的时间已过，设置为明天
        if (delayMillis <= 0) {
            delayMillis += 24 * 60 * 60 * 1000L
        }
        
        // 创建WorkRequest
        val workData = Data.Builder()
            .putString("notification_title", context.getString(R.string.notification_title))
            .putString("notification_text", context.getString(R.string.notification_text))
            .build()
        
        val reminderWork = OneTimeWorkRequestBuilder<MoodReminderWorker>()
            .setInitialDelay(delayMillis, TimeUnit.MILLISECONDS)
            .setInputData(workData)
            .addTag(WORK_TAG_MOOD_REMINDER)
            .build()
        
        WorkManager.getInstance(context).enqueue(reminderWork)
    }
    
    /**
     * 取消每日提醒
     */
    fun cancelDailyReminder() {
        WorkManager.getInstance(context).cancelAllWorkByTag(WORK_TAG_MOOD_REMINDER)
    }
    
    /**
     * 显示心情提醒通知
     */
    fun showMoodReminderNotification() {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        val notification = NotificationCompat.Builder(context, CHANNEL_ID_MOOD_REMINDER)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(context.getString(R.string.notification_title))
            .setContentText(context.getString(R.string.notification_text))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()
        
        systemNotificationManager.notify(NOTIFICATION_ID_MOOD_REMINDER, notification)
    }
} 