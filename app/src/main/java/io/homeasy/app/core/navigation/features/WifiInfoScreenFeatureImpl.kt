package io.homeasy.app.core.navigation.features

import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import io.homeasy.app.core.navigation.AppRoutes
import io.homeasy.app.core.navigation.FeatureApi
import io.homeasy.app.feature_connection.presentation.WifiInfoScreen
import io.homeasy.app.R
import io.homeasy.app.feature_connection.presentation.viewmodel.EZConnectViewModel
import io.homeasy.app.feature_connection.presentation.viewmodel.PairingDeviceTypeViewModel
import io.homeasy.app.feature_connection.presentation.viewmodel.WifiInfoViewModel
import io.homeasy.app.feature_home.presentation.viewmodel.HomeViewModel
import io.homeasy.app.feature_room.presentation.RoomViewModel

class WifiInfoScreenFeatureImpl : FeatureApi {
    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavController,
        viewModelsMap: Map<String, ViewModel>
    ) {
        navGraphBuilder.composable(route = AppRoutes.WIFI_INFO) {
            WifiInfoScreen(
                navController = navController,
                ezConnectViewModel = viewModelsMap[stringResource(id= R.string.ez_connect_view_model)] as EZConnectViewModel,
                homeViewModel = viewModelsMap["home_view_model"] as HomeViewModel,
                roomViewModel = viewModelsMap[stringResource(R.string.room_view_model)] as RoomViewModel,
                wifiInfoViewModel = viewModelsMap[stringResource(id = R.string.wifi_info_view_model)] as WifiInfoViewModel,
                pairingDeviceTypeViewModel = viewModelsMap[stringResource(id = R.string.pairing_device_type_view_model)] as PairingDeviceTypeViewModel
            )
        }
    }
}