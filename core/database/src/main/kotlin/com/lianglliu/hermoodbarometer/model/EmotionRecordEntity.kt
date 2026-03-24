package com.lianglliu.hermoodbarometer.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.Instant

/**
 * Emotion record database entity Stores individual emotion recordings with references to emotions
 * table
 */
@Entity(
    tableName = "emotion_records",
    foreignKeys =
        [
            ForeignKey(
                entity = EmotionEntity::class,
                parentColumns = ["id"],
                childColumns = ["emotionId"],
                onDelete = ForeignKey.CASCADE,
            )
        ],
    indices =
        [
            Index(value = ["timestamp"]), // For time-based queries
            Index(value = ["emotionId"]), // For emotion-based statistics
            Index(value = ["emotionId", "timestamp"]), // For combined queries
            Index(value = ["intensity"]), // For intensity-based statistics
        ],
)
data class EmotionRecordEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,

    // Foreign key to emotions table
    val emotionId: Long,

    // Cached emoji for performance (avoids joins in many cases)
    val emotionEmoji: String,

    // Intensity as integer (1-5)
    val intensity: Int,

    // Optional user note
    val note: String = "",

    // Timestamp of the record
    val timestamp: Instant = Instant.now(),

    // Optional weather as string (enum name)
    val weather: String? = null,

    // Activities stored as comma-separated string
    val activities: String = "",
)
