package io.homeasy.app.core.navigation

import android.app.Activity
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import io.homeasy.app.core.navigation.features.AddHomeFeatureImpl
import io.homeasy.app.core.navigation.features.HomeScreenFeatureImpl
import io.homeasy.app.core.navigation.features.LoginFeatureImpl
import io.homeasy.app.core.navigation.features.OtpFeatureImpl
import io.homeasy.app.core.navigation.features.RegisterFeatureImpl
import io.homeasy.app.core.navigation.features.UserDetailsFeatureImpl
import io.homeasy.app.core.navigation.features.UserHomeFeatureImpl
import io.homeasy.app.core.utils.ui.theme.White
import io.homeasy.app.core.utils.ui_components.HomeScreenAppBar
import io.homeasy.app.feature_connection.presentation.viewmodel.ScanDevicesBleViewModel
import io.homeasy.app.feature_home.presentation.viewmodel.HomeViewModel
import io.homeasy.app.feature_home.presentation.viewmodel.UserHomeViewModel
import io.homeasy.app.feature_login_register.presentation.LoginViewModel
import io.homeasy.app.feature_login_register.presentation.RegisterViewModel
import io.homeasy.app.feature_login_register.presentation.UserViewModel
import io.homeasy.app.R
import io.homeasy.app.core.navigation.features.CheckingPermissionsFeatureImpl
import io.homeasy.app.core.navigation.features.RoomFeatureImpl
import io.homeasy.app.core.navigation.features.ScanDevicesBleFeatureImpl
import io.homeasy.app.core.navigation.features.WifiInfoScreenFeatureImpl
import io.homeasy.app.feature_connection.presentation.viewmodel.EZConnectViewModel
import io.homeasy.app.feature_room.presentation.RoomViewModel

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
    val activity = LocalActivity.current as Activity
    val userViewModel : UserViewModel = hiltViewModel(activity as ViewModelStoreOwner)
    val registerViewModel : RegisterViewModel = hiltViewModel(activity as ViewModelStoreOwner)
    val loginViewModel : LoginViewModel = hiltViewModel(activity as ViewModelStoreOwner)
    val homeViewModel : HomeViewModel = hiltViewModel(activity as ViewModelStoreOwner)
    val userHomeViewModel : UserHomeViewModel = hiltViewModel(activity as ViewModelStoreOwner)
    val scanDevicesBleViewModel : ScanDevicesBleViewModel = hiltViewModel(activity as ViewModelStoreOwner)
    val ezConnectViewModel : EZConnectViewModel = hiltViewModel(activity as ViewModelStoreOwner)
    val roomViewModel : RoomViewModel = hiltViewModel(activity as ViewModelStoreOwner)


    val viewModelsMap = mapOf<String, ViewModel>(
        "user_view_model" to userViewModel,
        "register_view_model" to registerViewModel,
        "login_view_model" to loginViewModel,
        "home_view_model" to homeViewModel,
        "user_home_view_model" to userHomeViewModel,
        stringResource(id = R.string.scan_devices_ble_view_model) to scanDevicesBleViewModel,
        stringResource(id = R.string.ez_connect_view_model) to ezConnectViewModel,
        stringResource(id = R.string.room_view_model) to roomViewModel
    )

    val featureApis : List<FeatureApi> = listOf(
        LoginFeatureImpl(),
        RegisterFeatureImpl(),
        OtpFeatureImpl(),
        UserDetailsFeatureImpl(),
        HomeScreenFeatureImpl(),
        AddHomeFeatureImpl(),
        UserHomeFeatureImpl(),
        ScanDevicesBleFeatureImpl(),
        CheckingPermissionsFeatureImpl(),
        WifiInfoScreenFeatureImpl(),
        RoomFeatureImpl()
    )



    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = White,
        topBar = {
            when(navController.currentBackStackEntryAsState().value?.destination?.route) {
                "home" -> HomeScreenAppBar(
                    userViewModel = viewModelsMap["user_view_model"] as UserViewModel
                )

                "user_home" -> HomeScreenAppBar(
                    userViewModel = viewModelsMap["user_view_model"] as UserViewModel
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(route = "add_home") {
                        launchSingleTop = true
                    }
                }
            ) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = null)
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