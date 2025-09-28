package com.lianglliu.hermoodbarometer.core.domain


import com.lianglliu.hermoodbarometer.core.model.data.EmotionRecord
import com.lianglliu.hermoodbarometer.model.TimeRange
import com.lianglliu.hermoodbarometer.repository.EmotionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * 获取情绪记录的用例
 * 负责从Repository获取情绪记录数据
 */
class GetEmotionRecordsUseCase @Inject constructor(
    private val emotionRepository: EmotionRepository
) {
    
    /**
     * 获取所有情绪记录
     * @return 情绪记录流
     */
    operator fun invoke(): Flow<List<EmotionRecord>> {
        return emotionRepository.getAllEmotionRecords()
    }
    
    /**
     * 根据时间范围获取情绪记录
     * @param timeRange 时间范围
     * @return 指定时间范围内的情绪记录流
     */
    operator fun invoke(timeRange: TimeRange): Flow<List<EmotionRecord>> {
        return emotionRepository.getEmotionRecordsByTimeRange(timeRange)
    }
    
    /**
     * 根据情绪类型获取记录
     * @param emotionType 情绪类型
     * @return 指定情绪类型的记录流
     */
    operator fun invoke(emotionType: String): Flow<List<EmotionRecord>> {
        return emotionRepository.getEmotionRecordsByType(emotionType)
    }
} 