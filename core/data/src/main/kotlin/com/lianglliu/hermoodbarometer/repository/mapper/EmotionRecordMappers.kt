package com.lianglliu.hermoodbarometer.repository.mapper

import com.lianglliu.hermoodbarometer.core.model.data.Activity
import com.lianglliu.hermoodbarometer.core.model.data.EmotionIntensity
import com.lianglliu.hermoodbarometer.core.model.data.EmotionRecord
import com.lianglliu.hermoodbarometer.core.model.data.Weather
import com.lianglliu.hermoodbarometer.model.EmotionRecordEntity

/**
 * Extension functions for mapping between EmotionRecordEntity and EmotionRecord
 */

/**
 * Convert EmotionRecordEntity to EmotionRecord domain model
 */
fun EmotionRecordEntity.toDomainModel(): EmotionRecord {
    return EmotionRecord(
        id = id,
        emotionId = emotionId,
        emotionEmoji = emotionEmoji,
        intensity = EmotionIntensity.fromInt(intensity),
        note = note,
        timestamp = timestamp,
        weather = weather?.let { Weather.fromString(it) },
        activities = activities.parseActivities()
    )
}

/**
 * Convert EmotionRecord domain model to EmotionRecordEntity
 */
fun EmotionRecord.toEntity(): EmotionRecordEntity {
    return EmotionRecordEntity(
        id = id,
        emotionId = emotionId,
        emotionEmoji = emotionEmoji,
        intensity = intensity.level,
        note = note,
        timestamp = timestamp,
        weather = weather?.name,
        activities = activities.toDelimitedString()
    )
}

/**
 * Convert list of EmotionRecordEntity to list of EmotionRecord
 */
fun List<EmotionRecordEntity>.toDomainModels(): List<EmotionRecord> = map { it.toDomainModel() }

/**
 * Convert list of EmotionRecord to list of EmotionRecordEntity
 */
fun List<EmotionRecord>.toEntities(): List<EmotionRecordEntity> = map { it.toEntity() }

/**
 * Parse comma-separated activities string into list of Activity enums
 */
private fun String.parseActivities(): List<Activity> {
    if (this.isBlank()) return emptyList()

    return this.split(",")
        .map { it.trim() }
        .mapNotNull { Activity.fromString(it) }
}

/**
 * Convert list of Activity enums to comma-separated string
 */
private fun List<Activity>.toDelimitedString(): String {
    return joinToString(",") { it.name }
}

/**
 * Convert intensity Int to EmotionIntensity enum
 */
private fun EmotionIntensity.Companion.fromInt(value: Int): EmotionIntensity {
    return when (value) {
        1 -> EmotionIntensity.VERY_LOW
        2 -> EmotionIntensity.LOW
        3 -> EmotionIntensity.MEDIUM
        4 -> EmotionIntensity.HIGH
        5 -> EmotionIntensity.VERY_HIGH
        else -> EmotionIntensity.MEDIUM // Default to medium for invalid values
    }
}