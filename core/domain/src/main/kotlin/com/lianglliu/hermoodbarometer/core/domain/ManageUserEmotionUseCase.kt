package com.lianglliu.hermoodbarometer.core.domain

import com.lianglliu.hermoodbarometer.core.model.data.Emotion
import com.lianglliu.hermoodbarometer.repository.EmotionDefinitionRepository
import com.lianglliu.hermoodbarometer.repository.EmotionRepository
import javax.inject.Inject

/**
 * Use case for managing user-created emotions
 * Handles creation, update, and deletion of custom emotions
 */
class ManageUserEmotionUseCase @Inject constructor(
    private val emotionDefinitionRepository: EmotionDefinitionRepository,
    private val emotionRepository: EmotionRepository
) {

    /**
     * Create a new user emotion
     * @param name Emotion name
     * @param emoji Emoji representation
     * @param description Optional description
     * @return Result containing the new emotion ID
     */
    suspend fun createEmotion(
        name: String,
        emoji: String,
        description: String = ""
    ): Result<Long> {
        return try {
            // Validate input
            if (!isValidEmotionData(name, emoji)) {
                return Result.failure(IllegalArgumentException("Invalid emotion data"))
            }

            // Check if name already exists
            if (emotionDefinitionRepository.isEmotionNameExists(name)) {
                return Result.failure(IllegalArgumentException("Emotion name already exists"))
            }

            // Create new emotion
            val emotion = Emotion(
                name = name.trim(),
                emoji = emoji.trim(),
                description = description.trim(),
                isUserCreated = true,
                isActive = true
            )

            val emotionId = emotionDefinitionRepository.insertEmotion(emotion)
            Result.success(emotionId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Update an existing user emotion
     * @param emotion The emotion to update
     * @return Result indicating success or failure
     */
    suspend fun updateEmotion(emotion: Emotion): Result<Unit> {
        return try {
            // Validate that it's a user-created emotion
            if (!emotion.isUserCreated) {
                return Result.failure(IllegalArgumentException("Cannot modify predefined emotions"))
            }

            // Validate input
            if (!isValidEmotionData(emotion.name, emotion.emoji)) {
                return Result.failure(IllegalArgumentException("Invalid emotion data"))
            }

            // Check if name is already taken by another emotion
            if (emotionDefinitionRepository.isEmotionNameExists(emotion.name, emotion.id)) {
                return Result.failure(IllegalArgumentException("Emotion name already exists"))
            }

            emotionDefinitionRepository.updateEmotion(emotion)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Delete a user emotion
     * @param emotionId The ID of the emotion to delete
     * @param forceDelete If true, deletes even if there are associated records
     * @return Result indicating success or failure
     */
    suspend fun deleteEmotion(emotionId: Long, forceDelete: Boolean = false): Result<Unit> {
        return try {
            // Get the emotion to validate it exists and is user-created
            val emotion = emotionDefinitionRepository.getEmotionById(emotionId)
                ?: return Result.failure(IllegalArgumentException("Emotion not found"))

            if (!emotion.isUserCreated) {
                return Result.failure(IllegalArgumentException("Cannot delete predefined emotions"))
            }

            // Check if there are associated records
            if (!forceDelete && emotionRepository.hasRecordsForEmotion(emotionId)) {
                return Result.failure(IllegalStateException("Cannot delete emotion with existing records"))
            }

            // Soft delete the emotion
            emotionDefinitionRepository.deactivateEmotion(emotionId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Reactivate a previously deleted user emotion
     * @param emotionId The ID of the emotion to reactivate
     * @return Result indicating success or failure
     */
    suspend fun reactivateEmotion(emotionId: Long): Result<Unit> {
        return try {
            val emotion = emotionDefinitionRepository.getEmotionById(emotionId)
                ?: return Result.failure(IllegalArgumentException("Emotion not found"))

            if (!emotion.isUserCreated) {
                return Result.failure(IllegalArgumentException("Cannot modify predefined emotions"))
            }

            emotionDefinitionRepository.reactivateEmotion(emotionId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Validate emotion data
     */
    private fun isValidEmotionData(name: String, emoji: String): Boolean {
        return name.trim().isNotBlank() &&
               name.trim().length <= 50 && // Reasonable name length limit
               emoji.trim().isNotBlank() &&
               emoji.trim().length <= 10 // Account for complex emoji
    }
}