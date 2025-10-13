package io.homeasy.app.feature_device_control.camera_new.domain.usecase

import com.thingclips.smart.camera.camerasdk.thingplayer.callback.OperationDelegateCallBack
import io.homeasy.app.feature_device_control.camera_new.domain.repository.CameraRepo
import javax.inject.Inject

class StopRecordingUsecase @Inject constructor(
    private val repo : CameraRepo
) {
    suspend operator fun invoke(callback: OperationDelegateCallBack) = repo.stopRecording(callback)
}