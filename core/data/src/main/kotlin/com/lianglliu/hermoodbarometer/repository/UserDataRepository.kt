package com.lianglliu.hermoodbarometer.repository

import com.lianglliu.hermoodbarometer.core.model.data.ColorSchemeConfig
import com.lianglliu.hermoodbarometer.core.model.data.DarkThemeConfig
import com.lianglliu.hermoodbarometer.core.model.data.UserData
import kotlinx.coroutines.flow.Flow

interface UserDataRepository {

    val userData: Flow<UserData>

    suspend fun setDarkThemeConfig(darkThemeConfig: DarkThemeConfig)

    suspend fun setColorSchemeConfig(colorSchemeConfig: ColorSchemeConfig)

    suspend fun setReminderStatus(reminderStatus: Boolean)

    suspend fun setReminderTime(reminderTime: String)
}
