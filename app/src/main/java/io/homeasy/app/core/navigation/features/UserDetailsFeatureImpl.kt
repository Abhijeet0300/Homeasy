package io.homeasy.app.core.navigation.features

import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import io.homeasy.app.core.navigation.AppRoutes
import io.homeasy.app.core.navigation.FeatureApi
import io.homeasy.app.feature_login_register.presentation.UserDetails
import io.homeasy.app.feature_login_register.presentation.UserViewModel

class UserDetailsFeatureImpl : FeatureApi {
    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavController,
        viewModelsMap: Map<String, ViewModel>
    ) {
       navGraphBuilder.composable(route = AppRoutes.USER_DETAILS_SCREEN) {
           UserDetails(
               navController = navController,
               userViewModel = viewModelsMap["user_view_model"] as UserViewModel,
               toHomeScreen = {
                   navController.navigate(route = AppRoutes.HOME_SCREEN) {
                       popUpTo(route = AppRoutes.USER_DETAILS_SCREEN) {
                           inclusive = true
                       }
                       launchSingleTop = true
                       restoreState = true
                   }
               },
           )
       }
    }
}