package com.lianglliu.hermoodbarometer.core.permissions

import android.Manifest
import android.app.AlarmManager
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.core.content.ContextCompat

/**
 * 权限与设置跳转辅助
 *
 * 支持的权限类型：
 * - "notification" - 通知权限（Android 13+）
 * - "exact_alarm" - 精确闹钟权限（Android 12+）
 * - 其他标准 Android 权限字符串
 */
object PermissionHelpers {

    /**
     * 检查通知是否已启用
     */
    fun notificationsEnabled(context: Context): Boolean {
        val nm = context.getSystemService(NotificationManager::class.java)
        return nm.areNotificationsEnabled()
    }

    /**
     * 检查是否可以设置精确闹钟
     */
    fun canScheduleExactAlarms(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val am = context.getSystemService(AlarmManager::class.java)
            am.canScheduleExactAlarms()
        } else true
    }

    /**
     * 检查运行时权限（如POST_NOTIFICATIONS）
     */
    fun hasRuntimePermission(context: Context, permission: String): Boolean {
        return ContextCompat.checkSelfPermission(context, permission) ==
                PackageManager.PERMISSION_GRANTED
    }

    /**
     * 检查通知运行时权限（Android 13+）
     */
    fun hasNotificationPermission(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            hasRuntimePermission(context, Manifest.permission.POST_NOTIFICATIONS)
        } else {
            // Android 13 以下不需要运行时权限
            true
        }
    }

    /**
     * 打开应用通知设置页面
     */
    fun openAppNotificationSettings(context: Context) {
        val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
            putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        context.startActivity(intent)
    }

    /**
     * 打开精确闹钟设置页面
     */
    fun openExactAlarmSettings(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        }
    }

    /**
     * 打开应用详情设置页面（用于引导用户手动授予权限）
     */
    fun openAppSettings(context: Context) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", context.packageName, null)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        context.startActivity(intent)
    }

    /**
     * 根据权限类型打开对应的设置页面
     */
    fun openPermissionSettings(context: Context, permission: String) {
        when (permission) {
            "notification" -> openAppNotificationSettings(context)
            "exact_alarm" -> openExactAlarmSettings(context)
            else -> openAppSettings(context)
        }
    }

    /**
     * 检查权限状态
     */
    fun checkPermissionState(context: Context, permission: String): PermissionState {
        return when (permission) {
            "notification" -> {
                when {
                    !hasNotificationPermission(context) -> PermissionState.Denied
                    !notificationsEnabled(context) -> PermissionState.Denied
                    else -> PermissionState.Granted
                }
            }

            "exact_alarm" -> {
                if (canScheduleExactAlarms(context)) {
                    PermissionState.Granted
                } else {
                    PermissionState.Denied
                }
            }

            else -> {
                if (hasRuntimePermission(context, permission)) {
                    PermissionState.Granted
                } else {
                    PermissionState.Denied
                }
            }
        }
    }
}