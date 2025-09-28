package com.lianglliu.hermoodbarometer.di

import com.lianglliu.hermoodbarometer.repository.EmotionDefinitionRepository
import com.lianglliu.hermoodbarometer.repository.EmotionRepository
import com.lianglliu.hermoodbarometer.repository.UserDataRepository
import com.lianglliu.hermoodbarometer.repository.offline.EmotionRecordRepositoryImpl
import com.lianglliu.hermoodbarometer.repository.offline.EmotionRepositoryImpl
import com.lianglliu.hermoodbarometer.repository.offline.OfflineUserDataRepository
import com.lianglliu.hermoodbarometer.util.AppLocaleManager
import com.lianglliu.hermoodbarometer.util.AppLocaleManagerImpl
import com.lianglliu.hermoodbarometer.util.InAppReviewManager
import com.lianglliu.hermoodbarometer.util.InAppReviewManagerImpl
import com.lianglliu.hermoodbarometer.util.InAppUpdateManager
import com.lianglliu.hermoodbarometer.util.InAppUpdateManagerImpl
import com.lianglliu.hermoodbarometer.util.ReminderScheduler
import com.lianglliu.hermoodbarometer.util.ReminderSchedulerImpl
import com.lianglliu.hermoodbarometer.util.TimeZoneBroadcastMonitor
import com.lianglliu.hermoodbarometer.util.TimeZoneMonitor
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class DataModule {

    @Binds
    internal abstract fun provideEmotionRepository(
        emotionRecordRepositoryImpl: EmotionRecordRepositoryImpl
    ): EmotionRepository

    @Binds
    internal abstract fun provideEmotionDefinitionRepository(
        emotionRepositoryImpl: EmotionRepositoryImpl
    ): EmotionDefinitionRepository

    @Binds
    internal abstract fun bindsUserDataRepository(
        userDataRepository: OfflineUserDataRepository,
    ): UserDataRepository

    @Binds
    internal abstract fun bindsTimeZoneMonitor(
        timeZoneMonitor: TimeZoneBroadcastMonitor,
    ): TimeZoneMonitor

    @Binds
    internal abstract fun bindsNotificationAlarmScheduler(
        reminderScheduler: ReminderSchedulerImpl,
    ): ReminderScheduler

    @Binds
    internal abstract fun bindsInAppUpdateManager(
        inAppUpdateManager: InAppUpdateManagerImpl,
    ): InAppUpdateManager

    @Binds
    internal abstract fun bindsInAppReviewManager(
        inAppReviewManager: InAppReviewManagerImpl,
    ): InAppReviewManager

    @Binds
    internal abstract fun bindsAppLocaleManager(
        appLocaleManager: AppLocaleManagerImpl,
    ): AppLocaleManager
}