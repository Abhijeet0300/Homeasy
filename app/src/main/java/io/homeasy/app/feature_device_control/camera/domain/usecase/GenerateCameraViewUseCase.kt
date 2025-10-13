package io.homeasy.app.feature_device_control.camera.domain.usecase

import io.homeasy.app.feature_device_control.camera.domain.repository.CameraP2PRepository
import javax.inject.Inject

class GenerateCameraViewUseCase @Inject constructor(
    private val repo : CameraP2PRepository
) {
    operator fun invoke(renderedView : Any) = repo.generateCameraView(renderedView)
}