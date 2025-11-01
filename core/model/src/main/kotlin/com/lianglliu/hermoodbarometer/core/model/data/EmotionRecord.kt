package com.lianglliu.hermoodbarometer.core.model.data

import java.time.LocalDateTime

/**
 * 情绪记录领域模型
 * 表示用户的一次情绪记录
 */
data class EmotionRecord(
    val id: Long = 0,
    val emotionId: Long, // 情绪ID，关联emotions表的id
    val emotionName: String, // 情绪名称
    val emotionEmoji: String, // 情绪表情符号
    val intensity: Int = 3, // 情绪强度（1-5），默认中等强度
    val note: String = "", // 备注
    val timestamp: LocalDateTime, // 记录时间戳
    val weather: String? = null, // 天气（可选）
    val activities: List<String> = emptyList() // 活动列表（可选）
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
            intensity: Int = 3,
            note: String = "",
            timestamp: LocalDateTime = LocalDateTime.now(),
            weather: String? = null,
            activities: List<String> = emptyList()
        ): EmotionRecord {
            return EmotionRecord(
                emotionId = emotion.id,
                emotionName = emotion.name,
                emotionEmoji = emotion.emoji,
                intensity = intensity,
                note = note,
                timestamp = timestamp,
                weather = weather,
                activities = activities
            )
        }

    }
}