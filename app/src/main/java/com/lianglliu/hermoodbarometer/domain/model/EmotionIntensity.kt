package com.lianglliu.hermoodbarometer.domain.model

import kotlinx.serialization.Serializable

/**
 * 情绪强度等级
 * 表示情绪的强烈程度，范围从1-5
 */
@Serializable
enum class EmotionIntensity(val level: Int, val displayNameResId: String) {
    VERY_LOW(1, "intensity_very_low"),
    LOW(2, "intensity_low"),
    MEDIUM(3, "intensity_medium"),
    HIGH(4, "intensity_high"),
    VERY_HIGH(5, "intensity_very_high");

    companion object {
        fun fromLevel(level: Int): EmotionIntensity {
            return values().find { it.level == level } ?: MEDIUM
        }
    }
}