package com.lianglliu.hermoodbarometer.ui.screen.statistics.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.lianglliu.hermoodbarometer.domain.usecase.EmotionStatistics
import com.lianglliu.hermoodbarometer.ui.screen.statistics.ChartType

@Composable
fun EmotionChartContainer(
    statistics: EmotionStatistics?,
    chartType: ChartType,
    modifier: Modifier = Modifier
) {
    when (chartType) {
        ChartType.BAR -> EmotionBarChartCard(statistics = statistics, modifier = modifier)
        ChartType.LINE -> EmotionLineChartCard(statistics = statistics, modifier = modifier)
    }
}

fun getChartTypeIcon(chartType: ChartType) = when (chartType) {
    ChartType.BAR -> Icons.Default.Info
    ChartType.LINE -> Icons.Default.Star
}


