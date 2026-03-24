package com.lianglliu.hermoodbarometer.feature.diary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lianglliu.hermoodbarometer.core.model.data.EmotionRecord
import com.lianglliu.hermoodbarometer.repository.EmotionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import java.time.LocalDate
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface DiaryUiState {

    data object Loading : DiaryUiState

    data class Success(val groupedRecords: Map<LocalDate, List<EmotionRecord>> = emptyMap()) :
        DiaryUiState

    data class Error(val message: String) : DiaryUiState
}

@HiltViewModel
class DiaryViewModel @Inject constructor(private val emotionRepository: EmotionRepository) :
    ViewModel() {

    private val _uiState = MutableStateFlow<DiaryUiState>(DiaryUiState.Loading)
    val uiState: StateFlow<DiaryUiState> = _uiState.asStateFlow()

    init {
        loadAllRecords()
    }

    private fun loadAllRecords() {
        viewModelScope.launch {
            _uiState.value = DiaryUiState.Loading
            try {
                emotionRepository.getAllRecords().collect { records ->
                    // Group records by date
                    val groupedRecords =
                        records
                            .sortedByDescending { it.timestamp }
                            .groupBy { it.getLocalDateTime().toLocalDate() }

                    _uiState.value = DiaryUiState.Success(groupedRecords = groupedRecords)
                }
            } catch (e: Exception) {
                _uiState.value = DiaryUiState.Error(message = e.message ?: "Unknown error")
            }
        }
    }

    fun deleteRecord(recordId: Long) {
        viewModelScope.launch {
            try {
                emotionRepository.deleteRecord(recordId)
            } catch (e: Exception) {
                _uiState.value = DiaryUiState.Error(message = e.message ?: "Unknown error")
            }
        }
    }
}
