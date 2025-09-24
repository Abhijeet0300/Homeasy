package io.homeasy.app.feature_devices.domain.repository

import com.thingclips.smart.mqttclient.mqttv3.internal.Token
import io.homeasy.app.feature_devices.domain.model.DeviceActivationResult
import kotlinx.coroutines.flow.Flow

interface EZConnectRepository {
    suspend fun getToken(homeId : Long) : Result<String>
    fun ezPairing(
        ssid : String,
        password : String,
        timeOut : Int = 100,
        token: String,
        homeId : Long
    ) : Flow<DeviceActivationResult>
}