package com.lianglliu.hermoodbarometer.domain.model

import kotlinx.serialization.Serializable
import java.time.LocalDateTime

/**
 * 情绪记录领域模型
 * 表示用户的一次情绪记录
 */
@Serializable
data class EmotionRecord(
    val id: Long = 0,
    val emotionType: EmotionType,
    val intensity: EmotionIntensity,
    val note: String = "",
    val timestamp: String, // ISO格式的时间字符串，用于序列化
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
            emotionType.displayNameResId
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
                emotionType = emotionType,
                intensity = intensity,
                note = note,
                timestamp = LocalDateTime.now().toString(),
                isCustomEmotion = !customEmotionName.isNullOrBlank(),
                customEmotionName = customEmotionName
            )
        }
    }
}