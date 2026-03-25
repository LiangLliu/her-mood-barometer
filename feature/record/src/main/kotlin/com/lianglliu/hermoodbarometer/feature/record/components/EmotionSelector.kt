package com.lianglliu.hermoodbarometer.feature.record.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lianglliu.hermoodbarometer.core.designsystem.theme.ExtendedTheme
import com.lianglliu.hermoodbarometer.core.model.data.Emotion
import com.lianglliu.hermoodbarometer.core.ui.EmotionProvider

private const val MIN_EMOTIONS_FOR_SCROLL_INDICATOR = 12
private val EMOTION_CARD_SIZE = 80.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmotionSelector(
    selectedEmotion: Emotion?,
    userEmotions: List<Emotion>,
    onEmotionSelected: (Emotion) -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val colors = ExtendedTheme.colors

    val predefinedEmotions =
        remember(configuration) { EmotionProvider.getLocalizedDefaultEmotions(context) }
    val allEmotions by
        remember(predefinedEmotions, userEmotions) {
            derivedStateOf { predefinedEmotions + userEmotions }
        }

    val gridState = rememberLazyGridState()

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
                if (totalScrollableDistance <= 0f) 0f
                else (scrolledPastHeight / totalScrollableDistance).coerceIn(0f, 1f)
            }
        }
    }

    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .border(1.dp, colors.borderLight, RoundedCornerShape(20.dp))
                .background(colors.cardBackground)
                .padding(top = 0.dp, bottom = 16.dp, start = 4.dp, end = 4.dp)
                .height(280.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        // Scroll indicator
        if (allEmotions.size > MIN_EMOTIONS_FOR_SCROLL_INDICATOR) {
            LinearProgressIndicator(
                progress = { scrollProgress },
                modifier = Modifier.fillMaxWidth().height(3.dp),
                color = colors.accent,
                trackColor = colors.borderLight,
            )
        }

        // Emotion grid
        EmotionsGrid(
            emotions = allEmotions,
            gridState = gridState,
            selectedEmotion = selectedEmotion,
            onEmotionClicked = onEmotionSelected,
        )
    }
}

@Composable
private fun EmotionsGrid(
    emotions: List<Emotion>,
    gridState: LazyGridState,
    selectedEmotion: Emotion?,
    onEmotionClicked: (Emotion) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = modifier.fillMaxWidth().height(250.dp),
        state = gridState,
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(emotions, key = { it.id }) { emotion ->
            EmotionCard(
                emotion = emotion,
                isSelected = selectedEmotion?.id == emotion.id,
                onCardClicked = onEmotionClicked,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EmotionCard(
    emotion: Emotion,
    isSelected: Boolean,
    onCardClicked: (Emotion) -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = ExtendedTheme.colors

    Card(
        onClick = { onCardClicked(emotion) },
        modifier =
            modifier.then(
                if (isSelected) Modifier.border(2.dp, colors.accent, RoundedCornerShape(14.dp))
                else Modifier
            ),
        shape = RoundedCornerShape(14.dp),
        colors =
            CardDefaults.cardColors(
                containerColor = if (isSelected) colors.accentBg else colors.warmBackground
            ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(14.dp, 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(text = emotion.emoji, fontSize = 30.sp, textAlign = TextAlign.Center)
            Text(
                text = emotion.name,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                style = MaterialTheme.typography.labelSmall,
                color = if (isSelected) colors.accent else colors.textSecondary,
                textAlign = TextAlign.Center,
                maxLines = 1,
            )
        }
    }
}
