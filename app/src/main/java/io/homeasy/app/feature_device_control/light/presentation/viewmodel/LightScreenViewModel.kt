package io.homeasy.app.feature_device_control.light.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.homeasy.app.feature_device_control.light.domain.usecase.SetBrightnessUseCase
import io.homeasy.app.feature_device_control.light.domain.usecase.ToggleLightsUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LightScreenViewModel @Inject constructor(
    private val toggleLightsUseCase: ToggleLightsUseCase,
    private val setBrightnessUseCase: SetBrightnessUseCase
) : ViewModel() {
    fun toggleLights(id : String, turnOn : Boolean) {
        viewModelScope.launch {
            toggleLightsUseCase(id = id, turnOn = turnOn)
        }
    }
}