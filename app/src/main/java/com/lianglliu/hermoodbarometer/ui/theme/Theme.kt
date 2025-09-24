package com.lianglliu.hermoodbarometer.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalContext

/**
 * 暗色主题配色方案
 * 基于Material Design 3规范设计
 */
private val DarkColorScheme = darkColorScheme(
    primary = Primary80,
    onPrimary = Primary20,
    primaryContainer = Primary30,
    onPrimaryContainer = Primary90,
    secondary = Secondary80,
    onSecondary = Secondary20,
    secondaryContainer = Secondary30,
    onSecondaryContainer = Secondary90,
    tertiary = Tertiary80,
    onTertiary = Tertiary20,
    tertiaryContainer = Tertiary30,
    onTertiaryContainer = Tertiary90,
    error = Error80,
    onError = Error20,
    errorContainer = Error30,
    onErrorContainer = Error90,
    background = Neutral10,
    onBackground = Neutral90,
    surface = Neutral10,
    onSurface = Neutral90,
    surfaceVariant = NeutralVariant30,
    onSurfaceVariant = NeutralVariant80,
    outline = NeutralVariant60,
    outlineVariant = NeutralVariant30,
    scrim = Neutral0,
    inverseSurface = Neutral90,
    inverseOnSurface = Neutral20,
    inversePrimary = Primary40,
    // Material Design 3 扩展颜色
    surfaceDim = Neutral6,
    surfaceBright = Neutral24,
    surfaceContainerLowest = Neutral4,
    surfaceContainerLow = Neutral10,
    surfaceContainer = Neutral12,
    surfaceContainerHigh = Neutral17,
    surfaceContainerHighest = Neutral22,
)

/**
 * 浅色主题配色方案
 * 基于Material Design 3规范设计
 */
private val LightColorScheme = lightColorScheme(
    primary = Primary40,
    onPrimary = Primary100,
    primaryContainer = Primary90,
    onPrimaryContainer = Primary10,
    secondary = Secondary40,
    onSecondary = Secondary100,
    secondaryContainer = Secondary90,
    onSecondaryContainer = Secondary10,
    tertiary = Tertiary40,
    onTertiary = Tertiary100,
    tertiaryContainer = Tertiary90,
    onTertiaryContainer = Tertiary10,
    error = Error40,
    onError = Error100,
    errorContainer = Error90,
    onErrorContainer = Error10,
    background = Neutral99,
    onBackground = Neutral10,
    surface = Neutral99,
    onSurface = Neutral10,
    surfaceVariant = NeutralVariant90,
    onSurfaceVariant = NeutralVariant30,
    outline = NeutralVariant50,
    outlineVariant = NeutralVariant80,
    scrim = Neutral0,
    inverseSurface = Neutral20,
    inverseOnSurface = Neutral95,
    inversePrimary = Primary80,
    // Material Design 3 扩展颜色
    surfaceDim = Neutral87,
    surfaceBright = Neutral98,
    surfaceContainerLowest = Neutral100,
    surfaceContainerLow = Neutral96,
    surfaceContainer = Neutral94,
    surfaceContainerHigh = Neutral92,
    surfaceContainerHighest = Neutral90,
)

/**
 * 应用主题组件
 * 支持动态色彩、深色模式，并提供自定义设计tokens
 * 符合 Material Design 3 规范
 *
 * @param darkTheme 是否使用深色主题
 * @param dynamicColor 是否启用Android 12+的动态色彩
 * @param content 主题内容
 */
@Composable
fun HerMoodBarometerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    // 提供自定义设计tokens
    CompositionLocalProvider(
        LocalAppDimens provides GlobalDesignTokens.dimens,
        LocalAppShapes provides GlobalDesignTokens.shapes,
        LocalAppSpacing provides GlobalDesignTokens.spacing,
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = AppTypography,
            shapes = GlobalDesignTokens.shapes.materialShapes,
            content = content
        )
    }
}

/**
 * 预览主题，用于Compose预览
 */
@Composable
fun HerMoodBarometerPreviewTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    HerMoodBarometerTheme(
        darkTheme = darkTheme,
        dynamicColor = false,
        content = content
    )
}