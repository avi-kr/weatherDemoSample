package com.example.weatherapp.di

import com.example.weatherapp.TestDataRepository
import com.example.weatherapp.data.DataRepositorySource
import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DataModule::class]
)
abstract class TestDataModule {

    @Binds
    @Singleton
    abstract fun provideDataRepository(dataRepository: TestDataRepository): DataRepositorySource
}
