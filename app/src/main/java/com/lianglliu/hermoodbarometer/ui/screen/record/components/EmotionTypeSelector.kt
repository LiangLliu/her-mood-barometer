package com.lianglliu.hermoodbarometer.ui.screen.record.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    val emoji: String,
    val color: Color
)

/**
 * 获取情绪信息 - 使用Material 3颜色系统
 */
@Composable
private fun getEmotionInfo(emotion: EmotionType): EmotionInfo {
    return when (emotion) {
        EmotionType.HAPPY -> EmotionInfo(
            displayName = "开心",
            emoji = "😊",
            color = MaterialTheme.colorScheme.primary
        )

        EmotionType.SAD -> EmotionInfo(
            displayName = "难过",
            emoji = "😢",
            color = MaterialTheme.colorScheme.error
        )

        EmotionType.ANGRY -> EmotionInfo(
            displayName = "愤怒",
            emoji = "😡",
            color = MaterialTheme.colorScheme.error
        )

        EmotionType.ANXIOUS -> EmotionInfo(
            displayName = "焦虑",
            emoji = "😰",
            color = MaterialTheme.colorScheme.tertiary
        )

        EmotionType.CALM -> EmotionInfo(
            displayName = "平静",
            emoji = "😌",
            color = MaterialTheme.colorScheme.primary
        )

        EmotionType.EXCITED -> EmotionInfo(
            displayName = "兴奋",
            emoji = "🤩",
            color = MaterialTheme.colorScheme.secondary
        )

        EmotionType.TIRED -> EmotionInfo(
            displayName = "疲惫",
            emoji = "😴",
            color = MaterialTheme.colorScheme.outline
        )

        EmotionType.CONFUSED -> EmotionInfo(
            displayName = "困惑",
            emoji = "😕",
            color = MaterialTheme.colorScheme.outline
        )

        EmotionType.GRATEFUL -> EmotionInfo(
            displayName = "感恩",
            emoji = "🙏",
            color = MaterialTheme.colorScheme.primary
        )

        EmotionType.LONELY -> EmotionInfo(
            displayName = "孤独",
            emoji = "😔",
            color = MaterialTheme.colorScheme.outline
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
            Text(
                text = emotionInfo.emoji,
                fontSize = 32.sp,
                textAlign = TextAlign.Center
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
            Text(
                text = emotion.emoji,
                fontSize = 32.sp,
                textAlign = TextAlign.Center
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