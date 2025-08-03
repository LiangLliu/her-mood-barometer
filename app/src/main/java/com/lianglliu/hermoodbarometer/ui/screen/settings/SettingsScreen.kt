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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

/**
 * 设置页面
 * 应用的设置和配置页面
 */
@Composable
fun SettingsScreen() {
    var isDarkTheme by remember { mutableStateOf(false) }
    var isReminderEnabled by remember { mutableStateOf(false) }
    var selectedLanguage by remember { mutableStateOf("简体中文") }
    
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            // 页面标题
            Text(
                text = "设置",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
        
        item {
            // 外观设置
            SettingsSection(title = "外观") {
                // 主题设置
                SettingsItem(
                    icon = Icons.Default.Star,
                    title = "深色模式",
                    subtitle = "使用深色主题",
                    trailing = {
                        Switch(
                            checked = isDarkTheme,
                            onCheckedChange = { isDarkTheme = it }
                        )
                    }
                )
                
                // 语言设置
                SettingsItem(
                    icon = Icons.Default.Info,
                    title = "语言",
                    subtitle = selectedLanguage,
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
            SettingsSection(title = "通知") {
                SettingsItem(
                    icon = Icons.Default.Notifications,
                    title = "每日提醒",
                    subtitle = "每天提醒记录心情",
                    trailing = {
                        Switch(
                            checked = isReminderEnabled,
                            onCheckedChange = { isReminderEnabled = it }
                        )
                    }
                )
                
                if (isReminderEnabled) {
                    SettingsItem(
                        icon = Icons.Default.Info,
                        title = "提醒时间",
                        subtitle = "20:00",
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
            SettingsSection(title = "自定义") {
                SettingsItem(
                    icon = Icons.Default.Add,
                    title = "自定义情绪",
                    subtitle = "添加和管理自定义情绪类型",
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
            SettingsSection(title = "数据") {
                SettingsItem(
                    icon = Icons.Default.Info,
                    title = "导出数据",
                    subtitle = "将数据导出到文件",
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
                    title = "导入数据",
                    subtitle = "从文件导入数据",
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
            SettingsSection(title = "关于") {
                SettingsItem(
                    icon = Icons.Default.Info,
                    title = "版本",
                    subtitle = "1.0.0",
                    trailing = null
                )
                
                SettingsItem(
                    icon = Icons.Default.Favorite,
                    title = "关于应用",
                    subtitle = "她的晴雨表 - 记录生活中的每一份心情",
                    trailing = null
                )
            }
        }
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
        headlineContent = { Text(title) },
        supportingContent = { Text(subtitle) },
        leadingContent = {
            Icon(
                imageVector = icon,
                contentDescription = null
            )
        },
        trailingContent = trailing,
        modifier = clickableModifier
    )
}