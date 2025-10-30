package com.lianglliu.hermoodbarometer.core.permissions

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.lianglliu.hermoodbarometer.core.locales.R

/**
 * 权限请求对话框
 * 用于向用户解释为什么需要某个权限，并引导用户去设置页面
 */
@Composable
fun PermissionRequestDialog(
    permission: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = getPermissionTitle(permission))
        },
        text = {
            Text(text = getPermissionRationale(permission))
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(stringResource(R.string.permission_go_to_settings))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.permission_cancel))
            }
        }
    )
}

/**
 * 获取权限标题
 */
@Composable
private fun getPermissionTitle(permission: String): String {
    return when (permission) {
        "notification" -> stringResource(R.string.permission_notification_title)
        "exact_alarm" -> stringResource(R.string.permission_exact_alarm_title)
        else -> stringResource(R.string.permission_required)
    }
}

/**
 * 获取权限说明
 */
@Composable
private fun getPermissionRationale(permission: String): String {
    return when (permission) {
        "notification" -> stringResource(R.string.permission_notification_rationale)
        "exact_alarm" -> stringResource(R.string.permission_exact_alarm_rationale)
        else -> stringResource(R.string.permission_default_rationale)
    }
}