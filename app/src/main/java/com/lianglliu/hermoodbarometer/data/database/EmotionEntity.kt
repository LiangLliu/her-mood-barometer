package com.lianglliu.hermoodbarometer.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 统一情绪数据库实体
 * 存储所有类型的情绪，包括预定义和用户创建的
 * 
 * @property id 情绪ID，主键，自增
 * @property name 情绪名称
 * @property emoji 情绪emoji表情符号
 * @property description 情绪描述
 * @property isUserCreated 是否为用户创建的情绪
 * @property isActive 是否启用
 * @property createdAt 创建时间
 */
@Entity(tableName = "emotions")
data class EmotionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val emoji: String = "😊",
    val description: String = "",
    val isUserCreated: Boolean = false,
    val isActive: Boolean = true,
    val createdAt: Long = System.currentTimeMillis()
)
