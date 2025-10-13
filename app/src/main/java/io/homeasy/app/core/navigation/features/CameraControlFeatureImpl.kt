package io.homeasy.app.core.navigation.features

import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import io.homeasy.app.R
import io.homeasy.app.core.navigation.AppRoutes
import io.homeasy.app.core.navigation.FeatureApi
import io.homeasy.app.feature_device_control.camera.presentation.CameraControl
import io.homeasy.app.feature_device_control.camera.presentation.viewmodel.CameraViewModel
import io.homeasy.app.feature_room.presentation.RoomViewModel

class CameraControlFeatureImpl : FeatureApi {
    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavController,
        viewModelsMap: Map<String, ViewModel>
    ) {
        navGraphBuilder.composable(route = AppRoutes.CAMERA_CONTROL) {
            CameraControl(
                navController = navController,
                cameraViewModel = viewModelsMap[stringResource(id = R.string.camera_view_model)] as CameraViewModel,
                roomViewModel = viewModelsMap[stringResource(id = R.string.room_view_model)] as RoomViewModel
            )
        }
    }
}