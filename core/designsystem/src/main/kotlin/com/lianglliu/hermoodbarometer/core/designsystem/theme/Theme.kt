package com.lianglliu.hermoodbarometer.core.designsystem.theme

import android.app.Activity
import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialExpressiveTheme
import androidx.compose.material3.MotionScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import com.lianglliu.hermoodbarometer.core.model.data.ColorSchemeConfig

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun HerMoodBarometerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    colorSchemeConfig: ColorSchemeConfig = ColorSchemeConfig.WARM,
    content: @Composable () -> Unit,
) {
    val context = LocalContext.current

    val materialColorScheme: ColorScheme =
        remember(colorSchemeConfig, darkTheme) {
            when (colorSchemeConfig) {
                ColorSchemeConfig.WARM ->
                    if (darkTheme) WarmDarkColorScheme else WarmLightColorScheme
                ColorSchemeConfig.OCEAN ->
                    if (darkTheme) OceanDarkColorScheme else OceanLightColorScheme
                ColorSchemeConfig.PETAL ->
                    if (darkTheme) PetalDarkColorScheme else PetalLightColorScheme
                ColorSchemeConfig.DYNAMIC ->
                    if (supportsDynamicTheming()) {
                        if (darkTheme) dynamicDarkColorScheme(context)
                        else dynamicLightColorScheme(context)
                    } else {
                        if (darkTheme) WarmDarkColorScheme else WarmLightColorScheme
                    }
            }
        }

    val extendedColors =
        remember(colorSchemeConfig, darkTheme) {
            resolveExtendedColors(colorSchemeConfig, darkTheme)
        }

    val view = LocalView.current
    if (!view.isInEditMode) {
        LaunchedEffect(Unit) {
            val window = (view.context as Activity).window
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                window.isNavigationBarContrastEnforced = false
            }
        }
    }

    CompositionLocalProvider(LocalExtendedColors provides extendedColors) {
        MaterialExpressiveTheme(
            colorScheme = materialColorScheme,
            motionScheme = MotionScheme.expressive(),
            typography = AppTypography,
            content = content,
        )
    }
}

private fun resolveExtendedColors(
    colorSchemeConfig: ColorSchemeConfig,
    darkTheme: Boolean,
): ExtendedColorScheme =
    when (colorSchemeConfig) {
        ColorSchemeConfig.WARM -> if (darkTheme) WarmDarkExtendedColors else WarmLightExtendedColors
        ColorSchemeConfig.OCEAN ->
            if (darkTheme) OceanDarkExtendedColors else OceanLightExtendedColors
        ColorSchemeConfig.PETAL ->
            if (darkTheme) PetalDarkExtendedColors else PetalLightExtendedColors
        ColorSchemeConfig.DYNAMIC ->
            if (darkTheme) WarmDarkExtendedColors else WarmLightExtendedColors
    }

@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.S)
fun supportsDynamicTheming() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
