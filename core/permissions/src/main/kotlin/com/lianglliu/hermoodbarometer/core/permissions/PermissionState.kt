package com.lianglliu.hermoodbarometer.core.permissions

/**
 * 权限状态
 */
sealed interface PermissionState {
    data object Granted : PermissionState
    data object Denied : PermissionState
    data object PermanentlyDenied : PermissionState
}

