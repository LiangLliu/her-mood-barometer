package com.lianglliu.hermoodbarometer.feature.statistics

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lianglliu.hermoodbarometer.core.ui.component.ErrorCard
import com.lianglliu.hermoodbarometer.core.ui.component.ScreenContainer
import com.lianglliu.hermoodbarometer.feature.statistics.components.EmotionBarChartCard
import com.lianglliu.hermoodbarometer.feature.statistics.components.EmotionComparisonExplanation
import com.lianglliu.hermoodbarometer.feature.statistics.components.EmotionDistributionExplanation
import com.lianglliu.hermoodbarometer.feature.statistics.components.EmotionLineChartCard
import com.lianglliu.hermoodbarometer.feature.statistics.components.EmotionPatternsExplanation
import com.lianglliu.hermoodbarometer.feature.statistics.components.EmotionTrendExplanation
import com.lianglliu.hermoodbarometer.feature.statistics.components.StatisticsCard
import com.lianglliu.hermoodbarometer.feature.statistics.components.TimeRangeSelector
import com.lianglliu.hermoodbarometer.core.locales.R

/**
 * 统计页面
 * 显示情绪数据的统计分析
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsScreen(
    viewModel: StatisticsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // 处理错误消息
    LaunchedEffect(uiState.errorMessage) {
        if (uiState.errorMessage != null) {
            // 可以在这里显示Snackbar或其他错误提示
        }
    }

    ScreenContainer(
        title = stringResource(R.string.statistics)
    ) {
        item {
            TimeRangeSelector(
                selectedTimeRange = uiState.selectedTimeRange,
                onTimeRangeChanged = { viewModel.updateTimeRange(it) },
                customStartDate = uiState.customStartDate,
                customEndDate = uiState.customEndDate,
                onCustomDateRangeChanged = { start, end ->
                    viewModel.updateCustomDateRange(start, end)
                }
            )
        }

        item {
            StatisticsCard(
                statistics = uiState.statistics,
                isLoading = uiState.isLoading
            )
        }

        item {
            EmotionBarChartCard(
                statistics = uiState.statistics
            )
        }

        item {
            EmotionDistributionExplanation()
        }

        item {
            EmotionLineChartCard(
                statistics = uiState.statistics
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

        if (uiState.errorMessage != null) {
            item {
                ErrorCard(message = uiState.errorMessage!!)
            }
        }
    }
}