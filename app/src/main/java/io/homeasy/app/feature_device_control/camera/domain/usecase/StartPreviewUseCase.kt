package io.homeasy.app.feature_device_control.camera.domain.usecase

import io.homeasy.app.feature_device_control.camera.domain.repository.CameraP2PRepository
import javax.inject.Inject

class StartPreviewUseCase @Inject constructor(
    private val repo: CameraP2PRepository
){
    suspend operator fun invoke(clarity: Int = 0): Result<Unit> = repo.startPreview(clarity)
}