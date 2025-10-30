package com.lianglliu.hermoodbarometer.core.permissions

/**
 * 权限检查结果
 */
sealed interface PermissionCheckResult {
    data object Idle : PermissionCheckResult
    data object Success : PermissionCheckResult
    data class PermissionsRequired(val permissions: List<String>) : PermissionCheckResult
}
