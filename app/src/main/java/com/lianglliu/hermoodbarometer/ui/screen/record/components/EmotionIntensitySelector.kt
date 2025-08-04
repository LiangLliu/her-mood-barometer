package com.lianglliu.hermoodbarometer.ui.screen.record.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.lianglliu.hermoodbarometer.R
import com.lianglliu.hermoodbarometer.domain.model.EmotionIntensity

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
                text = stringResource(R.string.intensity_level, EmotionIntensity.fromLevel(intensityLevel.toInt()).name),
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
} 