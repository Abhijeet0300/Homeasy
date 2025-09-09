package io.homeasy.app.core.navigation.features

import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import io.homeasy.app.core.navigation.AppRoutes
import io.homeasy.app.core.navigation.FeatureApi
import io.homeasy.app.feature_home.presentation.HomeViewModel
import io.homeasy.app.feature_home.presentation.UserHome

class UserHomeFeatureImpl : FeatureApi {
    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavController,
        viewModelsMap: Map<String, ViewModel>
    ) {
        navGraphBuilder.composable(route = AppRoutes.USER_HOME) {
            UserHome(
                navController = navController,
                homeViewModel = viewModelsMap["home_view_model"] as HomeViewModel
            )
        }
    }

}