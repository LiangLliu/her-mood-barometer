package com.lianglliu.hermoodbarometer.feature.settings

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lianglliu.hermoodbarometer.core.model.data.Emotion
import com.lianglliu.hermoodbarometer.core.locales.R as LocalesR
import com.lianglliu.hermoodbarometer.repository.EmotionDefinitionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 情绪管理页面的ViewModel
 */
@HiltViewModel
class EmotionManagementViewModel @Inject constructor(
    private val emotionDefinitionRepository: EmotionDefinitionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(EmotionManagementUiState())
    val uiState: StateFlow<EmotionManagementUiState> = _uiState.asStateFlow()

    init {
        loadUserEmotions()
    }

    /**
     * 加载用户创建的情绪
     */
    private fun loadUserEmotions() {
        viewModelScope.launch {
            emotionDefinitionRepository.getUserCreatedEmotions().collect { emotions ->
                _uiState.update { state ->
                    state.copy(userEmotions = emotions)
                }
            }
        }
    }

    /**
     * 添加新情绪
     */
    fun addEmotion(name: String, emoji: String, description: String) {
        viewModelScope.launch {
            try {
                val emotion = Emotion(
                    name = name,
                    emoji = emoji,
                    description = description,
                    isUserCreated = true,
                    id = 0L // Let the database generate the ID
                )
                emotionDefinitionRepository.insertEmotion(emotion)
            } catch (e: Exception) {
                _uiState.update { state ->
                    state.copy(
                        errorMessageResId = LocalesR.string.error_add_emotion_failed,
                        errorMessageArg = e.message
                    )
                }
            }
        }
    }

    /**
     * 更新情绪
     */
    fun updateEmotion(emotion: Emotion) {
        viewModelScope.launch {
            try {
                emotionDefinitionRepository.updateEmotion(emotion)
            } catch (e: Exception) {
                _uiState.update { state ->
                    state.copy(
                        errorMessageResId = LocalesR.string.error_update_emotion_failed,
                        errorMessageArg = e.message
                    )
                }
            }
        }
    }

    /**
     * 删除情绪
     */
    fun deleteEmotion(emotion: Emotion) {
        viewModelScope.launch {
            try {
                emotionDefinitionRepository.deactivateEmotion(emotion.id)
            } catch (e: Exception) {
                _uiState.update { state ->
                    state.copy(
                        errorMessageResId = LocalesR.string.error_delete_emotion_failed,
                        errorMessageArg = e.message
                    )
                }
            }
        }
    }

    /**
     * 清除错误消息
     */
    fun clearErrorMessage() {
        _uiState.update { state ->
            state.copy(errorMessageResId = null, errorMessageArg = null)
        }
    }
}

/**
 * 情绪管理页面的UI状态
 */
data class EmotionManagementUiState(
    val userEmotions: List<Emotion> = emptyList(),
    @StringRes val errorMessageResId: Int? = null,
    val errorMessageArg: String? = null
)
