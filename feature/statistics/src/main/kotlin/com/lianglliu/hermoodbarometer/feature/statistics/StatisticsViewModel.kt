package com.lianglliu.hermoodbarometer.feature.statistics

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lianglliu.hermoodbarometer.core.domain.EmotionStatistics
import com.lianglliu.hermoodbarometer.core.domain.GetEmotionStatisticsUseCase
import com.lianglliu.hermoodbarometer.core.model.data.TimeRange
import com.lianglliu.hermoodbarometer.core.model.data.statistics.EmotionRecordFilter
import com.lianglliu.hermoodbarometer.core.network.AppDispatchers
import com.lianglliu.hermoodbarometer.core.network.Dispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import java.time.LocalDate
import javax.inject.Inject

/**
 * 统计页面的ViewModel
 * 负责处理情绪统计数据的业务逻辑
 */
@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val getEmotionStatisticsUseCase: GetEmotionStatisticsUseCase,
    @Dispatcher(AppDispatchers.Default) private val defaultDispatcher: CoroutineDispatcher,
) : ViewModel() {

    init {
        Log.d("StatisticsVM", "Init StatisticsViewModel")
    }

    private val emotionRecordFilterState = MutableStateFlow(
        EmotionRecordFilter(
            startDateTime = TimeRange.LAST_WEEK.getStartDateTime(),
            endDateTime = TimeRange.LAST_WEEK.getEndDateTime()
        )
    )

    private val selectedTimeRangeEnumState = MutableStateFlow(TimeRange.LAST_WEEK)

    val statisticsUiState: StateFlow<StatisticsUiState> = combine(
        emotionRecordFilterState,
        selectedTimeRangeEnumState
    ) { filter, timeRange ->
        Pair(filter, timeRange)
    }
        .flatMapLatest { (filter, timeRange) ->
            val startTime = System.currentTimeMillis()

            getEmotionStatisticsUseCase(filter)
                .map { statistics ->
                    Log.d("StatisticsVM", "Statistics loaded: ${statistics.totalRecords} records (${System.currentTimeMillis() - startTime}ms)")
                    StatisticsUiState.Success(
                        emotionRecordFilter = filter,
                        statistics = statistics,
                        timeRange = timeRange,
                        customStartDate = filter.startDateTime.toLocalDate(),
                        customEndDate = filter.endDateTime.toLocalDate()
                    ) as StatisticsUiState
                }
                .onStart {
                    emit(StatisticsUiState.Loading)
                }
                .catch { error ->
                    Log.e("StatisticsVM", "Error loading statistics: ${error.message}")
                    emit(StatisticsUiState.Loading)
                }
        }
        .flowOn(defaultDispatcher)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = StatisticsUiState.Loading,
        )

    /**
     * 更新选中的时间范围
     */
    fun updateTimeRange(timeRange: TimeRange) {
        selectedTimeRangeEnumState.value = timeRange

        emotionRecordFilterState.update {
            it.copy(
                startDateTime = timeRange.getStartDateTime(),
                endDateTime = timeRange.getEndDateTime()
            )
        }
    }

    fun updateCustomDateRange(startDate: LocalDate, endDate: LocalDate) {
        selectedTimeRangeEnumState.value = TimeRange.CUSTOM
        emotionRecordFilterState.update {
            it.copy(
                startDateTime = startDate.atStartOfDay(),
                endDateTime = endDate.plusDays(1).atStartOfDay()
            )
        }
    }
}

sealed interface StatisticsUiState {

    data object Loading : StatisticsUiState

    data class Success(
        val emotionRecordFilter: EmotionRecordFilter,
        val statistics: EmotionStatistics,
        val timeRange: TimeRange,
        val customStartDate: LocalDate,
        val customEndDate: LocalDate,
    ) : StatisticsUiState
}

/**
 * 图表类型枚举
 */
enum class ChartType {
    BAR,    // 柱状图
    LINE    // 折线图
} 