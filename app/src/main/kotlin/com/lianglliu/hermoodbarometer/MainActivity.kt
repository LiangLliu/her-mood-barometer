package com.lianglliu.hermoodbarometer

import android.os.Bundle
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.tracing.trace
import com.lianglliu.hermoodbarometer.MainActivityUiState.Loading
import com.lianglliu.hermoodbarometer.core.analytics.AnalyticsHelper
import com.lianglliu.hermoodbarometer.core.analytics.LocalAnalyticsHelper
import com.lianglliu.hermoodbarometer.core.designsystem.theme.HerMoodBarometerTheme
import com.lianglliu.hermoodbarometer.core.ui.LocalTimeZone
import com.lianglliu.hermoodbarometer.ui.MoodApp
import com.lianglliu.hermoodbarometer.ui.rememberAppState
import com.lianglliu.hermoodbarometer.util.InAppUpdateManager
import com.lianglliu.hermoodbarometer.util.TimeZoneMonitor
import com.lianglliu.hermoodbarometer.utils.isSystemInDarkTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.getValue

/**
 * 现代化主Activity
 * 集成了Edge-to-Edge设计和性能优化
 */
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var analyticsHelper: AnalyticsHelper

    @Inject
    lateinit var timeZoneMonitor: TimeZoneMonitor

    @Inject
    lateinit var inAppUpdateManager: InAppUpdateManager

    private val viewModel: MainActivityViewModel by viewModels()

    private var isPreloadComplete: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        var themeSettings by mutableStateOf(
            ThemeSettings(
                darkTheme = resources.configuration.isSystemInDarkTheme,
                dynamicTheme = Loading.shouldUseDynamicTheming,
            ),
        )

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                combine(
                    isSystemInDarkTheme(),
                    viewModel.uiState,
                ) { systemDark, uiState ->
                    ThemeSettings(
                        darkTheme = uiState.shouldUseDarkTheme(systemDark),
                        dynamicTheme = uiState.shouldUseDynamicTheming,
                    )
                }
                    .onEach { themeSettings = it }
                    .map { it.darkTheme }
                    .distinctUntilChanged()
                    .collect { darkTheme ->
                        trace("csEdgeToEdge") {
                            enableEdgeToEdge(
                                statusBarStyle = SystemBarStyle.auto(
                                    lightScrim = Color.Transparent.toArgb(),
                                    darkScrim = Color.Transparent.toArgb(),
                                ) { darkTheme },
                                navigationBarStyle = SystemBarStyle.auto(
                                    lightScrim = Color.Transparent.toArgb(),
                                    darkScrim = Color.Transparent.toArgb(),
                                ) { darkTheme },
                            )
                        }
                    }
            }
        }

        splashScreen.setKeepOnScreenCondition { viewModel.uiState.value.shouldKeepSplashScreen() }

        setContent {
            val appState = rememberAppState(
                timeZoneMonitor = timeZoneMonitor,
                inAppUpdateManager = inAppUpdateManager,
            )

            val currentTimeZone by appState.currentTimeZone.collectAsStateWithLifecycle()

            CompositionLocalProvider(
                LocalAnalyticsHelper provides analyticsHelper,
                LocalTimeZone provides currentTimeZone,
            ) {
                HerMoodBarometerTheme(
                    darkTheme = themeSettings.darkTheme,
                    dynamicTheme = themeSettings.dynamicTheme,
                ) {
                    MoodApp(appState)
                }
            }
        }
    }
}

data class ThemeSettings(
    val darkTheme: Boolean,
    val dynamicTheme: Boolean,
)