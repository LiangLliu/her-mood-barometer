package com.lianglliu.hermoodbarometer.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 自定义情绪数据库实体
 * 
 * @property id 自定义情绪ID，主键，自增
 * @property name 情绪名称
 * @property emoji 情绪emoji表情符号
 * @property description 情绪描述
 * @property isActive 是否启用
 * @property createdAt 创建时间
 */
@Entity(tableName = "custom_emotions")
data class CustomEmotionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val emoji: String = "😊",
    val description: String = "",
    val isActive: Boolean = true,
    val createdAt: Long = System.currentTimeMillis()
) 