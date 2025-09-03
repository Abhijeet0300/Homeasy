package io.homeasy.app.core.navigation.features

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import io.homeasy.app.core.navigation.AppRoutes
import io.homeasy.app.core.navigation.FeatureApi
import io.homeasy.app.feature_login.presentation.OtpScreen

class OtpFeatureImpl : FeatureApi {
    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavController
    ) {
        navGraphBuilder.composable(route = AppRoutes.OTP_SCREEN) {
            OtpScreen(
                navController = navController,
                onOtpVerified = {
                    navController.navigate(AppRoutes.USER_DETAILS_SCREEN) {
                        launchSingleTop = true
                    }
                }
            )
        }
    }

}