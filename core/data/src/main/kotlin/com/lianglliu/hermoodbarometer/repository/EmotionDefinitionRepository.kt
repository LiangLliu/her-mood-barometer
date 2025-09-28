package com.lianglliu.hermoodbarometer.repository


import com.lianglliu.hermoodbarometer.core.model.data.Emotion
import kotlinx.coroutines.flow.Flow

/**
 * 情绪定义仓库接口
 * 定义情绪相关的数据操作契约
 */
interface EmotionDefinitionRepository {
    
    /**
     * 获取所有活跃的情绪
     */
    fun getAllEmotions(): Flow<List<Emotion>>
    
    /**
     * 获取所有用户创建的情绪
     */
    fun getUserCreatedEmotions(): Flow<List<Emotion>>
    
    /**
     * 根据ID获取情绪
     */
    suspend fun getEmotionById(id: Long): Emotion?
    

    
    /**
     * 插入新情绪
     */
    suspend fun insertEmotion(emotion: Emotion): Long
    
    /**
     * 更新情绪
     */
    suspend fun updateEmotion(emotion: Emotion)
    
    /**
     * 删除情绪
     */
    suspend fun deleteEmotion(emotion: Emotion)
    
    /**
     * 根据ID删除情绪
     */
    suspend fun deleteEmotionById(id: Long)
    
    /**
     * 软删除情绪（设置为非活跃）
     */
    suspend fun deactivateEmotion(id: Long)
    
    /**
     * 检查情绪名称是否已存在
     */
    suspend fun isEmotionNameExists(name: String): Boolean
    
    /**
     * 获取情绪总数
     */
    suspend fun getEmotionCount(): Int
}
