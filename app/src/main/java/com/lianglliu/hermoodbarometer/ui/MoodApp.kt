package com.lianglliu.hermoodbarometer.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.lianglliu.hermoodbarometer.ui.navigation.MoodNavigation
import com.lianglliu.hermoodbarometer.ui.navigation.Screen
import com.lianglliu.hermoodbarometer.ui.navigation.getBottomNavItems
import com.lianglliu.hermoodbarometer.ui.screen.settings.SettingsViewModel
import com.lianglliu.hermoodbarometer.ui.theme.HerMoodBarometerTheme
import com.lianglliu.hermoodbarometer.ui.ApplyLocale

/**
 * 主应用组件
 * 包含底部导航和页面容器
 */

@Composable
fun MoodApp() {
    val settingsViewModel: SettingsViewModel = hiltViewModel()
    val uiState by settingsViewModel.uiState.collectAsStateWithLifecycle()
    
    // 应用语言设置
    ApplyLocale(uiState.selectedLanguage)
    
    // 根据主题设置确定是否使用深色模式
    val isDarkTheme = when (uiState.selectedTheme) {
        "dark" -> true
        "light" -> false
        else -> null // 使用系统设置
    }
    
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    HerMoodBarometerTheme(
        darkTheme = isDarkTheme ?: isSystemInDarkTheme()
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            bottomBar = {
            NavigationBar {
                getBottomNavItems().forEach { item ->
                    val isSelected = currentDestination?.hierarchy?.any {
                        it.route == item.screen.route
                    } == true

                    NavigationBarItem(
                        icon = {
                            Icon(
                                imageVector = getIconForScreen(item.screen),
                                contentDescription = getDisplayName(item.titleResId)
                            )
                        },
                        label = {
                            Text(getDisplayName(item.titleResId))
                        },
                        selected = isSelected,
                        onClick = {
                            navController.navigate(item.screen.route) {
                                // Pop up to the start destination of the graph to
                                // avoid building up a large stack of destinations
                                // on the back stack as users select items
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                // Avoid multiple copies of the same destination when
                                // reselecting the same item
                                launchSingleTop = true
                                // Restore state when reselecting a previously selected item
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            MoodNavigation(
                navController = navController,
                startDestination = Screen.Record.route
            )
        }
    }
    }
}

/**
 * 获取页面对应的图标
 */
private fun getIconForScreen(screen: Screen): ImageVector {
    return when (screen) {
        Screen.Record -> Icons.Default.Edit
        Screen.Statistics -> Icons.Default.Info
        Screen.Settings -> Icons.Default.Settings
    }
}

/**
 * 获取资源显示名称（临时实现，后续使用stringResource）
 */
private fun getDisplayName(resId: String): String {
    return when (resId) {
        "nav_record" -> "记录"
        "nav_statistics" -> "统计"
        "nav_settings" -> "设置"
        else -> resId
    }
}