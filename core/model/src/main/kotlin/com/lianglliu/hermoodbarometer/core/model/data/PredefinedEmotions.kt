package com.lianglliu.hermoodbarometer.core.model.data

/**
 * Predefined emotions with resource keys for localization
 */
object PredefinedEmotions {

    /**
     * Resource key prefix for emotion names
     */
    const val EMOTION_NAME_PREFIX = "emotion_"

    /**
     * Resource key suffix for emotion descriptions
     */
    const val EMOTION_DESCRIPTION_SUFFIX = "_description"

    val emotions = listOf(
        PredefinedEmotion(
            id = 1L,
            resourceKey = "happy",
            emoji = "😊",
            sortOrder = 1
        ),
        PredefinedEmotion(
            id = 2L,
            resourceKey = "sad",
            emoji = "😢",
            sortOrder = 2
        ),
        PredefinedEmotion(
            id = 3L,
            resourceKey = "angry",
            emoji = "😡",
            sortOrder = 3
        ),
        PredefinedEmotion(
            id = 4L,
            resourceKey = "anxious",
            emoji = "😰",
            sortOrder = 4
        ),
        PredefinedEmotion(
            id = 5L,
            resourceKey = "calm",
            emoji = "😌",
            sortOrder = 5
        ),
        PredefinedEmotion(
            id = 6L,
            resourceKey = "excited",
            emoji = "🤩",
            sortOrder = 6
        ),
        PredefinedEmotion(
            id = 7L,
            resourceKey = "tired",
            emoji = "😴",
            sortOrder = 7
        ),
        PredefinedEmotion(
            id = 8L,
            resourceKey = "confused",
            emoji = "😕",
            sortOrder = 8
        ),
        PredefinedEmotion(
            id = 9L,
            resourceKey = "grateful",
            emoji = "🙏",
            sortOrder = 9
        ),
        PredefinedEmotion(
            id = 10L,
            resourceKey = "lonely",
            emoji = "😔",
            sortOrder = 10
        )
    )

    /**
     * Get predefined emotion by ID
     */
    fun getById(id: Long): PredefinedEmotion? = emotions.find { it.id == id }

    /**
     * Get all predefined emotion IDs
     */
    fun getAllIds(): List<Long> = emotions.map { it.id }
}

/**
 * Data class for predefined emotion configuration
 */
data class PredefinedEmotion(
    val id: Long,
    val resourceKey: String,
    val emoji: String,
    val sortOrder: Int
) {
    /**
     * Get the full resource name for the emotion name
     */
    fun getNameResourceName(): String =
        "${PredefinedEmotions.EMOTION_NAME_PREFIX}$resourceKey"

    /**
     * Get the full resource name for the emotion description
     */
    fun getDescriptionResourceName(): String =
        "${PredefinedEmotions.EMOTION_NAME_PREFIX}${resourceKey}${PredefinedEmotions.EMOTION_DESCRIPTION_SUFFIX}"
}