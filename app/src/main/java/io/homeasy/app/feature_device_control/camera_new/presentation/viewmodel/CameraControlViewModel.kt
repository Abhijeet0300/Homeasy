package io.homeasy.app.feature_device_control.camera_new.presentation.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thingclips.smart.camera.camerasdk.bean.ThingVideoFrameInfo
import com.thingclips.smart.camera.camerasdk.thingplayer.callback.AbsP2pCameraListener
import com.thingclips.smart.camera.camerasdk.thingplayer.callback.OperationDelegateCallBack
import com.thingclips.smart.camera.middleware.p2p.IThingSmartCameraP2P
import com.thingclips.smart.camera.middleware.widget.ThingCameraView
import com.thingclips.smart.camera.nativeapi.ThingCameraNative.startPreview
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
import java.nio.ByteBuffer
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
    private val mCameraP2P = MutableStateFlow<IThingSmartCameraP2P<Any?>?>(null)
    private val _isCameraP2PCreated = MutableStateFlow<Boolean>(false)
    val isCameraP2PCreated = _isCameraP2PCreated.asStateFlow()

    private val _isRecording = MutableStateFlow<Boolean>(false)
    val isRecording = _isRecording.asStateFlow()

    private val _recordingMessage = MutableStateFlow<String?>(null)
    val recordingMessage = _recordingMessage.asStateFlow()

    private val _isStreaming = MutableStateFlow(false)
    val isStreaming = _isStreaming.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    private val _isConnected = MutableStateFlow<Boolean>(false)
    val isConnected = _isConnected.asStateFlow()

    private val _isReadyToConnect = MutableStateFlow(false)
    val isReadyToConnect = _isReadyToConnect.asStateFlow()

    private var pendingView: Any? = null

    private val p2pListener = MutableStateFlow<AbsP2pCameraListener?>(null)

    fun createCameraP2P(devId: String) {
        if (mCameraP2P.value != null) return // Avoid re-creation
        mCameraP2P.value = createCameraP2PUsecase(devId = devId)
        if (mCameraP2P != null) {
            _isCameraP2PCreated.value = true
        } else {
            Log.e("Camera Control View Model", "Camera P2P is null")
        }
    }

    fun setupCameraAndView(view: Any, devId: String, cameraView: ThingCameraView) {
        // 1. Bind the native view object to the P2P manager
        mCameraP2P.value?.generateCameraView(view)
        Log.d("CameraP2P", "View bound for $devId")

        // 2. Unregister any old listener
        unregisterListener()

        // 3. Create a new listener that knows about the Composable's cameraView
        p2pListener.value = createListener() // Pass the view to the listener
        mCameraP2P.value?.registerP2PCameraListener(p2pListener.value)
        Log.d("CameraP2P", "Listener registered")
    }

    fun registerListener(devId: String) {
        p2pListener.value = createListener()
        mCameraP2P.value?.registerP2PCameraListener(p2pListener.value)
        Log.d("CameraP2P", "Listener registered")
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


    fun bindView(view: Any, devId: String) {
        mCameraP2P.value?.generateCameraView(view)
        Log.d("CameraP2P", "View bound for $devId")
    }

    private fun createListener(): AbsP2pCameraListener {
        return object : AbsP2pCameraListener() {
            override fun onSessionStatusChanged(camera: Any?, sessionId: Int, sessionStatus: Int) {
                super.onSessionStatusChanged(camera, sessionId, sessionStatus)
                Log.d("CameraP2P", "Session status changed: $sessionStatus")
                if (sessionStatus == 1) { // STATUS_CONNECTED
                    Log.i("CameraP2P", "Connected! Starting preview...")
                    startPreview(2) // 2 for SD
                    _isConnected.value = true
                } else if (sessionStatus < 0) {
                    _isConnected.value = false
                    _isStreaming.value = false
                    _error.value = "Session error or disconnected: $sessionStatus"
                }
            }

            override fun onReceiveFrameYUVData(
                sessionId: Int, y: ByteBuffer, u: ByteBuffer, v: ByteBuffer,
                videoFrameInfo: ThingVideoFrameInfo, camera: Any
            ) {
                super.onReceiveFrameYUVData(sessionId, y, u, v, videoFrameInfo, camera)
                // The SDK handles rendering automatically.
                // We just use this callback to know that streaming has started.
                if (!_isStreaming.value) {
                    _isStreaming.value = true
                }
            }
        }
    }

    fun connectToStream(devId: String) {
        mCameraP2P.value?.connect(devId, object : OperationDelegateCallBack {
            override fun onSuccess(sessionId: Int, requestId: Int, data: String?) {
                Log.d("CameraP2P", "Connect command sent successfully for session: $sessionId")
                // Status is handled by the listener
            }

            override fun onFailure(sessionId: Int, requestId: Int, errCode: Int) {
                Log.e("CameraP2P", "Connect command failed: $errCode")
                _error.value = "Connection failed: $errCode"
            }
        })
    }



    fun unregisterListener() {
        p2pListener.value?.let { mCameraP2P.value?.removeOnP2PCameraListener(it) }
        p2pListener.value = null
    }

    fun connectAndStartStream(devId: String) {
        mCameraP2P.value?.connect(devId, object : OperationDelegateCallBack {
            override fun onSuccess(sessionId: Int, requestId: Int, data: String?) {
                Log.d("CameraP2P", "Connected: $sessionId")
                startPreview(2)  // SD
            }

            override fun onFailure(sessionId: Int, requestId: Int, errCode: Int) {
                Log.e("CameraP2P", "Connect fail: $errCode")
            }
        })
    }

     fun startPreview(clarity: Int) {
        mCameraP2P.value?.startPreview(clarity, object : OperationDelegateCallBack {
            override fun onSuccess(sessionId: Int, requestId: Int, data: String?) {
                Log.i("CameraP2P", "Preview started: $clarity")
            }

            override fun onFailure(sessionId: Int, requestId: Int, errCode: Int) {
                Log.e("CameraP2P", "Preview fail: $errCode")
            }
        })
    }

    fun stopPreview() {
        mCameraP2P.value?.stopPreview(object : OperationDelegateCallBack {
            override fun onSuccess(sessionId: Int, requestId: Int, data: String?) {
                Log.i("CameraP2P", "Preview stopped")
                _isStreaming.value = false
            }
            override fun onFailure(sessionId: Int, requestId: Int, errCode: Int) {
                Log.e("CameraP2P", "Stop preview fail: $errCode")
            }
        })
    }

    override fun onCleared() {
        unregisterListener()
        mCameraP2P.value?.destroyP2P()
        super.onCleared()
    }


//    fun bindView(view : Any, devId : String) {
//        generateCameraView(view)
//        registerListenerUsecase(object : AbsP2pCameraListener() {
//            override fun onSessionStatusChanged(camera: Any?, sessionId: Int, sessionStatus: Int) {
//                super.onSessionStatusChanged(camera, sessionId, sessionStatus)
//                if (sessionStatus < 0) _error.value = "Session error $sessionStatus"
//                Log.d("CameraP2P", "Session status changed → sessionId=$sessionId, status=$sessionStatus")
//
//                when (sessionStatus) {
//                    1 -> Log.i("CameraP2P", "✅ Session connected successfully")
//                    -2 -> Log.e("CameraP2P", "❌ Device offline or not reachable")
//                    -3 -> Log.e("CameraP2P", "❌ Connection timeout — consider reconnecting")
//                    -105 -> Log.e("CameraP2P", "❌ Authentication failed — check token/devId")
//                    else -> Log.w("CameraP2P", "⚠️ Unknown session status: $sessionStatus")
//                }
//            }
//        })
//
////        viewModelScope.launch {
////            connectCameraUsecase(devId = devId)
////                .onSuccess {
////                    startPreviewUsecase()
////                        .onSuccess {
////                            _isStreaming.value = true
////                        }
////                        .onFailure {
////                            _error.value = "Preview failed to start"
////                        }
////                }
////                .onFailure {
////                    _error.value = "Connect failed"
////                }
////        }
//    }
//
//    fun connect(devId : String) {
//        viewModelScope.launch {
//            connectCameraUsecase(devId)
//                .onSuccess{
//                    _isConnected.value = it
//                }
//                .onFailure{
//                    _error.value = "Camera didn't connect"
//                }
//        }
//    }
//
//    fun startStream() {
//        viewModelScope.launch {
//            startPreviewUsecase()
//                .onSuccess {
//                    _isStreaming.value = true
//                }
//                .onFailure {
//                    _error.value = "Preview failed to start."
//                }
//        }
//    }
//
//    fun stopStream() {
//        viewModelScope.launch {
//            stopPreviewUsecase()
//                .onSuccess {
//                    destroyConnectionUseCase()
//                    _isStreaming.value = false
//                }
//                .onFailure {
//                    _error.value = "Preview failed to stop"
//                }
//        }
//
//        destroyConnectionUseCase()
//        _isStreaming.value = false
//    }
//
//
//    fun tryBindIfPending(devId: String) {
//        pendingView?.let {
//            bindView(it, devId)
//        }
//    }
}