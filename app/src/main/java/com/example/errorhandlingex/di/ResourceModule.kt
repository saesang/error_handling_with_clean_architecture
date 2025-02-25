package com.example.errorhandlingex.di

import com.example.presentation.util.ResourceProvider
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ResourceModule {
    @Binds
    abstract fun bindResourceProvider(impl: AppResourceProvider): ResourceProvider
}