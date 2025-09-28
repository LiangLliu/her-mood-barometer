package com.lianglliu.hermoodbarometer.core.domain

import com.lianglliu.hermoodbarometer.core.model.data.EmotionRecord
import com.lianglliu.hermoodbarometer.repository.EmotionRepository
import javax.inject.Inject

/**
 * 添加情绪记录的用例
 * 负责验证数据并调用Repository保存记录
 */
class AddEmotionRecordUseCase @Inject constructor(
    private val emotionRepository: EmotionRepository
) {
    
    /**
     * 执行添加情绪记录操作
     * @param emotionRecord 要添加的情绪记录
     * @return 操作结果，成功返回true，失败返回false
     */
    suspend operator fun invoke(emotionRecord: EmotionRecord): Result<Boolean> {
        return try {
            // 验证记录数据
            if (!isValidRecord(emotionRecord)) {
                return Result.failure(IllegalArgumentException("Invalid emotion record data"))
            }
            
            // 保存记录
            emotionRepository.addEmotionRecord(emotionRecord)
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * 验证情绪记录数据是否有效
     */
    private fun isValidRecord(record: EmotionRecord): Boolean {
        return record.emotionId > 0 && 
               record.emotionName.isNotBlank() &&
               record.emotionEmoji.isNotBlank() &&
               record.intensity in 1..5
    }
} 