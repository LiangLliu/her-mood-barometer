package com.lianglliu.hermoodbarometer.ui.screen.settings.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.lianglliu.hermoodbarometer.R

/**
 * 数据管理设置模块
 */
@Composable
fun DataSection(
    onExportDataClick: () -> Unit,
    onImportDataClick: () -> Unit
) {
    SettingsSection(title = stringResource(R.string.data)) {
        SettingsItem(
            icon = Icons.Default.Info,
            title = stringResource(R.string.export_data),
            subtitle = stringResource(R.string.export_data_description),
            trailing = {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null
                )
            },
            onClick = onExportDataClick
        )
        
        SettingsItem(
            icon = Icons.Default.Info,
            title = stringResource(R.string.import_data),
            subtitle = stringResource(R.string.import_data_description),
            trailing = {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null
                )
            },
            onClick = onImportDataClick
        )
    }
} 