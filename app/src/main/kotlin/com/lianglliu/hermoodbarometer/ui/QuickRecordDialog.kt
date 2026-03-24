package com.lianglliu.hermoodbarometer.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.lianglliu.hermoodbarometer.core.designsystem.icon.Emojis
import com.lianglliu.hermoodbarometer.core.designsystem.theme.ExtendedTheme
import com.lianglliu.hermoodbarometer.core.locales.R
import com.lianglliu.hermoodbarometer.core.model.data.Emotion
import com.lianglliu.hermoodbarometer.core.ui.EmotionProvider
import com.lianglliu.hermoodbarometer.feature.record.components.ModalSectionTitle
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
    val context = LocalContext.current
    val allEmotions = remember(context) { EmotionProvider.getLocalizedDefaultEmotions(context) }

    var selectedEmotion by remember { mutableStateOf<Emotion?>(null) }
    var selectedWeather by remember { mutableStateOf(Weather.SUNNY) }
    var selectedActivities by remember { mutableStateOf(listOf<ActivityType>()) }
    var note by remember { mutableStateOf("") }
    var selectedDateTime by remember { mutableStateOf(LocalDateTime.now()) }

    val colors = ExtendedTheme.colors

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
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        ) {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                Box(
                    modifier = Modifier.fillMaxWidth().padding(top = 12.dp, bottom = 4.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Box(
                        modifier =
                            Modifier.width(36.dp)
                                .height(4.dp)
                                .clip(RoundedCornerShape(2.dp))
                                .background(colors.border)
                    )
                }

                Row(
                    modifier =
                        Modifier.fillMaxWidth()
                            .padding(start = 20.dp, end = 20.dp, top = 8.dp, bottom = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = stringResource(R.string.record_mood),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 20.sp,
                    )
                    Box(
                        modifier =
                            Modifier.size(32.dp)
                                .clip(CircleShape)
                                .background(colors.warmBackground)
                                .clickable(onClick = onDismiss),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(text = Emojis.CLOSE, fontSize = 18.sp, color = colors.textMuted)
                    }
                }

                Column(modifier = Modifier.padding(start = 20.dp, end = 20.dp, bottom = 28.dp)) {
                    DateTimeSelector(
                        selectedDateTime = selectedDateTime,
                        onDateTimeSelected = { selectedDateTime = it },
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    ModalSection(title = stringResource(R.string.label_feeling_moment)) {
                        val gridState = rememberLazyGridState()
                        val scrollProgress by remember {
                            derivedStateOf {
                                val info = gridState.layoutInfo
                                if (info.totalItemsCount == 0) 0f
                                else {
                                    val estimatedTotal = info.totalItemsCount * 70f
                                    val viewport = info.viewportSize.height
                                    val scrolled =
                                        gridState.firstVisibleItemIndex * 70f +
                                            gridState.firstVisibleItemScrollOffset
                                    val scrollable = (estimatedTotal - viewport).coerceAtLeast(1f)
                                    (scrolled / scrollable).coerceIn(0f, 1f)
                                }
                            }
                        }

                        Column {
                            LinearProgressIndicator(
                                progress = { scrollProgress },
                                modifier =
                                    Modifier.fillMaxWidth()
                                        .height(3.dp)
                                        .clip(RoundedCornerShape(1.5.dp)),
                                color = colors.accent,
                                trackColor = colors.borderLight,
                            )

                            LazyVerticalGrid(
                                columns = GridCells.Fixed(3),
                                state = gridState,
                                modifier = Modifier.heightIn(max = 260.dp),
                                contentPadding = PaddingValues(top = 6.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                            ) {
                                items(allEmotions, key = { it.id }) { emotion ->
                                    EmotionCard(
                                        emotion = emotion,
                                        isSelected = selectedEmotion?.id == emotion.id,
                                        onClick = { selectedEmotion = emotion },
                                    )
                                }
                            }
                        }
                    }

                    // Weather section
                    ModalSection(title = stringResource(R.string.label_weather)) {
                        @OptIn(ExperimentalLayoutApi::class)
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            Weather.values().forEach { weather ->
                                WeatherChip(
                                    weather = weather,
                                    isSelected = selectedWeather == weather,
                                    onClick = { selectedWeather = weather },
                                )
                            }
                        }
                    }

                    // Activity section
                    ModalSection(title = stringResource(R.string.label_activity)) {
                        @OptIn(ExperimentalLayoutApi::class)
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
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

                    // Note section
                    ModalSection(title = stringResource(R.string.note_optional)) {
                        OutlinedTextField(
                            value = note,
                            onValueChange = { note = it },
                            placeholder = {
                                Text(
                                    stringResource(R.string.note_placeholder),
                                    color = colors.textHint,
                                    fontSize = 14.sp,
                                )
                            },
                            modifier = Modifier.fillMaxWidth().heightIn(min = 80.dp),
                            minLines = 2,
                            maxLines = 4,
                            textStyle =
                                MaterialTheme.typography.bodyMedium.copy(
                                    fontSize = 14.sp,
                                    color = MaterialTheme.colorScheme.onSurface,
                                ),
                            shape = RoundedCornerShape(14.dp),
                            colors =
                                OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = Color.Transparent,
                                    unfocusedContainerColor = Color.Transparent,
                                    focusedBorderColor = colors.accent,
                                    unfocusedBorderColor = colors.border,
                                    cursorColor = colors.accent,
                                ),
                        )
                    }

                    Button(
                        onClick = {
                            selectedEmotion?.let { emotion ->
                                onConfirm(
                                    emotion,
                                    selectedWeather,
                                    selectedActivities.toList(),
                                    note,
                                    selectedDateTime,
                                )
                            }
                        },
                        enabled = selectedEmotion != null,
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors =
                            ButtonDefaults.buttonColors(
                                containerColor = colors.accent,
                                contentColor = Color.White,
                            ),
                    ) {
                        Text(
                            stringResource(R.string.btn_save_record),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Medium,
                            letterSpacing = 0.3.sp,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ModalSection(title: String, content: @Composable () -> Unit) {
    Column(modifier = Modifier.padding(bottom = 22.dp)) {
        ModalSectionTitle(text = title, modifier = Modifier.padding(bottom = 10.dp))
        content()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DateTimeSelector(
    selectedDateTime: LocalDateTime,
    onDateTimeSelected: (LocalDateTime) -> Unit,
) {
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    val colors = ExtendedTheme.colors

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier =
                Modifier.clip(RoundedCornerShape(100.dp))
                    .background(colors.warmBackground)
                    .clickable { showDatePicker = true }
                    .padding(horizontal = 14.dp, vertical = 8.dp)
        ) {
            Text(
                text =
                    "${Emojis.CALENDAR} ${
                        selectedDateTime.format(
                            DateTimeFormatter.ofPattern(
                                stringResource(R.string.date_format_full),
                                Locale.getDefault(),
                            )
                        )
                    }",
                fontSize = 13.sp,
                color = colors.textSecondary,
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Text(text = Emojis.MIDDLE_DOT, fontSize = 13.sp, color = colors.textHint)

        Spacer(modifier = Modifier.width(12.dp))

        // Time pill
        Box(
            modifier =
                Modifier.clip(RoundedCornerShape(100.dp))
                    .background(colors.warmBackground)
                    .clickable { showTimePicker = true }
                    .padding(horizontal = 14.dp, vertical = 8.dp)
        ) {
            val hour = selectedDateTime.hour
            val minute = selectedDateTime.minute
            val amPm =
                if (hour < 12) stringResource(R.string.time_am)
                else stringResource(R.string.time_pm)
            val displayHour =
                when {
                    hour == 0 -> 12
                    hour > 12 -> hour - 12
                    else -> hour
                }
            Text(
                text = "${Emojis.ALARM} $amPm $displayHour:${minute.toString().padStart(2, '0')}",
                fontSize = 13.sp,
                color = colors.textSecondary,
            )
        }
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

@Composable
private fun EmotionCard(emotion: Emotion, isSelected: Boolean, onClick: () -> Unit) {
    val colors = ExtendedTheme.colors

    val borderColor = if (isSelected) colors.accent else Color.Transparent
    val backgroundColor = if (isSelected) colors.accentBg else colors.warmBackground
    val textColor = if (isSelected) colors.accent else colors.textSecondary

    Box(
        modifier =
            Modifier.clip(RoundedCornerShape(14.dp))
                .background(backgroundColor)
                .border(2.dp, borderColor, RoundedCornerShape(14.dp))
                .clickable(onClick = onClick)
                .padding(14.dp, 10.dp),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(text = emotion.emoji, fontSize = 30.sp)
            Text(
                text = emotion.name,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = textColor,
                textAlign = TextAlign.Center,
                maxLines = 1,
            )
        }
    }
}

@Composable
private fun WeatherChip(weather: Weather, isSelected: Boolean, onClick: () -> Unit) {
    val colors = ExtendedTheme.colors
    val backgroundColor = if (isSelected) colors.accentBg else colors.warmBackground
    val borderColor = if (isSelected) colors.accentSoft else colors.borderLight
    val textColor = if (isSelected) colors.accent else colors.textSecondary

    Box(
        modifier =
            Modifier.clip(RoundedCornerShape(100.dp))
                .background(backgroundColor)
                .border(1.5.dp, borderColor, RoundedCornerShape(100.dp))
                .clickable(onClick = onClick)
                .padding(horizontal = 14.dp, vertical = 7.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "${weather.emoji} ${stringResource(weather.nameRes)}",
            fontSize = 13.sp,
            color = textColor,
        )
    }
}

@Composable
private fun ActivityChip(activity: ActivityType, isSelected: Boolean, onClick: () -> Unit) {
    val colors = ExtendedTheme.colors
    val backgroundColor = if (isSelected) colors.accentBg else colors.warmBackground
    val borderColor = if (isSelected) colors.accentSoft else colors.borderLight
    val textColor = if (isSelected) colors.accent else colors.textSecondary

    Box(
        modifier =
            Modifier.clip(RoundedCornerShape(100.dp))
                .background(backgroundColor)
                .border(1.5.dp, borderColor, RoundedCornerShape(100.dp))
                .clickable(onClick = onClick)
                .padding(horizontal = 14.dp, vertical = 7.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "${activity.emoji} ${stringResource(activity.nameRes)}",
            fontSize = 13.sp,
            color = textColor,
        )
    }
}

enum class Weather(val emoji: String, val nameRes: Int) {
    SUNNY(Emojis.SUNNY, R.string.weather_sunny),
    CLOUDY(Emojis.CLOUDY, R.string.weather_cloudy),
    RAINY(Emojis.RAINY, R.string.weather_rainy),
    SNOWY(Emojis.SNOWY, R.string.weather_snowy),
}

enum class ActivityType(val emoji: String, val nameRes: Int) {
    WORK(Emojis.BRIEFCASE, R.string.activity_work),
    EXERCISE(Emojis.RUNNING, R.string.activity_exercise),
    READING(Emojis.BOOK, R.string.activity_reading),
    SOCIAL(Emojis.PEOPLE, R.string.activity_social),
    ENTERTAINMENT(Emojis.GAME, R.string.activity_entertainment),
    REST(Emojis.SLEEPING, R.string.activity_rest),
}
