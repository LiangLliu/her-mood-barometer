package com.lianglliu.hermoodbarometer.ui.screen.settings.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Emoji选择器组件
 * 提供常用的情绪相关emoji供用户选择
 */
@Composable
fun EmojiSelector(
    selectedEmoji: String,
    onEmojiSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val commonEmojis = listOf(
        // 正面情绪
        "😊", "😄", "😃", "😁", "😆", "😂", "🤣", "😍", "🥰", "😘",
        "😋", "😎", "🤩", "🥳", "😇", "🙂", "🙃", "😉", "😌", "😚",
        "😗", "😙", "🤗", "🤤", "😴", "😪", "🤭", "🤫", "🤔", "🙄",
        
        // 负面情绪
        "😢", "😭", "😤", "😠", "😡", "🤬", "😱", "😨", "😰", "😥",
        "😓", "🤯", "😵", "🥴", "😮", "😯", "😲", "😳", "🥺", "😦",
        "😧", "😩", "😫", "😞", "😒", "😔", "😟", "😕", "🙁", "☹️",
        
        // 中性情绪
        "😐", "😑", "😶", "😬", "🙄", "😏", "😪", "😴", "🤐", "🤨",
        "🧐", "🤓", "😈", "👿", "💀", "☠️", "👻", "👽", "🤖", "💩",
        
        // 其他表情
        "🤷", "🤦", "🙋", "🙅", "🙆", "💁", "🤞", "✌️", "🤟", "🤘",
        "👌", "👍", "👎", "👊", "✊", "🤛", "🤜", "👏", "🙌", "👐",
        "🤲", "🤝", "🙏", "✍️", "💅", "🤳", "💪", "🦾", "🦵", "🦿"
    )
    
    LazyVerticalGrid(
        columns = GridCells.Fixed(8),
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(commonEmojis) { emoji ->
            EmojiItem(
                emoji = emoji,
                isSelected = selectedEmoji == emoji,
                onClick = { onEmojiSelected(emoji) }
            )
        }
    }
}

/**
 * 单个Emoji项组件
 */
@Composable
private fun EmojiItem(
    emoji: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .size(48.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) {
                MaterialTheme.colorScheme.primaryContainer
            } else {
                MaterialTheme.colorScheme.surface
            }
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) 8.dp else 2.dp
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = emoji,
                fontSize = 24.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}