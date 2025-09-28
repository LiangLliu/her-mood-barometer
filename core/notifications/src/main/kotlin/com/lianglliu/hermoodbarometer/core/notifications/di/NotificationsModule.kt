package com.lianglliu.hermoodbarometer.core.notifications.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import com.lianglliu.hermoodbarometer.core.notifications.Notifier
import com.lianglliu.hermoodbarometer.core.notifications.SystemTrayNotifier

@Module
@InstallIn(SingletonComponent::class)
internal abstract class NotificationsModule {

    @Binds
    internal abstract fun bindsNotifier(
        notifier: SystemTrayNotifier,
    ): Notifier
}
