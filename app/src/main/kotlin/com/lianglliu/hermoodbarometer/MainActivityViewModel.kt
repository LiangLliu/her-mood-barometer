package com.lianglliu.hermoodbarometer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lianglliu.hermoodbarometer.MainActivityUiState.Loading
import com.lianglliu.hermoodbarometer.core.domain.AddEmotionRecordUseCase
import com.lianglliu.hermoodbarometer.core.model.data.DarkThemeConfig.DARK
import com.lianglliu.hermoodbarometer.core.model.data.DarkThemeConfig.FOLLOW_SYSTEM
import com.lianglliu.hermoodbarometer.core.model.data.DarkThemeConfig.LIGHT
import com.lianglliu.hermoodbarometer.core.model.data.EmotionRecord
import com.lianglliu.hermoodbarometer.core.model.data.UserData
import com.lianglliu.hermoodbarometer.repository.UserDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    userDataRepository: UserDataRepository,
    private val addEmotionRecordUseCase: AddEmotionRecordUseCase
) : ViewModel() {

    private val _showQuickRecordDialog = MutableStateFlow(false)
    val showQuickRecordDialog: StateFlow<Boolean> = _showQuickRecordDialog.asStateFlow()

    private val _quickRecordSaveState = MutableStateFlow<QuickRecordSaveState>(QuickRecordSaveState.Idle)
    val quickRecordSaveState: StateFlow<QuickRecordSaveState> = _quickRecordSaveState.asStateFlow()

    val uiState: StateFlow<MainActivityUiState> = userDataRepository.userData
        .map<UserData, MainActivityUiState>(MainActivityUiState::Success)
        .stateIn(
            scope = viewModelScope,
            initialValue = Loading,
            started = SharingStarted.WhileSubscribed(5_000),
        )

    fun showQuickRecordDialog() {
        _showQuickRecordDialog.value = true
    }

    fun hideQuickRecordDialog() {
        _showQuickRecordDialog.value = false
        _quickRecordSaveState.value = QuickRecordSaveState.Idle
    }

    fun saveQuickRecord(
        emotionId: Long,
        emotionName: String,
        emotionEmoji: String,
        weather: String?,
        activities: List<String>,
        note: String,
        timestamp: LocalDateTime
    ) {
        viewModelScope.launch {
            _quickRecordSaveState.value = QuickRecordSaveState.Saving

            val record = EmotionRecord(
                emotionId = emotionId,
                emotionName = emotionName,
                emotionEmoji = emotionEmoji,
                intensity = 3, // Default intensity for quick records
                note = note,
                timestamp = timestamp,
                weather = weather,
                activities = activities
            )

            addEmotionRecordUseCase(record)
                .onSuccess {
                    _quickRecordSaveState.value = QuickRecordSaveState.Success
                    hideQuickRecordDialog()
                }
                .onFailure { exception ->
                    _quickRecordSaveState.value = QuickRecordSaveState.Error(exception.message ?: "Unknown error")
                }
        }
    }
}

sealed interface QuickRecordSaveState {
    data object Idle : QuickRecordSaveState
    data object Saving : QuickRecordSaveState
    data object Success : QuickRecordSaveState
    data class Error(val message: String) : QuickRecordSaveState
}

sealed interface MainActivityUiState {

    data object Loading : MainActivityUiState

    data class Success(val userData: UserData) : MainActivityUiState {
        override val shouldUseDynamicTheming = userData.useDynamicColor

        override fun shouldUseDarkTheme(isSystemDarkTheme: Boolean) =
            when (userData.darkThemeConfig) {
                FOLLOW_SYSTEM -> isSystemDarkTheme
                LIGHT -> false
                DARK -> true
            }
    }

    fun shouldKeepSplashScreen() = this is Loading

    val shouldUseDynamicTheming: Boolean get() = false

    fun shouldUseDarkTheme(isSystemDarkTheme: Boolean) = isSystemDarkTheme
}