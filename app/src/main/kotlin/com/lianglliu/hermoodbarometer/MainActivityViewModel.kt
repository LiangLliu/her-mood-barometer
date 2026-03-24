package com.lianglliu.hermoodbarometer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lianglliu.hermoodbarometer.MainActivityUiState.Loading
import com.lianglliu.hermoodbarometer.core.domain.AddEmotionRecordUseCase
import com.lianglliu.hermoodbarometer.core.model.data.Activity
import com.lianglliu.hermoodbarometer.core.model.data.ColorSchemeConfig
import com.lianglliu.hermoodbarometer.core.model.data.DarkThemeConfig.DARK
import com.lianglliu.hermoodbarometer.core.model.data.DarkThemeConfig.FOLLOW_SYSTEM
import com.lianglliu.hermoodbarometer.core.model.data.DarkThemeConfig.LIGHT
import com.lianglliu.hermoodbarometer.core.model.data.EmotionIntensity
import com.lianglliu.hermoodbarometer.core.model.data.EmotionRecord
import com.lianglliu.hermoodbarometer.core.model.data.UserData
import com.lianglliu.hermoodbarometer.core.model.data.Weather
import com.lianglliu.hermoodbarometer.repository.UserDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDateTime
import java.time.ZoneId
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class MainActivityViewModel
@Inject
constructor(
    userDataRepository: UserDataRepository,
    private val addEmotionRecordUseCase: AddEmotionRecordUseCase,
    private val initializeEmotionsUseCase:
        com.lianglliu.hermoodbarometer.core.domain.InitializeEmotionsUseCase,
) : ViewModel() {

    init {
        viewModelScope.launch {
            initializeEmotionsUseCase()
                .onSuccess { android.util.Log.d(TAG, "Predefined emotions initialized") }
                .onFailure { android.util.Log.e(TAG, "Failed to initialize emotions", it) }
        }
    }

    private val _showQuickRecordDialog = MutableStateFlow(false)
    val showQuickRecordDialog: StateFlow<Boolean> = _showQuickRecordDialog.asStateFlow()

    private val _quickRecordSaveState =
        MutableStateFlow<QuickRecordSaveState>(QuickRecordSaveState.Idle)
    val quickRecordSaveState: StateFlow<QuickRecordSaveState> = _quickRecordSaveState.asStateFlow()

    val uiState: StateFlow<MainActivityUiState> =
        userDataRepository.userData
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
        timestamp: LocalDateTime,
    ) {
        viewModelScope.launch {
            _quickRecordSaveState.value = QuickRecordSaveState.Saving

            val record =
                EmotionRecord(
                    emotionId = emotionId,
                    emotionEmoji = emotionEmoji,
                    intensity =
                        EmotionIntensity.fromLevel(3), // Default intensity for quick records
                    note = note,
                    timestamp = timestamp.atZone(ZoneId.systemDefault()).toInstant(),
                    weather = weather?.let { Weather.fromString(it) },
                    activities =
                        activities.mapNotNull { activityName ->
                            try {
                                Activity.valueOf(activityName)
                            } catch (e: IllegalArgumentException) {
                                null
                            }
                        },
                )

            android.util.Log.d(
                TAG,
                "Saving record: emotionId=$emotionId, emoji=$emotionEmoji, weather=$weather, activities=$activities",
            )
            addEmotionRecordUseCase(record)
                .onSuccess {
                    android.util.Log.d(TAG, "Record saved successfully")
                    _quickRecordSaveState.value = QuickRecordSaveState.Success
                    hideQuickRecordDialog()
                }
                .onFailure { exception ->
                    android.util.Log.e(TAG, "Failed to save record", exception)
                    _showQuickRecordDialog.value = false
                    _quickRecordSaveState.value =
                        QuickRecordSaveState.Error(exception.message ?: "Unknown error")
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

private const val TAG = "MainActivityViewModel"

sealed interface MainActivityUiState {

    data object Loading : MainActivityUiState

    data class Success(val userData: UserData) : MainActivityUiState {
        override val colorSchemeConfig: ColorSchemeConfig = userData.colorSchemeConfig

        override fun shouldUseDarkTheme(isSystemDarkTheme: Boolean) =
            when (userData.darkThemeConfig) {
                FOLLOW_SYSTEM -> isSystemDarkTheme
                LIGHT -> false
                DARK -> true
            }
    }

    fun shouldKeepSplashScreen() = this is Loading

    val colorSchemeConfig: ColorSchemeConfig
        get() = ColorSchemeConfig.WARM

    fun shouldUseDarkTheme(isSystemDarkTheme: Boolean) = isSystemDarkTheme
}
