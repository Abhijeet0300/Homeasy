package io.homeasy.app.feature_device_control.camera_new.domain.usecase

import io.homeasy.app.feature_device_control.camera_new.domain.repository.CameraRepo
import javax.inject.Inject

class StopPreviewUsecase @Inject constructor(
    private val repo: CameraRepo
) {
    suspend operator fun invoke() : Result<String> = repo.stopPreview()
}