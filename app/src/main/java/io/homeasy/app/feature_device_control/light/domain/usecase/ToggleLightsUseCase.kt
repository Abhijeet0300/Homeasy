package io.homeasy.app.feature_device_control.light.domain.usecase

import io.homeasy.app.feature_device_control.light.domain.repository.LightRepository
import javax.inject.Inject

class ToggleLightsUseCase @Inject constructor(
    private val lightRepository: LightRepository
) {
    suspend operator fun invoke(id : String, turnOn : Boolean) = lightRepository.toggleLights(devId = id, turnOn = turnOn)
}