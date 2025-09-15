package io.homeasy.app.core.navigation.features

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import io.homeasy.app.core.navigation.AppRoutes
import io.homeasy.app.core.navigation.FeatureApi
import io.homeasy.app.feature_home.presentation.viewmodel.HomeViewModel
import io.homeasy.app.feature_home.presentation.UserHome
import io.homeasy.app.feature_home.presentation.viewmodel.UserHomeViewModel

class UserHomeFeatureImpl : FeatureApi {
    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavController,
        viewModelsMap: Map<String, ViewModel>
    ) {
        navGraphBuilder.composable(route = AppRoutes.USER_HOME) {
            UserHome(
                navController = navController,
                homeViewModel = viewModelsMap["home_view_model"] as HomeViewModel,
                userHomeViewModel = viewModelsMap["user_home_view_model"] as UserHomeViewModel,
                onBackPressed = {
                    BackHandler {
                        val homeViewModel = viewModelsMap["home_view_model"] as HomeViewModel
                        homeViewModel.setSelectedHome(homeBean = null)
                        Log.i("User home", "Back pressed ${homeViewModel.selectedHome}")
                    }
                })
        }
    }

}