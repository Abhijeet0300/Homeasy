package io.homeasy.app.feature_connection.domain.usecases.camera

import io.homeasy.app.feature_connection.domain.model.CameraDeviceActivationResult
import io.homeasy.app.feature_connection.domain.repository.CameraPairingRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CameraPairingUseCase @Inject constructor(
    private val repo : CameraPairingRepository
){
    operator fun invoke(ssid : String, password : String, homeId : Long, timeOutSec : Long = 100) : Flow<CameraDeviceActivationResult> =
        repo.startCameraQrPairing(
        ssid = ssid,
        password = password,
        homeId = homeId,
        timeOutSec = timeOutSec
    )
}