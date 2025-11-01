package com.lianglliu.hermoodbarometer.feature.diary.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.lianglliu.hermoodbarometer.feature.diary.DiaryScreen
import kotlinx.serialization.Serializable

@Serializable
object DiaryBaseRoute

@Serializable
object DiaryRoute

fun NavGraphBuilder.diaryScreen() {
    navigation<DiaryBaseRoute>(startDestination = DiaryRoute) {
        composable<DiaryRoute> {
            DiaryScreen()
        }
    }
}

fun NavController.navigateToDiary() = navigate(DiaryRoute)