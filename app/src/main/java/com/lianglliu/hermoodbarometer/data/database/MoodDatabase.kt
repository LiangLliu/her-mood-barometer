package com.lianglliu.hermoodbarometer.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
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
    version = 3,
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
        val MIGRATIONS = arrayOf<Migration>(
            /**
             * 从版本2到版本3的迁移
             * 重构情绪数据结构，移除EmotionType枚举，改为直接存储emoji和名称
             */
            object : Migration(2, 3) {
                override fun migrate(database: SupportSQLiteDatabase) {
                    // 由于字段结构变化较大，使用重建表的方式
                    
                    // 创建新的emotion_records表
                    database.execSQL("""
                        CREATE TABLE emotion_records_new (
                            id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                            emotionId TEXT NOT NULL,
                            emotionName TEXT NOT NULL,
                            emotionEmoji TEXT NOT NULL,
                            intensity INTEGER NOT NULL,
                            note TEXT NOT NULL DEFAULT '',
                            timestamp TEXT NOT NULL,
                            isCustomEmotion INTEGER NOT NULL DEFAULT 0,
                            customEmotionId INTEGER
                        )
                    """)
                    
                    // 创建索引
                    database.execSQL("CREATE INDEX index_emotion_records_timestamp ON emotion_records_new(timestamp)")
                    database.execSQL("CREATE INDEX index_emotion_records_emotionId ON emotion_records_new(emotionId)")
                    database.execSQL("CREATE INDEX index_emotion_records_customEmotionId ON emotion_records_new(customEmotionId)")
                    
                    // 数据迁移：将旧数据转换为新格式
                    database.execSQL("""
                        INSERT INTO emotion_records_new (id, emotionId, emotionName, emotionEmoji, intensity, note, timestamp, isCustomEmotion, customEmotionId)
                        SELECT 
                            er.id,
                            CASE 
                                WHEN er.customEmotionId IS NOT NULL THEN 'custom_' || er.customEmotionId
                                ELSE LOWER(er.emotionType)
                            END as emotionId,
                            CASE 
                                WHEN er.customEmotionId IS NOT NULL THEN COALESCE(ce.name, '自定义情绪')
                                WHEN er.emotionType = 'HAPPY' THEN '开心'
                                WHEN er.emotionType = 'SAD' THEN '难过'
                                WHEN er.emotionType = 'ANGRY' THEN '愤怒'
                                WHEN er.emotionType = 'ANXIOUS' THEN '焦虑'
                                WHEN er.emotionType = 'CALM' THEN '平静'
                                WHEN er.emotionType = 'EXCITED' THEN '兴奋'
                                WHEN er.emotionType = 'TIRED' THEN '疲惫'
                                WHEN er.emotionType = 'CONFUSED' THEN '困惑'
                                WHEN er.emotionType = 'GRATEFUL' THEN '感恩'
                                WHEN er.emotionType = 'LONELY' THEN '孤独'
                                ELSE er.emotionType
                            END as emotionName,
                            CASE 
                                WHEN er.customEmotionId IS NOT NULL THEN COALESCE(ce.emoji, '😊')
                                WHEN er.emotionType = 'HAPPY' THEN '😊'
                                WHEN er.emotionType = 'SAD' THEN '😢'
                                WHEN er.emotionType = 'ANGRY' THEN '😡'
                                WHEN er.emotionType = 'ANXIOUS' THEN '😰'
                                WHEN er.emotionType = 'CALM' THEN '😌'
                                WHEN er.emotionType = 'EXCITED' THEN '🤩'
                                WHEN er.emotionType = 'TIRED' THEN '😴'
                                WHEN er.emotionType = 'CONFUSED' THEN '😕'
                                WHEN er.emotionType = 'GRATEFUL' THEN '🙏'
                                WHEN er.emotionType = 'LONELY' THEN '😔'
                                ELSE '😊'
                            END as emotionEmoji,
                            er.intensity,
                            er.note,
                            er.timestamp,
                            CASE WHEN er.customEmotionId IS NOT NULL THEN 1 ELSE 0 END as isCustomEmotion,
                            er.customEmotionId
                        FROM emotion_records er
                        LEFT JOIN custom_emotions ce ON er.customEmotionId = ce.id
                    """)
                    
                    // 删除旧表
                    database.execSQL("DROP TABLE emotion_records")
                    
                    // 重命名新表
                    database.execSQL("ALTER TABLE emotion_records_new RENAME TO emotion_records")
                }
            }
        )
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