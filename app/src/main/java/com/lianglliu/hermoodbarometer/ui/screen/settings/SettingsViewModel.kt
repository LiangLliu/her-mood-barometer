package com.lianglliu.hermoodbarometer.ui.screen.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lianglliu.hermoodbarometer.domain.model.CustomEmotion
import com.lianglliu.hermoodbarometer.domain.repository.CustomEmotionRepository
import com.lianglliu.hermoodbarometer.domain.repository.PreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 设置页面的ViewModel
 * 负责处理应用设置和自定义情绪管理
 */
@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val preferencesRepository: PreferencesRepository,
    private val customEmotionRepository: CustomEmotionRepository
) : ViewModel() {

    // UI状态
    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        loadSettings()
        loadCustomEmotions()
    }

    /**
     * 加载应用设置
     */
    private fun loadSettings() {
        viewModelScope.launch {
            try {
                preferencesRepository.getLanguage().collect { language ->
                    preferencesRepository.getTheme().collect { theme ->
                        preferencesRepository.isReminderEnabled().collect { isReminderEnabled ->
                            preferencesRepository.getReminderTime().collect { reminderTime ->
                                _uiState.value = _uiState.value.copy(
                                    selectedLanguage = language,
                                    selectedTheme = theme,
                                    isReminderEnabled = isReminderEnabled,
                                    reminderTime = reminderTime
                                )
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = e.message ?: "Failed to load settings"
                )
            }
        }
    }

    /**
     * 加载自定义情绪
     */
    private fun loadCustomEmotions() {
        viewModelScope.launch {
            try {
                customEmotionRepository.getAllCustomEmotions().collect { emotions ->
                    _uiState.value = _uiState.value.copy(
                        customEmotions = emotions
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = e.message ?: "Failed to load custom emotions"
                )
            }
        }
    }

    /**
     * 更新语言设置
     */
    fun updateLanguage(language: String) {
        viewModelScope.launch {
            try {
                preferencesRepository.setLanguage(language)
                _uiState.value = _uiState.value.copy(
                    selectedLanguage = language,
                    shouldRecreateActivity = true
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = e.message ?: "Failed to update language"
                )
            }
        }
    }

    /**
     * 更新主题设置
     */
    fun updateTheme(theme: String) {
        viewModelScope.launch {
            try {
                preferencesRepository.setTheme(theme)
                _uiState.value = _uiState.value.copy(
                    selectedTheme = theme
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = e.message ?: "Failed to update theme"
                )
            }
        }
    }

    /**
     * 更新提醒设置
     */
    fun updateReminderSettings(isEnabled: Boolean, time: String = "") {
        viewModelScope.launch {
            try {
                preferencesRepository.setReminderEnabled(isEnabled)
                if (time.isNotEmpty()) {
                    preferencesRepository.setReminderTime(time)
                }
                _uiState.value = _uiState.value.copy(
                    isReminderEnabled = isEnabled,
                    reminderTime = if (time.isNotEmpty()) time else _uiState.value.reminderTime
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = e.message ?: "Failed to update reminder settings"
                )
            }
        }
    }

    /**
     * 添加自定义情绪
     */
    fun addCustomEmotion(name: String, description: String, emoji: String) {
        viewModelScope.launch {
            try {
                val customEmotion = CustomEmotion.create(
                    name = name,
                    description = description,
                    emoji = emoji
                )
                customEmotionRepository.insertCustomEmotion(customEmotion)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = e.message ?: "Failed to add custom emotion"
                )
            }
        }
    }

    /**
     * 删除自定义情绪
     */
    fun deleteCustomEmotion(emotion: CustomEmotion) {
        viewModelScope.launch {
            try {
                customEmotionRepository.deleteCustomEmotion(emotion.id)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = e.message ?: "Failed to delete custom emotion"
                )
            }
        }
    }

    /**
     * 清除错误消息
     */
    fun clearErrorMessage() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }

    /**
     * 清除Activity重新创建标志
     */
    fun clearRecreateActivityFlag() {
        _uiState.value = _uiState.value.copy(shouldRecreateActivity = false)
    }
}

/**
 * 设置页面的UI状态
 */
data class SettingsUiState(
    val selectedLanguage: String = "en",
    val selectedTheme: String = "system",
    val isReminderEnabled: Boolean = false,
    val reminderTime: String = "09:00",
    val customEmotions: List<CustomEmotion> = emptyList(),
    val errorMessage: String? = null,
    val shouldRecreateActivity: Boolean = false
) 