package com.lianglliu.hermoodbarometer.ui.screen.settings.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.lianglliu.hermoodbarometer.R

/**
 * 通知设置模块
 */
@Composable
fun NotificationSection(
    isReminderEnabled: Boolean,
    reminderTime: String,
    onReminderEnabledChanged: (Boolean) -> Unit,
    onReminderTimeClick: () -> Unit
) {
    SettingsSection(title = stringResource(R.string.notifications)) {
        SettingsItem(
            icon = Icons.Default.Notifications,
            title = stringResource(R.string.daily_reminder),
            subtitle = stringResource(R.string.daily_reminder_description),
            trailing = {
                Switch(
                    checked = isReminderEnabled,
                    onCheckedChange = onReminderEnabledChanged
                )
            }
        )
        
        if (isReminderEnabled) {
            SettingsItem(
                icon = Icons.Default.Info,
                title = stringResource(R.string.reminder_time),
                subtitle = reminderTime,
                trailing = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = null
                    )
                },
                onClick = onReminderTimeClick
            )

            // 仅保留自定义时间入口
        }
    }
} 