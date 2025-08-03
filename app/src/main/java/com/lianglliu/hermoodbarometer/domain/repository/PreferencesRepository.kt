package com.lianglliu.hermoodbarometer.domain.repository

import kotlinx.coroutines.flow.Flow

/**
 * 偏好设置仓库接口
 * 定义应用设置相关的数据操作契约
 */
interface PreferencesRepository {
    
    /**
     * 获取当前语言设置
     */
    fun getLanguage(): Flow<String>
    
    /**
     * 设置语言
     */
    suspend fun setLanguage(language: String)
    
    /**
     * 获取主题设置
     */
    fun getTheme(): Flow<String>
    
    /**
     * 设置主题
     */
    suspend fun setTheme(theme: String)
    
    /**
     * 获取每日提醒开关
     */
    fun getDailyReminderEnabled(): Flow<Boolean>
    
    /**
     * 设置每日提醒开关
     */
    suspend fun setDailyReminderEnabled(enabled: Boolean)
    
    /**
     * 获取提醒开关状态（别名方法）
     */
    fun isReminderEnabled(): Flow<Boolean> = getDailyReminderEnabled()
    
    /**
     * 设置提醒开关状态（别名方法）
     */
    suspend fun setReminderEnabled(enabled: Boolean) = setDailyReminderEnabled(enabled)
    
    /**
     * 获取提醒时间
     */
    fun getReminderTime(): Flow<String>
    
    /**
     * 设置提醒时间
     */
    suspend fun setReminderTime(time: String)
    
    /**
     * 获取是否首次启动
     */
    fun isFirstLaunch(): Flow<Boolean>
    
    /**
     * 设置首次启动状态
     */
    suspend fun setFirstLaunch(isFirst: Boolean)
    
    /**
     * 清除所有设置
     */
    suspend fun clearAllPreferences()
}