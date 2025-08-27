package io.homeasy.app.feature_onboarding.domain

import io.homeasy.app.R
import io.homeasy.app.feature_onboarding.data.OnBoardingItem

class GetOnBoardingItemsUseCase {
    operator fun invoke() : List<OnBoardingItem> {
        return listOf<OnBoardingItem>(
            OnBoardingItem(
                message = R.string.onboarding_first_message,
                picture = R.drawable.first_onboarding
            ),
            OnBoardingItem(
                message = R.string.onboarding_second_message,
                picture = R.drawable.second_onboarding
            ),
            OnBoardingItem(
                message = R.string.onboarding_third_message,
                picture = R.drawable.third_onboarding
            )
        )
    }
}