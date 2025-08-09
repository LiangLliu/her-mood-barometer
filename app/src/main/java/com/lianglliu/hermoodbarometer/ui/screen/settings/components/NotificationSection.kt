package com.lianglliu.hermoodbarometer.ui.screen.settings.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
    onReminderTimeClick: () -> Unit,
    onQuickTimeSelected: (String) -> Unit
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

            // 快捷时间选择
            val presets = listOf("08:00", "12:00", "20:00", "22:00")
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 56.dp, top = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                presets.forEach { t ->
                    FilterChip(
                        selected = t == reminderTime,
                        onClick = { onQuickTimeSelected(t) },
                        label = { androidx.compose.material3.Text(t) }
                    )
                }
                FilterChip(
                    selected = false,
                    onClick = onReminderTimeClick,
                    label = { androidx.compose.material3.Text(stringResource(R.string.select_time)) }
                )
            }
        }
    }
} 