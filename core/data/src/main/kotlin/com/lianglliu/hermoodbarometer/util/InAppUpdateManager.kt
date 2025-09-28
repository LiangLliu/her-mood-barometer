package com.lianglliu.hermoodbarometer.util

import kotlinx.coroutines.flow.Flow

interface InAppUpdateManager {

    val inAppUpdateResult: Flow<InAppUpdateResult>
}