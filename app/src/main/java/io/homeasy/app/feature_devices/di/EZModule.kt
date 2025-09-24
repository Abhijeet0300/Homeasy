package io.homeasy.app.feature_devices.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.homeasy.app.feature_devices.data.EZConnectRepositoryImpl
import io.homeasy.app.feature_devices.domain.repository.EZConnectRepository
import io.homeasy.app.feature_devices.domain.usecases.EZTokenUseCase
import io.homeasy.app.feature_devices.domain.usecases.EZWifiPairingUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object EZModule {

    @Provides
    @Singleton
    fun provideEZConnectRepository(
        @ApplicationContext context : Context
    ) : EZConnectRepository = EZConnectRepositoryImpl(context)

    @Provides
    @Singleton
    fun provideEZWifiPairingUseCase(
        repo : EZConnectRepository
    ) : EZWifiPairingUseCase = EZWifiPairingUseCase(repo)

    @Provides
    @Singleton
    fun provideEZTokenUseCase(repo: EZConnectRepository) : EZTokenUseCase = EZTokenUseCase(repo)
}