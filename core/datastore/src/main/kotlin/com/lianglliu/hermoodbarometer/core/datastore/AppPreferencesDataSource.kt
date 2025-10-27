package com.lianglliu.hermoodbarometer.core.datastore

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import com.lianglliu.hermoodbarometer.core.model.data.DarkThemeConfig
import com.lianglliu.hermoodbarometer.core.model.data.DarkThemeConfig.DARK
import com.lianglliu.hermoodbarometer.core.model.data.DarkThemeConfig.FOLLOW_SYSTEM
import com.lianglliu.hermoodbarometer.core.model.data.DarkThemeConfig.LIGHT
import com.lianglliu.hermoodbarometer.core.model.data.UserData
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AppPreferencesDataSource @Inject constructor(
    private val userPreferences: DataStore<UserPreferences>,
) {
    val userData = userPreferences.data
        .catch {
            Log.e(TAG, "Failed to read user preferences.", it)
            emit(getCustomInstance())
        }
        .map {
            UserData(
                darkThemeConfig = when (it.darkThemeConfig) {
                    null,
                    DarkThemeConfigProto.DARK_THEME_CONFIG_UNSPECIFIED,
                    DarkThemeConfigProto.UNRECOGNIZED,
                    DarkThemeConfigProto.DARK_THEME_CONFIG_FOLLOW_SYSTEM,
                        -> FOLLOW_SYSTEM

                    DarkThemeConfigProto.DARK_THEME_CONFIG_LIGHT -> LIGHT
                    DarkThemeConfigProto.DARK_THEME_CONFIG_DARK -> DARK
                },
                useDynamicColor = it.useDynamicColor,
                reminderStatus = it.reminderStatus,
                reminderTime = it.reminderTime.ifEmpty { "09:00" },
            )
        }

    suspend fun setDynamicColorPreference(useDynamicColor: Boolean) {
        try {
            userPreferences.updateData {
                it.copy { this.useDynamicColor = useDynamicColor }
            }
        } catch (e: IOException) {
            Log.e(TAG, "Failed to update dynamic color.", e)
        }
    }

    suspend fun setDarkThemeConfig(darkThemeConfig: DarkThemeConfig) {
        try {
            userPreferences.updateData {
                it.copy {
                    this.darkThemeConfig = when (darkThemeConfig) {
                        FOLLOW_SYSTEM -> DarkThemeConfigProto.DARK_THEME_CONFIG_FOLLOW_SYSTEM
                        LIGHT -> DarkThemeConfigProto.DARK_THEME_CONFIG_LIGHT
                        DARK -> DarkThemeConfigProto.DARK_THEME_CONFIG_DARK
                    }
                }
            }
        } catch (e: IOException) {
            Log.e(TAG, "Failed to update theme config.", e)
        }
    }

    suspend fun setReminderStatus(reminderStatus: Boolean) {
        try {
            userPreferences.updateData {
                it.copy { this.reminderStatus = reminderStatus }
            }
        } catch (e: IOException) {
            Log.e(TAG, "Failed to update dynamic color.", e)
        }
    }

    suspend fun setReminderTime(reminderTime: String) {
        try {
            userPreferences.updateData {
                it.copy { this.reminderTime = reminderTime }
            }
        } catch (e: IOException) {
            Log.e(TAG, "Failed to update reminder time.", e)
        }
    }
}

private const val TAG = "HerMoodBarometerPreferencesDataSource"