package io.homeasy.app.core.navigation.features

import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import io.homeasy.app.core.navigation.AppRoutes
import io.homeasy.app.core.navigation.FeatureApi
import io.homeasy.app.feature_connection.presentation.SelectDeviceType
import io.homeasy.app.R
import io.homeasy.app.feature_connection.presentation.viewmodel.PairingDeviceTypeViewModel

class SelectDeviceTypeFeatureImpl : FeatureApi {
    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavController,
        viewModelsMap: Map<String, ViewModel>
    ) {
        navGraphBuilder.composable(route = AppRoutes.SELECT_DEVICES) {
            SelectDeviceType(
                navController = navController,
                pairingDeviceTypeViewModel = viewModelsMap[stringResource(id = R.string.pairing_device_type_view_model)] as PairingDeviceTypeViewModel
            )
        }
    }

}