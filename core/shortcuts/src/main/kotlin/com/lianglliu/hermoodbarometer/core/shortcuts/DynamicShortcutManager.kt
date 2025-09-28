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
import com.lianglliu.hermoodbarometer.core.util.Constants.TRANSACTION_PATH
import javax.inject.Inject
import com.lianglliu.hermoodbarometer.core.locales.R as localR

private const val DYNAMIC_TRANSACTION_SHORTCUT_ID = "dynamic_new_transaction"
private const val DEEP_LINK_BASE_PATH = "$DEEP_LINK_SCHEME_AND_HOST/$TRANSACTION_PATH"

internal class DynamicShortcutManager @Inject constructor(
    @ApplicationContext private val context: Context,
) : ShortcutManager {

    override fun addTransactionShortcut(walletId: String) {
        val shortcutInfo = ShortcutInfoCompat.Builder(
            context, DYNAMIC_TRANSACTION_SHORTCUT_ID
        )
            .setShortLabel(context.getString(localR.string.new_transaction))
            .setLongLabel(context.getString(localR.string.transaction_shortcut_long_label))
            .setIcon(IconCompat.createWithResource(context, R.drawable.ic_shortcut_receipt_long))
            .setIntent(
                Intent().apply {
                    action = Intent.ACTION_VIEW
                    data = "${DEEP_LINK_BASE_PATH}/$walletId/null/false".toUri()
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