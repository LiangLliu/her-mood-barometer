package com.lianglliu.hermoodbarometer.ui

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalContext
import java.util.Locale

/**
 * 语言管理器
 * 负责处理应用的语言切换和本地化
 */
object LocaleManager {
    
    /**
     * 应用语言设置到Context
     */
    fun applyLocale(context: Context, languageCode: String): Context {
        val locale = when (languageCode) {
            "zh" -> Locale.SIMPLIFIED_CHINESE
            "zh-rTW" -> Locale.TRADITIONAL_CHINESE
            "ja" -> Locale.JAPANESE
            "ko" -> Locale.KOREAN
            "en" -> Locale.ENGLISH
            else -> Locale.getDefault()
        }
        
        return updateResources(context, locale)
    }
    
    /**
     * 更新资源以应用新的语言设置
     */
    private fun updateResources(context: Context, locale: Locale): Context {
        Locale.setDefault(locale)
        
        val configuration = Configuration(context.resources.configuration)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            configuration.setLocale(locale)
            return context.createConfigurationContext(configuration)
        } else {
            @Suppress("DEPRECATION")
            configuration.locale = locale
            @Suppress("DEPRECATION")
            context.resources.updateConfiguration(configuration, context.resources.displayMetrics)
            return context
        }
    }
}

/**
 * Composable函数，用于在Compose中应用语言设置
 */
@Composable
fun ApplyLocale(languageCode: String) {
    val context = LocalContext.current
    
    DisposableEffect(languageCode) {
        val newContext = LocaleManager.applyLocale(context, languageCode)
        onDispose { }
    }
} 