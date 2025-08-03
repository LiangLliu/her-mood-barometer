package com.lianglliu.hermoodbarometer.domain.model

import kotlinx.serialization.Serializable
import java.time.LocalDateTime

/**
 * 时间范围枚举
 * 用于统计页面的时间筛选
 */
@Serializable
enum class TimeRange(val displayNameResId: String) {
    LAST_WEEK("last_week"),
    LAST_MONTH("last_month"),
    LAST_3_MONTHS("last_3_months"),
    LAST_YEAR("last_year");

    /**
     * 获取对应时间范围的开始时间
     */
    fun getStartDateTime(): LocalDateTime {
        val now = LocalDateTime.now()
        return when (this) {
            LAST_WEEK -> now.minusWeeks(1)
            LAST_MONTH -> now.minusMonths(1)
            LAST_3_MONTHS -> now.minusMonths(3)
            LAST_YEAR -> now.minusYears(1)
        }
    }
}