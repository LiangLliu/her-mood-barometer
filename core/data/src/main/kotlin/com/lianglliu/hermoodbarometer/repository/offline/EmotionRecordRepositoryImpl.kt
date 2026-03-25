package com.lianglliu.hermoodbarometer.repository.offline

import com.lianglliu.hermoodbarometer.core.model.data.DailyRecordCount
import com.lianglliu.hermoodbarometer.core.model.data.EmotionCount
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
import jakarta.inject.Inject
import jakarta.inject.Singleton
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Implementation of EmotionRepository using Room database Handles emotion record persistence and
 * retrieval
 */
@Singleton
class EmotionRecordRepositoryImpl
@Inject
constructor(private val emotionRecordDao: EmotionRecordDao) : EmotionRepository {

    override fun getAllRecords(): Flow<List<EmotionRecord>> {
        return emotionRecordDao.getAllRecords().map { entities -> entities.toDomainModels() }
    }

    override fun getRecordsByTimeRange(timeRange: TimeRange): Flow<List<EmotionRecord>> {
        val (startTime, endTime) = timeRange.toInstantBounds()
        return emotionRecordDao.getRecordsByTimeRange(startTime, endTime).map { entities ->
            entities.toDomainModels()
        }
    }

    override fun getRecordsByDateRange(
        startDate: Instant,
        endDate: Instant,
    ): Flow<List<EmotionRecord>> {
        return emotionRecordDao.getRecordsByTimeRange(startDate, endDate).map { entities ->
            entities.toDomainModels()
        }
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
        return emotionRecordDao.getRecentRecords(limit).map { entities ->
            entities.toDomainModels()
        }
    }

    override fun getRecordsByEmotionId(emotionId: Long): Flow<List<EmotionRecord>> {
        return emotionRecordDao.getRecordsByEmotionId(emotionId).map { entities ->
            entities.toDomainModels()
        }
    }

    override fun getRecordsByMonth(year: Int, month: Int): Flow<List<EmotionRecord>> {
        val startOfMonth =
            LocalDateTime.of(year, month, 1, 0, 0).atZone(ZoneId.systemDefault()).toInstant()

        val startOfNextMonth = startOfMonth.atZone(ZoneId.systemDefault()).plusMonths(1).toInstant()

        return emotionRecordDao.getRecordsByMonth(startOfMonth, startOfNextMonth).map { entities ->
            entities.toDomainModels()
        }
    }

    override fun searchRecordsByNote(query: String): Flow<List<EmotionRecord>> {
        return emotionRecordDao.searchRecordsByNote(query).map { entities ->
            entities.toDomainModels()
        }
    }

    override fun getEmotionStatisticsFlow(
        startDate: Instant,
        endDate: Instant,
    ): Flow<EmotionStatistics> {
        // Room Flow auto-emits when emotion_records table changes
        return emotionRecordDao.getEmotionStatistics(startDate, endDate).map { emotionStats ->
            val totalRecords = emotionStats.sumOf { it.count }

            val emotionDistribution = emotionStats.map { stats ->
                EmotionCount(
                    emotionId = stats.emotionId,
                    emotionEmoji = stats.emotionEmoji,
                    count = stats.count,
                    percentage =
                        if (totalRecords > 0) {
                            (stats.count.toFloat() / totalRecords) * 100f
                        } else 0f,
                )
            }

            val intensityCounts = emotionRecordDao.getIntensityCountsInRange(startDate, endDate)
            val intensityMap = intensityCounts.associate {
                EmotionIntensity.fromLevel(it.intensity) to it.count
            }

            val averageIntensity =
                if (totalRecords > 0) {
                    emotionStats.map { it.avgIntensity * it.count }.sum() / totalRecords
                } else 0f

            val mostFrequent = emotionDistribution.maxByOrNull { it.count }

            val daysBetween =
                ChronoUnit.DAYS.between(
                        startDate.atZone(ZoneId.systemDefault()).toLocalDate(),
                        endDate.atZone(ZoneId.systemDefault()).toLocalDate(),
                    )
                    .toInt() + 1

            val timeRangeInfo =
                TimeRangeInfo(startDate = startDate, endDate = endDate, totalDays = daysBetween)

            val dailyRecords = emotionRecordDao.getRecordsForDailyStats(startDate, endDate)
            val dailyRecordCounts = groupRecordsByDate(dailyRecords)

            EmotionStatistics(
                totalRecords = totalRecords,
                emotionDistribution = emotionDistribution,
                intensityDistribution = intensityMap,
                averageIntensity = averageIntensity,
                mostFrequentEmotion = mostFrequent,
                timeRange = timeRangeInfo,
                dailyRecords = dailyRecordCounts,
            )
        }
    }

    override suspend fun hasRecordsForEmotion(emotionId: Long): Boolean {
        return emotionRecordDao.hasRecordsForEmotion(emotionId)
    }

    override suspend fun getMostRecentTimestamp(): Instant? {
        return emotionRecordDao.getMostRecentTimestamp()
    }

    /** Group records by date and calculate daily counts */
    private fun groupRecordsByDate(records: List<EmotionRecordEntity>): List<DailyRecordCount> {
        val zoneId = ZoneId.systemDefault()

        // Group records by local date
        val groupedByDate = records.groupBy { record ->
            record.timestamp.atZone(zoneId).toLocalDate()
        }

        // Convert to DailyRecordCount objects
        return groupedByDate
            .map { (date, dayRecords) -> DailyRecordCount(date = date, count = dayRecords.size) }
            .sortedBy { it.date }
    }
}
