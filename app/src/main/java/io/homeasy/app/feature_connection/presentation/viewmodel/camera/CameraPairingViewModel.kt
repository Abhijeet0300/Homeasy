package io.homeasy.app.feature_connection.presentation.viewmodel.camera

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.homeasy.app.feature_connection.domain.model.CameraDeviceActivationResult
import io.homeasy.app.feature_connection.domain.model.DeviceActivationResult
import io.homeasy.app.feature_connection.domain.usecases.camera.CameraPairingUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CameraPairingViewModel @Inject constructor(
    private val cameraPairingUseCase: CameraPairingUseCase
) : ViewModel() {
    private val _qrBitmap = MutableStateFlow<Bitmap?>(null)
    val qrBitmap = _qrBitmap.asStateFlow()

    private val _status = MutableStateFlow<String>("idle")
    val status = _status.asStateFlow()

    private val _device = MutableStateFlow<com.thingclips.smart.sdk.bean.DeviceBean?>(null)
    val device = _device.asStateFlow()

    fun startPairing(ssid: String, password: String, homeId: Long, timeoutSec: Long = 100) {
        viewModelScope.launch {
            _status.value = "requesting token..."
            cameraPairingUseCase(ssid, password, homeId, timeoutSec)
                .collect { ev ->
                    when (ev) {
                        is CameraDeviceActivationResult.QrCode -> {
                            _qrBitmap.value = ev.bitmap
                            _status.value = "showing QR"
                        }

                        is CameraDeviceActivationResult.Success -> {
                            _device.value = ev.device
                            _status.value = "paired"
                        }

                        is CameraDeviceActivationResult.Failure -> {
                            _status.value = "error: ${ev.message}"
                        }

                        is CameraDeviceActivationResult.Step -> {
                            _status.value = "step: ${ev.step}"
                        }

                        else -> {}
                    }
                }
        }
    }
}