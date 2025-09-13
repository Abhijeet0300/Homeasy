package io.homeasy.app.feature_home.di

import com.thingclips.smart.home.sdk.ThingHomeSdk
import com.thingclips.smart.home.sdk.api.IThingHomeManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.homeasy.app.feature_home.data.HomeRepositoryImpl
import io.homeasy.app.feature_home.domain.repository.HomeRepository
import io.homeasy.app.feature_home.domain.usecase.AddRoomUseCase
import io.homeasy.app.feature_home.domain.usecase.CreateHomeUseCase
import io.homeasy.app.feature_home.domain.usecase.QueryHomeListUseCase
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
    fun provideHomeRepository(
        homeManager : IThingHomeManager
    ) : HomeRepository = HomeRepositoryImpl(homeManager)

    @Provides
    @Singleton
    fun provideHomeRepoUseCaseInstance(
        homeRepository: HomeRepository
    ) : CreateHomeUseCase = CreateHomeUseCase(homeRepository)

    @Provides
    fun provideAddRoomUseCase(
        homeRepository : HomeRepository
    ) : AddRoomUseCase = AddRoomUseCase(homeRepository = homeRepository)

    @Provides
    fun provideQueryHomeListUseCase(
        homeRepository: HomeRepository
    ) : QueryHomeListUseCase = QueryHomeListUseCase(homeRepository = homeRepository)
}