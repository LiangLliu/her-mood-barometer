package com.lianglliu.hermoodbarometer

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.lianglliu.hermoodbarometer.dao.EmotionDao
import com.lianglliu.hermoodbarometer.dao.EmotionRecordDao
import com.lianglliu.hermoodbarometer.model.EmotionEntity
import com.lianglliu.hermoodbarometer.model.EmotionRecordEntity
import com.lianglliu.hermoodbarometer.util.InstantConverter

/**
 * Mood tracking database
 * Uses Room persistence library to manage emotion records and custom emotions
 */
@Database(
    entities = [
        EmotionRecordEntity::class,
        EmotionEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(InstantConverter::class)
abstract class MoodDatabase : RoomDatabase() {

    /**
     * Emotion record data access object
     */
    abstract fun emotionRecordDao(): EmotionRecordDao

    /**
     * Unified emotion data access object
     */
    abstract fun emotionDao(): EmotionDao

    companion object {
        /**
         * Database name constant
         */
        const val DATABASE_NAME = "mood_database"
    }
}