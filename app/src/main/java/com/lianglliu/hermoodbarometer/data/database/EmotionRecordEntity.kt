package com.lianglliu.hermoodbarometer.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.lianglliu.hermoodbarometer.domain.model.EmotionIntensity
import com.lianglliu.hermoodbarometer.domain.model.EmotionType
import java.time.LocalDateTime

/**
 * 情绪记录数据库实体
 * 
 * @property id 记录ID，主键，自增
 * @property emotionType 情绪类型
 * @property intensity 情绪强度
 * @property note 备注信息
 * @property timestamp 记录时间戳
 * @property customEmotionId 自定义情绪ID（如果使用自定义情绪）
 */
@Entity(tableName = "emotion_records")
data class EmotionRecordEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val emotionType: EmotionType,
    val intensity: EmotionIntensity,
    val note: String = "",
    val timestamp: LocalDateTime,
    val customEmotionId: Long? = null
) 