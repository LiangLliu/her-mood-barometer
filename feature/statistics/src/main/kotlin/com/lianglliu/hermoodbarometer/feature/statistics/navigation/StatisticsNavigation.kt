package com.lianglliu.hermoodbarometer.feature.statistics.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInHorizontally
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.lianglliu.hermoodbarometer.feature.statistics.StatisticsScreen
import kotlinx.serialization.Serializable

@Serializable
object StatisticsBaseRoute

@Serializable
object StatisticsRoute

fun NavController.navigateToStatistics(navOptions: NavOptions? = null) =
    navigate(route = StatisticsBaseRoute, navOptions)

fun NavGraphBuilder.statisticsScreen(
//    nestedGraphs: NavGraphBuilder.() -> Unit,
) {
    navigation<StatisticsBaseRoute>(
        startDestination = StatisticsRoute,
        popEnterTransition = { slideInHorizontally { -it / 12 } + fadeIn(tween(300)) },
    ) {
        composable<StatisticsRoute> {
            StatisticsScreen()
        }
//        nestedGraphs()
    }
}