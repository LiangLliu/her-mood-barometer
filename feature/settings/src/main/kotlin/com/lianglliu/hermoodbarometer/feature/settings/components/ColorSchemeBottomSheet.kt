package com.lianglliu.hermoodbarometer.feature.settings.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lianglliu.hermoodbarometer.core.designsystem.icon.AppIcons
import com.lianglliu.hermoodbarometer.core.designsystem.icon.outlined.Check
import com.lianglliu.hermoodbarometer.core.designsystem.theme.HerMoodBarometerTheme
import com.lianglliu.hermoodbarometer.core.designsystem.theme.OceanLightExtendedColors
import com.lianglliu.hermoodbarometer.core.designsystem.theme.PetalLightExtendedColors
import com.lianglliu.hermoodbarometer.core.designsystem.theme.WarmLightExtendedColors
import com.lianglliu.hermoodbarometer.core.designsystem.theme.supportsDynamicTheming
import com.lianglliu.hermoodbarometer.core.locales.R
import com.lianglliu.hermoodbarometer.core.model.data.ColorSchemeConfig

// Stable visual anchors — use light-mode extended colors as preview swatches
private val WarmPreviewColors =
    with(WarmLightExtendedColors) { listOf(accent, sage, amber, rose, lavender) }
private val OceanPreviewColors =
    with(OceanLightExtendedColors) { listOf(accent, sage, amber, rose, lavender) }
private val PetalPreviewColors =
    with(PetalLightExtendedColors) { listOf(accent, sage, amber, rose, lavender) }

private val CardShape = RoundedCornerShape(16.dp)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ColorSchemeBottomSheet(
    currentConfig: ColorSchemeConfig,
    onConfigSelected: (ColorSchemeConfig) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(onDismissRequest = onDismiss, sheetState = sheetState, modifier = modifier) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp).padding(bottom = 32.dp)
        ) {
            Text(
                text = stringResource(R.string.color_scheme_title),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 16.dp),
            )

            ColorSchemeCard(
                name = stringResource(R.string.color_scheme_warm),
                description = stringResource(R.string.color_scheme_warm_desc),
                accentColor = WarmLightExtendedColors.accent,
                previewColors = WarmPreviewColors,
                isSelected = currentConfig == ColorSchemeConfig.WARM,
                onClick = {
                    onConfigSelected(ColorSchemeConfig.WARM)
                    onDismiss()
                },
            )

            Spacer(Modifier.height(12.dp))

            ColorSchemeCard(
                name = stringResource(R.string.color_scheme_ocean),
                description = stringResource(R.string.color_scheme_ocean_desc),
                accentColor = OceanLightExtendedColors.accent,
                previewColors = OceanPreviewColors,
                isSelected = currentConfig == ColorSchemeConfig.OCEAN,
                onClick = {
                    onConfigSelected(ColorSchemeConfig.OCEAN)
                    onDismiss()
                },
            )

            Spacer(Modifier.height(12.dp))

            ColorSchemeCard(
                name = stringResource(R.string.color_scheme_petal),
                description = stringResource(R.string.color_scheme_petal_desc),
                accentColor = PetalLightExtendedColors.accent,
                previewColors = PetalPreviewColors,
                isSelected = currentConfig == ColorSchemeConfig.PETAL,
                onClick = {
                    onConfigSelected(ColorSchemeConfig.PETAL)
                    onDismiss()
                },
            )

            if (supportsDynamicTheming()) {
                Spacer(Modifier.height(12.dp))

                ColorSchemeCard(
                    name = stringResource(R.string.color_scheme_dynamic),
                    description = stringResource(R.string.color_scheme_dynamic_desc),
                    accentColor = null,
                    previewColors = emptyList(),
                    isSelected = currentConfig == ColorSchemeConfig.DYNAMIC,
                    onClick = {
                        onConfigSelected(ColorSchemeConfig.DYNAMIC)
                        onDismiss()
                    },
                )
            }
        }
    }
}

@Composable
private fun ColorSchemeCard(
    name: String,
    description: String,
    accentColor: Color?,
    previewColors: List<Color>,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val borderColor =
        if (isSelected) MaterialTheme.colorScheme.primary
        else MaterialTheme.colorScheme.outlineVariant

    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .clip(CardShape)
                .border(
                    width = if (isSelected) 2.dp else 1.dp,
                    color = borderColor,
                    shape = CardShape,
                )
                .clickable(onClick = onClick)
                .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (accentColor != null) {
            Box(modifier = Modifier.size(12.dp).clip(CircleShape).background(accentColor))
            Spacer(Modifier.width(12.dp))
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(text = name, style = MaterialTheme.typography.titleMedium)
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            if (previewColors.isNotEmpty()) {
                Spacer(Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    previewColors.forEach { color ->
                        Box(modifier = Modifier.size(24.dp).clip(CircleShape).background(color))
                    }
                }
            }
        }

        if (isSelected) {
            Spacer(Modifier.width(8.dp))
            Icon(
                imageVector = AppIcons.Outlined.Check,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp),
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun ColorSchemeBottomSheetPreview() {
    HerMoodBarometerTheme {
        ColorSchemeBottomSheet(
            currentConfig = ColorSchemeConfig.WARM,
            onConfigSelected = {},
            onDismiss = {},
        )
    }
}
