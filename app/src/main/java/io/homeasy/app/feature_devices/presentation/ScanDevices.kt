package io.homeasy.app.feature_devices.presentation

import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import io.homeasy.app.feature_devices.presentation.viewmodel.ScanDevicesBleViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import com.thingclips.smart.android.ble.api.ScanDeviceBean
import io.homeasy.app.core.navigation.AppRoutes

@Composable
fun ScanDevices(
    navController: NavController,
    viewModel: ScanDevicesBleViewModel = hiltViewModel()
) {
    val devices by viewModel.devices.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.startScan()
    }

    LaunchedEffect(devices.size) { }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Discovered Devices", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(8.dp))

        if (devices.isEmpty()) {
            Text("Scanning... no devices found yet.")
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(devices.size) { index ->
                    viewModel.setDeviceName(devices[index])
                    var deviceName = viewModel.getDeviceName()
                    DeviceItem(devices[index], deviceName, navController)
                }
            }
        }
    }
}

@Composable
fun DeviceItem(bean: ScanDeviceBean, deviceName : String, navController: NavController) {
    Card(modifier = Modifier.fillMaxWidth().combinedClickable(
        onClick = {
            navController.navigate(route = AppRoutes.WIFI_INFO){
                launchSingleTop = true
            }
        }
    )) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text("Name: $deviceName")
            Text("MAC: ${bean.mac}")
            Text("Device Type : ${bean.deviceType}")
            Text("ProductId: ${bean.productId}")
        }
    }
}