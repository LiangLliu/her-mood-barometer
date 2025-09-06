package com.lianglliu.hermoodbarometer.ui.screen.settings.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.lianglliu.hermoodbarometer.R

/**
 * 通知设置模块
 * 符合 Material Design 3 设计规范
 */
@Composable
fun NotificationSection(
    isReminderEnabled: Boolean,
    reminderTime: String,
    onReminderEnabledChanged: (Boolean) -> Unit,
    onReminderTimeClick: () -> Unit,
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
                text = stringResource(R.string.notifications),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            // 每日提醒设置
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
            
            // 提醒时间设置
            if (isReminderEnabled) {
                SettingsItem(
                    icon = Icons.Default.MoreVert,
                    title = stringResource(R.string.reminder_time),
                    subtitle = reminderTime,
                    trailing = {
                        IconButton(onClick = onReminderTimeClick) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    },
                    onClick = onReminderTimeClick
                )
            }
        }
    }
}