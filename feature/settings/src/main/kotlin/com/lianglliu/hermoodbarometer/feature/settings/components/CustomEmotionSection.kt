package com.lianglliu.hermoodbarometer.feature.settings.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.lianglliu.hermoodbarometer.core.designsystem.icon.AppIcons
import com.lianglliu.hermoodbarometer.core.designsystem.icon.outlined.ChevronRight
import com.lianglliu.hermoodbarometer.core.designsystem.icon.outlined.MoreVert
import com.lianglliu.hermoodbarometer.core.locales.R

/**
 * 情绪管理设置模块
 * 符合 Material Design 3 设计规范
 */
@Composable
fun CustomEmotionSection(
    onCustomEmotionClick: () -> Unit,
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
                text = stringResource(R.string.emotion_management),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            SettingsItem(
                icon = AppIcons.Outlined.MoreVert,
                title = stringResource(R.string.manage_emotions),
                subtitle = stringResource(R.string.manage_emotions_desc),
                trailing = {
                    IconButton(onClick = onCustomEmotionClick) {
                        Icon(
                            imageVector = AppIcons.Outlined.ChevronRight,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                onClick = onCustomEmotionClick
            )
        }
    }
}