package com.lianglliu.hermoodbarometer.ui.screen.record

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lianglliu.hermoodbarometer.domain.model.EmotionIntensity
import com.lianglliu.hermoodbarometer.domain.model.EmotionRecord
import com.lianglliu.hermoodbarometer.domain.model.EmotionType
import com.lianglliu.hermoodbarometer.domain.usecase.AddEmotionRecordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 记录页面的ViewModel
 * 负责处理情绪记录的业务逻辑
 */
@HiltViewModel
class RecordViewModel @Inject constructor(
    private val addEmotionRecordUseCase: AddEmotionRecordUseCase
) : ViewModel() {

    // UI状态
    private val _uiState = MutableStateFlow(RecordUiState())
    val uiState: StateFlow<RecordUiState> = _uiState.asStateFlow()

    /**
     * 更新选中的情绪类型
     */
    fun updateSelectedEmotion(emotion: EmotionType?) {
        _uiState.value = _uiState.value.copy(selectedEmotion = emotion)
    }

    /**
     * 更新情绪强度
     */
    fun updateIntensity(intensity: Float) {
        _uiState.value = _uiState.value.copy(intensityLevel = intensity)
    }

    /**
     * 更新备注文本
     */
    fun updateNote(note: String) {
        _uiState.value = _uiState.value.copy(noteText = note)
    }

    /**
     * 保存情绪记录
     */
    fun saveEmotionRecord() {
        val currentState = _uiState.value
        
        // 验证数据
        if (currentState.selectedEmotion == null) {
            _uiState.value = currentState.copy(
                errorMessage = "请选择情绪类型"
            )
            return
        }

        // 创建情绪记录
        val emotionRecord = EmotionRecord.create(
            emotionType = currentState.selectedEmotion!!,
            intensity = EmotionIntensity.fromLevel(currentState.intensityLevel.toInt()),
            note = currentState.noteText
        )

        // 显示加载状态
        _uiState.value = currentState.copy(isLoading = true, errorMessage = null)

        viewModelScope.launch {
            try {
                val result = addEmotionRecordUseCase(emotionRecord)
                result.fold(
                    onSuccess = {
                        // 保存成功，重置表单
                        _uiState.value = RecordUiState(
                            showSuccessMessage = true
                        )
                    },
                    onFailure = { exception ->
                        // 保存失败，显示错误信息
                        _uiState.value = currentState.copy(
                            isLoading = false,
                            errorMessage = exception.message ?: "保存失败，请重试"
                        )
                    }
                )
            } catch (e: Exception) {
                _uiState.value = currentState.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "保存失败，请重试"
                )
            }
        }
    }

    /**
     * 清除成功消息
     */
    fun clearSuccessMessage() {
        _uiState.value = _uiState.value.copy(showSuccessMessage = false)
    }

    /**
     * 清除错误消息
     */
    fun clearErrorMessage() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}

/**
 * 记录页面的UI状态
 */
data class RecordUiState(
    val selectedEmotion: EmotionType? = null,
    val intensityLevel: Float = 3f,
    val noteText: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val showSuccessMessage: Boolean = false
) 