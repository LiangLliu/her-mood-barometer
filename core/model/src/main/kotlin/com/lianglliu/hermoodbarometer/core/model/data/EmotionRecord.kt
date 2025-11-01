package com.lianglliu.hermoodbarometer.core.model.data

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

/**
 * Emotion record domain model
 * Represents a single emotional state recorded by the user
 *
 * @property id Unique identifier (database primary key)
 * @property emotionId Emotion ID (references emotions table)
 * @property emotionEmoji Emoji representation
 * @property intensity Emotion intensity level (1-5)
 * @property note Optional user note
 * @property timestamp When the record was created
 * @property weather Optional weather condition
 * @property activities Optional list of activities
 */
data class EmotionRecord(
    val id: Long = 0,
    val emotionId: Long,
    val emotionEmoji: String,
    val intensity: EmotionIntensity = EmotionIntensity.MEDIUM,
    val note: String = "",
    val timestamp: Instant = Instant.now(),
    val weather: Weather? = null,
    val activities: List<Activity> = emptyList()
) {
    /**
     * Get the timestamp as LocalDateTime in the system's default timezone
     */
    fun getLocalDateTime(): LocalDateTime =
        LocalDateTime.ofInstant(timestamp, ZoneId.systemDefault())

    /**
     * Check if this record has additional context (weather or activities)
     */
    fun hasContext(): Boolean = weather != null || activities.isNotEmpty()
}

/**
 * Weather conditions enum
 */
enum class Weather(val emoji: String) {
    SUNNY("☀️"),
    PARTLY_CLOUDY("⛅"),
    CLOUDY("☁️"),
    RAINY("🌧️"),
    STORMY("⛈️"),
    SNOWY("❄️"),
    FOGGY("🌫️"),
    WINDY("💨");

    companion object {
        fun fromString(value: String): Weather? =
            values().find { it.name.equals(value, ignoreCase = true) }
    }
}

/**
 * Activity types enum
 */
enum class Activity(val emoji: String) {
    WORK("💼"),
    EXERCISE("🏃"),
    READING("📚"),
    SOCIAL("👥"),
    ENTERTAINMENT("🎭"),
    TRAVEL("✈️"),
    EATING("🍽️"),
    SHOPPING("🛍️"),
    STUDY("📖"),
    CREATIVE("🎨"),
    RELAXING("🧘"),
    OUTDOOR("🌳");

    companion object {
        fun fromString(value: String): Activity? =
            values().find { it.name.equals(value, ignoreCase = true) }
    }
}