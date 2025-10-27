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
     * Cancel daily mood reminder notification
     */
    fun cancelDailyReminder()
}