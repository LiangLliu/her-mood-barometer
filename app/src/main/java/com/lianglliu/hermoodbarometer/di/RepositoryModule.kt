package com.lianglliu.hermoodbarometer.di

import android.content.Context
import com.lianglliu.hermoodbarometer.data.preferences.PreferencesManager
import com.lianglliu.hermoodbarometer.data.repository.CustomEmotionRepositoryImpl
import com.lianglliu.hermoodbarometer.data.repository.EmotionRepositoryImpl
import com.lianglliu.hermoodbarometer.data.repository.PreferencesRepositoryImpl
import com.lianglliu.hermoodbarometer.domain.repository.CustomEmotionRepository
import com.lianglliu.hermoodbarometer.domain.repository.EmotionRepository
import com.lianglliu.hermoodbarometer.domain.repository.PreferencesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * 仓库依赖注入模块
 * 提供Repository相关的依赖
 */
@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    /**
     * 提供偏好设置管理器
     */
    @Provides
    @Singleton
    fun providePreferencesManager(
        @ApplicationContext context: Context
    ): PreferencesManager {
        return PreferencesManager(context)
    }

    /**
     * 提供情绪记录仓库
     */
    @Provides
    @Singleton
    fun provideEmotionRepository(
        emotionRepositoryImpl: EmotionRepositoryImpl
    ): EmotionRepository {
        return emotionRepositoryImpl
    }

    /**
     * 提供自定义情绪仓库
     */
    @Provides
    @Singleton
    fun provideCustomEmotionRepository(
        customEmotionRepositoryImpl: CustomEmotionRepositoryImpl
    ): CustomEmotionRepository {
        return customEmotionRepositoryImpl
    }

    /**
     * 提供偏好设置仓库
     */
    @Provides
    @Singleton
    fun providePreferencesRepository(
        preferencesRepositoryImpl: PreferencesRepositoryImpl
    ): PreferencesRepository {
        return preferencesRepositoryImpl
    }
} 