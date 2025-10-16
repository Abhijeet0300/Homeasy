package io.homeasy.app.feature_device_control.camera_new.presentation
import android.content.Context
import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.thingclips.smart.camera.middleware.widget.AbsVideoViewCallback
import com.thingclips.smart.camera.middleware.widget.ThingCameraView
import io.homeasy.app.core.utils.ui_components.RegularButton
import io.homeasy.app.feature_device_control.camera_new.presentation.viewmodel.CameraControlViewModel
import io.homeasy.app.feature_home.presentation.viewmodel.HomeViewModel
import io.homeasy.app.feature_room.presentation.RoomViewModel
import kotlinx.coroutines.launch

@Composable
fun CControl(
    homeViewModel: HomeViewModel,
    roomViewModel: RoomViewModel,
    cameraControlViewModel: CameraControlViewModel
) {
    val context = LocalContext.current
    val TAG = "CameraControlUI"

    val deviceBean by roomViewModel.selectedDevice.collectAsState()
    val devId = deviceBean?.devId

    val isCameraP2PCreated by cameraControlViewModel.isCameraP2PCreated.collectAsState()
    val isStreaming by cameraControlViewModel.isStreaming.collectAsState()
    val isRecording by cameraControlViewModel.isRecording.collectAsState()
    val isReadyToConnect by cameraControlViewModel.isReadyToConnect.collectAsState()

    var cameraView by remember { mutableStateOf<ThingCameraView?>(null) }
    var isViewCreated by remember { mutableStateOf(false) }

    val isCameraReady = isCameraP2PCreated && isViewCreated

//    LaunchedEffect(isCameraReady) {
//        Log.d(TAG, "isCameraReady: $isCameraReady")
//    }

    LaunchedEffect(devId) {
        devId?.let { id ->
            Log.d(TAG, "Creating P2P for $id")
            cameraControlViewModel.createCameraP2P(id)
        }
    }

    // Register listener AFTER view creation (matches SDK order)
//    LaunchedEffect(isViewCreated, isCameraP2PCreated) {
//        if (isViewCreated && isCameraP2PCreated && devId != null) {
//            cameraControlViewModel.registerListener(devId)
//            Log.d(TAG, "Listener registered after view ready")
//        }
//    }

    Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        RegularButton(
            label = if (isRecording) "Recording..." else "Start Recording",
            onClick = { if (isCameraReady) cameraControlViewModel.startRecording("Camera", context) },
            enabled = isCameraReady
        )
        RegularButton(
            label = "Stop Recording",
            onClick = { cameraControlViewModel.stopRecording() },
            enabled = isRecording
        )

        Box(
            Modifier
                .size(400.dp)
                .border(1.dp, Color.Black, RoundedCornerShape(10.dp))
        ) {
            if (devId != null && isCameraP2PCreated) {
                AndroidView(
                    modifier = Modifier.fillMaxSize(),
                    factory = { ctx ->
                        Log.d(TAG, "AndroidView Factory for $devId")
                        ThingCameraView(ctx).apply {
                            setViewCallback(object : AbsVideoViewCallback() {
                                override fun onCreated(view: Any?) {
                                    super.onCreated(view)
                                    if (view != null) {
                                        // ✨ THE FIX: Setup view and listener here
                                        Log.d(TAG, "ThingCameraView onCreated callback. View is ready.")
                                        cameraControlViewModel.setupCameraAndView(view, devId, this@apply)
                                        isViewCreated = true
                                    } else {
                                        Log.e(TAG, "ThingCameraView onCreated with a null view.")
                                    }
                                }
                            })
                            createVideoView(devId)
                        }
                    },
                    onRelease = { view ->
                        Log.d(TAG, "AndroidView onRelease")
                        cameraControlViewModel.unregisterListener()
                        isViewCreated = false
                    }
                )
            }
        }

        if (!isStreaming) {
            RegularButton(
                label = "Start Live Stream",
                enabled = isCameraReady,
                onClick = { devId?.let { cameraControlViewModel.connectAndStartStream(it) } }
            )
        } else {
            RegularButton(
                label = "Stop Live Stream",
                onClick = { cameraControlViewModel.stopPreview() }
            )
        }
    }
}


/*
Abhijeet Code
import android.content.Context
import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewModelScope
import com.thingclips.smart.camera.middleware.widget.AbsVideoViewCallback
import com.thingclips.smart.camera.middleware.widget.ThingCameraView
import io.homeasy.app.core.utils.ui_components.RegularButton
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
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
var isViewCreated by remember { mutableStateOf(false) }
val coroutineScope = rememberCoroutineScope()


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
cameraControlViewModel.startRecording("Camera", context)
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
modifier = Modifier.size(400.dp).border(width = 1.dp, color = Color.Black, shape = RoundedCornerShape(10.dp))
) {
if(isCameraP2PCreated) {
//                AndroidView(
//                    modifier = Modifier.fillMaxSize(),
//                    factory = { ctx ->
//                        ThingCameraView(ctx).apply {
//                            setCameraViewCallback(object : AbsVideoViewCallback() {
//                                override fun onCreated(view: Any?) {
//                                    super.onCreated(view)
//                                    createVideoView(devId)
//                                    isViewCreated = true
//                                    cameraView = this@apply
//                                }
//                            })
//                        }
//                    }
//                )
AndroidView(
modifier = Modifier.fillMaxSize(),
factory = { ctx : Context ->
cameraView = ThingCameraView(ctx)
cameraView!!.setCameraViewCallback(object : AbsVideoViewCallback(){
override fun onCreated(view: Any?) {
super.onCreated(view)
cameraView!!.createVideoView(devId)
isViewCreated = true
if (isCameraP2PCreated && view != null) {
cameraControlViewModel.bindView(cameraView!!, deviceBean!!.devId)
} else {
Log.e("CameraControl", "P2P not ready yet, skipping bindView")
}
cameraView!!.createVideoView(devId)
}
})
cameraView!!
}
)
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
//cameraControlViewModel.bindView(cameraView!!, devId)
coroutineScope.launch {
delay(1000)
}
cameraControlViewModel.connect(devId)
coroutineScope.launch {
delay(1000)
}
cameraControlViewModel.startStream()
} else {
Log.e("CameraControl", "P2P not ready yet, skipping bindView, view = $cameraView devId: $devId")
}
}
)
}
}
}
*/