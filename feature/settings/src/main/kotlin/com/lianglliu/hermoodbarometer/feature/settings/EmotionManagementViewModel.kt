package com.lianglliu.hermoodbarometer.feature.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lianglliu.hermoodbarometer.core.model.data.Emotion

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
                val emotion = Emotion.createUserEmotion(
                    name = name,
                    emoji = emoji,
                    description = description,
                    id = System.currentTimeMillis() // 使用时间戳作为临时ID
                )
                emotionDefinitionRepository.insertEmotion(emotion)
            } catch (e: Exception) {
                _uiState.update { state ->
                    state.copy(errorMessage = "添加情绪失败：${e.message}")
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
                    state.copy(errorMessage = "更新情绪失败：${e.message}")
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
                emotionDefinitionRepository.deleteEmotion(emotion)
            } catch (e: Exception) {
                _uiState.update { state ->
                    state.copy(errorMessage = "删除情绪失败：${e.message}")
                }
            }
        }
    }

    /**
     * 清除错误消息
     */
    fun clearErrorMessage() {
        _uiState.update { state ->
            state.copy(errorMessage = null)
        }
    }
}

/**
 * 情绪管理页面的UI状态
 */
data class EmotionManagementUiState(
    val userEmotions: List<Emotion> = emptyList(),
    val errorMessage: String? = null
)
