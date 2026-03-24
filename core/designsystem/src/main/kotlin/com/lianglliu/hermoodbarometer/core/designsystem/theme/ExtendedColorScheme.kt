package com.lianglliu.hermoodbarometer.core.designsystem.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class ExtendedColorScheme(
    val accent: Color,
    val accentSoft: Color,
    val accentBg: Color,
    val sage: Color,
    val sageSoft: Color,
    val sageBg: Color,
    val amber: Color,
    val amberSoft: Color,
    val amberBg: Color,
    val rose: Color,
    val roseSoft: Color,
    val roseBg: Color,
    val lavender: Color,
    val lavenderSoft: Color,
    val lavenderBg: Color,
    val textSecondary: Color,
    val textMuted: Color,
    val textHint: Color,
    val border: Color,
    val borderLight: Color,
    val cardBackground: Color,
    val warmBackground: Color,
    val modalOverlay: Color,
)

@Immutable data class MoodColorMapping(val color: Color, val soft: Color, val background: Color)

fun ExtendedColorScheme.moodColor(emotionResourceKey: String): MoodColorMapping =
    when (emotionResourceKey) {
        "happy" -> MoodColorMapping(amber, amberSoft, amberBg)
        "calm",
        "grateful" -> MoodColorMapping(sage, sageSoft, sageBg)
        "anxious",
        "angry",
        "excited" -> MoodColorMapping(accent, accentSoft, accentBg)
        "tired",
        "confused" -> MoodColorMapping(lavender, lavenderSoft, lavenderBg)
        "sad",
        "lonely" -> MoodColorMapping(rose, roseSoft, roseBg)
        else -> MoodColorMapping(accent, accentSoft, accentBg)
    }

val LocalExtendedColors = staticCompositionLocalOf { WarmLightExtendedColors }

object ExtendedTheme {
    val colors: ExtendedColorScheme
        @Composable @ReadOnlyComposable get() = LocalExtendedColors.current
}

// ==================== Warm Sun (暖阳) ====================

val WarmLightExtendedColors =
    ExtendedColorScheme(
        accent = Color(0xFFC4735B),
        accentSoft = Color(0xFFE8B4A2),
        accentBg = Color(0xFFFFF0EB),
        sage = Color(0xFF7A9E7E),
        sageSoft = Color(0xFFC5DBC7),
        sageBg = Color(0xFFEFF5F0),
        amber = Color(0xFFC9A84C),
        amberSoft = Color(0xFFE8D9A0),
        amberBg = Color(0xFFFDF8EC),
        rose = Color(0xFFB8687B),
        roseSoft = Color(0xFFE0B4BF),
        roseBg = Color(0xFFFBF0F3),
        lavender = Color(0xFF8B7BB5),
        lavenderSoft = Color(0xFFC5BBD9),
        lavenderBg = Color(0xFFF3F0F8),
        textSecondary = Color(0xFF6B5E55),
        textMuted = Color(0xFF9A8E85),
        textHint = Color(0xFFBEB3AA),
        border = Color(0xFFD8CFC7),
        borderLight = Color(0xFFEDE6DD),
        cardBackground = Color(0xFFFFFFFF),
        warmBackground = Color(0xFFF5F0E8),
        modalOverlay = Color(0x662C2520),
    )

val WarmDarkExtendedColors =
    ExtendedColorScheme(
        accent = Color(0xFFD8896F),
        accentSoft = Color(0xFF8B5A48),
        accentBg = Color(0xFF3A2820),
        sage = Color(0xFF8DB890),
        sageSoft = Color(0xFF4A6B4E),
        sageBg = Color(0xFF253028),
        amber = Color(0xFFD4B86A),
        amberSoft = Color(0xFF7A6A30),
        amberBg = Color(0xFF302A18),
        rose = Color(0xFFC88090),
        roseSoft = Color(0xFF7A4A55),
        roseBg = Color(0xFF352028),
        lavender = Color(0xFFA090C8),
        lavenderSoft = Color(0xFF5E5480),
        lavenderBg = Color(0xFF2A2540),
        textSecondary = Color(0xFFBEB3AA),
        textMuted = Color(0xFF8A7F76),
        textHint = Color(0xFF5E554E),
        border = Color(0xFF3E3832),
        borderLight = Color(0xFF2E2824),
        cardBackground = Color(0xFF272320),
        warmBackground = Color(0xFF211E1A),
        modalOverlay = Color(0x99000000),
    )

// ==================== Azure Sea (碧海) ====================

val OceanLightExtendedColors =
    ExtendedColorScheme(
        accent = Color(0xFF3D8FA0),
        accentSoft = Color(0xFF8DC4D0),
        accentBg = Color(0xFFE8F4F7),
        sage = Color(0xFF5BA88E),
        sageSoft = Color(0xFFA8D6C4),
        sageBg = Color(0xFFEBF5F0),
        amber = Color(0xFFD4946B),
        amberSoft = Color(0xFFE8C4A8),
        amberBg = Color(0xFFFDF2EB),
        rose = Color(0xFFB07080),
        roseSoft = Color(0xFFD8B0B8),
        roseBg = Color(0xFFF8EEF0),
        lavender = Color(0xFF7B85A8),
        lavenderSoft = Color(0xFFB4BAD0),
        lavenderBg = Color(0xFFEDEEF5),
        textSecondary = Color(0xFF4A5A65),
        textMuted = Color(0xFF7A8A95),
        textHint = Color(0xFFAABAC5),
        border = Color(0xFFCED8DE),
        borderLight = Color(0xFFE2ECF0),
        cardBackground = Color(0xFFFFFFFF),
        warmBackground = Color(0xFFE8F0F4),
        modalOverlay = Color(0x661C2830),
    )

val OceanDarkExtendedColors =
    ExtendedColorScheme(
        accent = Color(0xFF52A8BA),
        accentSoft = Color(0xFF2A6070),
        accentBg = Color(0xFF182830),
        sage = Color(0xFF70BEA5),
        sageSoft = Color(0xFF3A6855),
        sageBg = Color(0xFF1A3028),
        amber = Color(0xFFE0AA80),
        amberSoft = Color(0xFF7A5A3A),
        amberBg = Color(0xFF302418),
        rose = Color(0xFFC08898),
        roseSoft = Color(0xFF6A4550),
        roseBg = Color(0xFF301820),
        lavender = Color(0xFF9098B8),
        lavenderSoft = Color(0xFF505870),
        lavenderBg = Color(0xFF202430),
        textSecondary = Color(0xFFAABAC5),
        textMuted = Color(0xFF708090),
        textHint = Color(0xFF455565),
        border = Color(0xFF2E3840),
        borderLight = Color(0xFF1E2830),
        cardBackground = Color(0xFF1E262C),
        warmBackground = Color(0xFF182228),
        modalOverlay = Color(0x99000000),
    )

// ==================== Flower Whisper (花语) ====================

val PetalLightExtendedColors =
    ExtendedColorScheme(
        accent = Color(0xFFC2789A),
        accentSoft = Color(0xFFE0B0C4),
        accentBg = Color(0xFFFBEEF4),
        sage = Color(0xFF6EB5A0),
        sageSoft = Color(0xFFB0D8C8),
        sageBg = Color(0xFFECF6F2),
        amber = Color(0xFFD4977B),
        amberSoft = Color(0xFFE8C8B0),
        amberBg = Color(0xFFFDF2EC),
        rose = Color(0xFFA07AB8),
        roseSoft = Color(0xFFCCB0D8),
        roseBg = Color(0xFFF4EEF8),
        lavender = Color(0xFF8895B0),
        lavenderSoft = Color(0xFFB8C0D4),
        lavenderBg = Color(0xFFEFF1F6),
        textSecondary = Color(0xFF6B5060),
        textMuted = Color(0xFF9A8090),
        textHint = Color(0xFFBEB0B8),
        border = Color(0xFFD8CDD0),
        borderLight = Color(0xFFEDE4E8),
        cardBackground = Color(0xFFFFFFFF),
        warmBackground = Color(0xFFF5ECF0),
        modalOverlay = Color(0x662C2028),
    )

val PetalDarkExtendedColors =
    ExtendedColorScheme(
        accent = Color(0xFFD88AAE),
        accentSoft = Color(0xFF805068),
        accentBg = Color(0xFF351828),
        sage = Color(0xFF80C8B0),
        sageSoft = Color(0xFF3A6855),
        sageBg = Color(0xFF1A3028),
        amber = Color(0xFFE0B090),
        amberSoft = Color(0xFF7A5A40),
        amberBg = Color(0xFF302018),
        rose = Color(0xFFB890C8),
        roseSoft = Color(0xFF604870),
        roseBg = Color(0xFF281838),
        lavender = Color(0xFF98A5C0),
        lavenderSoft = Color(0xFF506078),
        lavenderBg = Color(0xFF202030),
        textSecondary = Color(0xFFBEB0B8),
        textMuted = Color(0xFF8A7880),
        textHint = Color(0xFF5E4E58),
        border = Color(0xFF3E3438),
        borderLight = Color(0xFF2E2428),
        cardBackground = Color(0xFF282024),
        warmBackground = Color(0xFF241C20),
        modalOverlay = Color(0x99000000),
    )
