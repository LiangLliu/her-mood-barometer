package com.lianglliu.hermoodbarometer.core.permissions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext

/**
 * 权限处理器
 * 提供一个完整的权限请求流程，包括检查、提示和跳转设置
 */
@Composable
fun PermissionHandler(
    permissions: List<String>,
    onAllPermissionsGranted: () -> Unit,
    onPermissionsDenied: () -> Unit = {},
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    var showPermissionDialog by remember { mutableStateOf(false) }
    var currentPermission by remember { mutableStateOf("") }

    val permissionManager = rememberPermissionManager { results ->
        // 检查是否所有权限都已授予
        val allGranted = results.all { it.value == PermissionState.Granted }
        if (allGranted) {
            onAllPermissionsGranted()
            showPermissionDialog = false
        } else {
            // 找到第一个未授予的权限
            val deniedPermission = results.entries.firstOrNull {
                it.value != PermissionState.Granted
            }
            if (deniedPermission != null) {
                currentPermission = deniedPermission.key
                showPermissionDialog = true
            }
        }
    }

    // 初始权限检查
    LaunchedEffect(permissions) {
        val results = permissions.associateWith { permission ->
            PermissionHelpers.checkPermissionState(context, permission)
        }

        val allGranted = results.all { it.value == PermissionState.Granted }
        if (!allGranted) {
            // 找到第一个未授予的权限
            val deniedPermission = results.entries.firstOrNull {
                it.value != PermissionState.Granted
            }
            if (deniedPermission != null) {
                currentPermission = deniedPermission.key
                showPermissionDialog = true
            }
        } else {
            onAllPermissionsGranted()
        }
    }

    // 显示权限请求对话框
    if (showPermissionDialog && currentPermission.isNotEmpty()) {
        PermissionRequestDialog(
            permission = currentPermission,
            onConfirm = {
                permissionManager.requestPermission(currentPermission)
                showPermissionDialog = false
            },
            onDismiss = {
                showPermissionDialog = false
                onPermissionsDenied()
            }
        )
    }

    content()
}

/**
 * 单个权限处理器
 */
@Composable
fun SinglePermissionHandler(
    permission: String,
    onPermissionGranted: () -> Unit,
    onPermissionDenied: () -> Unit = {},
    content: @Composable () -> Unit
) {
    PermissionHandler(
        permissions = listOf(permission),
        onAllPermissionsGranted = onPermissionGranted,
        onPermissionsDenied = onPermissionDenied,
        content = content
    )
}