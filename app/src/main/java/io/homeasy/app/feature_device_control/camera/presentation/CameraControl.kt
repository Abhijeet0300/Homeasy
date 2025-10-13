package io.homeasy.app.feature_device_control.camera.presentation

import android.R.attr.padding
import android.util.Log
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.thingclips.smart.camera.middleware.widget.AbsVideoViewCallback
import io.homeasy.app.feature_device_control.camera.presentation.viewmodel.CameraViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import io.homeasy.app.feature_room.presentation.RoomViewModel
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import com.thingclips.smart.camera.middleware.widget.ThingCameraView
import io.homeasy.app.core.navigation.AppRoutes
import io.homeasy.app.core.utils.ui.theme.ColoredTextColor
import io.homeasy.app.core.utils.ui_components.RegularButton
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@Composable
fun CameraControl(
    navController: NavController,
    cameraViewModel: CameraViewModel = hiltViewModel(),
    roomViewModel: RoomViewModel
) {
    val context = LocalContext.current
    val state by cameraViewModel.state.collectAsState()
    val device by roomViewModel.selectedDevice.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    var devId by remember { mutableStateOf(device?.devId) }

    LaunchedEffect(Unit) {
        Log.e("CameraControl", "DevId : ${devId}")
        cameraViewModel.initP2P(devId!!)
    }

    LaunchedEffect(device) {
        if (device != null) {
            devId = device!!.devId
            Log.d("CameraControl", "Device ID: $devId")
            //cameraViewModel.initP2P(devId)
        } else {
            Log.e("CameraControl", "Device is null")
            Toast.makeText(context, "No device selected", Toast.LENGTH_SHORT).show()
            navController.popBackStack(AppRoutes.HOME_SCREEN, inclusive = false)
        }
    }

    LaunchedEffect(state.error) {
        state.error?.let { error ->
            Log.e("CameraControl", "Error: $error")
            coroutineScope.launch {
                snackbarHostState.showSnackbar(
                    message = error,
                    actionLabel = if (error.contains("failed", true) || error.contains("invalid", true)) "Retry" else null,
                    duration = SnackbarDuration.Long
                ).let { result ->
                    if (result == SnackbarResult.ActionPerformed) {
                        cameraViewModel.initP2P(devId!!)
                    }
                }
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            cameraViewModel.stopStreaming(devId!!)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {
        AndroidView(
            factory = { ctx ->
                ThingCameraView(ctx).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                }
            },
            update = { cameraView ->
                if (devId!!.isNotBlank()) {
                    cameraView.setViewCallback(object : AbsVideoViewCallback() {
                        override fun onCreated(view: Any?) {
                            super.onCreated(view)
                            try {
                                Log.d("CameraControl", "Creating video view for devId: $devId")
                                cameraView.createVideoView(devId)
                                cameraViewModel.onVideoViewCreated(view, devId!!)
                            } catch (e: Exception) {
                                Log.e("CameraControl", "Failed to create video view: ${e.message}")
                                cameraViewModel._state.update { it.copy(error = "Failed to create video view: ${e.message}") }
                            }
                        }
                    })
                }
            },
            modifier = Modifier.fillMaxSize()
        )
        if (state.isReconnecting) {
            CircularProgressIndicator(
                color = ColoredTextColor,
                modifier = Modifier.align(Alignment.Center)
            )
            Log.e("Camera Control", "Reconnecting")
            Text(
                "Reconnecting...",
                color = Color.White,
                modifier = Modifier.align(Alignment.Center).offset(y = 30.dp)
            )
        } else if (!state.isReceivingVideo && state.isStreaming) {
            Log.e("Camera Control", "No video data received")
            Text(
                "No video data received",
                color = Color.Yellow,
                modifier = Modifier.align(Alignment.Center)
            )
        } else if (!state.isStreaming) {
            Log.e("Camera Control", "Not streaming")
            Text(
                "Not streaming",
                color = Color.White,
                modifier = Modifier.align(Alignment.Center)
            )
        }
        state.error?.let {
            Log.e("Camera Control", it)
            Text(
                "Error: $it",
                color = Color.Red,
                modifier = Modifier.align(Alignment.TopCenter).padding(8.dp)
            )
        }
        if (state.isConnected) {
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (!state.isStreaming) {
                    RegularButton(
                        label = "Stop Streaming",
                        onClick = { cameraViewModel.stopStreaming(devId!!) }
                    )
                } else {
                    Button(onClick = { cameraViewModel.startPreview() }) {
                        Text("Start Streaming")
                    }
                }
            }
        }
    }
}
