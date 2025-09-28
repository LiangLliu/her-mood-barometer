package com.lianglliu.hermoodbarometer.navigation

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.vector.ImageVector
import kotlin.reflect.KClass
import com.lianglliu.hermoodbarometer.core.locales.R
import com.lianglliu.hermoodbarometer.core.designsystem.icon.AppIcons
import com.lianglliu.hermoodbarometer.core.designsystem.icon.filled.Settings
import com.lianglliu.hermoodbarometer.core.designsystem.icon.outlined.Edit
import com.lianglliu.hermoodbarometer.core.designsystem.icon.outlined.Info
import com.lianglliu.hermoodbarometer.feature.record.navigation.RecordBaseRoute
import com.lianglliu.hermoodbarometer.feature.record.navigation.RecordRoute
import com.lianglliu.hermoodbarometer.feature.settings.navigation.SettingsBaseRoute
import com.lianglliu.hermoodbarometer.feature.settings.navigation.SettingsRoute
import com.lianglliu.hermoodbarometer.feature.statistics.navigation.StatisticsBaseRoute
import com.lianglliu.hermoodbarometer.feature.statistics.navigation.StatisticsRoute

/**
 * Enum class representing the top-level destinations in the application.
 *
 * Each destination corresponds to a screen accessible from the main navigation bar.
 * It holds information about the destination's icons, titles, routes, and associated FAB (Floating Action Button) data.
 *
 * @property selectedIcon The [ImageVector] to display for the destination when it is selected in the navigation bar.
 * @property unselectedIcon The [ImageVector] to display for the destination when it is not selected in the navigation bar.
 * @property iconTextId The [StringRes] resource ID for the content description of the icon.
 * @property titleTextId The [StringRes] resource ID for the title of the destination.
 * @property route The [KClass] representing the composable route associated with the destination.
 * @property baseRoute The [KClass] representing the base route for the destination, for nested navigation. Defaults to [route] if not provided.
 * @property fabIcon The [ImageVector] to display on the FAB when this destination is active. Null if no FAB is needed.
 * @property fabTitle The [StringRes] resource ID for the content description or title of the FAB when this destination is active. Null if no FAB is needed.
 */
enum class TopLevelDestination(
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    @StringRes val iconTextId: Int,
    @StringRes val titleTextId: Int,
    val route: KClass<*>,
    val baseRoute: KClass<*> = route,
    val fabIcon: ImageVector? = null,
    @StringRes val fabTitle: Int? = null,
) {
    RECORD(
        selectedIcon = AppIcons.Outlined.Edit,
        unselectedIcon = AppIcons.Outlined.Edit,
        iconTextId = R.string.nav_record,
        titleTextId = R.string.nav_record,
        route = RecordRoute::class,
        baseRoute = RecordBaseRoute::class,
    ),
    STATISTICS(
        selectedIcon = AppIcons.Outlined.Info,
        unselectedIcon = AppIcons.Outlined.Info,
        iconTextId = R.string.nav_statistics,
        titleTextId = R.string.nav_statistics,
        route = StatisticsRoute::class,
        baseRoute = StatisticsBaseRoute::class,
    ),
    SETTINGS(
        selectedIcon = AppIcons.Filled.Settings,
        unselectedIcon = AppIcons.Filled.Settings,
        iconTextId = R.string.nav_settings,
        titleTextId = R.string.nav_settings,
        route = SettingsRoute::class,
        baseRoute = SettingsBaseRoute::class,
    )
}