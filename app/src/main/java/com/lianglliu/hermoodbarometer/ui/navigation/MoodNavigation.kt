package com.lianglliu.hermoodbarometer.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.lianglliu.hermoodbarometer.ui.screen.record.RecordScreen
import com.lianglliu.hermoodbarometer.ui.screen.statistics.StatisticsScreen
import com.lianglliu.hermoodbarometer.ui.screen.settings.SettingsScreen

/**
 * 应用导航图
 * 定义应用的页面导航逻辑
 */
@Composable
fun MoodNavigation(
    navController: NavHostController,
    startDestination: String = Screen.Record.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Record.route) {
            RecordScreen()
        }
        
        composable(Screen.Statistics.route) {
            StatisticsScreen()
        }
        
        composable(Screen.Settings.route) {
            SettingsScreen()
        }
    }
}