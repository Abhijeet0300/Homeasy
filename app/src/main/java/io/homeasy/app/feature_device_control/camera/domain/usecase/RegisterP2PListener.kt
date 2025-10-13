package io.homeasy.app.feature_device_control.camera.domain.usecase

import com.thingclips.smart.camera.camerasdk.thingplayer.callback.AbsP2pCameraListener
import io.homeasy.app.feature_device_control.camera.domain.repository.CameraP2PRepository
import javax.inject.Inject

class RegisterP2PListener @Inject constructor(
    private val repo: CameraP2PRepository
) {
    operator fun invoke(listener: AbsP2pCameraListener) = repo.registerP2PListener(listener)
}