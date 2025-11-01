package com.lianglliu.hermoodbarometer.repository.offline

import com.lianglliu.hermoodbarometer.core.model.data.DailyRecordCount
import com.lianglliu.hermoodbarometer.core.model.data.EmotionCount
import com.lianglliu.hermoodbarometer.core.model.data.EmotionEmoji
import com.lianglliu.hermoodbarometer.core.model.data.EmotionIntensity
import com.lianglliu.hermoodbarometer.core.model.data.EmotionRecord
import com.lianglliu.hermoodbarometer.core.model.data.EmotionStatistics
import com.lianglliu.hermoodbarometer.core.model.data.TimeRange
import com.lianglliu.hermoodbarometer.core.model.data.TimeRangeInfo
import com.lianglliu.hermoodbarometer.dao.EmotionRecordDao
import com.lianglliu.hermoodbarometer.model.EmotionRecordEntity
import com.lianglliu.hermoodbarometer.repository.EmotionRepository
import com.lianglliu.hermoodbarometer.repository.mapper.toDomainModel
import com.lianglliu.hermoodbarometer.repository.mapper.toDomainModels
import com.lianglliu.hermoodbarometer.repository.mapper.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of EmotionRepository using Room database
 * Handles emotion record persistence and retrieval
 */
@Singleton
class EmotionRecordRepositoryImpl @Inject constructor(
    private val emotionRecordDao: EmotionRecordDao
) : EmotionRepository {

    override fun getAllRecords(): Flow<List<EmotionRecord>> {
        return emotionRecordDao.getAllRecords()
            .map { entities -> entities.toDomainModels() }
    }

    override fun getRecordsByTimeRange(timeRange: TimeRange): Flow<List<EmotionRecord>> {
        val (startTime, endTime) = getTimeRangeBounds(timeRange)
        return emotionRecordDao.getRecordsByTimeRange(startTime, endTime)
            .map { entities -> entities.toDomainModels() }
    }

    override fun getRecordsByDateRange(
        startDate: Instant,
        endDate: Instant
    ): Flow<List<EmotionRecord>> {
        return emotionRecordDao.getRecordsByTimeRange(startDate, endDate)
            .map { entities -> entities.toDomainModels() }
    }

    override suspend fun getRecordById(id: Long): EmotionRecord? {
        return emotionRecordDao.getRecordById(id)?.toDomainModel()
    }

    override suspend fun insertRecord(record: EmotionRecord): Long {
        return emotionRecordDao.insertRecord(record.toEntity())
    }

    override suspend fun updateRecord(record: EmotionRecord) {
        emotionRecordDao.updateRecord(record.toEntity())
    }

    override suspend fun deleteRecord(id: Long) {
        emotionRecordDao.deleteRecordById(id)
    }

    override suspend fun deleteAllRecords() {
        emotionRecordDao.deleteAllRecords()
    }

    override fun getRecordCount(): Flow<Int> {
        return emotionRecordDao.getRecordCount()
    }

    override fun getRecentRecords(limit: Int): Flow<List<EmotionRecord>> {
        return emotionRecordDao.getRecentRecords(limit)
            .map { entities -> entities.toDomainModels() }
    }

    override fun getRecordsByEmotionId(emotionId: Long): Flow<List<EmotionRecord>> {
        return emotionRecordDao.getRecordsByEmotionId(emotionId)
            .map { entities -> entities.toDomainModels() }
    }

    override fun getRecordsByMonth(year: Int, month: Int): Flow<List<EmotionRecord>> {
        val startOfMonth = LocalDateTime.of(year, month, 1, 0, 0)
            .atZone(ZoneId.systemDefault())
            .toInstant()

        val startOfNextMonth = startOfMonth
            .atZone(ZoneId.systemDefault())
            .plusMonths(1)
            .toInstant()

        return emotionRecordDao.getRecordsByMonth(startOfMonth, startOfNextMonth)
            .map { entities -> entities.toDomainModels() }
    }

    override fun searchRecordsByNote(query: String): Flow<List<EmotionRecord>> {
        return emotionRecordDao.searchRecordsByNote(query)
            .map { entities -> entities.toDomainModels() }
    }

    override suspend fun getEmotionStatistics(
        startDate: Instant,
        endDate: Instant
    ): EmotionStatistics {
        // Get emotion distribution
        val emotionStats = emotionRecordDao.getEmotionStatistics(startDate, endDate).first()
        val totalRecords = emotionStats.sumOf { it.count }

        // Convert to EmotionCount objects
        val emotionDistribution = emotionStats.map { stats ->
            EmotionCount(
                emotionId = stats.emotionId,
                emotionEmoji = stats.emotionEmoji,
                count = stats.count,
                percentage = if (totalRecords > 0) {
                    (stats.count.toFloat() / totalRecords) * 100f
                } else 0f
            )
        }

        // Calculate intensity distribution
        val intensityMap = mutableMapOf<EmotionIntensity, Int>()
        EmotionIntensity.values().forEach { intensity ->
            val count = emotionRecordDao.getRecordsByIntensity(intensity.level).first().size
            if (count > 0) intensityMap[intensity] = count
        }

        // Calculate average intensity
        val averageIntensity = if (totalRecords > 0) {
            emotionStats.map { it.avgIntensity * it.count }.sum() / totalRecords
        } else 0f

        // Get most frequent emotion
        val mostFrequent = emotionDistribution.maxByOrNull { it.count }

        // Calculate time range info
        val daysBetween = ChronoUnit.DAYS.between(
            startDate.atZone(ZoneId.systemDefault()).toLocalDate(),
            endDate.atZone(ZoneId.systemDefault()).toLocalDate()
        ).toInt() + 1

        val timeRangeInfo = TimeRangeInfo(
            startDate = startDate,
            endDate = endDate,
            totalDays = daysBetween
        )

        // Get daily record counts
        val dailyRecords = emotionRecordDao.getRecordsForDailyStats(startDate, endDate)
        val dailyRecordCounts = groupRecordsByDate(dailyRecords)

        return EmotionStatistics(
            totalRecords = totalRecords,
            emotionDistribution = emotionDistribution,
            intensityDistribution = intensityMap,
            averageIntensity = averageIntensity,
            mostFrequentEmotion = mostFrequent,
            timeRange = timeRangeInfo,
            dailyRecords = dailyRecordCounts
        )
    }

    override suspend fun hasRecordsForEmotion(emotionId: Long): Boolean {
        return emotionRecordDao.hasRecordsForEmotion(emotionId)
    }

    override suspend fun getMostRecentTimestamp(): Instant? {
        return emotionRecordDao.getMostRecentTimestamp()
    }

    /**
     * Convert TimeRange enum to Instant bounds
     */
    private fun getTimeRangeBounds(timeRange: TimeRange): Pair<Instant, Instant> {
        val now = Instant.now()
        val zoneId = ZoneId.systemDefault()

        val startTime = when (timeRange) {
            TimeRange.LAST_WEEK -> {
                now.minus(7, ChronoUnit.DAYS)
            }
            TimeRange.LAST_MONTH -> {
                now.minus(30, ChronoUnit.DAYS)
            }
            TimeRange.LAST_3_MONTHS -> {
                now.minus(90, ChronoUnit.DAYS)
            }
            TimeRange.LAST_SIX_MONTHS -> {
                now.minus(180, ChronoUnit.DAYS)
            }
            TimeRange.LAST_YEAR -> {
                now.minus(365, ChronoUnit.DAYS)
            }
            TimeRange.CUSTOM -> {
                now.minus(30, ChronoUnit.DAYS) // Default to last month for custom
            }
        }

        return startTime to now
    }

    /**
     * Group records by date and calculate daily counts
     */
    private fun groupRecordsByDate(records: List<EmotionRecordEntity>): List<DailyRecordCount> {
        val zoneId = ZoneId.systemDefault()

        // Group records by local date
        val groupedByDate = records.groupBy { record ->
            record.timestamp.atZone(zoneId).toLocalDate()
        }

        // Convert to DailyRecordCount objects
        return groupedByDate.map { (date, dayRecords) ->
            DailyRecordCount(
                date = date,
                count = dayRecords.size
            )
        }.sortedBy { it.date }
    }
}
