package com.lianglliu.hermoodbarometer.ui.screen.record.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.lianglliu.hermoodbarometer.R
import com.lianglliu.hermoodbarometer.domain.model.CustomEmotion
import com.lianglliu.hermoodbarometer.domain.model.EmotionType

/**
 * 情绪类型选择器组件
 * 支持预定义情绪和自定义情绪的选择
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmotionTypeSelector(
    selectedEmotion: EmotionType?,
    selectedCustomEmotion: CustomEmotion?,
    customEmotions: List<CustomEmotion>,
    onEmotionSelected: (EmotionType?) -> Unit,
    onCustomEmotionSelected: (CustomEmotion?) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = stringResource(R.string.select_emotion),
                style = MaterialTheme.typography.titleMedium
            )
            
            // 预定义情绪
            Text(
                text = stringResource(R.string.predefined_emotions),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(EmotionType.values()) { emotion ->
                    val isSelected = selectedEmotion == emotion && selectedCustomEmotion == null
                    val emotionInfo = getEmotionInfo(emotion)
                    EmotionCard(
                        emotionInfo = emotionInfo,
                        isSelected = isSelected,
                        onClick = {
                            onEmotionSelected(emotion)
                            onCustomEmotionSelected(null)
                        }
                    )
                }
            }
            
            // 自定义情绪
            if (customEmotions.isNotEmpty()) {
                Text(
                    text = stringResource(R.string.custom_emotions),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(customEmotions) { emotion ->
                        val isSelected = selectedCustomEmotion == emotion
                        CustomEmotionCard(
                            emotion = emotion,
                            isSelected = isSelected,
                            onClick = {
                                onCustomEmotionSelected(emotion)
                                onEmotionSelected(null)
                            }
                        )
                    }
                }
            }
        }
    }
}

/**
 * 情绪信息数据类
 */
private data class EmotionInfo(
    val displayName: String,
    val icon: ImageVector,
    val color: Color
)

/**
 * 获取情绪信息
 */
private fun getEmotionInfo(emotion: EmotionType): EmotionInfo {
    return when (emotion) {
        EmotionType.HAPPY -> EmotionInfo(
            displayName = "开心",
            icon = Icons.Default.ThumbUp,
            color = Color(0xFF4CAF50)
        )
        EmotionType.SAD -> EmotionInfo(
            displayName = "难过",
            icon = Icons.Default.Info,
            color = Color(0xFF2196F3)
        )
        EmotionType.ANGRY -> EmotionInfo(
            displayName = "愤怒",
            icon = Icons.Default.Warning,
            color = Color(0xFFFF5722)
        )
        EmotionType.EXCITED -> EmotionInfo(
            displayName = "兴奋",
            icon = Icons.Default.Star,
            color = Color(0xFFFF9800)
        )
        EmotionType.CALM -> EmotionInfo(
            displayName = "平静",
            icon = Icons.Default.Home,
            color = Color(0xFF9C27B0)
        )
        EmotionType.CONFUSED -> EmotionInfo(
            displayName = "困惑",
            icon = Icons.Default.Person,
            color = Color(0xFF607D8B)
        )
        EmotionType.ANXIOUS -> EmotionInfo(
            displayName = "焦虑",
            icon = Icons.Default.Warning,
            color = Color(0xFFFF9800)
        )
        EmotionType.TIRED -> EmotionInfo(
            displayName = "疲惫",
            icon = Icons.Default.Home,
            color = Color(0xFF795548)
        )
        EmotionType.GRATEFUL -> EmotionInfo(
            displayName = "感恩",
            icon = Icons.Default.Person,
            color = Color(0xFFFF5722)
        )
        EmotionType.LONELY -> EmotionInfo(
            displayName = "孤独",
            icon = Icons.Default.Person,
            color = Color(0xFF9E9E9E)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EmotionCard(
    emotionInfo: EmotionInfo,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        onClick = {
            // 添加调试信息
            android.util.Log.d("EmotionCard", "Clicked: ${emotionInfo.displayName}, isSelected: $isSelected")
            onClick()
        },
        modifier = Modifier.size(80.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) {
                MaterialTheme.colorScheme.primaryContainer
            } else {
                MaterialTheme.colorScheme.surface
            }
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) 8.dp else 2.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = emotionInfo.icon,
                contentDescription = emotionInfo.displayName,
                modifier = Modifier.size(24.dp),
                tint = if (isSelected) {
                    MaterialTheme.colorScheme.onPrimaryContainer
                } else {
                    emotionInfo.color
                }
            )
            
            Text(
                text = emotionInfo.displayName,
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center,
                color = if (isSelected) {
                    MaterialTheme.colorScheme.onPrimaryContainer
                } else {
                    MaterialTheme.colorScheme.onSurface
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CustomEmotionCard(
    emotion: CustomEmotion,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.size(80.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) {
                MaterialTheme.colorScheme.primaryContainer
            } else {
                MaterialTheme.colorScheme.surface
            }
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) 8.dp else 2.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = emotion.name,
                modifier = Modifier.size(24.dp),
                tint = if (isSelected) {
                    MaterialTheme.colorScheme.onPrimaryContainer
                } else {
                    Color(android.graphics.Color.parseColor(emotion.color))
                }
            )
            
            Text(
                text = emotion.name,
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center,
                color = if (isSelected) {
                    MaterialTheme.colorScheme.onPrimaryContainer
                } else {
                    MaterialTheme.colorScheme.onSurface
                }
            )
        }
    }
} 