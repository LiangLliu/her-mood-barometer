package com.lianglliu.hermoodbarometer.ui.screen.statistics.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.lianglliu.hermoodbarometer.R
import com.lianglliu.hermoodbarometer.domain.model.TimeRange

/**
 * 时间范围选择器组件
 */
@Composable
fun TimeRangeSelector(
    selectedTimeRange: TimeRange,
    onTimeRangeChanged: (TimeRange) -> Unit,
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
                text = stringResource(R.string.time_range),
                style = MaterialTheme.typography.titleMedium
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TimeRange.values().forEach { timeRange ->
                    FilterChip(
                        selected = selectedTimeRange == timeRange,
                        onClick = { onTimeRangeChanged(timeRange) },
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

@Composable
private fun getTimeRangeDisplayName(timeRange: TimeRange): String {
    return when (timeRange) {
        TimeRange.LAST_WEEK -> stringResource(R.string.last_week)
        TimeRange.LAST_MONTH -> stringResource(R.string.last_month)
        TimeRange.LAST_3_MONTHS -> stringResource(R.string.last_3_months)
        TimeRange.LAST_YEAR -> stringResource(R.string.last_year)
    }
} 