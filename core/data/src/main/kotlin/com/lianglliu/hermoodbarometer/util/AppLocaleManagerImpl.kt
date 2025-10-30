package com.lianglliu.hermoodbarometer.util

import android.app.LocaleManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.LocaleList
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.tracing.trace
import com.lianglliu.hermoodbarometer.core.network.AppDispatchers
import com.lianglliu.hermoodbarometer.core.network.Dispatcher
import com.lianglliu.hermoodbarometer.core.network.di.ApplicationScope
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Singleton

@Singleton
internal class AppLocaleManagerImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    @Dispatcher(AppDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
    @ApplicationScope private val appScope: CoroutineScope,
) : AppLocaleManager {

    // MutableSharedFlow to allow manual updates
    private val _currentLocale = MutableSharedFlow<String>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    init {
        // Initialize with current language code
        appScope.launch {
            _currentLocale.emit(getLanguageCode())
        }

        // Setup broadcast receiver for system locale changes
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                if (intent.action != Intent.ACTION_LOCALE_CHANGED) return
                appScope.launch {
                    _currentLocale.emit(getLanguageCode())
                }
            }
        }

        trace("AppLocaleBroadcastReceiver.register") {
            context.registerReceiver(receiver, IntentFilter(Intent.ACTION_LOCALE_CHANGED))
        }
    }

    override val currentLocale: SharedFlow<String> = _currentLocale
        .distinctUntilChanged()
        .shareIn(
            scope = appScope,
            started = SharingStarted.WhileSubscribed(5_000),
            replay = 1,
        )

    override fun updateLocale(languageCode: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.getSystemService(LocaleManager::class.java).applicationLocales =
                LocaleList.forLanguageTags(languageCode)
        } else {
            AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(languageCode))
        }

        // Immediately emit the new language code to update UI state
        appScope.launch {
            _currentLocale.emit(languageCode)
        }
    }

    private fun getLanguageCode(): String {
        val locale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.getSystemService(LocaleManager::class.java).applicationLocales.get(0)
        } else {
            AppCompatDelegate.getApplicationLocales().get(0)
        }
        return locale?.toLanguageTag() ?: "en"
    }
}