package com.lianglliu.hermoodbarometer.feature.calendar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lianglliu.hermoodbarometer.core.designsystem.component.CardLabel
import com.lianglliu.hermoodbarometer.core.designsystem.component.ScreenHeader
import com.lianglliu.hermoodbarometer.core.designsystem.component.StatRow
import com.lianglliu.hermoodbarometer.core.designsystem.component.WarmCard
import com.lianglliu.hermoodbarometer.core.designsystem.icon.Emojis
import com.lianglliu.hermoodbarometer.core.designsystem.theme.ExtendedTheme
import com.lianglliu.hermoodbarometer.core.locales.R
import com.lianglliu.hermoodbarometer.core.model.data.EmotionRecord
import com.lianglliu.hermoodbarometer.core.ui.component.emojiToLocalizedName
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun CalendarScreen(modifier: Modifier = Modifier, viewModel: CalendarViewModel = hiltViewModel()) {
    val currentYearMonth by viewModel.currentYearMonth.collectAsStateWithLifecycle()
    val selectedDate by viewModel.selectedDate.collectAsStateWithLifecycle()
    val monthlyRecords by viewModel.monthlyRecords.collectAsStateWithLifecycle()
    val selectedDateRecords by viewModel.selectedDateRecords.collectAsStateWithLifecycle()

    val colors = ExtendedTheme.colors

    Scaffold(
        topBar = {
            ScreenHeader(
                title = stringResource(R.string.calendar_title),
                subtitle = stringResource(R.string.calendar_subtitle),
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
        modifier = modifier,
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(paddingValues),
            contentPadding =
                PaddingValues(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 100.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            // Calendar Card
            item {
                CalendarCard(
                    yearMonth = currentYearMonth,
                    selectedDate = selectedDate,
                    monthlyRecords = monthlyRecords,
                    onPreviousMonth = { viewModel.navigateToPreviousMonth() },
                    onNextMonth = { viewModel.navigateToNextMonth() },
                    onDateSelected = { viewModel.selectDate(it) },
                )
            }

            // Selected Date Details
            item {
                AnimatedVisibility(
                    visible = selectedDate != null && selectedDateRecords.isNotEmpty(),
                    enter = expandVertically() + fadeIn(),
                    exit = shrinkVertically() + fadeOut(),
                ) {
                    SelectedDateCard(
                        date = selectedDate,
                        records = selectedDateRecords,
                        onDeleteRecord = { viewModel.deleteRecord(it) },
                    )
                }
            }

            // Monthly Stats Card
            item { MonthlyStatsCard(monthlyRecords = monthlyRecords) }

            // Emotion Legend
            item { EmotionLegendCard(monthlyRecords = monthlyRecords) }

            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }
}

// ==================== Calendar Card ====================

@Composable
private fun CalendarCard(
    yearMonth: YearMonth,
    selectedDate: LocalDate?,
    monthlyRecords: Map<LocalDate, List<EmotionRecord>>,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit,
    onDateSelected: (LocalDate) -> Unit,
) {
    val colors = ExtendedTheme.colors

    WarmCard(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier =
                    Modifier.size(30.dp)
                        .clip(CircleShape)
                        .border(1.dp, colors.border, CircleShape)
                        .clickable { onPreviousMonth() },
                contentAlignment = Alignment.Center,
            ) {
                Text(text = Emojis.NAV_PREV, fontSize = 14.sp, color = colors.textSecondary)
            }

            Text(
                text =
                    yearMonth.format(
                        DateTimeFormatter.ofPattern(stringResource(R.string.calendar_month_format))
                    ),
                style = MaterialTheme.typography.titleMedium,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
            )

            Box(
                modifier =
                    Modifier.size(30.dp)
                        .clip(CircleShape)
                        .border(1.dp, colors.border, CircleShape)
                        .clickable { onNextMonth() },
                contentAlignment = Alignment.Center,
            ) {
                Text(text = Emojis.NAV_NEXT, fontSize = 14.sp, color = colors.textSecondary)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth().padding(bottom = 6.dp)) {
            val daysOfWeek =
                listOf(
                    DayOfWeek.SUNDAY,
                    DayOfWeek.MONDAY,
                    DayOfWeek.TUESDAY,
                    DayOfWeek.WEDNESDAY,
                    DayOfWeek.THURSDAY,
                    DayOfWeek.FRIDAY,
                    DayOfWeek.SATURDAY,
                )

            daysOfWeek.forEach { dayOfWeek ->
                Text(
                    text = dayOfWeek.getDisplayName(TextStyle.NARROW, Locale.getDefault()),
                    modifier = Modifier.weight(1f).padding(vertical = 6.dp),
                    textAlign = TextAlign.Center,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    color = colors.textHint,
                    letterSpacing = 0.5.sp,
                )
            }
        }

        val daysInMonth = yearMonth.lengthOfMonth()
        val firstDayOfMonth = yearMonth.atDay(1)
        val firstDayOfWeek = firstDayOfMonth.dayOfWeek.value % 7
        val prevMonth = yearMonth.minusMonths(1)
        val prevMonthLastDay = prevMonth.lengthOfMonth()
        val totalCells = firstDayOfWeek + daysInMonth
        val remainingCells = if (totalCells % 7 == 0) 0 else 7 - totalCells % 7
        val totalRows = (totalCells + remainingCells) / 7

        // Hoist LocalDate.now() outside the loop to avoid creating a new instance per cell
        val today = LocalDate.now()

        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            for (row in 0 until totalRows) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    for (col in 0 until 7) {
                        val cellIndex = row * 7 + col
                        when {
                            cellIndex < firstDayOfWeek -> {
                                val day = prevMonthLastDay - (firstDayOfWeek - 1 - cellIndex)
                                OtherMonthDay(dayNum = day, modifier = Modifier.weight(1f))
                            }
                            cellIndex >= firstDayOfWeek + daysInMonth -> {
                                val day = cellIndex - firstDayOfWeek - daysInMonth + 1
                                OtherMonthDay(dayNum = day, modifier = Modifier.weight(1f))
                            }
                            else -> {
                                val dayNum = cellIndex - firstDayOfWeek + 1
                                val date = yearMonth.atDay(dayNum)
                                val records = monthlyRecords[date] ?: emptyList()
                                CalendarDay(
                                    date = date,
                                    isSelected = date == selectedDate,
                                    isToday = date == today,
                                    emotionEmoji = records.firstOrNull()?.emotionEmoji,
                                    onClick = { onDateSelected(date) },
                                    modifier = Modifier.weight(1f),
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CalendarDay(
    date: LocalDate,
    isSelected: Boolean,
    isToday: Boolean,
    emotionEmoji: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = ExtendedTheme.colors
    val shape = RoundedCornerShape(8.dp)

    Box(
        modifier =
            modifier
                .aspectRatio(1f)
                .clip(shape)
                .clickable { onClick() }
                .then(
                    when {
                        isSelected ->
                            Modifier.background(colors.accentBg, shape)
                                .border(1.5.dp, colors.accent, shape)
                        isToday ->
                            Modifier.background(colors.accentBg, shape)
                                .border(1.5.dp, colors.accent, shape)
                        else -> Modifier
                    }
                ),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = date.dayOfMonth.toString(),
                fontSize = 12.sp,
                fontWeight = if (isToday || isSelected) FontWeight.SemiBold else FontWeight.Normal,
                color =
                    when {
                        isSelected -> colors.accent
                        isToday -> colors.accent
                        else -> colors.textSecondary
                    },
                lineHeight = 12.sp,
            )

            if (emotionEmoji != null) {
                Text(text = emotionEmoji, fontSize = 14.sp, lineHeight = 14.sp)
            }
        }
    }
}

@Composable
private fun OtherMonthDay(dayNum: Int, modifier: Modifier = Modifier) {
    val colors = ExtendedTheme.colors
    Box(modifier = modifier.aspectRatio(1f), contentAlignment = Alignment.Center) {
        Text(
            text = dayNum.toString(),
            fontSize = 12.sp,
            color = colors.textSecondary.copy(alpha = 0.25f),
            lineHeight = 12.sp,
        )
    }
}

// ==================== Selected Date Card ====================

@Composable
private fun SelectedDateCard(
    date: LocalDate?,
    records: List<EmotionRecord>,
    onDeleteRecord: (Long) -> Unit,
) {
    date ?: return
    val colors = ExtendedTheme.colors

    WarmCard(modifier = Modifier.fillMaxWidth()) {
        Text(
            text =
                date.format(
                    DateTimeFormatter.ofPattern(stringResource(R.string.date_format_full_cn))
                ),
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface,
        )

        Spacer(modifier = Modifier.height(12.dp))

        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.heightIn(max = 300.dp),
        ) {
            records.forEach { record ->
                RecordItem(record = record, onDelete = { onDeleteRecord(record.id) })
            }
        }
    }
}

@Composable
private fun RecordItem(record: EmotionRecord, onDelete: () -> Unit) {
    val colors = ExtendedTheme.colors
    var showDeleteDialog by remember { mutableStateOf(false) }

    Row(
        modifier =
            Modifier.fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(colors.warmBackground)
                .padding(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // Emoji
        Box(
            modifier =
                Modifier.size(40.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(colors.cardBackground),
            contentAlignment = Alignment.Center,
        ) {
            Text(text = record.emotionEmoji, fontSize = 22.sp)
        }

        // Info
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = record.getLocalDateTime().format(DateTimeFormatter.ofPattern("HH:mm")),
                style = MaterialTheme.typography.labelSmall,
                color = colors.textMuted,
            )
            if (record.note.isNotBlank()) {
                Text(
                    text = record.note,
                    style = MaterialTheme.typography.bodySmall,
                    color = colors.textSecondary,
                    maxLines = 2,
                    modifier = Modifier.padding(top = 2.dp),
                )
            }
        }

        // Delete
        Box(
            modifier =
                Modifier.size(28.dp).clip(CircleShape).background(colors.roseBg).clickable {
                    showDeleteDialog = true
                },
            contentAlignment = Alignment.Center,
        ) {
            Text(text = Emojis.CLOSE, fontSize = 14.sp, color = colors.rose)
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text(stringResource(R.string.delete)) },
            text = { Text(stringResource(R.string.confirm_delete_record)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDelete()
                        showDeleteDialog = false
                    }
                ) {
                    Text(stringResource(R.string.delete))
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text(stringResource(R.string.cancel))
                }
            },
        )
    }
}

// ==================== Monthly Stats Card ====================

@Composable
private fun MonthlyStatsCard(monthlyRecords: Map<LocalDate, List<EmotionRecord>>) {
    val colors = ExtendedTheme.colors

    // Memoize all derived stats to avoid recomputation on every recomposition
    val stats =
        remember(monthlyRecords) {
            val totalRecords = monthlyRecords.values.sumOf { it.size }
            val daysWithRecords = monthlyRecords.count { it.value.isNotEmpty() }
            val allRecords = monthlyRecords.values.flatten()
            val mostFrequent = allRecords.groupBy { it.emotionEmoji }.maxByOrNull { it.value.size }

            // Compute mood index based on positive/negative ratio
            val positiveCount = allRecords.count { it.emotionEmoji in Emojis.POSITIVE_EMOTIONS }
            val positiveRatio =
                if (allRecords.isNotEmpty()) positiveCount.toFloat() / allRecords.size else 0f

            MonthlyStats(
                daysWithRecords = daysWithRecords,
                mostFrequent = mostFrequent,
                allRecordsEmpty = allRecords.isEmpty(),
                positiveRatio = positiveRatio,
            )
        }

    val moodIndexText =
        when {
            stats.allRecordsEmpty -> "-"
            stats.positiveRatio >= 0.6f -> stringResource(R.string.mood_good)
            stats.positiveRatio >= 0.3f -> stringResource(R.string.mood_average)
            else -> stringResource(R.string.mood_low)
        }

    WarmCard(modifier = Modifier.fillMaxWidth()) {
        CardLabel(text = stringResource(R.string.monthly_stats))

        StatRow(
            emoji = Emojis.CALENDAR,
            label = stringResource(R.string.recording_days),
            value = "${stats.daysWithRecords}${stringResource(R.string.days_suffix)}",
        )
        StatRow(
            emoji = Emojis.SMILE,
            label = stringResource(R.string.most_mood),
            value =
                if (stats.mostFrequent != null) {
                    "${emojiToLocalizedName(stats.mostFrequent.key)} (${stats.mostFrequent.value.size}${stringResource(R.string.times_recorded)})"
                } else {
                    "-"
                },
        )
        StatRow(
            emoji = Emojis.CHART_UP,
            label = stringResource(R.string.mood_index),
            value = moodIndexText,
            showDivider = false,
        )
    }
}

// ==================== Emotion Legend Card ====================

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun EmotionLegendCard(monthlyRecords: Map<LocalDate, List<EmotionRecord>>) {
    val colors = ExtendedTheme.colors

    // Memoize unique emotions list to avoid recomputation on every recomposition
    val uniqueEmotions =
        remember(monthlyRecords) {
            monthlyRecords.values.flatten().map { it.emotionEmoji }.distinct().take(8)
        }

    if (uniqueEmotions.isEmpty()) return

    WarmCard(modifier = Modifier.fillMaxWidth()) {
        CardLabel(text = stringResource(R.string.emotion_legend))

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            uniqueEmotions.forEach { emoji ->
                Box(
                    modifier =
                        Modifier.clip(RoundedCornerShape(100.dp))
                            .background(colors.warmBackground)
                            .border(1.5.dp, colors.borderLight, RoundedCornerShape(100.dp))
                            .padding(horizontal = 14.dp, vertical = 7.dp)
                ) {
                    Text(
                        text = "$emoji ${emojiToLocalizedName(emoji)}",
                        fontSize = 13.sp,
                        color = colors.textSecondary,
                    )
                }
            }
        }
    }
}

// Data class for memoized monthly statistics
private data class MonthlyStats(
    val daysWithRecords: Int,
    val mostFrequent: Map.Entry<String, List<EmotionRecord>>?,
    val allRecordsEmpty: Boolean,
    val positiveRatio: Float,
)
