package com.lianglliu.hermoodbarometer.core.domain

import com.lianglliu.hermoodbarometer.repository.EmotionDefinitionRepository
import jakarta.inject.Inject

/**
 * Use case for initializing predefined emotions in the database Should be called during app
 * initialization
 */
class InitializeEmotionsUseCase
@Inject
constructor(private val emotionDefinitionRepository: EmotionDefinitionRepository) {

    /**
     * Initialize predefined emotions if they don't exist
     *
     * @return Result indicating success or failure
     */
    suspend operator fun invoke(): Result<Unit> {
        return try {
            emotionDefinitionRepository.initializePredefinedEmotions()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
