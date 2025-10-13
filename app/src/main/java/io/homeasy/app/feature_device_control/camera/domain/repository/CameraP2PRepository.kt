package io.homeasy.app.feature_device_control.camera.domain.repository

import com.thingclips.smart.camera.camerasdk.thingplayer.callback.AbsP2pCameraListener

interface CameraP2PRepository {
    fun createP2P(devId: String): Boolean
    fun generateCameraView(renderedView: Any)
    fun registerP2PListener(listener: AbsP2pCameraListener)
    suspend fun connect(devId: String, mode: Int = 0): Result<Int> // returns sessionId
    suspend fun startPreview(clarity: Int = 0): Result<Unit>
    suspend fun stopPreview() : Result<Unit>
    suspend fun disconnect(devId: String): Result<Unit>
    fun destroy()
}


