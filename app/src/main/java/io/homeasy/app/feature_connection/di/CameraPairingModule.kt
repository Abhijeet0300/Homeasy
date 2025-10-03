package io.homeasy.app.feature_connection.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.homeasy.app.feature_connection.data.CameraPairingRepositoryImpl
import io.homeasy.app.feature_connection.domain.repository.CameraPairingRepository
import io.homeasy.app.feature_connection.domain.usecases.camera.CameraPairingUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CameraPairingModule {

    @Provides
    @Singleton
    fun provideCameraRepositoryInstance(@ApplicationContext context : Context) =
        CameraPairingRepositoryImpl(context = context)

    @Provides
    @Singleton
    fun provideCameraPairingUseCase(repo: CameraPairingRepository): CameraPairingUseCase =
        CameraPairingUseCase(repo = repo)
}