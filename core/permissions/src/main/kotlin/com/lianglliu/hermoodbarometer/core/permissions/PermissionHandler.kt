package com.lianglliu.hermoodbarometer.core.permissions

import android.Manifest
import android.app.Activity
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle

/**
 * 权限处理器 提供一个完整的权限请求流程，包括检查、提示和跳转设置
 *
 * 对于 Android 13+ 的通知权限，会先尝试显示系统权限对话框， 只有在用户永久拒绝后才会引导到设置页面
 */
@Composable
fun PermissionHandler(
    permissions: List<String>,
    onAllPermissionsGranted: () -> Unit,
    onPermissionsDenied: () -> Unit = {},
    content: @Composable () -> Unit,
) {
    val context = LocalContext.current
    val activity = context as? Activity
    val lifecycleOwner = LocalLifecycleOwner.current
    var showPermissionDialog by remember { mutableStateOf(false) }
    var currentPermission by remember { mutableStateOf("") }
    var hasRequestedRuntimePermission by remember { mutableStateOf(false) }

    // Runtime permission launcher for POST_NOTIFICATIONS
    val permissionLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted
            ->
            if (isGranted) {
                // Check if all permissions are now granted
                val allGranted = permissions.all {
                    PermissionHelpers.checkPermissionState(context, it) == PermissionState.Granted
                }
                if (allGranted) {
                    onAllPermissionsGranted()
                }
            } else {
                hasRequestedRuntimePermission = true
                // User denied - check if we should show rationale or go to settings
                val shouldShowRationale =
                    activity?.let {
                        ActivityCompat.shouldShowRequestPermissionRationale(
                            it,
                            Manifest.permission.POST_NOTIFICATIONS,
                        )
                    } ?: false

                if (!shouldShowRationale) {
                    // Permanently denied - show dialog to go to settings
                    currentPermission = "notification"
                    showPermissionDialog = true
                } else {
                    onPermissionsDenied()
                }
            }
        }

    // Lifecycle observer for when user returns from Settings
    LaunchedEffect(lifecycleOwner) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
            if (hasRequestedRuntimePermission || showPermissionDialog) {
                val allGranted = permissions.all {
                    PermissionHelpers.checkPermissionState(context, it) == PermissionState.Granted
                }
                if (allGranted) {
                    onAllPermissionsGranted()
                    showPermissionDialog = false
                }
            }
        }
    }

    // 初始权限检查
    LaunchedEffect(permissions) {
        val results = permissions.associateWith { permission ->
            PermissionHelpers.checkPermissionState(context, permission)
        }

        val allGranted = results.all { it.value == PermissionState.Granted }
        if (allGranted) {
            onAllPermissionsGranted()
        } else {
            val deniedPermission =
                results.entries.firstOrNull { it.value != PermissionState.Granted }
            if (deniedPermission != null) {
                val permission = deniedPermission.key
                if (
                    permission == "notification" &&
                        Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                        !hasRequestedRuntimePermission
                ) {
                    // Try runtime permission request first
                    permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                } else {
                    // Show dialog to guide to settings
                    currentPermission = permission
                    showPermissionDialog = true
                }
            }
        }
    }

    // 显示权限请求对话框
    if (showPermissionDialog && currentPermission.isNotEmpty()) {
        PermissionRequestDialog(
            permission = currentPermission,
            onConfirm = {
                PermissionHelpers.openPermissionSettings(context, currentPermission)
                showPermissionDialog = false
            },
            onDismiss = {
                showPermissionDialog = false
                onPermissionsDenied()
            },
        )
    }

    content()
}

/** 单个权限处理器 */
@Composable
fun SinglePermissionHandler(
    permission: String,
    onPermissionGranted: () -> Unit,
    onPermissionDenied: () -> Unit = {},
    content: @Composable () -> Unit,
) {
    PermissionHandler(
        permissions = listOf(permission),
        onAllPermissionsGranted = onPermissionGranted,
        onPermissionsDenied = onPermissionDenied,
        content = content,
    )
}
