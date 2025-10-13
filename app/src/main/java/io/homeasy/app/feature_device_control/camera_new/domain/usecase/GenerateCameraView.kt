package io.homeasy.app.feature_device_control.camera_new.domain.usecase

import io.homeasy.app.feature_device_control.camera_new.domain.repository.CameraRepo
import javax.inject.Inject

class GenerateCameraView @Inject constructor(
    private val repo : CameraRepo
) {
    operator fun invoke(view : Any) = repo.generateCameraView(view)
}