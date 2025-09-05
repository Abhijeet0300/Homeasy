package io.homeasy.app.core.navigation.features

import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import io.homeasy.app.core.navigation.AppRoutes
import io.homeasy.app.core.navigation.FeatureApi
import io.homeasy.app.feature_login_register.presentation.RegisterScreen
import io.homeasy.app.feature_login_register.presentation.RegisterViewModel

class RegisterFeatureImpl : FeatureApi {
    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavController,
        viewModelsMap: Map<String, ViewModel>
    ) {
        navGraphBuilder.composable(route = AppRoutes.REGISTER_SCREEN) {
            RegisterScreen(
                navController = navController,
                registerViewModel = viewModelsMap["register_view_model"] as RegisterViewModel,
                toOtpScreen =  {
                    navController.navigate(route = AppRoutes.OTP_SCREEN) {
                        launchSingleTop = true
                    }
                }
            )
        }
    }

}