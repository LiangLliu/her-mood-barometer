package com.lianglliu.hermoodbarometer.ui.screen.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lianglliu.hermoodbarometer.MainActivity
import com.lianglliu.hermoodbarometer.R
import com.lianglliu.hermoodbarometer.ui.components.PageTitle
import com.lianglliu.hermoodbarometer.ui.screen.settings.components.AboutSection
import com.lianglliu.hermoodbarometer.ui.screen.settings.components.AppearanceSection
import com.lianglliu.hermoodbarometer.ui.screen.settings.components.CustomEmotionSection
import com.lianglliu.hermoodbarometer.ui.screen.settings.components.LanguageSelectionDialog
import com.lianglliu.hermoodbarometer.ui.screen.settings.components.NotificationSection

/**
 * 设置页面
 * 应用的设置和配置页面
 */
@Composable
fun SettingsScreen(
    onNavigateToCustomEmotion: () -> Unit = {},
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showLanguageDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current

    // 处理错误消息
    LaunchedEffect(uiState.errorMessage) {
        if (uiState.errorMessage != null) {
            // 可以在这里显示Snackbar或其他错误提示
            viewModel.clearErrorMessage()
        }
    }

    // 处理Activity重新创建
    LaunchedEffect(uiState.shouldRecreateActivity) {
        if (uiState.shouldRecreateActivity) {
            // 重新创建Activity以应用新的语言设置
            if (context is MainActivity) {
                context.recreateWithLanguage(uiState.selectedLanguage)
            }
            viewModel.clearRecreateActivityFlag()
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            PageTitle(title = stringResource(R.string.settings))
        }

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
                onReminderEnabledChanged = { enabled -> viewModel.updateReminderSettings(enabled) },
                onReminderTimeClick = {
                    // TODO: 打开时间选择器
                }
            )
        }

        item {
            // 自定义情绪
            CustomEmotionSection(
                onCustomEmotionClick = onNavigateToCustomEmotion
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
}