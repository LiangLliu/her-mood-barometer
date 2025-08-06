package com.lianglliu.hermoodbarometer.data.repository

import com.lianglliu.hermoodbarometer.data.database.EmotionRecordDao
import com.lianglliu.hermoodbarometer.data.database.EmotionRecordEntity
import com.lianglliu.hermoodbarometer.domain.model.EmotionRecord
import com.lianglliu.hermoodbarometer.domain.model.TimeRange
import com.lianglliu.hermoodbarometer.domain.repository.EmotionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 情绪记录仓库实现
 * 实现情绪记录的数据访问和业务逻辑
 */
@Singleton
class EmotionRepositoryImpl @Inject constructor(
    private val emotionRecordDao: EmotionRecordDao
) : EmotionRepository {
    
    override fun getAllRecords(): Flow<List<EmotionRecord>> {
        return emotionRecordDao.getAllRecords().map { entities ->
            entities.map { it.toDomainModel() }
        }
    }
    
    override fun getRecordsByTimeRange(timeRange: TimeRange): Flow<List<EmotionRecord>> {
        val startTime = timeRange.getStartDateTime()
        val endTime = LocalDateTime.now()
        return emotionRecordDao.getRecordsByTimeRange(startTime, endTime).map { entities ->
            entities.map { it.toDomainModel() }
        }
    }
    
    override fun getRecordsByDateRange(startDate: LocalDateTime, endDate: LocalDateTime): Flow<List<EmotionRecord>> {
        return emotionRecordDao.getRecordsByTimeRange(startDate, endDate).map { entities ->
            entities.map { it.toDomainModel() }
        }
    }
    
    override fun getRecentRecords(limit: Int): Flow<List<EmotionRecord>> {
        return emotionRecordDao.getRecentRecords(limit).map { entities ->
            entities.map { it.toDomainModel() }
        }
    }
    
    override suspend fun getRecordById(id: Long): EmotionRecord? {
        return emotionRecordDao.getRecordById(id)?.toDomainModel()
    }
    
    override suspend fun insertRecord(record: EmotionRecord): Long {
        val entity = record.toEntity()
        return emotionRecordDao.insert(entity)
    }
    
    override suspend fun updateRecord(record: EmotionRecord) {
        val entity = record.toEntity()
        emotionRecordDao.update(entity)
    }
    
    override suspend fun deleteRecord(id: Long) {
        emotionRecordDao.deleteById(id)
    }
    
    override suspend fun deleteAllRecords() {
        // 这里需要添加一个删除所有记录的方法到DAO
        // 暂时使用空实现
    }
    
    override fun getRecordCount(): Flow<Int> {
        // 这里需要添加一个获取记录总数的方法到DAO
        // 暂时返回0
        return kotlinx.coroutines.flow.flowOf(0)
    }
    
    override fun getEmotionRecordsByType(emotionType: String): Flow<List<EmotionRecord>> {
        return emotionRecordDao.getRecordsByEmotionType(emotionType).map { entities ->
            entities.map { it.toDomainModel() }
        }
    }
}

/**
 * 扩展函数：数据库实体转领域模型
 */
private fun EmotionRecordEntity.toDomainModel(): EmotionRecord {
    return EmotionRecord(
        id = id,
        emotionType = emotionType,
        intensity = intensity,
        note = note,
        timestamp = timestamp, // 直接使用LocalDateTime
        isCustomEmotion = customEmotionId != null,
        customEmotionName = null // 需要从自定义情绪表中查询
    )
}

/**
 * 扩展函数：领域模型转数据库实体
 */
private fun EmotionRecord.toEntity(): EmotionRecordEntity {
    return EmotionRecordEntity(
        id = id,
        emotionType = emotionType,
        intensity = intensity,
        note = note,
        timestamp = timestamp, // 直接使用LocalDateTime
        customEmotionId = if (isCustomEmotion) 1L else null // 简化处理，实际应该查询自定义情绪ID
    )
} 