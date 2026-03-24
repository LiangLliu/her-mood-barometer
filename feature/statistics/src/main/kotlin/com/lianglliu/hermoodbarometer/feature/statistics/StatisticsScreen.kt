package com.lianglliu.hermoodbarometer.feature.statistics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lianglliu.hermoodbarometer.core.locales.R
import com.lianglliu.hermoodbarometer.core.model.data.EmotionStatistics
import com.lianglliu.hermoodbarometer.core.model.data.TimeRange
import com.lianglliu.hermoodbarometer.core.ui.component.LoadingState
import com.lianglliu.hermoodbarometer.feature.statistics.components.EmotionBarChartCard
import com.lianglliu.hermoodbarometer.feature.statistics.components.EmotionLineChartCard
import com.lianglliu.hermoodbarometer.feature.statistics.components.TimeRangeSelector
import java.time.LocalDate

/** 统计页面 显示情绪数据的统计分析 */
@Composable
fun StatisticsScreen(viewModel: StatisticsViewModel = hiltViewModel()) {
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
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = { Text(text = stringResource(R.string.statistics)) })
        }
    ) { paddingValues ->
        when (statisticsUiState) {
            StatisticsUiState.Loading -> {
                LoadingState(modifier = Modifier.fillMaxSize().padding(paddingValues))
            }

            is StatisticsUiState.Success -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(paddingValues),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    // Time Range Selector
                    item {
                        TimeRangeSelector(
                            selectedTimeRange = statisticsUiState.timeRange,
                            customStartDate =
                                statisticsUiState.emotionRecordFilter.startDateTime.toLocalDate(),
                            customEndDate =
                                statisticsUiState.emotionRecordFilter.endDateTime.toLocalDate(),
                            onTimeRangeChanged = { updateTimeRange(it) },
                            onCustomDateRangeChanged = { startDate, endDate ->
                                updateCustomDateRange(startDate, endDate)
                            },
                        )
                    }

                    // Quick Stats Overview
                    item { QuickStatsOverview(statistics = statisticsUiState.statistics) }

                    // Emotion Distribution Chart
                    item {
                        ModernChartCard(
                            title = stringResource(R.string.emotion_distribution),
                            icon = Icons.Default.PieChart,
                        ) {
                            EmotionBarChartCard(statistics = statisticsUiState.statistics)
                        }
                    }

                    // Emotion Trend Chart
                    item {
                        ModernChartCard(
                            title = stringResource(R.string.emotion_trend),
                            icon = Icons.AutoMirrored.Filled.TrendingUp,
                        ) {
                            EmotionLineChartCard(statistics = statisticsUiState.statistics)
                        }
                    }

                    // Daily Average
                    item { DailyAverageCard(statistics = statisticsUiState.statistics) }

                    // Most Common Emotion
                    item { MostCommonEmotionCard(statistics = statisticsUiState.statistics) }

                    item { Spacer(modifier = Modifier.height(16.dp)) }
                }
            }
        }
    }
}

@Composable
private fun QuickStatsOverview(statistics: EmotionStatistics) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        StatCard(
            modifier = Modifier.weight(1f),
            value = statistics.totalRecords.toString(),
            label = stringResource(R.string.total_records),
            color = MaterialTheme.colorScheme.primary,
        )

        StatCard(
            modifier = Modifier.weight(1f),
            value = "%.1f".format(statistics.averageIntensity),
            label = stringResource(R.string.average_mood),
            color = MaterialTheme.colorScheme.secondary,
        )

        StatCard(
            modifier = Modifier.weight(1f),
            value = statistics.timeRange?.totalDays?.toString() ?: "0",
            label = stringResource(R.string.days_tracked),
            color = MaterialTheme.colorScheme.tertiary,
        )
    }
}

@Composable
private fun StatCard(modifier: Modifier = Modifier, value: String, label: String, color: Color) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.1f)),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = color,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
private fun ModernChartCard(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    content: @Composable () -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                )
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            content()
        }
    }
}

@Composable
private fun DailyAverageCard(statistics: EmotionStatistics) {
    val averageRecordsPerDay =
        statistics.timeRange?.getAverageRecordsPerDay(statistics.totalRecords) ?: 0f

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors =
            CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(R.string.daily_average),
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = stringResource(R.string.records_per_day),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f),
                )
            }
            Text(
                text = "%.1f".format(averageRecordsPerDay),
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
            )
        }
    }
}

@Composable
private fun MostCommonEmotionCard(statistics: EmotionStatistics) {
    val mostCommon = statistics.mostFrequentEmotion

    mostCommon?.let { emotionCount ->
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors =
                CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                ),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = stringResource(R.string.most_common_emotion),
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${emotionCount.count} ${stringResource(R.string.times_recorded)}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f),
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = emotionCount.emotionEmoji,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.secondary,
                    )
                    Text(
                        text = "${emotionCount.percentage.toInt()}%",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.7f),
                    )
                }
            }
        }
    }
}
