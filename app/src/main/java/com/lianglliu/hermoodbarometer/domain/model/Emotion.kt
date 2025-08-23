package com.lianglliu.hermoodbarometer.domain.model

import kotlinx.serialization.Serializable

/**
 * 统一的情绪模型
 * 同时支持预定义情绪和自定义情绪
 */
@Serializable
data class Emotion(
    val id: String, // 唯一标识符，预定义情绪使用固定ID，自定义情绪使用 "custom_${customId}"
    val name: String, // 情绪名称（支持多语言）
    val emoji: String, // 表情符号
    val description: String = "", // 描述信息
    val isCustom: Boolean = false, // 是否为自定义情绪
    val customId: Long? = null, // 自定义情绪的数据库ID
    val color: String? = null // 可选的颜色标识
) {
    companion object {
        
        /**
         * 获取所有预定义情绪
         * 这些情绪会根据当前语言环境显示对应的名称
         */
        fun getDefaultEmotions(): List<Emotion> = listOf(
            Emotion(
                id = "happy",
                name = "开心", // 在实际使用中会被 stringResource 替代
                emoji = "😊",
                description = "感到快乐和满足"
            ),
            Emotion(
                id = "sad", 
                name = "难过",
                emoji = "😢",
                description = "感到悲伤或沮丧"
            ),
            Emotion(
                id = "angry",
                name = "愤怒", 
                emoji = "😡",
                description = "感到生气或愤怒"
            ),
            Emotion(
                id = "anxious",
                name = "焦虑",
                emoji = "😰", 
                description = "感到紧张或担心"
            ),
            Emotion(
                id = "calm",
                name = "平静",
                emoji = "😌",
                description = "感到平和与宁静"
            ),
            Emotion(
                id = "excited",
                name = "兴奋",
                emoji = "🤩",
                description = "感到激动和兴奋"
            ),
            Emotion(
                id = "tired",
                name = "疲惫", 
                emoji = "😴",
                description = "感到疲劳和困倦"
            ),
            Emotion(
                id = "confused",
                name = "困惑",
                emoji = "😕", 
                description = "感到迷茫或困惑"
            ),
            Emotion(
                id = "grateful",
                name = "感恩",
                emoji = "🙏",
                description = "感到感激和感谢"
            ),
            Emotion(
                id = "lonely",
                name = "孤独",
                emoji = "😔",
                description = "感到孤单或寂寞"
            )
        )
        
        /**
         * 根据ID获取预定义情绪
         */
        fun getDefaultEmotionById(id: String): Emotion? {
            return getDefaultEmotions().find { it.id == id }
        }
        
        /**
         * 从自定义情绪创建Emotion对象
         */
        fun fromCustomEmotion(customEmotion: CustomEmotion): Emotion {
            return Emotion(
                id = "custom_${customEmotion.id}",
                name = customEmotion.name,
                emoji = customEmotion.emoji,
                description = customEmotion.description,
                isCustom = true,
                customId = customEmotion.id
            )
        }
        
        /**
         * 从旧的存储格式转换（兼容性方法）
         */
        fun fromLegacyEmotionType(emotionType: String, customEmotion: CustomEmotion? = null): Emotion {
            return if (customEmotion != null) {
                fromCustomEmotion(customEmotion)
            } else {
                getDefaultEmotionById(emotionType.lowercase()) 
                    ?: getDefaultEmotions().first() // 回退到默认情绪
            }
        }
    }
}
