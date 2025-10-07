package io.homeasy.app.feature_device_control.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.homeasy.app.feature_device_control.data.LightRepositoryImpl
import io.homeasy.app.feature_device_control.domain.repository.LightRepository
import io.homeasy.app.feature_device_control.domain.usecase.SetBrightnessUseCase
import io.homeasy.app.feature_device_control.domain.usecase.ToggleLightsUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LightModule {
    @Provides
    @Singleton
    fun provideLightRepositoryInstance() : LightRepository = LightRepositoryImpl()

    @Provides
    @Singleton
    fun provideSetBrightnessUseCase(repo : LightRepository) : SetBrightnessUseCase = SetBrightnessUseCase(lightRepository = repo)

    @Provides
    @Singleton
    fun provideToggleLightUseCase(repo : LightRepository) : ToggleLightsUseCase =
        ToggleLightsUseCase(lightRepository = repo)
}