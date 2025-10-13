package io.homeasy.app.feature_device_control.camera.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thingclips.smart.camera.camerasdk.bean.ThingVideoFrameInfo
import com.thingclips.smart.camera.camerasdk.thingplayer.callback.AbsP2pCameraListener
import dagger.hilt.android.lifecycle.HiltViewModel
import io.homeasy.app.feature_device_control.camera.domain.repository.CameraP2PRepository
import io.homeasy.app.feature_device_control.camera.domain.usecase.ConnectUseCase
import io.homeasy.app.feature_device_control.camera.domain.usecase.CreateP2PUseCase
import io.homeasy.app.feature_device_control.camera.domain.usecase.DestroyUseCase
import io.homeasy.app.feature_device_control.camera.domain.usecase.DisconnectUseCase
import io.homeasy.app.feature_device_control.camera.domain.usecase.GenerateCameraViewUseCase
import io.homeasy.app.feature_device_control.camera.domain.usecase.RegisterP2PListener
import io.homeasy.app.feature_device_control.camera.domain.usecase.StartPreviewUseCase
import io.homeasy.app.feature_device_control.camera.domain.usecase.StopPreviewUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.nio.ByteBuffer
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor(
    private val repo: CameraP2PRepository,
    private val connectUseCase: ConnectUseCase,
    private val createP2PUseCase: CreateP2PUseCase,
    private val stopPreviewUseCase: StopPreviewUseCase,
    private val disconnectUseCase: DisconnectUseCase,
    private val destroyUseCase: DestroyUseCase,
    private val registerP2PListener: RegisterP2PListener,
    private val startPreviewUseCase: StartPreviewUseCase,
    private val generateCameraViewUseCase: GenerateCameraViewUseCase
): ViewModel() {

    val _state = MutableStateFlow(CameraState())
    val state = _state.asStateFlow()
    private var reconnectAttempts = 0
    private val maxReconnectAttempts = 3

    data class CameraState(
        val isStreaming: Boolean = false,
        val isConnected: Boolean = false,
        val isReceivingVideo: Boolean = false,
        val error: String? = null,
        val isReconnecting: Boolean = false,
        val sessionId: Int? = null,
        val shouldReconnect: Boolean = false
    )
    fun initP2P(devId: String) {
        if (devId.isBlank()) {
            _state.update { it.copy(error = "Invalid device ID") }
            return
        }
        val ok = createP2PUseCase(devId)
        if (!ok) {
            _state.update { it.copy(error = "Failed to create P2P for $devId") }
        } else {
            _state.update { it.copy(error = null) }
            registerP2PListener(object : AbsP2pCameraListener() {
                override fun onSessionStatusChanged(camera: Any?, sessionId: Int, sessionStatus: Int) {
                    when (sessionStatus) {
                        -3, -105 -> { // Timeout or auth failure
                            if (reconnectAttempts < maxReconnectAttempts) {
                                reconnectAttempts++
                                _state.update { it.copy(isReconnecting = true, error = "Reconnecting ($reconnectAttempts/$maxReconnectAttempts)") }
                                connect(devId)
                            } else {
                                _state.update { it.copy(isConnected = false, isReconnecting = false, error = "Max reconnection attempts reached: Status $sessionStatus") }
                            }
                        }
                        else -> {
                            _state.update { it.copy(isConnected = sessionStatus > 0, isReconnecting = false, error = if (sessionStatus < 0) "Session error: $sessionStatus" else null) }
                        }
                    }
                }

                override fun onReceiveFrameYUVData(
                    sessionId: Int,
                    y: ByteBuffer,
                    u: ByteBuffer,
                    v: ByteBuffer,
                    videoFrameInfo: ThingVideoFrameInfo,
                    camera: Any
                ) {
                    if (y.hasRemaining() && u.hasRemaining() && v.hasRemaining() && videoFrameInfo.width > 0 && videoFrameInfo.height > 0) {
                        _state.update { it.copy(isReceivingVideo = true, error = null) }
                    } else {
                        _state.update { it.copy(isReceivingVideo = false, error = "Invalid video frame data") }
                    }
                }
            })
        }
    }

    // called from Compose when ThingCameraView.onCreated(view) happens
    fun onVideoViewCreated(renderedView: Any?, devId: String) {
        if (devId.isBlank()) {
            _state.update { it.copy(error = "Invalid device ID") }
            return
        }
        generateCameraViewUseCase(renderedView!!)
        connect(devId)
    }

     fun connect(devId: String) {
        viewModelScope.launch {
            connectUseCase(devId, 1).onSuccess { sessionId ->
                _state.update { it.copy(isConnected = true, sessionId = sessionId, error = null, isReconnecting = false) }
                reconnectAttempts = 0
                startPreview()
            }.onFailure { ex ->
                _state.update { it.copy(error = "Connect failed: ${ex.message}") }
                Log.e("CameraViewModel", "Connect failed: ${ex.message}")
            }
        }
    }

     fun startPreview() {
         if (!_state.value.isConnected || _state.value.sessionId == null) {
             _state.update { it.copy(error = "Not connected. Please connect first.") }
             Log.e("CameraViewModel", "Start preview failed: Not connected or no sessionId")
             return
         }
        viewModelScope.launch {
            startPreviewUseCase(2).onSuccess { // Use SD (2) as default
                _state.update { it.copy(isStreaming = true, error = null) }
            }.onFailure { ex ->
                _state.update { it.copy(error = "Start preview failed: ${ex.message}") }
            }
        }
    }

    fun stopStreaming(devId: String) {
        viewModelScope.launch {
            Log.d("CameraViewModel", "Stopping streaming for devId=$devId")
            stopPreviewUseCase().onSuccess {
                disconnectUseCase(devId).onSuccess {
                    _state.update { it.copy(isStreaming = false, isConnected = false, isReceivingVideo = false, error = null, sessionId = null) }
                    Log.d("CameraViewModel", "Streaming stopped successfully")
                }.onFailure { ex ->
                    _state.update { it.copy(error = "Disconnect failed: ${ex.message}") }
                    Log.e("CameraViewModel", "Disconnect failed: ${ex.message}")
                }
            }.onFailure { ex ->
                _state.update { it.copy(error = "Stop preview failed: ${ex.message}") }
                Log.e("CameraViewModel", "Stop preview failed: ${ex.message}")
            }
        }
    }

    init {
        viewModelScope.launch {
            _state.collect { state ->
                if (state.shouldReconnect && state.isReconnecting) {
                    Log.d("CameraViewModel", "Scheduling reconnect attempt: $reconnectAttempts")
                    delay(2000) // Delay to avoid deadlock
                    connect(state.error?.substringAfter("Reconnecting")?.substringBefore(")")?.substringBefore("/")?.toIntOrNull()?.let { it - 1 }?.toString() ?: "")
                }
            }
        }
    }

    override fun onCleared() {
        destroyUseCase()
        super.onCleared()
    }
}
