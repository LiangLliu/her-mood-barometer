package com.lianglliu.hermoodbarometer.feature.record.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lianglliu.hermoodbarometer.core.designsystem.theme.ExtendedTheme
import com.lianglliu.hermoodbarometer.core.locales.R
import timber.log.Timber

private const val NUMBER_OF_INTENSITY_LEVELS = 5

@Composable
private fun getIntensityDisplayName(level: Int): String {
    return when (level) {
        1 -> stringResource(R.string.intensity_very_low)
        2 -> stringResource(R.string.intensity_low)
        3 -> stringResource(R.string.intensity_medium)
        4 -> stringResource(R.string.intensity_high)
        5 -> stringResource(R.string.intensity_very_high)
        else -> {
            Timber.w("Unexpected intensity level: $level, defaulting to medium.")
            stringResource(R.string.intensity_medium)
        }
    }
}

@Composable
fun EmotionIntensitySelector(
    intensityLevel: Float,
    onIntensityChanged: (Float) -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = ExtendedTheme.colors
    val accentColor = colors.accent

    val gradientColors =
        listOf(
            accentColor.copy(alpha = 0.3f),
            accentColor.copy(alpha = 0.5f),
            accentColor.copy(alpha = 0.7f),
            accentColor.copy(alpha = 0.85f),
            accentColor,
        )

    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .border(1.dp, colors.borderLight, RoundedCornerShape(20.dp))
                .background(colors.cardBackground)
                .padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = stringResource(R.string.emotion_intensity),
            style = MaterialTheme.typography.labelSmall,
            color = colors.textMuted,
            letterSpacing = 1.sp,
        )

        Text(
            text = getIntensityDisplayName(intensityLevel.toInt()),
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface,
        )

        Box(modifier = Modifier.fillMaxWidth().height(24.dp)) {
            Box(
                modifier =
                    Modifier.fillMaxWidth()
                        .height(8.dp)
                        .align(androidx.compose.ui.Alignment.Center)
                        .clip(RoundedCornerShape(4.dp))
                        .background(Brush.horizontalGradient(colors = gradientColors))
            )
            Slider(
                value = intensityLevel,
                onValueChange = onIntensityChanged,
                valueRange = 1f..NUMBER_OF_INTENSITY_LEVELS.toFloat(),
                steps = NUMBER_OF_INTENSITY_LEVELS - 2,
                modifier = Modifier.fillMaxWidth(),
                colors =
                    SliderDefaults.colors(
                        thumbColor = accentColor,
                        activeTrackColor = Color.Transparent,
                        inactiveTrackColor = Color.Transparent,
                    ),
            )
        }
    }
}
