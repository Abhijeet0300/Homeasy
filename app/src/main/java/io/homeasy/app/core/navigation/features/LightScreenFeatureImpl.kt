package io.homeasy.app.core.navigation.features

import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import io.homeasy.app.core.navigation.AppRoutes
import io.homeasy.app.core.navigation.FeatureApi
import io.homeasy.app.feature_device_control.presentation.LightScreen
import io.homeasy.app.feature_device_control.presentation.viewmodel.LightScreenViewModel

class LightScreenFeatureImpl : FeatureApi {
    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavController,
        viewModelsMap: Map<String, ViewModel>
    ) {
        navGraphBuilder.composable(route = AppRoutes.LIGHT_SCREEN) {
            LightScreen(viewModel = viewModelsMap["light_screen_view_model"] as LightScreenViewModel)
        }
    }

}