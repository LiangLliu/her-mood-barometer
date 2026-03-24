package com.lianglliu.hermoodbarometer.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.lianglliu.hermoodbarometer.dao.model.EmotionStatsResult
import com.lianglliu.hermoodbarometer.dao.model.IntensityCount
import com.lianglliu.hermoodbarometer.model.EmotionRecordEntity
import java.time.Instant
import kotlinx.coroutines.flow.Flow

/** Data Access Object for emotion records Provides CRUD operations and statistical queries */
@Dao
interface EmotionRecordDao {

    /** Insert new emotion record */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecord(record: EmotionRecordEntity): Long

    /** Insert multiple emotion records */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecords(records: List<EmotionRecordEntity>)

    /** Update emotion record */
    @Update suspend fun updateRecord(record: EmotionRecordEntity)

    /** Delete emotion record */
    @Delete suspend fun deleteRecord(record: EmotionRecordEntity)

    /** Delete record by ID */
    @Query("DELETE FROM emotion_records WHERE id = :id") suspend fun deleteRecordById(id: Long)

    /** Delete all records (use with caution) */
    @Query("DELETE FROM emotion_records") suspend fun deleteAllRecords()

    /** Get all emotion records ordered by timestamp (newest first) */
    @Query(
        """
        SELECT * FROM emotion_records
        ORDER BY timestamp DESC
    """
    )
    fun getAllRecords(): Flow<List<EmotionRecordEntity>>

    /** Get record by ID */
    @Query("SELECT * FROM emotion_records WHERE id = :id")
    suspend fun getRecordById(id: Long): EmotionRecordEntity?

    /** Get records by time range */
    @Query(
        """
        SELECT * FROM emotion_records
        WHERE timestamp >= :startTime AND timestamp <= :endTime
        ORDER BY timestamp DESC
    """
    )
    fun getRecordsByTimeRange(startTime: Instant, endTime: Instant): Flow<List<EmotionRecordEntity>>

    /** Get records for specific emotion */
    @Query(
        """
        SELECT * FROM emotion_records
        WHERE emotionId = :emotionId
        ORDER BY timestamp DESC
    """
    )
    fun getRecordsByEmotionId(emotionId: Long): Flow<List<EmotionRecordEntity>>

    /** Get recent records with limit */
    @Query(
        """
        SELECT * FROM emotion_records
        ORDER BY timestamp DESC
        LIMIT :limit
    """
    )
    fun getRecentRecords(limit: Int): Flow<List<EmotionRecordEntity>>

    /** Get records by year and month (for calendar view) */
    @Query(
        """
        SELECT * FROM emotion_records
        WHERE timestamp >= :startOfMonth AND timestamp < :startOfNextMonth
        ORDER BY timestamp ASC
    """
    )
    fun getRecordsByMonth(
        startOfMonth: Instant,
        startOfNextMonth: Instant,
    ): Flow<List<EmotionRecordEntity>>

    /** Get records count */
    @Query("SELECT COUNT(*) FROM emotion_records") fun getRecordCount(): Flow<Int>

    /** Get emotion statistics for time range */
    @Query(
        """
        SELECT
            emotionId,
            emotionEmoji,
            COUNT(*) as count,
            AVG(CAST(intensity as FLOAT)) as avgIntensity
        FROM emotion_records
        WHERE timestamp >= :startTime AND timestamp <= :endTime
        GROUP BY emotionId, emotionEmoji
        ORDER BY count DESC
    """
    )
    fun getEmotionStatistics(startTime: Instant, endTime: Instant): Flow<List<EmotionStatsResult>>

    /**
     * Get all records within time range for daily statistics Date grouping will be handled in the
     * repository layer
     */
    @Query(
        """
        SELECT * FROM emotion_records
        WHERE timestamp >= :startTime AND timestamp <= :endTime
        ORDER BY timestamp ASC
    """
    )
    suspend fun getRecordsForDailyStats(
        startTime: Instant,
        endTime: Instant,
    ): List<EmotionRecordEntity>

    /** Check if any records exist for given emotion */
    @Query(
        """
        SELECT EXISTS(
            SELECT 1 FROM emotion_records
            WHERE emotionId = :emotionId
            LIMIT 1
        )
    """
    )
    suspend fun hasRecordsForEmotion(emotionId: Long): Boolean

    /** Get records with pagination */
    @Query(
        """
        SELECT * FROM emotion_records
        ORDER BY timestamp DESC
        LIMIT :limit OFFSET :offset
    """
    )
    fun getRecordsPaged(limit: Int, offset: Int): Flow<List<EmotionRecordEntity>>

    /** Search records by note content */
    @Query(
        """
        SELECT * FROM emotion_records
        WHERE note LIKE '%' || :query || '%'
        ORDER BY timestamp DESC
    """
    )
    fun searchRecordsByNote(query: String): Flow<List<EmotionRecordEntity>>

    /** Get records with specific intensity */
    @Query(
        """
        SELECT * FROM emotion_records
        WHERE intensity = :intensity
        ORDER BY timestamp DESC
    """
    )
    fun getRecordsByIntensity(intensity: Int): Flow<List<EmotionRecordEntity>>

    /** Get the most recent record's timestamp */
    @Query(
        """
        SELECT timestamp FROM emotion_records
        ORDER BY timestamp DESC
        LIMIT 1
    """
    )
    suspend fun getMostRecentTimestamp(): Instant?

    /** Get intensity counts within time range (avoids N+1 query problem) */
    @Query(
        """
        SELECT intensity, COUNT(*) as count
        FROM emotion_records
        WHERE timestamp >= :startTime AND timestamp <= :endTime
        GROUP BY intensity
    """
    )
    suspend fun getIntensityCountsInRange(
        startTime: Instant,
        endTime: Instant,
    ): List<IntensityCount>
}
