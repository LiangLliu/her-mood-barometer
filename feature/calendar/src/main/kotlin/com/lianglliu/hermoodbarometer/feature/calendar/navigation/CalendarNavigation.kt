package com.lianglliu.hermoodbarometer.feature.calendar.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.lianglliu.hermoodbarometer.feature.calendar.CalendarScreen
import kotlinx.serialization.Serializable

@Serializable
object CalendarBaseRoute

@Serializable
object CalendarRoute

fun NavGraphBuilder.calendarScreen() {
    navigation<CalendarBaseRoute>(startDestination = CalendarRoute) {
        composable<CalendarRoute> {
            CalendarScreen()
        }
    }
}

fun NavController.navigateToCalendar() = navigate(CalendarRoute)