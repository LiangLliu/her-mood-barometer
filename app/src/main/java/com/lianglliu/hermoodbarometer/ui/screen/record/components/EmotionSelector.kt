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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lianglliu.hermoodbarometer.domain.model.Emotion
import com.lianglliu.hermoodbarometer.domain.model.EmotionProvider

/**
 * 统一的情绪选择器组件
 * 支持预定义情绪和自定义情绪的选择
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmotionSelector(
    selectedEmotion: Emotion?,
    userEmotions: List<Emotion>,
    onEmotionSelected: (Emotion) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val predefinedEmotions = EmotionProvider.getLocalizedDefaultEmotions(context)
    val allEmotions = predefinedEmotions + userEmotions
    
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 所有情绪统一显示
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 16.dp)
        ) {
            items(allEmotions) { emotion ->
                EmotionCard(
                    emotion = emotion,
                    isSelected = selectedEmotion?.id == emotion.id,
                    onClick = { onEmotionSelected(emotion) }
                )
            }
        }
    }
}

/**
 * 单个情绪卡片组件
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EmotionCard(
    emotion: Emotion,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier.size(80.dp), // 增大卡片尺寸
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
                .padding(10.dp), // 增大内边距
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // 表情符号
            Text(
                text = emotion.emoji,
                fontSize = 28.sp, // 增大表情符号字体
                textAlign = TextAlign.Center
            )
            
            // 情绪名称
            Text(
                text = emotion.name,
                fontSize = 14.sp,
                style = MaterialTheme.typography.labelSmall,
                color = if (isSelected) {
                    MaterialTheme.colorScheme.onPrimaryContainer
                } else {
                    MaterialTheme.colorScheme.onSurface
                },
                textAlign = TextAlign.Center,
                maxLines = 1
            )
        }
    }
}

/**
 * 情绪信息数据类（用于UI显示）
 */
private data class EmotionInfo(
    val displayName: String,
    val emoji: String,
    val color: Color
)
