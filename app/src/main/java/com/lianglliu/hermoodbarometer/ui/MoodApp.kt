package com.lianglliu.hermoodbarometer.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.lianglliu.hermoodbarometer.ui.navigation.*

/**
 * 主应用组件
 * 包含底部导航和页面容器
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoodApp() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    
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
        MoodNavigation(
            navController = navController,
            startDestination = Screen.Record.route
        )
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