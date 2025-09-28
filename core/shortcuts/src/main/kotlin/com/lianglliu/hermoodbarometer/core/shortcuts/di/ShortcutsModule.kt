package com.lianglliu.hermoodbarometer.core.shortcuts.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import com.lianglliu.hermoodbarometer.core.shortcuts.DynamicShortcutManager
import com.lianglliu.hermoodbarometer.core.shortcuts.ShortcutManager

@Module
@InstallIn(SingletonComponent::class)
internal abstract class ShortcutsModule {

    @Binds
    internal abstract fun bindsShortcutManager(
        shortcutManager: DynamicShortcutManager,
    ): ShortcutManager
}
