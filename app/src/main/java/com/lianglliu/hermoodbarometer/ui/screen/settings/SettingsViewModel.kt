package com.lianglliu.hermoodbarometer.ui.screen.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lianglliu.hermoodbarometer.domain.model.Emotion
import com.lianglliu.hermoodbarometer.domain.repository.EmotionDefinitionRepository
import com.lianglliu.hermoodbarometer.domain.repository.NotificationRepository
import com.lianglliu.hermoodbarometer.domain.repository.PreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 设置页面的ViewModel
 * 负责处理应用设置和自定义情绪管理
 */
@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val preferencesRepository: PreferencesRepository,
    private val emotionDefinitionRepository: EmotionDefinitionRepository,
    private val notificationRepository: NotificationRepository
) : ViewModel() {

    // UI状态
    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        loadSettings()
        loadUserEmotions()
    }

    /**
     * 加载应用设置
     */
    private fun loadSettings() {
        viewModelScope.launch {
            try {
                combine(
                    preferencesRepository.getLanguage(),
                    preferencesRepository.getTheme(),
                    preferencesRepository.isReminderEnabled(),
                    preferencesRepository.getReminderTime()
                ) { language, theme, isReminderEnabled, reminderTime ->
                    SettingsUiState(
                        selectedLanguage = language,
                        selectedTheme = theme,
                        isReminderEnabled = isReminderEnabled,
                        reminderTime = reminderTime,
                        userEmotions = _uiState.value.userEmotions,
                        errorMessage = null,
                        shouldRecreateActivity = _uiState.value.shouldRecreateActivity,
                        isInitialized = true
                    )
                }.collect { combined ->
                    _uiState.value = combined
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = e.message ?: "Failed to load settings"
                )
            }
        }
    }

    /**
     * 加载用户创建的情绪
     */
    private fun loadUserEmotions() {
        viewModelScope.launch {
            try {
                emotionDefinitionRepository.getUserCreatedEmotions().collect { emotions ->
                    _uiState.value = _uiState.value.copy(
                        userEmotions = emotions
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = e.message ?: "Failed to load user emotions"
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
                    selectedLanguage = language
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
                    reminderTime = time.ifEmpty { _uiState.value.reminderTime }
                )

                // 调度或取消 WorkManager 周期任务
                if (isEnabled) {
                    val finalTime = time.ifEmpty { _uiState.value.reminderTime }
                    notificationRepository.scheduleDailyReminder(finalTime)
                } else {
                    notificationRepository.cancelDailyReminder()
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = e.message ?: "Failed to update reminder settings"
                )
            }
        }
    }

    /**
     * 添加用户情绪
     */
    fun addUserEmotion(name: String, description: String, emoji: String) {
        viewModelScope.launch {
            try {
                val emotion = Emotion.createUserEmotion(
                    name = name,
                    emoji = emoji,
                    description = description,
                    id = System.currentTimeMillis()
                )
                emotionDefinitionRepository.insertEmotion(emotion)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = e.message ?: "Failed to add user emotion"
                )
            }
        }
    }

    /**
     * 删除用户情绪
     */
    fun deleteUserEmotion(emotion: Emotion) {
        viewModelScope.launch {
            try {
                emotionDefinitionRepository.deleteEmotion(emotion)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = e.message ?: "Failed to delete user emotion"
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
    fun clearRecreateActivityFlag() { /* no-op after per-app locales */ }
}

/**
 * 设置页面的UI状态
 */
data class SettingsUiState(
    val selectedLanguage: String = "system",
    val selectedTheme: String = "system",
    val isReminderEnabled: Boolean = false,
    val reminderTime: String = "09:00",
    val userEmotions: List<Emotion> = emptyList(),
    val errorMessage: String? = null,
    val shouldRecreateActivity: Boolean = false,
    // 标记设置是否已从数据源加载完成，避免启动阶段用默认值覆盖已保存的语言
    val isInitialized: Boolean = false
) 