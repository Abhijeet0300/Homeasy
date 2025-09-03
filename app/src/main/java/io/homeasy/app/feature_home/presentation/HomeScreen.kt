package io.homeasy.app.feature_home.presentation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import io.homeasy.app.core.navigation.AppRoutes
import io.homeasy.app.feature_login.presentation.UserViewModel
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Preview(showBackground = true)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
//    navController: NavController,
//    userViewModel: UserViewModel = hiltViewModel(navController.getBackStackEntry(AppRoutes.USER_DETAILS_SCREEN))
) {
//    val currentUser by userViewModel.currentUser.collectAsState()
//    Text(
//        text = currentUser?.username.toString()
//    )
}