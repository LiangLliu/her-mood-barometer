package com.lianglliu.hermoodbarometer.data.database

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.LocalDateTime

/**
 * 情绪记录数据库实体
 * 
 * @property id 记录ID，主键，自增
 * @property emotionId 情绪ID（关联emotions表的id）
 * @property emotionName 情绪名称
 * @property emotionEmoji 情绪表情符号
 * @property intensity 情绪强度（1-5）
 * @property note 备注信息
 * @property timestamp 记录时间戳
 */
@Entity(
    tableName = "emotion_records",
    indices = [
        Index(value = ["timestamp"]), // 时间戳索引，用于时间范围查询
        Index(value = ["emotionId"]) // 情绪ID索引，用于统计查询
    ]
)
data class EmotionRecordEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val emotionId: Long, // 情绪ID
    val emotionName: String, // 情绪名称
    val emotionEmoji: String, // 表情符号
    val intensity: Int,
    val note: String = "",
    val timestamp: LocalDateTime
) 