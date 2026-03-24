package com.lianglliu.hermoodbarometer.feature.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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
import com.lianglliu.hermoodbarometer.core.locales.R
import com.lianglliu.hermoodbarometer.core.model.data.ColorSchemeConfig
import com.lianglliu.hermoodbarometer.core.model.data.DarkThemeConfig
import com.lianglliu.hermoodbarometer.core.permissions.PermissionCheckResult
import com.lianglliu.hermoodbarometer.core.permissions.PermissionHandler
import com.lianglliu.hermoodbarometer.core.permissions.PermissionHelpers
import com.lianglliu.hermoodbarometer.core.ui.component.LoadingState
import com.lianglliu.hermoodbarometer.feature.settings.SettingsUiState.Loading
import com.lianglliu.hermoodbarometer.feature.settings.components.ColorSchemeBottomSheet
import com.lianglliu.hermoodbarometer.feature.settings.components.LanguageDialog
import com.lianglliu.hermoodbarometer.feature.settings.components.SectionTitle
import com.lianglliu.hermoodbarometer.feature.settings.components.ThemeDialog
import com.lianglliu.hermoodbarometer.feature.settings.components.TimePickerDialog
import timber.log.Timber

/** 设置页面 应用的设置和配置页面 */
@Composable
fun SettingsScreen(
    onNavigateToAboutLicenses: () -> Unit = {},
    viewModel: SettingsViewModel = hiltViewModel(),
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
                },
            ) {
                // 正常显示设置界面
                SettingsScreenContent(
                    settingsState = settingsState,
                    onLicensesClick = onNavigateToAboutLicenses,
                    onColorSchemeConfigUpdate = viewModel::updateColorSchemeConfig,
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
                onColorSchemeConfigUpdate = viewModel::updateColorSchemeConfig,
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
    onColorSchemeConfigUpdate: (ColorSchemeConfig) -> Unit,
    onDarkThemeConfigUpdate: (DarkThemeConfig) -> Unit,
    onLanguageUpdate: (String) -> Unit,
    onReminderEnabledChanged: (Boolean) -> Unit,
    onReminderTimeChanged: (String) -> Unit,
) {
    val context = LocalContext.current

    // Compute notification blocked state
    val isNotificationBlocked =
        if (settingsState is SettingsUiState.Success && settingsState.settings.isReminderEnabled) {
            !PermissionHelpers.notificationsEnabled(context)
        } else {
            false
        }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = { Text(text = stringResource(R.string.settings)) })
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(paddingValues),
            contentPadding = PaddingValues(vertical = 8.dp),
        ) {
            when (settingsState) {
                Loading -> {
                    item { LoadingState(modifier = Modifier.fillMaxSize()) }
                }

                is SettingsUiState.Success -> {
                    appearance(
                        settings = settingsState.settings,
                        onColorSchemeConfigUpdate = onColorSchemeConfigUpdate,
                        onDarkThemeConfigUpdate = onDarkThemeConfigUpdate,
                        onLanguageUpdate = onLanguageUpdate,
                    )

                    notification(
                        settings = settingsState.settings,
                        isNotificationBlocked = isNotificationBlocked,
                        onReminderEnabledChanged = onReminderEnabledChanged,
                        onTimeSelected = { time ->
                            Timber.d("Set notification time to $time")
                            onReminderTimeChanged(time)
                        },
                    )

                    about(onAboutLicensesClick = onLicensesClick)
                }
            }
        }
    }
}

private fun LazyListScope.appearance(
    settings: UserEditableSettings,
    onColorSchemeConfigUpdate: (ColorSchemeConfig) -> Unit,
    onDarkThemeConfigUpdate: (DarkThemeConfig) -> Unit,
    onLanguageUpdate: (String) -> Unit,
) {
    item { SectionTitle(stringResource(R.string.appearance)) }
    item {
        var showLanguageDialog by rememberSaveable { mutableStateOf(false) }

        ListItem(
            headlineContent = { Text(stringResource(R.string.language)) },
            leadingContent = {
                Icon(imageVector = AppIcons.Outlined.Language, contentDescription = null)
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
        val themeOptions =
            listOf(
                stringResource(R.string.system_theme),
                stringResource(R.string.light_theme),
                stringResource(R.string.dark_theme),
            )
        var showThemeDialog by rememberSaveable { mutableStateOf(false) }

        ListItem(
            headlineContent = { Text(stringResource(R.string.theme)) },
            leadingContent = {
                Icon(imageVector = AppIcons.Outlined.Palette, contentDescription = null)
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
        var showColorSchemeSheet by rememberSaveable { mutableStateOf(false) }

        ListItem(
            headlineContent = { Text(stringResource(R.string.color_scheme_title)) },
            leadingContent = {
                Icon(imageVector = AppIcons.Outlined.FormatPaint, contentDescription = null)
            },
            supportingContent = {
                Text(
                    when (settings.colorSchemeConfig) {
                        ColorSchemeConfig.WARM -> stringResource(R.string.color_scheme_warm)
                        ColorSchemeConfig.OCEAN -> stringResource(R.string.color_scheme_ocean)
                        ColorSchemeConfig.PETAL -> stringResource(R.string.color_scheme_petal)
                        ColorSchemeConfig.DYNAMIC -> stringResource(R.string.color_scheme_dynamic)
                    }
                )
            },
            onClick = { showColorSchemeSheet = true },
        )

        if (showColorSchemeSheet) {
            ColorSchemeBottomSheet(
                currentConfig = settings.colorSchemeConfig,
                onConfigSelected = onColorSchemeConfigUpdate,
                onDismiss = { showColorSchemeSheet = false },
            )
        }
    }
}

private fun LazyListScope.notification(
    settings: UserEditableSettings,
    isNotificationBlocked: Boolean,
    onReminderEnabledChanged: (Boolean) -> Unit,
    onTimeSelected: (String) -> Unit,
) {
    item { SectionTitle(stringResource(R.string.notifications)) }

    item {
        ToggableListItem(
            headlineContent = { Text(stringResource(R.string.daily_reminder)) },
            supportingContent = { Text(stringResource(R.string.daily_reminder_description)) },
            leadingContent = {
                Icon(imageVector = AppIcons.Outlined.Notifications, contentDescription = null)
            },
            trailingContent = {
                AppSwitch(checked = settings.isReminderEnabled, onCheckedChange = null)
            },
            checked = settings.isReminderEnabled,
            onCheckedChange = onReminderEnabledChanged,
        )
    }

    // Notification permission blocked warning
    if (settings.isReminderEnabled && isNotificationBlocked) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp),
                colors =
                    CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    ),
                shape = RoundedCornerShape(12.dp),
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error,
                    )
                    Text(
                        text = stringResource(R.string.notification_permission_blocked_warning),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onErrorContainer,
                    )
                }
            }
        }
    }

    item {
        if (settings.isReminderEnabled) {
            var showTimePickerDialog by rememberSaveable { mutableStateOf(false) }
            ListItem(
                headlineContent = { Text(stringResource(R.string.reminder_time)) },
                leadingContent = {
                    Icon(imageVector = AppIcons.Outlined.MoreVert, contentDescription = null)
                },
                supportingContent = { Text(settings.reminderTime) },
                onClick = { showTimePickerDialog = !showTimePickerDialog },
            )

            //  提醒时间选择对话框
            if (showTimePickerDialog) {
                TimePickerDialog(
                    currentTime = settings.reminderTime,
                    onTimeSelected = onTimeSelected,
                    onDismiss = { showTimePickerDialog = false },
                )
            }
        }
    }
}

private fun LazyListScope.about(onAboutLicensesClick: () -> Unit) {
    item { SectionTitle(stringResource(R.string.about)) }
    item {
        ListItem(
            headlineContent = { Text(stringResource(R.string.about_app)) },
            leadingContent = {
                Icon(imageVector = AppIcons.Outlined.Info, contentDescription = null)
            },
            supportingContent = { Text(stringResource(R.string.about_app_description)) },
        )
    }

    item {
        ListItem(
            headlineContent = { Text(stringResource(R.string.licenses)) },
            leadingContent = {
                Icon(imageVector = AppIcons.Outlined.HistoryEdu, contentDescription = null)
            },
            supportingContent = { stringResource(R.string.licenses_description) },
            onClick = { onAboutLicensesClick },
        )
    }
    item {
        ListItem(
            headlineContent = { Text(stringResource(R.string.app_name)) },
            leadingContent = {
                Icon(imageVector = AppIcons.Outlined.Info, contentDescription = null)
            },
            supportingContent = { Text("${stringResource(R.string.version)}: 1.0.0") },
        )
    }
}
