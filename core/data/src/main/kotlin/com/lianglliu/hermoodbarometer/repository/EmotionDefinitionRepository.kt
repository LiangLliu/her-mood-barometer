package com.lianglliu.hermoodbarometer.repository

import com.lianglliu.hermoodbarometer.core.model.data.Emotion
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for emotion definitions
 * Manages predefined and user-created emotions
 */
interface EmotionDefinitionRepository {

    /**
     * Get all active emotions (predefined + user-created)
     * Ordered by user-created status and sort order
     */
    fun getAllActiveEmotions(): Flow<List<Emotion>>

    /**
     * Get only user-created emotions
     */
    fun getUserCreatedEmotions(): Flow<List<Emotion>>

    /**
     * Get only predefined emotions
     */
    fun getPredefinedEmotions(): Flow<List<Emotion>>

    /**
     * Get emotion by ID
     */
    suspend fun getEmotionById(id: Long): Emotion?

    /**
     * Get multiple emotions by IDs
     */
    suspend fun getEmotionsByIds(ids: List<Long>): List<Emotion>

    /**
     * Insert new user-created emotion
     */
    suspend fun insertEmotion(emotion: Emotion): Long

    /**
     * Update existing emotion
     */
    suspend fun updateEmotion(emotion: Emotion)

    /**
     * Deactivate emotion (soft delete)
     * Preferred over hard delete to maintain referential integrity
     */
    suspend fun deactivateEmotion(id: Long)

    /**
     * Reactivate a previously deactivated emotion
     */
    suspend fun reactivateEmotion(id: Long)

    /**
     * Check if emotion name already exists
     * Case-insensitive comparison
     *
     * @param name Emotion name to check
     * @param excludeId Optional ID to exclude (for update operations)
     */
    suspend fun isEmotionNameExists(name: String, excludeId: Long? = null): Boolean

    /**
     * Get count of active emotions
     */
    suspend fun getActiveEmotionCount(): Int

    /**
     * Get count of user-created emotions
     */
    suspend fun getUserCreatedEmotionCount(): Int

    /**
     * Update sort orders for multiple emotions
     * Used for reordering emotions in the UI
     */
    suspend fun updateSortOrders(updates: List<Pair<Long, Int>>)

    /**
     * Initialize predefined emotions if not already present
     */
    suspend fun initializePredefinedEmotions()
}
