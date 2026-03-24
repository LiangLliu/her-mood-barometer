package com.lianglliu.hermoodbarometer.di

import com.lianglliu.hermoodbarometer.MoodDatabase
import com.lianglliu.hermoodbarometer.dao.EmotionDao
import com.lianglliu.hermoodbarometer.dao.EmotionRecordDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DaosModule {

    /** 提供情绪记录DAO */
    @Provides
    @Singleton
    fun provideEmotionRecordDao(database: MoodDatabase): EmotionRecordDao {
        return database.emotionRecordDao()
    }

    /** 提供统一情绪DAO */
    @Provides
    @Singleton
    fun provideEmotionDao(database: MoodDatabase): EmotionDao {
        return database.emotionDao()
    }
}
