package io.homeasy.app.feature_connection.domain.repository

import io.homeasy.app.feature_connection.domain.model.DeviceActivationResult
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