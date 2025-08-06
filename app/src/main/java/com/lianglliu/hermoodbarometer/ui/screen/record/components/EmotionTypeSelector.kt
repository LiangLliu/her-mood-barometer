package com.lianglliu.hermoodbarometer.ui.screen.record.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.lianglliu.hermoodbarometer.R
import com.lianglliu.hermoodbarometer.domain.model.EmotionType

/**
 * 情绪类型选择器组件
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmotionTypeSelector(
    selectedEmotion: EmotionType?,
    onEmotionSelected: (EmotionType) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth()
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
                        isSelected = selectedEmotion == emotion,
                        onClick = { onEmotionSelected(emotion) }
                    )
                }
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