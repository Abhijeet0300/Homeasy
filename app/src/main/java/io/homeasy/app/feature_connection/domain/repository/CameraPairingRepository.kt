package io.homeasy.app.feature_connection.domain.repository

import io.homeasy.app.feature_connection.domain.model.CameraDeviceActivationResult
import kotlinx.coroutines.flow.Flow

interface CameraPairingRepository {
    suspend fun getToken(homeId : Long) : Result<String>
    fun startCameraQrPairing(ssid : String, password : String, homeId : Long, timeOutSec : Long = 100) : Flow<CameraDeviceActivationResult>
}