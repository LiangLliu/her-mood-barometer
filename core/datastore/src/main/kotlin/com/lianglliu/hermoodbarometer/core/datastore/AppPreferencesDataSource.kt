package com.lianglliu.hermoodbarometer.core.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import com.lianglliu.hermoodbarometer.core.model.data.ColorSchemeConfig
import com.lianglliu.hermoodbarometer.core.model.data.DarkThemeConfig
import com.lianglliu.hermoodbarometer.core.model.data.DarkThemeConfig.DARK
import com.lianglliu.hermoodbarometer.core.model.data.DarkThemeConfig.FOLLOW_SYSTEM
import com.lianglliu.hermoodbarometer.core.model.data.DarkThemeConfig.LIGHT
import com.lianglliu.hermoodbarometer.core.model.data.UserData
import jakarta.inject.Inject
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import timber.log.Timber

class AppPreferencesDataSource
@Inject
constructor(private val userPreferences: DataStore<UserPreferences>) {
    val userData =
        userPreferences.data
            .catch {
                Timber.e(it, "Failed to read user preferences.")
                emit(getCustomInstance())
            }
            .map {
                UserData(
                    darkThemeConfig =
                        when (it.darkThemeConfig) {
                            null,
                            DarkThemeConfigProto.DARK_THEME_CONFIG_UNSPECIFIED,
                            DarkThemeConfigProto.UNRECOGNIZED,
                            DarkThemeConfigProto.DARK_THEME_CONFIG_FOLLOW_SYSTEM -> FOLLOW_SYSTEM

                            DarkThemeConfigProto.DARK_THEME_CONFIG_LIGHT -> LIGHT
                            DarkThemeConfigProto.DARK_THEME_CONFIG_DARK -> DARK
                        },
                    colorSchemeConfig =
                        when (it.colorSchemeConfig) {
                            null,
                            ColorSchemeConfigProto.COLOR_SCHEME_CONFIG_UNSPECIFIED,
                            ColorSchemeConfigProto.UNRECOGNIZED,
                            ColorSchemeConfigProto.COLOR_SCHEME_CONFIG_WARM ->
                                ColorSchemeConfig.WARM
                            ColorSchemeConfigProto.COLOR_SCHEME_CONFIG_OCEAN ->
                                ColorSchemeConfig.OCEAN
                            ColorSchemeConfigProto.COLOR_SCHEME_CONFIG_PETAL ->
                                ColorSchemeConfig.PETAL
                            ColorSchemeConfigProto.COLOR_SCHEME_CONFIG_DYNAMIC ->
                                ColorSchemeConfig.DYNAMIC
                        },
                    reminderStatus = it.reminderStatus,
                    reminderTime = it.reminderTime.ifEmpty { "09:00" },
                )
            }

    suspend fun setColorSchemeConfig(colorSchemeConfig: ColorSchemeConfig) {
        try {
            userPreferences.updateData {
                it.copy {
                    this.colorSchemeConfig =
                        when (colorSchemeConfig) {
                            ColorSchemeConfig.WARM ->
                                ColorSchemeConfigProto.COLOR_SCHEME_CONFIG_WARM
                            ColorSchemeConfig.OCEAN ->
                                ColorSchemeConfigProto.COLOR_SCHEME_CONFIG_OCEAN
                            ColorSchemeConfig.PETAL ->
                                ColorSchemeConfigProto.COLOR_SCHEME_CONFIG_PETAL
                            ColorSchemeConfig.DYNAMIC ->
                                ColorSchemeConfigProto.COLOR_SCHEME_CONFIG_DYNAMIC
                        }
                }
            }
        } catch (e: IOException) {
            Timber.e(e, "Failed to update color scheme config.")
        }
    }

    suspend fun setDarkThemeConfig(darkThemeConfig: DarkThemeConfig) {
        try {
            userPreferences.updateData {
                it.copy {
                    this.darkThemeConfig =
                        when (darkThemeConfig) {
                            FOLLOW_SYSTEM -> DarkThemeConfigProto.DARK_THEME_CONFIG_FOLLOW_SYSTEM
                            LIGHT -> DarkThemeConfigProto.DARK_THEME_CONFIG_LIGHT
                            DARK -> DarkThemeConfigProto.DARK_THEME_CONFIG_DARK
                        }
                }
            }
        } catch (e: IOException) {
            Timber.e(e, "Failed to update theme config.")
        }
    }

    suspend fun setReminderStatus(reminderStatus: Boolean) {
        try {
            userPreferences.updateData { it.copy { this.reminderStatus = reminderStatus } }
        } catch (e: IOException) {
            Timber.e(e, "Failed to update dynamic color.")
        }
    }

    suspend fun setReminderTime(reminderTime: String) {
        try {
            userPreferences.updateData { it.copy { this.reminderTime = reminderTime } }
        } catch (e: IOException) {
            Timber.e(e, "Failed to update reminder time.")
        }
    }
}
