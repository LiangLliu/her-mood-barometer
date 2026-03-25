package com.lianglliu.hermoodbarometer.core.domain

import com.lianglliu.hermoodbarometer.core.model.data.EmotionStatistics
import com.lianglliu.hermoodbarometer.core.model.data.TimeRange
import com.lianglliu.hermoodbarometer.repository.EmotionRepository
import jakarta.inject.Inject
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import kotlinx.coroutines.flow.Flow

/**
 * Use case for retrieving emotion statistics. Returns reactive Flows that auto-update when the
 * underlying data changes.
 */
class GetEmotionStatisticsUseCase
@Inject
constructor(private val emotionRepository: EmotionRepository) {

    /** Get statistics for a predefined time range */
    operator fun invoke(timeRange: TimeRange): Flow<EmotionStatistics> {
        val (startDate, endDate) = timeRange.toInstantBounds()
        return emotionRepository.getEmotionStatisticsFlow(startDate, endDate)
    }

    /** Get statistics for a custom date range */
    operator fun invoke(
        startDateTime: LocalDateTime,
        endDateTime: LocalDateTime,
    ): Flow<EmotionStatistics> {
        val startInstant = startDateTime.atZone(ZoneId.systemDefault()).toInstant()
        val endInstant = endDateTime.atZone(ZoneId.systemDefault()).toInstant()
        return emotionRepository.getEmotionStatisticsFlow(startInstant, endInstant)
    }

    /** Get statistics for a specific month */
    fun getMonthlyStatistics(year: Int, month: Int): Flow<EmotionStatistics> {
        val startOfMonth =
            LocalDateTime.of(year, month, 1, 0, 0).atZone(ZoneId.systemDefault()).toInstant()
        val startOfNextMonth = startOfMonth.atZone(ZoneId.systemDefault()).plusMonths(1).toInstant()
        return emotionRepository.getEmotionStatisticsFlow(
            startOfMonth,
            startOfNextMonth.minus(1, ChronoUnit.MILLIS),
        )
    }
}
