package com.lianglliu.hermoodbarometer.ui.navigation

/**
 * 应用页面路由定义
 * 定义应用中所有页面的导航路由
 */
sealed class Screen(val route: String) {
    object Record : Screen("record")
    object Statistics : Screen("statistics") 
    object Settings : Screen("settings")
    object CustomEmotion : Screen("custom_emotion")
}

/**
 * 底部导航项定义
 */
data class BottomNavItem(
    val screen: Screen,
    val titleResId: String,
    val iconName: String
)

/**
 * 获取底部导航项列表
 */
fun getBottomNavItems(): List<BottomNavItem> {
    return listOf(
        BottomNavItem(
            screen = Screen.Record,
            titleResId = "nav_record",
            iconName = "edit"
        ),
        BottomNavItem(
            screen = Screen.Statistics,
            titleResId = "nav_statistics", 
            iconName = "analytics"
        ),
        BottomNavItem(
            screen = Screen.Settings,
            titleResId = "nav_settings",
            iconName = "settings"
        )
    )
}