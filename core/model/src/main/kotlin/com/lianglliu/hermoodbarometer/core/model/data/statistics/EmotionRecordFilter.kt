package com.lianglliu.hermoodbarometer.core.model.data.statistics

import java.time.LocalDateTime

data class EmotionRecordFilter(
    val startDateTime: LocalDateTime,
    val endDateTime: LocalDateTime
)
