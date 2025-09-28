package com.lianglliu.hermoodbarometer.repository.offline

import com.lianglliu.hermoodbarometer.core.model.data.Emotion
import com.lianglliu.hermoodbarometer.dao.EmotionDao
import com.lianglliu.hermoodbarometer.model.EmotionEntity
import com.lianglliu.hermoodbarometer.repository.EmotionDefinitionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * 统一情绪仓库实现
 * 管理所有情绪（预定义和用户创建的）
 */
class EmotionRepositoryImpl @Inject constructor(
    private val emotionDao: EmotionDao
) : EmotionDefinitionRepository {

    override fun getAllEmotions(): Flow<List<Emotion>> {
        return emotionDao.getAllActiveEmotions().map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    override fun getUserCreatedEmotions(): Flow<List<Emotion>> {
        return emotionDao.getUserCreatedEmotions().map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    override suspend fun getEmotionById(id: Long): Emotion? {
        return emotionDao.getEmotionById(id)?.toDomainModel()
    }

    override suspend fun insertEmotion(emotion: Emotion): Long {
        val entity = emotion.toEntity()
        return emotionDao.insertEmotion(entity)
    }

    override suspend fun updateEmotion(emotion: Emotion) {
        val entity = emotion.toEntity()
        emotionDao.updateEmotion(entity)
    }

    override suspend fun deleteEmotion(emotion: Emotion) {
        val entity = emotion.toEntity()
        emotionDao.deleteEmotion(entity)
    }

    override suspend fun deleteEmotionById(id: Long) {
        emotionDao.deleteEmotionById(id)
    }

    override suspend fun deactivateEmotion(id: Long) {
        emotionDao.deactivateEmotion(id)
    }

    override suspend fun isEmotionNameExists(name: String): Boolean {
        return emotionDao.countEmotionsByName(name) > 0
    }

    override suspend fun getEmotionCount(): Int {
        return emotionDao.getEmotionCount()
    }

    /**
     * 将数据库实体转换为领域模型
     */
    private fun EmotionEntity.toDomainModel(): Emotion {
        return Emotion(
            id = this.id,
            name = this.name,
            emoji = this.emoji,
            description = this.description,
            isUserCreated = this.isUserCreated,
            isActive = this.isActive,
            createdAt = this.createdAt
        )
    }

    /**
     * 将领域模型转换为数据库实体
     */
    private fun Emotion.toEntity(): EmotionEntity {
        return EmotionEntity(
            id = this.id,
            name = this.name,
            emoji = this.emoji,
            description = this.description,
            isUserCreated = this.isUserCreated,
            isActive = this.isActive,
            createdAt = this.createdAt
        )
    }
} 