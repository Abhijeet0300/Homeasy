package io.homeasy.app.feature_home.domain.model

import com.thingclips.smart.sdk.bean.DeviceBean

sealed class ActivatorResult {
    data class Success(val device : DeviceBean) : ActivatorResult()
    data class Failure(val errorCode : String, val errorMessage : String) : ActivatorResult()
}