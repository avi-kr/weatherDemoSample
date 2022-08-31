package com.example.weatherapp.di

import com.example.weatherapp.data.error.mapper.ErrorMapper
import com.example.weatherapp.data.error.mapper.ErrorMapperSource
import com.example.weatherapp.usecase.errors.ErrorManager
import com.example.weatherapp.usecase.errors.ErrorUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Created by Abhishek Kumar on 30,August,2022
 * (c)2022 VMock, India. All rights reserved.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class ErrorModule {

    @Binds
    @Singleton
    abstract fun provideErrorFactoryImpl(errorManager: ErrorManager): ErrorUseCase

    @Binds
    @Singleton
    abstract fun provideErrorMapper(errorMapper: ErrorMapper): ErrorMapperSource
}