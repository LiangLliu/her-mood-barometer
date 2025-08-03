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
import com.lianglliu.hermoodbarometer.domain.model.EmotionType
import com.lianglliu.hermoodbarometer.domain.model.EmotionIntensity

/**
 * 情绪记录页面
 * 用户记录当前情绪的主页面
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecordScreen() {
    var selectedEmotion by remember { mutableStateOf<EmotionType?>(null) }
    var intensityLevel by remember { mutableFloatStateOf(3f) }
    var noteText by remember { mutableStateOf("") }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // 页面标题
        Text(
            text = "记录心情",
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
                    text = "选择情绪类型",
                    style = MaterialTheme.typography.titleMedium
                )
                
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(EmotionType.getDefaultEmotions()) { emotion ->
                        EmotionChip(
                            emotion = emotion,
                            isSelected = selectedEmotion == emotion,
                            onClick = { selectedEmotion = emotion }
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
                    text = "情绪强度",
                    style = MaterialTheme.typography.titleMedium
                )
                
                Slider(
                    value = intensityLevel,
                    onValueChange = { intensityLevel = it },
                    valueRange = 1f..5f,
                    steps = 3,
                    modifier = Modifier.fillMaxWidth()
                )
                
                Text(
                    text = "强度: ${EmotionIntensity.fromLevel(intensityLevel.toInt()).name}",
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
                    text = "添加备注",
                    style = MaterialTheme.typography.titleMedium
                )
                
                OutlinedTextField(
                    value = noteText,
                    onValueChange = { noteText = it },
                    placeholder = { Text("今天心情如何？") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3,
                    maxLines = 5
                )
            }
        }
        
        // 保存按钮
        Button(
            onClick = {
                // TODO: 保存记录逻辑
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = selectedEmotion != null
        ) {
            Text("保存记录")
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

private fun getEmotionDisplayName(emotion: EmotionType): String {
    return when (emotion) {
        EmotionType.HAPPY -> "开心"
        EmotionType.SAD -> "难过"
        EmotionType.ANGRY -> "愤怒"
        EmotionType.ANXIOUS -> "焦虑"
        EmotionType.CALM -> "平静"
        EmotionType.EXCITED -> "兴奋"
        EmotionType.TIRED -> "疲惫"
        EmotionType.CONFUSED -> "困惑"
        EmotionType.GRATEFUL -> "感激"
        EmotionType.LONELY -> "孤独"
    }
}