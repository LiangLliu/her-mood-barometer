package com.lianglliu.hermoodbarometer.feature.statistics.components


import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.lianglliu.hermoodbarometer.core.designsystem.icon.AppIcons
import com.lianglliu.hermoodbarometer.core.designsystem.icon.outlined.Info
import com.lianglliu.hermoodbarometer.core.designsystem.icon.outlined.Star
import com.lianglliu.hermoodbarometer.core.domain.EmotionStatistics
import com.lianglliu.hermoodbarometer.feature.statistics.ChartType

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
    ChartType.BAR -> AppIcons.Outlined.Info
    ChartType.LINE -> AppIcons.Outlined.Star
}


