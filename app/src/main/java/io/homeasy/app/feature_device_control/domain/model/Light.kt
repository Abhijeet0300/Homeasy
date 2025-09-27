package io.homeasy.app.feature_device_control.domain.model

data class Light(
    val id : String,
    val name : String,
    val isOn : Boolean,
    val brightness : Int
)
