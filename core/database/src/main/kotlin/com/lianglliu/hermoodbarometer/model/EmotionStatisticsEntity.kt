package com.lianglliu.hermoodbarometer.model

/**
 * 情绪统计数据类
 */
data class EmotionStatisticsEntity(
    val emotionId: Long,
    val emotionName: String,
    val emotionEmoji: String,
    val count: Int,
    val avgIntensity: Double
)