package com.lianglliu.hermoodbarometer.data.database

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.LocalDateTime

/**
 * 情绪记录数据库实体
 * 
 * @property id 记录ID，主键，自增
 * @property emotionType 情绪类型（字符串）
 * @property intensity 情绪强度（整数）
 * @property note 备注信息
 * @property timestamp 记录时间戳
 * @property customEmotionId 自定义情绪ID（如果使用自定义情绪）
 */
@Entity(
    tableName = "emotion_records",
    indices = [
        Index(value = ["timestamp"]), // 时间戳索引，用于时间范围查询
        Index(value = ["emotionType"]), // 情绪类型索引，用于统计查询
        Index(value = ["customEmotionId"]) // 自定义情绪ID索引
    ]
)
data class EmotionRecordEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val emotionType: String,
    val intensity: Int,
    val note: String = "",
    val timestamp: LocalDateTime,
    val customEmotionId: Long? = null
) 