package com.lianglliu.hermoodbarometer.domain.repository

import com.lianglliu.hermoodbarometer.domain.model.CustomEmotion
import kotlinx.coroutines.flow.Flow

/**
 * 自定义情绪仓库接口
 * 定义自定义情绪相关的数据操作契约
 */
interface CustomEmotionRepository {
    
    /**
     * 获取所有自定义情绪
     */
    fun getAllCustomEmotions(): Flow<List<CustomEmotion>>
    
    /**
     * 根据ID获取自定义情绪
     */
    suspend fun getCustomEmotionById(id: Long): CustomEmotion?
    
    /**
     * 根据名称获取自定义情绪
     */
    suspend fun getCustomEmotionByName(name: String): CustomEmotion?
    
    /**
     * 插入新的自定义情绪
     */
    suspend fun insertCustomEmotion(emotion: CustomEmotion): Long
    
    /**
     * 更新自定义情绪
     */
    suspend fun updateCustomEmotion(emotion: CustomEmotion)
    
    /**
     * 删除自定义情绪
     */
    suspend fun deleteCustomEmotion(id: Long)
    
    /**
     * 检查名称是否已存在
     */
    suspend fun isNameExists(name: String): Boolean
}