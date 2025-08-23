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
    val emotionId: String, // 情绪ID，预定义情绪为固定字符串，自定义情绪为 "custom_${id}"
    val emotionName: String, // 情绪名称
    val emotionEmoji: String, // 情绪表情符号
    val intensity: Int, // 情绪强度（1-5）
    val note: String = "", // 备注
    @Contextual
    val timestamp: LocalDateTime, // 记录时间戳
    val isCustomEmotion: Boolean = false, // 是否为自定义情绪
    val customEmotionId: Long? = null // 自定义情绪的数据库ID（仅用于自定义情绪）
) {
    /**
     * 获取情绪显示文本（表情符号 + 名称）
     */
    fun getDisplayText(): String {
        return "$emotionEmoji $emotionName"
    }
    
    /**
     * 获取用于图表显示的简短标签
     */
    fun getChartLabel(): String {
        return emotionEmoji
    }

    companion object {
        /**
         * 从预定义情绪创建记录
         */
        fun fromEmotion(
            emotion: Emotion,
            intensity: Int,
            note: String = "",
            timestamp: LocalDateTime = LocalDateTime.now()
        ): EmotionRecord {
            return EmotionRecord(
                emotionId = emotion.id,
                emotionName = emotion.name,
                emotionEmoji = emotion.emoji,
                intensity = intensity,
                note = note,
                timestamp = timestamp,
                isCustomEmotion = emotion.isCustom,
                customEmotionId = emotion.customId
            )
        }
        
        /**
         * 兼容性方法：从旧格式创建（用于数据迁移）
         */
        fun fromLegacyData(
            emotionType: String,
            intensity: Int,
            note: String = "",
            timestamp: LocalDateTime,
            customEmotionId: Long? = null,
            customEmotionName: String? = null,
            customEmotionEmoji: String? = null
        ): EmotionRecord {
            val isCustom = customEmotionId != null
            return if (isCustom && customEmotionName != null && customEmotionEmoji != null) {
                EmotionRecord(
                    emotionId = "custom_$customEmotionId",
                    emotionName = customEmotionName,
                    emotionEmoji = customEmotionEmoji,
                    intensity = intensity,
                    note = note,
                    timestamp = timestamp,
                    isCustomEmotion = true,
                    customEmotionId = customEmotionId
                )
            } else {
                // 从预定义情绪转换
                val defaultEmotion = Emotion.getDefaultEmotionById(emotionType.lowercase()) 
                    ?: Emotion.getDefaultEmotions().first()
                EmotionRecord(
                    emotionId = defaultEmotion.id,
                    emotionName = defaultEmotion.name,
                    emotionEmoji = defaultEmotion.emoji,
                    intensity = intensity,
                    note = note,
                    timestamp = timestamp,
                    isCustomEmotion = false,
                    customEmotionId = null
                )
            }
        }

    }
}