package com.lianglliu.hermoodbarometer.ui.screen.settings

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lianglliu.hermoodbarometer.R
import com.lianglliu.hermoodbarometer.ui.components.ScreenContainer
import com.lianglliu.hermoodbarometer.ui.permissions.PermissionHelpers
import com.lianglliu.hermoodbarometer.ui.screen.settings.components.AboutSection
import com.lianglliu.hermoodbarometer.ui.screen.settings.components.AppearanceSection
import com.lianglliu.hermoodbarometer.ui.screen.settings.components.CustomEmotionSection
import com.lianglliu.hermoodbarometer.ui.screen.settings.components.LanguageSelectionDialog
import com.lianglliu.hermoodbarometer.ui.screen.settings.components.NotificationSection
import com.lianglliu.hermoodbarometer.ui.screen.settings.components.TimePickerDialog

/**
 * 设置页面
 * 应用的设置和配置页面
 */
@Composable
fun SettingsScreen(
    onNavigateToEmotionManagement: () -> Unit = {},
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showLanguageDialog by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    val context = LocalContext.current

    // 处理错误消息
    LaunchedEffect(uiState.errorMessage) {
        if (uiState.errorMessage != null) {
            // 可以在这里显示Snackbar或其他错误提示
            viewModel.clearErrorMessage()
        }
    }

    // per-app locales 将自动应用，无需手动重建 Activity

    ScreenContainer(
        title = stringResource(R.string.settings),
        contentPadding = PaddingValues(16.dp)
    ) {
        item {
            // 外观设置
            AppearanceSection(
                selectedTheme = uiState.selectedTheme,
                selectedLanguage = uiState.selectedLanguage,
                onThemeChanged = { theme -> viewModel.updateTheme(theme) },
                onLanguageClick = { showLanguageDialog = true }
            )
        }

        item {
            // 通知设置
            NotificationSection(
                isReminderEnabled = uiState.isReminderEnabled,
                reminderTime = uiState.reminderTime,
                onReminderEnabledChanged = { enabled ->
                    // 最佳实践：提示用户开启必要权限/设置
                    if (enabled) {
                        // 通知权限
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU &&
                            !PermissionHelpers.notificationsEnabled(context)
                        ) {
                            PermissionHelpers.openAppNotificationSettings(context)
                        }
                        // 精准闹钟（S+）
                        if (!PermissionHelpers.canScheduleExactAlarms(context)) {
                            PermissionHelpers.openExactAlarmSettings(context)
                        }
                    }
                    viewModel.updateReminderSettings(enabled)
                },
                onReminderTimeClick = { showTimePicker = true }
            )
        }

        item {
            // 情绪管理
            CustomEmotionSection(
                onCustomEmotionClick = onNavigateToEmotionManagement
            )
        }

        item {
            // 关于
            AboutSection()
        }
    }

    // 语言选择对话框
    if (showLanguageDialog) {
        LanguageSelectionDialog(
            currentLanguage = uiState.selectedLanguage,
            onLanguageSelected = { language ->
                viewModel.updateLanguage(language)
                showLanguageDialog = false
            },
            onDismiss = {
                showLanguageDialog = false
            }
        )
    }

    // 提醒时间选择对话框
    if (showTimePicker) {
        TimePickerDialog(
            currentTime = uiState.reminderTime,
            onTimeSelected = { selected ->
                // 选中时间即保存并调度
                viewModel.updateReminderSettings(isEnabled = true, time = selected)
            },
            onDismiss = { showTimePicker = false }
        )
    }
}