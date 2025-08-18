package com.lianglliu.hermoodbarometer.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.lianglliu.hermoodbarometer.ui.screen.record.RecordScreen
import com.lianglliu.hermoodbarometer.ui.screen.settings.CustomEmotionScreen
import com.lianglliu.hermoodbarometer.ui.screen.settings.SettingsScreen
import com.lianglliu.hermoodbarometer.ui.screen.settings.SettingsViewModel
import com.lianglliu.hermoodbarometer.ui.screen.statistics.StatisticsScreen

/**
 * 应用导航图
 * 定义应用的页面导航逻辑
 */
@Composable
fun MoodNavigation(
    navController: NavHostController,
    startDestination: String = Screen.Record.route,
    modifier: Modifier = Modifier,
    settingsViewModel: SettingsViewModel
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(Screen.Record.route) {
            RecordScreen()
        }
        
        composable(Screen.Statistics.route) {
            StatisticsScreen()
        }
        
        composable(Screen.Settings.route) {
            SettingsScreen(
                onNavigateToCustomEmotion = {
                    navController.navigate(Screen.CustomEmotion.route)
                },
                viewModel = settingsViewModel
            )
        }
        
        composable(Screen.CustomEmotion.route) {
            CustomEmotionScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}