package com.lianglliu.hermoodbarometer.dao.model

/**
 * Data class for aggregated emotion statistics from database queries
 * Used internally by DAO for statistical queries
 */
data class EmotionStatsResult(
    val emotionId: Long,
    val emotionEmoji: String,
    val count: Int,
    val avgIntensity: Float
)