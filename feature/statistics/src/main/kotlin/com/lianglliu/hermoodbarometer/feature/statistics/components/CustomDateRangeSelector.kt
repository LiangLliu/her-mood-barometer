package com.lianglliu.hermoodbarometer.feature.statistics.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.unit.dp
import com.lianglliu.hermoodbarometer.core.designsystem.icon.AppIcons
import com.lianglliu.hermoodbarometer.core.designsystem.icon.outlined.Calendar
import com.lianglliu.hermoodbarometer.core.locales.R
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale


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

    var dateRangeError by remember { mutableStateOf<String?>(null) }

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

    val clickStartDate = { millis: Long ->
        val selectedDate = Instant.ofEpochMilli(millis)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
        if (endDate != null) {
            validateDateRange(selectedDate, endDate)
            if (dateRangeError == null) {
                onDateRangeChanged(selectedDate, endDate)
            }
        } else {
            onDateRangeChanged(
                selectedDate,
                selectedDate.plusDays(7)
            )
        }
    }
    val clickEndDate = { millis: Long ->
        val selectedDate = Instant.ofEpochMilli(millis)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
        if (startDate != null) {
            validateDateRange(startDate, selectedDate)
            if (dateRangeError == null) {
                onDateRangeChanged(startDate, selectedDate)
            }
        } else {
            onDateRangeChanged(
                selectedDate.minusDays(7),
                selectedDate
            )
        }
    }


    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        SingleDateRangeSelector(startDate, clickStartDate, dateRangeError)
        SingleDateRangeSelector(endDate, clickEndDate, dateRangeError)
    }
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
            modifier = Modifier.padding(top = 16.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SingleDateRangeSelector(
    date: LocalDate?,
    selectDate: (Long) -> Unit,
    dateRangeError: String?,
) {
    var showDatePicker by remember { mutableStateOf(false) }

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = date?.atStartOfDay(ZoneId.systemDefault())?.toInstant()
            ?.toEpochMilli()
    )

    val dateFormatter = remember {
        DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
            .withLocale(Locale.getDefault())
    }

    OutlinedTextField(
        value = date?.format(dateFormatter) ?: "",
        onValueChange = { /* 忽略输入，只响应点击 */ },
        readOnly = true,
        label = { Text(stringResource(R.string.start_date)) },
        placeholder = {
            Text(
                text = stringResource(R.string.select_date),
                style = MaterialTheme.typography.bodyMedium
            )
        },
        trailingIcon = {
            Icon(
                imageVector = AppIcons.Outlined.Calendar,
                contentDescription = stringResource(R.string.select_start_date),
                tint = MaterialTheme.colorScheme.primary
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .clickable { showDatePicker = true }
            .semantics {
                contentDescription = "点击选择开始日期"
            },
        isError = dateRangeError != null && date != null,
        interactionSource = remember {
            MutableInteractionSource()
        }.also { interactionSource ->
            LaunchedEffect(interactionSource) {
                interactionSource.interactions.collect { interaction ->
                    when (interaction) {
                        is PressInteraction.Press -> {
                            showDatePicker = true
                        }
                    }
                }
            }
        }
    )

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            selectDate(millis)
                        }
                        showDatePicker = false
                    }
                ) {
                    Text(stringResource(R.string.confirm))
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        ) {
            DatePicker(
                state = datePickerState,
                title = {
                    Text(
                        text = stringResource(R.string.select_start_date),
                        modifier = Modifier.padding(16.dp)
                    )
                }
            )
        }
    }
}