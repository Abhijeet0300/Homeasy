package io.homeasy.app.feature_device_control.camera_new.di

import android.content.Context
import com.thingclips.smart.android.camera.sdk.ThingIPCSdk
import com.thingclips.smart.android.camera.sdk.api.IThingIPCCore
import com.thingclips.smart.camera.middleware.p2p.IThingSmartCameraP2P
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.homeasy.app.feature_device_control.camera_new.data.CameraRepoImpl
import io.homeasy.app.feature_device_control.camera_new.domain.repository.CameraRepo
import io.homeasy.app.feature_device_control.camera_new.domain.usecase.ConnectCameraUsecase
import io.homeasy.app.feature_device_control.camera_new.domain.usecase.CreateCameraP2PUsecase
import io.homeasy.app.feature_device_control.camera_new.domain.usecase.DestroyConnectionUseCase
import io.homeasy.app.feature_device_control.camera_new.domain.usecase.GenerateCameraView
import io.homeasy.app.feature_device_control.camera_new.domain.usecase.RegisterListenerUsecase
import io.homeasy.app.feature_device_control.camera_new.domain.usecase.StartPreviewUsecase
import io.homeasy.app.feature_device_control.camera_new.domain.usecase.StartRecordingUsecase
import io.homeasy.app.feature_device_control.camera_new.domain.usecase.StopPreviewUsecase
import io.homeasy.app.feature_device_control.camera_new.domain.usecase.StopRecordingUsecase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CameraModule {
    @Provides
    @Singleton
    fun getCameraInstance() : IThingIPCCore = ThingIPCSdk.getCameraInstance()

    @Provides
    @Singleton
    fun getCameraRepo(
        cameraInstance : IThingIPCCore,
        @ApplicationContext context : Context
    ) : CameraRepo = CameraRepoImpl(
        cameraInstance = cameraInstance,
        context = context
    )

    @Provides
    @Singleton
    fun getCameraP2PUseCase(
        repo : CameraRepo
    ) : CreateCameraP2PUsecase = CreateCameraP2PUsecase(repo = repo)

    @Provides
    @Singleton
    fun getStartRecordingUsecase(repo : CameraRepo) : StartRecordingUsecase = StartRecordingUsecase(repo = repo)

    @Provides
    @Singleton
    fun getStopRecordingUsecase(repo : CameraRepo) : StopRecordingUsecase = StopRecordingUsecase(repo = repo)

    @Provides
    @Singleton
    fun provideGenerateCameraViewInstance(repo : CameraRepo) : GenerateCameraView =
        GenerateCameraView(repo = repo)

    @Provides
    @Singleton
    fun provideConnectCameraUsecase(repo : CameraRepo) : ConnectCameraUsecase =
        ConnectCameraUsecase(repo = repo)

    @Provides
    @Singleton
    fun provideRegisterListenerUsecase(repo : CameraRepo) : RegisterListenerUsecase =
        RegisterListenerUsecase(repo = repo)

    @Provides
    @Singleton
    fun provideStartPreviewUsecase(repo : CameraRepo) : StartPreviewUsecase = StartPreviewUsecase(repo = repo)

    @Provides
    @Singleton
    fun provideStopPreviewUsecase(repo : CameraRepo) : StopPreviewUsecase = StopPreviewUsecase(repo = repo)

    @Provides
    @Singleton
    fun provideDestroyUsecase(repo : CameraRepo) : DestroyConnectionUseCase =
        DestroyConnectionUseCase(repo = repo)
}