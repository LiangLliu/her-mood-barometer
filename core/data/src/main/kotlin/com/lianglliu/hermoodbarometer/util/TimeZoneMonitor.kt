package com.lianglliu.hermoodbarometer.util

import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.TimeZone

interface TimeZoneMonitor {

    val currentTimeZone: Flow<TimeZone>
}