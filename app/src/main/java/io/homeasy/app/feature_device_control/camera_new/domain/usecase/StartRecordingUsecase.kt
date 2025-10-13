package io.homeasy.app.feature_device_control.camera_new.domain.usecase

import android.content.Context
import com.thingclips.smart.camera.camerasdk.thingplayer.callback.OperationDelegateCallBack
import io.homeasy.app.feature_device_control.camera_new.domain.repository.CameraRepo
import javax.inject.Inject

class StartRecordingUsecase @Inject constructor(
    private val repo : CameraRepo
) {
    suspend operator fun invoke(folderPath: String, context: Context, callback: OperationDelegateCallBack)
    = repo.startRecording(folderPath, context, callback)
}