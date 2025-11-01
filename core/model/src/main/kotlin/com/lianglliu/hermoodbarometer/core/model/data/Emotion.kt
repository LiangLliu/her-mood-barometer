package com.lianglliu.hermoodbarometer.core.model.data

import java.time.Instant

/**
 * Unified emotion model supporting both predefined and user-created emotions
 *
 * @property id Unique identifier (database primary key)
 * @property nameResId Resource ID for predefined emotion names (0 for user-created)
 * @property name Custom name for user-created emotions (empty for predefined)
 * @property emoji Emoji representation of the emotion
 * @property descriptionResId Resource ID for predefined emotion descriptions (0 for user-created)
 * @property description Custom description for user-created emotions (empty for predefined)
 * @property isUserCreated Whether this emotion was created by the user
 * @property isActive Whether this emotion is currently active
 * @property createdAt Timestamp when the emotion was created
 * @property sortOrder Display order for the emotion (lower values appear first)
 */
data class Emotion(
    val id: Long = 0,
    val nameResId: Int = 0, // Resource ID for localized name
    val name: String = "", // Custom name for user-created emotions
    val emoji: String,
    val descriptionResId: Int = 0, // Resource ID for localized description
    val description: String = "", // Custom description for user-created emotions
    val isUserCreated: Boolean = false,
    val isActive: Boolean = true,
    val createdAt: Instant = Instant.now(),
    val sortOrder: Int = Int.MAX_VALUE
) {
    /**
     * Get the display name for this emotion
     * Returns localized name for predefined emotions or custom name for user-created ones
     */
    fun getDisplayName(stringProvider: (Int) -> String): String {
        return if (isUserCreated || nameResId == 0) {
            name
        } else {
            stringProvider(nameResId)
        }
    }

    /**
     * Get the display description for this emotion
     * Returns localized description for predefined emotions or custom description for user-created ones
     */
    fun getDisplayDescription(stringProvider: (Int) -> String): String {
        return if (isUserCreated || descriptionResId == 0) {
            description
        } else {
            stringProvider(descriptionResId)
        }
    }
}
