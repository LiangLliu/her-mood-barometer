package com.lianglliu.hermoodbarometer.domain.usecase

import com.lianglliu.hermoodbarometer.domain.model.EmotionRecord
import com.lianglliu.hermoodbarometer.domain.model.TimeRange
import com.lianglliu.hermoodbarometer.domain.repository.EmotionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * 获取情绪统计数据的用例
 * 负责计算和提供各种统计数据
 */
class GetEmotionStatisticsUseCase @Inject constructor(
    private val emotionRepository: EmotionRepository
) {
    
    /**
     * 获取指定时间范围的统计数据
     * @param timeRange 时间范围
     * @return 统计数据流
     */
    operator fun invoke(timeRange: TimeRange): Flow<EmotionStatistics> {
        return emotionRepository.getEmotionRecordsByTimeRange(timeRange)
            .map { records -> calculateStatistics(records) }
    }
    
    /**
     * 计算统计数据
     */
    private fun calculateStatistics(records: List<EmotionRecord>): EmotionStatistics {
        if (records.isEmpty()) {
            return EmotionStatistics(
                totalRecords = 0,
                averageIntensity = 0f,
                mostFrequentEmotion = null,
                emotionDistribution = emptyMap(),
                averageIntensityByEmotion = emptyMap()
            )
        }
        
        // 计算总记录数
        val totalRecords = records.size
        
        // 计算平均强度
        val averageIntensity = records.map { it.intensity.toDouble() }.average().toFloat()
        
        // 计算最频繁的情绪类型
        val emotionCounts = records.groupBy { it.emotionType }
            .mapValues { it.value.size }
        val mostFrequentEmotion = emotionCounts.maxByOrNull { it.value }?.key
        
        // 计算情绪分布
        val emotionDistribution = emotionCounts.mapValues { it.value.toFloat() / totalRecords }
        
        // 计算每种情绪的平均强度
        val averageIntensityByEmotion = records.groupBy { it.emotionType }
            .mapValues { it.value.map { record -> record.intensity.toDouble() }.average().toFloat() }
        
        return EmotionStatistics(
            totalRecords = totalRecords,
            averageIntensity = averageIntensity,
            mostFrequentEmotion = mostFrequentEmotion,
            emotionDistribution = emotionDistribution,
            averageIntensityByEmotion = averageIntensityByEmotion
        )
    }
}

/**
 * 情绪统计数据模型
 */
data class EmotionStatistics(
    val totalRecords: Int,
    val averageIntensity: Float,
    val mostFrequentEmotion: String?,
    val emotionDistribution: Map<String, Float>,
    val averageIntensityByEmotion: Map<String, Float>
) 