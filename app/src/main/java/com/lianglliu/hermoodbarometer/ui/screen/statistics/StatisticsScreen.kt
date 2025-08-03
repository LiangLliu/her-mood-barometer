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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.lianglliu.hermoodbarometer.domain.model.TimeRange

/**
 * 统计页面
 * 显示情绪数据的统计分析
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsScreen() {
    var selectedTimeRange by remember { mutableStateOf(TimeRange.LAST_WEEK) }
    var selectedChartType by remember { mutableStateOf(ChartType.BAR_CHART) }
    
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            // 页面标题
            Text(
                text = "统计",
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
                        text = "时间范围",
                        style = MaterialTheme.typography.titleMedium
                    )
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        TimeRange.values().forEach { timeRange ->
                            FilterChip(
                                selected = selectedTimeRange == timeRange,
                                onClick = { selectedTimeRange = timeRange },
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
                        text = "图表类型",
                        style = MaterialTheme.typography.titleMedium
                    )
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        ChartType.values().forEach { chartType ->
                            FilterChip(
                                selected = selectedChartType == chartType,
                                onClick = { selectedChartType = chartType },
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
                        text = "统计概览",
                        style = MaterialTheme.typography.titleMedium
                    )
                    
                    StatisticItem("总记录数", "0")
                    StatisticItem("平均情绪", "暂无数据")
                    StatisticItem("最常出现的情绪", "暂无数据")
                }
            }
        }
        
        item {
            // 图表区域（占位符）
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = getChartTypeIcon(selectedChartType),
                            contentDescription = null,
                            modifier = Modifier.size(48.dp)
                        )
                        Text(
                            text = "暂无数据",
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

enum class ChartType {
    BAR_CHART,
    LINE_CHART,
    PIE_CHART
}

private fun getTimeRangeDisplayName(timeRange: TimeRange): String {
    return when (timeRange) {
        TimeRange.LAST_WEEK -> "最近一周"
        TimeRange.LAST_MONTH -> "最近一个月"
        TimeRange.LAST_3_MONTHS -> "最近三个月"
        TimeRange.LAST_YEAR -> "最近一年"
    }
}

private fun getChartTypeDisplayName(chartType: ChartType): String {
    return when (chartType) {
        ChartType.BAR_CHART -> "柱状图"
        ChartType.LINE_CHART -> "折线图"
        ChartType.PIE_CHART -> "饼图"
    }
}

private fun getChartTypeIcon(chartType: ChartType) = when (chartType) {
    ChartType.BAR_CHART -> Icons.Default.Info
    ChartType.LINE_CHART -> Icons.Default.Star
    ChartType.PIE_CHART -> Icons.Default.Favorite
}