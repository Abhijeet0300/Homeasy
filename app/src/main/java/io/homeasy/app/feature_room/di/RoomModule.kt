package io.homeasy.app.feature_room.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.homeasy.app.feature_room.data.RoomRepositoryImpl
import io.homeasy.app.feature_room.domain.repository.RoomRepository
import io.homeasy.app.feature_room.domain.usecase.AddDeviceToRoomUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {
    @Provides
    @Singleton
    fun provideRoomRepository() : RoomRepository = RoomRepositoryImpl()

    fun provideAddDeviceToRoomUseCase(
        repo : RoomRepository
    ) : AddDeviceToRoomUseCase = AddDeviceToRoomUseCase( repo = repo )
}