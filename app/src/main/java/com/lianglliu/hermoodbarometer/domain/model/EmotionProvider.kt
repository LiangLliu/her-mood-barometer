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
        ),
        Emotion(
            id = 11L,
            name = context.getString(R.string.emotion_thinking),
            emoji = "🤔",
            description = context.getString(R.string.emotion_thinking_desc)
        ),
        Emotion(
            id = 12L,
            name = context.getString(R.string.emotion_confident),
            emoji = "😎",
            description = context.getString(R.string.emotion_confident_desc)
        ),
        Emotion(
            id = 13L,
            name = context.getString(R.string.emotion_celebrating),
            emoji = "🥳",
            description = context.getString(R.string.emotion_celebrating_desc)
        ),
        Emotion(
            id = 14L,
            name = context.getString(R.string.emotion_fearful),
            emoji = "😨",
            description = context.getString(R.string.emotion_fearful_desc)
        ),
        Emotion(
            id = 15L,
            name = context.getString(R.string.emotion_dissatisfied),
            emoji = "😤",
            description = context.getString(R.string.emotion_dissatisfied_desc)
        ),
        Emotion(
            id = 16L,
            name = context.getString(R.string.emotion_stressed),
            emoji = "😓",
            description = context.getString(R.string.emotion_stressed_desc)
        ),
        Emotion(
            id = 17L,
            name = context.getString(R.string.emotion_wronged),
            emoji = "🥺",
            description = context.getString(R.string.emotion_wronged_desc)
        ),
        Emotion(
            id = 18L,
            name = context.getString(R.string.emotion_satisfied),
            emoji = "😇",
            description = context.getString(R.string.emotion_satisfied_desc)
        ),
        Emotion(
            id = 19L,
            name = context.getString(R.string.emotion_helpless),
            emoji = "🙃",
            description = context.getString(R.string.emotion_helpless_desc)
        ),
        Emotion(
            id = 20L,
            name = context.getString(R.string.emotion_embarrassed),
            emoji = "😬",
            description = context.getString(R.string.emotion_embarrassed_desc)
        ),
        Emotion(
            id = 21L,
            name = context.getString(R.string.emotion_silent),
            emoji = "🤐",
            description = context.getString(R.string.emotion_silent_desc)
        )
    )
    
    /**
     * 根据ID获取本地化的预定义情绪
     */
    fun getLocalizedEmotionById(context: Context, id: Long): Emotion? {
        return getLocalizedDefaultEmotions(context).find { it.id == id }
    }
}