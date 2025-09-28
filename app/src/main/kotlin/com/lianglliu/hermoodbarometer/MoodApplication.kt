package com.lianglliu.hermoodbarometer

import android.app.Application
import com.lianglliu.hermoodbarometer.initializer.DeleteOutdatedRates
import dagger.hilt.android.HiltAndroidApp

/**
 * 心情应用类
 * 配置应用级别的设置
 */
@HiltAndroidApp
class MoodApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        DeleteOutdatedRates.initialize(context = this)
    }
}