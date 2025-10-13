package io.homeasy.app.feature_connection.domain.model

import com.thingclips.smart.sdk.bean.DeviceBean

enum class DeviceType {
    LIGHT,
    CAMERA
}

fun DeviceBean.toDeviceType(): DeviceType? {
    val name = this.name?.lowercase() ?: ""

    return when {
        name.contains("bulb") -> DeviceType.LIGHT
        name.contains("camera") -> DeviceType.CAMERA
        else -> null
    }
}