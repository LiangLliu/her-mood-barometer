package com.lianglliu.hermoodbarometer.core.analytics

import jakarta.inject.Inject
import jakarta.inject.Singleton
import timber.log.Timber

/**
 * An implementation of [AnalyticsHelper] just writes the events to logcat. Used in builds where no
 * analytics events should be sent to a backend.
 */
@Singleton
internal class FakeAnalyticsHelper @Inject constructor() : AnalyticsHelper {

    override fun logEvent(event: AnalyticsEvent) {
        Timber.d("Received analytics event: $event")
    }
}
