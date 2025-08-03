package com.lianglliu.hermoodbarometer.ui.screen.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lianglliu.hermoodbarometer.R

/**
 * 设置页面
 * 应用的设置和配置页面
 */
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    
    // 处理错误消息
    LaunchedEffect(uiState.errorMessage) {
        if (uiState.errorMessage != null) {
            // 可以在这里显示Snackbar或其他错误提示
            viewModel.clearErrorMessage()
        }
    }
    
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            // 页面标题
            Text(
                text = stringResource(R.string.settings),
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
        
        item {
            // 外观设置
            SettingsSection(title = stringResource(R.string.appearance)) {
                // 主题设置
                SettingsItem(
                    icon = Icons.Default.Star,
                    title = stringResource(R.string.dark_mode),
                    subtitle = stringResource(R.string.dark_mode_description),
                    trailing = {
                        Switch(
                            checked = uiState.selectedTheme == "dark",
                            onCheckedChange = { isDark ->
                                val theme = if (isDark) "dark" else "light"
                                viewModel.updateTheme(theme)
                            }
                        )
                    }
                )
                
                // 语言设置
                SettingsItem(
                    icon = Icons.Default.Info,
                    title = stringResource(R.string.language),
                    subtitle = getLanguageDisplayName(uiState.selectedLanguage),
                    trailing = {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = null
                        )
                    },
                    onClick = {
                        // TODO: 打开语言选择对话框
                    }
                )
            }
        }
        
        item {
            // 通知设置
            SettingsSection(title = stringResource(R.string.notifications)) {
                SettingsItem(
                    icon = Icons.Default.Notifications,
                    title = stringResource(R.string.daily_reminder),
                    subtitle = stringResource(R.string.daily_reminder_description),
                    trailing = {
                        Switch(
                            checked = uiState.isReminderEnabled,
                            onCheckedChange = { enabled ->
                                viewModel.updateReminderSettings(enabled)
                            }
                        )
                    }
                )
                
                if (uiState.isReminderEnabled) {
                    SettingsItem(
                        icon = Icons.Default.Info,
                        title = stringResource(R.string.reminder_time),
                        subtitle = uiState.reminderTime,
                        trailing = {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                contentDescription = null
                            )
                        },
                        onClick = {
                            // TODO: 打开时间选择器
                        }
                    )
                }
            }
        }
        
        item {
            // 自定义情绪
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
                    onClick = {
                        // TODO: 导航到自定义情绪页面
                    }
                )
            }
        }
        
        item {
            // 数据管理
            SettingsSection(title = stringResource(R.string.data)) {
                SettingsItem(
                    icon = Icons.Default.Info,
                    title = stringResource(R.string.export_data),
                    subtitle = stringResource(R.string.export_data_description),
                    trailing = {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = null
                        )
                    },
                    onClick = {
                        // TODO: 导出数据
                    }
                )
                
                SettingsItem(
                    icon = Icons.Default.Info,
                    title = stringResource(R.string.import_data),
                    subtitle = stringResource(R.string.import_data_description),
                    trailing = {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = null
                        )
                    },
                    onClick = {
                        // TODO: 导入数据
                    }
                )
            }
        }
        
        item {
            // 关于
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
    }
}

/**
 * 获取语言显示名称
 */
@Composable
private fun getLanguageDisplayName(languageCode: String): String {
    return when (languageCode) {
        "zh" -> stringResource(R.string.language_zh)
        "zh-rTW" -> stringResource(R.string.language_zh_tw)
        "ja" -> stringResource(R.string.language_ja)
        "ko" -> stringResource(R.string.language_ko)
        "en" -> stringResource(R.string.language_en)
        else -> stringResource(R.string.language_zh)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsSection(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            content()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    trailing: @Composable (() -> Unit)? = null,
    onClick: (() -> Unit)? = null
) {
    val clickableModifier = if (onClick != null) {
        Modifier.clickable { onClick() }
    } else {
        Modifier
    }
    
    ListItem(
        headlineContent = {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge
            )
        },
        supportingContent = {
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        leadingContent = {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        },
        trailingContent = trailing,
        modifier = clickableModifier
    )
}