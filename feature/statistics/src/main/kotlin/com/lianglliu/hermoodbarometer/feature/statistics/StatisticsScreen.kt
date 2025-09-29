package com.lianglliu.hermoodbarometer.feature.statistics

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lianglliu.hermoodbarometer.core.locales.R
import com.lianglliu.hermoodbarometer.core.model.data.TimeRange
import com.lianglliu.hermoodbarometer.core.ui.component.LoadingState
import com.lianglliu.hermoodbarometer.core.ui.component.ScreenContainer
import com.lianglliu.hermoodbarometer.feature.statistics.components.EmotionBarChartCard
import com.lianglliu.hermoodbarometer.feature.statistics.components.EmotionComparisonExplanation
import com.lianglliu.hermoodbarometer.feature.statistics.components.EmotionDistributionExplanation
import com.lianglliu.hermoodbarometer.feature.statistics.components.EmotionLineChartCard
import com.lianglliu.hermoodbarometer.feature.statistics.components.EmotionPatternsExplanation
import com.lianglliu.hermoodbarometer.feature.statistics.components.EmotionTrendExplanation
import com.lianglliu.hermoodbarometer.feature.statistics.components.StatisticsCard
import com.lianglliu.hermoodbarometer.feature.statistics.components.TimeRangeSelector
import java.time.LocalDate

/**
 * 统计页面
 * 显示情绪数据的统计分析
 */
@Composable
fun StatisticsScreen(
    viewModel: StatisticsViewModel = hiltViewModel()
) {
    val uiState by viewModel.statisticsUiState.collectAsStateWithLifecycle()

    StatisticsScreen(
        statisticsUiState = uiState,
        updateTimeRange = viewModel::updateTimeRange,
        updateCustomDateRange = viewModel::updateCustomDateRange,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun StatisticsScreen(
    statisticsUiState: StatisticsUiState,
    updateTimeRange: (TimeRange) -> Unit,
    updateCustomDateRange: (startDate: LocalDate, endDate: LocalDate) -> Unit,
) {
    ScreenContainer(
        title = stringResource(R.string.statistics)
    ) {

        when (statisticsUiState) {
            StatisticsUiState.Loading -> {
                item {
                    LoadingState(
                        modifier = Modifier
                            .fillMaxSize(),
                    )
                }
            }

            is StatisticsUiState.Success -> {
                item {
                    TimeRangeSelector(
                        selectedTimeRange = statisticsUiState.timeRange,
                        customStartDate = statisticsUiState.emotionRecordFilter.startDateTime.toLocalDate(),
                        customEndDate = statisticsUiState.emotionRecordFilter.endDateTime.toLocalDate(),
                        onTimeRangeChanged = { updateTimeRange(it) },
                        onCustomDateRangeChanged = { startDate, endDate -> updateCustomDateRange(startDate, endDate)}
                    )
                }

                item {
                    StatisticsCard(
                        statistics = statisticsUiState.statistics,
                    )
                }

                item {
                    EmotionBarChartCard(
                        statistics = statisticsUiState.statistics
                    )
                }

                item {
                    EmotionDistributionExplanation()
                }

                item {
                    EmotionLineChartCard(
                        statistics = statisticsUiState.statistics
                    )
                }

                item {
                    EmotionTrendExplanation()
                }

                item {
                    EmotionPatternsExplanation()
                }

                item {
                    EmotionComparisonExplanation()
                }

            }
        }
    }
}