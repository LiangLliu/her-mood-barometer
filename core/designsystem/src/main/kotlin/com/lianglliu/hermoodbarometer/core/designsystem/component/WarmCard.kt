package com.lianglliu.hermoodbarometer.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lianglliu.hermoodbarometer.core.designsystem.theme.ExtendedTheme

/**
 * A warm-styled card container with rounded corners, border, and background.
 *
 * @param modifier Modifier to be applied to the card
 * @param content The content to be displayed inside the card
 */
@Composable
fun WarmCard(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    val colors = ExtendedTheme.colors
    Box(
        modifier =
            modifier
                .clip(RoundedCornerShape(20.dp))
                .border(1.dp, colors.borderLight, RoundedCornerShape(20.dp))
                .background(colors.cardBackground)
                .padding(18.dp)
    ) {
        Column { content() }
    }
}

/**
 * A label text for card sections with muted color and letter spacing.
 *
 * @param text The label text to display
 * @param modifier Modifier to be applied to the text
 */
@Composable
fun CardLabel(text: String, modifier: Modifier = Modifier) {
    val colors = ExtendedTheme.colors
    Text(
        text = text,
        style = MaterialTheme.typography.labelSmall,
        color = colors.textMuted,
        letterSpacing = 1.sp,
        modifier = modifier.padding(bottom = 12.dp),
    )
}

/**
 * A row displaying a statistic with emoji, label, and value. Used within WarmCard for displaying
 * statistics.
 *
 * @param emoji The emoji icon to display
 * @param label The label text describing the statistic
 * @param value The value of the statistic
 * @param showDivider Whether to show a divider below this row (default: true)
 */
@Composable
fun StatRow(emoji: String, label: String, value: String, showDivider: Boolean = true) {
    val colors = ExtendedTheme.colors
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(text = emoji, fontSize = 14.sp)
            Text(text = label, fontSize = 13.5.sp, color = colors.textSecondary)
        }
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
    if (showDivider) {
        HorizontalDivider(thickness = 1.dp, color = colors.borderLight)
    }
}
