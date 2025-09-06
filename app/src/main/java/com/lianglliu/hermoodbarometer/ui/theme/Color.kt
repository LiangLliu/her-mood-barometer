package com.lianglliu.hermoodbarometer.ui.theme

import androidx.compose.ui.graphics.Color

/**
 * Material Design 3 色彩系统
 * 基于Material You设计规范的色彩标记系统
 * 
 * 色彩命名规则：[色相][明度等级]
 * 明度等级：0(最暗) - 100(最亮)
 */

// ============ Primary 主色调 ============
val Primary0 = Color(0xFF000000)
val Primary10 = Color(0xFF001F25)
val Primary20 = Color(0xFF003A42)
val Primary30 = Color(0xFF00555E)
val Primary40 = Color(0xFF00717B)
val Primary50 = Color(0xFF008E99)
val Primary60 = Color(0xFF00ACB8)
val Primary70 = Color(0xFF00CAD7)
val Primary80 = Color(0xFF4FE8F6)
val Primary90 = Color(0xFFA6F4FA)
val Primary95 = Color(0xFFD0F9FC)
val Primary99 = Color(0xFFF4FEFF)
val Primary100 = Color(0xFFFFFFFF)

// ============ Secondary 辅助色 ============
val Secondary0 = Color(0xFF000000)
val Secondary10 = Color(0xFF0A1E1F)
val Secondary20 = Color(0xFF253334)
val Secondary30 = Color(0xFF3B494A)
val Secondary40 = Color(0xFF536061)
val Secondary50 = Color(0xFF6C7879)
val Secondary60 = Color(0xFF859092)
val Secondary70 = Color(0xFF9FAAAB)
val Secondary80 = Color(0xFFBAC5C6)
val Secondary90 = Color(0xFFD6E1E2)
val Secondary95 = Color(0xFFE4EFEF)
val Secondary99 = Color(0xFFF4FEFF)
val Secondary100 = Color(0xFFFFFFFF)

// ============ Tertiary 第三色 ============
val Tertiary0 = Color(0xFF000000)
val Tertiary10 = Color(0xFF1A1B23)
val Tertiary20 = Color(0xFF2F3038)
val Tertiary30 = Color(0xFF45464F)
val Tertiary40 = Color(0xFF5D5E67)
val Tertiary50 = Color(0xFF767680)
val Tertiary60 = Color(0xFF90909A)
val Tertiary70 = Color(0xFFABABB4)
val Tertiary80 = Color(0xFFC6C6CF)
val Tertiary90 = Color(0xFFE2E2EB)
val Tertiary95 = Color(0xFFF0F0F9)
val Tertiary99 = Color(0xFFFFFBFF)
val Tertiary100 = Color(0xFFFFFFFF)

// ============ Error 错误色 ============
val Error0 = Color(0xFF000000)
val Error10 = Color(0xFF410002)
val Error20 = Color(0xFF690005)
val Error30 = Color(0xFF93000A)
val Error40 = Color(0xFFBA1A1A)
val Error50 = Color(0xFFDE3730)
val Error60 = Color(0xFFFF5449)
val Error70 = Color(0xFFFF897D)
val Error80 = Color(0xFFFFB4AB)
val Error90 = Color(0xFFFFDAD6)
val Error95 = Color(0xFFFFEDEA)
val Error99 = Color(0xFFFFFBFF)
val Error100 = Color(0xFFFFFFFF)

// ============ Neutral 中性色 ============
val Neutral0 = Color(0xFF000000)
val Neutral4 = Color(0xFF060606)
val Neutral6 = Color(0xFF0F0F0F)
val Neutral10 = Color(0xFF191C1C)
val Neutral12 = Color(0xFF1E1E1E)
val Neutral17 = Color(0xFF2B2B2B)
val Neutral20 = Color(0xFF2E3131)
val Neutral22 = Color(0xFF383838)
val Neutral24 = Color(0xFF3C3C3C)
val Neutral30 = Color(0xFF444748)
val Neutral40 = Color(0xFF5C5F5F)
val Neutral50 = Color(0xFF747878)
val Neutral60 = Color(0xFF8E9192)
val Neutral70 = Color(0xFFA8ABAC)
val Neutral80 = Color(0xFFC4C7C7)
val Neutral87 = Color(0xFFDEDEDE)
val Neutral90 = Color(0xFFE6E6E6)
val Neutral92 = Color(0xFFEBEBEB)
val Neutral94 = Color(0xFFEFEFEF)
val Neutral95 = Color(0xFFEEF1F1)
val Neutral96 = Color(0xFFF5F5F5)
val Neutral98 = Color(0xFFFAFAFA)
val Neutral99 = Color(0xFFFAFDFD)
val Neutral100 = Color(0xFFFFFFFF)

// ============ NeutralVariant 变体中性色 ============
val NeutralVariant0 = Color(0xFF000000)
val NeutralVariant10 = Color(0xFF161D1E)
val NeutralVariant20 = Color(0xFF2B3233)
val NeutralVariant30 = Color(0xFF414849)
val NeutralVariant40 = Color(0xFF596061)
val NeutralVariant50 = Color(0xFF727879)
val NeutralVariant60 = Color(0xFF8C9293)
val NeutralVariant70 = Color(0xFFA6ACAD)
val NeutralVariant80 = Color(0xFFC2C8C8)
val NeutralVariant90 = Color(0xFFDEE4E4)
val NeutralVariant95 = Color(0xFFECF2F2)
val NeutralVariant99 = Color(0xFFF7FDFD)
val NeutralVariant100 = Color(0xFFFFFFFF)

// ============ 语义化颜色定义 ============

/**
 * 成功状态颜色
 */
val Success = Color(0xFF4CAF50)
val OnSuccess = Color(0xFFFFFFFF)
val SuccessContainer = Color(0xFFE8F5E8)
val OnSuccessContainer = Color(0xFF1B5E20)

/**
 * 警告状态颜色
 */
val Warning = Color(0xFFFF9800)
val OnWarning = Color(0xFFFFFFFF)
val WarningContainer = Color(0xFFFFF3E0)
val OnWarningContainer = Color(0xFFE65100)

/**
 * 信息状态颜色
 */
val Info = Color(0xFF2196F3)
val OnInfo = Color(0xFFFFFFFF)
val InfoContainer = Color(0xFFE3F2FD)
val OnInfoContainer = Color(0xFF0D47A1)

/**
 * 情绪相关的语义化颜色
 */
object EmotionColors {
    val Happy = Color(0xFFFFEB3B)
    val Sad = Color(0xFF2196F3)
    val Angry = Color(0xFFFF5722)
    val Calm = Color(0xFF4CAF50)
    val Excited = Color(0xFFFF9800)
    val Anxious = Color(0xFF9C27B0)
    val Tired = Color(0xFF795548)
    val Grateful = Color(0xFFE91E63)
    val Confused = Color(0xFF607D8B)
    val Lonely = Color(0xFF9E9E9E)
}