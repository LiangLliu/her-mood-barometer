package com.lianglliu.hermoodbarometer.core.shortcuts

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.graphics.drawable.IconCompat
import androidx.core.net.toUri
import com.lianglliu.hermoodbarometer.core.util.Constants.DEEP_LINK_SCHEME_AND_HOST
import dagger.hilt.android.qualifiers.ApplicationContext
import com.lianglliu.hermoodbarometer.core.util.Constants.TARGET_ACTIVITY_NAME
import javax.inject.Inject
import com.lianglliu.hermoodbarometer.core.locales.R as localR

private const val DYNAMIC_RECORD_MOOD_SHORTCUT_ID = "dynamic_record_mood"
private const val DEEP_LINK_BASE_PATH = "$DEEP_LINK_SCHEME_AND_HOST/record"

internal class DynamicShortcutManager @Inject constructor(
    @ApplicationContext private val context: Context,
) : ShortcutManager {

    override fun addRecordMoodShortcut() {
        val shortcutInfo = ShortcutInfoCompat.Builder(
            context, DYNAMIC_RECORD_MOOD_SHORTCUT_ID
        )
            .setShortLabel(context.getString(localR.string.record_emotion))
            .setLongLabel(context.getString(localR.string.record_emotion))
            .setIcon(IconCompat.createWithResource(context, android.R.drawable.ic_menu_edit))
            .setIntent(
                Intent().apply {
                    action = Intent.ACTION_VIEW
                    data = DEEP_LINK_BASE_PATH.toUri()
                    component = ComponentName(
                        context.packageName,
                        TARGET_ACTIVITY_NAME,
                    )
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                }
            )
            .build()
        ShortcutManagerCompat.pushDynamicShortcut(context, shortcutInfo)
    }

    override fun removeShortcuts() = ShortcutManagerCompat.removeAllDynamicShortcuts(context)
}