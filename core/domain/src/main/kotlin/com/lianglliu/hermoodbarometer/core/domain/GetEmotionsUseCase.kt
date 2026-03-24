package com.lianglliu.hermoodbarometer.core.domain

import com.lianglliu.hermoodbarometer.core.model.data.Emotion
import com.lianglliu.hermoodbarometer.repository.EmotionDefinitionRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

/**
 * Use case for retrieving emotion definitions Handles various scenarios for fetching available
 * emotions
 */
class GetEmotionsUseCase
@Inject
constructor(private val emotionDefinitionRepository: EmotionDefinitionRepository) {

    /**
     * Get all active emotions (predefined + user-created)
     *
     * @return Flow of all active emotions
     */
    operator fun invoke(): Flow<List<Emotion>> {
        return emotionDefinitionRepository.getAllActiveEmotions()
    }

    /**
     * Get only predefined emotions
     *
     * @return Flow of predefined emotions
     */
    fun getPredefined(): Flow<List<Emotion>> {
        return emotionDefinitionRepository.getPredefinedEmotions()
    }

    /**
     * Get only user-created emotions
     *
     * @return Flow of user-created emotions
     */
    fun getUserCreated(): Flow<List<Emotion>> {
        return emotionDefinitionRepository.getUserCreatedEmotions()
    }

    /**
     * Get emotion by ID
     *
     * @param emotionId The emotion ID
     * @return The emotion if found, null otherwise
     */
    suspend fun getById(emotionId: Long): Emotion? {
        return emotionDefinitionRepository.getEmotionById(emotionId)
    }

    /**
     * Get multiple emotions by IDs
     *
     * @param emotionIds List of emotion IDs
     * @return List of emotions found
     */
    suspend fun getByIds(emotionIds: List<Long>): List<Emotion> {
        return emotionDefinitionRepository.getEmotionsByIds(emotionIds)
    }
}
