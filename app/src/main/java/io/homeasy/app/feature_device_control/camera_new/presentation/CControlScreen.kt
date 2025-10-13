package io.homeasy.app.feature_device_control.camera_new.presentation

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import io.homeasy.app.feature_device_control.camera_new.presentation.viewmodel.CameraControlViewModel
import io.homeasy.app.feature_home.presentation.viewmodel.HomeViewModel
import io.homeasy.app.feature_room.presentation.RoomViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewModelScope
import com.thingclips.smart.camera.middleware.widget.AbsVideoViewCallback
import com.thingclips.smart.camera.middleware.widget.ThingCameraView
import io.homeasy.app.core.utils.ui_components.RegularButton
import kotlinx.coroutines.delay

@Composable
fun CControl(
    homeViewModel: HomeViewModel,
    roomViewModel: RoomViewModel,
    cameraControlViewModel: CameraControlViewModel
) {
    val homeBean by homeViewModel.selectedHome.collectAsState()
    val roomBean by roomViewModel.selectedRoom.collectAsState()
    val deviceBean by roomViewModel.selectedDevice.collectAsState()
    val context = LocalContext.current

    val isRecording by cameraControlViewModel.isRecording.collectAsState()
    val recordingMessage by cameraControlViewModel.recordingMessage.collectAsState()
    val isCameraP2PCreated by cameraControlViewModel.isCameraP2PCreated.collectAsState()
    val isStreaming by cameraControlViewModel.isStreaming.collectAsState()
    val error by cameraControlViewModel.error.collectAsState()
    val isConnected by cameraControlViewModel.isConnected.collectAsState()
    var cameraView by remember {
        mutableStateOf<ThingCameraView?>(null)
    }
    val devId = deviceBean?.devId


    LaunchedEffect(deviceBean?.devId) {
        deviceBean?.let {
            cameraControlViewModel.createCameraP2P(it.devId)
            Log.i("Camera Control", "${it.devId} created")
        }
    }

    LaunchedEffect(isCameraP2PCreated) {
        if (isCameraP2PCreated && devId != null && cameraView != null) {
            delay(300)
            cameraControlViewModel.bindView(cameraView!!, devId)
        }
    }


    LaunchedEffect(error) {
        Log.e("CameraControl", "Error: $error")
    }

    DisposableEffect(Unit) {
        onDispose {
            cameraControlViewModel.stopCamera()
        }
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        RegularButton(
            label = "Start",
            onClick = {
                if(isCameraP2PCreated) {
                    cameraControlViewModel.startRecording("/Camera/", context)
                } else {
                    Log.e("CameraControl", "CameraP2P not created")
                }
            }
        )

        RegularButton(
            label = "Stop",
            onClick = {
                cameraControlViewModel.stopRecording()
            }
        )

        Box(
            modifier = Modifier.size(400.dp)
        ) {
            if(isCameraP2PCreated) {
                AndroidView(
                    modifier = Modifier.fillMaxSize(),
                    factory = { ctx : Context ->
                        cameraView = ThingCameraView(ctx)
                        cameraView!!.setCameraViewCallback(object : AbsVideoViewCallback(){
                            override fun onCreated(view: Any?) {
                                super.onCreated(view)
                                cameraView?.createVideoView(deviceBean!!.devId)
//                        if (isCameraP2PCreated && view != null) {
//                            cameraControlViewModel.bindView(cameraView, deviceBean!!.devId)
//                        } else {
//                            Log.e("CameraControl", "P2P not ready yet, skipping bindView")
//                        }
                            }
                        })
                        cameraView!!
                    })
                Log.i("CameraControl", "P2P ready to bindView, view = $cameraView devId: $devId")
            } else {
                Log.e("CameraControl", "P2P not ready yet, skipping bindView, view = $cameraView devId: $devId")
            }
        }

        if(!isStreaming) {
            RegularButton(
                label = "Start Live Stream",
                onClick = {
                    if(isCameraP2PCreated && cameraView != null && devId != null) {
                        cameraControlViewModel.bindView(cameraView!!, devId)
                    } else {
                        Log.e("CameraControl", "P2P not ready yet, skipping bindView, view = $cameraView devId: $devId")
                    }
                }
            )
        }
    }
}