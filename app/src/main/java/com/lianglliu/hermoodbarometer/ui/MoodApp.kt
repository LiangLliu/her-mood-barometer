package com.lianglliu.hermoodbarometer.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.lianglliu.hermoodbarometer.R
import com.lianglliu.hermoodbarometer.ui.navigation.MoodNavigation
import com.lianglliu.hermoodbarometer.ui.navigation.Screen
import com.lianglliu.hermoodbarometer.ui.navigation.getBottomNavItems
import com.lianglliu.hermoodbarometer.ui.screen.settings.SettingsViewModel
import com.lianglliu.hermoodbarometer.ui.theme.HerMoodBarometerTheme

/**
 * 现代化主应用组件
 * 基于Material Design 3的设计系统
 * 支持动态色彩和深色模式
 */
@Composable
fun MoodApp() {
    val settingsViewModel: SettingsViewModel = hiltViewModel()
    val uiState by settingsViewModel.uiState.collectAsStateWithLifecycle()
    
    // 仅在设置初始化完成后应用语言，避免启动初期用默认值覆盖
    if (uiState.isInitialized) {
        ApplyLocale(uiState.selectedLanguage)
    }
    
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
        darkTheme = isDarkTheme ?: isSystemInDarkTheme(),
        dynamicColor = true
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground,
            bottomBar = {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer,
                    contentColor = MaterialTheme.colorScheme.onSurface
                ) {
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
                                Text(
                                    text = getDisplayName(item.titleResId),
                                    style = MaterialTheme.typography.labelMedium
                                )
                            },
                            selected = isSelected,
                            onClick = {
                                navController.navigate(item.screen.route) {
                                    // Pop up to the start destination of the graph to
                                    // avoid building up a large stack of destinations
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
            MoodNavigation(
                navController = navController,
                startDestination = Screen.Record.route,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                settingsViewModel = settingsViewModel
            )
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
        Screen.CustomEmotion -> Icons.Default.Settings // 使用设置图标作为默认
    }
}

/**
 * 获取资源显示名称
 */
@Composable
private fun getDisplayName(resId: String): String {
    return when (resId) {
        "nav_record" -> stringResource(R.string.nav_record)
        "nav_statistics" -> stringResource(R.string.nav_statistics)
        "nav_settings" -> stringResource(R.string.nav_settings)
        else -> resId
    }
}