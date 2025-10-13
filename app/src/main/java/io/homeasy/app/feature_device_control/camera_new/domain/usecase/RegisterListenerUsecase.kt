package io.homeasy.app.feature_device_control.camera_new.domain.usecase

import com.thingclips.smart.camera.camerasdk.thingplayer.callback.AbsP2pCameraListener
import io.homeasy.app.feature_device_control.camera_new.domain.repository.CameraRepo
import javax.inject.Inject

class RegisterListenerUsecase @Inject constructor(
    private val repo : CameraRepo
){
    operator fun invoke(listener : AbsP2pCameraListener) = repo.registerListener(listener)
}