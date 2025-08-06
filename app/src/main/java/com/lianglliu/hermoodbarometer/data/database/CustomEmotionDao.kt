package com.lianglliu.hermoodbarometer.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/**
 * 自定义情绪数据访问对象
 * 提供对自定义情绪数据的增删改查操作
 */
@Dao
interface CustomEmotionDao {

    /**
     * 插入新的自定义情绪
     */
    @Insert
    suspend fun insert(customEmotion: CustomEmotionEntity): Long

    /**
     * 更新自定义情绪
     */
    @Update
    suspend fun update(customEmotion: CustomEmotionEntity)

    /**
     * 删除自定义情绪
     */
    @Delete
    suspend fun delete(customEmotion: CustomEmotionEntity)

    /**
     * 根据ID删除自定义情绪
     */
    @Query("DELETE FROM custom_emotions WHERE id = :id")
    suspend fun deleteById(id: Long)

    /**
     * 获取所有启用的自定义情绪
     */
    @Query("SELECT * FROM custom_emotions WHERE isActive = 1 ORDER BY createdAt DESC")
    fun getAllActiveEmotions(): Flow<List<CustomEmotionEntity>>

    /**
     * 获取所有自定义情绪（包括禁用的）
     */
    @Query("SELECT * FROM custom_emotions ORDER BY createdAt DESC")
    fun getAllEmotions(): Flow<List<CustomEmotionEntity>>

    /**
     * 根据ID获取自定义情绪
     */
    @Query("SELECT * FROM custom_emotions WHERE id = :id")
    suspend fun getEmotionById(id: Long): CustomEmotionEntity?

    /**
     * 根据名称查找自定义情绪
     */
    @Query("SELECT * FROM custom_emotions WHERE name = :name LIMIT 1")
    suspend fun getEmotionByName(name: String): CustomEmotionEntity?

    /**
     * 启用或禁用自定义情绪
     */
    @Query("UPDATE custom_emotions SET isActive = :isActive WHERE id = :id")
    suspend fun setEmotionActive(id: Long, isActive: Boolean)

    /**
     * 检查是否存在同名情绪
     */
    @Query("SELECT COUNT(*) FROM custom_emotions WHERE name = :name AND id != :excludeId")
    suspend fun countEmotionsByName(name: String, excludeId: Long = 0): Int
} 