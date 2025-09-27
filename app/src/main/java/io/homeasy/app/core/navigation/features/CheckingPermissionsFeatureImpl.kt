package io.homeasy.app.core.navigation.features

import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import io.homeasy.app.core.navigation.AppRoutes
import io.homeasy.app.core.navigation.FeatureApi
import io.homeasy.app.permissions.presentation.CheckingPermissions
import io.homeasy.app.R
import io.homeasy.app.feature_connection.presentation.viewmodel.ScanDevicesBleViewModel

class CheckingPermissionsFeatureImpl : FeatureApi {
    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavController,
        viewModelsMap: Map<String, ViewModel>
    ) {
        navGraphBuilder.composable(route = AppRoutes.CHECKING_PERMISSIONS) {
            CheckingPermissions(
                navController = navController,
                viewModel = viewModelsMap[stringResource(id=R.string.scan_devices_ble_view_model)] as ScanDevicesBleViewModel
            )
        }
    }
}