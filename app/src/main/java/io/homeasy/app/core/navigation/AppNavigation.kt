package io.homeasy.app.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.homeasy.app.core.navigation.features.HomeScreenFeatureImpl
import io.homeasy.app.core.navigation.features.LoginFeatureImpl
import io.homeasy.app.core.navigation.features.OtpFeatureImpl
import io.homeasy.app.core.navigation.features.RegisterFeatureImpl
import io.homeasy.app.core.navigation.features.UserDetailsFeatureImpl
import io.homeasy.app.feature_login.presentation.LoginScreen
import io.homeasy.app.feature_login.presentation.OtpScreen
import io.homeasy.app.feature_login.presentation.RegisterScreen

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()

    val featureApis : List<FeatureApi> = listOf(
        LoginFeatureImpl(),
        RegisterFeatureImpl(),
        OtpFeatureImpl(),
        UserDetailsFeatureImpl(),
        HomeScreenFeatureImpl()
    )

    NavHost(
        navController = navController,
        startDestination = AppRoutes.LOGIN_SCREEN
    ) {
        featureApis.forEach { featureApi ->
            featureApi.registerGraph(this, navController)
        }

//        composable(route = AppRoutes.LOGIN_SCREEN) {
//            LoginScreen()
//        }
//
//        composable(route = AppRoutes.REGISTER_SCREEN) {
//            RegisterScreen()
//        }
//
//        composable(route = AppRoutes.OTP_SCREEN) {
//            OtpScreen()
//        }
    }
}