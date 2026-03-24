package com.lianglliu.hermoodbarometer.core.domain

import com.lianglliu.hermoodbarometer.core.model.data.EmotionRecord
import com.lianglliu.hermoodbarometer.core.model.data.TimeRange
import com.lianglliu.hermoodbarometer.repository.EmotionRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

/** Use case for retrieving emotion records Handles various query scenarios for emotion records */
class GetEmotionRecordsUseCase
@Inject
constructor(private val emotionRepository: EmotionRepository) {

    /**
     * Get all emotion records
     *
     * @return Flow of emotion records
     */
    operator fun invoke(): Flow<List<EmotionRecord>> {
        return emotionRepository.getAllRecords()
    }

    /**
     * Get emotion records by time range
     *
     * @param timeRange Time range filter
     * @return Flow of emotion records within the specified time range
     */
    operator fun invoke(timeRange: TimeRange): Flow<List<EmotionRecord>> {
        return emotionRepository.getRecordsByTimeRange(timeRange)
    }

    /**
     * Get emotion records by emotion ID
     *
     * @param emotionId The ID of the emotion type
     * @return Flow of emotion records for the specified emotion
     */
    fun byEmotionId(emotionId: Long): Flow<List<EmotionRecord>> {
        return emotionRepository.getRecordsByEmotionId(emotionId)
    }

    /**
     * Get recent emotion records
     *
     * @param limit Number of recent records to retrieve
     * @return Flow of recent emotion records
     */
    fun getRecent(limit: Int = 10): Flow<List<EmotionRecord>> {
        return emotionRepository.getRecentRecords(limit)
    }

    /**
     * Search emotion records by note content
     *
     * @param query Search query for note content
     * @return Flow of matching emotion records
     */
    fun searchByNote(query: String): Flow<List<EmotionRecord>> {
        return emotionRepository.searchRecordsByNote(query)
    }
}
