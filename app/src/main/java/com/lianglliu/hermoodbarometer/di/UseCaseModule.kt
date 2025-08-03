package com.lianglliu.hermoodbarometer.di

import com.lianglliu.hermoodbarometer.domain.usecase.AddEmotionRecordUseCase
import com.lianglliu.hermoodbarometer.domain.usecase.GetEmotionRecordsUseCase
import com.lianglliu.hermoodbarometer.domain.usecase.GetEmotionStatisticsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * UseCase依赖注入模块
 * 提供业务用例相关的依赖
 */
@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {
    
    // UseCase类已经使用@Inject注解，Hilt会自动创建实例
    // 不需要额外的@Provides方法
} 