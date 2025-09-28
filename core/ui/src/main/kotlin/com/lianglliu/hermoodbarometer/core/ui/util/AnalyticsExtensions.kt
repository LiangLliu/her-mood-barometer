package com.lianglliu.hermoodbarometer.core.ui.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import com.lianglliu.hermoodbarometer.core.analytics.AnalyticsEvent
import com.lianglliu.hermoodbarometer.core.analytics.AnalyticsEvent.Types
import com.lianglliu.hermoodbarometer.core.analytics.AnalyticsEvent.Param
import com.lianglliu.hermoodbarometer.core.analytics.AnalyticsEvent.ParamKeys
import com.lianglliu.hermoodbarometer.core.analytics.AnalyticsHelper
import com.lianglliu.hermoodbarometer.core.analytics.LocalAnalyticsHelper


private fun AnalyticsHelper.logScreenView(screenName: String) {
    logEvent(
        event = AnalyticsEvent(
            type = Types.SCREEN_VIEW,
            extras = listOf(
                AnalyticsEvent.Param(AnalyticsEvent.ParamKeys.SCREEN_NAME, screenName),
            ),
        ),
    )
}

fun AnalyticsHelper.logNewItemAdded(itemType: String) {
    logEvent(
        event = AnalyticsEvent(
            type = Types.ADD_ITEM,
            extras = listOf(
                Param(ParamKeys.ITEM_TYPE, itemType),
            ),
        ),
    )
}

/**
 * A side-effect which records a screen view event.
 */
@Composable
fun TrackScreenViewEvent(
    screenName: String,
    analyticsHelper: AnalyticsHelper = LocalAnalyticsHelper.current,
) = DisposableEffect(Unit) {
    analyticsHelper.logScreenView(screenName)
    onDispose {}
}
