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
    val emoji: String = "😊", // 默认笑脸emoji
    val createdAt: Long = System.currentTimeMillis() // 创建时间戳
) {
    companion object {
        fun create(
            name: String,
            description: String = "",
            emoji: String = "😊"
        ): CustomEmotion {
            return CustomEmotion(
                name = name,
                description = description,
                emoji = emoji,
                createdAt = System.currentTimeMillis()
            )
        }
    }
}