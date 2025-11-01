package com.lianglliu.hermoodbarometer.core.domain

import com.lianglliu.hermoodbarometer.repository.EmotionRepository
import javax.inject.Inject

/**
 * Use case for deleting emotion records
 * Handles single and batch deletion of emotion records
 */
class DeleteEmotionRecordUseCase @Inject constructor(
    private val emotionRepository: EmotionRepository
) {

    /**
     * Delete a single emotion record by ID
     * @param recordId The ID of the record to delete
     * @return Result indicating success or failure
     */
    suspend operator fun invoke(recordId: Long): Result<Unit> {
        return try {
            if (recordId <= 0) {
                return Result.failure(IllegalArgumentException("Invalid record ID"))
            }

            emotionRepository.deleteRecord(recordId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Delete multiple emotion records
     * @param recordIds List of record IDs to delete
     * @return Result indicating success or failure
     */
    suspend fun deleteMultiple(recordIds: List<Long>): Result<Unit> {
        return try {
            if (recordIds.isEmpty()) {
                return Result.failure(IllegalArgumentException("No records specified for deletion"))
            }

            if (recordIds.any { it <= 0 }) {
                return Result.failure(IllegalArgumentException("Invalid record IDs"))
            }

            // Delete records one by one
            // Could be optimized with batch delete in the future
            recordIds.forEach { recordId ->
                emotionRepository.deleteRecord(recordId)
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Delete all emotion records
     * Use with extreme caution - this operation cannot be undone
     * @return Result indicating success or failure
     */
    suspend fun deleteAll(): Result<Unit> {
        return try {
            emotionRepository.deleteAllRecords()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}