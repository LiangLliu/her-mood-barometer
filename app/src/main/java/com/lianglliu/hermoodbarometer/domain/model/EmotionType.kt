package com.lianglliu.hermoodbarometer.domain.model

import kotlinx.serialization.Serializable

/**
 * 情绪类型枚举
 * 定义应用支持的基础情绪类型
 */
@Serializable
enum class EmotionType(val displayNameResId: String) {
    HAPPY("emotion_happy"),
    SAD("emotion_sad"),
    ANGRY("emotion_angry"),
    ANXIOUS("emotion_anxious"),
    CALM("emotion_calm"),
    EXCITED("emotion_excited"),
    TIRED("emotion_tired"),
    CONFUSED("emotion_confused"),
    GRATEFUL("emotion_grateful"),
    LONELY("emotion_lonely");

    companion object {
        fun getDefaultEmotions(): List<EmotionType> = values().toList()
    }
}