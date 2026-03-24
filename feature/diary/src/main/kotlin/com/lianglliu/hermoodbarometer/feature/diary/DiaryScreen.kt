package com.lianglliu.hermoodbarometer.feature.diary

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lianglliu.hermoodbarometer.core.designsystem.component.ScreenHeader
import com.lianglliu.hermoodbarometer.core.designsystem.icon.Emojis
import com.lianglliu.hermoodbarometer.core.designsystem.theme.ExtendedColorScheme
import com.lianglliu.hermoodbarometer.core.designsystem.theme.ExtendedTheme
import com.lianglliu.hermoodbarometer.core.locales.R
import com.lianglliu.hermoodbarometer.core.model.data.Activity
import com.lianglliu.hermoodbarometer.core.model.data.EmotionRecord
import com.lianglliu.hermoodbarometer.core.model.data.Weather
import com.lianglliu.hermoodbarometer.core.ui.component.emojiToLocalizedName
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DiaryScreen(modifier: Modifier = Modifier, viewModel: DiaryViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val colors = ExtendedTheme.colors

    Scaffold(
        topBar = {
            ScreenHeader(
                title = stringResource(R.string.diary_title),
                subtitle = stringResource(R.string.app_description),
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
        modifier = modifier,
    ) { paddingValues ->
        when (uiState) {
            is DiaryUiState.Loading -> {
                com.lianglliu.hermoodbarometer.core.ui.component.LoadingState(
                    modifier = Modifier.fillMaxSize().padding(paddingValues)
                )
            }

            is DiaryUiState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize().padding(paddingValues),
                    contentAlignment = Alignment.Center,
                ) {
                    com.lianglliu.hermoodbarometer.core.ui.component.ErrorCard(
                        message = (uiState as DiaryUiState.Error).message,
                        modifier = Modifier.padding(16.dp),
                    )
                }
            }

            is DiaryUiState.Success -> {
                val successState = uiState as DiaryUiState.Success
                if (successState.groupedRecords.isEmpty()) {
                    EmptyDiaryState(modifier = Modifier.fillMaxSize().padding(paddingValues))
                } else {
                    DiaryContent(
                        recordsByDate = successState.groupedRecords,
                        onDeleteRecord = viewModel::deleteRecord,
                        modifier = Modifier.fillMaxSize().padding(paddingValues),
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun DiaryContent(
    recordsByDate: Map<LocalDate, List<EmotionRecord>>,
    onDeleteRecord: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(start = 16.dp, top = 8.dp, end = 16.dp, bottom = 100.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        recordsByDate.forEach { (date, records) ->
            stickyHeader { DateGroupHeader(date = date) }

            items(items = records, key = { it.id }) { record ->
                DiaryEntryCard(
                    record = record,
                    onDelete = { onDeleteRecord(record.id) },
                    modifier = Modifier.animateItem(),
                )
            }

            item { Spacer(modifier = Modifier.height(6.dp)) }
        }
    }
}

@Composable
private fun DateGroupHeader(date: LocalDate, modifier: Modifier = Modifier) {
    val today = LocalDate.now()
    val isToday = date == today
    val isYesterday = date == today.minusDays(1)
    val colors = ExtendedTheme.colors

    val dateText =
        when {
            isToday -> stringResource(R.string.today)
            isYesterday -> stringResource(R.string.yesterday)
            else ->
                date.format(
                    DateTimeFormatter.ofPattern(
                        stringResource(R.string.date_format_full_cn),
                        Locale.getDefault(),
                    )
                )
        }

    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
                .padding(start = 4.dp, end = 4.dp, bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = dateText,
            style = MaterialTheme.typography.titleSmall.copy(fontSize = 14.sp),
            fontWeight = FontWeight.Medium,
            color = colors.textMuted,
        )
        Spacer(modifier = Modifier.width(8.dp))
        HorizontalDivider(
            modifier = Modifier.weight(1f),
            thickness = 1.dp,
            color = colors.borderLight,
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun DiaryEntryCard(
    record: EmotionRecord,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    val colors = ExtendedTheme.colors
    val moodColor = emojiToMoodColor(record.emotionEmoji, colors)
    val emotionName = emojiToLocalizedName(record.emotionEmoji)

    Box(
        modifier =
            modifier
                .fillMaxWidth()
                .background(colors.cardBackground, RoundedCornerShape(20.dp))
                .border(1.dp, colors.borderLight, RoundedCornerShape(20.dp))
                .clip(RoundedCornerShape(20.dp))
    ) {
        Row(modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min)) {
            // Left mood-colored border
            Box(
                modifier =
                    Modifier.width(3.dp)
                        .fillMaxHeight()
                        .background(moodColor, RoundedCornerShape(topEnd = 3.dp, bottomEnd = 3.dp))
            )

            Row(
                modifier = Modifier.weight(1f).padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                verticalAlignment = Alignment.Top,
            ) {
                // Emoji in warm background square
                Box(
                    modifier =
                        Modifier.size(48.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(colors.warmBackground),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(text = record.emotionEmoji, fontSize = 32.sp)
                }

                // Body content
                Column(modifier = Modifier.weight(1f)) {
                    // Row 1: Emotion name + Time
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = emotionName,
                            style =
                                MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 16.sp,
                                ),
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                        Text(
                            text =
                                record
                                    .getLocalDateTime()
                                    .format(
                                        DateTimeFormatter.ofPattern("a h:mm", Locale.getDefault())
                                    ),
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 12.sp),
                            color = colors.textHint,
                        )
                    }

                    // Row 2: Note text (if available)
                    if (record.note.isNotBlank()) {
                        Text(
                            text = record.note,
                            style =
                                MaterialTheme.typography.bodyMedium.copy(
                                    fontSize = 13.5.sp,
                                    lineHeight = 22.sp,
                                ),
                            color = colors.textSecondary,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.padding(bottom = 8.dp),
                        )
                    }

                    // Row 3: Tags (weather + activities)
                    if (record.hasContext()) {
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp),
                        ) {
                            record.weather?.let { weather ->
                                TagChip(text = weather.emoji + " " + weatherDisplayName(weather))
                            }
                            record.activities.forEach { activity ->
                                TagChip(text = activity.emoji + " " + activityDisplayName(activity))
                            }
                        }
                    }
                }
            }
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text(stringResource(R.string.confirm_delete_record)) },
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

@Composable
private fun TagChip(text: String, modifier: Modifier = Modifier) {
    val colors = ExtendedTheme.colors
    Text(
        text = text,
        style = MaterialTheme.typography.labelSmall,
        color = colors.textMuted,
        modifier =
            modifier
                .background(colors.warmBackground, RoundedCornerShape(50))
                .border(1.dp, colors.borderLight, RoundedCornerShape(50))
                .padding(horizontal = 10.dp, vertical = 3.dp),
    )
}

@Composable
private fun EmptyDiaryState(modifier: Modifier = Modifier) {
    val colors = ExtendedTheme.colors
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(text = Emojis.NOTEBOOK, style = MaterialTheme.typography.displayLarge)
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.diary_empty),
            style = MaterialTheme.typography.bodyLarge,
            color = colors.textSecondary,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.diary_empty_hint),
            style = MaterialTheme.typography.bodyMedium,
            color = colors.textMuted,
        )
    }
}

// ==================== Emoji → Mood Color ====================

@Composable
private fun emojiToMoodColor(emoji: String, colors: ExtendedColorScheme): Color =
    when {
        emoji in Emojis.BAR_JOYFUL -> colors.amber
        emoji in Emojis.BAR_PEACEFUL -> colors.sage
        emoji in Emojis.BAR_INTENSE -> colors.accent
        emoji in Emojis.BAR_THOUGHTFUL -> colors.lavender
        emoji in Emojis.BAR_SAD -> colors.rose
        else -> colors.accent
    }

// ==================== Weather / Activity Display Names ====================

@Composable
private fun weatherDisplayName(weather: Weather): String =
    when (weather) {
        Weather.SUNNY -> stringResource(R.string.weather_sunny)
        Weather.PARTLY_CLOUDY -> stringResource(R.string.weather_partly_cloudy)
        Weather.CLOUDY -> stringResource(R.string.weather_cloudy)
        Weather.RAINY -> stringResource(R.string.weather_rainy)
        Weather.STORMY -> stringResource(R.string.weather_stormy)
        Weather.SNOWY -> stringResource(R.string.weather_snowy)
        Weather.FOGGY -> stringResource(R.string.weather_foggy)
        Weather.WINDY -> stringResource(R.string.weather_windy)
    }

@Composable
private fun activityDisplayName(activity: Activity): String =
    when (activity) {
        Activity.WORK -> stringResource(R.string.activity_work)
        Activity.EXERCISE -> stringResource(R.string.activity_exercise)
        Activity.READING -> stringResource(R.string.activity_reading)
        Activity.SOCIAL -> stringResource(R.string.activity_social)
        Activity.ENTERTAINMENT -> stringResource(R.string.activity_entertainment)
        Activity.TRAVEL -> stringResource(R.string.activity_travel)
        Activity.EATING -> stringResource(R.string.activity_eating)
        Activity.SHOPPING -> stringResource(R.string.activity_shopping)
        Activity.STUDY -> stringResource(R.string.activity_study)
        Activity.CREATIVE -> stringResource(R.string.activity_creative)
        Activity.RELAXING -> stringResource(R.string.activity_rest)
        Activity.OUTDOOR -> stringResource(R.string.activity_outdoor)
    }
