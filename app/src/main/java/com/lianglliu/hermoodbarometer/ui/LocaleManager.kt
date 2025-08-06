package com.lianglliu.hermoodbarometer.ui

import android.content.Context
import android.content.res.Configuration
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
        configuration.setLocale(locale)
        return context.createConfigurationContext(configuration)
    }

    /**
     * 获取当前语言代码
     */
    fun getLanguageCode(context: Context): String {
        val locale =  context.resources.configuration.locales[0]

        return when (locale.language) {
            "zh" -> if (locale.country == "TW") "zh-rTW" else "zh"
            "ja" -> "ja"
            "ko" -> "ko"
            "en" -> "en"
            else -> "zh"
        }
    }
}

/**
 * Composable函数，用于在Compose中应用语言设置
 * 注意：这个函数现在主要用于检测语言变化，真正的语言切换需要在Activity级别处理
 */
@Composable
fun ApplyLocale(languageCode: String) {
    val context = LocalContext.current

    DisposableEffect(languageCode) {
        // 检查当前语言是否与设置的语言一致
        val currentLanguage = LocaleManager.getLanguageCode(context)
        if (currentLanguage != languageCode) {
            // 如果语言不匹配，这里可以触发重新创建Activity
            // 但实际的重新创建需要在ViewModel中处理
        }
        onDispose { }
    }
} 