package com.lianglliu.hermoodbarometer.startup

import android.content.Context
import androidx.startup.Initializer
import com.lianglliu.hermoodbarometer.data.preferences.PreferencesManager
import com.lianglliu.hermoodbarometer.ui.LocaleManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

/**
 * 在进程启动最早期应用已保存语言，确保冷启动即为用户选择的语言。
 * 使用 DataStore 是轻量的；读取一次并同步设置 per-app locales。
 */
class LocaleInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        try {
            val appContext = context.applicationContext
            val preferencesManager = PreferencesManager(appContext)
            val language = runBlocking { preferencesManager.language.first() }
            LocaleManager.setAppLanguage(appContext, language)
        } catch (_: Exception) {
            // 忽略，保持系统语言
        }
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()
}


