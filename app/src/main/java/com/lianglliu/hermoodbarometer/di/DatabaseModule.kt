package com.lianglliu.hermoodbarometer.di

import android.content.Context
import androidx.room.Room
import com.lianglliu.hermoodbarometer.data.database.EmotionDao
import com.lianglliu.hermoodbarometer.data.database.EmotionRecordDao
import com.lianglliu.hermoodbarometer.data.database.MoodDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * 数据库依赖注入模块
 * 提供数据库相关的依赖
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    /**
     * 提供Room数据库实例
     */
    @Provides
    @Singleton
    fun provideMoodDatabase(
        @ApplicationContext context: Context
    ): MoodDatabase {
        return Room.databaseBuilder(
            context,
            MoodDatabase::class.java,
            MoodDatabase.DATABASE_NAME
        )
            // 开发期使用破坏性迁移
            .fallbackToDestructiveMigration()
            .build()
    }

    /**
     * 提供情绪记录DAO
     */
    @Provides
    @Singleton
    fun provideEmotionRecordDao(database: MoodDatabase): EmotionRecordDao {
        return database.emotionRecordDao()
    }

    /**
     * 提供统一情绪DAO
     */
    @Provides
    @Singleton
    fun provideEmotionDao(database: MoodDatabase): EmotionDao {
        return database.emotionDao()
    }
} 