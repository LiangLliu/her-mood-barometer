package com.lianglliu.hermoodbarometer.core.model.data

import java.time.LocalDate

/**
 * Daily record count for statistics
 */
data class DailyRecordCount(
    val date: LocalDate,
    val count: Int
)