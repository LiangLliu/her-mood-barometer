package com.lianglliu.hermoodbarometer.data.repository

import com.lianglliu.hermoodbarometer.data.database.CustomEmotionDao
import com.lianglliu.hermoodbarometer.data.database.CustomEmotionEntity
import com.lianglliu.hermoodbarometer.domain.model.CustomEmotion
import com.lianglliu.hermoodbarometer.domain.repository.CustomEmotionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 自定义情绪仓库实现
 * 实现自定义情绪的数据访问和业务逻辑
 */
@Singleton
class CustomEmotionRepositoryImpl @Inject constructor(
    private val customEmotionDao: CustomEmotionDao
) : CustomEmotionRepository {
    
    override fun getAllCustomEmotions(): Flow<List<CustomEmotion>> {
        return customEmotionDao.getAllActiveEmotions().map { entities ->
            entities.map { it.toDomainModel() }
        }
    }
    
    override suspend fun getCustomEmotionById(id: Long): CustomEmotion? {
        return customEmotionDao.getEmotionById(id)?.toDomainModel()
    }
    
    override suspend fun getCustomEmotionByName(name: String): CustomEmotion? {
        return customEmotionDao.getEmotionByName(name)?.toDomainModel()
    }
    
    override suspend fun insertCustomEmotion(emotion: CustomEmotion): Long {
        val entity = emotion.toEntity()
        return customEmotionDao.insert(entity)
    }
    
    override suspend fun updateCustomEmotion(emotion: CustomEmotion) {
        val entity = emotion.toEntity()
        customEmotionDao.update(entity)
    }
    
    override suspend fun deleteCustomEmotion(id: Long) {
        customEmotionDao.deleteById(id)
    }
    
    override suspend fun isNameExists(name: String): Boolean {
        return customEmotionDao.countEmotionsByName(name, 0) > 0
    }
}

/**
 * 扩展函数：数据库实体转领域模型
 */
private fun CustomEmotionEntity.toDomainModel(): CustomEmotion {
    return CustomEmotion(
        id = id,
        name = name,
        description = "", // 数据库中没有description字段，使用空字符串
        color = color,
        iconName = icon,
        createdAt = createdAt
    )
}

/**
 * 扩展函数：领域模型转数据库实体
 */
private fun CustomEmotion.toEntity(): CustomEmotionEntity {
    return CustomEmotionEntity(
        id = id,
        name = name,
        color = color,
        icon = iconName,
        isActive = true, // 默认启用
        createdAt = createdAt
    )
} 