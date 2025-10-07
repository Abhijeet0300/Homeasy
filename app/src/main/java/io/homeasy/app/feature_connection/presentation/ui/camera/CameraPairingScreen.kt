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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.core.graphics.drawable.toBitmap
import androidx.navigation.NavController
import io.homeasy.app.core.navigation.AppRoutes
import io.homeasy.app.core.utils.ui_components.RegularButton
import io.homeasy.app.feature_connection.presentation.viewmodel.WifiInfoViewModel
import io.homeasy.app.feature_home.presentation.viewmodel.HomeViewModel
import io.homeasy.app.feature_room.presentation.RoomViewModel

@Composable
fun CameraPairingScreen(
//    ssid : String,
//    password : String,
//    homeId : Long,
    navController: NavController,
    homeViewModel: HomeViewModel,
    cameraPairingViewModel : CameraPairingViewModel,
    wifiInfoViewModel: WifiInfoViewModel,
    roomViewModel : RoomViewModel
) {
    val qrBitmap by cameraPairingViewModel.qrBitmap.collectAsState()
    val status by cameraPairingViewModel.status.collectAsState()
    val device by cameraPairingViewModel.device.collectAsState()
    val context = LocalContext.current

    val ssid by wifiInfoViewModel.ssid.collectAsState()
    val password by wifiInfoViewModel.password.collectAsState()
    val homeBean by homeViewModel.selectedHome.collectAsState()

    LaunchedEffect(Unit) {
        if(homeBean != null) {
            cameraPairingViewModel.startPairing(
                ssid = ssid.toString(),
                password = password.toString(),
                homeId = homeBean!!.homeId,
                timeoutSec = 100
            )
        }
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
            onClick = {
                if(device != null) {
                    roomViewModel.setAddedDevice(device = device!!)
                    navController.navigate(route = AppRoutes.USER_HOME) {
                        launchSingleTop = true
                    }
                }
            },
            enabled = device != null
        )

    }
}