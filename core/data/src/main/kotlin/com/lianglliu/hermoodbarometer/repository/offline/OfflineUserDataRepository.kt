package com.lianglliu.hermoodbarometer.repository.offline

import com.lianglliu.hermoodbarometer.core.datastore.CsPreferencesDataSource
import com.lianglliu.hermoodbarometer.core.model.data.DarkThemeConfig
import com.lianglliu.hermoodbarometer.core.model.data.UserData
import com.lianglliu.hermoodbarometer.core.shortcuts.ShortcutManager
import com.lianglliu.hermoodbarometer.repository.UserDataRepository
import com.lianglliu.hermoodbarometer.util.DatabaseTransferManager
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class OfflineUserDataRepository @Inject constructor(
    private val csPreferencesDataSource: CsPreferencesDataSource,
    private val shortcutManager: ShortcutManager,
    private val databaseTransferManager: DatabaseTransferManager,
) : UserDataRepository {

    override val userData: Flow<UserData> = csPreferencesDataSource.userData

    override suspend fun setDarkThemeConfig(darkThemeConfig: DarkThemeConfig) =
        csPreferencesDataSource.setDarkThemeConfig(darkThemeConfig)

    override suspend fun setDynamicColorPreference(useDynamicColor: Boolean) =
        csPreferencesDataSource.setDynamicColorPreference(useDynamicColor)

}