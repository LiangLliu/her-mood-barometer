package com.lianglliu.hermoodbarometer.core.notifications

import com.lianglliu.hermoodbarometer.core.model.data.Subscription

/**
 * Interface for creating notifications in the app
 */
interface Notifier {

    fun postSubscriptionNotification(subscription: Subscription)
}