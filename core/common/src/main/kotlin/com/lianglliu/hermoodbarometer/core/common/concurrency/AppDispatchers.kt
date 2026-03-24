package com.lianglliu.hermoodbarometer.core.common.concurrency

import jakarta.inject.Qualifier
import kotlin.annotation.AnnotationRetention.RUNTIME

@Retention(RUNTIME) @Qualifier annotation class Dispatcher(val appDispatcher: AppDispatchers)

enum class AppDispatchers {
    Default,
    IO,
}
