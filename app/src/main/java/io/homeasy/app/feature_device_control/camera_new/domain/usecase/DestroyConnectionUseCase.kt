package io.homeasy.app.feature_device_control.camera_new.domain.usecase

import io.homeasy.app.feature_device_control.camera_new.domain.repository.CameraRepo
import javax.inject.Inject

class DestroyConnectionUseCase @Inject constructor(
    private val repo : CameraRepo
) {
    operator fun invoke() = repo.destroy()
}