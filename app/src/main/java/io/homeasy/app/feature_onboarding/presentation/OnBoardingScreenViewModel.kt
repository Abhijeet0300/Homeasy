package io.homeasy.app.feature_onboarding.presentation

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.homeasy.app.feature_onboarding.data.OnBoardingItem
import io.homeasy.app.feature_onboarding.domain.GetOnBoardingItemsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class OnBoardingScreenViewModel @Inject constructor(
    private val getOnBoardingItemsUseCase: GetOnBoardingItemsUseCase
)  : ViewModel() {
    private val items = getOnBoardingItemsUseCase()

    private val _currentIndex = MutableStateFlow(0)
    val currentIndex = _currentIndex.asStateFlow()

    val currentItem : OnBoardingItem get() = items[_currentIndex.value]

    fun next() {
        if(_currentIndex.value < items.lastIndex) {
            _currentIndex.value++
        }
    }
}