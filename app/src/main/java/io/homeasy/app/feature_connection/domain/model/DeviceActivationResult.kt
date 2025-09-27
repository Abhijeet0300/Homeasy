package io.homeasy.app.feature_connection.domain.model

import com.thingclips.smart.sdk.bean.DeviceBean

sealed class DeviceActivationResult {
    data class Success(val deviceBean: DeviceBean) : DeviceActivationResult()
    data class Failure(val errorCode: String, val errorMessage: String) : DeviceActivationResult()
    data class Step(val step: String, val data: Any?) : DeviceActivationResult()
}