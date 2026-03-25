package com.lianglliu.hermoodbarometer.feature.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lianglliu.hermoodbarometer.core.common.concurrency.AppDispatchers
import com.lianglliu.hermoodbarometer.core.common.concurrency.Dispatcher
import com.lianglliu.hermoodbarometer.core.domain.GetEmotionStatisticsUseCase
import com.lianglliu.hermoodbarometer.core.model.data.EmotionStatistics
import com.lianglliu.hermoodbarometer.core.model.data.TimeRange
import com.lianglliu.hermoodbarometer.core.model.data.statistics.EmotionRecordFilter
import com.lianglliu.hermoodbarometer.repository.EmotionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import timber.log.Timber

/** 统计页面的ViewModel 负责处理情绪统计数据的业务逻辑 */
@HiltViewModel
class StatisticsViewModel
@Inject
constructor(
    private val getEmotionStatisticsUseCase: GetEmotionStatisticsUseCase,
    emotionRepository: EmotionRepository,
    @Dispatcher(AppDispatchers.Default) private val defaultDispatcher: CoroutineDispatcher,
) : ViewModel() {

    init {
        Timber.d("Init StatisticsViewModel")
    }

    private val selectedTimeRangeEnumState = MutableStateFlow(TimeRange.LAST_WEEK)

    val statisticsUiState: StateFlow<StatisticsUiState> =
        combine(
                selectedTimeRangeEnumState,
                // Record count as data-change trigger: any insert/delete causes re-emission
                emotionRepository.getRecordCount(),
            ) { timeRange, _ ->
                timeRange
            }
            .flatMapLatest { timeRange ->
                // Fresh time bounds computed NOW, not frozen at init
                val now = LocalDateTime.now()
                val startDateTime = timeRange.getStartDateTime()
                val endDateTime = now
                val filter =
                    EmotionRecordFilter(startDateTime = startDateTime, endDateTime = endDateTime)

                val startTime = System.currentTimeMillis()
                val durationDays =
                    ChronoUnit.DAYS.between(startDateTime, endDateTime).coerceAtLeast(1)
                val previousEnd = startDateTime.minusSeconds(1)
                val previousStart = previousEnd.minusDays(durationDays)

                combine(
                        getEmotionStatisticsUseCase(startDateTime, endDateTime),
                        getEmotionStatisticsUseCase(previousStart, previousEnd),
                    ) { currentStats, prevStats ->
                        Timber.d(
                            "Statistics loaded: ${currentStats.totalRecords} records (${System.currentTimeMillis() - startTime}ms)"
                        )
                        StatisticsUiState.Success(
                            emotionRecordFilter = filter,
                            statistics = currentStats,
                            previousStatistics = prevStats,
                            timeRange = timeRange,
                            customStartDate = filter.startDateTime.toLocalDate(),
                            customEndDate = filter.endDateTime.toLocalDate(),
                        ) as StatisticsUiState
                    }
                    .onStart { emit(StatisticsUiState.Loading) }
                    .catch { error ->
                        Timber.e("Error loading statistics: ${error.message}")
                        emit(StatisticsUiState.Loading)
                    }
            }
            .flowOn(defaultDispatcher)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = StatisticsUiState.Loading,
            )

    /** 更新选中的时间范围 */
    fun updateTimeRange(timeRange: TimeRange) {
        selectedTimeRangeEnumState.value = timeRange
    }
}

sealed interface StatisticsUiState {

    data object Loading : StatisticsUiState

    data class Success(
        val emotionRecordFilter: EmotionRecordFilter,
        val statistics: EmotionStatistics,
        val previousStatistics: EmotionStatistics? = null,
        val timeRange: TimeRange,
        val customStartDate: LocalDate,
        val customEndDate: LocalDate,
    ) : StatisticsUiState
}

/** Chart type enumeration */
enum class ChartType {
    BAR, // Bar chart
    LINE, // Line chart
}
