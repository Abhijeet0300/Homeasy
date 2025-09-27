package io.homeasy.app.feature_connection.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.homeasy.app.feature_connection.domain.model.DeviceActivationResult
import io.homeasy.app.feature_connection.domain.usecases.EZTokenUseCase
import io.homeasy.app.feature_connection.domain.usecases.EZWifiPairingUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EZConnectViewModel @Inject constructor(
    private val startEZPairingUseCase: EZWifiPairingUseCase,
    private val getEZTokenUseCase: EZTokenUseCase
) : ViewModel(){
    private val _isPairing = MutableStateFlow<Boolean>(false)
    val isPairing = _isPairing.asStateFlow()

    private val _pairingResult = MutableStateFlow<DeviceActivationResult?>(null)
    val pairingResult = _pairingResult.asStateFlow()


    fun startPairing(
        ssid : String,
        password : String,
        homeId : Long,
        timeOutSec : Long = 120
    ) {
        viewModelScope.launch {
            _isPairing.value = true
            _pairingResult.value = null

            getEZTokenUseCase(homeId)
                .onSuccess{ token ->
                    Log.i("EZConnectViewModel", "Token: $token")
                    Log.i("EZConnectViewModel", "SSID: $ssid")
                    Log.i("EZConnectViewModel", "Starting activator")

                    startEZPairingUseCase(ssid, password, homeId, token, timeOutSec.toInt())
                        .collect { result ->
                            _pairingResult.value = result
                            _isPairing.value = false
                        }

                }
                .onFailure {
                    Log.e("EZConnectViewModel", "Error getting token: ${it.message}")
                    _isPairing.value = false
                }
        }
    }

    fun resetResult() {
        _pairingResult.value = null
    }
}