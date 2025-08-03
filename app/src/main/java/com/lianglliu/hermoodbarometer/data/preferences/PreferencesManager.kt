package com.lianglliu.hermoodbarometer.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalTime

/**
 * DataStore偏好设置管理器
 * 管理应用的各种设置和偏好
 */
class PreferencesManager(private val context: Context) {
    
    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "mood_preferences")
        
        // 偏好设置键
        private val LANGUAGE_KEY = stringPreferencesKey("language")
        private val THEME_KEY = stringPreferencesKey("theme")
        private val REMINDER_ENABLED_KEY = booleanPreferencesKey("reminder_enabled")
        private val REMINDER_TIME_KEY = stringPreferencesKey("reminder_time")
        private val FIRST_LAUNCH_KEY = booleanPreferencesKey("first_launch")
        private val NOTIFICATION_ENABLED_KEY = booleanPreferencesKey("notification_enabled")
        private val CHART_TYPE_KEY = stringPreferencesKey("chart_type")
        private val TIME_RANGE_KEY = stringPreferencesKey("time_range")
    }
    
    /**
     * 语言设置
     */
    val language: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[LANGUAGE_KEY] ?: "zh"
    }
    
    /**
     * 主题设置
     */
    val theme: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[THEME_KEY] ?: "system"
    }
    
    /**
     * 提醒是否启用
     */
    val isReminderEnabled: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[REMINDER_ENABLED_KEY] ?: false
    }
    
    /**
     * 提醒时间
     */
    val reminderTime: Flow<LocalTime> = context.dataStore.data.map { preferences ->
        val timeString = preferences[REMINDER_TIME_KEY] ?: "20:00"
        LocalTime.parse(timeString)
    }
    
    /**
     * 是否首次启动
     */
    val isFirstLaunch: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[FIRST_LAUNCH_KEY] ?: true
    }
    
    /**
     * 通知是否启用
     */
    val isNotificationEnabled: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[NOTIFICATION_ENABLED_KEY] ?: true
    }
    
    /**
     * 默认图表类型
     */
    val defaultChartType: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[CHART_TYPE_KEY] ?: "bar"
    }
    
    /**
     * 默认时间范围
     */
    val defaultTimeRange: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[TIME_RANGE_KEY] ?: "week"
    }
    
    /**
     * 设置语言
     */
    suspend fun setLanguage(language: String) {
        context.dataStore.edit { preferences ->
            preferences[LANGUAGE_KEY] = language
        }
    }
    
    /**
     * 设置主题
     */
    suspend fun setTheme(theme: String) {
        context.dataStore.edit { preferences ->
            preferences[THEME_KEY] = theme
        }
    }
    
    /**
     * 设置提醒开关
     */
    suspend fun setReminderEnabled(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[REMINDER_ENABLED_KEY] = enabled
        }
    }
    
    /**
     * 设置提醒时间
     */
    suspend fun setReminderTime(time: LocalTime) {
        context.dataStore.edit { preferences ->
            preferences[REMINDER_TIME_KEY] = time.toString()
        }
    }
    
    /**
     * 设置首次启动标记
     */
    suspend fun setFirstLaunch(isFirst: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[FIRST_LAUNCH_KEY] = isFirst
        }
    }
    
    /**
     * 设置通知开关
     */
    suspend fun setNotificationEnabled(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[NOTIFICATION_ENABLED_KEY] = enabled
        }
    }
    
    /**
     * 设置默认图表类型
     */
    suspend fun setDefaultChartType(chartType: String) {
        context.dataStore.edit { preferences ->
            preferences[CHART_TYPE_KEY] = chartType
        }
    }
    
    /**
     * 设置默认时间范围
     */
    suspend fun setDefaultTimeRange(timeRange: String) {
        context.dataStore.edit { preferences ->
            preferences[TIME_RANGE_KEY] = timeRange
        }
    }
    
    /**
     * 清除所有设置
     */
    suspend fun clearAllPreferences() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }
} 