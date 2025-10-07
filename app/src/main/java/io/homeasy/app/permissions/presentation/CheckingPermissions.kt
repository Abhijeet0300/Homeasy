package io.homeasy.app.permissions.presentation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import io.homeasy.app.core.navigation.AppRoutes
import io.homeasy.app.feature_connection.presentation.ScanDevices
import io.homeasy.app.feature_connection.presentation.viewmodel.ScanDevicesBleViewModel

@Composable
fun CheckingPermissions(
    navController: NavController,
    viewModel : ScanDevicesBleViewModel
) {
    RequestAllPermissions(
        onAllGranted = {
            navController.navigate(route = AppRoutes.SELECT_DEVICES)
//            ScanDevices(
//                viewModel = viewModel,
//                navController = navController
//            )
        },
        onDenied = {
            Text("We need Bluetooth permission to discover your smart devices.")
        },
        onShowRational = {
            Text("Permission denied. Please enable Bluetooth permission from settings.")
        }
    )
}