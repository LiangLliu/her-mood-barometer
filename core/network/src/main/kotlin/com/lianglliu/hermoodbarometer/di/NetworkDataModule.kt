package com.lianglliu.hermoodbarometer.di

import com.lianglliu.hermoodbarometer.AppNetworkDataSource
import com.lianglliu.hermoodbarometer.ktor.KtorCsNetwork
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class NetworkDataModule {

    @Binds
    internal abstract fun bindsCsNetworkDataSource(
        csNetworkDataSource: KtorCsNetwork
    ): AppNetworkDataSource
}