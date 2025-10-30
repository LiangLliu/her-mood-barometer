package com.lianglliu.hermoodbarometer.feature.settings

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lianglliu.hermoodbarometer.core.designsystem.component.AppSwitch
import com.lianglliu.hermoodbarometer.core.designsystem.component.ListItem
import com.lianglliu.hermoodbarometer.core.designsystem.component.ToggableListItem
import com.lianglliu.hermoodbarometer.core.designsystem.icon.AppIcons
import com.lianglliu.hermoodbarometer.core.designsystem.icon.outlined.FormatPaint
import com.lianglliu.hermoodbarometer.core.designsystem.icon.outlined.HistoryEdu
import com.lianglliu.hermoodbarometer.core.designsystem.icon.outlined.Info
import com.lianglliu.hermoodbarometer.core.designsystem.icon.outlined.Language
import com.lianglliu.hermoodbarometer.core.designsystem.icon.outlined.MoreVert
import com.lianglliu.hermoodbarometer.core.designsystem.icon.outlined.Notifications
import com.lianglliu.hermoodbarometer.core.designsystem.icon.outlined.Palette
import com.lianglliu.hermoodbarometer.core.designsystem.theme.supportsDynamicTheming
import com.lianglliu.hermoodbarometer.core.locales.R
import com.lianglliu.hermoodbarometer.core.model.data.DarkThemeConfig
import com.lianglliu.hermoodbarometer.core.permissions.PermissionCheckResult
import com.lianglliu.hermoodbarometer.core.ui.component.LoadingState
import com.lianglliu.hermoodbarometer.core.ui.component.ScreenContainer
import com.lianglliu.hermoodbarometer.feature.settings.SettingsUiState.Loading
import com.lianglliu.hermoodbarometer.feature.settings.components.LanguageDialog
import com.lianglliu.hermoodbarometer.feature.settings.components.SectionTitle
import com.lianglliu.hermoodbarometer.feature.settings.components.ThemeDialog
import com.lianglliu.hermoodbarometer.feature.settings.components.TimePickerDialog
import com.lianglliu.hermoodbarometer.core.permissions.PermissionHandler

/**
 * 设置页面
 * 应用的设置和配置页面
 */
@Composable
fun SettingsScreen(
    onNavigateToAboutLicenses: () -> Unit = {}, viewModel: SettingsViewModel = hiltViewModel()
) {
    val settingsState by viewModel.settingsUiState.collectAsStateWithLifecycle()
    val permissionState by viewModel.permissionState.collectAsStateWithLifecycle()

    // 处理权限请求结果
    when (val state = permissionState) {
        is PermissionCheckResult.PermissionsRequired -> {
            PermissionHandler(
                permissions = state.permissions,
                onAllPermissionsGranted = {
                    // 所有权限已授予，继续开启提醒
                    viewModel.recheckPermissionsAndContinue()
                },
                onPermissionsDenied = {
                    // 用户拒绝了权限，清除状态
                    viewModel.clearPermissionState()
                }
            ) {
                // 正常显示设置界面
                SettingsScreenContent(
                    settingsState = settingsState,
                    onLicensesClick = onNavigateToAboutLicenses,
                    onDynamicColorPreferenceUpdate = viewModel::updateDynamicColorPreference,
                    onDarkThemeConfigUpdate = viewModel::updateDarkThemeConfig,
                    onLanguageUpdate = viewModel::updateLanguage,
                    onReminderEnabledChanged = viewModel::updateReminderEnabled,
                    onReminderTimeChanged = viewModel::updateReminderTime,
                )
            }
        }

        else -> {
            // 正常显示设置界面
            SettingsScreenContent(
                settingsState = settingsState,
                onLicensesClick = onNavigateToAboutLicenses,
                onDynamicColorPreferenceUpdate = viewModel::updateDynamicColorPreference,
                onDarkThemeConfigUpdate = viewModel::updateDarkThemeConfig,
                onLanguageUpdate = viewModel::updateLanguage,
                onReminderEnabledChanged = viewModel::updateReminderEnabled,
                onReminderTimeChanged = viewModel::updateReminderTime,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsScreenContent(
    settingsState: SettingsUiState,
    onLicensesClick: () -> Unit,
    onDynamicColorPreferenceUpdate: (Boolean) -> Unit,
    onDarkThemeConfigUpdate: (DarkThemeConfig) -> Unit,
    onLanguageUpdate: (String) -> Unit,
    onReminderEnabledChanged: (Boolean) -> Unit,
    onReminderTimeChanged: (String) -> Unit,
) {
    ScreenContainer(
        title = stringResource(R.string.settings), contentPadding = PaddingValues(16.dp)
    ) {

        when (settingsState) {
            Loading -> {
                item {
                    LoadingState(
                        modifier = Modifier.fillMaxSize(),
                    )
                }
            }

            is SettingsUiState.Success -> {
                appearance(
                    settings = settingsState.settings,
                    onDynamicColorPreferenceUpdate = onDynamicColorPreferenceUpdate,
                    onDarkThemeConfigUpdate = onDarkThemeConfigUpdate,
                    onLanguageUpdate = onLanguageUpdate,
                )

                notification(
                    settings = settingsState.settings,
                    onReminderEnabledChanged = onReminderEnabledChanged,
                    onTimeSelected = { time ->
                        Log.d("Setting", "Set notification time to $time")
                        onReminderTimeChanged(time)
                    },
                )

                about(
                    onAboutLicensesClick = onLicensesClick
                )
            }
        }


//        item {
//            // 通知设置
//            NotificationSection(
//                isReminderEnabled = uiState.isReminderEnabled,
//                reminderTime = uiState.reminderTime,
//                onReminderEnabledChanged = { enabled ->
//                    // 最佳实践：提示用户开启必要权限/设置

//                    viewModel.updateReminderSettings(enabled)
//                },
//                onReminderTimeClick = { showTimePicker = true }
//            )
//        }

//        item {
//            // 情绪管理
//            CustomEmotionSection(
//                onCustomEmotionClick = onNavigateToEmotionManagement
//            )
//        }


    }
}


private fun LazyListScope.appearance(
    settings: UserEditableSettings,
    supportDynamicColor: Boolean = supportsDynamicTheming(),
    onDynamicColorPreferenceUpdate: (Boolean) -> Unit,
    onDarkThemeConfigUpdate: (DarkThemeConfig) -> Unit,
    onLanguageUpdate: (String) -> Unit,
) {
    item { SectionTitle(stringResource(R.string.appearance)) }
    item {
        var showLanguageDialog by rememberSaveable { mutableStateOf(false) }

        ListItem(
            headlineContent = { Text(stringResource(R.string.language)) },
            leadingContent = {
                Icon(
                    imageVector = AppIcons.Outlined.Language,
                    contentDescription = null,
                )
            },
            supportingContent = { Text(settings.language.displayName) },
            onClick = { showLanguageDialog = true },
        )

        if (showLanguageDialog) {
            LanguageDialog(
                language = settings.language.code,
                availableLanguages = settings.availableLanguages,
                onLanguageClick = onLanguageUpdate,
                onDismiss = { showLanguageDialog = false },
            )
        }
    }
    item {
        val themeOptions = listOf(
            stringResource(R.string.system_theme),
            stringResource(R.string.light_theme),
            stringResource(R.string.dark_theme),
        )
        var showThemeDialog by rememberSaveable { mutableStateOf(false) }

        ListItem(
            headlineContent = { Text(stringResource(R.string.theme)) },
            leadingContent = {
                Icon(
                    imageVector = AppIcons.Outlined.Palette,
                    contentDescription = null,
                )
            },
            supportingContent = { Text(themeOptions.elementAt(settings.darkThemeConfig.ordinal)) },
            onClick = { showThemeDialog = true },
        )

        if (showThemeDialog) {
            ThemeDialog(
                themeConfig = settings.darkThemeConfig,
                themeOptions = themeOptions,
                onDarkThemeConfigUpdate = onDarkThemeConfigUpdate,
                onDismiss = { showThemeDialog = false },
            )
        }
    }
    item {
        AnimatedVisibility(supportDynamicColor) {
            ToggableListItem(
                headlineContent = { Text(stringResource(R.string.dynamic_color)) },
                leadingContent = {
                    Icon(
                        imageVector = AppIcons.Outlined.FormatPaint,
                        contentDescription = null,
                    )
                },
                trailingContent = {
                    AppSwitch(
                        checked = settings.useDynamicColor,
                        onCheckedChange = null,
                    )
                },
                checked = settings.useDynamicColor,
                onCheckedChange = onDynamicColorPreferenceUpdate,
            )
        }
    }
}

private fun LazyListScope.notification(
    settings: UserEditableSettings,
    onReminderEnabledChanged: (Boolean) -> Unit,
    onTimeSelected: (String) -> Unit,
) {
    item { SectionTitle(stringResource(R.string.notifications)) }

    item {
        ToggableListItem(
            headlineContent = { Text(stringResource(R.string.daily_reminder)) },
            supportingContent = { Text(stringResource(R.string.daily_reminder_description)) },
            leadingContent = {
                Icon(
                    imageVector = AppIcons.Outlined.Notifications,
                    contentDescription = null,
                )
            },
            trailingContent = {
                AppSwitch(
                    checked = settings.isReminderEnabled,
                    onCheckedChange = null,
                )
            },
            checked = settings.isReminderEnabled,
            onCheckedChange = onReminderEnabledChanged,
        )
    }

    item {
        if (settings.isReminderEnabled) {
            var showTimePickerDialog by rememberSaveable { mutableStateOf(false) }
            ListItem(
                headlineContent = { Text(stringResource(R.string.reminder_time)) },
                leadingContent = {
                    Icon(
                        imageVector = AppIcons.Outlined.MoreVert,
                        contentDescription = null,
                    )
                },
                supportingContent = { Text(settings.reminderTime) },
                onClick = { showTimePickerDialog = !showTimePickerDialog },
            )


            //  提醒时间选择对话框
            if (showTimePickerDialog) {
                TimePickerDialog(
                    currentTime = settings.reminderTime,
                    onTimeSelected = onTimeSelected,
                    onDismiss = { showTimePickerDialog = false })
            }
        }
    }
}

private fun LazyListScope.about(
    onAboutLicensesClick: () -> Unit,
) {
    item { SectionTitle(stringResource(R.string.about)) }
    item {
        ListItem(
            headlineContent = { Text(stringResource(R.string.about_app)) },
            leadingContent = {
                Icon(
                    imageVector = AppIcons.Outlined.Info,
                    contentDescription = null,
                )
            },
            supportingContent = { Text(stringResource(R.string.about_app_description)) },
        )
    }


    item {
        ListItem(
            headlineContent = { Text(stringResource(R.string.licenses)) },
            leadingContent = {
                Icon(
                    imageVector = AppIcons.Outlined.HistoryEdu,
                    contentDescription = null,
                )
            },
            supportingContent = { stringResource(R.string.licenses_description) },
            onClick = { onAboutLicensesClick },
        )
    }
    item {
        ListItem(
            headlineContent = { Text(stringResource(R.string.app_name)) },
            leadingContent = {
                Icon(
                    imageVector = AppIcons.Outlined.Info,
                    contentDescription = null,
                )
            },
            supportingContent = { Text("${stringResource(R.string.version)}: 1.0.0") },
        )
    }
}


