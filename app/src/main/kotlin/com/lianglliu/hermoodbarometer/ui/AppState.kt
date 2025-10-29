package com.lianglliu.hermoodbarometer.ui

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import androidx.tracing.trace
import com.lianglliu.hermoodbarometer.feature.record.navigation.RecordRoute
import com.lianglliu.hermoodbarometer.feature.record.navigation.navigateToRecord
import com.lianglliu.hermoodbarometer.feature.settings.navigation.SettingsRoute
import com.lianglliu.hermoodbarometer.feature.settings.navigation.navigateToSettings
import com.lianglliu.hermoodbarometer.feature.statistics.navigation.StatisticsRoute
import com.lianglliu.hermoodbarometer.feature.statistics.navigation.navigateToStatistics
import com.lianglliu.hermoodbarometer.navigation.TopLevelDestination
import com.lianglliu.hermoodbarometer.navigation.TopLevelDestination.RECORD
import com.lianglliu.hermoodbarometer.navigation.TopLevelDestination.SETTINGS
import com.lianglliu.hermoodbarometer.navigation.TopLevelDestination.STATISTICS
import com.lianglliu.hermoodbarometer.util.InAppUpdateManager
import com.lianglliu.hermoodbarometer.util.InAppUpdateResult
import com.lianglliu.hermoodbarometer.util.TimeZoneMonitor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.datetime.TimeZone

@Composable
fun rememberAppState(
    timeZoneMonitor: TimeZoneMonitor,
    inAppUpdateManager: InAppUpdateManager,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    navController: NavHostController = rememberNavController(),
): AppState {

    return remember(
        timeZoneMonitor,
        coroutineScope,
        navController,
    ) {
        AppState(
            timeZoneMonitor = timeZoneMonitor,
            inAppUpdateManager = inAppUpdateManager,
            coroutineScope = coroutineScope,
            navController = navController,
        )
    }
}

@Stable
class AppState(
    timeZoneMonitor: TimeZoneMonitor,
    inAppUpdateManager: InAppUpdateManager,
    coroutineScope: CoroutineScope,
    val navController: NavHostController,
) {
    val currentDestination: NavDestination?
        @Composable get() = navController.currentBackStackEntryAsState().value?.destination

    val currentTopLevelDestination: TopLevelDestination?
        @Composable get() {
            with(currentDestination) {
                if (this?.hasRoute<RecordRoute>() == true
                ) return RECORD
                if (this?.hasRoute<StatisticsRoute>() == true
                ) return STATISTICS

                if (this?.hasRoute<SettingsRoute>() == true) return SETTINGS
            }
            return null
        }

    val topLevelDestinations: List<TopLevelDestination> = TopLevelDestination.entries

    val currentTimeZone = timeZoneMonitor.currentTimeZone
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = TimeZone.currentSystemDefault(),
        )

    val inAppUpdateResult = inAppUpdateManager.inAppUpdateResult
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = InAppUpdateResult.NotAvailable,
        )

    fun navigateToTopLevelDestination(topLevelDestination: TopLevelDestination) {
        val startTime = System.currentTimeMillis()
        Log.d("Navigation", "Navigate to ${topLevelDestination.name}")

        trace("Navigation: ${topLevelDestination.name}") {
            val topLevelNavOptions = navOptions {
                popUpTo(navController.graph.findStartDestination().id) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }

            when (topLevelDestination) {
                RECORD -> {
                    navController.navigateToRecord(navOptions = topLevelNavOptions)
                    Log.d("Navigation", "→ RECORD (${System.currentTimeMillis() - startTime}ms)")
                }
                STATISTICS -> {
                    navController.navigateToStatistics(topLevelNavOptions)
                    Log.d("Navigation", "→ STATISTICS (${System.currentTimeMillis() - startTime}ms)")
                }
                SETTINGS -> {
                    navController.navigateToSettings(topLevelNavOptions)
                    Log.d("Navigation", "→ SETTINGS (${System.currentTimeMillis() - startTime}ms)")
                }
            }
        }
    }
}