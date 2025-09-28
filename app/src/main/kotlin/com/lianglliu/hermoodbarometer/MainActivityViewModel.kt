package com.lianglliu.hermoodbarometer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lianglliu.hermoodbarometer.MainActivityUiState.Loading
import com.lianglliu.hermoodbarometer.core.model.data.DarkThemeConfig.DARK
import com.lianglliu.hermoodbarometer.core.model.data.DarkThemeConfig.FOLLOW_SYSTEM
import com.lianglliu.hermoodbarometer.core.model.data.DarkThemeConfig.LIGHT
import com.lianglliu.hermoodbarometer.core.model.data.UserData
import com.lianglliu.hermoodbarometer.repository.UserDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    userDataRepository: UserDataRepository,
) : ViewModel() {

    val uiState: StateFlow<MainActivityUiState> = userDataRepository.userData
        .map<UserData, MainActivityUiState>(MainActivityUiState::Success)
        .stateIn(
            scope = viewModelScope,
            initialValue = Loading,
            started = SharingStarted.WhileSubscribed(5_000),
        )
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