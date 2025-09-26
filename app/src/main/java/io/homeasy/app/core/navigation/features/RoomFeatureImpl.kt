package io.homeasy.app.core.navigation.features

import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import io.homeasy.app.core.navigation.AppRoutes
import io.homeasy.app.core.navigation.FeatureApi
import io.homeasy.app.feature_home.presentation.viewmodel.HomeViewModel
import io.homeasy.app.feature_room.presentation.Room
import io.homeasy.app.feature_room.presentation.RoomViewModel
import io.homeasy.app.R

class RoomFeatureImpl : FeatureApi {
    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavController,
        viewModelsMap: Map<String, ViewModel>
    ) {
        navGraphBuilder.composable(route = AppRoutes.ROOM) {
            Room(
                navController = navController,
                roomViewModel = viewModelsMap[stringResource(id = R.string.room_view_model)] as RoomViewModel,
                homeViewModel = viewModelsMap["home_view_model"] as HomeViewModel
            )
        }
    }
}