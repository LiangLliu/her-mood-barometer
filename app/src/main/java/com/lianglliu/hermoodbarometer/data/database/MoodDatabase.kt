package com.lianglliu.hermoodbarometer.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * 心情数据库
 * 使用Room持久化库管理情绪记录和自定义情绪数据
 */
@Database(
    entities = [
        EmotionRecordEntity::class,
        EmotionEntity::class
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
     * 统一情绪数据访问对象
     */
    abstract fun emotionDao(): EmotionDao
    
    companion object {
        /**
         * 数据库名称
         */
        const val DATABASE_NAME = "mood_database"
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
    

} 