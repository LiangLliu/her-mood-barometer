package com.lianglliu.hermoodbarometer.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.lianglliu.hermoodbarometer.model.EmotionEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for emotions table
 * Manages all emotion-related database operations
 */
@Dao
interface EmotionDao {

    /**
     * Get all active emotions ordered by user-created status and sort order
     * Predefined emotions come first, followed by user-created ones
     */
    @Query("""
        SELECT * FROM emotions
        WHERE isActive = 1
        ORDER BY isUserCreated ASC, sortOrder ASC, name ASC
    """)
    fun getAllActiveEmotions(): Flow<List<EmotionEntity>>

    /**
     * Get only user-created active emotions
     */
    @Query("""
        SELECT * FROM emotions
        WHERE isUserCreated = 1 AND isActive = 1
        ORDER BY sortOrder ASC, name ASC
    """)
    fun getUserCreatedEmotions(): Flow<List<EmotionEntity>>

    /**
     * Get only predefined active emotions
     */
    @Query("""
        SELECT * FROM emotions
        WHERE isUserCreated = 0 AND isActive = 1
        ORDER BY sortOrder ASC
    """)
    fun getPredefinedEmotions(): Flow<List<EmotionEntity>>

    /**
     * Get emotion by ID
     */
    @Query("SELECT * FROM emotions WHERE id = :id")
    suspend fun getEmotionById(id: Long): EmotionEntity?

    /**
     * Get multiple emotions by IDs
     */
    @Query("SELECT * FROM emotions WHERE id IN (:ids)")
    suspend fun getEmotionsByIds(ids: List<Long>): List<EmotionEntity>

    /**
     * Insert new emotion
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEmotion(emotion: EmotionEntity): Long

    /**
     * Insert multiple emotions
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEmotions(emotions: List<EmotionEntity>)

    /**
     * Update emotion
     */
    @Update
    suspend fun updateEmotion(emotion: EmotionEntity)

    /**
     * Delete emotion (hard delete - use with caution)
     */
    @Delete
    suspend fun deleteEmotion(emotion: EmotionEntity)

    /**
     * Soft delete emotion by setting isActive to false
     */
    @Query("UPDATE emotions SET isActive = 0 WHERE id = :id")
    suspend fun deactivateEmotion(id: Long)

    /**
     * Reactivate a deactivated emotion
     */
    @Query("UPDATE emotions SET isActive = 1 WHERE id = :id")
    suspend fun reactivateEmotion(id: Long)

    /**
     * Check if an emotion name already exists for active emotions
     * Case-insensitive comparison
     */
    @Query("""
        SELECT COUNT(*) FROM emotions
        WHERE LOWER(name) = LOWER(:name)
        AND isActive = 1
        AND (:excludeId IS NULL OR id != :excludeId)
    """)
    suspend fun countEmotionsByName(name: String, excludeId: Long? = null): Int

    /**
     * Get total count of active emotions
     */
    @Query("SELECT COUNT(*) FROM emotions WHERE isActive = 1")
    suspend fun getActiveEmotionCount(): Int

    /**
     * Get total count of user-created active emotions
     */
    @Query("SELECT COUNT(*) FROM emotions WHERE isUserCreated = 1 AND isActive = 1")
    suspend fun getUserCreatedEmotionCount(): Int

    /**
     * Update sort order for multiple emotions
     * Used for reordering emotions in the UI
     */
    @Transaction
    suspend fun updateSortOrders(updates: List<Pair<Long, Int>>) {
        updates.forEach { (id, sortOrder) ->
            updateSortOrder(id, sortOrder)
        }
    }

    /**
     * Update sort order for a single emotion
     */
    @Query("UPDATE emotions SET sortOrder = :sortOrder WHERE id = :id")
    suspend fun updateSortOrder(id: Long, sortOrder: Int)
}