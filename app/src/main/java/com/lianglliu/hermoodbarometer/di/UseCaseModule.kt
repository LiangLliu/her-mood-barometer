package com.lianglliu.hermoodbarometer.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

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