package io.homeasy.app.core.navigation.features

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import io.homeasy.app.core.navigation.AppRoutes
import io.homeasy.app.core.navigation.FeatureApi
import io.homeasy.app.feature_login.presentation.UserDetails

class UserDetailsFeatureImpl : FeatureApi {
    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavController
    ) {
       navGraphBuilder.composable(route = AppRoutes.USER_DETAILS_SCREEN) {
           UserDetails(
               navController = navController,
               toHomeScreen = {
                   navController.navigate(route = AppRoutes.HOME_SCREEN) {
                       popUpTo(route = AppRoutes.USER_DETAILS_SCREEN) {
                           inclusive = false
                       }
                       launchSingleTop = true
                       restoreState = true
                   }
               }
           )
       }
    }
}