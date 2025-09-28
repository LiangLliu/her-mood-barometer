package com.lianglliu.hermoodbarometer.model

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
    val emotionId: Long, // 情绪ID，关联emotions表的id
    val emotionName: String, // 情绪名称
    val emotionEmoji: String, // 情绪表情符号
    val intensity: Int, // 情绪强度（1-5）
    val note: String = "", // 备注
    @Contextual
    val timestamp: LocalDateTime // 记录时间戳
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
         * 从情绪创建记录
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
                timestamp = timestamp
            )
        }

    }
}