package com.lianglliu.hermoodbarometer.feature.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lianglliu.hermoodbarometer.core.designsystem.component.ScreenHeader
import com.lianglliu.hermoodbarometer.core.designsystem.icon.Emojis
import com.lianglliu.hermoodbarometer.core.designsystem.theme.ExtendedTheme
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
import com.lianglliu.hermoodbarometer.feature.settings.components.ThemeDialog
import com.lianglliu.hermoodbarometer.feature.settings.components.TimePickerDialog
import com.lianglliu.hermoodbarometer.feature.settings.components.displayName
import timber.log.Timber

@Composable
fun SettingsScreen(
    onNavigateToAboutLicenses: () -> Unit = {},
    viewModel: SettingsViewModel = hiltViewModel(),
) {
    val settingsState by viewModel.settingsUiState.collectAsStateWithLifecycle()
    val permissionState by viewModel.permissionState.collectAsStateWithLifecycle()

    when (val state = permissionState) {
        is PermissionCheckResult.PermissionsRequired -> {
            PermissionHandler(
                permissions = state.permissions,
                onAllPermissionsGranted = { viewModel.recheckPermissionsAndContinue() },
                onPermissionsDenied = { viewModel.clearPermissionState() },
            ) {
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
    val colors = ExtendedTheme.colors

    val isNotificationBlocked =
        if (settingsState is SettingsUiState.Success && settingsState.settings.isReminderEnabled) {
            !PermissionHelpers.notificationsEnabled(context)
        } else {
            false
        }

    Scaffold(
        topBar = {
            ScreenHeader(
                title = stringResource(R.string.settings),
                subtitle = stringResource(R.string.settings_subtitle),
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(paddingValues),
            contentPadding =
                PaddingValues(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 100.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            when (settingsState) {
                Loading -> {
                    item { LoadingState(modifier = Modifier.fillMaxSize()) }
                }

                is SettingsUiState.Success -> {
                    // Appearance group
                    item { SettingsGroupTitle(stringResource(R.string.appearance)) }
                    item {
                        AppearanceGroup(
                            settings = settingsState.settings,
                            onColorSchemeConfigUpdate = onColorSchemeConfigUpdate,
                            onDarkThemeConfigUpdate = onDarkThemeConfigUpdate,
                            onLanguageUpdate = onLanguageUpdate,
                        )
                    }

                    // Notification group
                    item { SettingsGroupTitle(stringResource(R.string.notifications)) }
                    item {
                        NotificationGroup(
                            settings = settingsState.settings,
                            isNotificationBlocked = isNotificationBlocked,
                            onReminderEnabledChanged = onReminderEnabledChanged,
                            onReminderTimeChanged = { time ->
                                Timber.d("Set notification time to $time")
                                onReminderTimeChanged(time)
                            },
                        )
                    }

                    // About group
                    item { SettingsGroupTitle(stringResource(R.string.about)) }
                    item { AboutGroup(onLicensesClick = onLicensesClick) }
                }
            }
        }
    }
}

@Composable
private fun SettingsGroupTitle(text: String, modifier: Modifier = Modifier) {
    val colors = ExtendedTheme.colors
    Text(
        text = text,
        style =
            MaterialTheme.typography.labelSmall.copy(
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
            ),
        color = colors.textHint,
        letterSpacing = 1.sp,
        modifier = modifier.padding(start = 4.dp, bottom = 8.dp),
    )
}

@Composable
private fun SettingsGroupCard(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    val colors = ExtendedTheme.colors
    Box(
        modifier =
            modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .border(1.dp, colors.borderLight, RoundedCornerShape(20.dp))
                .background(colors.cardBackground)
                .padding(vertical = 6.dp, horizontal = 2.dp)
    ) {
        Column { content() }
    }
}

@Composable
private fun SettingItem(
    emoji: String,
    iconBgColor: Color,
    title: String,
    description: String,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    trailing: @Composable (() -> Unit)? = null,
) {
    val colors = ExtendedTheme.colors
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier)
                .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        // Emoji icon in tinted square
        Box(
            modifier = Modifier.size(36.dp).clip(RoundedCornerShape(10.dp)).background(iconBgColor),
            contentAlignment = Alignment.Center,
        ) {
            Text(text = emoji, fontSize = 18.sp)
        }

        // Body
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp),
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Spacer(modifier = Modifier.height(1.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 12.sp),
                color = colors.textMuted,
            )
        }

        // Trail
        if (trailing != null) {
            trailing()
        } else if (onClick != null) {
            Text(
                text = Emojis.NAV_NEXT,
                style = MaterialTheme.typography.titleMedium.copy(fontSize = 16.sp),
                color = colors.textHint,
            )
        }
    }
}

@Composable
private fun AppearanceGroup(
    settings: UserEditableSettings,
    onColorSchemeConfigUpdate: (ColorSchemeConfig) -> Unit,
    onDarkThemeConfigUpdate: (DarkThemeConfig) -> Unit,
    onLanguageUpdate: (String) -> Unit,
) {
    val colors = ExtendedTheme.colors
    var showLanguageDialog by rememberSaveable { mutableStateOf(false) }
    var showThemeDialog by rememberSaveable { mutableStateOf(false) }
    var showColorSchemeSheet by rememberSaveable { mutableStateOf(false) }

    val themeOptions =
        listOf(
            stringResource(R.string.system_theme),
            stringResource(R.string.light_theme),
            stringResource(R.string.dark_theme),
        )

    SettingsGroupCard {
        SettingItem(
            emoji = Emojis.GLOBE,
            iconBgColor = colors.accentBg,
            title = stringResource(R.string.language),
            description = settings.language.displayName(),
            onClick = { showLanguageDialog = true },
        )
        SettingItem(
            emoji = Emojis.PALETTE,
            iconBgColor = colors.lavenderBg,
            title = stringResource(R.string.theme),
            description = themeOptions.elementAt(settings.darkThemeConfig.ordinal),
            onClick = { showThemeDialog = true },
        )
        SettingItem(
            emoji = Emojis.MASKS,
            iconBgColor = colors.roseBg,
            title = stringResource(R.string.color_scheme_title),
            description =
                when (settings.colorSchemeConfig) {
                    ColorSchemeConfig.WARM -> stringResource(R.string.color_scheme_warm)
                    ColorSchemeConfig.OCEAN -> stringResource(R.string.color_scheme_ocean)
                    ColorSchemeConfig.PETAL -> stringResource(R.string.color_scheme_petal)
                    ColorSchemeConfig.DYNAMIC -> stringResource(R.string.color_scheme_dynamic)
                },
            onClick = { showColorSchemeSheet = true },
        )
    }

    if (showLanguageDialog) {
        LanguageDialog(
            language = settings.language.code,
            availableLanguages = settings.availableLanguages,
            onLanguageClick = onLanguageUpdate,
            onDismiss = { showLanguageDialog = false },
        )
    }
    if (showThemeDialog) {
        ThemeDialog(
            themeConfig = settings.darkThemeConfig,
            themeOptions = themeOptions,
            onDarkThemeConfigUpdate = onDarkThemeConfigUpdate,
            onDismiss = { showThemeDialog = false },
        )
    }
    if (showColorSchemeSheet) {
        ColorSchemeBottomSheet(
            currentConfig = settings.colorSchemeConfig,
            onConfigSelected = onColorSchemeConfigUpdate,
            onDismiss = { showColorSchemeSheet = false },
        )
    }
}

@Composable
private fun NotificationGroup(
    settings: UserEditableSettings,
    isNotificationBlocked: Boolean,
    onReminderEnabledChanged: (Boolean) -> Unit,
    onReminderTimeChanged: (String) -> Unit,
) {
    val colors = ExtendedTheme.colors
    var showTimePickerDialog by rememberSaveable { mutableStateOf(false) }

    SettingsGroupCard {
        SettingItem(
            emoji = Emojis.BELL,
            iconBgColor = colors.amberBg,
            title = stringResource(R.string.daily_reminder),
            description = stringResource(R.string.daily_reminder_description),
            trailing = {
                Switch(
                    checked = settings.isReminderEnabled,
                    onCheckedChange = onReminderEnabledChanged,
                    colors =
                        SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = colors.accent,
                            uncheckedThumbColor = Color.White,
                            uncheckedTrackColor = colors.border,
                        ),
                )
            },
        )

        if (settings.isReminderEnabled) {
            SettingItem(
                emoji = Emojis.ALARM,
                iconBgColor = colors.roseBg,
                title = stringResource(R.string.reminder_time),
                description = settings.reminderTime,
                onClick = { showTimePickerDialog = true },
            )
        }
    }

    // Notification permission blocked warning
    if (settings.isReminderEnabled && isNotificationBlocked) {
        Spacer(modifier = Modifier.height(4.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors =
                CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
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

    if (showTimePickerDialog) {
        TimePickerDialog(
            currentTime = settings.reminderTime,
            onTimeSelected = onReminderTimeChanged,
            onDismiss = { showTimePickerDialog = false },
        )
    }
}

@Composable
private fun AboutGroup(onLicensesClick: () -> Unit) {
    val colors = ExtendedTheme.colors
    val context = LocalContext.current
    val versionName = remember {
        context.packageManager.getPackageInfo(context.packageName, 0).versionName.orEmpty()
    }

    SettingsGroupCard {
        SettingItem(
            emoji = Emojis.INFO,
            iconBgColor = colors.accentBg,
            title = stringResource(R.string.about_app),
            description = stringResource(R.string.about_app_description),
        )
        SettingItem(
            emoji = Emojis.PAGE,
            iconBgColor = colors.lavenderBg,
            title = stringResource(R.string.licenses),
            description = stringResource(R.string.licenses_description),
            onClick = onLicensesClick,
        )
        SettingItem(
            emoji = Emojis.NUMBERS,
            iconBgColor = colors.sageBg,
            title = stringResource(R.string.app_name),
            description = "${stringResource(R.string.version)} $versionName",
        )
    }
}
