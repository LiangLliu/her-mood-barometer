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
     * languageCode: "zh", "zh-rTW", "ja", "ko", "en"
     */
    fun setAppLanguage(context: Context, languageCode: String) {
        val tags = when (languageCode) {
            "zh" -> "zh"
            "zh-rTW" -> "zh-TW"
            "ja" -> "ja"
            "ko" -> "ko"
            "en" -> "en"
            else -> Locale.getDefault().toLanguageTag()
        }

        val locales = LocaleListCompat.forLanguageTags(tags)
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