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
    val colorHex: String = "#2196F3", // 默认蓝色
    val iconName: String = "favorite", // Material Icon名称
    val createdAt: String // ISO格式的创建时间
) {
    companion object {
        fun create(
            name: String,
            description: String = "",
            colorHex: String = "#2196F3",
            iconName: String = "favorite"
        ): CustomEmotion {
            return CustomEmotion(
                name = name,
                description = description,
                colorHex = colorHex,
                iconName = iconName,
                createdAt = java.time.LocalDateTime.now().toString()
            )
        }
    }
}