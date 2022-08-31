package com.example.weatherapp.di

import com.example.weatherapp.data.DataRepository
import com.example.weatherapp.data.DataRepositorySource
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
abstract class DataModule {

    @Binds
    @Singleton
    abstract fun provideDataRepository(dataRepository: DataRepository): DataRepositorySource
}