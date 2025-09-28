package com.lianglliu.hermoodbarometer.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.lianglliu.hermoodbarometer.model.EmotionEntity
import kotlinx.coroutines.flow.Flow

/**
 * 统一情绪数据访问对象
 * 管理所有情绪的数据库操作
 */
@Dao
interface EmotionDao {

    /**
     * 获取所有活跃的情绪
     */
    @Query("SELECT * FROM emotions WHERE isActive = 1 ORDER BY isUserCreated ASC, name ASC")
    fun getAllActiveEmotions(): Flow<List<EmotionEntity>>

    /**
     * 获取所有用户创建的情绪
     */
    @Query("SELECT * FROM emotions WHERE isUserCreated = 1 AND isActive = 1 ORDER BY name ASC")
    fun getUserCreatedEmotions(): Flow<List<EmotionEntity>>

    /**
     * 根据ID获取情绪
     */
    @Query("SELECT * FROM emotions WHERE id = :id")
    suspend fun getEmotionById(id: Long): EmotionEntity?

    /**
     * 插入新情绪
     */
    @Insert
    suspend fun insertEmotion(emotion: EmotionEntity): Long

    /**
     * 更新情绪
     */
    @Update
    suspend fun updateEmotion(emotion: EmotionEntity)

    /**
     * 删除情绪
     */
    @Delete
    suspend fun deleteEmotion(emotion: EmotionEntity)

    /**
     * 根据ID删除情绪
     */
    @Query("DELETE FROM emotions WHERE id = :id")
    suspend fun deleteEmotionById(id: Long)

    /**
     * 软删除情绪（设置为非活跃）
     */
    @Query("UPDATE emotions SET isActive = 0 WHERE id = :id")
    suspend fun deactivateEmotion(id: Long)

    /**
     * 检查情绪名称是否已存在
     */
    @Query("SELECT COUNT(*) FROM emotions WHERE name = :name AND isActive = 1")
    suspend fun countEmotionsByName(name: String): Int

    /**
     * 获取情绪总数
     */
    @Query("SELECT COUNT(*) FROM emotions WHERE isActive = 1")
    suspend fun getEmotionCount(): Int
}