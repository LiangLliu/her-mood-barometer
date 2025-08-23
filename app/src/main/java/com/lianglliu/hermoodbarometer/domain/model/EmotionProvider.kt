package com.lianglliu.hermoodbarometer.domain.model

import android.content.Context
import com.lianglliu.hermoodbarometer.R

/**
 * 情绪提供者
 * 负责根据当前语言环境提供本地化的情绪数据
 */
object EmotionProvider {
    
    /**
     * 获取本地化的预定义情绪列表
     */
    fun getLocalizedDefaultEmotions(context: Context): List<Emotion> = listOf(
        Emotion(
            id = "happy",
            name = context.getString(R.string.emotion_happy),
            emoji = "😊",
            description = context.getString(R.string.emotion_happy_desc)
        ),
        Emotion(
            id = "sad",
            name = context.getString(R.string.emotion_sad),
            emoji = "😢",
            description = context.getString(R.string.emotion_sad_desc)
        ),
        Emotion(
            id = "angry",
            name = context.getString(R.string.emotion_angry),
            emoji = "😡",
            description = context.getString(R.string.emotion_angry_desc)
        ),
        Emotion(
            id = "anxious",
            name = context.getString(R.string.emotion_anxious),
            emoji = "😰",
            description = context.getString(R.string.emotion_anxious_desc)
        ),
        Emotion(
            id = "calm",
            name = context.getString(R.string.emotion_calm),
            emoji = "😌",
            description = context.getString(R.string.emotion_calm_desc)
        ),
        Emotion(
            id = "excited",
            name = context.getString(R.string.emotion_excited),
            emoji = "🤩",
            description = context.getString(R.string.emotion_excited_desc)
        ),
        Emotion(
            id = "tired",
            name = context.getString(R.string.emotion_tired),
            emoji = "😴",
            description = context.getString(R.string.emotion_tired_desc)
        ),
        Emotion(
            id = "confused",
            name = context.getString(R.string.emotion_confused),
            emoji = "😕",
            description = context.getString(R.string.emotion_confused_desc)
        ),
        Emotion(
            id = "grateful",
            name = context.getString(R.string.emotion_grateful),
            emoji = "🙏",
            description = context.getString(R.string.emotion_grateful_desc)
        ),
        Emotion(
            id = "lonely",
            name = context.getString(R.string.emotion_lonely),
            emoji = "😔",
            description = context.getString(R.string.emotion_lonely_desc)
        )
    )
    
    /**
     * 根据ID获取本地化的预定义情绪
     */
    fun getLocalizedEmotionById(context: Context, id: String): Emotion? {
        return getLocalizedDefaultEmotions(context).find { it.id == id }
    }
}
