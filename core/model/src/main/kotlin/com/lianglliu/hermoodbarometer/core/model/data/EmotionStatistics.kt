package com.lianglliu.hermoodbarometer.core.model.data

import java.time.Instant

/**
 * Emotion statistics domain model
 * Contains aggregated data about emotion records
 */
data class EmotionStatistics(
    val totalRecords: Int = 0,
    val emotionDistribution: List<EmotionCount> = emptyList(),
    val intensityDistribution: Map<EmotionIntensity, Int> = emptyMap(),
    val averageIntensity: Float = 0f,
    val mostFrequentEmotion: EmotionCount? = null,
    val timeRange: TimeRangeInfo? = null,
    val dailyRecords: List<DailyRecordCount> = emptyList()
)

/**
 * Count of specific emotion occurrences
 */
data class EmotionCount(
    val emotionId: Long,
    val emotionEmoji: String,
    val count: Int,
    val percentage: Float
)

/**
 * Time range information for statistics
 */
data class TimeRangeInfo(
    val startDate: Instant,
    val endDate: Instant,
    val totalDays: Int
) {
    /**
     * Calculate the average records per day
     */
    fun getAverageRecordsPerDay(totalRecords: Int): Float =
        if (totalDays > 0) totalRecords.toFloat() / totalDays else 0f
}


/**
 * Simplified emotion representation for statistics
 */
data class EmotionEmoji(
    val emotionId: Long,
    val emoji: String
)