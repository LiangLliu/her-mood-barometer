package com.lianglliu.hermoodbarometer.core.domain

import com.lianglliu.hermoodbarometer.core.model.data.EmotionRecord
import com.lianglliu.hermoodbarometer.repository.EmotionRepository
import jakarta.inject.Inject

/**
 * Use case for adding emotion records Validates data and delegates to repository for persistence
 */
class AddEmotionRecordUseCase
@Inject
constructor(private val emotionRepository: EmotionRepository) {

    /**
     * Execute the add emotion record operation
     *
     * @param emotionRecord The emotion record to add
     * @return Result containing the record ID on success
     */
    suspend operator fun invoke(emotionRecord: EmotionRecord): Result<Long> {
        return try {
            // Validate record data
            if (!isValidRecord(emotionRecord)) {
                return Result.failure(IllegalArgumentException("Invalid emotion record data"))
            }

            // Save record and return the generated ID
            val recordId = emotionRepository.insertRecord(emotionRecord)
            Result.success(recordId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /** Validate emotion record data */
    private fun isValidRecord(record: EmotionRecord): Boolean {
        return record.emotionId > 0 &&
            record.emotionEmoji.isNotBlank() &&
            record.intensity.level in 1..5
    }
}
