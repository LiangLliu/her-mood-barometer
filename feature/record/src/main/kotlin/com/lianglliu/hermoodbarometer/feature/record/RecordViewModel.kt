package com.lianglliu.hermoodbarometer.feature.record

import android.util.Log
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lianglliu.hermoodbarometer.core.domain.AddEmotionRecordUseCase
import com.lianglliu.hermoodbarometer.core.model.data.Emotion
import com.lianglliu.hermoodbarometer.core.model.data.EmotionIntensity
import com.lianglliu.hermoodbarometer.core.model.data.EmotionRecord
import com.lianglliu.hermoodbarometer.repository.EmotionDefinitionRepository
import com.lianglliu.hermoodbarometer.core.locales.R as LocalesR
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
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
    }

    /** 内部可变的UI状态 */
    private val _uiState = MutableStateFlow(RecordUiState())

    /** 对外暴露的只读UI状态 */
    val uiState: StateFlow<RecordUiState> = _uiState.asStateFlow()

    init {
        Log.d("RecordVM", "Init RecordViewModel")

        // 延迟加载用户情绪，避免初始化时的性能压力
        viewModelScope.launch {
            // 给UI一点时间完成初始渲染
            kotlinx.coroutines.delay(300)
            loadUserEmotions()
        }
    }

    /**
     * 加载用户创建的情绪
     */
    private fun loadUserEmotions() {
        val startTime = System.currentTimeMillis()

        viewModelScope.launch {
            // Assuming this flow is designed to emit updates if user emotions change elsewhere
            emotionDefinitionRepository.getUserCreatedEmotions()
                .flowOn(kotlinx.coroutines.Dispatchers.IO)
                .collect { emotions ->
                    Log.d("RecordVM", "Emotions loaded: ${emotions.size} items (${System.currentTimeMillis() - startTime}ms)")
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
                errorMessageResId = null // Clear previous error when selection changes
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
        val startTime = System.currentTimeMillis()

        val currentState = _uiState.value

        val validationErrorResId = validateEmotionRecord(currentState)
        if (validationErrorResId != null) {
            _uiState.update { state ->
                state.copy(errorMessageResId = validationErrorResId)
            }
            return
        }

        // selectedEmotion is guaranteed to be non-null here due to validation
        val emotion = currentState.selectedEmotion!!
        val emotionRecord = EmotionRecord(
            emotionId = emotion.id,
            emotionEmoji = emotion.emoji,
            intensity = EmotionIntensity.fromLevel(currentState.intensityLevel.toInt()),
            note = currentState.noteText
        )

        _uiState.update { state ->
            state.copy(isLoading = true, errorMessageResId = null)
        }

        viewModelScope.launch {
            addEmotionRecordUseCase(emotionRecord)
                .onSuccess {
                    Log.d("RecordVM", "Record saved (${System.currentTimeMillis() - startTime}ms)")
                    _uiState.update { state ->
                        state.copy(
                            selectedEmotion = null,
                            intensityLevel = DEFAULT_INTENSITY_LEVEL,
                            noteText = "",
                            isLoading = false,
                            errorMessageResId = null,
                            showSuccessMessage = true
                        )
                    }
                }
                .onFailure { exception ->
                    val errorResId = mapExceptionToErrorResourceId(exception)
                    Log.e("RecordVM", "Save failed: error resource $errorResId")
                    _uiState.update { state ->
                        state.copy(
                            isLoading = false,
                            errorMessageResId = errorResId
                        )
                    }
                }
        }
    }

    /**
     * 将异常映射为用户友好的错误消息资源ID。
     * 假设 UseCase 会将特定的业务异常（如网络、数据库问题）封装并传递。
     */
    @StringRes
    private fun mapExceptionToErrorResourceId(exception: Throwable): Int {
        val message = exception.message?.lowercase() ?: ""
        return when {
            message.contains(ERROR_TAG_NETWORK) -> LocalesR.string.error_network_failed
            message.contains(ERROR_TAG_TIMEOUT) -> LocalesR.string.error_timeout
            message.contains(ERROR_TAG_DATABASE) -> LocalesR.string.error_data_save_failed
            exception is IllegalArgumentException -> LocalesR.string.error_invalid_input
            exception is IllegalStateException -> LocalesR.string.error_app_state
            else -> LocalesR.string.error_save_failed
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
            state.copy(errorMessageResId = null)
        }
    }

    /**
     * 验证情绪记录数据
     * @return 错误消息的资源ID，如果验证通过则返回null
     */
    @StringRes
    private fun validateEmotionRecord(state: RecordUiState): Int? {
        return when {
            state.selectedEmotion == null ->
                LocalesR.string.validation_select_emotion

            state.intensityLevel < MIN_INTENSITY_LEVEL || state.intensityLevel > MAX_INTENSITY_LEVEL ->
                LocalesR.string.validation_intensity_range

            state.noteText.length > MAX_NOTE_LENGTH ->
                LocalesR.string.validation_note_too_long

            else -> null
        }
    }
}

/**
 * 记录页面的UI状态
 *
 * @property selectedEmotion 当前选中的情绪
 * @property userEmotions 用户创建的情绪列表
 * @property intensityLevel 情绪强度等级（默认由 RecordViewModel.DEFAULT_INTENSITY_LEVEL 定义）
 * @property noteText 备注文本内容
 * @property isLoading 是否正在保存数据
 * @property errorMessageResId 错误信息资源ID（null表示无错误）
 * @property showSuccessMessage 是否显示成功提示
 */
data class RecordUiState(
    val selectedEmotion: Emotion? = null,
    val userEmotions: List<Emotion> = emptyList(),
    val intensityLevel: Float = 3f,
    val noteText: String = "",
    val isLoading: Boolean = false,
    @StringRes val errorMessageResId: Int? = null,
    val showSuccessMessage: Boolean = false
)
