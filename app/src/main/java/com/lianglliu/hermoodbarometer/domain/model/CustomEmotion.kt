package com.lianglliu.hermoodbarometer.domain.model

import kotlinx.serialization.Serializable

/**
 * 自定义情绪模型
 * 允许用户创建自己的情绪类型
 */
@Serializable
data class CustomEmotion(
    val id: Long = 0,
    val name: String,
    val description: String = "",
    val color: String = "#2196F3", // 默认蓝色
    val iconName: String = "favorite", // Material Icon名称
    val createdAt: Long = System.currentTimeMillis() // 创建时间戳
) {
    companion object {
        fun create(
            name: String,
            description: String = "",
            color: String = "#2196F3",
            iconName: String = "favorite"
        ): CustomEmotion {
            return CustomEmotion(
                name = name,
                description = description,
                color = color,
                iconName = iconName,
                createdAt = System.currentTimeMillis()
            )
        }
    }
}