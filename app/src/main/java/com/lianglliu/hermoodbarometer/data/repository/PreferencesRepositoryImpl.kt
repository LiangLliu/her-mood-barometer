package com.lianglliu.hermoodbarometer.data.repository

import com.lianglliu.hermoodbarometer.data.preferences.PreferencesManager
import com.lianglliu.hermoodbarometer.domain.repository.PreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalTime
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 偏好设置仓库实现
 * 实现应用设置的数据访问和业务逻辑
 */
@Singleton
class PreferencesRepositoryImpl @Inject constructor(
    private val preferencesManager: PreferencesManager
) : PreferencesRepository {

    override fun getLanguage(): Flow<String> = preferencesManager.language

    override fun getTheme(): Flow<String> = preferencesManager.theme

    override fun getDailyReminderEnabled(): Flow<Boolean> = preferencesManager.isReminderEnabled

    override fun getReminderTime(): Flow<String> =
        preferencesManager.reminderTime.map { time -> time.toString() }

    override fun isFirstLaunch(): Flow<Boolean> = preferencesManager.isFirstLaunch

    override suspend fun setLanguage(language: String) {
        preferencesManager.setLanguage(language)
    }

    override suspend fun setTheme(theme: String) {
        preferencesManager.setTheme(theme)
    }

    override suspend fun setDailyReminderEnabled(enabled: Boolean) {
        preferencesManager.setReminderEnabled(enabled)
    }

    override suspend fun setReminderTime(time: String) {
        val localTime = LocalTime.parse(time)
        preferencesManager.setReminderTime(localTime)
    }

    override suspend fun setFirstLaunch(isFirst: Boolean) {
        preferencesManager.setFirstLaunch(isFirst)
    }

    override suspend fun clearAllPreferences() {
        preferencesManager.clearAllPreferences()
    }
} 