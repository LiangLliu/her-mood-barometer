package com.lianglliu.hermoodbarometer.ui

import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalContext
import java.util.Locale

/**
 * 语言管理器
 * 负责处理应用的语言切换（Android 13+ 使用平台API，低版本使用 AppCompatDelegate）
 */
object LocaleManager {

    /**
     * 设置应用语言（per-app locales）
     * languageCode: "system", "zh", "zh-TW", "ja", "ko", "en"
     */
    fun setAppLanguage(context: Context, languageCode: String) {
        val normalized = when (languageCode.lowercase(Locale.ROOT)) {
            "system", "default" -> null
            "zh", "zh-cn", "zh_hans" -> "zh"
            "zh-tw", "zh_rtw", "zh-hant", "zh-hk", "zh-mo" -> "zh-TW"
            "en", "en-us", "en-gb" -> "en"
            "ja" -> "ja"
            "ko" -> "ko"
            else -> languageCode
        }

        val locales = if (normalized == null) {
            // 跟随系统
            LocaleListCompat.getEmptyLocaleList()
        } else {
            LocaleListCompat.forLanguageTags(normalized)
        }
        AppCompatDelegate.setApplicationLocales(locales)
    }
}

/**
 * 在 Compose 中观察语言变更并应用 per-app locales
 */
@Composable
fun ApplyLocale(languageCode: String) {
    val context = LocalContext.current
    DisposableEffect(languageCode) {
        LocaleManager.setAppLanguage(context, languageCode)
        onDispose { }
    }
}