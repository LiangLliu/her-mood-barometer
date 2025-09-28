package com.lianglliu.hermoodbarometer.permissions

import android.app.AlarmManager
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings

/**
 * 权限与设置跳转辅助
 */
object PermissionHelpers {
    fun notificationsEnabled(context: Context): Boolean {
        val nm = context.getSystemService(NotificationManager::class.java)
        return nm.areNotificationsEnabled()
    }

    fun canScheduleExactAlarms(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val am = context.getSystemService(AlarmManager::class.java)
            am.canScheduleExactAlarms()
        } else true
    }

    fun openAppNotificationSettings(context: Context) {
        val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
            putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        context.startActivity(intent)
    }

    fun openExactAlarmSettings(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        }
    }
}


