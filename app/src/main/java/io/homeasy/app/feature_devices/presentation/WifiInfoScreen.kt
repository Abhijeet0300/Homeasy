package io.homeasy.app.feature_devices.presentation


import android.content.Context
import android.net.wifi.WifiManager
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import io.homeasy.app.core.utils.ui_components.AppTextField
import io.homeasy.app.R
import io.homeasy.app.core.utils.ui_components.RegularButton
import io.homeasy.app.feature_devices.domain.model.DeviceActivationResult
import io.homeasy.app.feature_devices.presentation.viewmodel.EZConnectViewModel
import io.homeasy.app.feature_home.presentation.viewmodel.HomeViewModel


@Composable
fun WifiInfoScreen(
   navController: NavController,
   viewModel : EZConnectViewModel,
   homeViewModel: HomeViewModel
) {
    val context = LocalContext.current
    val isPairing by viewModel.isPairing.collectAsState()
    val pairingResult by viewModel.pairingResult.collectAsState()

    var wifiSsid = remember {
        mutableStateOf("")
    }

    var wifiPassword = remember {
        mutableStateOf("")
    }
    val coroutineScope = rememberCoroutineScope()
    val wifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
    val wifiInfo = wifiManager.connectionInfo

    LaunchedEffect(Unit) {
        wifiSsid.value = if (wifiInfo != null && wifiInfo.ssid != null) {
            wifiInfo.ssid.removeSurrounding("\"") // Remove quotes around SSID
        } else {
            "Not connected"
        }
    }

    LaunchedEffect(pairingResult) {
        pairingResult?.let { result ->
            Log.i("WifiInfoScreen", "Result: $result")
            when(result) {
                is DeviceActivationResult.Success -> {
                    Log.i("WifiInfoScreen", "Paired: ${result.deviceBean.devId ?: result.deviceBean.name}")
                    Toast.makeText(context, "Paired: ${result.deviceBean.devId ?: result.deviceBean.name}",
                        Toast.LENGTH_SHORT).show()
                }

                is DeviceActivationResult.Failure -> {
                    Log.e("WifiInfoScreen", "Error: ${result.errorMessage}")
                    Toast.makeText(context, "Error: ${result.errorMessage}", Toast.LENGTH_SHORT).show()
                }

                is DeviceActivationResult.Step -> {
                    Toast.makeText(context, "Step: ${result.step}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        AppTextField(
            value = wifiSsid,
            label = stringResource(id = R.string.wifi_name),
            imeAction = ImeAction.Next
        )

        Spacer(modifier = Modifier.height(10.dp))

        AppTextField(
            value = wifiPassword,
            label = stringResource(id= R.string.wifi_password),
            placeholder = stringResource(id = R.string.enter_wifi_password),
            imeAction = ImeAction.Done
        )

        Spacer(modifier = Modifier.height(10.dp))

        RegularButton(
            label = stringResource(id = R.string.wifi_connect),
            onClick = {
                viewModel.startPairing(
                    ssid = wifiSsid.value,
                    password = wifiPassword.value,
                    homeId = homeViewModel.selectedHome.value!!.homeId
                )
            }
        )
    }
}