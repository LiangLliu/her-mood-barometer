package com.lianglliu.hermoodbarometer.ui.screen.settings.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.lianglliu.hermoodbarometer.R

/**
 * 外观设置模块
 */
@Composable
fun AppearanceSection(
    selectedTheme: String,
    selectedLanguage: String,
    onThemeChanged: (String) -> Unit,
    onLanguageClick: () -> Unit
) {
    SettingsSection(title = stringResource(R.string.appearance)) {
        // 主题设置
        SettingsItem(
            icon = Icons.Default.Star,
            title = stringResource(R.string.dark_mode),
            subtitle = stringResource(R.string.dark_mode_description),
            trailing = {
                Switch(
                    checked = selectedTheme == "dark",
                    onCheckedChange = { isDark ->
                        val theme = if (isDark) "dark" else "light"
                        onThemeChanged(theme)
                    }
                )
            }
        )
        
        // 语言设置（加入“跟随系统”）
        SettingsItem(
            icon = Icons.Default.Info,
            title = stringResource(R.string.language),
            subtitle = getLanguageDisplayName(selectedLanguage),
            trailing = {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null
                )
            },
            onClick = onLanguageClick
        )
    }
}

/**
 * 获取语言显示名称
 */
@Composable
private fun getLanguageDisplayName(languageCode: String): String {
    return when (languageCode) {
        "system", "default" -> stringResource(R.string.system_theme)
        "zh" -> stringResource(R.string.language_zh)
        "zh-TW" -> stringResource(R.string.language_zh_tw)
        "ja" -> stringResource(R.string.language_ja)
        "ko" -> stringResource(R.string.language_ko)
        "en" -> stringResource(R.string.language_en)
        else -> stringResource(R.string.language_en)
    }
} 