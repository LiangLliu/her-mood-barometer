package com.lianglliu.hermoodbarometer.util

import com.lianglliu.hermoodbarometer.core.model.data.Reminder

interface ReminderScheduler {

    fun schedule(reminder: Reminder)

    fun cancel(reminderId: Int)
}