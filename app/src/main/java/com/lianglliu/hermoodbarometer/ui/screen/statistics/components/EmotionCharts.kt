package com.lianglliu.hermoodbarometer.ui.screen.statistics.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.lianglliu.hermoodbarometer.R
import com.lianglliu.hermoodbarometer.domain.usecase.EmotionStatistics
import com.lianglliu.hermoodbarometer.ui.components.EmptyState
import com.lianglliu.hermoodbarometer.ui.screen.statistics.ChartType

/**
 * 情绪图表容器组件
 */
@Composable
fun EmotionChartContainer(
    statistics: EmotionStatistics?,
    chartType: ChartType,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(300.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (statistics != null && statistics.totalRecords > 0) {
                // 渲染真实图表
                EmotionChart(
                    statistics = statistics,
                    chartType = chartType,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                )
            } else {
                // 显示空状态
                EmptyState(
                    icon = {
                        Icon(
                            imageVector = getChartTypeIcon(chartType),
                            contentDescription = null,
                            modifier = Modifier.size(48.dp)
                        )
                    },
                    title = stringResource(R.string.no_data),
                    subtitle = "开始记录情绪来查看统计图表"
                )
            }
        }
    }
}

/**
 * 情绪图表组件
 * 根据图表类型渲染不同的图表
 */
@Composable
private fun EmotionChart(
    statistics: EmotionStatistics,
    chartType: ChartType,
    modifier: Modifier = Modifier
) {
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
    statistics: EmotionStatistics,
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
    statistics: EmotionStatistics,
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
    statistics: EmotionStatistics,
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

private fun getChartTypeIcon(chartType: ChartType) = when (chartType) {
    ChartType.BAR -> Icons.Default.Info
    ChartType.LINE -> Icons.Default.Star
    ChartType.PIE -> Icons.Default.Favorite
} 