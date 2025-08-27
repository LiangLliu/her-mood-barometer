package com.lianglliu.hermoodbarometer.domain.usecase

import app.cash.turbine.test
import com.lianglliu.hermoodbarometer.domain.model.EmotionRecord
import com.lianglliu.hermoodbarometer.domain.model.TimeRange
import com.lianglliu.hermoodbarometer.domain.repository.EmotionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate
import java.time.LocalDateTime

class GetEmotionStatisticsUseCaseTest {

    @Test
    fun calculateStatistics_countsDistributionDailyAverage_ok() = runTest {
        val now = LocalDateTime.now()
        val records = listOf(
            // HAPPY: 3
            EmotionRecord(id = 1, emotionId = 1L, emotionName = "开心", emotionEmoji = "😊", intensity = 4, note = "", timestamp = now.minusDays(2)),
            EmotionRecord(id = 2, emotionId = 1L, emotionName = "开心", emotionEmoji = "😊", intensity = 3, note = "", timestamp = now.minusDays(1)),
            EmotionRecord(id = 3, emotionId = 1L, emotionName = "开心", emotionEmoji = "😊", intensity = 5, note = "", timestamp = now),
            // SAD: 2
            EmotionRecord(id = 4, emotionId = 2L, emotionName = "难过", emotionEmoji = "😢", intensity = 2, note = "", timestamp = now.minusDays(2)),
            EmotionRecord(id = 5, emotionId = 2L, emotionName = "难过", emotionEmoji = "😢", intensity = 3, note = "", timestamp = now.minusDays(1)),
            // CUSTOM: 1 (named "Grumpy")
            EmotionRecord(id = 6, emotionId = 10L, emotionName = "生气", emotionEmoji = "😠", intensity = 4, note = "", timestamp = now.minusDays(1))
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
            override fun getEmotionRecordsByType(emotionType: String): Flow<List<EmotionRecord>> = flowOf(records.filter { it.emotionId.toString() == emotionType })
        }

        val useCase = GetEmotionStatisticsUseCase(repo)

        useCase(TimeRange.LAST_MONTH).test {
            val stats = awaitItem()

            // totals
            assertEquals(6, stats.totalRecords)

            // counts by emotion (使用表情符号+名称格式)
            assertEquals(3, stats.countsByEmotion["😊 开心"])
            assertEquals(2, stats.countsByEmotion["😢 难过"])
            assertEquals(1, stats.countsByEmotion["😠 生气"])

            // distribution sum ~ 1.0
            val sum = stats.emotionDistribution.values.sum()
            assertEquals(1.0f, sum, 0.0001f)

            // daily emotion trend has entries for dates present
            val dates: Set<LocalDate> = stats.dailyEmotionTrend.map { it.date }.toSet()
            // we used: now, now-1d, now-2d
            assertEquals(3, dates.size)
            
            // verify emotion trend data
            val trendEmotions = stats.dailyEmotionTrend.map { it.emotionEmoji }
            assertTrue("Should contain happy emotion", trendEmotions.contains("😊")) // happy
            assertTrue("Should contain angry emotion", trendEmotions.contains("😠")) // angry (custom)
            // 注意：难过可能不会出现在趋势中，因为每天都有强度更高的情绪

            cancelAndIgnoreRemainingEvents()
        }
    }
}


