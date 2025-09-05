package io.homeasy.app.feature_home.presentation

import android.util.Log
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import io.homeasy.app.core.navigation.AppRoutes
import io.homeasy.app.feature_login_register.presentation.UserViewModel
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue


@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    comingFrom : String = "",
    navController : NavController,
    userViewModel: UserViewModel,
    toUserDetailsScreen : () -> Unit
) {
    val currentUser by userViewModel.currentUser.collectAsState()
//    LaunchedEffect(Unit) {
//        if(currentUser?.username == currentUser?.email) {
//            Log.i("UserDetails", "Username: ${currentUser?.username} Email: ${currentUser?.email}")
//            toUserDetailsScreen()
//        }
//    }
    Text(
        text = currentUser?.username.toString()
    )
}