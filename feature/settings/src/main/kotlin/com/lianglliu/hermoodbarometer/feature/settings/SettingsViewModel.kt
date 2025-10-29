package com.lianglliu.hermoodbarometer.feature.settings

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lianglliu.hermoodbarometer.core.model.data.DarkThemeConfig
import com.lianglliu.hermoodbarometer.core.model.data.Language
import com.lianglliu.hermoodbarometer.repository.UserDataRepository
import com.lianglliu.hermoodbarometer.util.AppLocaleManager
import com.lianglliu.hermoodbarometer.util.ReminderScheduler
import dagger.hilt.android.lifecycle.HiltViewModel
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
    private val userDataRepository: UserDataRepository,
    private val appLocaleManager: AppLocaleManager,
    private val reminderScheduler: ReminderScheduler,
) : ViewModel() {
    private val availableLanguages = Language.entries

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
        viewModelScope.launch {
            userDataRepository.setReminderStatus(reminderStatus)
            if (reminderStatus) {
                // Schedule daily reminder with current time
                val currentSettings = settingsUiState.value
                if (currentSettings is SettingsUiState.Success) {
                    scheduleDailyReminder(currentSettings.settings.reminderTime)
                }
            } else {
                // Cancel daily reminder
                reminderScheduler.cancelDailyReminder()
            }
        }
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


