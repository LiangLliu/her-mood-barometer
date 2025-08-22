package com.lianglliu.hermoodbarometer.domain.usecase

import app.cash.turbine.test
import com.lianglliu.hermoodbarometer.domain.model.EmotionRecord
import com.lianglliu.hermoodbarometer.domain.model.TimeRange
import com.lianglliu.hermoodbarometer.domain.repository.EmotionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate
import java.time.LocalDateTime

class GetEmotionStatisticsUseCaseTest {

    @Test
    fun calculateStatistics_countsDistributionDailyAverage_ok() = runTest {
        val now = LocalDateTime.now()
        val records = listOf(
            // HAPPY: 3
            EmotionRecord(id = 1, emotionType = "HAPPY", intensity = 4, note = "", timestamp = now.minusDays(2)),
            EmotionRecord(id = 2, emotionType = "HAPPY", intensity = 3, note = "", timestamp = now.minusDays(1)),
            EmotionRecord(id = 3, emotionType = "HAPPY", intensity = 5, note = "", timestamp = now),
            // SAD: 2
            EmotionRecord(id = 4, emotionType = "SAD", intensity = 2, note = "", timestamp = now.minusDays(2)),
            EmotionRecord(id = 5, emotionType = "SAD", intensity = 3, note = "", timestamp = now.minusDays(1)),
            // CUSTOM: 1 (named "Grumpy")
            EmotionRecord(id = 6, emotionType = "CUSTOM", intensity = 4, note = "", timestamp = now.minusDays(1), customEmotionId = 10L, isCustomEmotion = true, customEmotionName = "Grumpy")
        )

        val repo = object : EmotionRepository {
            override fun getAllRecords(): Flow<List<EmotionRecord>> = flowOf(records)
            override fun getRecordsByTimeRange(timeRange: TimeRange): Flow<List<EmotionRecord>> = flowOf(records)
            override fun getRecordsByDateRange(startDate: LocalDateTime, endDate: LocalDateTime): Flow<List<EmotionRecord>> = flowOf(records)
            override suspend fun getRecordById(id: Long): EmotionRecord? = records.find { it.id == id }
            override suspend fun insertRecord(record: EmotionRecord): Long = 0
            override suspend fun updateRecord(record: EmotionRecord) {}
            override suspend fun deleteRecord(id: Long) {}
            override suspend fun deleteAllRecords() {}
            override fun getRecordCount(): Flow<Int> = flowOf(records.size)
            override fun getRecentRecords(limit: Int): Flow<List<EmotionRecord>> = flowOf(records.take(limit))
            override fun getEmotionRecordsByType(emotionType: String): Flow<List<EmotionRecord>> = flowOf(records.filter { it.emotionType == emotionType })
        }

        val useCase = GetEmotionStatisticsUseCase(repo)

        useCase(TimeRange.LAST_MONTH).test {
            val stats = awaitItem()

            // totals
            assertEquals(6, stats.totalRecords)

            // counts by emotion (custom name should be used instead of "CUSTOM")
            assertEquals(3, stats.countsByEmotion["HAPPY"])
            assertEquals(2, stats.countsByEmotion["SAD"])
            assertEquals(1, stats.countsByEmotion["Grumpy"])

            // distribution sum ~ 1.0
            val sum = stats.emotionDistribution.values.sum()
            assertEquals(1.0f, sum, 0.0001f)

            // daily average intensity has entries for dates present
            val dates: Set<LocalDate> = stats.dailyAverageIntensity.map { it.date }.toSet()
            // we used: now, now-1d, now-2d
            assertEquals(3, dates.size)

            cancelAndIgnoreRemainingEvents()
        }
    }
}


