package com.lianglliu.hermoodbarometer.feature.record

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lianglliu.hermoodbarometer.core.domain.AddEmotionRecordUseCase
import com.lianglliu.hermoodbarometer.core.model.data.Emotion
import com.lianglliu.hermoodbarometer.core.model.data.EmotionRecord
import com.lianglliu.hermoodbarometer.repository.EmotionDefinitionRepository
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
 * - 加载用户创建的情绪列表
 * - 验证和保存情绪记录
 * - 错误处理和用户反馈
 *
 * @param addEmotionRecordUseCase 添加情绪记录的用例
 * @param emotionDefinitionRepository 情绪定义的仓库
 */
@HiltViewModel
class RecordViewModel @Inject constructor(
    private val addEmotionRecordUseCase: AddEmotionRecordUseCase,
    private val emotionDefinitionRepository: EmotionDefinitionRepository
) : ViewModel() {

    companion object {
        const val DEFAULT_INTENSITY_LEVEL = 3f
        const val MIN_INTENSITY_LEVEL = 1f
        const val MAX_INTENSITY_LEVEL = 5f
        const val MAX_NOTE_LENGTH = 500

        // Error message tags (can be more specific if needed)
        const val ERROR_TAG_NETWORK = "network"
        const val ERROR_TAG_TIMEOUT = "timeout"
        const val ERROR_TAG_DATABASE = "database"

        // Generic error messages
        const val ERROR_MSG_SAVE_FAILED_RETRY = "保存失败，请重试"
        const val ERROR_MSG_NETWORK_FAILURE = "网络连接失败，请检查网络后重试"
        const val ERROR_MSG_REQUEST_TIMEOUT = "请求超时，请重试"
        const val ERROR_MSG_DATABASE_SAVE_FAILED = "数据保存失败，请重试"
        const val ERROR_MSG_INVALID_INPUT = "输入数据无效，请检查后重试"
        const val ERROR_MSG_APP_STATE_ERROR = "应用状态异常，请重启应用后重试"
        const val ERROR_MSG_SYSTEM_ERROR = "系统异常，请重试"

        // Validation messages
        const val VALIDATION_MSG_SELECT_EMOTION = "请选择情绪类型"
        const val VALIDATION_MSG_INTENSITY_RANGE =
            "情绪强度必须在${MIN_INTENSITY_LEVEL.toInt()}-${MAX_INTENSITY_LEVEL.toInt()}之间"

        fun validationMsgNoteLength(maxLength: Int) = "备注不能超过${maxLength}个字符"

    }

    /** 内部可变的UI状态 */
    private val _uiState = MutableStateFlow(RecordUiState())

    /** 对外暴露的只读UI状态 */
    val uiState: StateFlow<RecordUiState> = _uiState.asStateFlow()

    init {
        loadUserEmotions()
    }

    /**
     * 加载用户创建的情绪
     */
    private fun loadUserEmotions() {
        viewModelScope.launch {
            // Assuming this flow is designed to emit updates if user emotions change elsewhere
            emotionDefinitionRepository.getUserCreatedEmotions().collect { emotions ->
                _uiState.update { state ->
                    state.copy(userEmotions = emotions)
                }
            }
        }
    }

    /**
     * 更新选中的情绪
     */
    fun updateSelectedEmotion(emotion: Emotion?) {
        _uiState.update { currentState ->
            currentState.copy(
                selectedEmotion = emotion,
                errorMessage = null // Clear previous error when selection changes
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

        val validationError = validateEmotionRecord(currentState)
        if (validationError != null) {
            _uiState.update { state ->
                state.copy(errorMessage = validationError)
            }
            return
        }

        // selectedEmotion is guaranteed to be non-null here due to validation
        val emotionRecord = EmotionRecord.fromEmotion(
            emotion = currentState.selectedEmotion!!,
            intensity = currentState.intensityLevel.toInt(),
            note = currentState.noteText
        )

        _uiState.update { state ->
            state.copy(isLoading = true, errorMessage = null)
        }

        viewModelScope.launch {
            addEmotionRecordUseCase(emotionRecord)
                .onSuccess {
                    _uiState.update { state ->
                        state.copy(
                            selectedEmotion = null,
                            intensityLevel = DEFAULT_INTENSITY_LEVEL,
                            noteText = "",
                            isLoading = false,
                            errorMessage = null,
                            showSuccessMessage = true
                        )
                    }
                }
                .onFailure { exception ->
                    _uiState.update { state ->
                        state.copy(
                            isLoading = false,
                            errorMessage = mapExceptionToFriendlyMessage(exception)
                        )
                    }
                }
        }
    }

    /**
     * 将异常映射为用户友好的错误消息。
     * 假设 UseCase 会将特定的业务异常（如网络、数据库问题）封装并传递。
     */
    private fun mapExceptionToFriendlyMessage(exception: Throwable): String {
        // Log the actual exception for debugging purposes
        // Log.e("RecordViewModel", "Error saving emotion record", exception)

        val message = exception.message?.lowercase() ?: ""
        return when {
            message.contains(ERROR_TAG_NETWORK) -> ERROR_MSG_NETWORK_FAILURE
            message.contains(ERROR_TAG_TIMEOUT) -> ERROR_MSG_REQUEST_TIMEOUT
            message.contains(ERROR_TAG_DATABASE) -> ERROR_MSG_DATABASE_SAVE_FAILED
            exception is IllegalArgumentException -> ERROR_MSG_INVALID_INPUT
            exception is IllegalStateException -> ERROR_MSG_APP_STATE_ERROR
            else -> ERROR_MSG_SAVE_FAILED_RETRY
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
            state.selectedEmotion == null ->
                VALIDATION_MSG_SELECT_EMOTION

            state.intensityLevel < MIN_INTENSITY_LEVEL || state.intensityLevel > MAX_INTENSITY_LEVEL ->
                VALIDATION_MSG_INTENSITY_RANGE

            state.noteText.length > MAX_NOTE_LENGTH ->
                validationMsgNoteLength(MAX_NOTE_LENGTH)

            else -> null
        }
    }
}

/**
 * 记录页面的UI状态
 * (No changes needed here based on the ViewModel refactoring,
 * but ensure its KDoc is up-to-date if RecordViewModel.DEFAULT_INTENSITY_LEVEL is used as its default)
 *
 * @property selectedEmotion 当前选中的情绪
 * @property userEmotions 用户创建的情绪列表
 * @property intensityLevel 情绪强度等级（默认由 RecordViewModel.DEFAULT_INTENSITY_LEVEL 定义）
 * @property noteText 备注文本内容
 * @property isLoading 是否正在保存数据
 * @property errorMessage 错误信息（null表示无错误）
 * @property showSuccessMessage 是否显示成功提示
 */
data class RecordUiState(
    val selectedEmotion: Emotion? = null,
    val userEmotions: List<Emotion> = emptyList(),
    val intensityLevel: Float = 3f,
    val noteText: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val showSuccessMessage: Boolean = false
)
