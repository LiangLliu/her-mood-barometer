package com.lianglliu.hermoodbarometer.data.database

import androidx.room.Entity
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
@Entity(tableName = "emotion_records")
data class EmotionRecordEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val emotionType: String,
    val intensity: Int,
    val note: String = "",
    val timestamp: LocalDateTime,
    val customEmotionId: Long? = null
) 