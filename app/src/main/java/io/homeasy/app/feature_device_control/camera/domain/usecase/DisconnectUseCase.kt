package io.homeasy.app.feature_device_control.camera.domain.usecase

import io.homeasy.app.feature_device_control.camera.domain.repository.CameraP2PRepository
import javax.inject.Inject

class DisconnectUseCase @Inject constructor(
    private val repo : CameraP2PRepository
) {
    suspend operator fun invoke(devId : String) : Result<Unit> = repo.disconnect(devId)
}