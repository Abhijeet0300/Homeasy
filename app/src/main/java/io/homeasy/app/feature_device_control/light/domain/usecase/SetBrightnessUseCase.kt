package io.homeasy.app.feature_device_control.light.domain.usecase

import io.homeasy.app.feature_device_control.light.domain.repository.LightRepository
import javax.inject.Inject

class SetBrightnessUseCase @Inject constructor(
    private val lightRepository: LightRepository
) {
    suspend operator fun invoke(id : String, brightness : Int) = lightRepository.setBrightness(devId = id, brightness = brightness)
}