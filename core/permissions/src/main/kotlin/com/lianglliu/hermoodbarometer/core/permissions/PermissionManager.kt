package com.lianglliu.hermoodbarometer.core.permissions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * Composable 权限管理器
 * 用于在 Compose UI 中处理权限申请和状态管理
 */
@Composable
fun rememberPermissionManager(
    onPermissionResult: (Map<String, PermissionState>) -> Unit = {}
): PermissionManager {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val permissionManager = remember {
        PermissionManager(
            checkPermissions = { permissions ->
                permissions.associateWith { permission ->
                    PermissionHelpers.checkPermissionState(context, permission)
                }
            },
            requestPermissions = { permissions ->
                permissions.forEach { permission ->
                    PermissionHelpers.openPermissionSettings(context, permission)
                }
            }
        )
    }

    // 监听生命周期，当从设置页面返回时重新检查权限
    LaunchedEffect(lifecycleOwner) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
            val currentPermissions = permissionManager.permissionsToCheck
            if (currentPermissions.isNotEmpty()) {
                val results = permissionManager.checkPermissions(currentPermissions)
                permissionManager.updatePermissionStates(results)
                onPermissionResult(results)
            }
        }
    }

    return permissionManager
}

/**
 * 权限管理器
 * 处理权限检查、申请和状态管理
 */
class PermissionManager(
    private val checkPermissions: (List<String>) -> Map<String, PermissionState>,
    private val requestPermissions: (List<String>) -> Unit
) {
    private val _permissionStates = MutableStateFlow<Map<String, PermissionState>>(emptyMap())

    internal var permissionsToCheck = listOf<String>()
        private set

    /**
     * 检查权限状态
     */
    fun checkPermission(permission: String): PermissionState {
        return checkPermissions(listOf(permission))[permission] ?: PermissionState.Denied
    }

    /**
     * 检查多个权限状态
     */
    fun checkPermissions(permissions: List<String>): Map<String, PermissionState> {
        permissionsToCheck = permissions
        return checkPermissions.invoke(permissions)
    }

    /**
     * 请求权限
     */
    fun requestPermission(permission: String) {
        requestPermissions(listOf(permission))
    }

    /**
     * 请求多个权限
     */
    fun requestPermissions(permissions: List<String>) {
        permissionsToCheck = permissions
        requestPermissions.invoke(permissions)
    }

    /**
     * 更新权限状态
     */
    internal fun updatePermissionStates(states: Map<String, PermissionState>) {
        _permissionStates.value = states
    }

    /**
     * 检查所有权限是否都已授予
     */
    fun allPermissionsGranted(permissions: List<String>): Boolean {
        val states = checkPermissions(permissions)
        return states.all { it.value == PermissionState.Granted }
    }
}

/**
 * 权限请求状态
 */
data class PermissionRequestState(
    val showDialog: Boolean = false,
    val permission: String = "",
    val onResult: (Boolean) -> Unit = {}
)