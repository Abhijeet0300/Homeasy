package io.homeasy.app.feature_connection.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import io.homeasy.app.core.navigation.AppRoutes
import io.homeasy.app.core.utils.ui_components.RegularButton
import io.homeasy.app.feature_connection.domain.model.DeviceType
import io.homeasy.app.feature_connection.presentation.viewmodel.PairingDeviceTypeViewModel

@Composable
fun SelectDeviceType(
    navController: NavController,
    pairingDeviceTypeViewModel : PairingDeviceTypeViewModel
) {
    Column(
        modifier = Modifier.fillMaxSize() ,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        RegularButton(
            label = "Light",
            onClick = {
                pairingDeviceTypeViewModel.setSelectedDeviceType(deviceType = DeviceType.LIGHT)
                navController.navigate(route = AppRoutes.SCAN_DEVICES)
            }
        )

        RegularButton(
            label = "Smart Camera",
            onClick = {
                pairingDeviceTypeViewModel.setSelectedDeviceType(deviceType = DeviceType.CAMERA)
                navController.navigate(route = AppRoutes.WIFI_INFO)
            }
        )
    }
}