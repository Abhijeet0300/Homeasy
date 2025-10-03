package io.homeasy.app.feature_connection.domain.model

import android.graphics.Bitmap
import android.graphics.Camera
import com.thingclips.smart.sdk.bean.DeviceBean

sealed class CameraDeviceActivationResult {
    data class QrCode(val url : String, val bitmap : Bitmap) : CameraDeviceActivationResult()
    data class Success(val device : DeviceBean) : CameraDeviceActivationResult()
    data class Failure(val code : String? ,  val message : String?) : CameraDeviceActivationResult()
    data class Step(val step : String, val data : Any?) : CameraDeviceActivationResult()
}