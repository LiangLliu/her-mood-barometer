package com.lianglliu.hermoodbarometer.feature.diary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lianglliu.hermoodbarometer.core.model.data.EmotionRecord
import com.lianglliu.hermoodbarometer.repository.EmotionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

data class DiaryUiState(
    val isLoading: Boolean = false,
    val recordsByDate: Map<LocalDate, List<EmotionRecord>> = emptyMap(),
    val error: String? = null
)

@HiltViewModel
class DiaryViewModel @Inject constructor(
    private val emotionRepository: EmotionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DiaryUiState())
    val uiState: StateFlow<DiaryUiState> = _uiState.asStateFlow()

    init {
        loadAllRecords()
    }

    private fun loadAllRecords() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                emotionRepository.getAllRecords().collect { records ->
                    // Group records by date
                    val recordsByDate = records
                        .sortedByDescending { it.timestamp }
                        .groupBy { it.timestamp.toLocalDate() }

                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        recordsByDate = recordsByDate,
                        error = null
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }

    fun deleteRecord(recordId: Long) {
        viewModelScope.launch {
            try {
                emotionRepository.deleteRecord(recordId)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message
                )
            }
        }
    }
}