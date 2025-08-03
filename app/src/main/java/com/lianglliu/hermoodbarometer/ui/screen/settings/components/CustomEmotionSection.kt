package com.lianglliu.hermoodbarometer.ui.screen.settings.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.lianglliu.hermoodbarometer.R

/**
 * 自定义情绪设置模块
 */
@Composable
fun CustomEmotionSection(
    onCustomEmotionClick: () -> Unit
) {
    SettingsSection(title = stringResource(R.string.custom_emotions)) {
        SettingsItem(
            icon = Icons.Default.Add,
            title = stringResource(R.string.custom_emotions),
            subtitle = stringResource(R.string.add_custom_emotion),
            trailing = {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null
                )
            },
            onClick = onCustomEmotionClick
        )
    }
} 