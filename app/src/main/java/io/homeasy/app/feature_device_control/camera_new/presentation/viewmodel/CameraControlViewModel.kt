package io.homeasy.app.feature_device_control.camera_new.presentation.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thingclips.smart.camera.camerasdk.thingplayer.callback.AbsP2pCameraListener
import com.thingclips.smart.camera.camerasdk.thingplayer.callback.OperationDelegateCallBack
import dagger.hilt.android.lifecycle.HiltViewModel
import io.homeasy.app.feature_device_control.camera_new.domain.usecase.ConnectCameraUsecase
import io.homeasy.app.feature_device_control.camera_new.domain.usecase.CreateCameraP2PUsecase
import io.homeasy.app.feature_device_control.camera_new.domain.usecase.DestroyConnectionUseCase
import io.homeasy.app.feature_device_control.camera_new.domain.usecase.GenerateCameraView
import io.homeasy.app.feature_device_control.camera_new.domain.usecase.RegisterListenerUsecase
import io.homeasy.app.feature_device_control.camera_new.domain.usecase.StartPreviewUsecase
import io.homeasy.app.feature_device_control.camera_new.domain.usecase.StartRecordingUsecase
import io.homeasy.app.feature_device_control.camera_new.domain.usecase.StopPreviewUsecase
import io.homeasy.app.feature_device_control.camera_new.domain.usecase.StopRecordingUsecase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CameraControlViewModel @Inject constructor(
    private val createCameraP2PUsecase: CreateCameraP2PUsecase,
    private val startRecordingUsecase: StartRecordingUsecase,
    private val stopRecordingUsecase: StopRecordingUsecase,
    private val generateCameraView: GenerateCameraView,
    private val connectCameraUsecase: ConnectCameraUsecase,
    private val startPreviewUsecase: StartPreviewUsecase,
    private val stopPreviewUsecase: StopPreviewUsecase,
    private val registerListenerUsecase: RegisterListenerUsecase,
    private val destroyConnectionUseCase: DestroyConnectionUseCase
) : ViewModel() {
    private val _isCameraP2PCreated = MutableStateFlow<Boolean>(false)
    val isCameraP2PCreated = _isCameraP2PCreated.asStateFlow()

    private val _isRecording = MutableStateFlow<Boolean?>(null)
    val isRecording = _isRecording.asStateFlow()

    private val _recordingMessage = MutableStateFlow<String?>(null)
    val recordingMessage = _recordingMessage.asStateFlow()

    private val _isStreaming = MutableStateFlow(false)
    val isStreaming = _isStreaming.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    private val _isConnected = MutableStateFlow<Boolean>(false)
    val isConnected = _isConnected.asStateFlow()

    private var pendingView: Any? = null

    fun createCameraP2P(devId : String) {
        _isCameraP2PCreated.value = createCameraP2PUsecase(devId = devId)
    }

    fun startRecording(
        folderPath: String,
        context: Context
    ) {
        viewModelScope.launch {
            startRecordingUsecase(folderPath, context, object : OperationDelegateCallBack{
                override fun onSuccess(
                    sessionId: Int,
                    requestId: Int,
                    data: String?
                ) {
                    _isRecording.value = true
                    _recordingMessage.value = "Recording has started."
                }

                override fun onFailure(
                    sessionId: Int,
                    requestId: Int,
                    errCode: Int
                ) {
                    _recordingMessage.value = "Recording failed, code=$errCode"
                }
            })
        }
    }

    fun stopRecording() {
        viewModelScope.launch {
            stopRecordingUsecase(object : OperationDelegateCallBack{
                override fun onSuccess(
                    sessionId: Int,
                    requestId: Int,
                    data: String?
                ) {
                    _isRecording.value = false
                    _recordingMessage.value = "Recording has stopped."
                }

                override fun onFailure(
                    sessionId: Int,
                    requestId: Int,
                    errCode: Int
                ) {
                    _recordingMessage.value = "Stop recording failed, code=$errCode"
                }
            })
        }
    }

    fun bindView(view : Any, devId : String) {
        generateCameraView(view)
        registerListenerUsecase(object : AbsP2pCameraListener() {
            override fun onSessionStatusChanged(camera: Any?, sessionId: Int, sessionStatus: Int) {
                super.onSessionStatusChanged(camera, sessionId, sessionStatus)
                if (sessionStatus < 0) _error.value = "Session error $sessionStatus"
                Log.d("CameraP2P", "Session status changed → sessionId=$sessionId, status=$sessionStatus")

                when (sessionStatus) {
                    1 -> Log.i("CameraP2P", "✅ Session connected successfully")
                    -2 -> Log.e("CameraP2P", "❌ Device offline or not reachable")
                    -3 -> Log.e("CameraP2P", "❌ Connection timeout — consider reconnecting")
                    -105 -> Log.e("CameraP2P", "❌ Authentication failed — check token/devId")
                    else -> Log.w("CameraP2P", "⚠️ Unknown session status: $sessionStatus")
                }
            }
        })

        viewModelScope.launch {
            connectCameraUsecase(devId = devId)
                .onSuccess {
                    startPreviewUsecase()
                        .onSuccess {
                            _isStreaming.value = true
                        }
                        .onFailure {
                            _error.value = "Preview failed to start"
                        }
                }
                .onFailure {
                    _error.value = "Connect failed"
                }
        }
    }

    fun connect(devId : String) {
        viewModelScope.launch {
            connectCameraUsecase(devId)
                .onSuccess{
                    _isConnected.value = it
                }
                .onFailure{
                    _error.value = "Camera didn't connect"
                }
        }
    }

    fun startStream() {
        viewModelScope.launch {
            startPreviewUsecase()
                .onSuccess {
                    _isStreaming.value = true
                }
                .onFailure {
                    _error.value = "Preview failed to start."
                }
        }
    }

    fun stopCamera() {
        viewModelScope.launch {
            stopPreviewUsecase()
                .onSuccess {
                    destroyConnectionUseCase()
                    _isStreaming.value = false
                }
                .onFailure {
                    _error.value = "Preview failed to stop"
                }
        }

        destroyConnectionUseCase()
        _isStreaming.value = false
    }


    fun tryBindIfPending(devId: String) {
        pendingView?.let {
            bindView(it, devId)
        }
    }
}