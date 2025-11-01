package com.lianglliu.hermoodbarometer.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.Instant

/**
 * Unified emotion database entity
 * Stores both predefined and user-created emotions
 */
@Entity(
    tableName = "emotions",
    indices = [
        Index(value = ["isActive", "isUserCreated", "sortOrder"]), // For efficient querying
        Index(value = ["name"], unique = false) // For name-based lookups
    ]
)
data class EmotionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    // For predefined emotions: stores resource key (e.g., "happy")
    // For user emotions: stores custom name
    val name: String,

    val emoji: String,

    // Optional description for user-created emotions
    val description: String = "",

    val isUserCreated: Boolean = false,
    val isActive: Boolean = true,
    val sortOrder: Int = Int.MAX_VALUE,
    val createdAt: Instant = Instant.now()
)