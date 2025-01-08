package com.mayad7474.mazaady_task.core.di

import com.mayad7474.mazaady_task.data.dataSource.CategoriesAS
import com.mayad7474.mazaady_task.data.repository.CategoriesRepo
import com.mayad7474.mazaady_task.doamin.repository.ICategoriesRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {

    @Provides
    @Singleton
    fun provideCategoriesRepo(api: CategoriesAS): ICategoriesRepo = CategoriesRepo(api)
}