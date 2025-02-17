package com.example.todayfortunebytdd.di

import com.example.data.repositoryImpl.TotalInfoRepositoryImpl
import com.example.domain.repository.TotalInfoRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun totalInfoRepository(repositoryImpl: TotalInfoRepositoryImpl): TotalInfoRepository
}