package com.lianglliu.hermoodbarometer.ui.screen.record.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
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

private const val MIN_EMOTIONS_FOR_SCROLL_INDICATOR = 12
private val EMOTION_CARD_SIZE = 80.dp // 如果这个尺寸是固定的且多处使用

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

    val predefinedEmotions = remember(context) { // Memoize based on context
        EmotionProvider.getLocalizedDefaultEmotions(context)
    }
    val allEmotions by remember(predefinedEmotions, userEmotions) {
        derivedStateOf { // Or remember { ... }
            predefinedEmotions + userEmotions
        }
    }

    val gridState = rememberLazyGridState()

    // 计算滚动进度
    val scrollProgress by remember {
        derivedStateOf {
            if (gridState.layoutInfo.totalItemsCount == 0) {
                0f
            } else {
                val firstVisibleItemIndex = gridState.firstVisibleItemIndex
                val firstVisibleItemScrollOffset = gridState.firstVisibleItemScrollOffset
                val estimatedTotalContentHeight =
                    gridState.layoutInfo.totalItemsCount * EMOTION_CARD_SIZE.value
                val viewportHeight = gridState.layoutInfo.viewportSize.height
                val scrolledPastHeight =
                    firstVisibleItemIndex * EMOTION_CARD_SIZE.value + firstVisibleItemScrollOffset

                val totalScrollableDistance =
                    (estimatedTotalContentHeight - viewportHeight).coerceAtLeast(1f)

                if (totalScrollableDistance <= 0f) {
                    0f
                } else {
                    (scrolledPastHeight / totalScrollableDistance).coerceIn(0f, 1f)
                }
            }
        }
    }

    OutlinedCard(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        border = BorderStroke(
            0.5.dp,
            MaterialTheme.colorScheme.primary
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(280.dp)
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(top = 0.dp, bottom = 16.dp, start = 4.dp, end = 4.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 滚动进度指示器
            ScrollIndicator(
                isVisible = allEmotions.size > MIN_EMOTIONS_FOR_SCROLL_INDICATOR,
                progress = scrollProgress
            )

            // 情绪网格
            EmotionsGrid(
                emotions = allEmotions,
                gridState = gridState,
                selectedEmotion = selectedEmotion,
                onEmotionClicked = onEmotionSelected // 或者 onCardClicked，取决于你是否采纳了建议2
            )
        }
    }
}

@Composable
private fun ScrollIndicator(
    isVisible: Boolean,
    progress: Float,
    modifier: Modifier = Modifier
) {
    if (isVisible) {
        LinearProgressIndicator(
            progress = { progress },
            modifier = modifier
                .fillMaxWidth()
                .height(4.dp),
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.outlineVariant
        )
    }
}

@Composable
private fun EmotionsGrid(
    emotions: List<Emotion>,
    gridState: LazyGridState,
    selectedEmotion: Emotion?,
    onEmotionClicked: (Emotion) -> Unit, // 或者 onCardClicked
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = EMOTION_CARD_SIZE), // 使用常量
        modifier = modifier
            .fillMaxWidth()
            .height(250.dp), // 这个高度也可以考虑作为参数或常量
        state = gridState,
        contentPadding = PaddingValues(4.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(emotions, key = { it.id }) { emotion ->
            EmotionCard(
                emotion = emotion,
                isSelected = selectedEmotion?.id == emotion.id,
                onCardClicked = onEmotionClicked
            )
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
    onCardClicked: (Emotion) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = { onCardClicked(emotion) },
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