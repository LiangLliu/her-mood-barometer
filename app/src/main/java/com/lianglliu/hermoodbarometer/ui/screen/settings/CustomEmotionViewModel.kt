package com.lianglliu.hermoodbarometer.ui.screen.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lianglliu.hermoodbarometer.domain.model.CustomEmotion
import com.lianglliu.hermoodbarometer.domain.repository.CustomEmotionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 自定义情绪管理页面的ViewModel
 */
@HiltViewModel
class CustomEmotionViewModel @Inject constructor(
    private val customEmotionRepository: CustomEmotionRepository
) : ViewModel() {

    // UI状态
    private val _uiState = MutableStateFlow(CustomEmotionUiState())
    val uiState: StateFlow<CustomEmotionUiState> = _uiState.asStateFlow()

    init {
        loadCustomEmotions()
    }

    /**
     * 加载自定义情绪
     */
    private fun loadCustomEmotions() {
        viewModelScope.launch {
            try {
                customEmotionRepository.getAllCustomEmotions().collect { emotions ->
                    _uiState.value = _uiState.value.copy(
                        customEmotions = emotions,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = e.message ?: "Failed to load custom emotions",
                    isLoading = false
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
     * 更新自定义情绪
     */
    fun updateCustomEmotion(emotion: CustomEmotion) {
        viewModelScope.launch {
            try {
                customEmotionRepository.updateCustomEmotion(emotion)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = e.message ?: "Failed to update custom emotion"
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
}

/**
 * 自定义情绪页面的UI状态
 */
data class CustomEmotionUiState(
    val customEmotions: List<CustomEmotion> = emptyList(),
    val isLoading: Boolean = true,
    val errorMessage: String? = null
) 