package com.lianglliu.hermoodbarometer.repository

import com.lianglliu.hermoodbarometer.core.model.data.EmotionRecord
import com.lianglliu.hermoodbarometer.core.model.data.EmotionStatistics
import com.lianglliu.hermoodbarometer.core.model.data.TimeRange
import java.time.Instant
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for emotion records Defines the contract for emotion record data operations
 */
interface EmotionRepository {

    /** Get all emotion records ordered by timestamp (newest first) */
    fun getAllRecords(): Flow<List<EmotionRecord>>

    /** Get emotion records by predefined time range */
    fun getRecordsByTimeRange(timeRange: TimeRange): Flow<List<EmotionRecord>>

    /** Get emotion records by custom date range */
    fun getRecordsByDateRange(startDate: Instant, endDate: Instant): Flow<List<EmotionRecord>>

    /** Get emotion record by ID */
    suspend fun getRecordById(id: Long): EmotionRecord?

    /** Insert new emotion record */
    suspend fun insertRecord(record: EmotionRecord): Long

    /** Update existing emotion record */
    suspend fun updateRecord(record: EmotionRecord)

    /** Delete emotion record by ID */
    suspend fun deleteRecord(id: Long)

    /** Delete all emotion records */
    suspend fun deleteAllRecords()

    /** Get total record count as Flow */
    fun getRecordCount(): Flow<Int>

    /** Get recent emotion records with limit */
    fun getRecentRecords(limit: Int = 10): Flow<List<EmotionRecord>>

    /** Get emotion records by specific emotion ID */
    fun getRecordsByEmotionId(emotionId: Long): Flow<List<EmotionRecord>>

    /** Get emotion records by year and month */
    fun getRecordsByMonth(year: Int, month: Int): Flow<List<EmotionRecord>>

    /** Search emotion records by note content */
    fun searchRecordsByNote(query: String): Flow<List<EmotionRecord>>

    /** Get emotion statistics for a time range (reactive — re-emits on data changes) */
    fun getEmotionStatisticsFlow(startDate: Instant, endDate: Instant): Flow<EmotionStatistics>

    /** Check if emotion has any records (before deletion) */
    suspend fun hasRecordsForEmotion(emotionId: Long): Boolean

    /** Get the most recent record timestamp */
    suspend fun getMostRecentTimestamp(): Instant?
}
