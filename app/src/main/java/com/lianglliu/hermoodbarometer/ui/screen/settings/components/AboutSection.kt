package com.lianglliu.hermoodbarometer.ui.screen.settings.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.lianglliu.hermoodbarometer.R
import com.lianglliu.hermoodbarometer.ui.components.icons.AppIcons
import com.lianglliu.hermoodbarometer.ui.components.icons.outlined.HistoryEdu
import com.lianglliu.hermoodbarometer.ui.components.icons.outlined.Info

/**
 * 关于设置模块
 * 符合 Material Design 3 设计规范
 */
@Composable
fun AboutSection(
    onAboutLicensesClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = stringResource(R.string.about),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            // 应用名称和版本
            SettingsItem(
                icon = Icons.Default.Info,
                title = stringResource(R.string.about_app),
                subtitle = stringResource(R.string.about_app_description)
            )

            SettingsItem(
                icon = AppIcons.Outlined.HistoryEdu,
                title = stringResource(R.string.licenses),
                onClick = onAboutLicensesClick
            )
            
            // 版本信息
            SettingsItem(
                icon = AppIcons.Outlined.Info,
                title = stringResource(R.string.version),
                subtitle = "1.0.0"
            )
        }
    }
}
