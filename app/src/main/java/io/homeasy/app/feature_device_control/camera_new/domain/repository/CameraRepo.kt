package io.homeasy.app.feature_device_control.camera_new.domain.repository

import android.content.Context
import com.thingclips.smart.camera.camerasdk.thingplayer.callback.AbsP2pCameraListener
import com.thingclips.smart.camera.camerasdk.thingplayer.callback.OperationDelegateCallBack

interface CameraRepo {

    fun createCameraP2P(devId : String) : Boolean

    suspend fun startRecording(
        folderPath: String,
        context: Context,
        callback: OperationDelegateCallBack
    ): Result<Unit>

    suspend fun stopRecording(callback: OperationDelegateCallBack): Result<Unit>

    fun generateCameraView(view : Any)

    fun registerListener(listener : AbsP2pCameraListener)

    suspend fun connect(devId : String) : Result<Boolean>

    suspend fun startPreview() : Result<String>

    suspend fun stopPreview() : Result<String>

    fun destroy()
}