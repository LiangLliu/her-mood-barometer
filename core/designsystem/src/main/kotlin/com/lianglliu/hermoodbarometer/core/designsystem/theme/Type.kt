package com.lianglliu.hermoodbarometer.core.designsystem.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.LineHeightStyle.Alignment
import androidx.compose.ui.text.style.LineHeightStyle.Trim
import androidx.compose.ui.unit.sp
import com.lianglliu.hermoodbarometer.core.designsystem.R

private val googleFontProvider =
    GoogleFont.Provider(
        providerAuthority = "com.google.android.gms.fonts",
        providerPackage = "com.google.android.gms",
        certificates = R.array.com_google_android_gms_fonts_certs,
    )

private val NotoSerifSCFont = GoogleFont("Noto Serif SC")

val NotoSerifSCFamily =
    FontFamily(
        Font(
            googleFont = NotoSerifSCFont,
            fontProvider = googleFontProvider,
            weight = FontWeight.Normal,
        ),
        Font(
            googleFont = NotoSerifSCFont,
            fontProvider = googleFontProvider,
            weight = FontWeight.Medium,
        ),
        Font(
            googleFont = NotoSerifSCFont,
            fontProvider = googleFontProvider,
            weight = FontWeight.SemiBold,
        ),
        Font(
            googleFont = NotoSerifSCFont,
            fontProvider = googleFontProvider,
            weight = FontWeight.Bold,
        ),
    )

val BodyFontFamily = FontFamily.Default

internal val AppTypography =
    Typography(
        displayLarge =
            TextStyle(
                fontFamily = NotoSerifSCFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 57.sp,
                lineHeight = 64.sp,
                letterSpacing = (-0.25).sp,
            ),
        displayMedium =
            TextStyle(
                fontFamily = NotoSerifSCFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 45.sp,
                lineHeight = 52.sp,
                letterSpacing = 0.sp,
            ),
        displaySmall =
            TextStyle(
                fontFamily = NotoSerifSCFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 36.sp,
                lineHeight = 44.sp,
                letterSpacing = 0.sp,
            ),
        headlineLarge =
            TextStyle(
                fontFamily = NotoSerifSCFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 32.sp,
                lineHeight = 40.sp,
                letterSpacing = 0.sp,
            ),
        headlineMedium =
            TextStyle(
                fontFamily = NotoSerifSCFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 28.sp,
                lineHeight = 36.sp,
                letterSpacing = 0.sp,
            ),
        headlineSmall =
            TextStyle(
                fontFamily = NotoSerifSCFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 24.sp,
                lineHeight = 32.sp,
                letterSpacing = 0.sp,
            ),
        titleLarge =
            TextStyle(
                fontFamily = NotoSerifSCFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 22.sp,
                lineHeight = 28.sp,
                letterSpacing = 0.sp,
                lineHeightStyle = LineHeightStyle(alignment = Alignment.Center, trim = Trim.None),
            ),
        titleMedium =
            TextStyle(
                fontFamily = NotoSerifSCFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                lineHeight = 24.sp,
                letterSpacing = 0.15.sp,
                lineHeightStyle =
                    LineHeightStyle(alignment = Alignment.Bottom, trim = Trim.LastLineBottom),
            ),
        titleSmall =
            TextStyle(
                fontFamily = NotoSerifSCFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                letterSpacing = 0.1.sp,
            ),
        bodyLarge =
            TextStyle(
                fontFamily = BodyFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                lineHeight = 24.sp,
                letterSpacing = 0.5.sp,
                lineHeightStyle = LineHeightStyle(alignment = Alignment.Center, trim = Trim.None),
            ),
        bodyMedium =
            TextStyle(
                fontFamily = BodyFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                letterSpacing = 0.25.sp,
            ),
        bodySmall =
            TextStyle(
                fontFamily = BodyFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp,
                lineHeight = 16.sp,
                letterSpacing = 0.4.sp,
            ),
        labelLarge =
            TextStyle(
                fontFamily = BodyFontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                letterSpacing = 0.1.sp,
                lineHeightStyle =
                    LineHeightStyle(alignment = Alignment.Center, trim = Trim.LastLineBottom),
            ),
        labelMedium =
            TextStyle(
                fontFamily = BodyFontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 12.sp,
                lineHeight = 16.sp,
                letterSpacing = 0.5.sp,
                lineHeightStyle =
                    LineHeightStyle(alignment = Alignment.Top, trim = Trim.FirstLineTop),
            ),
        labelSmall =
            TextStyle(
                fontFamily = BodyFontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 11.sp,
                lineHeight = 16.sp,
                letterSpacing = 0.5.sp,
                lineHeightStyle =
                    LineHeightStyle(alignment = Alignment.Center, trim = Trim.LastLineBottom),
            ),
    )
