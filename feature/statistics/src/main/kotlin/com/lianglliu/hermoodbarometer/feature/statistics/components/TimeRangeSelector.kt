package com.lianglliu.hermoodbarometer.feature.statistics.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.lianglliu.hermoodbarometer.core.locales.R
import com.lianglliu.hermoodbarometer.core.model.data.TimeRange
import java.time.LocalDate

/**
 * 时间范围选择器组件
 * 支持下拉选择和自定义日期范围
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeRangeSelector(
    selectedTimeRange: TimeRange,
    onTimeRangeChanged: (TimeRange) -> Unit,
    customStartDate: LocalDate? = null,
    customEndDate: LocalDate? = null,
    onCustomDateRangeChanged: (startDate: LocalDate, endDate: LocalDate) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    var showCustomDateRange by remember { mutableStateOf(selectedTimeRange == TimeRange.CUSTOM) }

    LaunchedEffect(selectedTimeRange) {
        showCustomDateRange = selectedTimeRange == TimeRange.CUSTOM
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = stringResource(R.string.time_range),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )

            // 时间范围下拉选择器
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = getTimeRangeDisplayName(selectedTimeRange),
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    TimeRange.entries.forEach { timeRange ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = getTimeRangeDisplayName(timeRange),
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            },
                            onClick = {
                                onTimeRangeChanged(timeRange)
                                expanded = false
                                showCustomDateRange = timeRange == TimeRange.CUSTOM
                            }
                        )
                    }
                }
            }

            // 自定义日期范围选择器
            if (showCustomDateRange) {
                CustomDateRangeSelector(
                    startDate = customStartDate,
                    endDate = customEndDate,
                    onDateRangeChanged = onCustomDateRangeChanged
                )
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
        TimeRange.LAST_SIX_MONTHS -> stringResource(R.string.last_six_months)
        TimeRange.LAST_YEAR -> stringResource(R.string.last_year)
        TimeRange.CUSTOM -> stringResource(R.string.custom_range)
    }
}
