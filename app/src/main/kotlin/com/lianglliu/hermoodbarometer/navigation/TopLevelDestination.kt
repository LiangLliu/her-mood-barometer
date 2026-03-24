package com.lianglliu.hermoodbarometer.navigation

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.vector.ImageVector
import com.lianglliu.hermoodbarometer.core.designsystem.icon.AppIcons
import com.lianglliu.hermoodbarometer.core.designsystem.icon.Emojis
import com.lianglliu.hermoodbarometer.core.designsystem.icon.filled.Add
import com.lianglliu.hermoodbarometer.core.designsystem.icon.filled.Book
import com.lianglliu.hermoodbarometer.core.designsystem.icon.filled.CalendarToday
import com.lianglliu.hermoodbarometer.core.designsystem.icon.filled.Settings
import com.lianglliu.hermoodbarometer.core.designsystem.icon.outlined.Book
import com.lianglliu.hermoodbarometer.core.designsystem.icon.outlined.CalendarToday
import com.lianglliu.hermoodbarometer.core.designsystem.icon.outlined.Info
import com.lianglliu.hermoodbarometer.core.locales.R
import com.lianglliu.hermoodbarometer.feature.calendar.navigation.CalendarBaseRoute
import com.lianglliu.hermoodbarometer.feature.calendar.navigation.CalendarRoute
import com.lianglliu.hermoodbarometer.feature.diary.navigation.DiaryBaseRoute
import com.lianglliu.hermoodbarometer.feature.diary.navigation.DiaryRoute
import com.lianglliu.hermoodbarometer.feature.settings.navigation.SettingsBaseRoute
import com.lianglliu.hermoodbarometer.feature.settings.navigation.SettingsRoute
import com.lianglliu.hermoodbarometer.feature.statistics.navigation.StatisticsBaseRoute
import com.lianglliu.hermoodbarometer.feature.statistics.navigation.StatisticsRoute
import kotlin.reflect.KClass

/**
 * Enum class representing the top-level destinations in the application.
 *
 * Each destination corresponds to a screen accessible from the main navigation bar. It holds
 * information about the destination's icons, titles, routes, and associated FAB (Floating Action
 * Button) data.
 *
 * @property selectedIcon The [ImageVector] to display for the destination when it is selected in
 *   the navigation bar.
 * @property unselectedIcon The [ImageVector] to display for the destination when it is not selected
 *   in the navigation bar.
 * @property iconTextId The [StringRes] resource ID for the content description of the icon.
 * @property titleTextId The [StringRes] resource ID for the title of the destination.
 * @property route The [KClass] representing the composable route associated with the destination.
 * @property baseRoute The [KClass] representing the base route for the destination, for nested
 *   navigation. Defaults to [route] if not provided.
 * @property fabIcon The [ImageVector] to display on the FAB when this destination is active. Null
 *   if no FAB is needed.
 * @property fabTitle The [StringRes] resource ID for the content description or title of the FAB
 *   when this destination is active. Null if no FAB is needed.
 */
enum class TopLevelDestination(
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val emojiIcon: String,
    @StringRes val iconTextId: Int,
    @StringRes val titleTextId: Int,
    val route: KClass<*>,
    val baseRoute: KClass<*> = route,
    val fabIcon: ImageVector? = null,
    @StringRes val fabTitle: Int? = null,
) {
    DIARY(
        selectedIcon = AppIcons.Filled.Book,
        unselectedIcon = AppIcons.Outlined.Book,
        emojiIcon = Emojis.NOTEBOOK,
        iconTextId = R.string.nav_diary,
        titleTextId = R.string.nav_diary,
        route = DiaryRoute::class,
        baseRoute = DiaryBaseRoute::class,
        fabIcon = AppIcons.Filled.Add,
        fabTitle = R.string.record_mood,
    ),
    STATISTICS(
        selectedIcon = AppIcons.Outlined.Info,
        unselectedIcon = AppIcons.Outlined.Info,
        emojiIcon = Emojis.STATISTICS,
        iconTextId = R.string.nav_statistics,
        titleTextId = R.string.nav_statistics,
        route = StatisticsRoute::class,
        baseRoute = StatisticsBaseRoute::class,
        fabIcon = AppIcons.Filled.Add,
        fabTitle = R.string.record_mood,
    ),
    CALENDAR(
        selectedIcon = AppIcons.Filled.CalendarToday,
        unselectedIcon = AppIcons.Outlined.CalendarToday,
        emojiIcon = Emojis.CALENDAR,
        iconTextId = R.string.nav_calendar,
        titleTextId = R.string.nav_calendar,
        route = CalendarRoute::class,
        baseRoute = CalendarBaseRoute::class,
        fabIcon = AppIcons.Filled.Add,
        fabTitle = R.string.record_mood,
    ),
    SETTINGS(
        selectedIcon = AppIcons.Filled.Settings,
        unselectedIcon = AppIcons.Filled.Settings,
        emojiIcon = Emojis.GEAR,
        iconTextId = R.string.nav_settings,
        titleTextId = R.string.nav_settings,
        route = SettingsRoute::class,
        baseRoute = SettingsBaseRoute::class,
    ),
}
