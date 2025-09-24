package com.lianglliu.hermoodbarometer.ui.screen.statistics.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lianglliu.hermoodbarometer.R
import com.lianglliu.hermoodbarometer.domain.model.TimeRange
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale

/**
 * 时间范围选择器组件
 * 支持下拉选择和自定义日期范围
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeRangeSelector(
    selectedTimeRange: TimeRange,
    onTimeRangeChanged: (TimeRange) -> Unit,
    customStartDate: LocalDate?,
    customEndDate: LocalDate?,
    onCustomDateRangeChanged: (LocalDate, LocalDate) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    var showCustomDateRange by remember { mutableStateOf(selectedTimeRange == TimeRange.CUSTOM) }

    // 同步showCustomDateRange状态
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
            modifier = Modifier.padding(16.dp),
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
                        .menuAnchor(MenuAnchorType.PrimaryNotEditable),
                    colors = ExposedDropdownMenuDefaults.textFieldColors(
                        focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                        unfocusedIndicatorColor = MaterialTheme.colorScheme.outline
                    )
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

/**
 * 自定义日期范围选择器
 * 使用Material 3 DatePickerDialog实现完整的日期选择功能
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDateRangeSelector(
    startDate: LocalDate?,
    endDate: LocalDate?,
    onDateRangeChanged: (LocalDate, LocalDate) -> Unit,
    modifier: Modifier = Modifier
) {
    var showStartDatePicker by remember { mutableStateOf(false) }
    var showEndDatePicker by remember { mutableStateOf(false) }
    var dateRangeError by remember { mutableStateOf<String?>(null) }

    // 创建日期格式化器，根据系统语言显示
    val dateFormatter = remember {
        DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
            .withLocale(Locale.getDefault())
    }

    // 开始日期选择器状态
    val startDatePickerState = rememberDatePickerState(
        initialSelectedDateMillis = startDate?.atStartOfDay(ZoneId.systemDefault())?.toInstant()
            ?.toEpochMilli()
    )

    // 结束日期选择器状态
    val endDatePickerState = rememberDatePickerState(
        initialSelectedDateMillis = endDate?.atStartOfDay(ZoneId.systemDefault())?.toInstant()
            ?.toEpochMilli()
    )

    // 日期验证逻辑
    val validateDateRange = { start: LocalDate?, end: LocalDate? ->
        when {
            start == null || end == null -> {
                dateRangeError = null
            }

            start.isAfter(end) -> {
                dateRangeError = "date_range_error_start_after_end"
            }

            start.isAfter(LocalDate.now()) -> {
                dateRangeError = "date_range_error_start_future"
            }

            end.isAfter(LocalDate.now()) -> {
                dateRangeError = "date_range_error_end_future"
            }

            else -> {
                dateRangeError = null
            }
        }
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // 开始日期选择
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = stringResource(R.string.start_date),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 16.dp)
            )

            OutlinedTextField(
                value = startDate?.format(dateFormatter) ?: "",
                onValueChange = { /* 忽略输入，只响应点击 */ },
                readOnly = true,
                placeholder = {
                    Text(
                        text = stringResource(R.string.select_date),
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = stringResource(R.string.select_start_date),
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                modifier = Modifier
                    .weight(1f)
                    .semantics {
                        contentDescription = "点击选择开始日期"
                    },
                colors = ExposedDropdownMenuDefaults.textFieldColors(
                    focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                    unfocusedIndicatorColor = MaterialTheme.colorScheme.outline
                ),
                isError = dateRangeError != null && startDate != null,
                interactionSource = remember {
                    MutableInteractionSource()
                }.also { interactionSource ->
                    LaunchedEffect(interactionSource) {
                        interactionSource.interactions.collect { interaction ->
                            when (interaction) {
                                is PressInteraction.Press -> {
                                    showStartDatePicker = true
                                }
                            }
                        }
                    }
                }
            )
        }

        // 结束日期选择
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = stringResource(R.string.end_date),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 16.dp)
            )

            OutlinedTextField(
                value = endDate?.format(dateFormatter) ?: "",
                onValueChange = { /* 忽略输入，只响应点击 */ },
                readOnly = true,
                placeholder = {
                    Text(
                        text = stringResource(R.string.select_date),
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = stringResource(R.string.select_end_date),
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                modifier = Modifier
                    .weight(1f)
                    .semantics {
                        contentDescription = "点击选择结束日期"
                    },
                colors = ExposedDropdownMenuDefaults.textFieldColors(
                    focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                    unfocusedIndicatorColor = MaterialTheme.colorScheme.outline
                ),
                isError = dateRangeError != null && endDate != null,
                interactionSource = remember {
                    MutableInteractionSource()
                }.also { interactionSource ->
                    LaunchedEffect(interactionSource) {
                        interactionSource.interactions.collect { interaction ->
                            when (interaction) {
                                is PressInteraction.Press -> {
                                    showEndDatePicker = true
                                }
                            }
                        }
                    }
                }
            )
        }

        // 错误提示
        if (dateRangeError != null) {
            val errorMessage = when (dateRangeError) {
                "date_range_error_start_after_end" -> stringResource(R.string.date_range_error_start_after_end)
                "date_range_error_start_future" -> stringResource(R.string.date_range_error_start_future)
                "date_range_error_end_future" -> stringResource(R.string.date_range_error_end_future)
                else -> dateRangeError!!
            }
            Text(
                text = errorMessage,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }

    // 开始日期选择器对话框
    if (showStartDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showStartDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        startDatePickerState.selectedDateMillis?.let { millis ->
                            val selectedDate = Instant.ofEpochMilli(millis)
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate()

                            // 如果已有结束日期，验证并更新
                            if (endDate != null) {
                                validateDateRange(selectedDate, endDate)
                                if (dateRangeError == null) {
                                    onDateRangeChanged(selectedDate, endDate)
                                }
                            } else {
                                onDateRangeChanged(
                                    selectedDate,
                                    selectedDate.plusDays(7)
                                ) // 默认选择7天范围
                            }
                        }
                        showStartDatePicker = false
                    }
                ) {
                    Text(stringResource(R.string.confirm))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showStartDatePicker = false }
                ) {
                    Text(stringResource(R.string.cancel))
                }
            }
        ) {
            DatePicker(
                state = startDatePickerState,
                title = {
                    Text(
                        text = stringResource(R.string.select_start_date),
                        modifier = Modifier.padding(16.dp)
                    )
                }
            )
        }
    }

    // 结束日期选择器对话框
    if (showEndDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showEndDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        endDatePickerState.selectedDateMillis?.let { millis ->
                            val selectedDate = Instant.ofEpochMilli(millis)
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate()

                            // 如果已有开始日期，验证并更新
                            if (startDate != null) {
                                validateDateRange(startDate, selectedDate)
                                if (dateRangeError == null) {
                                    onDateRangeChanged(startDate, selectedDate)
                                }
                            } else {
                                onDateRangeChanged(
                                    selectedDate.minusDays(7),
                                    selectedDate
                                ) // 默认选择7天范围
                            }
                        }
                        showEndDatePicker = false
                    }
                ) {
                    Text(stringResource(R.string.confirm))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showEndDatePicker = false }
                ) {
                    Text(stringResource(R.string.cancel))
                }
            }
        ) {
            DatePicker(
                state = endDatePickerState,
                title = {
                    Text(
                        text = stringResource(R.string.select_end_date),
                        modifier = Modifier.padding(16.dp)
                    )
                }
            )
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

// 预览函数
@Preview(showBackground = true)
@Composable
private fun TimeRangeSelectorPreview() {
    MaterialTheme {
        TimeRangeSelector(
            selectedTimeRange = TimeRange.CUSTOM,
            onTimeRangeChanged = {},
            customStartDate = LocalDate.now().minusDays(7),
            customEndDate = LocalDate.now(),
            onCustomDateRangeChanged = { _, _ -> }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CustomDateRangeSelectorPreview() {
    MaterialTheme {
        CustomDateRangeSelector(
            startDate = LocalDate.now().minusDays(7),
            endDate = LocalDate.now(),
            onDateRangeChanged = { _, _ -> }
        )
    }
}