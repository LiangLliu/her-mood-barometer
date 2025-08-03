package com.lianglliu.hermoodbarometer.ui.screen.statistics

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lianglliu.hermoodbarometer.R
import com.lianglliu.hermoodbarometer.domain.model.TimeRange


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
            // 页面标题
            Text(
                text = stringResource(R.string.statistics),
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }

        item {
            // 时间范围选择
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = stringResource(R.string.time_range),
                        style = MaterialTheme.typography.titleMedium
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        TimeRange.values().forEach { timeRange ->
                            FilterChip(
                                selected = uiState.selectedTimeRange == timeRange,
                                onClick = { viewModel.updateTimeRange(timeRange) },
                                label = {
                                    Text(
                                        text = getTimeRangeDisplayName(timeRange),
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
        }

        item {
            // 图表类型选择
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = stringResource(R.string.chart_type),
                        style = MaterialTheme.typography.titleMedium
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        com.lianglliu.hermoodbarometer.ui.screen.statistics.ChartType.values().forEach { chartType ->
                            FilterChip(
                                selected = uiState.selectedChartType == chartType,
                                onClick = { viewModel.updateChartType(chartType) },
                                label = {
                                    Text(
                                        text = getChartTypeDisplayName(chartType),
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                },
                                leadingIcon = {
                                    Icon(
                                        imageVector = getChartTypeIcon(chartType),
                                        contentDescription = null
                                    )
                                },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
        }

        item {
            // 统计数据卡片
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = stringResource(R.string.mood_trends),
                        style = MaterialTheme.typography.titleMedium
                    )

                    if (uiState.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                    } else {
                        val statistics = uiState.statistics
                        if (statistics != null) {
                            StatisticItem(stringResource(R.string.total_records), statistics.totalRecords.toString())
                            StatisticItem(stringResource(R.string.average_mood), "%.1f".format(statistics.averageIntensity))
                            StatisticItem(stringResource(R.string.most_frequent_emotion), statistics.mostFrequentEmotion ?: stringResource(R.string.no_data))
                        } else {
                            StatisticItem(stringResource(R.string.total_records), "0")
                            StatisticItem(stringResource(R.string.average_mood), stringResource(R.string.no_data))
                            StatisticItem(stringResource(R.string.most_frequent_emotion), stringResource(R.string.no_data))
                        }
                    }
                }
            }
        }

        item {
            // 图表区域
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    val statistics = uiState.statistics
                    if (statistics != null && statistics.totalRecords > 0) {
                        // 渲染真实图表
                        EmotionChart(
                            statistics = statistics,
                            chartType = uiState.selectedChartType,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp)
                        )
                    } else {
                        // 显示空状态
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = getChartTypeIcon(uiState.selectedChartType),
                                contentDescription = null,
                                modifier = Modifier.size(48.dp)
                            )
                            Text(
                                text = stringResource(R.string.no_data),
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Text(
                                text = "开始记录情绪来查看统计图表",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StatisticItem(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

// ChartType 已移至 StatisticsViewModel 中定义

@Composable
private fun getTimeRangeDisplayName(timeRange: TimeRange): String {
    return when (timeRange) {
        TimeRange.LAST_WEEK -> stringResource(R.string.last_week)
        TimeRange.LAST_MONTH -> stringResource(R.string.last_month)
        TimeRange.LAST_3_MONTHS -> stringResource(R.string.last_3_months)
        TimeRange.LAST_YEAR -> stringResource(R.string.last_year)
    }
}

@Composable
private fun getChartTypeDisplayName(chartType: ChartType): String {
    return when (chartType) {
        ChartType.BAR -> stringResource(R.string.bar_chart)
        ChartType.LINE -> stringResource(R.string.line_chart)
        ChartType.PIE -> stringResource(R.string.pie_chart)
    }
}

private fun getChartTypeIcon(chartType: ChartType) = when (chartType) {
    ChartType.BAR -> Icons.Default.Info
    ChartType.LINE -> Icons.Default.Star
    ChartType.PIE -> Icons.Default.Favorite
}

/**
 * 情绪图表组件
 * 根据图表类型渲染不同的图表
 */
@Composable
private fun EmotionChart(
    statistics: com.lianglliu.hermoodbarometer.domain.usecase.EmotionStatistics,
    chartType: ChartType,
    modifier: Modifier = Modifier
) {
    // 暂时显示简单的数据可视化，后续集成Vico Charts
    when (chartType) {
        ChartType.BAR -> EmotionBarChart(statistics, modifier)
        ChartType.LINE -> EmotionLineChart(statistics, modifier)
        ChartType.PIE -> EmotionPieChart(statistics, modifier)
    }
}

/**
 * 情绪柱状图（简化版本）
 */
@Composable
private fun EmotionBarChart(
    statistics: com.lianglliu.hermoodbarometer.domain.usecase.EmotionStatistics,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "柱状图 - 情绪强度分布",
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.primary
        )
        
        if (statistics.averageIntensityByEmotion.isNotEmpty()) {
            statistics.averageIntensityByEmotion.entries.forEach { (emotion, intensity) ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = emotion,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.weight(1f)
                    )
                    LinearProgressIndicator(
                        progress = { (intensity / 5f).coerceIn(0f, 1f) },
                        modifier = Modifier
                            .weight(2f)
                            .height(8.dp)
                    )
                    Text(
                        text = "%.1f".format(intensity),
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        } else {
            Text(
                text = "暂无数据",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}

/**
 * 情绪折线图（简化版本）
 */
@Composable
private fun EmotionLineChart(
    statistics: com.lianglliu.hermoodbarometer.domain.usecase.EmotionStatistics,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "折线图 - 情绪强度趋势",
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.primary
        )
        
        if (statistics.averageIntensityByEmotion.isNotEmpty()) {
            statistics.averageIntensityByEmotion.entries.forEachIndexed { index, (emotion, intensity) ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${index + 1}",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = emotion,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.weight(2f)
                    )
                    Text(
                        text = "%.1f".format(intensity),
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.End
                    )
                }
            }
        } else {
            Text(
                text = "暂无数据",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}

/**
 * 情绪饼图（简化版本）
 */
@Composable
private fun EmotionPieChart(
    statistics: com.lianglliu.hermoodbarometer.domain.usecase.EmotionStatistics,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "饼图 - 情绪分布",
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.primary
        )
        
        if (statistics.emotionDistribution.isNotEmpty()) {
            statistics.emotionDistribution.entries.forEach { (emotion, percentage) ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = emotion,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.weight(1f)
                    )
                    LinearProgressIndicator(
                        progress = { percentage },
                        modifier = Modifier
                            .weight(2f)
                            .height(8.dp)
                    )
                    Text(
                        text = "%.1f%%".format(percentage * 100),
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        } else {
            Text(
                text = "暂无数据",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}