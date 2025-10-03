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

@Composable
fun SelectDeviceType(
    navController: NavController
) {
    Column(
        modifier = Modifier.fillMaxSize() ,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        RegularButton(
            label = "Light",
            onClick = {
                navController.navigate(route = AppRoutes.SCAN_DEVICES)
            }
        )

        RegularButton(
            label = "Smart Camera",
            onClick = {
                navController.navigate(route = AppRoutes.SCAN_DEVICES)
            }
        )
    }
}