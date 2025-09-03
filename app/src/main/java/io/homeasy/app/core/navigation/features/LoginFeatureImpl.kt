package io.homeasy.app.core.navigation.features

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import io.homeasy.app.core.navigation.AppRoutes
import io.homeasy.app.core.navigation.FeatureApi
import io.homeasy.app.feature_login.presentation.LoginScreen

class LoginFeatureImpl : FeatureApi {
    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavController
    ) {
        navGraphBuilder.composable(route = AppRoutes.LOGIN_SCREEN) {
            LoginScreen(
                toRegisterScreen = {
                    navController.navigate(AppRoutes.REGISTER_SCREEN){
                        launchSingleTop = true
                    }
                },
                toHomeScreen = {
                    navController.navigate(AppRoutes.HOME_SCREEN) {
                        popUpTo(AppRoutes.LOGIN_SCREEN) {
                            inclusive = true
                        }
                    }
                }
            )
        }
    }
}