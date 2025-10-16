package io.homeasy.app.feature_device_control.camera_new.domain.usecase

import com.thingclips.smart.camera.middleware.p2p.IThingSmartCameraP2P
import io.homeasy.app.feature_device_control.camera_new.domain.repository.CameraRepo
import javax.inject.Inject

class CreateCameraP2PUsecase @Inject constructor(
    private val repo : CameraRepo
) {
    operator fun invoke(devId : String) : IThingSmartCameraP2P<Any?>? = repo.createCameraP2P(devId)
}