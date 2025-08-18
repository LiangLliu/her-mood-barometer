package com.lianglliu.hermoodbarometer.data.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.flow.take

/**
 * 接收闹钟广播并显示通知，同时安排下一天的提醒
 */
class ReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        val manager = NotificationManager(context)
        manager.showMoodReminderNotification()

        // 从 DataStore 读取时间并安排下一次
        try {
            val preferences =
                com.lianglliu.hermoodbarometer.data.preferences.PreferencesManager(context)
            val time = kotlinx.coroutines.runBlocking {
                preferences.reminderTime
                    .take(1)
                    .single()
            }
            // 复用仓库的精确闹钟逻辑
            com.lianglliu.hermoodbarometer.data.repository.NotificationRepositoryImpl(context)
                .scheduleDailyReminder(time.toString())
        } catch (_: Exception) { /* ignore */
        }
    }
}


