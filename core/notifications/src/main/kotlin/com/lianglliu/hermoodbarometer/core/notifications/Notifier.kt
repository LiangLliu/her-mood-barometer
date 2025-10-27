package com.lianglliu.hermoodbarometer.core.notifications

/**
 * Interface for creating notifications in the app
 */
interface Notifier {
    /**
     * Post a daily reminder notification to record mood
     */
    fun postDailyReminderNotification()
}