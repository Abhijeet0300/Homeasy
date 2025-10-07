package io.homeasy.app.core.navigation.features

import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import io.homeasy.app.core.navigation.AppRoutes
import io.homeasy.app.core.navigation.FeatureApi
import io.homeasy.app.feature_connection.presentation.ui.camera.CameraPairingScreen
import io.homeasy.app.R
import io.homeasy.app.feature_connection.presentation.viewmodel.WifiInfoViewModel
import io.homeasy.app.feature_connection.presentation.viewmodel.camera.CameraPairingViewModel
import io.homeasy.app.feature_home.presentation.viewmodel.HomeViewModel
import io.homeasy.app.feature_room.presentation.RoomViewModel

class CameraPairingScreenFeatureImpl : FeatureApi {
    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavController,
        viewModelsMap: Map<String, ViewModel>
    ) {
        navGraphBuilder.composable(route = AppRoutes.CAMERA_PAIRING) {
            CameraPairingScreen(
                navController = navController,
                cameraPairingViewModel = viewModelsMap[stringResource(id = R.string.camera_pairing_view_model)] as CameraPairingViewModel,
                wifiInfoViewModel = viewModelsMap[stringResource(id = R.string.wifi_info_view_model)] as WifiInfoViewModel,
                homeViewModel = viewModelsMap["home_view_model"] as HomeViewModel,
                roomViewModel = viewModelsMap[stringResource(id = R.string.room_view_model)] as RoomViewModel
            )
        }
    }
}