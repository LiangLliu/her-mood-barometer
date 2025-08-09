package com.lianglliu.hermoodbarometer.domain.repository

/**
 * 通知调度仓库
 * 负责与平台层的通知调度交互（调度/取消每日提醒）
 */
interface NotificationRepository {
    /** 调度每日提醒（24h 周期），time 格式 HH:mm */
    fun scheduleDailyReminder(time: String)

    /** 取消每日提醒 */
    fun cancelDailyReminder()
}


