package com.lianglliu.hermoodbarometer.ui.screen.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lianglliu.hermoodbarometer.domain.model.TimeRange
import com.lianglliu.hermoodbarometer.domain.usecase.GetEmotionStatisticsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 统计页面的ViewModel
 * 负责处理情绪统计数据的业务逻辑
 */
@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val getEmotionStatisticsUseCase: GetEmotionStatisticsUseCase
) : ViewModel() {

    // UI状态
    private val _uiState = MutableStateFlow(StatisticsUiState())
    val uiState: StateFlow<StatisticsUiState> = _uiState.asStateFlow()

    init {
        // 初始化时加载默认时间范围的数据
        loadStatistics(TimeRange.LAST_WEEK)
    }

    /**
     * 加载统计数据
     * @param timeRange 时间范围
     */
    fun loadStatistics(timeRange: TimeRange) {
        _uiState.value = _uiState.value.copy(
            selectedTimeRange = timeRange,
            isLoading = true,
            errorMessage = null
        )

        viewModelScope.launch {
            try {
                getEmotionStatisticsUseCase(timeRange).collect { statistics ->
                    _uiState.value = _uiState.value.copy(
                        statistics = statistics,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "Failed to load statistics"
                )
            }
        }
    }

    /**
     * 更新选中的时间范围
     */
    fun updateTimeRange(timeRange: TimeRange) {
        if (_uiState.value.selectedTimeRange != timeRange) {
            loadStatistics(timeRange)
        }
    }

    /**
     * 更新选中的图表类型
     */
    fun updateChartType(chartType: ChartType) {
        _uiState.value = _uiState.value.copy(selectedChartType = chartType)
    }

    /**
     * 清除错误消息
     */
    fun clearErrorMessage() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}

/**
 * 统计页面的UI状态
 */
data class StatisticsUiState(
    val selectedTimeRange: TimeRange = TimeRange.LAST_WEEK,
    val selectedChartType: ChartType = ChartType.BAR,
    val statistics: com.lianglliu.hermoodbarometer.domain.usecase.EmotionStatistics? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

/**
 * 图表类型枚举
 */
enum class ChartType {
    BAR,    // 柱状图
    LINE,   // 折线图
    PIE     // 饼图
} 