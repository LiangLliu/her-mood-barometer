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
     * 根据时间范围获取情绪记录
     */
    fun getRecordsByTimeRange(timeRange: TimeRange): Flow<List<EmotionRecord>>
    
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
}