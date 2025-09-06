package com.lianglliu.hermoodbarometer.domain.repository

import com.lianglliu.hermoodbarometer.domain.model.EmotionRecord
import com.lianglliu.hermoodbarometer.domain.model.TimeRange
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

/**
 * 情绪记录仓库接口
 * 定义情绪记录相关的数据操作契约
 */
interface EmotionRepository {
    
    /**
     * 获取所有情绪记录
     */
    fun getAllRecords(): Flow<List<EmotionRecord>>
    
    /**
     * 获取所有情绪记录（别名方法）
     */
    fun getAllEmotionRecords(): Flow<List<EmotionRecord>> = getAllRecords()
    
    /**
     * 根据时间范围获取情绪记录
     */
    fun getRecordsByTimeRange(timeRange: TimeRange): Flow<List<EmotionRecord>>
    
    /**
 * 根据时间范围获取情绪记录（别名方法）
 */
fun getEmotionRecordsByTimeRange(timeRange: TimeRange): Flow<List<EmotionRecord>> = getRecordsByTimeRange(timeRange)

/**
 * 根据自定义时间范围获取情绪记录（别名方法）
 */
fun getEmotionRecordsByCustomTimeRange(
    startDateTime: LocalDateTime,
    endDateTime: LocalDateTime
): Flow<List<EmotionRecord>> = getRecordsByDateRange(startDateTime, endDateTime)
    
    /**
     * 根据时间段获取情绪记录
     */
    fun getRecordsByDateRange(
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): Flow<List<EmotionRecord>>
    
    /**
     * 根据ID获取情绪记录
     */
    suspend fun getRecordById(id: Long): EmotionRecord?
    
    /**
     * 插入新的情绪记录
     */
    suspend fun insertRecord(record: EmotionRecord): Long
    
    /**
     * 添加情绪记录（别名方法）
     */
    suspend fun addEmotionRecord(record: EmotionRecord): Long = insertRecord(record)
    
    /**
     * 更新情绪记录
     */
    suspend fun updateRecord(record: EmotionRecord)
    
    /**
     * 删除情绪记录
     */
    suspend fun deleteRecord(id: Long)
    
    /**
     * 删除所有记录
     */
    suspend fun deleteAllRecords()
    
    /**
     * 获取记录总数
     */
    fun getRecordCount(): Flow<Int>
    
    /**
     * 获取最近的记录
     */
    fun getRecentRecords(limit: Int = 10): Flow<List<EmotionRecord>>
    
    /**
     * 根据情绪类型获取记录
     */
    fun getEmotionRecordsByType(emotionType: String): Flow<List<EmotionRecord>>
}