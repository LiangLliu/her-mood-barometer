package com.lianglliu.hermoodbarometer.ui.screen.statistics

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lianglliu.hermoodbarometer.R
import com.lianglliu.hermoodbarometer.ui.components.PageTitle
import com.lianglliu.hermoodbarometer.ui.screen.statistics.components.*


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

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            PageTitle(title = stringResource(R.string.statistics))
        }

        item {
            TimeRangeSelector(
                selectedTimeRange = uiState.selectedTimeRange,
                onTimeRangeChanged = { viewModel.updateTimeRange(it) }
            )
        }

        item {
            ChartTypeSelector(
                selectedChartType = uiState.selectedChartType,
                onChartTypeChanged = { viewModel.updateChartType(it) }
            )
        }

        item {
            StatisticsCard(
                statistics = uiState.statistics,
                isLoading = uiState.isLoading
            )
        }

        item {
            EmotionChartContainer(
                statistics = uiState.statistics,
                chartType = uiState.selectedChartType
            )
        }
    }
}

