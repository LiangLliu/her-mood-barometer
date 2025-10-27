package com.lianglliu.hermoodbarometer.core.notifications

import android.Manifest.permission
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import androidx.core.app.ActivityCompat.checkSelfPermission
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.net.toUri
import dagger.hilt.android.qualifiers.ApplicationContext
import com.lianglliu.hermoodbarometer.core.util.Constants.DEEP_LINK_SCHEME_AND_HOST
import com.lianglliu.hermoodbarometer.core.util.Constants.TARGET_ACTIVITY_NAME
import javax.inject.Inject
import javax.inject.Singleton
import com.lianglliu.hermoodbarometer.core.locales.R as localesR

private const val MOOD_REMINDER_NOTIFICATION_REQUEST_CODE = 0
private const val MOOD_REMINDER_NOTIFICATION_ID = 1
private const val MOOD_REMINDER_NOTIFICATION_CHANNEL_ID = "mood_reminder_channel"

/**
 * Implementation of [Notifier] that displays notifications in the system tray.
 */
@Singleton
internal class SystemTrayNotifier @Inject constructor(
    @ApplicationContext private val context: Context,
) : Notifier {

    override fun postDailyReminderNotification() = with(context) {
        if (checkSelfPermission(this, permission.POST_NOTIFICATIONS) != PERMISSION_GRANTED) return

        val notification = createMoodReminderNotification {
            setSmallIcon(android.R.drawable.ic_menu_my_calendar)
                .setContentTitle(getString(localesR.string.notification_title))
                .setContentText(getString(localesR.string.notification_text))
                .setContentIntent(moodRecordPendingIntent())
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
        }

        // Send the notification
        val notificationManager = NotificationManagerCompat.from(this)
        notificationManager.notify(MOOD_REMINDER_NOTIFICATION_ID, notification)
    }
}

/**
 * Creates a notification for mood reminder
 */
private fun Context.createMoodReminderNotification(
    block: NotificationCompat.Builder.() -> Unit,
): Notification {
    ensureNotificationChannelExists()
    return NotificationCompat.Builder(
        this,
        MOOD_REMINDER_NOTIFICATION_CHANNEL_ID,
    )
        .apply(block)
        .build()
}

/**
 * Ensures that a notification channel is present if applicable
 */
private fun Context.ensureNotificationChannelExists() {
    val channel = NotificationChannel(
        MOOD_REMINDER_NOTIFICATION_CHANNEL_ID,
        getString(localesR.string.daily_reminder),
        NotificationManager.IMPORTANCE_DEFAULT,
    ).apply {
        description = getString(localesR.string.daily_reminder_description)
    }
    NotificationManagerCompat.from(this).createNotificationChannel(channel)
}

private fun Context.moodRecordPendingIntent(): PendingIntent? = PendingIntent.getActivity(
    this,
    MOOD_REMINDER_NOTIFICATION_REQUEST_CODE,
    Intent().apply {
        action = Intent.ACTION_VIEW
        data = "$DEEP_LINK_SCHEME_AND_HOST/record".toUri()
        component = ComponentName(
            packageName,
            TARGET_ACTIVITY_NAME,
        )
    },
    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
)