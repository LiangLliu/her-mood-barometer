package com.lianglliu.hermoodbarometer.navigation

import android.util.Log
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import com.lianglliu.hermoodbarometer.feature.calendar.navigation.calendarScreen
import com.lianglliu.hermoodbarometer.feature.diary.navigation.DiaryBaseRoute
import com.lianglliu.hermoodbarometer.feature.diary.navigation.diaryScreen
import com.lianglliu.hermoodbarometer.feature.settings.navigation.licensesScreen
import com.lianglliu.hermoodbarometer.feature.settings.navigation.navigateToLicenses
import com.lianglliu.hermoodbarometer.feature.settings.navigation.settingsScreen
import com.lianglliu.hermoodbarometer.feature.statistics.navigation.statisticsScreen
import com.lianglliu.hermoodbarometer.ui.AppState

@Composable
fun MoodNavHost(
    appState: AppState,
    onShowSnackbar: suspend (String, String?) -> Boolean,
    modifier: Modifier = Modifier,
) {
    val navController = appState.navController

    // 监控导航变化
    DisposableEffect(navController) {
        val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
            Log.d("NavTransition", "Arrived at: ${destination.route?.substringAfterLast('.')?.substringBefore("Route") ?: destination.route}")
        }

        navController.addOnDestinationChangedListener(listener)

        onDispose {
            navController.removeOnDestinationChangedListener(listener)
        }
    }

    NavHost(
        navController = navController,
        startDestination = DiaryBaseRoute,
        modifier = modifier,
        enterTransition = { fadeIn(animationSpec = tween(300)) },
        exitTransition = { fadeOut(animationSpec = tween(300)) },
        popEnterTransition = { fadeIn(animationSpec = tween(300)) },
        popExitTransition = { fadeOut(animationSpec = tween(300)) },
    ) {
        diaryScreen()
        calendarScreen()
        statisticsScreen()
        settingsScreen(
            onLicensesClick = navController::navigateToLicenses,
            nestedGraphs = { licensesScreen(navController::navigateUp) },
        )
    }
}