package com.lianglliu.hermoodbarometer.ui.screen.statistics.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.lianglliu.hermoodbarometer.R
import com.lianglliu.hermoodbarometer.ui.screen.statistics.ChartType

/**
 * 图表类型选择器组件
 */
@Composable
fun ChartTypeSelector(
    selectedChartType: ChartType,
    onChartTypeChanged: (ChartType) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth()
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
                listOf(ChartType.BAR, ChartType.LINE).forEach { chartType ->
                    FilterChip(
                        selected = selectedChartType == chartType,
                        onClick = { onChartTypeChanged(chartType) },
                        label = {
                            Text(
                                text = chartTypeDisplayName(chartType),
                                style = MaterialTheme.typography.bodySmall
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = chartTypeIcon(chartType),
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

@Composable
private fun chartTypeDisplayName(chartType: ChartType): String {
    return when (chartType) {
        ChartType.BAR -> stringResource(R.string.bar_chart)
        ChartType.LINE -> stringResource(R.string.line_chart)
    }
}

private fun chartTypeIcon(chartType: ChartType) = when (chartType) {
    ChartType.BAR -> Icons.Default.Info
    ChartType.LINE -> Icons.Default.Star
} 