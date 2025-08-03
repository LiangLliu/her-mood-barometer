package com.lianglliu.hermoodbarometer.ui.screen.record

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lianglliu.hermoodbarometer.R
import com.lianglliu.hermoodbarometer.domain.model.EmotionType
import com.lianglliu.hermoodbarometer.domain.model.EmotionIntensity

/**
 * 情绪记录页面
 * 用户记录当前情绪的主页面
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecordScreen(
    viewModel: RecordViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    
    // 处理成功消息
    LaunchedEffect(uiState.showSuccessMessage) {
        if (uiState.showSuccessMessage) {
            viewModel.clearSuccessMessage()
        }
    }
    
    // 处理错误消息
    LaunchedEffect(uiState.errorMessage) {
        if (uiState.errorMessage != null) {
            // 可以在这里显示Snackbar或其他错误提示
        }
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // 页面标题
        Text(
            text = stringResource(R.string.record_title),
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        
        // 情绪类型选择
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = stringResource(R.string.select_emotion_type),
                    style = MaterialTheme.typography.titleMedium
                )
                
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(EmotionType.getDefaultEmotions()) { emotion ->
                        EmotionChip(
                            emotion = emotion,
                            isSelected = uiState.selectedEmotion == emotion,
                            onClick = { viewModel.updateSelectedEmotion(emotion) }
                        )
                    }
                }
            }
        }
        
        // 情绪强度滑块
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = stringResource(R.string.emotion_intensity),
                    style = MaterialTheme.typography.titleMedium
                )
                
                Slider(
                    value = uiState.intensityLevel,
                    onValueChange = { viewModel.updateIntensity(it) },
                    valueRange = 1f..5f,
                    steps = 3,
                    modifier = Modifier.fillMaxWidth()
                )
                
                Text(
                    text = stringResource(R.string.intensity_level, EmotionIntensity.fromLevel(uiState.intensityLevel.toInt()).name),
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
        
        // 备注输入
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = stringResource(R.string.add_note),
                    style = MaterialTheme.typography.titleMedium
                )
                
                OutlinedTextField(
                    value = uiState.noteText,
                    onValueChange = { viewModel.updateNote(it) },
                    placeholder = { Text(stringResource(R.string.note_placeholder)) },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3,
                    maxLines = 5
                )
            }
        }
        
        // 保存按钮
        Button(
            onClick = { viewModel.saveEmotionRecord() },
            modifier = Modifier.fillMaxWidth(),
            enabled = uiState.selectedEmotion != null && !uiState.isLoading
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Text(stringResource(R.string.save_record))
            }
        }
        
        // 错误消息显示
        uiState.errorMessage?.let { errorMessage ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Text(
                    text = errorMessage,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
        
        // 成功消息显示
        if (uiState.showSuccessMessage) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Text(
                    text = stringResource(R.string.record_saved_successfully),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EmotionChip(
    emotion: EmotionType,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    FilterChip(
        selected = isSelected,
        onClick = onClick,
        label = { 
            Text(
                text = getEmotionDisplayName(emotion),
                style = MaterialTheme.typography.bodySmall
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = null
            )
        }
    )
}

@Composable
private fun getEmotionDisplayName(emotion: EmotionType): String {
    return when (emotion) {
        EmotionType.HAPPY -> stringResource(R.string.emotion_happy)
        EmotionType.SAD -> stringResource(R.string.emotion_sad)
        EmotionType.ANGRY -> stringResource(R.string.emotion_angry)
        EmotionType.ANXIOUS -> stringResource(R.string.emotion_anxious)
        EmotionType.CALM -> stringResource(R.string.emotion_calm)
        EmotionType.EXCITED -> stringResource(R.string.emotion_excited)
        EmotionType.TIRED -> stringResource(R.string.emotion_tired)
        EmotionType.CONFUSED -> stringResource(R.string.emotion_confused)
        EmotionType.GRATEFUL -> stringResource(R.string.emotion_grateful)
        EmotionType.LONELY -> stringResource(R.string.emotion_lonely)
    }
}