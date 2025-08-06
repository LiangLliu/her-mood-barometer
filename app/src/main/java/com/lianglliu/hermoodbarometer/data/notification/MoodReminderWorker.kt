package com.lianglliu.hermoodbarometer.data.notification

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import javax.inject.Inject

/**
 * 心情提醒工作器
 * 使用WorkManager处理定时通知任务
 */
class MoodReminderWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {
    
    private val notificationManager = NotificationManager(context)
    
    override suspend fun doWork(): Result {
        return try {
            // 显示通知
            notificationManager.showMoodReminderNotification()
            
            // 重新调度明天的提醒
            val reminderTime = inputData.getString("reminder_time") ?: "20:00"
            val time = java.time.LocalTime.parse(reminderTime)
            notificationManager.scheduleDailyReminder(time)
            
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
} 