package com.lianglliu.hermoodbarometer.ui.screen.record

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lianglliu.hermoodbarometer.domain.model.CustomEmotion
import com.lianglliu.hermoodbarometer.domain.model.EmotionIntensity
import com.lianglliu.hermoodbarometer.domain.model.EmotionRecord
import com.lianglliu.hermoodbarometer.domain.model.EmotionType
import com.lianglliu.hermoodbarometer.domain.repository.CustomEmotionRepository
import com.lianglliu.hermoodbarometer.domain.usecase.AddEmotionRecordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 记录页面的ViewModel
 *
 * 负责处理情绪记录的业务逻辑，包括：
 * - 管理UI状态（选中的情绪、强度、备注等）
 * - 加载自定义情绪列表
 * - 验证和保存情绪记录
 * - 错误处理和用户反馈
 *
 * @param addEmotionRecordUseCase 添加情绪记录的用例
 * @param customEmotionRepository 自定义情绪的仓库
 */
@HiltViewModel
class RecordViewModel @Inject constructor(
    private val addEmotionRecordUseCase: AddEmotionRecordUseCase,
    private val customEmotionRepository: CustomEmotionRepository
) : ViewModel() {

    /** 内部可变的UI状态 */
    private val _uiState = MutableStateFlow(RecordUiState())

    /** 对外暴露的只读UI状态 */
    val uiState: StateFlow<RecordUiState> = _uiState.asStateFlow()

    init {
        loadCustomEmotions()
    }

    /**
     * 加载自定义情绪
     */
    private fun loadCustomEmotions() {
        viewModelScope.launch {
            customEmotionRepository.getAllCustomEmotions().collect { emotions ->
                _uiState.update { state ->
                    state.copy(customEmotions = emotions)
                }
            }
        }
    }

    /**
     * 更新选中的情绪类型
     */
    fun updateSelectedEmotion(emotion: EmotionType?) {
        _uiState.update { currentState ->
            currentState.copy(
                selectedEmotion = emotion,
                selectedCustomEmotion = null,
                errorMessage = null
            )
        }
    }

    /**
     * 更新选中的自定义情绪
     */
    fun updateSelectedCustomEmotion(emotion: CustomEmotion?) {
        _uiState.update { currentState ->
            currentState.copy(
                selectedCustomEmotion = emotion,
                selectedEmotion = null,
                errorMessage = null
            )
        }
    }

    /**
     * 更新情绪强度
     */
    fun updateIntensity(intensity: Float) {
        _uiState.update { currentState ->
            currentState.copy(intensityLevel = intensity)
        }
    }

    /**
     * 更新备注文本
     */
    fun updateNote(note: String) {
        _uiState.update { currentState ->
            currentState.copy(noteText = note)
        }
    }

    /**
     * 保存情绪记录
     */
    fun saveEmotionRecord() {
        val currentState = _uiState.value

        // 详细验证数据
        val validationError = validateEmotionRecord(currentState)
        if (validationError != null) {
            _uiState.update { state ->
                state.copy(errorMessage = validationError)
            }
            return
        }

        // 创建情绪记录
        val emotionRecord = if (currentState.selectedEmotion != null) {
            // 预定义情绪
            EmotionRecord.create(
                emotionType = currentState.selectedEmotion,
                intensity = EmotionIntensity.fromLevel(currentState.intensityLevel.toInt()),
                note = currentState.noteText
            )
        } else {
            // 自定义情绪
            EmotionRecord.createCustom(
                customEmotionId = currentState.selectedCustomEmotion!!.id,
                customEmotionName = currentState.selectedCustomEmotion!!.name,
                intensity = EmotionIntensity.fromLevel(currentState.intensityLevel.toInt()),
                note = currentState.noteText
            )
        }

        // 显示加载状态
        _uiState.update { state ->
            state.copy(isLoading = true, errorMessage = null)
        }

        viewModelScope.launch {
            try {
                val result = addEmotionRecordUseCase(emotionRecord)
                result.fold(
                    onSuccess = {
                        // 保存成功，重置表单但保留自定义情绪列表
                        _uiState.update { state ->
                            state.copy(
                                selectedEmotion = null,
                                selectedCustomEmotion = null,
                                intensityLevel = 3f,
                                noteText = "",
                                isLoading = false,
                                errorMessage = null,
                                showSuccessMessage = true
                            )
                        }
                    },
                    onFailure = { exception ->
                        // 保存失败，显示友好的错误信息
                        val friendlyErrorMessage = when {
                            exception.message?.contains("network", ignoreCase = true) == true ->
                                "网络连接失败，请检查网络后重试"

                            exception.message?.contains("timeout", ignoreCase = true) == true ->
                                "请求超时，请重试"

                            exception.message?.contains("database", ignoreCase = true) == true ->
                                "数据保存失败，请重试"

                            else -> "保存失败，请重试"
                        }
                        _uiState.update { state ->
                            state.copy(
                                isLoading = false,
                                errorMessage = friendlyErrorMessage
                            )
                        }
                    }
                )
            } catch (e: Exception) {
                // 处理未捕获的异常
                val friendlyErrorMessage = when (e) {
                    is IllegalArgumentException -> "输入数据无效，请检查后重试"
                    is IllegalStateException -> "应用状态异常，请重启应用后重试"
                    else -> "系统异常，请重试"
                }
                _uiState.update { state ->
                    state.copy(
                        isLoading = false,
                        errorMessage = friendlyErrorMessage
                    )
                }
            }
        }
    }

    /**
     * 清除成功消息
     */
    fun clearSuccessMessage() {
        _uiState.update { state ->
            state.copy(showSuccessMessage = false)
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

    /**
     * 验证情绪记录数据
     */
    private fun validateEmotionRecord(state: RecordUiState): String? {
        return when {
            state.selectedEmotion == null && state.selectedCustomEmotion == null ->
                "请选择情绪类型"

            state.intensityLevel < 1f || state.intensityLevel > 5f ->
                "情绪强度必须在1-5之间"

            state.noteText.length > 500 ->
                "备注不能超过500个字符"

            else -> null
        }
    }
}

/**
 * 记录页面的UI状态
 *
 * @property selectedEmotion 当前选中的预定义情绪类型
 * @property selectedCustomEmotion 当前选中的自定义情绪（与selectedEmotion互斥）
 * @property customEmotions 可用的自定义情绪列表
 * @property intensityLevel 情绪强度等级（1-5，默认3）
 * @property noteText 备注文本内容
 * @property isLoading 是否正在保存数据
 * @property errorMessage 错误信息（null表示无错误）
 * @property showSuccessMessage 是否显示成功提示
 */
data class RecordUiState(
    val selectedEmotion: EmotionType? = null,
    val selectedCustomEmotion: CustomEmotion? = null,
    val customEmotions: List<CustomEmotion> = emptyList(),
    val intensityLevel: Float = 3f,
    val noteText: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val showSuccessMessage: Boolean = false
) 