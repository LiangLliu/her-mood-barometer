package com.lianglliu.hermoodbarometer.navigation

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.lianglliu.hermoodbarometer.feature.record.navigation.RecordBaseRoute
import com.lianglliu.hermoodbarometer.feature.record.navigation.recordScreen
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

    NavHost(
        navController = navController,
        startDestination = RecordBaseRoute,
        modifier = modifier,
        enterTransition = { slideInVertically(spring(Spring.DampingRatioLowBouncy)) { it / 24 } + fadeIn() },
        exitTransition = { fadeOut(snap()) },
        popEnterTransition = { slideInVertically(spring(Spring.DampingRatioLowBouncy)) { it / 24 } + fadeIn() },
        popExitTransition = { fadeOut(snap()) },
    ) {
        recordScreen()
        statisticsScreen()
        settingsScreen(
            onLicensesClick = navController::navigateToLicenses,
            nestedGraphs = { licensesScreen(navController::navigateUp) },
        )
    }
}