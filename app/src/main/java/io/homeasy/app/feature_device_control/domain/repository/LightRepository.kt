package io.homeasy.app.feature_device_control.domain.repository

interface LightRepository {
    suspend fun toggleLights(devId : String, turnOn : Boolean)
    suspend fun setBrightness(devId : String, brightness : Int)

}