package com.lianglliu.hermoodbarometer.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import com.lianglliu.hermoodbarometer.domain.model.EmotionIntensity
import com.lianglliu.hermoodbarometer.domain.model.EmotionType
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * 心情数据库
 * 使用Room持久化库管理情绪记录和自定义情绪数据
 */
@Database(
    entities = [
        EmotionRecordEntity::class,
        CustomEmotionEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class MoodDatabase : RoomDatabase() {
    
    /**
     * 情绪记录数据访问对象
     */
    abstract fun emotionRecordDao(): EmotionRecordDao
    
    /**
     * 自定义情绪数据访问对象
     */
    abstract fun customEmotionDao(): CustomEmotionDao
    
    companion object {
        /**
         * 数据库名称
         */
        const val DATABASE_NAME = "mood_database"
        
        /**
         * 数据库迁移策略
         * 当数据库结构发生变化时，在这里添加迁移逻辑
         */
        val MIGRATIONS = arrayOf<Migration>()
    }
}

/**
 * 类型转换器
 * 用于Room数据库中的自定义类型转换
 */
class Converters {
    
    private val dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
    
    /**
     * LocalDateTime 转 String
     */
    @TypeConverter
    fun fromLocalDateTime(value: LocalDateTime?): String? {
        return value?.format(dateTimeFormatter)
    }
    
    /**
     * String 转 LocalDateTime
     */
    @TypeConverter
    fun toLocalDateTime(value: String?): LocalDateTime? {
        return value?.let { LocalDateTime.parse(it, dateTimeFormatter) }
    }
    
    /**
     * EmotionType 转 String
     */
    @TypeConverter
    fun fromEmotionType(value: EmotionType?): String? {
        return value?.name
    }
    
    /**
     * String 转 EmotionType
     */
    @TypeConverter
    fun toEmotionType(value: String?): EmotionType? {
        return value?.let { EmotionType.valueOf(it) }
    }
    
    /**
     * EmotionIntensity 转 Int
     */
    @TypeConverter
    fun fromEmotionIntensity(value: EmotionIntensity?): Int? {
        return value?.level
    }
    
    /**
     * Int 转 EmotionIntensity
     */
    @TypeConverter
    fun toEmotionIntensity(value: Int?): EmotionIntensity? {
        return value?.let { EmotionIntensity.fromLevel(it) }
    }
} 