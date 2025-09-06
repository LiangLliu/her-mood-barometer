package com.lianglliu.hermoodbarometer.ui.screen.record.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lianglliu.hermoodbarometer.domain.model.Emotion
import com.lianglliu.hermoodbarometer.domain.model.EmotionProvider

/**
 * 统一的情绪选择器组件
 * 支持预定义情绪和自定义情绪的选择
 * 包含滚动功能和进度指示器
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
    val gridState = rememberLazyGridState()
    
    // 计算滚动进度
    val scrollProgress by remember {
        derivedStateOf {
            val layoutInfo = gridState.layoutInfo
            if (layoutInfo.totalItemsCount == 0) return@derivedStateOf 0f
            
            val visibleItemsInfo = layoutInfo.visibleItemsInfo
            if (visibleItemsInfo.isEmpty()) return@derivedStateOf 0f
            
            val firstVisibleItem = visibleItemsInfo.first()
            val lastVisibleItem = visibleItemsInfo.last()
            
            val firstVisibleItemScrollOffset = firstVisibleItem.offset.packedValue

            val totalScrollRange = layoutInfo.viewportEndOffset - layoutInfo.viewportStartOffset
            val currentScrollOffset = firstVisibleItemScrollOffset
            
            currentScrollOffset.toFloat() / (totalScrollRange - layoutInfo.viewportEndOffset + layoutInfo.viewportStartOffset).coerceAtLeast(1)
        }
    }
    
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 滚动进度指示器
        if (allEmotions.size > 12) { // 只在需要滚动时显示进度条
            LinearProgressIndicator(
                progress = scrollProgress,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.outlineVariant
            )
        }
        
        // 情绪网格
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 80.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            state = gridState,
            contentPadding = androidx.compose.foundation.layout.PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
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
        modifier = modifier.size(80.dp),
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
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // 表情符号
            Text(
                text = emotion.emoji,
                fontSize = 28.sp,
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