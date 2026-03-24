package com.lianglliu.hermoodbarometer.feature.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lianglliu.hermoodbarometer.core.model.data.EmotionRecord
import com.lianglliu.hermoodbarometer.repository.EmotionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import java.time.LocalDate
import java.time.YearMonth
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@HiltViewModel
class CalendarViewModel @Inject constructor(private val emotionRepository: EmotionRepository) :
    ViewModel() {

    private val _currentYearMonth = MutableStateFlow(YearMonth.now())
    val currentYearMonth: StateFlow<YearMonth> = _currentYearMonth.asStateFlow()

    private val _selectedDate = MutableStateFlow<LocalDate?>(null)
    val selectedDate: StateFlow<LocalDate?> = _selectedDate.asStateFlow()

    private val _uiState = MutableStateFlow(CalendarUiState())
    val uiState: StateFlow<CalendarUiState> = _uiState.asStateFlow()

    val monthlyRecords: StateFlow<Map<LocalDate, List<EmotionRecord>>> =
        currentYearMonth
            .flatMapLatest { yearMonth ->
                emotionRepository.getRecordsByMonth(yearMonth.year, yearMonth.monthValue).map {
                    records ->
                    records.groupBy { it.getLocalDateTime().toLocalDate() }
                }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyMap(),
            )

    val selectedDateRecords: StateFlow<List<EmotionRecord>> =
        combine(selectedDate, monthlyRecords) { date, records ->
                date?.let { records[it] } ?: emptyList()
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList(),
            )

    init {
        loadCalendarData()
    }

    fun navigateToPreviousMonth() {
        _currentYearMonth.value = _currentYearMonth.value.minusMonths(1)
        _selectedDate.value = null
    }

    fun navigateToNextMonth() {
        _currentYearMonth.value = _currentYearMonth.value.plusMonths(1)
        _selectedDate.value = null
    }

    fun navigateToToday() {
        _currentYearMonth.value = YearMonth.now()
        _selectedDate.value = LocalDate.now()
    }

    fun selectDate(date: LocalDate) {
        _selectedDate.value = if (_selectedDate.value == date) null else date
    }

    fun deleteRecord(recordId: Long) {
        viewModelScope.launch {
            try {
                emotionRepository.deleteRecord(recordId)
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = e.message ?: "Failed to delete record") }
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    private fun loadCalendarData() {
        // Initial load is handled by the flows above
    }
}

data class CalendarUiState(val isLoading: Boolean = false, val errorMessage: String? = null)
