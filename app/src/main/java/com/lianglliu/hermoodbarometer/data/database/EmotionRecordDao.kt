package com.lianglliu.hermoodbarometer.data.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

/**
 * 情绪记录数据访问对象
 * 提供对情绪记录数据的增删改查操作
 */
@Dao
interface EmotionRecordDao {
    
    /**
     * 插入新的情绪记录
     */
    @Insert
    suspend fun insert(emotionRecord: EmotionRecordEntity): Long
    
    /**
     * 更新情绪记录
     */
    @Update
    suspend fun update(emotionRecord: EmotionRecordEntity)
    
    /**
     * 删除情绪记录
     */
    @Delete
    suspend fun delete(emotionRecord: EmotionRecordEntity)
    
    /**
     * 根据ID删除情绪记录
     */
    @Query("DELETE FROM emotion_records WHERE id = :id")
    suspend fun deleteById(id: Long)
    
    /**
     * 获取所有情绪记录（按时间倒序）
     */
    @Query("SELECT * FROM emotion_records ORDER BY timestamp DESC")
    fun getAllRecords(): Flow<List<EmotionRecordEntity>>
    
    /**
     * 根据ID获取情绪记录
     */
    @Query("SELECT * FROM emotion_records WHERE id = :id")
    suspend fun getRecordById(id: Long): EmotionRecordEntity?
    
    /**
     * 获取指定时间范围内的情绪记录
     */
    @Query("SELECT * FROM emotion_records WHERE timestamp BETWEEN :startTime AND :endTime ORDER BY timestamp DESC")
    fun getRecordsByTimeRange(startTime: LocalDateTime, endTime: LocalDateTime): Flow<List<EmotionRecordEntity>>
    
    /**
     * 获取指定情绪类型的记录
     */
    @Query("SELECT * FROM emotion_records WHERE emotionType = :emotionType ORDER BY timestamp DESC")
    fun getRecordsByEmotionType(emotionType: String): Flow<List<EmotionRecordEntity>>
    
    /**
     * 获取最近的N条记录
     */
    @Query("SELECT * FROM emotion_records ORDER BY timestamp DESC LIMIT :limit")
    fun getRecentRecords(limit: Int): Flow<List<EmotionRecordEntity>>
    
    /**
     * 获取情绪统计信息
     */
    @Query("""
        SELECT emotionType, COUNT(*) as count, AVG(intensity) as avgIntensity
        FROM emotion_records 
        WHERE timestamp BETWEEN :startTime AND :endTime
        GROUP BY emotionType
        ORDER BY count DESC
    """)
    fun getEmotionStatistics(startTime: LocalDateTime, endTime: LocalDateTime): Flow<List<EmotionStatistics>>
}

/**
 * 情绪统计数据类
 */
data class EmotionStatistics(
    val emotionType: String,
    val count: Int,
    val avgIntensity: Double
) 