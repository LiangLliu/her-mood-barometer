package com.lianglliu.hermoodbarometer.core.model.data


/**
 * 统一的情绪模型
 * 支持所有类型的情绪，不再区分预定义和自定义
 */
data class Emotion(
    val id: Long, // 唯一标识符，数据库主键
    val name: String, // 情绪名称
    val emoji: String, // 表情符号
    val description: String = "", // 描述信息
    val isUserCreated: Boolean = false, // 是否为用户创建的情绪
    val isActive: Boolean = true, // 是否启用
    val createdAt: Long = System.currentTimeMillis() // 创建时间戳
) {
    companion object {
        
        /**
         * 获取所有预定义情绪
         * 这些情绪会根据当前语言环境显示对应的名称
         */
        fun getDefaultEmotions(): List<Emotion> = listOf(
            Emotion(
                id = 1L,
                name = "开心",
                emoji = "😊",
                description = "感到快乐和满足"
            ),
            Emotion(
                id = 2L, 
                name = "难过",
                emoji = "😢",
                description = "感到悲伤或沮丧"
            ),
            Emotion(
                id = 3L,
                name = "愤怒", 
                emoji = "😡",
                description = "感到生气或愤怒"
            ),
            Emotion(
                id = 4L,
                name = "焦虑",
                emoji = "😰", 
                description = "感到紧张或担心"
            ),
            Emotion(
                id = 5L,
                name = "平静",
                emoji = "😌",
                description = "感到平和与宁静"
            ),
            Emotion(
                id = 6L,
                name = "兴奋",
                emoji = "🤩",
                description = "感到激动和兴奋"
            ),
            Emotion(
                id = 7L,
                name = "疲惫", 
                emoji = "😴",
                description = "感到疲劳和困倦"
            ),
            Emotion(
                id = 8L,
                name = "困惑",
                emoji = "😕", 
                description = "感到迷茫或困惑"
            ),
            Emotion(
                id = 9L,
                name = "感恩",
                emoji = "🙏",
                description = "感到感激和感谢"
            ),
            Emotion(
                id = 10L,
                name = "孤独",
                emoji = "😔",
                description = "感到孤单或寂寞"
            )
        )
        
        /**
         * 根据ID获取预定义情绪
         */
        fun getDefaultEmotionById(id: Long): Emotion? {
            return getDefaultEmotions().find { it.id == id }
        }
        
        /**
         * 创建用户情绪
         */
        fun createUserEmotion(
            name: String,
            emoji: String,
            description: String = "",
            id: Long = 0
        ): Emotion {
            return Emotion(
                id = id,
                name = name,
                emoji = emoji,
                description = description,
                isUserCreated = true
            )
        }
    }
}
