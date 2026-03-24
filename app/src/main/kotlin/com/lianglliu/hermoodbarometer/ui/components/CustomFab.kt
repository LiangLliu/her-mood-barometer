package com.lianglliu.hermoodbarometer.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lianglliu.hermoodbarometer.core.designsystem.icon.Emojis
import com.lianglliu.hermoodbarometer.core.designsystem.theme.ExtendedTheme
import com.lianglliu.hermoodbarometer.core.locales.R

@Composable
fun CustomFab(onClick: () -> Unit, modifier: Modifier = Modifier) {
    val fabDescription = stringResource(R.string.record_mood)
    val colors = ExtendedTheme.colors
    val gradientBrush =
        remember(colors.accent, colors.accentSoft) {
            Brush.linearGradient(
                colors = listOf(colors.accent, colors.accentSoft),
                start = Offset(0f, 0f),
                end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY),
            )
        }

    Box(
        modifier =
            modifier
                .size(52.dp)
                .shadow(
                    elevation = 12.dp,
                    shape = CircleShape,
                    ambientColor = colors.accent,
                    spotColor = colors.accent,
                )
                .clip(CircleShape)
                .background(brush = gradientBrush)
                .clickable(onClick = onClick)
                .semantics { contentDescription = fabDescription },
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = Emojis.PLUS,
            color = Color.White,
            fontSize = 26.sp,
            fontWeight = FontWeight.Light,
            modifier = Modifier.offset(y = (-2).dp),
        )
    }
}
