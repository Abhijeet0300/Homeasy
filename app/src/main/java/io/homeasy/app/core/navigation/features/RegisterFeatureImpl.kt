package io.homeasy.app.core.navigation.features

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import io.homeasy.app.core.navigation.AppRoutes
import io.homeasy.app.core.navigation.FeatureApi
import io.homeasy.app.feature_login.presentation.RegisterScreen

class RegisterFeatureImpl : FeatureApi {
    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavController,
    ) {
        navGraphBuilder.composable(route = AppRoutes.REGISTER_SCREEN) {
            RegisterScreen(
                navController = navController,
                toOtpScreen =  {
                    navController.navigate(route = AppRoutes.OTP_SCREEN) {
                        launchSingleTop = true
                    }
                }
            )
        }
    }

}