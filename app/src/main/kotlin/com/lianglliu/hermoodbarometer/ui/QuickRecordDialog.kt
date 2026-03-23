package com.lianglliu.hermoodbarometer.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.lianglliu.hermoodbarometer.core.locales.R
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun QuickRecordDialog(
    onDismiss: () -> Unit,
    onConfirm:
        (
            emotion: Emotion,
            weather: Weather,
            activities: List<ActivityType>,
            note: String,
            dateTime: LocalDateTime,
        ) -> Unit,
) {
    var selectedEmotion by remember { mutableStateOf(Emotion.HAPPY) }
    var selectedWeather by remember { mutableStateOf(Weather.SUNNY) }
    var selectedActivities by remember { mutableStateOf(listOf<ActivityType>()) }
    var note by remember { mutableStateOf("") }
    var selectedDateTime by remember { mutableStateOf(LocalDateTime.now()) }

    Dialog(
        onDismissRequest = onDismiss,
        properties =
            DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true,
                usePlatformDefaultWidth = false,
            ),
    ) {
        Card(
            modifier = Modifier.fillMaxWidth().padding(16.dp).clip(RoundedCornerShape(28.dp)),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        ) {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()).padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp),
            ) {
                // Title
                Text(
                    text = stringResource(R.string.how_feeling_now),
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                )

                // Date and Time
                DateTimeSelector(
                    selectedDateTime = selectedDateTime,
                    onDateTimeSelected = { selectedDateTime = it },
                )

                // Emotion Selection
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text(
                        text = stringResource(R.string.label_emotion),
                        style = MaterialTheme.typography.titleMedium,
                    )
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        modifier = Modifier.height(160.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        items(Emotion.values()) { emotion ->
                            EmotionCard(
                                emotion = emotion,
                                isSelected = selectedEmotion == emotion,
                                onClick = { selectedEmotion = emotion },
                            )
                        }
                    }
                }

                // Weather Selection
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text(
                        text = stringResource(R.string.label_weather),
                        style = MaterialTheme.typography.titleMedium,
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Weather.values().forEach { weather ->
                            WeatherChip(
                                weather = weather,
                                isSelected = selectedWeather == weather,
                                onClick = { selectedWeather = weather },
                                modifier = Modifier.weight(1f),
                            )
                        }
                    }
                }

                // Activities Selection
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text(
                        text = stringResource(R.string.label_activity),
                        style = MaterialTheme.typography.titleMedium,
                    )
                    FlowRow(horizontalSpacing = 8.dp, verticalSpacing = 8.dp) {
                        ActivityType.values().forEach { activity ->
                            ActivityChip(
                                activity = activity,
                                isSelected = activity in selectedActivities,
                                onClick = {
                                    if (activity in selectedActivities) {
                                        selectedActivities = selectedActivities - activity
                                    } else {
                                        selectedActivities = selectedActivities + activity
                                    }
                                },
                            )
                        }
                    }
                }

                // Note Input
                OutlinedTextField(
                    value = note,
                    onValueChange = { note = it },
                    label = { Text(stringResource(R.string.label_note)) },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2,
                    maxLines = 4,
                )

                // Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    OutlinedButton(onClick = onDismiss, modifier = Modifier.weight(1f)) {
                        Text(stringResource(R.string.cancel))
                    }
                    Button(
                        onClick = {
                            onConfirm(
                                selectedEmotion,
                                selectedWeather,
                                selectedActivities.toList(),
                                note,
                                selectedDateTime,
                            )
                        },
                        modifier = Modifier.weight(1f),
                    ) {
                        Text(stringResource(R.string.save))
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DateTimeSelector(
    selectedDateTime: LocalDateTime,
    onDateTimeSelected: (LocalDateTime) -> Unit,
) {
    val context = LocalContext.current
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text =
                selectedDateTime.format(
                    DateTimeFormatter.ofPattern(
                        stringResource(R.string.date_format_full_cn),
                        Locale.getDefault(),
                    )
                ),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.clickable { showDatePicker = true },
        )
        Text(text = " · ", style = MaterialTheme.typography.bodyLarge)
        Text(
            text = selectedDateTime.format(DateTimeFormatter.ofPattern("HH:mm")),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.clickable { showTimePicker = true },
        )
    }

    // Date Picker Dialog
    if (showDatePicker) {
        val datePickerState =
            rememberDatePickerState(
                initialSelectedDateMillis = selectedDateTime.toLocalDate().toEpochDay() * 86400000L
            )

        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            val newDate =
                                java.time.Instant.ofEpochMilli(millis)
                                    .atZone(java.time.ZoneId.systemDefault())
                                    .toLocalDate()
                            onDateTimeSelected(
                                selectedDateTime
                                    .withYear(newDate.year)
                                    .withMonth(newDate.monthValue)
                                    .withDayOfMonth(newDate.dayOfMonth)
                            )
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
            },
        ) {
            DatePicker(state = datePickerState)
        }
    }

    // Time Picker Dialog
    if (showTimePicker) {
        val timePickerState =
            rememberTimePickerState(
                initialHour = selectedDateTime.hour,
                initialMinute = selectedDateTime.minute,
            )

        Dialog(onDismissRequest = { showTimePicker = false }) {
            Card(modifier = Modifier.padding(16.dp), shape = RoundedCornerShape(16.dp)) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    TimePicker(state = timePickerState)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                    ) {
                        TextButton(onClick = { showTimePicker = false }) {
                            Text(stringResource(R.string.cancel))
                        }
                        TextButton(
                            onClick = {
                                onDateTimeSelected(
                                    selectedDateTime
                                        .withHour(timePickerState.hour)
                                        .withMinute(timePickerState.minute)
                                )
                                showTimePicker = false
                            }
                        ) {
                            Text(stringResource(R.string.confirm))
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EmotionCard(emotion: Emotion, isSelected: Boolean, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.aspectRatio(1f).clip(RoundedCornerShape(16.dp)),
        colors =
            CardDefaults.cardColors(
                containerColor =
                    if (isSelected) {
                        MaterialTheme.colorScheme.primaryContainer
                    } else {
                        MaterialTheme.colorScheme.surfaceVariant
                    }
            ),
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(text = emotion.emoji, style = MaterialTheme.typography.headlineLarge)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stringResource(emotion.nameRes),
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
private fun WeatherChip(
    weather: Weather,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    FilterChip(
        selected = isSelected,
        onClick = onClick,
        label = {
            Text(
                text = "${weather.emoji} ${stringResource(weather.nameRes)}",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
            )
        },
        modifier = modifier,
        border =
            if (!isSelected) {
                BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
            } else null,
    )
}

@Composable
private fun ActivityChip(activity: ActivityType, isSelected: Boolean, onClick: () -> Unit) {
    FilterChip(
        selected = isSelected,
        onClick = onClick,
        label = { Text("${activity.emoji} ${stringResource(activity.nameRes)}") },
    )
}

@Composable
private fun FlowRow(
    modifier: Modifier = Modifier,
    horizontalSpacing: Dp = 0.dp,
    verticalSpacing: Dp = 0.dp,
    content: @Composable () -> Unit,
) {
    Layout(modifier = modifier, content = content) { measurables, constraints ->
        val horizontalSpacingPx = horizontalSpacing.roundToPx()
        val verticalSpacingPx = verticalSpacing.roundToPx()

        val placeables = measurables.map { measurable ->
            measurable.measure(constraints.copy(minWidth = 0, minHeight = 0))
        }

        var width = 0
        var height = 0
        var rowWidth = 0
        var rowHeight = 0

        placeables.forEach { placeable ->
            if (rowWidth + placeable.width > constraints.maxWidth) {
                width = maxOf(width, rowWidth)
                height += rowHeight + verticalSpacingPx
                rowWidth = 0
                rowHeight = 0
            }
            rowWidth += placeable.width + horizontalSpacingPx
            rowHeight = maxOf(rowHeight, placeable.height)
        }

        width = maxOf(width, rowWidth)
        height += rowHeight

        layout(width, height) {
            var x = 0
            var y = 0
            var currentRowHeight = 0

            placeables.forEach { placeable ->
                if (x + placeable.width > constraints.maxWidth) {
                    x = 0
                    y += currentRowHeight + verticalSpacingPx
                    currentRowHeight = 0
                }
                placeable.place(x, y)
                x += placeable.width + horizontalSpacingPx
                currentRowHeight = maxOf(currentRowHeight, placeable.height)
            }
        }
    }
}

enum class Emotion(val emoji: String, val nameRes: Int, val predefinedId: Long) {
    HAPPY("😊", R.string.emotion_happy, 1L),
    CALM("😌", R.string.emotion_calm, 5L),
    TOUCHED("🥺", R.string.emotion_touched, 9L),
    ANXIOUS("😰", R.string.emotion_anxious, 4L),
    WRONGED("😢", R.string.emotion_wronged, 2L),
    TIRED("😔", R.string.emotion_tired, 7L),
}

enum class Weather(val emoji: String, val nameRes: Int) {
    SUNNY("☀️", R.string.weather_sunny),
    CLOUDY("☁️", R.string.weather_cloudy),
    RAINY("🌧️", R.string.weather_rainy),
    SNOWY("❄️", R.string.weather_snowy),
}

enum class ActivityType(val emoji: String, val nameRes: Int) {
    WORK("💼", R.string.activity_work),
    EXERCISE("🏃", R.string.activity_exercise),
    READING("📖", R.string.activity_reading),
    SOCIAL("👥", R.string.activity_social),
    ENTERTAINMENT("🎮", R.string.activity_entertainment),
    REST("😴", R.string.activity_rest),
}
