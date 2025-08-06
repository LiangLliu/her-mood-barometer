package com.lianglliu.hermoodbarometer.di

import android.content.Context
import com.lianglliu.hermoodbarometer.data.notification.NotificationManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * 通知模块
 * 提供通知相关的依赖注入
 */
@Module
@InstallIn(SingletonComponent::class)
object NotificationModule {
    
    /**
     * 提供通知管理器
     */
    @Provides
    @Singleton
    fun provideNotificationManager(
        @ApplicationContext context: Context
    ): NotificationManager {
        return NotificationManager(context)
    }
} 