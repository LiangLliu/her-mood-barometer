package com.lianglliu.hermoodbarometer.feature.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lianglliu.hermoodbarometer.core.model.data.DarkThemeConfig
import com.lianglliu.hermoodbarometer.core.model.data.Language
import com.lianglliu.hermoodbarometer.repository.UserDataRepository
import com.lianglliu.hermoodbarometer.util.AppLocaleManager
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
) : ViewModel() {
    private val availableLanguages = Language.entries

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
}

/**
 * 设置页面的UI状态
 */
data class UserEditableSettings(
    val useDynamicColor: Boolean,
    val darkThemeConfig: DarkThemeConfig,
    val language: Language,
    val availableLanguages: List<Language>,

//    val isReminderEnabled: Boolean = false,
//    val reminderTime: String = "09:00",
//    val userEmotions: List<Emotion> = emptyList(),
//    val errorMessage: String? = null,
//    val shouldRecreateActivity: Boolean = false,
//    // 标记设置是否已从数据源加载完成，避免启动阶段用默认值覆盖已保存的语言
//    val isInitialized: Boolean = false
)

sealed interface SettingsUiState {

    data object Loading : SettingsUiState

    data class Success(val settings: UserEditableSettings) : SettingsUiState
}


