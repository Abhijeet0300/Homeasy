package io.homeasy.app.feature_home.presentation

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.unit.dp


@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    comingFrom : String = "",
    navController : NavController,
    userViewModel: UserViewModel,
    toUserDetailsScreen : () -> Unit,
    homeViewModel: HomeViewModel
) {
    val currentUser by userViewModel.currentUser.collectAsState()
    val homeList by homeViewModel.homeList.collectAsState()

    LaunchedEffect(Unit) {
        homeViewModel.queryHomeList()
    }
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(homeList?.size ?: 0) { index ->
            Text(
                modifier = Modifier.combinedClickable(
                    onLongClick = {},
                    onClick = {
                        homeViewModel.setSelectedHome(homeList?.get(index)!!)
                        navController.navigate(route = AppRoutes.USER_HOME) {
                            launchSingleTop = true
                        }
                    }
                ),
                text = homeList?.get(index)?.name.toString()
            )
        }
    }
}