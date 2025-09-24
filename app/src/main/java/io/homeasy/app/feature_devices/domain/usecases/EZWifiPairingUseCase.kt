package io.homeasy.app.feature_devices.domain.usecases

import io.homeasy.app.feature_devices.domain.model.DeviceActivationResult
import io.homeasy.app.feature_devices.domain.repository.EZConnectRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class EZWifiPairingUseCase @Inject constructor(
    private val repo : EZConnectRepository
) {
    operator fun invoke(
        ssid : String,
        password : String,
        homeId : Long,
        token : String,
        timeOutSec : Int = 120
    ) : Flow<DeviceActivationResult> = repo.ezPairing(
        ssid = ssid,
        password = password,
        homeId = homeId,
        token = token,
        timeOut = timeOutSec
    )
}