package com.lianglliu.hermoodbarometer.repository.mapper

import com.lianglliu.hermoodbarometer.core.model.data.Emotion
import com.lianglliu.hermoodbarometer.core.model.data.PredefinedEmotions
import com.lianglliu.hermoodbarometer.model.EmotionEntity
import com.lianglliu.hermoodbarometer.core.locales.R as LocalesR

/**
 * Extension functions for mapping between database entities and domain models
 */

/**
 * Convert EmotionEntity to Emotion domain model
 */
fun EmotionEntity.toDomainModel(): Emotion {
    // Check if this is a predefined emotion
    val predefinedEmotion = if (!isUserCreated) {
        PredefinedEmotions.emotions.find { it.resourceKey == name }
    } else null

    // Resolve resource IDs for predefined emotions
    val (nameResId, descriptionResId) = if (predefinedEmotion != null) {
        val nameId = getEmotionNameResourceId(predefinedEmotion.resourceKey)
        val descId = getEmotionDescriptionResourceId(predefinedEmotion.resourceKey)
        Pair(nameId, descId)
    } else {
        Pair(0, 0)
    }

    return Emotion(
        id = id,
        nameResId = nameResId,
        name = if (isUserCreated) name else "", // Only user emotions have custom names
        emoji = emoji,
        descriptionResId = descriptionResId,
        description = description,
        isUserCreated = isUserCreated,
        isActive = isActive,
        createdAt = createdAt,
        sortOrder = sortOrder
    )
}

/**
 * Convert Emotion domain model to EmotionEntity
 */
fun Emotion.toEntity(): EmotionEntity {
    return EmotionEntity(
        id = id,
        name = if (isUserCreated) name else {
            // For predefined emotions, store the resource key
            PredefinedEmotions.emotions.find { it.id == id }?.resourceKey ?: name
        },
        emoji = emoji,
        description = description,
        isUserCreated = isUserCreated,
        isActive = isActive,
        sortOrder = sortOrder,
        createdAt = createdAt
    )
}

/**
 * Convert list of EmotionEntity to list of Emotion
 */
fun List<EmotionEntity>.toDomainModels(): List<Emotion> = map { it.toDomainModel() }

/**
 * Convert list of Emotion to list of EmotionEntity
 */
fun List<Emotion>.toEntities(): List<EmotionEntity> = map { it.toEntity() }

/**
 * Get emotion name resource ID from resource key
 */
private fun getEmotionNameResourceId(resourceKey: String): Int {
    return when (resourceKey) {
        "happy" -> LocalesR.string.emotion_happy
        "sad" -> LocalesR.string.emotion_sad
        "angry" -> LocalesR.string.emotion_angry
        "anxious" -> LocalesR.string.emotion_anxious
        "calm" -> LocalesR.string.emotion_calm
        "excited" -> LocalesR.string.emotion_excited
        "tired" -> LocalesR.string.emotion_tired
        "confused" -> LocalesR.string.emotion_confused
        "grateful" -> LocalesR.string.emotion_grateful
        "lonely" -> LocalesR.string.emotion_lonely
        else -> 0
    }
}

/**
 * Get emotion description resource ID from resource key
 */
private fun getEmotionDescriptionResourceId(resourceKey: String): Int {
    return when (resourceKey) {
        "happy" -> LocalesR.string.emotion_happy_desc
        "sad" -> LocalesR.string.emotion_sad_desc
        "angry" -> LocalesR.string.emotion_angry_desc
        "anxious" -> LocalesR.string.emotion_anxious_desc
        "calm" -> LocalesR.string.emotion_calm_desc
        "excited" -> LocalesR.string.emotion_excited_desc
        "tired" -> LocalesR.string.emotion_tired_desc
        "confused" -> LocalesR.string.emotion_confused_desc
        "grateful" -> LocalesR.string.emotion_grateful_desc
        "lonely" -> LocalesR.string.emotion_lonely_desc
        else -> 0
    }
}