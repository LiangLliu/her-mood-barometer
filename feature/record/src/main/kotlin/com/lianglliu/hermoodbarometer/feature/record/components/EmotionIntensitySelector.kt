package com.lianglliu.hermoodbarometer.feature.record.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.lianglliu.hermoodbarometer.core.locales.R

private const val NUMBER_OF_INTENSITY_LEVELS = 5
private const val TAG = "EmotionIntensity" // 用于日志

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
        else -> {
            // 对于非预期值，记录警告并返回一个安全的默认值
            Log.w(TAG, "Unexpected intensity level: $level, defaulting to medium.")
            stringResource(R.string.intensity_medium)
        }
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
    // 定义你的渐变颜色
    val gradientColors = listOf(
        Color(0xFFE64A19),
        Color(0xFFFFEB3B),
        Color(0xFF0288D1),
        Color(0xFF388E3C),
        Color(0xFF673AB7)
    )

    Card(
        modifier = modifier.fillMaxWidth()
            .padding(top = 8.dp, bottom = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = stringResource(
                    R.string.emotion_intensity,
                    getIntensityDisplayName(intensityLevel.toInt())
                ),
                style = MaterialTheme.typography.titleMedium
            )

            Box( // 包裹 Slider 和渐变背景
                modifier = Modifier
                    .fillMaxWidth()
                    .height(24.dp) // 调整高度以匹配 Slider 视觉效果
            ) {
                // 渐变背景
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp) // 轨迹的高度
                        .align(androidx.compose.ui.Alignment.Center) // 使轨迹居中
                        .background(Brush.horizontalGradient(colors = gradientColors))
                )
                // Slider 在渐变背景之上
                Slider(
                    value = intensityLevel,
                    onValueChange = onIntensityChanged,
                    valueRange = 1f..NUMBER_OF_INTENSITY_LEVELS.toFloat(),
                    steps = NUMBER_OF_INTENSITY_LEVELS - 2,
                    modifier = Modifier.fillMaxWidth(),
                    colors = SliderDefaults.colors(
                        thumbColor = MaterialTheme.colorScheme.primary, // 你选择的滑块颜色
                        activeTrackColor = Color.Transparent,           // 活动轨迹透明
                        inactiveTrackColor = Color.Transparent          // 非活动轨迹透明
                    )
                )
            }
        }
    }
}
