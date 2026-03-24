package com.lianglliu.hermoodbarometer.repository.offline

import com.lianglliu.hermoodbarometer.core.datastore.AppPreferencesDataSource
import com.lianglliu.hermoodbarometer.core.model.data.ColorSchemeConfig
import com.lianglliu.hermoodbarometer.core.model.data.DarkThemeConfig
import com.lianglliu.hermoodbarometer.core.model.data.UserData
import com.lianglliu.hermoodbarometer.repository.UserDataRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

internal class OfflineUserDataRepository
@Inject
constructor(private val appPreferencesDataSource: AppPreferencesDataSource) : UserDataRepository {

    override val userData: Flow<UserData> = appPreferencesDataSource.userData

    override suspend fun setDarkThemeConfig(darkThemeConfig: DarkThemeConfig) =
        appPreferencesDataSource.setDarkThemeConfig(darkThemeConfig)

    override suspend fun setColorSchemeConfig(colorSchemeConfig: ColorSchemeConfig) =
        appPreferencesDataSource.setColorSchemeConfig(colorSchemeConfig)

    override suspend fun setReminderStatus(reminderStatus: Boolean) {
        appPreferencesDataSource.setReminderStatus(reminderStatus)
    }

    override suspend fun setReminderTime(reminderTime: String) {
        appPreferencesDataSource.setReminderTime(reminderTime)
    }
}
