package io.homeasy.app.core.navigation

import android.app.Activity
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import io.homeasy.app.core.navigation.features.HomeScreenFeatureImpl
import io.homeasy.app.core.navigation.features.LoginFeatureImpl
import io.homeasy.app.core.navigation.features.OtpFeatureImpl
import io.homeasy.app.core.navigation.features.RegisterFeatureImpl
import io.homeasy.app.core.navigation.features.UserDetailsFeatureImpl
import io.homeasy.app.core.utils.ui.theme.White
import io.homeasy.app.core.utils.ui_components.HomeScreenAppBar
import io.homeasy.app.feature_login_register.presentation.LoginViewModel
import io.homeasy.app.feature_login_register.presentation.RegisterViewModel
import io.homeasy.app.feature_login_register.presentation.UserViewModel

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
    val activity = LocalActivity.current as Activity
    val userViewModel : UserViewModel = hiltViewModel(activity as ViewModelStoreOwner)
    val registerViewModel : RegisterViewModel = hiltViewModel(activity as ViewModelStoreOwner)
    val loginViewModel : LoginViewModel = hiltViewModel(activity as ViewModelStoreOwner)

    val viewModelsMap = mapOf<String, ViewModel>(
        "user_view_model" to userViewModel,
        "register_view_model" to registerViewModel,
        "login_view_model" to loginViewModel
    )

    val featureApis : List<FeatureApi> = listOf(
        LoginFeatureImpl(),
        RegisterFeatureImpl(),
        OtpFeatureImpl(),
        UserDetailsFeatureImpl(),
        HomeScreenFeatureImpl()
    )



    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = White,
        topBar = {
            when(navController.currentBackStackEntryAsState().value?.destination?.route) {
                "home" -> HomeScreenAppBar(
                    userViewModel = viewModelsMap["user_view_model"] as UserViewModel
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            modifier = modifier.padding(innerPadding),
            navController = navController,
            startDestination = AppRoutes.LOGIN_SCREEN
        ) {
            featureApis.forEach { featureApi ->
                featureApi.registerGraph(this, navController, viewModelsMap)
            }
        }
    }
}