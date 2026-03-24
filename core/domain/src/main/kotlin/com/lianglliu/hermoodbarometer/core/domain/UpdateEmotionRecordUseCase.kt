package com.lianglliu.hermoodbarometer.core.domain

import com.lianglliu.hermoodbarometer.core.model.data.EmotionRecord
import com.lianglliu.hermoodbarometer.repository.EmotionRepository
import jakarta.inject.Inject

/** Use case for updating emotion records Validates and updates existing emotion records */
class UpdateEmotionRecordUseCase
@Inject
constructor(private val emotionRepository: EmotionRepository) {

    /**
     * Execute the update emotion record operation
     *
     * @param emotionRecord The emotion record to update
     * @return Result indicating success or failure
     */
    suspend operator fun invoke(emotionRecord: EmotionRecord): Result<Unit> {
        return try {
            // Validate that the record has an ID
            if (emotionRecord.id <= 0) {
                return Result.failure(IllegalArgumentException("Invalid record ID"))
            }

            // Validate record data
            if (!isValidRecord(emotionRecord)) {
                return Result.failure(IllegalArgumentException("Invalid emotion record data"))
            }

            // Update the record
            emotionRepository.updateRecord(emotionRecord)
            Result.success(Unit)
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
