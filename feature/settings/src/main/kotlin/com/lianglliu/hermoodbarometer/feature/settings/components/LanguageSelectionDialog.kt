package com.lianglliu.hermoodbarometer.feature.settings.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.lianglliu.hermoodbarometer.core.locales.R

/**
 * 语言选择对话框
 */
@Composable
fun LanguageSelectionDialog(
    currentLanguage: String,
    onLanguageSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val languages = listOf(
        "system" to stringResource(R.string.system_theme),
        "zh" to stringResource(R.string.language_zh),
        // 使用标准 BCP-47 标签
        "zh-TW" to stringResource(R.string.language_zh_tw),
        "ja" to stringResource(R.string.language_ja),
        "ko" to stringResource(R.string.language_ko),
        "en" to stringResource(R.string.language_en)
    )
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(stringResource(R.string.language))
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                languages.forEach { (code, name) ->
                    Button(
                        onClick = { onLanguageSelected(code) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (code == currentLanguage) 
                                MaterialTheme.colorScheme.primary 
                            else 
                                MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Text(
                            text = name,
                            color = if (code == currentLanguage) 
                                MaterialTheme.colorScheme.onPrimary 
                            else 
                                MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
} 