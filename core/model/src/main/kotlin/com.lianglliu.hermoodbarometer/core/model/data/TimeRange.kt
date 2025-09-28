package com.lianglliu.hermoodbarometer.core.model.data

import java.time.LocalDateTime

/**
 * 时间范围枚举
 * 用于统计页面的时间筛选
 */
enum class TimeRange {
    LAST_WEEK,
    LAST_MONTH,
    LAST_3_MONTHS,
    LAST_SIX_MONTHS,
    LAST_YEAR,
    CUSTOM;

    /**
     * 获取对应时间范围的开始时间
     */
    fun getStartDateTime(): LocalDateTime {
        val now = LocalDateTime.now()
        return when (this) {
            LAST_WEEK -> now.minusWeeks(1)
            LAST_MONTH -> now.minusMonths(1)
            LAST_3_MONTHS -> now.minusMonths(3)
            LAST_SIX_MONTHS -> now.minusMonths(6)
            LAST_YEAR -> now.minusYears(1)
            CUSTOM -> now.minusMonths(1) // 默认显示最近一个月
        }
    }
    
    /**
     * 获取对应时间范围的结束时间
     */
    fun getEndDateTime(): LocalDateTime {
        return LocalDateTime.now()
    }
}