package com.lianglliu.hermoodbarometer.core.model.data

import kotlin.time.Instant

data class Reminder(
    val id: Int? = null,
    val notificationDate: Instant? = null,
    val repeatingInterval: Long? = null,
)