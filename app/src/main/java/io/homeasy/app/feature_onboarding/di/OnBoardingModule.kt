package io.homeasy.app.feature_onboarding.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import io.homeasy.app.feature_onboarding.domain.GetOnBoardingItemsUseCase

@Module
@InstallIn(ViewModelComponent::class)
object OnBoardingModule {
    @Provides
    fun provideGetOnBoardingItemsUseCase(): GetOnBoardingItemsUseCase {
        return GetOnBoardingItemsUseCase()
    }
}