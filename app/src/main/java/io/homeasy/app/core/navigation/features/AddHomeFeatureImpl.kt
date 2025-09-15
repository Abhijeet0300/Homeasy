package io.homeasy.app.core.navigation.features

import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import io.homeasy.app.core.navigation.AppRoutes
import io.homeasy.app.core.navigation.FeatureApi
import io.homeasy.app.feature_home.presentation.AddHome
import io.homeasy.app.feature_home.presentation.viewmodel.HomeViewModel

class AddHomeFeatureImpl : FeatureApi {
    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavController,
        viewModelsMap: Map<String, ViewModel>
    ) {
        navGraphBuilder.composable(route = AppRoutes.ADD_HOME) {
            AddHome(
                navController = navController,
                homeViewModel = viewModelsMap["home_view_model"] as HomeViewModel
            )
        }
    }

}