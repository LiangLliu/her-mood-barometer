package com.lianglliu.hermoodbarometer.di

import android.content.Context
import androidx.room.Room
import com.lianglliu.hermoodbarometer.MoodDatabase
import com.lianglliu.hermoodbarometer.MoodDatabase.Companion.DATABASE_NAME
import com.lianglliu.hermoodbarometer.util.DatabaseTransferManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

/**
 * 数据库依赖注入模块
 * 提供数据库相关的依赖
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideMoodDatabase(
        @ApplicationContext context: Context,
    ): MoodDatabase = Room.databaseBuilder(
        context,
        MoodDatabase::class.java,
        DATABASE_NAME,
    ).build()

    @Provides
    fun providesDatabaseTransferManager(
        @ApplicationContext context: Context,
        database: MoodDatabase,
    ) = DatabaseTransferManager(
        context = context,
        databaseOpenHelper = database.openHelper,
    )
}