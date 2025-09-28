package com.lianglliu.hermoodbarometer.repository.offline

import com.lianglliu.hermoodbarometer.core.datastore.AppPreferencesDataSource
import com.lianglliu.hermoodbarometer.core.model.data.DarkThemeConfig
import com.lianglliu.hermoodbarometer.core.model.data.UserData
import com.lianglliu.hermoodbarometer.repository.UserDataRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class OfflineUserDataRepository @Inject constructor(
    private val appPreferencesDataSource: AppPreferencesDataSource
) : UserDataRepository {

    override val userData: Flow<UserData> = appPreferencesDataSource.userData

    override suspend fun setDarkThemeConfig(darkThemeConfig: DarkThemeConfig) =
        appPreferencesDataSource.setDarkThemeConfig(darkThemeConfig)

    override suspend fun setDynamicColorPreference(useDynamicColor: Boolean) =
        appPreferencesDataSource.setDynamicColorPreference(useDynamicColor)

    override suspend fun setReminderStatus(reminderStatus: Boolean) {
        appPreferencesDataSource.setReminderStatus(reminderStatus)
    }

}