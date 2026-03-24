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
import com.lianglliu.hermoodbarometer.core.common.util.Constants.DEEP_LINK_SCHEME_AND_HOST
import com.lianglliu.hermoodbarometer.core.common.util.Constants.TARGET_ACTIVITY_NAME
import com.lianglliu.hermoodbarometer.core.locales.R as localesR
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import jakarta.inject.Singleton

private const val MOOD_REMINDER_NOTIFICATION_REQUEST_CODE = 0
private const val MOOD_REMINDER_NOTIFICATION_ID = 1
private const val MOOD_REMINDER_NOTIFICATION_CHANNEL_ID = "mood_reminder_channel"

/** Implementation of [Notifier] that displays notifications in the system tray. */
@Singleton
internal class SystemTrayNotifier
@Inject
constructor(@ApplicationContext private val context: Context) : Notifier {

    override fun postDailyReminderNotification(): Unit =
        with(context) {
            if (checkSelfPermission(this, permission.POST_NOTIFICATIONS) != PERMISSION_GRANTED)
                return

            // Pick random title and text
            val titles = resources.getStringArray(localesR.array.notification_titles)
            val texts = resources.getStringArray(localesR.array.notification_texts)
            val index = (System.currentTimeMillis() / (24 * 60 * 60 * 1000)).toInt() % titles.size

            // Quick record action
            val quickRecordIntent =
                Intent().apply {
                    action = Intent.ACTION_VIEW
                    data = "$DEEP_LINK_SCHEME_AND_HOST/quick-record".toUri()
                    component = ComponentName(packageName, TARGET_ACTIVITY_NAME)
                }
            val quickRecordPendingIntent =
                PendingIntent.getActivity(
                    this,
                    MOOD_REMINDER_NOTIFICATION_REQUEST_CODE + 1,
                    quickRecordIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
                )

            val notification = createMoodReminderNotification {
                setSmallIcon(R.drawable.ic_notification)
                setContentTitle(titles[index])
                setContentText(texts[index])
                setContentIntent(moodRecordPendingIntent())
                setStyle(NotificationCompat.BigTextStyle().bigText(texts[index]))
                setPriority(NotificationCompat.PRIORITY_DEFAULT)
                setAutoCancel(true)
                setCategory(NotificationCompat.CATEGORY_REMINDER)
                addAction(
                    0,
                    getString(localesR.string.quick_record_action),
                    quickRecordPendingIntent,
                )
            }

            val notificationManager = NotificationManagerCompat.from(this)
            @Suppress("MissingPermission")
            notificationManager.notify(MOOD_REMINDER_NOTIFICATION_ID, notification)
        }
}

/** Creates a notification for mood reminder */
private fun Context.createMoodReminderNotification(
    block: NotificationCompat.Builder.() -> Unit
): Notification {
    ensureNotificationChannelExists()
    return NotificationCompat.Builder(this, MOOD_REMINDER_NOTIFICATION_CHANNEL_ID)
        .apply(block)
        .build()
}

/** Ensures that a notification channel is present if applicable */
private fun Context.ensureNotificationChannelExists() {
    val channel =
        NotificationChannel(
                MOOD_REMINDER_NOTIFICATION_CHANNEL_ID,
                getString(localesR.string.daily_reminder),
                NotificationManager.IMPORTANCE_DEFAULT,
            )
            .apply { description = getString(localesR.string.daily_reminder_description) }
    NotificationManagerCompat.from(this).createNotificationChannel(channel)
}

private fun Context.moodRecordPendingIntent(): PendingIntent? =
    PendingIntent.getActivity(
        this,
        MOOD_REMINDER_NOTIFICATION_REQUEST_CODE,
        Intent().apply {
            action = Intent.ACTION_VIEW
            data = "$DEEP_LINK_SCHEME_AND_HOST/record".toUri()
            component = ComponentName(packageName, TARGET_ACTIVITY_NAME)
        },
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
    )
