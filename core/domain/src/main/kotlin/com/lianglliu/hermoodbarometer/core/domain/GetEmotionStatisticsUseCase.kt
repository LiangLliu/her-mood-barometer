package com.lianglliu.hermoodbarometer.core.domain

import com.lianglliu.hermoodbarometer.core.model.data.EmotionStatistics
import com.lianglliu.hermoodbarometer.core.model.data.TimeRange
import com.lianglliu.hermoodbarometer.repository.EmotionRepository
import jakarta.inject.Inject
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Use case for retrieving emotion statistics Calculates and provides various statistical insights
 */
class GetEmotionStatisticsUseCase
@Inject
constructor(private val emotionRepository: EmotionRepository) {

    /**
     * Get statistics for a predefined time range
     *
     * @param timeRange Time range for statistics
     * @return Flow of emotion statistics
     */
    operator fun invoke(timeRange: TimeRange): Flow<EmotionStatistics> = flow {
        val (startDate, endDate) = timeRange.toInstantBounds()
        val statistics = emotionRepository.getEmotionStatistics(startDate, endDate)
        emit(statistics)
    }

    /**
     * Get statistics for a custom date range
     *
     * @param startDateTime Start date and time
     * @param endDateTime End date and time
     * @return Flow of emotion statistics
     */
    operator fun invoke(
        startDateTime: LocalDateTime,
        endDateTime: LocalDateTime,
    ): Flow<EmotionStatistics> = flow {
        val startInstant = startDateTime.atZone(ZoneId.systemDefault()).toInstant()
        val endInstant = endDateTime.atZone(ZoneId.systemDefault()).toInstant()
        val statistics = emotionRepository.getEmotionStatistics(startInstant, endInstant)
        emit(statistics)
    }

    /**
     * Get statistics for a specific month
     *
     * @param year Year
     * @param month Month (1-12)
     * @return Flow of emotion statistics
     */
    fun getMonthlyStatistics(year: Int, month: Int): Flow<EmotionStatistics> = flow {
        val startOfMonth =
            LocalDateTime.of(year, month, 1, 0, 0).atZone(ZoneId.systemDefault()).toInstant()

        val startOfNextMonth = startOfMonth.atZone(ZoneId.systemDefault()).plusMonths(1).toInstant()

        val statistics =
            emotionRepository.getEmotionStatistics(
                startOfMonth,
                startOfNextMonth.minus(1, ChronoUnit.MILLIS),
            )
        emit(statistics)
    }
}
