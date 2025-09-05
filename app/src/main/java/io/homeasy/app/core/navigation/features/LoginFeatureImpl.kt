package io.homeasy.app.core.navigation.features

import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import io.homeasy.app.core.navigation.AppRoutes
import io.homeasy.app.core.navigation.FeatureApi
import io.homeasy.app.feature_login_register.presentation.LoginScreen
import io.homeasy.app.feature_login_register.presentation.LoginViewModel
import io.homeasy.app.feature_login_register.presentation.UserViewModel

class LoginFeatureImpl : FeatureApi {
    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavController,
        viewModelsMap: Map<String, ViewModel>
    ) {
        navGraphBuilder.composable(route = AppRoutes.LOGIN_SCREEN) {
            LoginScreen(
                userViewModel = viewModelsMap["user_view_model"] as UserViewModel,
                loginViewModel = viewModelsMap["login_view_model"] as LoginViewModel,
                toRegisterScreen = {
                    navController.navigate(AppRoutes.REGISTER_SCREEN){
                        launchSingleTop = true
                    }
                },
                toHomeScreen = {
                    navController.navigate(AppRoutes.HOME_SCREEN) {
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}