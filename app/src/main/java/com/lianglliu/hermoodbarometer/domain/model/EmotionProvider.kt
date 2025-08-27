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
            id = 1L,
            name = context.getString(R.string.emotion_happy),
            emoji = "😊",
            description = context.getString(R.string.emotion_happy_desc)
        ),
        Emotion(
            id = 2L,
            name = context.getString(R.string.emotion_sad),
            emoji = "😢",
            description = context.getString(R.string.emotion_sad_desc)
        ),
        Emotion(
            id = 3L,
            name = context.getString(R.string.emotion_angry),
            emoji = "😡",
            description = context.getString(R.string.emotion_angry_desc)
        ),
        Emotion(
            id = 4L,
            name = context.getString(R.string.emotion_anxious),
            emoji = "😰",
            description = context.getString(R.string.emotion_anxious_desc)
        ),
        Emotion(
            id = 5L,
            name = context.getString(R.string.emotion_calm),
            emoji = "😌",
            description = context.getString(R.string.emotion_calm_desc)
        ),
        Emotion(
            id = 6L,
            name = context.getString(R.string.emotion_excited),
            emoji = "🤩",
            description = context.getString(R.string.emotion_excited_desc)
        ),
        Emotion(
            id = 7L,
            name = context.getString(R.string.emotion_tired),
            emoji = "😴",
            description = context.getString(R.string.emotion_tired_desc)
        ),
        Emotion(
            id = 8L,
            name = context.getString(R.string.emotion_confused),
            emoji = "😕",
            description = context.getString(R.string.emotion_confused_desc)
        ),
        Emotion(
            id = 9L,
            name = context.getString(R.string.emotion_grateful),
            emoji = "🙏",
            description = context.getString(R.string.emotion_grateful_desc)
        ),
        Emotion(
            id = 10L,
            name = context.getString(R.string.emotion_lonely),
            emoji = "😔",
            description = context.getString(R.string.emotion_lonely_desc)
        )
    )
    
    /**
     * 根据ID获取本地化的预定义情绪
     */
    fun getLocalizedEmotionById(context: Context, id: Long): Emotion? {
        return getLocalizedDefaultEmotions(context).find { it.id == id }
    }
}
