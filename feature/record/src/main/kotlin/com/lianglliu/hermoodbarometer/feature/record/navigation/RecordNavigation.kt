package com.lianglliu.hermoodbarometer.feature.record.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInHorizontally
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import kotlinx.serialization.Serializable
import com.lianglliu.hermoodbarometer.feature.record.RecordScreen

@Serializable
object RecordBaseRoute

@Serializable
object RecordRoute

fun NavController.navigateToRecord(navOptions: NavOptions? = null) =
    navigate(route = RecordBaseRoute, navOptions)

fun NavGraphBuilder.recordScreen(
//    nestedGraphs: NavGraphBuilder.() -> Unit,
) {
    navigation<RecordBaseRoute>(
        startDestination = RecordRoute,
        popEnterTransition = { slideInHorizontally { -it / 12 } + fadeIn(tween(300)) },
    ) {
        composable<RecordRoute> {
            RecordScreen()
        }
//        nestedGraphs()
    }
}