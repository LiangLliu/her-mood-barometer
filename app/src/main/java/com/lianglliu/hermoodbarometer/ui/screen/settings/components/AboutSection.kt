package com.lianglliu.hermoodbarometer.ui.screen.settings.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.lianglliu.hermoodbarometer.R

/**
 * 关于设置模块
 */
@Composable
fun AboutSection() {
    SettingsSection(title = stringResource(R.string.about)) {
        SettingsItem(
            icon = Icons.Default.Info,
            title = stringResource(R.string.version),
            subtitle = "1.0.0",
            trailing = null
        )
        
        SettingsItem(
            icon = Icons.Default.Favorite,
            title = stringResource(R.string.about_app),
            subtitle = stringResource(R.string.about_app_description),
            trailing = null
        )
    }
} 