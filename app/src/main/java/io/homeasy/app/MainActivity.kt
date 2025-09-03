package io.homeasy.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import dagger.hilt.android.AndroidEntryPoint
import io.homeasy.app.core.navigation.AppNavigation
import io.homeasy.app.core.utils.ui.theme.AppTheme
import io.homeasy.app.core.utils.ui.theme.White
import io.homeasy.app.core.utils.ui_components.HomeasyAppBar
import io.homeasy.app.feature_login.presentation.LoginScreen
import io.homeasy.app.feature_login.presentation.OtpScreen
import io.homeasy.app.feature_login.presentation.RegisterScreen
import io.homeasy.feature_home.presentation.HomeScreen

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = White,
                    topBar = { HomeasyAppBar() }
                ) { innerPadding ->
                    HomeScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}
