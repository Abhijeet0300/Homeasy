package io.homeasy.app.core.navigation.features

import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import io.homeasy.app.core.navigation.AppRoutes
import io.homeasy.app.core.navigation.FeatureApi
import io.homeasy.app.feature_login_register.presentation.OtpScreen
import io.homeasy.app.feature_login_register.presentation.RegisterViewModel
import io.homeasy.app.feature_login_register.presentation.UserViewModel

class OtpFeatureImpl : FeatureApi {
    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavController,
        viewModelsMap: Map<String, ViewModel>
    ) {
        navGraphBuilder.composable(route = AppRoutes.OTP_SCREEN) {
            OtpScreen(
                navController = navController,
                registerViewModel = viewModelsMap["register_view_model"] as RegisterViewModel,
                userViewModel = viewModelsMap["user_view_model"] as UserViewModel,
                toUserDetailsScreen = {
                    navController.navigate(AppRoutes.USER_DETAILS_SCREEN) {
                        launchSingleTop = true
                    }
                }
            )
        }
    }

}