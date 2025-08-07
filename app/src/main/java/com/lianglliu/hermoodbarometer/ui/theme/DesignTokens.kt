package com.lianglliu.hermoodbarometer.ui.theme

import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Material Design 3 设计标记系统
 * 提供一致的设计标准和可复用的设计原子
 */

// ============ 尺寸设计标记 ============

/**
 * 应用尺寸标记
 * 定义组件的标准尺寸
 */
@Immutable
data class AppDimens(
    // 组件高度
    val buttonHeight: Dp = 48.dp,
    val toolbarHeight: Dp = 56.dp,
    val bottomNavHeight: Dp = 80.dp,
    val cardMinHeight: Dp = 72.dp,
    val listItemHeight: Dp = 56.dp,
    val fabSize: Dp = 56.dp,
    val iconButtonSize: Dp = 48.dp,
    
    // 组件宽度
    val dialogMaxWidth: Dp = 560.dp,
    val chipMinWidth: Dp = 96.dp,
    val bottomSheetMaxWidth: Dp = 640.dp,
    
    // 图标尺寸
    val iconSizeSmall: Dp = 16.dp,
    val iconSizeMedium: Dp = 24.dp,
    val iconSizeLarge: Dp = 32.dp,
    val iconSizeXLarge: Dp = 48.dp,
    
    // 头像尺寸
    val avatarSizeSmall: Dp = 32.dp,
    val avatarSizeMedium: Dp = 40.dp,
    val avatarSizeLarge: Dp = 56.dp,
    val avatarSizeXLarge: Dp = 72.dp,
    
    // 缩略图尺寸
    val thumbnailSize: Dp = 72.dp,
    
    // 触摸目标尺寸
    val minTouchTarget: Dp = 48.dp,
)

// ============ 间距设计标记 ============

/**
 * 应用间距标记
 * 基于8dp网格系统
 */
@Immutable
data class AppSpacing(
    val none: Dp = 0.dp,
    val extraSmall: Dp = 4.dp,
    val small: Dp = 8.dp,
    val medium: Dp = 16.dp,
    val large: Dp = 24.dp,
    val extraLarge: Dp = 32.dp,
    val huge: Dp = 48.dp,
    val massive: Dp = 64.dp,
    
    // 页面间距
    val pageHorizontal: Dp = 16.dp,
    val pageVertical: Dp = 16.dp,
    
    // 卡片间距
    val cardPadding: Dp = 16.dp,
    val cardSpacing: Dp = 8.dp,
    
    // 列表间距
    val listItemPadding: Dp = 16.dp,
    val listItemSpacing: Dp = 8.dp,
    
    // 按钮间距
    val buttonPadding: Dp = 16.dp,
    val buttonSpacing: Dp = 8.dp,
    
    // 对话框间距
    val dialogPadding: Dp = 24.dp,
    val dialogContentSpacing: Dp = 16.dp,
)

// ============ 形状设计标记 ============

/**
 * 应用形状标记
 * 基于Material Design 3形状系统
 */
@Immutable
data class AppShapes(
    // Material 3 标准形状
    val materialShapes: Shapes = Shapes(
        extraSmall = RoundedCornerShape(4.dp),
        small = RoundedCornerShape(8.dp),
        medium = RoundedCornerShape(12.dp),
        large = RoundedCornerShape(16.dp),
        extraLarge = RoundedCornerShape(28.dp)
    ),
    
    // 自定义组件形状
    val button: CornerBasedShape = RoundedCornerShape(8.dp),
    val card: CornerBasedShape = RoundedCornerShape(12.dp),
    val chip: CornerBasedShape = RoundedCornerShape(8.dp),
    val bottomSheet: CornerBasedShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
    val dialog: CornerBasedShape = RoundedCornerShape(16.dp),
    val fab: CornerBasedShape = RoundedCornerShape(16.dp),
    val avatar: CornerBasedShape = RoundedCornerShape(50),
    val image: CornerBasedShape = RoundedCornerShape(8.dp),
    
    // 情绪相关形状
    val emotionCard: CornerBasedShape = RoundedCornerShape(12.dp),
    val emotionChip: CornerBasedShape = RoundedCornerShape(16.dp),
    val intensitySlider: CornerBasedShape = RoundedCornerShape(4.dp),
)

// ============ 高程设计标记 ============

/**
 * 应用高程标记
 * 定义组件的阴影层级
 */
@Immutable
data class AppElevation(
    val none: Dp = 0.dp,
    val level1: Dp = 1.dp,
    val level2: Dp = 3.dp,
    val level3: Dp = 6.dp,
    val level4: Dp = 8.dp,
    val level5: Dp = 12.dp,
    
    // 组件特定高程
    val card: Dp = 2.dp,
    val cardElevated: Dp = 6.dp,
    val button: Dp = 0.dp,
    val buttonElevated: Dp = 1.dp,
    val bottomSheet: Dp = 8.dp,
    val dialog: Dp = 24.dp,
    val fab: Dp = 6.dp,
    val navigationBar: Dp = 3.dp,
    val appBar: Dp = 0.dp,
    val menu: Dp = 8.dp,
)

// ============ CompositionLocal 提供者 ============

/**
 * 通过CompositionLocal提供设计标记
 * 确保在整个组件树中可以访问
 */
val LocalAppDimens = staticCompositionLocalOf { AppDimens() }
val LocalAppSpacing = staticCompositionLocalOf { AppSpacing() }
val LocalAppShapes = staticCompositionLocalOf { AppShapes() }
val LocalAppElevation = staticCompositionLocalOf { AppElevation() }

/**
 * 全局设计标记实例
 */
object GlobalDesignTokens {
    val dimens = AppDimens()
    val spacing = AppSpacing()
    val shapes = AppShapes()
    val elevation = AppElevation()
}

/**
 * 便捷访问器
 * 在Composable中快速访问设计标记
 */
object DesignTokens {
    val dimens: AppDimens
        @androidx.compose.runtime.Composable
        @androidx.compose.runtime.ReadOnlyComposable
        get() = LocalAppDimens.current
    
    val spacing: AppSpacing
        @androidx.compose.runtime.Composable
        @androidx.compose.runtime.ReadOnlyComposable
        get() = LocalAppSpacing.current
    
    val shapes: AppShapes
        @androidx.compose.runtime.Composable
        @androidx.compose.runtime.ReadOnlyComposable
        get() = LocalAppShapes.current
    
    val elevation: AppElevation
        @androidx.compose.runtime.Composable
        @androidx.compose.runtime.ReadOnlyComposable
        get() = LocalAppElevation.current
}