package com.lianglliu.hermoodbarometer.domain.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.Contextual
import java.time.LocalDateTime

/**
 * 情绪记录领域模型
 * 表示用户的一次情绪记录
 */
@Serializable
data class EmotionRecord(
    val id: Long = 0,
    val emotionType: String, // 改为String类型以兼容数据库存储
    val intensity: Int, // 改为Int类型以兼容数据库存储
    val note: String = "",
    @Contextual
    val timestamp: LocalDateTime, // 使用LocalDateTime以与数据库实体保持一致
    val isCustomEmotion: Boolean = false,
    val customEmotionName: String? = null
) {
    /**
     * 获取情绪显示名称
     * 如果是自定义情绪，返回自定义名称，否则返回预定义名称的资源ID
     */
    fun getEmotionDisplayName(): String {
        return if (isCustomEmotion && !customEmotionName.isNullOrBlank()) {
            customEmotionName
        } else {
            emotionType
        }
    }

    companion object {
        fun create(
            emotionType: EmotionType,
            intensity: EmotionIntensity,
            note: String = "",
            customEmotionName: String? = null
        ): EmotionRecord {
            return EmotionRecord(
                emotionType = emotionType.name,
                intensity = intensity.level,
                note = note,
                timestamp = LocalDateTime.now(), // 使用LocalDateTime.now()
                isCustomEmotion = !customEmotionName.isNullOrBlank(),
                customEmotionName = customEmotionName
            )
        }
    }
}