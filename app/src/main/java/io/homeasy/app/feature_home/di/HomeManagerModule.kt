package io.homeasy.app.feature_home.di

import com.thingclips.smart.home.sdk.ThingHomeSdk
import com.thingclips.smart.home.sdk.api.IThingHomeManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.homeasy.app.feature_home.data.HomeRepositoryImpl
import io.homeasy.app.feature_home.domain.usecase.CreateHomeUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HomeManagerModule {
    @Provides
    @Singleton
    fun provideHomeManagerInstance() : IThingHomeManager {
        return ThingHomeSdk.getHomeManagerInstance()
    }

    @Provides
    @Singleton
    fun provideHomeRepositoryImplInstance(
        homeManager : IThingHomeManager
    ) : HomeRepositoryImpl = HomeRepositoryImpl(homeManager)

    @Provides
    @Singleton
    fun provideHomeRepoUseCaseInstance(
        homeRepositoryImpl: HomeRepositoryImpl
    ) : CreateHomeUseCase = CreateHomeUseCase(homeRepositoryImpl)

}