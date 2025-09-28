package com.lianglliu.hermoodbarometer.feature.settings.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.lianglliu.hermoodbarometer.core.designsystem.icon.AppIcons
import com.lianglliu.hermoodbarometer.core.designsystem.icon.outlined.ChevronRight
import com.lianglliu.hermoodbarometer.core.designsystem.icon.outlined.Info
import com.lianglliu.hermoodbarometer.core.designsystem.icon.outlined.Language
import com.lianglliu.hermoodbarometer.core.locales.R


/**
 * 外观设置模块
 * 符合 Material Design 3 设计规范
 */
@Composable
fun AppearanceSection(
    selectedTheme: String,
    selectedLanguage: String,
    onThemeChanged: (String) -> Unit,
    onLanguageClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = stringResource(R.string.appearance),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // 主题设置 todo: 优化主题设置图标
            SettingsItem(
                icon = if (selectedTheme == "dark") AppIcons.Outlined.Info else AppIcons.Outlined.Info,
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

            // 语言设置 todo: 优化语言设置图标
            SettingsItem(
                icon = AppIcons.Outlined.Language,
                title = stringResource(R.string.language),
                subtitle = getLanguageDisplayName(selectedLanguage),
                trailing = {
                    IconButton(onClick = onLanguageClick) {
                        Icon(
                            imageVector = AppIcons.Outlined.ChevronRight,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                onClick = onLanguageClick
            )
        }
    }
}

/**
 * 设置项组件
 * 符合 Material Design 3 设计规范
 */
@Composable
fun SettingsItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    trailing: @Composable (() -> Unit)? = null,
    onClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        onClick = onClick ?: {}
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            trailing?.invoke()
        }
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