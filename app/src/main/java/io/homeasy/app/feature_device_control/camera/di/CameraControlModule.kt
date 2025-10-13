package io.homeasy.app.feature_device_control.camera.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.homeasy.app.feature_device_control.camera.data.CameraP2PRepositoryImpl
import io.homeasy.app.feature_device_control.camera.domain.repository.CameraP2PRepository
import io.homeasy.app.feature_device_control.camera.domain.usecase.ConnectUseCase
import io.homeasy.app.feature_device_control.camera.domain.usecase.CreateP2PUseCase
import io.homeasy.app.feature_device_control.camera.domain.usecase.DestroyUseCase
import io.homeasy.app.feature_device_control.camera.domain.usecase.DisconnectUseCase
import io.homeasy.app.feature_device_control.camera.domain.usecase.GenerateCameraViewUseCase
import io.homeasy.app.feature_device_control.camera.domain.usecase.RegisterP2PListener
import io.homeasy.app.feature_device_control.camera.domain.usecase.StartPreviewUseCase
import io.homeasy.app.feature_device_control.camera.domain.usecase.StopPreviewUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CameraControlModule {

    @Provides
    @Singleton
    fun provideCameraP2PRepository(
        @ApplicationContext context : Context
    ) : CameraP2PRepository = CameraP2PRepositoryImpl(context)

    @Provides
    fun provideCreateP2P(repo : CameraP2PRepository) : CreateP2PUseCase = CreateP2PUseCase(repo)

    @Provides
    fun provideGenerateCameraViewUseCase(repo : CameraP2PRepository) : GenerateCameraViewUseCase = GenerateCameraViewUseCase(repo)

    @Provides
    fun provideDestroyUseCase(repo : CameraP2PRepository) : DestroyUseCase = DestroyUseCase(repo)

    @Provides
    fun provideConnectUseCase(repo : CameraP2PRepository) : ConnectUseCase = ConnectUseCase(repo)

    @Provides
    fun provideStartPreviewUseCase(repo : CameraP2PRepository) : StartPreviewUseCase = StartPreviewUseCase(repo)

    @Provides
    fun provideStopPreviewUseCase(repo : CameraP2PRepository) : StopPreviewUseCase = StopPreviewUseCase(repo)

    @Provides
    fun provideDisconnectUseCase(repo : CameraP2PRepository) : DisconnectUseCase =
        DisconnectUseCase(repo)

    @Provides
    fun provideRegisterP2PListener(repo : CameraP2PRepository) : RegisterP2PListener =
        RegisterP2PListener(repo)

}