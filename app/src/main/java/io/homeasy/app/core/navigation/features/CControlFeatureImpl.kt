package io.homeasy.app.core.navigation.features

import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import io.homeasy.app.R
import io.homeasy.app.core.navigation.AppRoutes
import io.homeasy.app.core.navigation.FeatureApi
import io.homeasy.app.feature_device_control.camera_new.presentation.CControl
import io.homeasy.app.feature_device_control.camera_new.presentation.viewmodel.CameraControlViewModel
import io.homeasy.app.feature_home.presentation.viewmodel.HomeViewModel
import io.homeasy.app.feature_room.presentation.RoomViewModel

class CControlFeatureImpl : FeatureApi {
    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavController,
        viewModelsMap: Map<String, ViewModel>
    ) {
        navGraphBuilder.composable(route = AppRoutes.C_CONTROL) {
            CControl(
                homeViewModel = viewModelsMap["home_view_model"] as HomeViewModel,
                roomViewModel = viewModelsMap[stringResource(id = R.string.room_view_model)] as RoomViewModel,
                cameraControlViewModel = viewModelsMap["camera_control_view_model"] as CameraControlViewModel
            )
        }
    }

}