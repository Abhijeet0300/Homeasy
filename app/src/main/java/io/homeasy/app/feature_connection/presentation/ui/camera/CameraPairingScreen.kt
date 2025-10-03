package io.homeasy.app.feature_connection.presentation.ui.camera

import android.graphics.drawable.BitmapDrawable
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.homeasy.app.feature_connection.presentation.viewmodel.camera.CameraPairingViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.core.graphics.drawable.toBitmap
import io.homeasy.app.core.utils.ui_components.RegularButton

@Composable
fun CameraPairingScreen(
    ssid : String,
    password : String,
    homeId : Long,
    viewModel : CameraPairingViewModel
) {
    val qrBitmap by viewModel.qrBitmap.collectAsState()
    val status by viewModel.status.collectAsState()
    val device by viewModel.device.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.startPairing(
            ssid = ssid,
            password = password,
            homeId = homeId,
            timeoutSec = 100
        )
    }

    LaunchedEffect(status) {
        Toast.makeText(context, status.toString(), Toast.LENGTH_SHORT).show()
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(
            modifier = Modifier.size(300.dp),
            contentAlignment = Alignment.Center
        ) {
            qrBitmap?.let { bmp ->
                Image(
                    bitmap = BitmapDrawable(LocalContext.current.resources, bmp).toBitmap().asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        device?.let {
            Toast.makeText(context, "Device paired successfully", Toast.LENGTH_SHORT).show()
        }

        Spacer(modifier = Modifier.height(10.dp))

        RegularButton(
            label = "Next",
            onClick = {}
        )

    }
}