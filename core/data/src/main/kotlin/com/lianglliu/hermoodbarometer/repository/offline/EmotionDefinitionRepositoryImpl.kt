package com.lianglliu.hermoodbarometer.repository.offline

import com.lianglliu.hermoodbarometer.core.model.data.Emotion
import com.lianglliu.hermoodbarometer.core.model.data.PredefinedEmotions
import com.lianglliu.hermoodbarometer.dao.EmotionDao
import com.lianglliu.hermoodbarometer.repository.EmotionDefinitionRepository
import com.lianglliu.hermoodbarometer.repository.mapper.toDomainModel
import com.lianglliu.hermoodbarometer.repository.mapper.toDomainModels
import com.lianglliu.hermoodbarometer.repository.mapper.toEntity
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

/**
 * Implementation of EmotionDefinitionRepository using Room database Manages both predefined and
 * user-created emotions
 */
@Singleton
class EmotionDefinitionRepositoryImpl @Inject constructor(private val emotionDao: EmotionDao) :
    EmotionDefinitionRepository {

    override fun getAllActiveEmotions(): Flow<List<Emotion>> {
        return emotionDao.getAllActiveEmotions().map { entities -> entities.toDomainModels() }
    }

    override fun getUserCreatedEmotions(): Flow<List<Emotion>> {
        return emotionDao.getUserCreatedEmotions().map { entities -> entities.toDomainModels() }
    }

    override fun getPredefinedEmotions(): Flow<List<Emotion>> {
        return emotionDao.getPredefinedEmotions().map { entities -> entities.toDomainModels() }
    }

    override suspend fun getEmotionById(id: Long): Emotion? {
        return emotionDao.getEmotionById(id)?.toDomainModel()
    }

    override suspend fun getEmotionsByIds(ids: List<Long>): List<Emotion> {
        return emotionDao.getEmotionsByIds(ids).toDomainModels()
    }

    override suspend fun insertEmotion(emotion: Emotion): Long {
        return emotionDao.insertEmotion(emotion.toEntity())
    }

    override suspend fun updateEmotion(emotion: Emotion) {
        emotionDao.updateEmotion(emotion.toEntity())
    }

    override suspend fun deactivateEmotion(id: Long) {
        emotionDao.deactivateEmotion(id)
    }

    override suspend fun reactivateEmotion(id: Long) {
        emotionDao.reactivateEmotion(id)
    }

    override suspend fun isEmotionNameExists(name: String, excludeId: Long?): Boolean {
        return emotionDao.countEmotionsByName(name, excludeId) > 0
    }

    override suspend fun getActiveEmotionCount(): Int {
        return emotionDao.getActiveEmotionCount()
    }

    override suspend fun getUserCreatedEmotionCount(): Int {
        return emotionDao.getUserCreatedEmotionCount()
    }

    override suspend fun updateSortOrders(updates: List<Pair<Long, Int>>) {
        emotionDao.updateSortOrders(updates)
    }

    override suspend fun initializePredefinedEmotions() {
        // Get existing predefined emotion IDs
        val existingIds = emotionDao.getPredefinedEmotions().first().map { it.id }

        // Create entities for missing predefined emotions
        val missingEmotions =
            PredefinedEmotions.emotions
                .filter { it.id !in existingIds }
                .map { predefinedEmotion ->
                    Emotion(
                            id = predefinedEmotion.id,
                            name = predefinedEmotion.resourceKey, // Store resource key as name
                            emoji = predefinedEmotion.emoji,
                            description = "",
                            isUserCreated = false,
                            isActive = true,
                            sortOrder = predefinedEmotion.sortOrder,
                        )
                        .toEntity()
                }

        // Insert missing predefined emotions
        if (missingEmotions.isNotEmpty()) {
            emotionDao.insertEmotions(missingEmotions)
        }
    }
}
