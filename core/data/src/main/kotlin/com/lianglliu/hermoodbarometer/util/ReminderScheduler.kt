package com.lianglliu.hermoodbarometer.util

import com.lianglliu.hermoodbarometer.core.model.data.Reminder

interface ReminderScheduler {

    fun schedule(reminder: Reminder)

    fun cancel(reminderId: Int)

    /**
     * Schedule daily mood reminder notification
     */
    fun scheduleDailyReminder()

    /**
     * Schedule daily mood reminder notification at specific time
     * @param hour Hour in 24-hour format (0-23)
     * @param minute Minute (0-59)
     */
    fun scheduleDailyReminder(hour: Int, minute: Int)

    /**
     * Cancel daily mood reminder notification
     */
    fun cancelDailyReminder()
}