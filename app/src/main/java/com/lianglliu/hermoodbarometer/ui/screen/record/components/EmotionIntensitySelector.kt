package com.lianglliu.hermoodbarometer.ui.screen.record.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.lianglliu.hermoodbarometer.R
import com.lianglliu.hermoodbarometer.domain.model.EmotionIntensity

/**
 * 获取情绪强度的本地化显示名称
 */
@Composable
private fun getIntensityDisplayName(level: Int): String {
    return when (level) {
        1 -> stringResource(R.string.intensity_very_low)
        2 -> stringResource(R.string.intensity_low)
        3 -> stringResource(R.string.intensity_medium)
        4 -> stringResource(R.string.intensity_high)
        5 -> stringResource(R.string.intensity_very_high)
        else -> stringResource(R.string.intensity_medium)
    }
}

/**
 * 情绪强度选择器组件
 */
@Composable
fun EmotionIntensitySelector(
    intensityLevel: Float,
    onIntensityChanged: (Float) -> Unit,
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
                text = stringResource(R.string.emotion_intensity),
                style = MaterialTheme.typography.titleMedium
            )
            
            Slider(
                value = intensityLevel,
                onValueChange = onIntensityChanged,
                valueRange = 1f..5f,
                steps = 3,
                modifier = Modifier.fillMaxWidth()
            )
            
            Text(
                text = stringResource(R.string.intensity_level, getIntensityDisplayName(intensityLevel.toInt())),
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
} 