package com.lianglliu.hermoodbarometer.domain.usecase

import com.lianglliu.hermoodbarometer.domain.model.EmotionRecord
import com.lianglliu.hermoodbarometer.domain.model.TimeRange
import com.lianglliu.hermoodbarometer.domain.repository.EmotionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
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
                averageIntensityByEmotion = emptyMap(),
                countsByEmotion = emptyMap(),
                chartLabelMapping = emptyMap(),
                dailyAverageIntensity = emptyList()
            )
        }
        
        // 计算总记录数
        val totalRecords = records.size
        
        // 计算平均强度
        val averageIntensity = records.map { it.intensity.toDouble() }.average().toFloat()
        
        // 计算最频繁的情绪类型（使用表情符号+名称作为显示标签）
        val emotionCounts = records.groupBy { record ->
            record.getDisplayText() // 使用 "😊 开心" 格式
        }
            .mapValues { it.value.size }
        val mostFrequentEmotion = emotionCounts.maxByOrNull { it.value }?.key
        
        // 计算情绪分布
        val emotionDistribution = emotionCounts.mapValues { it.value.toFloat() / totalRecords }
        
        // 计算每种情绪的平均强度
        val averageIntensityByEmotion = records
            .groupBy { record ->
                record.getDisplayText() // 使用 "😊 开心" 格式
            }
            .mapValues { entry -> entry.value.map { record -> record.intensity.toDouble() }.average().toFloat() }
            
        // 为图表生成简化的表情符号映射
        val chartLabelMapping = records.groupBy { it.getDisplayText() }
            .mapValues { it.value.first().getChartLabel() } // 获取表情符号用于图表显示

        // 按日聚合：平均强度
        val dailyAverageIntensity: List<DailyPoint> = records
            .groupBy { it.timestamp.toLocalDate() }
            .toSortedMap()
            .map { (date, list) ->
                val avg = list.map { it.intensity.toDouble() }.average().toFloat()
                DailyPoint(date = date, value = avg)
            }
        
        return EmotionStatistics(
            totalRecords = totalRecords,
            averageIntensity = averageIntensity,
            mostFrequentEmotion = mostFrequentEmotion,
            emotionDistribution = emotionDistribution,
            averageIntensityByEmotion = averageIntensityByEmotion,
            countsByEmotion = emotionCounts,
            chartLabelMapping = chartLabelMapping,
            dailyAverageIntensity = dailyAverageIntensity
        )
    }
}

/**
 * 情绪统计数据模型
 */
data class EmotionStatistics(
    val totalRecords: Int,
    val averageIntensity: Float,
    val mostFrequentEmotion: String?, // 最频繁的情绪（表情符号+名称格式）
    val emotionDistribution: Map<String, Float>, // 情绪分布（表情符号+名称 -> 比例）
    val averageIntensityByEmotion: Map<String, Float>, // 每种情绪的平均强度（表情符号+名称 -> 强度）
    val countsByEmotion: Map<String, Int>, // 每种情绪的计数（表情符号+名称 -> 计数）
    val chartLabelMapping: Map<String, String>, // 图表标签映射（表情符号+名称 -> 表情符号）
    val dailyAverageIntensity: List<DailyPoint>
)

/**
 * 折线图时间序列点（按日）
 */
data class DailyPoint(
    val date: LocalDate,
    val value: Float
)