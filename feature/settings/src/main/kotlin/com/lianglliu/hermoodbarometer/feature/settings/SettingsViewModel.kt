package com.lianglliu.hermoodbarometer.feature.settings

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.lianglliu.hermoodbarometer.core.model.data.DarkThemeConfig
import com.lianglliu.hermoodbarometer.core.model.data.Language
import com.lianglliu.hermoodbarometer.core.permissions.PermissionCheckResult
import com.lianglliu.hermoodbarometer.core.permissions.PermissionHelpers
import com.lianglliu.hermoodbarometer.core.permissions.PermissionState
import com.lianglliu.hermoodbarometer.repository.UserDataRepository
import com.lianglliu.hermoodbarometer.util.AppLocaleManager
import com.lianglliu.hermoodbarometer.util.ReminderScheduler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 设置页面的ViewModel
 * 负责处理应用设置和自定义情绪管理
 */
@HiltViewModel
class SettingsViewModel @Inject constructor(
    application: Application,
    private val userDataRepository: UserDataRepository,
    private val appLocaleManager: AppLocaleManager,
    private val reminderScheduler: ReminderScheduler,
) : AndroidViewModel(application) {
    private val availableLanguages = Language.entries

    // 权限状态流
    private val _permissionState = MutableStateFlow<PermissionCheckResult>(PermissionCheckResult.Idle)
    val permissionState: StateFlow<PermissionCheckResult> = _permissionState

    init {
        Log.d("SettingsVM", "Init SettingsViewModel")
    }

    val settingsUiState: StateFlow<SettingsUiState> = combine(
        userDataRepository.userData,
        appLocaleManager.currentLocale,
    ) { userData, currentLocale ->
        val language = availableLanguages
            .find { it.code == currentLocale } ?: availableLanguages.first()
        SettingsUiState.Success(
            settings = UserEditableSettings(
                useDynamicColor = userData.useDynamicColor,
                darkThemeConfig = userData.darkThemeConfig,
                isReminderEnabled = userData.reminderStatus,
                reminderTime = userData.reminderTime,
                language = language,
                availableLanguages = availableLanguages,
            )
        )
    }
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(5_000),
            initialValue = SettingsUiState.Loading,
        )

    fun updateDarkThemeConfig(darkThemeConfig: DarkThemeConfig) {
        viewModelScope.launch {
            userDataRepository.setDarkThemeConfig(darkThemeConfig)
        }
    }

    fun updateDynamicColorPreference(useDynamicColor: Boolean) {
        viewModelScope.launch {
            userDataRepository.setDynamicColorPreference(useDynamicColor)
        }
    }

    fun updateLanguage(language: String) {
        appLocaleManager.updateLocale(language)
    }

    fun updateReminderEnabled(reminderStatus: Boolean) {
        if (reminderStatus) {
            // 检查所需权限
            val missingPermissions = checkRequiredPermissions()

            if (missingPermissions.isNotEmpty()) {
                // 有权限未授予，通知UI层处理
                _permissionState.value = PermissionCheckResult.PermissionsRequired(missingPermissions)
                return
            }
        }

        // 权限已授予或关闭提醒，继续处理
        viewModelScope.launch {
            userDataRepository.setReminderStatus(reminderStatus)
            if (reminderStatus) {
                val currentSettings = settingsUiState.value
                if (currentSettings is SettingsUiState.Success) {
                    scheduleDailyReminder(currentSettings.settings.reminderTime)
                }
            } else {
                reminderScheduler.cancelDailyReminder()
            }
            _permissionState.value = PermissionCheckResult.Success
        }
    }

    /**
     * 检查所需权限
     */
    private fun checkRequiredPermissions(): List<String> {
        val context = getApplication<Application>()
        val missingPermissions = mutableListOf<String>()

        // 检查通知权限
        if (PermissionHelpers.checkPermissionState(context, "notification") != PermissionState.Granted) {
            missingPermissions.add("notification")
        }

        // 检查精确闹钟权限
        if (PermissionHelpers.checkPermissionState(context, "exact_alarm") != PermissionState.Granted) {
            missingPermissions.add("exact_alarm")
        }

        return missingPermissions
    }

    /**
     * 从设置页面返回后重新检查权限
     */
    fun recheckPermissionsAndContinue() {
        val settingsState = settingsUiState.value
        if (settingsState is SettingsUiState.Success && settingsState.settings.isReminderEnabled) {
            updateReminderEnabled(true)
        }
    }

    /**
     * 清除权限状态
     */
    fun clearPermissionState() {
        _permissionState.value = PermissionCheckResult.Idle
    }

    fun updateReminderTime(reminderTime: String) {
        viewModelScope.launch {
            userDataRepository.setReminderTime(reminderTime)
            // If reminder is enabled, reschedule with new time
            val currentSettings = settingsUiState.value
            if (currentSettings is SettingsUiState.Success &&
                currentSettings.settings.isReminderEnabled) {
                scheduleDailyReminder(reminderTime)
            }
        }
    }

    private fun scheduleDailyReminder(reminderTime: String) {
        // Parse the time string (HH:mm)
        val timeParts = reminderTime.split(":")
        if (timeParts.size == 2) {
            val hour = timeParts[0].toIntOrNull() ?: 9
            val minute = timeParts[1].toIntOrNull() ?: 0
            reminderScheduler.scheduleDailyReminder(hour, minute)
        }
    }
}

/**
 * 设置页面的UI状态
 */
data class UserEditableSettings(
    val useDynamicColor: Boolean,
    val darkThemeConfig: DarkThemeConfig,
    val language: Language,
    val availableLanguages: List<Language>,

    val isReminderEnabled: Boolean = false,
    val reminderTime: String = "09:00",
)

sealed interface SettingsUiState {

    data object Loading : SettingsUiState

    data class Success(val settings: UserEditableSettings) : SettingsUiState
}


