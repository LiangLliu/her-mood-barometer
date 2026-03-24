package com.lianglliu.hermoodbarometer.feature.diary

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lianglliu.hermoodbarometer.core.designsystem.component.CsIconButton
import com.lianglliu.hermoodbarometer.core.designsystem.component.CsTopAppBar
import com.lianglliu.hermoodbarometer.core.designsystem.icon.AppIcons
import com.lianglliu.hermoodbarometer.core.designsystem.icon.outlined.Delete
import com.lianglliu.hermoodbarometer.core.locales.R
import com.lianglliu.hermoodbarometer.core.model.data.EmotionRecord
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun DiaryScreen(modifier: Modifier = Modifier, viewModel: DiaryViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(topBar = { CsTopAppBar(titleRes = R.string.nav_diary) }, modifier = modifier) {
        paddingValues ->
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
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        recordsByDate.forEach { (date, records) ->
            stickyHeader { DateHeader(date = date) }

            items(items = records, key = { it.id }) { record ->
                EmotionRecordCard(
                    record = record,
                    onDelete = { onDeleteRecord(record.id) },
                    modifier = Modifier.animateItem(),
                )
            }
        }
    }
}

@Composable
private fun DateHeader(date: LocalDate, modifier: Modifier = Modifier) {
    val isToday = date == LocalDate.now()
    val isYesterday = date == LocalDate.now().minusDays(1)

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

    Box(
        modifier =
            modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(vertical = 8.dp)
    ) {
        Text(
            text = dateText,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EmotionRecordCard(
    record: EmotionRecord,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var showDeleteDialog by remember { mutableStateOf(false) }

    Card(
        modifier = modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
    ) {
        Row(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.Top) {
            // Time
            Text(
                text = record.getLocalDateTime().format(DateTimeFormatter.ofPattern("HH:mm")),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.width(50.dp),
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Content
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                // Emotion
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Text(text = record.emotionEmoji, style = MaterialTheme.typography.headlineSmall)
                    // We can show just the emoji since we don't have emotion name

                    // Intensity indicator
                    IntensityIndicator(intensity = record.intensity.level)
                }

                // Note
                if (record.note.isNotBlank()) {
                    Text(
                        text = record.note,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }

            // Delete button
            CsIconButton(
                icon = AppIcons.Outlined.Delete,
                onClick = { showDeleteDialog = true },
                contentDescription = stringResource(R.string.delete),
            )
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
private fun IntensityIndicator(intensity: Int, modifier: Modifier = Modifier) {
    Row(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(2.dp)) {
        repeat(5) { index ->
            Box(
                modifier =
                    Modifier.size(4.dp, 12.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(
                            if (index < intensity) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.surfaceVariant
                            }
                        )
            )
        }
    }
}

@Composable
private fun EmptyDiaryState(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(text = "📔", style = MaterialTheme.typography.displayLarge)
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.diary_empty),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.diary_empty_hint),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}
