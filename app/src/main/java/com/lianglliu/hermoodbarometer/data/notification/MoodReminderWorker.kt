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
            
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
} 