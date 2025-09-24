package io.homeasy.app.permissions.presentation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import io.homeasy.app.feature_devices.presentation.ScanDevices
import io.homeasy.app.feature_devices.presentation.viewmodel.ScanDevicesBleViewModel

@Composable
fun CheckingPermissions(
    navController: NavController,
    viewModel : ScanDevicesBleViewModel
) {
    RequestAllPermissions(
        onAllGranted = {
            ScanDevices(
                viewModel = viewModel,
                navController = navController
            )
        },
        onDenied = {
            Text("We need Bluetooth permission to discover your smart devices.")
        },
        onShowRational = {
            Text("Permission denied. Please enable Bluetooth permission from settings.")
        }
    )
}