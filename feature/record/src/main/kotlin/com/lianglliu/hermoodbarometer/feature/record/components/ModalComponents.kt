package com.lianglliu.hermoodbarometer.feature.record.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lianglliu.hermoodbarometer.core.designsystem.icon.Emojis
import com.lianglliu.hermoodbarometer.core.designsystem.theme.ExtendedTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/** Section title component for modal sections */
@Composable
fun ModalSectionTitle(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        fontSize = 12.sp,
        fontWeight = FontWeight.Medium,
        color = ExtendedTheme.colors.textMuted,
        letterSpacing = 0.5.sp,
        modifier = modifier,
    )
}

/**
 * DateTime row with date and time pills. Date/time is captured at first composition and does not
 * auto-update — it represents the record creation time.
 */
@Composable
fun DateTimeRow(modifier: Modifier = Modifier) {
    val currentDate = remember { Date() }
    val dateFormat = remember { SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()) }
    val timeFormat = remember { SimpleDateFormat("HH:mm", Locale.getDefault()) }

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        DateTimePill(emoji = Emojis.CALENDAR, text = dateFormat.format(currentDate))

        Text(
            text = " ${Emojis.MIDDLE_DOT} ",
            color = ExtendedTheme.colors.textHint,
            fontSize = 13.sp,
        )

        DateTimePill(emoji = Emojis.ALARM, text = timeFormat.format(currentDate))
    }
}

@Composable
private fun DateTimePill(emoji: String, text: String, onClick: (() -> Unit)? = null) {
    Box(
        modifier =
            Modifier.background(ExtendedTheme.colors.warmBackground, RoundedCornerShape(50))
                .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier)
                .padding(horizontal = 14.dp, vertical = 8.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(text = emoji, fontSize = 13.sp)
            Text(text = text, fontSize = 13.sp, color = ExtendedTheme.colors.textSecondary)
        }
    }
}
